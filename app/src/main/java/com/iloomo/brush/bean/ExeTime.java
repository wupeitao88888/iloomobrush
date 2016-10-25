package com.iloomo.brush.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wupeitao on 16/3/17.
 */
public class ExeTime implements Serializable {
    private String exec_id;
    private String device_id;
    private String exec_date;
    private String exec_time;
    private String pnumber;
    private String networktype;
    private String type;
    private List<MpData> mpdata;
    private List<PackageData> packagedata;
    private List<VpnData> vpndata;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExec_id() {
        return exec_id;
    }

    public void setExec_id(String exec_id) {
        this.exec_id = exec_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getExec_date() {
        return exec_date;
    }

    public void setExec_date(String exec_date) {
        this.exec_date = exec_date;
    }

    public String getExec_time() {
        return exec_time;
    }

    public void setExec_time(String exec_time) {
        this.exec_time = exec_time;
    }

    public String getPnumber() {
        return pnumber;
    }

    public void setPnumber(String pnumber) {
        this.pnumber = pnumber;
    }

    public String getNetworktype() {
        return networktype;
    }

    public void setNetworktype(String networktype) {
        this.networktype = networktype;
    }

    public List<MpData> getMpdata() {
        return mpdata;
    }

    public void setMpdata(List<MpData> mpdata) {
        this.mpdata = mpdata;
    }

    public List<PackageData> getPackagedata() {
        return packagedata;
    }

    public void setPackagedata(List<PackageData> packagedata) {
        this.packagedata = packagedata;
    }

    public List<VpnData> getVpndata() {
        return vpndata;
    }

    public void setVpndata(List<VpnData> vpndata) {
        this.vpndata = vpndata;
    }
}
