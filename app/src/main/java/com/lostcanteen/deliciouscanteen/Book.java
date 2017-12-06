package com.lostcanteen.deliciouscanteen;

import java.io.Serializable;
import java.sql.Date;
import java.sql.SQLException;

/**
 * Created by yw199 on 2017/10/24.
 */

public class Book implements Serializable{
    private int canteenid;
    private Date date;
    private String type;  // b：早餐 l：午餐 d：晚餐
    private int dishid;
    private int cnt;

    public Book(int canteenid, Date date, String type, int dishid, int cnt) {
        this.canteenid = canteenid;
        this.date = date;
        this.type = type;
        this.dishid = dishid;
        this.cnt = cnt;
    }

    //预订信息如何显示 只显示食堂名及菜名 或详细信息？ 对应数据库查询方法编写


    public int getCanteenid() {
        return canteenid;
    }

    /**
     * 显示信息获取
     * @return 食堂名
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public String getCanteenName() throws SQLException, ClassNotFoundException {
        return null;
    }

    public Date getDate() {
        return date;
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

    public int getDishid() {
        return dishid;
    }

    /**
     * 显示信息获取
     * @return 菜品名
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public String getDishName() throws SQLException, ClassNotFoundException {
        return null;
    }

    public int getCnt() {
        return cnt;
    }
}
