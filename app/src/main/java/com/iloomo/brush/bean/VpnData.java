package com.iloomo.brush.bean;

import java.io.Serializable;

/**
 * Created by wupeitao on 16/3/17.
 */
public class VpnData implements Serializable {
    private String conip;
    private String vpnname;
    private String vpnpwd;

    public String getConip() {
        return conip;
    }

    public void setConip(String conip) {
        this.conip = conip;
    }

    public String getVpnname() {
        return vpnname;
    }

    public void setVpnname(String vpnname) {
        this.vpnname = vpnname;
    }

    public String getVpnpwd() {
        return vpnpwd;
    }

    public void setVpnpwd(String vpnpwd) {
        this.vpnpwd = vpnpwd;
    }
}
