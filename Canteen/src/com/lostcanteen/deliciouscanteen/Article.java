package com.lostcanteen.deliciouscanteen;

import java.io.Serializable;
import java.sql.Date;

public class Article implements Serializable {
    private String url; //����
    private String title; //���±���
    private Date date;//����ʱ��
    private String image;//����ͼ

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
