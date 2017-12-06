package com.lostcanteen.deliciouscanteen;

import java.io.Serializable;
import java.sql.Date;

public class Article implements Serializable {
    private String url; //链接
    private String title; //文章标题
    private Date date;//发布时间
    private String image;//封面图

    public Article(String url, String title, Date date, String image) {
        this.url = url;
        this.title = title;
        this.date = date;
        this.image = image;
    }
    
    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }
}
