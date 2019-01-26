package com.warn.entity;

import javax.xml.crypto.Data;

public class WorkerMarker {
    private Integer id;
    private String cx;//坐标
    private String cy;//坐标
    private Data time;//更新时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getcx() {
        return cx;
    }

    public void setcx(String cx) {
        this.cx = cx;
    }

    public String getcy() {
        return cy;
    }

    public void setcy(String cy) {
        this.cy = cy;
    }

    public Data getTime() {
        return time;
    }

    public void setTime(Data time) {
        this.time = time;
    }
}
