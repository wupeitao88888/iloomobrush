/*
 * Copyright 2011 meiyitian
 * Blog  :http://www.cnblogs.com/meiyitian
 * Email :haoqqemail@qq.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iloomo.net;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

import com.alibaba.fastjson.JSON;
import com.iloomo.bean.BaseModel;
import com.iloomo.utils.L;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

/**
 * 
 * 异步HTTPPOST请求
 * 
 * 线程的终止工作交给线程池，当activity停止的时候，设置回调函数为false ，就不会执行回调方法。
 * 
 * @author sailor
 * @param <T>
 * @param <T>
 * 
 */
public class AsyncHttpPost<T> extends BaseRequest {
	private static final long serialVersionUID = 2L;
	DefaultHttpClient httpClient;
	Map<String, Object> parameter = null;
	private int resultCode = -1;
	private Class<T> modelClass;
	Handler resultHandler = new Handler() {
		public void handleMessage(Message msg) {
			String resultData = (String) msg.obj;
			if (!resultData.contains("ERROR.HTTP.008")) {
				ThreadCallBack callBack = (ThreadCallBack) msg.getData()
						.getSerializable("callback");
				L.e("返回值："+resultData);
				Object model=JSON.parseObject(resultData, modelClass);
				BaseModel baseMadel=(BaseModel)model;
				String code=baseMadel.getCode();
				if(!TextUtils.isEmpty(code)){
					if (resultCode == -1){
						callBack.onCallbackFromThreadError(resultData,baseMadel);
					}
					callBack.onCallBackFromThreadError(resultData, resultCode,baseMadel);
					return;
				}
				if (resultCode == -1){
					callBack.onCallbackFromThread(resultData,model);
				}
				callBack.onCallBackFromThread(resultData, resultCode,model);
			}

		}
	};
	ThreadCallBack callBack;

	public AsyncHttpPost(ThreadCallBack callBack, String url,
			Map<String, Object> parameter, int resultCode,Class<T> modelClass) {
		this.callBack = callBack;
		this.resultCode = resultCode;
		this.url = url;
		this.parameter = parameter;
		this.modelClass=modelClass;
		L.e("请求："+url+"?"+sort(parameter));
		if (httpClient == null)
			httpClient = new DefaultHttpClient();
	}
	
    public String sort(Map<String, Object> params) {
    	
    	 List<String> key = new ArrayList<String>();  
    	 Iterator it = params.keySet().iterator();  
         while (it.hasNext()) {  
             String keys = it.next().toString();  
             key.add(keys);   
         } 
        
        Collections.sort(key);
        StringBuilder result = new StringBuilder();
        for (String temp : key) {
            if (key.size() > 0)
                result.append("&");
            result.append(temp);
            result.append("=");
            result.append(params.get(temp));  
        }
        try {
            return result.toString().substring(1, result.toString().length());
        } catch (Exception e) {
            return "";
        }
    }
    

	public AsyncHttpPost(ThreadCallBack callBack, String url,
			Map<String, Object> parameter, int connectTimeout, int readTimeout,Class<T> modelClass) {
		this(callBack, url, parameter, -1,modelClass);
		if (connectTimeout > 0) {
			this.connectTimeout = connectTimeout;
		}
		if (readTimeout > 0) {
			this.readTimeout = readTimeout;
		}
	}

	public AsyncHttpPost(ThreadCallBack callBack, String url,
			Map<String, Object> parameter, String loadingDialogContent,
			boolean isHideCloseBtn, int connectTimeout, int readTimeout,
			int resultCode,Class<T> modelClass) {
		this(callBack, url, parameter, resultCode,modelClass);
		if (connectTimeout > 0) {
			this.connectTimeout = connectTimeout;
		}
		if (readTimeout > 0) {
			this.readTimeout = readTimeout;
		}
	}

	@Override
	public void run() {
		String ret = "";
		try {
			for (int i = 0; i < HttpConstant.CONNECTION_COUNT; i++) {
				try {
					request = new HttpPost(url);
					request.addHeader("Accept-Encoding", "default");

					if (parameter != null && parameter.size() > 0) {
						List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
						Set<String> keys = parameter.keySet();

						for (String key : keys) {
							list.add(new BasicNameValuePair(key, String
									.valueOf(parameter.get(key))));
						}
						((HttpPost) request)
								.setEntity(new UrlEncodedFormEntity(list,
										HTTP.UTF_8));
					}
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT,
							connectTimeout);
					httpClient.getParams().setParameter(
							CoreConnectionPNames.SO_TIMEOUT, readTimeout);
					HttpResponse response = httpClient.execute(request);
					int statusCode = response.getStatusLine().getStatusCode();
					if (statusCode == HttpStatus.SC_OK) {
						InputStream is = response.getEntity().getContent();
						BufferedInputStream bis = new BufferedInputStream(is);
						bis.mark(2);
						// 取前两个字节
						byte[] header = new byte[2];
						int result = bis.read(header);
						// reset输入流到开始位置
						bis.reset();
						// 判断是否是GZIP格式
						int headerData = getShort(header);
						// Gzip 流 的前两个字节是 0x1f8b
						if (result != -1 && headerData == 0x1f8b) {
							// LogUtil.d("HttpTask", " use GZIPInputStream  ");
							is = new GZIPInputStream(bis);
						} else {
							// LogUtil.d("HttpTask",
							// " not use GZIPInputStream");
							is = bis;
						}
						InputStreamReader reader = new InputStreamReader(is,
								"utf-8");
						char[] data = new char[100];
						int readSize;
						StringBuffer sb = new StringBuffer();
						while ((readSize = reader.read(data)) > 0) {
							sb.append(data, 0, readSize);
						}
						ret = sb.toString();
						bis.close();
						reader.close();

					} else {
						RequestException exception = new RequestException(
								RequestException.IO_EXCEPTION, "响应码异常,响应码："
										+ statusCode);
						ret = ErrorUtil
								.errorJson("999", exception.getMessage());
					}

					break;
				} catch (Exception e) {
					if (i == HttpConstant.CONNECTION_COUNT - 1) {
						RequestException exception = new RequestException(
								RequestException.IO_EXCEPTION, "网络连接超时");
						ret = ErrorUtil
								.errorJson("999", exception.getMessage());
					} else {
						Log.d("connection url", "连接超时" + i);
						continue;
					}
				}
			}
		} catch (IllegalArgumentException e) {
			RequestException exception = new RequestException(
					RequestException.IO_EXCEPTION, HttpConstant.ERROR_MESSAGE);
			ret = ErrorUtil.errorJson("999", exception.getMessage());
		} finally {
			if (!HttpConstant.IS_STOP_REQUEST) {
				Message msg = new Message();
				msg.obj = ret;
				msg.getData().putSerializable("callback", callBack);
				resultHandler.sendMessage(msg);
			}
		}
		super.run();
	}

	private int getShort(byte[] data) {
		return (int) ((data[0] << 8) | data[1] & 0xFF);
	}
}
