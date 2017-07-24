package com.hb.ex01.a0622_dbserver2;

/**
 * Created by HB04-03 on 2017-06-22.
 */

public class ListVO {
    private String idx,id,pwd,name,age,addr;

    public ListVO(String idx, String id, String pwd, String name, String age, String addr) {
        this.idx = idx;
        this.id = id;
        this.pwd = pwd;
        this.name = name;
        this.age = age;
        this.addr = addr;
    }
    public String getIdx() {
        return idx;
    }
    public void setIdx(String idx) {
        this.idx = idx;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPwd() {
        return pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public String getAddr() {
        return addr;
    }
    public void setAddr(String addr) {
        this.addr = addr;
    }
}
