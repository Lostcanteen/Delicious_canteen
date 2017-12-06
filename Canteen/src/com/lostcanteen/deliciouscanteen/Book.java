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
    private String type;  // b锛氭棭椁�? l锛氬崍椁�? d锛氭櫄椁�?
    private int dishid;
    private int cnt;

    public Book(int canteenid, Date date, String type, int dishid, int cnt) {
        this.canteenid = canteenid;
        this.date = date;
        this.type = type;
        this.dishid = dishid;
        this.cnt = cnt;
    }

    //棰勮淇℃伅濡備綍鏄剧ず 鍙樉绀洪鍫傚悕鍙婅彍鍚� 鎴栬缁嗕俊鎭�? 瀵瑰簲鏁版嵁搴撴煡璇㈡柟娉曠紪鍐�?


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

    public Date getDate() {
        return date;
    }

    public String getTypePattern() {
        if (type.equals("b")) {
            return "鏃╅�?";
        } else if (type.equals("l")) {
            return "鍗堥�?";
        } else if (type.equals("d")) {
            return "鏅氶�?";
        } else {
            return "error";
        }
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

    public int getCnt() {
        return cnt;
    }
}
