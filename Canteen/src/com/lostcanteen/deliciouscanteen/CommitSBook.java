package com.lostcanteen.deliciouscanteen;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommitSBook extends HttpServlet{

public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException {
        
        try {
            ObjectInputStream ois = new ObjectInputStream(request.getInputStream());  
            SpecialBook specialBook = (SpecialBook) ois.readObject();
            
            DBConnection db = new DBConnection();
            db.commitSBook(specialBook);
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
