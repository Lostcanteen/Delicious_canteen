package com.lostcanteen.deliciouscanteen;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateDish extends HttpServlet{

public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException {
        
        request.setCharacterEncoding("utf-8");  //…Ë÷√±‡¬Î∑Ω Ω  
        response.setCharacterEncoding("utf-8");  

        try {
            ObjectInputStream ois = new ObjectInputStream(request.getInputStream());  
            Dish dish = (Dish) ois.readObject();
            
            DBConnection db = new DBConnection();
            db.updateDish(dish);
            ois.close();
            
            OutputStream os = response.getOutputStream();  
            OutputStreamWriter osw = new OutputStreamWriter(os,"utf-8");  
            BufferedWriter bw = new BufferedWriter(osw);    
            bw.write("true"); 
            bw.flush();
            
            if (os != null)  os.close();  
            if (osw != null)  osw.close();  
            if (bw != null)  bw.close();
            if (ois != null) ois.close();
        } catch (Exception e){e.printStackTrace();}

    }
}
