package com.lostcanteen.deliciouscanteen;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by yw199 on 2017/10/24.
 */

public class SpecialBook implements Serializable {
    private int sbookid;
    private String canteenName;
    private String username;
    private Date date; //日期
    private String type; //时间  b：早餐 l：午餐 d：晚餐
    private String spot;  //场景选择
    private String num;  //人数
    private String other; //其他信息
    private String deal; //处理情况 w：等待处理 ac：接受 re：拒绝
    private int adminid;


    public SpecialBook(int sbookid,String canteenName, String username, Date date, String type, String spot, String num, String other,int adminid,String deal) {
        this.sbookid = sbookid;
        this.canteenName = canteenName;
        this.username = username;
        this.date = date;
        this.type = type;
        this.spot = spot;
        this.num = num;
        this.other = other;
        this.adminid = adminid;
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

    public int getSbookid() {
        return sbookid;
    }

    public String getCanteenName() {
        return canteenName;
    }

    public String getDeal() {
        return deal;
    }

    public int getAdminid() {
        return adminid;
    }

    public String getDealPattern() {
        if (deal.equals("w")) {
            return "等待处理";
        } else if (deal.equals("ac")) {
            return "接受";
        } else if (deal.equals("re")) {
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
