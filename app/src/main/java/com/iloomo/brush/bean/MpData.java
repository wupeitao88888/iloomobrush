package com.iloomo.brush.bean;

import java.io.Serializable;

/**
 * Created by wupeitao on 16/3/17.
 */
public class MpData implements Serializable {
    private String mp_id;
    private String m_products;
    private String screen_width;
    private String screen_hight;
    private String m_apparatus;
    private String rom_version;
    private String mac_address;
    private String simoperatorname;
    private String simoperator;

    public String getSimoperatorname() {
        return simoperatorname;
    }

    public void setSimoperatorname(String simoperatorname) {
        this.simoperatorname = simoperatorname;
    }

    public String getSimoperator() {
        return simoperator;
    }

    public void setSimoperator(String simoperator) {
        this.simoperator = simoperator;
    }

    public String getMp_id() {
        return mp_id;
    }

    public void setMp_id(String mp_id) {
        this.mp_id = mp_id;
    }

    public String getM_products() {
        return m_products;
    }

    public void setM_products(String m_products) {
        this.m_products = m_products;
    }

    public String getScreen_width() {
        return screen_width;
    }

    public void setScreen_width(String screen_width) {
        this.screen_width = screen_width;
    }

    public String getScreen_hight() {
        return screen_hight;
    }

    public void setScreen_hight(String screen_hight) {
        this.screen_hight = screen_hight;
    }

    public String getM_apparatus() {
        return m_apparatus;
    }

    public void setM_apparatus(String m_apparatus) {
        this.m_apparatus = m_apparatus;
    }

    public String getRom_version() {
        return rom_version;
    }

    public void setRom_version(String rom_version) {
        this.rom_version = rom_version;
    }

    public String getMac_address() {
        return mac_address;
    }

    public void setMac_address(String mac_address) {
        this.mac_address = mac_address;
    }
}
