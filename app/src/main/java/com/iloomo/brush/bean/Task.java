package com.iloomo.brush.bean;

import com.iloomo.bean.BaseModel;

import java.util.List;

/**
 * Created by wupeitao on 16/3/22.
 */
public class Task extends BaseModel {
    private List<ExeTime> list;
    private String servertime;
    private String appversion;
    private String appurl;

    public String getAppversion() {
        return appversion;
    }

    public void setAppversion(String appversion) {
        this.appversion = appversion;
    }

    public String getAppurl() {
        return appurl;
    }

    public void setAppurl(String appurl) {
        this.appurl = appurl;
    }

    public String getServertime() {
        return servertime;
    }

    public void setServertime(String servertime) {
        this.servertime = servertime;
    }

    public List<ExeTime> getList() {
        return list;
    }

    public void setList(List<ExeTime> list) {
        this.list = list;
    }
}
