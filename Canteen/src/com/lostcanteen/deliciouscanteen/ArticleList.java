package com.lostcanteen.deliciouscanteen;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ArticleList extends HttpServlet {
public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException {
        
        request.setCharacterEncoding("utf-8");  //设置编码方式  
        response.setCharacterEncoding("utf-8");   
          
        try {   
            DBConnection db = new DBConnection();
            ArrayList<Article> canteen = db.articleList();
            OutputStream outStream = response.getOutputStream();  
            ObjectOutputStream objOutputStream = new ObjectOutputStream(outStream);  
            objOutputStream.writeObject(canteen);//将对象写入对象输出流中  
            objOutputStream.flush();//刷新流，保证对象存储到流中  
            objOutputStream.close();//关闭流 
        } catch (Exception e){e.printStackTrace();}

    }


}
