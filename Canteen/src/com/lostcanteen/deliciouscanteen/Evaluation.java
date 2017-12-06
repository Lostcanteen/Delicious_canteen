package com.lostcanteen.deliciouscanteen;

import java.sql.Date;
import java.io.Serializable;
import java.sql.SQLException;

/**
 * Created by yw199 on 2017/10/24.
 */

public class Evaluation implements Serializable{
    private int canteenid; //椋熷爞id
    private int dishid; //鑿滃搧id
    private String username; //鐢ㄦ埛鍚�?
    private int star; //璇勪环鏄熺骇
    private Date date; //鏃ユ�?
    private String comment; //璇勪环璇︾粏

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
     * 鏄剧ず淇℃伅鑾峰�?
     * @return 椋熷爞鍚�?
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
     * 鏄剧ず淇℃伅鑾峰�?
     * @return 鑿滃搧鍚�?
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
