package com.lostcanteen.deliciouscanteen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class AddCanteen extends HttpServlet{

public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException {
        
        try {
            ObjectInputStream ois = new ObjectInputStream(request.getInputStream());  
            CanteenDetail c = (CanteenDetail) ois.readObject();
            
            DBConnection db = new DBConnection();
            db.addCanteen(c);
            ois.close();
            
            OutputStream os = response.getOutputStream();  
            OutputStreamWriter osw = new OutputStreamWriter(os,"utf-8");  
            BufferedWriter bw = new BufferedWriter(osw);    
            bw.write("true"); 
            bw.flush();
            if (os != null)  os.close();  
            if (osw != null)  osw.close();  
            if (bw != null)  bw.close();
        } catch (Exception e){e.printStackTrace();}

    }
}
