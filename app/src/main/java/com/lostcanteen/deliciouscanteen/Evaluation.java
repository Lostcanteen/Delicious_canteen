package com.lostcanteen.deliciouscanteen;

import com.lostcanteen.deliciouscanteen.DBConnection;

import java.io.Serializable;
import java.sql.Date;
import java.sql.SQLException;

/**
 * Created by yw199 on 2017/10/24.
 */

public class Evaluation implements Serializable {
    private int canteenid; //食堂id
    private int dishid; //菜品id
    private String username; //用户名
    private int star; //评价星级
    private Date date; //日期
    private String comment; //评价详细

    public Evaluation(int canteenid, int dishid, String username, int star, Date date, String comment) {
        this.canteenid = canteenid;
        this.dishid = dishid;
        this.username = username;
        this.star = star;
        this.date = date;
        this.comment = comment;
    }

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
        DBConnection db = new DBConnection();
        return db.queryCanteen(canteenid).getName();
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
        DBConnection db = new DBConnection();
        return db.queryDish(canteenid,dishid).getName();
    }

    public String getUsername() {
        return username;
    }

    public int getStar() {
        return star;
    }

    public Date getDate() {
        return date;
    }

    public String getComment() {
        return comment;
    }
}
