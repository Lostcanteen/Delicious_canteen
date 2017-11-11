package com.lostcanteen.deliciouscanteen;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by yw199 on 2017/10/18.
 */

public class Test {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
       // DBConnection dbConnection = new DBConnection();
       if(WebTrans.isPasswordTrue("18845787905","123456")) System.out.println("登陆成功");
       // new DBConnection().addCanteen(new CanteenDetail(2,"/image","学苑","","",1));
       // new DBConnection().deleteCanteen(10);
        List<Dish> list = WebTrans.listMenu(10,new Date(2017,9,23),'B');



        new DBConnection().addMenu(11,new Date(2017,9,23),new int[2],'b');

    }
}
