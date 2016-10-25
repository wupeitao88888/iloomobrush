package com.iloomo.brush.bean;

import java.io.Serializable;

/**
 * Created by wupeitao on 16/3/17.
 */
public class PackageData implements Serializable {
    private String package_name;
    private String timeonpage;
    private String task_id;
    private String countlist;
    private String type;
    private String down_url;

    public String getDown_url() {
        return down_url;
    }

    public void setDown_url(String down_url) {
        this.down_url = down_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getTimeonpage() {
        return timeonpage;
    }

    public void setTimeonpage(String timeonpage) {
        this.timeonpage = timeonpage;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getCountlist() {
        return countlist;
    }

    public void setCountlist(String countlist) {
        this.countlist = countlist;
    }
}
