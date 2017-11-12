package com.lostcanteen.deliciouscanteen;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yw199 on 2017/10/21.
 */

public class User implements Serializable {
    private int userid;
    private String username;
    private String password;
    private boolean admin;

    public User() {}

    public User(int userid, String username, String password, boolean admin) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.admin = admin;
    }

    /**
     * 菜品评价平均星级计算
     * @param eva 菜品评价list
     * @return 平均星级
     */
    public float averageStar(ArrayList<Evaluation> eva) {
        int sum = 0;
        for(Evaluation e:eva) {
            sum += e.getStar();
        }
        return sum/eva.size();
    }

    public String getUsername() {
        return username;
    }

    public int getUserid() {
        return userid;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
