package com.lostcanteen.deliciouscanteen;

import java.io.Serializable;

/**
 * Created by yw199 on 2017/10/21.
 */

public class CanteenDetail implements Serializable {
    private int canteenid;
    private String picture; //图片路径
    private String name; //食堂名
    private String location; //位置
    private String hours; //营业时间
    private boolean specialbook; //预约服务是否开启
    private String[] sbookpattern;
    private int adminid; //管理员id

    public CanteenDetail(){}

    public CanteenDetail(int canteenid, String picture, String name, String location, String hours, int adminid) {
        this.canteenid = canteenid;
        this.picture = picture;
        this.name = name;
        this.location = location;
        this.hours = hours;
        this.adminid = adminid;
    }

    public CanteenDetail(int canteenid, String picture, String name, String location, String hours, boolean specialbook, String[] sbookpattern, int adminid) {
        this.canteenid = canteenid;
        this.picture = picture;
        this.name = name;
        this.location = location;
        this.hours = hours;
        this.specialbook = specialbook;
        this.sbookpattern = sbookpattern;
        this.adminid = adminid;
    }

    public boolean isSpecialbook() {
        return specialbook;
    }

    public String[] getSbookpattern() {
        return sbookpattern;
    }

    public void setCanteenid(int canteenid) {
        this.canteenid = canteenid;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public void setAdminid(int adminid) {
        this.adminid = adminid;
    }

    public int getCanteenid() {
        return canteenid;
    }

    public String getPicture() {
        return picture;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getHours() {
        return hours;
    }

    public int getAdminid() {
        return adminid;
    }
}
