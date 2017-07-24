package com.hb.ex01.a0628_xmlpullparser;

/**
 * Created by HB04-03 on 2017-06-28.
 */

public class XmlVO {
    private String local,desc,ta,icon;
    public XmlVO() {}
    public XmlVO(String local, String desc, String ta, String icon) {
        this.local = local;
        this.desc = desc;
        this.ta = ta;
        this.icon = icon;
    }
    public String getLocal() {
        return local;
    }
    public void setLocal(String local) {
        this.local = local;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getTa() {
        return ta;
    }
    public void setTa(String ta) {
        this.ta = ta;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
}
