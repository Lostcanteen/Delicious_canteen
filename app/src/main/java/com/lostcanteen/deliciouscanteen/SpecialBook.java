package com.lostcanteen.deliciouscanteen;

import java.sql.Date;

/**
 * Created by yw199 on 2017/10/24.
 */

public class SpecialBook {
    private int sbookid;
    private int canteenid;
    private String username;
    private Date date; //日期
    private String type; //时间  b：早餐 l：午餐 d：晚餐
    private String spot;  //场景选择
    private String num;  //人数
    private String other; //其他信息
    private String deal; //处理情况 w：等待处理 ac：接受 re：拒绝

    //添加特殊预约时使用的构造器 commitSBook
    public SpecialBook(int canteenid, String username, Date date, String type, String spot, String num, String other) {
        this.canteenid = canteenid;
        this.username = username;
        this.date = date;
        this.type = type;
        this.spot = spot;
        this.num = num;
        this.other = other;
    }

    public SpecialBook(int sbookid, int canteenid, String username, Date date, String type, String spot, String num, String other, String deal) {
        this.sbookid = sbookid;
        this.canteenid = canteenid;
        this.username = username;
        this.date = date;
        this.type = type;
        this.spot = spot;
        this.num = num;
        this.other = other;
        this.deal = deal;
    }

    public SpecialBook(int canteenid, String username, Date date, String type, String spot, String num, String other, String deal) {
        this.canteenid = canteenid;
        this.username = username;
        this.date = date;
        this.type = type;
        this.spot = spot;
        this.num = num;
        this.other = other;
        this.deal = deal;
    }
    public String getTypePattern() {
        if (type.equals("b")) {
            return "早餐";
        } else if (type.equals("l")) {
            return "午餐";
        } else if (type.equals("d")) {
            return "晚餐";
        } else {
            return "error";
        }
    }

    public String getDealPattern() {
        if (type.equals("w")) {
            return "等待处理";
        } else if (type.equals("ac")) {
            return "接受";
        } else if (type.equals("re")) {
            return "拒绝";
        } else {
            return "error";
        }
    }

    public Date getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public int getCanteenid() {
        return canteenid;
    }

    public String getUsername() {
        return username;
    }

    public String getSpot() {
        return spot;
    }

    public String getNum() {
        return num;
    }

    public String getOther() {
        return other;
    }
}
