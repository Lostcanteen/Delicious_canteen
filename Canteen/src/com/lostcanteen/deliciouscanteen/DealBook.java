package com.lostcanteen.deliciouscanteen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class DealBook extends HttpServlet {

public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException {
        
        request.setCharacterEncoding("utf-8");  //���ñ��뷽ʽ  
        response.setCharacterEncoding("utf-8");  
          
        //(1) ��ȡandroid ���󣬶Դ����������ݽ��д���  
        InputStream is = request.getInputStream();  
        InputStreamReader isr = new InputStreamReader(is,"utf-8");  
        BufferedReader br = new BufferedReader(isr);  
        String userjson = br.readLine();  
          
        System.out.println("userjson:"+userjson.toString());  //��� json ��ʽ����  
          
        try {  
            JSONArray userarr = new JSONArray(userjson);  
            JSONObject userobj = userarr.getJSONObject(0);  
               
            int sbookid = userobj.getInt("sbookid");  
            String deal = userobj.getString("deal");  
              
            // (2)���ؽ�����ɹ���ʧ�� 
            DBConnection db = new DBConnection();
            db.dealBook(sbookid, deal);
            OutputStream os = response.getOutputStream();  
            OutputStreamWriter osw = new OutputStreamWriter(os,"utf-8");  
            BufferedWriter bw = new BufferedWriter(osw);   
            bw.write("true");  
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
