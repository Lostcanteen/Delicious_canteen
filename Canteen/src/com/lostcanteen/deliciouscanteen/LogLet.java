package com.lostcanteen.deliciouscanteen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogLet extends HttpServlet {
    
    public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException {
        
        request.setCharacterEncoding("utf-8");  //设置编码方式  
        response.setCharacterEncoding("utf-8");  
          
        //(1) 获取android 请求，对传输对象的数据进行处理  
        InputStream is = request.getInputStream();  
        InputStreamReader isr = new InputStreamReader(is,"utf-8");  
        BufferedReader br = new BufferedReader(isr);  
        String userjson = br.readLine();  
          
        System.out.println("userjson:"+userjson.toString());  //输出 json 格式数据  
          
        try {  
            JSONArray userarr = new JSONArray(userjson);  
            JSONObject userobj = userarr.getJSONObject(0);  
               
            String username = userobj.getString("username");  
            String password = userobj.getString("password");  
              
            // (2)返回结果，成功或失败 
            DBConnection db = new DBConnection();
            boolean ret = db.isPasswordTrue(username, password);
            OutputStream os = response.getOutputStream();  
            OutputStreamWriter osw = new OutputStreamWriter(os,"utf-8");  
            BufferedWriter bw = new BufferedWriter(osw);  
               
              
            if (ret == true)   
                bw.write("true");  
            else   
                bw.write("false");  
              
            bw.flush();  
            if (os != null)  os.close();  
            if (osw != null)  osw.close();  
            if (is != null)  is.close();  
            if (isr != null)  isr.close();  
            if (br != null)  br.close();  
            if (bw != null)  bw.close();  
  
        } catch (Exception e){e.printStackTrace();}

    }
   

}
