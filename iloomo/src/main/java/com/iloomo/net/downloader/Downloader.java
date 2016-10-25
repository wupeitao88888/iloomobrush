package com.iloomo.net.downloader;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.iloomo.bean.Info;
import com.iloomo.db.DbHelperBase;
import com.iloomo.db.DBbase;
import com.iloomo.global.MApplication;
import com.iloomo.threadpool.MyThreadPool;
import com.iloomo.utils.L;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by wupeitao on 16/3/17.
 */
public class Downloader {

    private int done;
    private DBbase dao;
    private int fileLen;
    private Handler handler;
    private boolean isPause;
    public static final int FILELENTH = 100;
    public static final int PROGRESS = 101;
    public static final int ERROR = 102;
    public static final int FINISH = 103;

    public Downloader(Context context, Handler handler) {
        DbHelperBase dbHelperBase = new DbHelperBase(context);
        dao = new DBbase(context, dbHelperBase);
        this.handler = handler;
    }

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        } else {
            return "";
        }
        String replace = MApplication.context.getPackageName();
        String path = sdDir.toString() + "/Android/data/" + replace + "/apk/";
        File dbFolder = new File(path);
        // 目录不存在则自动创建目录
        if (!dbFolder.exists()) {
            dbFolder.mkdirs();
        }
        return path;
    }

    /**
     * 多线程下载
     *
     * @param path    下载路径
     * @param thCount 需要开启多少个线程
     * @throws Exception
     */
    public void download(final String path, final int thCount) {
        MyThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {


                URL url = null;
                try {
                    L.e("路径：" + getSDPath() + ",名字" + path.substring(path.lastIndexOf("/") + 1));
                    File fileapk = new File(getSDPath(), path.substring(path.lastIndexOf("/") + 1));
                    L.e("文件是否存在："+fileapk.exists());
                    if (fileapk.exists()) {
                        Message msg = new Message();
                        msg.what = FINISH;
                        msg.obj = getSDPath() + path.substring(path.lastIndexOf("/") + 1);
                        handler.sendMessage(msg);
                        return;
                    }

                    url = new URL(path);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //设置超时时间
                    conn.setConnectTimeout(5000);
                    if (conn.getResponseCode() == 200) {
                        fileLen = conn.getContentLength();

                        String name = path.substring(path.lastIndexOf("/") + 1).replace(".apk", ".tmp");
                        File file = new File(getSDPath(), name);

                        RandomAccessFile raf = new RandomAccessFile(file, "rws");
                        raf.setLength(fileLen);
                        raf.close();
                        //Handler发送消息，主线程接收消息，获取数据的长度
                        Message msg = new Message();
                        msg.what = FILELENTH;
                        msg.getData().putInt("fileLen", fileLen);
                        handler.sendMessage(msg);

                        //计算每个线程下载的字节数
                        int partLen = (fileLen + thCount - 1) / thCount;
                        for (int i = 0; i < thCount; i++)
                            new DownloadThread(url, file, partLen, i).start();
                    } else {
                        Message msg = new Message();
                        msg.what = ERROR;
                        msg.obj = 404;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//            Message msg = new Message();
//            msg.what = ERROR;
//            msg.obj = e;
//            handler.sendMessage(msg);
                }
            }
        });
    }

    private final class DownloadThread extends Thread {
        private URL url;
        private File file;
        private int partLen;
        private int id;

        public DownloadThread(URL url, File file, int partLen, int id) {
            this.url = url;
            this.file = file;
            this.partLen = partLen;
            this.id = id;
        }

        /**
         * 写入操作
         */
        public void run() {
            // 判断上次是否有未完成任务
            Info info = null;
            try {
                info = dao.query(url.toString(), id);
                if (info != null) {
                    // 如果有, 读取当前线程已下载量
                    done += Integer.parseInt(info.getDone());
                } else {
                    // 如果没有, 则创建一个新记录存入
                    info = new Info();
                    info.setPath(url.toString());
                    info.setThid(id + "");
                    info.setDone(0 + "");
                    dao.insert(info);
                }
            } catch (Exception e) {
                info = new Info();
                info.setPath(url.toString());
                info.setThid(id + "");
                info.setDone(0 + "");
                dao.insert(info);
            }


            int start = id * partLen + Integer.parseInt(info.getDone()); // 开始位置 += 已下载量
            int end = (id + 1) * partLen - 1;

            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(3000);
                //获取指定位置的数据，Range范围如果超出服务器上数据范围, 会以服务器数据末尾为准
                conn.setRequestProperty("Range", "bytes=" + start + "-" + end);
                RandomAccessFile raf = new RandomAccessFile(file, "rws");
                raf.seek(start);
                //开始读写数据
                InputStream in = conn.getInputStream();
                byte[] buf = new byte[1024 * 10];
                int len;
                while ((len = in.read(buf)) != -1) {
                    if (isPause) {
                        //使用线程锁锁定该线程
                        synchronized (dao) {
                            try {
                                dao.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    raf.write(buf, 0, len);
                    done += len;
                    info.setDone(info.getDone() + len);
                    // 记录每个线程已下载的数据量
                    dao.update(info);
                    //新线程中用Handler发送消息，主线程接收消息
                    Message msg = new Message();
                    msg.what = PROGRESS;
                    msg.getData().putInt("done", done);
                    handler.sendMessage(msg);
                }
                in.close();
                raf.close();
                // 删除下载记录
                dao.deleteAll(info.getPath(), fileLen);
                String path = getSDPath() + file.getName().replace(".tmp", ".apk");
                file.renameTo(new File(path));
                Message msg = new Message();
                msg.what = FINISH;
                msg.obj = path;
                handler.sendMessage(msg);
            } catch (IOException e) {
                Message msg = new Message();
                msg.what = ERROR;
                msg.obj = e;
                handler.sendMessage(msg);
            }
        }
    }

    //暂停下载
    public void pause() {
        isPause = true;
    }

    //继续下载
    public void resume() {
        isPause = false;
        //恢复所有线程
        synchronized (dao) {
            dao.notifyAll();
        }
    }
}
