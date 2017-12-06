package com.lostcanteen.deliciouscanteen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class ListMenu extends HttpServlet{

public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException {
        
        request.setCharacterEncoding("utf-8");  //设置编码方式  
        response.setCharacterEncoding("utf-8");   
        InputStream is = request.getInputStream();  
        InputStreamReader isr = new InputStreamReader(is,"utf-8");  
        BufferedReader br = new BufferedReader(isr);  
        String userjson = br.readLine();  
        
        System.out.println("userjson:"+userjson.toString());  //输出 json 格式数据 
        try {
            
            JSONArray userarr = new JSONArray(userjson);  
            JSONObject userobj = userarr.getJSONObject(0);  
               
            int canteenid = userobj.getInt("canteenid");
            String str = userobj.getString("date");
            int option = userobj.getInt("option");
            
            SimpleDateFormat bartDateFormat =  
                    new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(bartDateFormat.parse(str).getTime());
            
            DBConnection db = new DBConnection();
            ArrayList<Dish> dish = db.listMenu(canteenid, date,(char) option);
            OutputStream os = response.getOutputStream();  
            ObjectOutputStream objOutputStream = new ObjectOutputStream(os);  
            objOutputStream.writeObject(dish);//将对象写入对象输出流中  
            objOutputStream.flush();//刷新流，保证对象存储到流中  
            objOutputStream.close();//关闭流
            
            if (os != null)  os.close();   
            if (is != null)  is.close();  
            if (isr != null)  isr.close();  
            if (br != null)  br.close();  
        } catch (Exception e){e.printStackTrace();}

    }
}
