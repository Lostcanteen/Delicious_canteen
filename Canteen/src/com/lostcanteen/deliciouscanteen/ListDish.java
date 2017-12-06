package com.lostcanteen.deliciouscanteen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class ListDish extends HttpServlet{

public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException {
        
        request.setCharacterEncoding("utf-8");  //���ñ��뷽ʽ  
        response.setCharacterEncoding("utf-8");   
        InputStream is = request.getInputStream();  
        InputStreamReader isr = new InputStreamReader(is,"utf-8");  
        BufferedReader br = new BufferedReader(isr);  
        String userjson = br.readLine();  
        try {
            
            JSONArray userarr = new JSONArray(userjson);  
            JSONObject userobj = userarr.getJSONObject(0);  
               
            int canteenid = userobj.getInt("canteenid");
            int option = userobj.getInt("option");
            
            DBConnection db = new DBConnection();
            ArrayList<Dish> dish = db.listDish(canteenid,(char) option);
            OutputStream os = response.getOutputStream();  
            ObjectOutputStream objOutputStream = new ObjectOutputStream(os);  
            objOutputStream.writeObject(dish);//������д������������  
            objOutputStream.flush();//ˢ��������֤����洢������  
            objOutputStream.close();//�ر���
            
            if (os != null)  os.close();   
            if (is != null)  is.close();  
            if (isr != null)  isr.close();  
            if (br != null)  br.close();  
        } catch (Exception e){e.printStackTrace();}

    }
}
