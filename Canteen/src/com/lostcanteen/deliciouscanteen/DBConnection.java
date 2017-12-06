package com.lostcanteen.deliciouscanteen;

import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Created by yw199 on 2017/10/16.
 */

public class DBConnection {

    private String driver = "com.mysql.jdbc.Driver"; // 椹卞姩绋嬪簭鍚�
    private String url = "jdbc:mysql://w.rdc.sae.sina.com.cn:3306/app_canteen"; 
    private String user = "j3wk0354w1";  
    private String password = "53j4yy4yimwix40hy1hy05kwj414mmk112j22j5k";
    private static Connection conn;

    public DBConnection() throws ClassNotFoundException, SQLException
    {
        Class.forName(driver);
        conn = DriverManager.getConnection(url, user, password);
    }

    /**
     * 注册用户名是否存在（注册时提示重新选择用户名，登陆时提示用户名不存在，请注册）
     * @param username 用户名
     * @return 存在：true 不存在：false
     * @throws SQLException
     */
    public boolean isUsernameExist(String username) throws SQLException {
        boolean ret=false;
        Statement statement = conn.createStatement();	// statement鐢ㄦ潵鎵цSQL璇�?
        String sql = "SELECT * FROM user where username='"+username+"'";	// 瑕佹墽琛�?殑SQL璇�?
        ResultSet rs = statement.executeQuery(sql);	// 缁撴灉闆�?
        if(rs.next())  ret=true;
        else ret=false;
        statement.close();
        rs.close();
        conn.close();
        return ret;
    }

    /**
     * 添加用户
     * @param username 用户名
     * @param password 密码
     * @param admin 是否为管理员
     * @return
     * @throws SQLException
     */
    public boolean addUser(String username,String password,boolean admin) throws SQLException {

        PreparedStatement statement;
        String sql = "insert into user (username,password,admin) values (?,?,?)";	// 瑕佹墽琛�?殑SQL璇�?
        statement=conn.prepareStatement(sql);
        statement.setString(1,username);
        statement.setString(2,password);
        statement.setBoolean(3,admin);
        statement.executeUpdate();
        statement.close();
        conn.close();
        return true;
    }

    /**
     * 登陆密码判断 (done)
     * @param username 用户名
     * @param password 密码
     * @return true登陆成功，false密码错误
     * @throws SQLException
     */
    public boolean isPasswordTrue(String username,String password) throws SQLException {
        boolean ret=false;
        Statement statement = conn.createStatement();	// statement鐢ㄦ潵鎵цSQL璇�?
        String sql = "SELECT * FROM user where username='"+username+"'";	// 瑕佹墽琛�?殑SQL璇�?
        ResultSet rs = statement.executeQuery(sql);	// 缁撴灉闆�?
        if(rs.next())
        {
            if(password.equals(rs.getString("password"))) ret=true;
            else ret=false;
        }
        statement.close();
        rs.close();
        conn.close();
        return ret;
    }

    /**
     * 是否为管理员查询
     * @param username 用户名
     * @return 是：true 否：false
     * @throws SQLException
     */
    public User isAdmin(String username) throws SQLException {
        User user = new User();
        Statement statement = conn.createStatement();   // statement用来执行SQL语句
        String sql = "SELECT * FROM user where username='"+username+"'";    // 要执行的SQL语句
        ResultSet rs = statement.executeQuery(sql); // 结果集
        if (rs.next()) {
            user.setUserid(rs.getInt("iduser"));
            user.setUsername(username);
            user.setAdmin(rs.getBoolean("admin"));
        }
        statement.close();
        rs.close();
        conn.close();
        return user;
    }

    /**
     * 普通用户主页食堂列表显示 (done)
     * @return 全部食堂详细列表
     */
    public ArrayList<CanteenDetail> allCantainDetail() throws SQLException {
        ArrayList<CanteenDetail> ret=new ArrayList<>();
        Statement statement = conn.createStatement();	// statement鐢ㄦ潵鎵цSQL璇�?
        String sql = "SELECT * FROM canteendetail";	// 瑕佹墽琛�?殑SQL璇�?
        ResultSet rs = statement.executeQuery(sql);	// 缁撴灉闆�?
        while (rs.next()) {
            int canteenid = rs.getInt("idcanteendetail");
            String picture = rs.getString("picture");
            String name = rs.getString("name");
            String location = rs.getString("location");
            String hours = rs.getString("hours");
            Boolean specialbook = rs.getBoolean("specialbook");
            String sbookpattern = rs.getString("sbookpattern");
            int adminid = rs.getInt("idadmin");
            CanteenDetail tmp = new CanteenDetail(canteenid,picture,name,location,hours,specialbook,strpatternChange(sbookpattern),adminid);
            ret.add(tmp);
        }
        statement.close();
        rs.close();
        conn.close();
        return ret;
    }

    /**
     * 管理员主页食堂列表 (done)
     * @param adminid 管理员id
     * @return 管理员所管理食堂列表 无返回空list，非null
     * @throws SQLException
     */
    public ArrayList<CanteenDetail> adminCantainDetail(int adminid) throws SQLException {
        ArrayList<CanteenDetail> ret=new ArrayList<>();
        Statement statement = conn.createStatement();	// statement鐢ㄦ潵鎵цSQL璇�?
        String sql = "SELECT * FROM canteendetail where idadmin='"+adminid+"'";	// 瑕佹墽琛�?殑SQL璇�?
        ResultSet rs = statement.executeQuery(sql);	// 缁撴灉闆�?
        while (rs.next()) {
            int canteenid = rs.getInt("idcanteendetail");
            String picture = rs.getString("picture");
            String name = rs.getString("name");
            String location = rs.getString("location");
            String hours = rs.getString("hours");
            Boolean specialbook = rs.getBoolean("specialbook");
            String sbookpattern = rs.getString("sbookpattern");
            CanteenDetail tmp = new CanteenDetail(canteenid,picture,name,location,hours,specialbook,strpatternChange(sbookpattern),adminid);
            ret.add(tmp);
        }
        statement.close();
        rs.close();
        conn.close();
        return ret;
    }

    /**
     * 根据食堂id查询食堂详细信息
     * @param canteenid
     * @return
     * @throws SQLException
     */
    public CanteenDetail queryCanteen(int canteenid) throws SQLException {
        CanteenDetail canteen = null;
        Statement statement = conn.createStatement();	// statement鐢ㄦ潵鎵цSQL璇�?
        String sql = "SELECT * FROM canteendetail where idcanteendetail='"+canteenid+"'";	// 瑕佹墽琛�?殑SQL璇�?
        ResultSet rs = statement.executeQuery(sql);	// 缁撴灉闆�?
        if (rs.next()) {
            String image = rs.getString("picture");
            String name = rs.getString("name");
            String location = rs.getString("location");
            String hours =  rs.getString("hours");
            boolean specialbook = rs.getBoolean("specialbook");
            String sbookpattern = rs.getString("sbookpattern");
            int adminid = rs.getInt("idadmin");
            canteen = new CanteenDetail(canteenid,image,name,location,hours,specialbook,strpatternChange(sbookpattern),adminid);
        }
        rs.close();
        return canteen;
    }

    /**
     * 管理员添加食堂，填写详细信息及是否提供特殊预约服务（即初始信息设置） (done)
     * @param canteenDetail canteenid无需给出,adminid为获取的当前用户信息
     * @throws SQLException
     */
    public void addCanteen(CanteenDetail canteenDetail) throws SQLException {
        int canteenid= -1;
        PreparedStatement statement;
        Statement stmt;
        ResultSet rs;
        //娣诲姞椋熷爞璇︾粏淇℃伅
        String sql = "insert into canteendetail (picture,name,location,hours,idadmin,specialbook,sbookpattern) values (?,?,?,?,?,?,?)";	// 瑕佹墽琛�?殑SQL璇�?
        statement=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        statement.setString(1,canteenDetail.getPicture());
        statement.setString(2,canteenDetail.getName());
        statement.setString(3,canteenDetail.getLocation());
        statement.setString(4,canteenDetail.getHours());
        statement.setInt(5,canteenDetail.getAdminid());
        statement.setBoolean(6,canteenDetail.isSpecialbook());
        statement.setString(7,strpatternReverse(canteenDetail.getSbookpattern()));
        statement.executeUpdate();
        rs = statement.getGeneratedKeys();
        if (rs.next()) {
            canteenid = rs.getInt(1);
        }
        rs.close();
        statement.close();
        //鍒涘缓璇ラ鍫傝彍鍝佽�??,椋熻氨琛�?
        stmt=conn.createStatement();
        sql = "create table canteen"+canteenid+"dishes("
                + "iddishes int not null auto_increment,"
                +"name varchar(45) not null,"
                +"image varchar(45) null,"
                +"price float null,"
                +"breakfast tinyint not null,"
                +"lunch tinyint not null,"
                +"dinner tinyint not null,"
                +"main tinyint not null,"
                +"inuse tinyint not null," 
                +"primary key (iddishes));";
        stmt.executeUpdate(sql);
        sql = "create table canteen"+canteenid+"menu("
                + "date date not null,"
                +"breakfast varchar(100) null,"
                +"lunch varchar(100) null,"
                +"dinner varchar(100) null,"
                +"bbook varchar(100) null,"
                +"lbook varchar(100) null,"
                +"dbook varchar(100) null,"
                +"bbookcnt varchar(100) null,"
                +"lbookcnt varchar(100) null,"
                +"dbookcnt varchar(100) null,"
                +"primary key (date));";
        stmt.executeUpdate(sql);
        stmt.close();
        conn.close();
    }

    /**
     * 管理员修改食堂详细信息 获取文本栏中需载入原始信息，管理员在此基础上修改
     * @param canteenDetail 根据canteenid修改，无需提供adminid
     * @throws SQLException
     */
    public void updateCanteen(CanteenDetail canteenDetail) throws SQLException {
        String sql = "update canteendetail set picture=?,name=?,location=?,hours=?,specialbook=?,sbookpattern=? where idcanteendetail='"+canteenDetail.getCanteenid()+"'";	// 瑕佹墽琛�?殑SQL璇�?
        PreparedStatement statement=conn.prepareStatement(sql);
        statement.setString(1,canteenDetail.getPicture());
        statement.setString(2,canteenDetail.getName());
        statement.setString(3,canteenDetail.getLocation());
        statement.setString(4,canteenDetail.getHours());
        statement.setBoolean(5,canteenDetail.isSpecialbook());
        statement.setString(6,strpatternReverse(canteenDetail.getSbookpattern()));
        statement.executeUpdate();
        statement.close();
        conn.close();
    }

    /**
     * 管理员修改特殊预订信息
     * @param canteenid 食堂id
     * @param specialbook 是否开启boolean
     * @param sbookpattern 开启标签
     * @throws SQLException
     */
    public void updateSbook(int canteenid,boolean specialbook,String[] sbookpattern) throws SQLException {
        String sql = "update canteendetail set specialbook=?,sbookpattern=? where idcanteendetail='"+canteenid+"'";	// 瑕佹墽琛�?殑SQL璇�?
        PreparedStatement statement=conn.prepareStatement(sql);
        statement.setBoolean(1,specialbook);
        statement.setString(2,strpatternReverse(sbookpattern));
        statement.executeUpdate();
        statement.close();
        conn.close();
    }

    /**
     * 管理员删除食堂 （菜品,食谱删除）
     * @param canteenid 食堂id
     * @throws SQLException
     */
    public void deleteCanteen(int canteenid) throws SQLException {
        Statement statement = conn.createStatement();	// statement鐢ㄦ潵鎵цSQL璇�?
        String sql = "delete FROM canteendetail where idcanteendetail='"+canteenid+"'";	// 瑕佹墽琛�?殑SQL璇�?
        statement.executeUpdate(sql);	// 缁撴灉闆�?
        sql = "drop table canteen"+canteenid+"dishes";	// 瑕佹墽琛�?殑SQL璇�?
        statement.executeUpdate(sql);
        sql = "drop table canteen"+canteenid+"menu";
        statement.executeUpdate(sql);
        statement.close();
        conn.close();
    }

    /**
     * 管理员添加菜品 (done)
     * @param dish 菜品详细信息 （无需提供dishid checkbox 早午晚餐复选框 main单选）
     * @throws SQLException
     */
    public void addDish(Dish dish) throws SQLException {
        PreparedStatement statement;
        String sql = "insert into canteen"+dish.getCanteenid()+"dishes"+" (name,image,price,breakfast," +
                "lunch,dinner,main,inuse) values (?,?,?,?,?,?,?,?)";	// 瑕佹墽琛�?殑SQL璇�?
        statement=conn.prepareStatement(sql);
        statement.setString(1,dish.getName());
        statement.setString(2,dish.getImage());
        statement.setFloat(3,dish.getPrice());
        statement.setBoolean(4,dish.isBreakfast());
        statement.setBoolean(5,dish.isLunch());
        statement.setBoolean(6,dish.isDinner());
        statement.setBoolean(7,dish.isMain());
        statement.setBoolean(8, true);
        statement.executeUpdate();
        statement.close();
        conn.close();
    }

    /**
     * 管理员删除菜品
     * @param canteenid 食堂id
     * @param dishid 菜品id
     * @throws SQLException
     */
    public void deleteDish(int canteenid,int dishid) throws SQLException {
        String sql = "update canteen"+canteenid+"dishes"+" set inuse=?"+" where iddishes='"+dishid+"'";	// 瑕佹墽琛�?殑SQL璇�?
        PreparedStatement statement=conn.prepareStatement(sql);
        statement.setBoolean(1, false);
        statement.executeUpdate();
        statement.close();
        conn.close();
    }

    /**
     * 管理员修改菜品信息 （文本框及checkbox内显示原始信息，在此基础上进行修改）
     * @param cantennid 食堂id
     * @param dish 修改后菜品详细信息
     * @throws SQLException
     */
    public void updateDish(Dish dish) throws SQLException {
        String sql = "update canteen"+dish.getCanteenid()+"dishes"+" set name=?,image=?,price=?,breakfast=?,lunch=?,dinner=?,main=? " +
                "where iddishes='"+dish.getDishid()+"'";	// 瑕佹墽琛�?殑SQL璇�?
        PreparedStatement statement=conn.prepareStatement(sql);
        statement.setString(1,dish.getName());
        statement.setString(2,dish.getImage());
        statement.setFloat(3,dish.getPrice());
        statement.setBoolean(4,dish.isBreakfast());
        statement.setBoolean(5,dish.isLunch());
        statement.setBoolean(6,dish.isDinner());
        statement.setBoolean(7,dish.isMain());
        statement.executeUpdate();
        statement.close();
        conn.close();
    }

    /**
     * 根据菜品id查询菜品详细信息
     * @param canteenid 食堂id
     * @param dishid 菜品id
     * @return Dish信息
     * @throws SQLException
     */
    public Dish queryDish(int canteenid,int dishid) throws SQLException {
        Dish dish = null;
        Statement statement = conn.createStatement();
        String sql = "SELECT * FROM canteen"+canteenid+"dishes"+" where iddishes='"+dishid+"'";
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()) {
            String name = rs.getString("name");
            String image = rs.getString("image");
            float price = rs.getFloat("price");
            dish = new Dish(canteenid,dishid,name,image,price,false,false,false,false);
        }
        rs.close();
        return dish;
    }

    /**
     * 菜品列表获取 （done)
     * @param canteenid 食堂id
     * @param option 功能选择 'A'：全部菜品 'M'：主营菜品 'B'：早餐 'L'：午餐 'D'：晚餐
     * @return 菜品详细信息list
     * @throws SQLException
     */
    public ArrayList<Dish> listDish(int canteenid,char option) throws SQLException {
        String sql;
        ArrayList<Dish> ret=new ArrayList<>();
        Statement statement = conn.createStatement();
        if (option == 'A') {
            sql = "SELECT * FROM canteen"+canteenid+"dishes where inuse='1'";
        } else if (option == 'M') {
            sql = "SELECT * FROM canteen"+canteenid+"dishes"+" where main='1' and inuse='1'";
        } else if (option == 'B') {
            sql = "SELECT * FROM canteen"+canteenid+"dishes"+" where breakfast='1' and inuse='1'";
        } else if (option == 'L') {
            sql = "SELECT * FROM canteen"+canteenid+"dishes"+" where lunch='1' and inuse='1'";
        } else if (option == 'D'){
            sql = "SELECT * FROM canteen"+canteenid+"dishes"+" where dinner='1' and inuse='1'";
        } else {
            return ret;
        }
        ResultSet rs = statement.executeQuery(sql);	// 缁撴灉闆�?
        while (rs.next()) {
            int dishid = rs.getInt("iddishes");
            String name = rs.getString("name");
            String image = rs.getString("image");
            float price = rs.getFloat("price");
            boolean breakfast = rs.getBoolean("breakfast");
            boolean lunch = rs.getBoolean("lunch");
            boolean dinner = rs.getBoolean("dinner");
            boolean main = rs.getBoolean("main");
            Dish tmp = new Dish(canteenid,dishid,name,image,price,breakfast,lunch,dinner,main);
            ret.add(tmp);
        }
        statement.close();
        rs.close();
        conn.close();
        return ret;
    }

    //添加日期
    void addDate(int canteenid,Date date) throws SQLException {
        Statement statement = conn.createStatement();
        PreparedStatement stmt;
        String sql = "SELECT * FROM canteen"+canteenid+"menu"+" where date='"+date+"'";
        ResultSet rs = statement.executeQuery(sql);
        if (!rs.next()) {
            sql = "insert into canteen"+canteenid+"menu"+" (date) values (?)";
            stmt=conn.prepareStatement(sql);
            stmt.setDate(1,date);
            stmt.executeUpdate();
            stmt.close();
        }
        rs.close();
        statement.close();
    }

    /**
     * 管理员添加食谱或预订信息 (done)
     * @param canteenid 食堂id
     * @param date 发布食谱日期
     * @param menu 食谱内容 按规定格式字符串存储 如：1_2_3
     * @param option 发布食谱时间选择 B：早餐 L:午餐 D：晚餐 b:早餐预订 l：午餐预订 d：晚餐预订
     * @throws SQLException
     */
    public void addMenu(int canteenid,Date date,String menu,char option) throws SQLException {
        addDate(canteenid,date);
        updateMenu(canteenid,date,menu,option);
    }

    /**
     * 管理员删除食谱及预订信息 （如删除30天前食谱）
     * @param canteenid 食堂id
     * @param date 需删除记录时间
     * @throws SQLException
     */
    public void deleteMenu(int canteenid,Date date) throws SQLException {
        Statement statement = conn.createStatement();	// statement鐢ㄦ潵鎵цSQL璇�?
        String sql = "SELECT * FROM canteen"+canteenid+"menu"+" where date='"+date+"'";
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()) {
            sql = "delete FROM canteen"+canteenid+"menu"+" where date='"+date+"'";
            statement.executeUpdate(sql);
        }
        rs.close();
        statement.close();
        conn.close();
    }

    /**
     * 管理员修改菜单或预订信息
     * @param canteenid 食堂id
     * @param date 修改菜单日期
     * @param menu 更新后的食谱菜品id数组 按格式串传入 例 ： 1_2_3
     * @param option 修改食谱时间选择 B：早餐 L:午餐 D：晚餐
     * @throws SQLException
     */
    public void updateMenu(int canteenid,Date date,String menu,char option) throws SQLException {
        PreparedStatement statement;
        String sql,sql1=null;
        if (option == 'B') {
            sql = "update canteen"+canteenid+"menu"+" set breakfast=? where date='"+date+"'";
        } else if (option == 'L') {
            sql = "update canteen"+canteenid+"menu"+" set lunch=? where date='"+date+"'";
        } else if (option == 'D') {
            sql = "update canteen"+canteenid+"menu"+" set dinner=? where date='"+date+"'";
        } else if (option == 'b') {
            sql = "update canteen"+canteenid+"menu"+" set bbook=? where date='"+date+"'";
            sql1 = "update canteen"+canteenid+"menu"+" set bbookcnt=? where date='"+date+"'";
        } else if (option == 'l') {
            sql = "update canteen"+canteenid+"menu"+" set lbook=? where date='"+date+"'";
            sql1 = "update canteen"+canteenid+"menu"+" set lbookcnt=? where date='"+date+"'";
        } else if (option == 'd') {
            sql = "update canteen"+canteenid+"menu"+" set dbook=? where date='"+date+"'";
            sql1 = "update canteen"+canteenid+"menu"+" set dbookcnt=? where date='"+date+"'";
        } else {
            return ;
        }
        
        statement=conn.prepareStatement(sql);
        statement.setString(1,menu);
        statement.executeUpdate();
        if (sql1 != null) {
            int[] cnt = new int[menu.split("_").length];  
            statement = conn.prepareStatement(sql1);
            statement.setString(1,patternReverse(cnt));
            statement.executeUpdate();
        }
        statement.close();
        conn.close();
    }

    int[] patternChange(String str) {
        String[] arrStr=str.split("_");
        int[] ret=new int[arrStr.length];
        for (int i = 0;i < ret.length;i ++) {
            ret[i] = Integer.parseInt(arrStr[i]);
        }
        return ret;
    }

    String patternReverse(int[] arrMenu) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i<arrMenu.length;i ++) {
            sb.append(arrMenu[i]);
            sb.append("_");
        }
        return sb.toString();
    }

    String[] strpatternChange(String str) {
        return str.split("_");
    }

    String strpatternReverse(String[] arrStr) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i<arrStr.length;i ++) {
            sb.append(arrStr[i]);
            sb.append("_");
        }
        return sb.toString();
    }

    /**
     * 管理员及普通用户菜单信息获取 (done)
     * @param canteenid 食堂id
     * @param date 日期
     * @param option 时间选择 B：早餐 L:午餐 D：晚餐 b:早餐预订 l：午餐预订 d：晚餐预订
     * @return 菜谱列表 未发布则返回空arraylist，不是null
     * @throws SQLException
     */
    public ArrayList<Dish> listMenu(int canteenid,Date date,char option) throws SQLException {
        ArrayList<Dish> ret=new ArrayList<>();
        Statement statement = conn.createStatement();	// statement鐢ㄦ潵鎵цSQL璇�?
        String sql = "SELECT * FROM canteen"+canteenid+"menu"+" where date='"+date+"'";
        String menu;
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()) {
            if (option == 'B') {
                menu = rs.getString("breakfast");
            } else if (option == 'L') {
                menu = rs.getString("lunch");
            } else if (option == 'D') {
                menu = rs.getString("dinner");
            } else if (option == 'b') {
                menu = rs.getString("bbook");
            } else if (option == 'l') {
                menu = rs.getString("lbook");
            } else if (option == 'd') {
                menu = rs.getString("dbook");
            } else {
                return ret;
            }
            if (menu != null) {
                int[] arrMenu = patternChange(menu);
                for (int i = 0;i < arrMenu.length;i ++) {
                    sql = "SELECT * FROM canteen"+canteenid+"dishes"+" where iddishes='"+arrMenu[i]+"'";
                    rs = statement.executeQuery(sql);
                    if(rs.next()) {
                        String name = rs.getString("name");
                        String image = rs.getString("image");
                        float price = rs.getFloat("price");
                        Dish tmp = new Dish(canteenid,arrMenu[i],name,image,price,false,false,false,false);
                        ret.add(tmp);
                    }
                    
                }
            }
        }
        rs.close(); statement.close(); conn.close();
        return ret;
    }

    /**
     * 用户提交预订信息 (done)
     * @param canteenid 食堂id
     * @param date 预订日期
     * @param option 预订时间选择 b：早餐 l：午餐 d：晚餐
     * @param userid 用户id
     * @param dishid 菜品id数组 （可一次预订多种菜品）
     * @param cnt 预订数量数组
     * @throws SQLException
     */
    public void commitBook(int canteenid,Date date,char option,int userid,String dishidstr,String cntstr) throws SQLException {
        PreparedStatement statement;
        Statement stmt = conn.createStatement();
        String sql = "SELECT * FROM canteen"+canteenid+"menu"+" where date='"+date+"'";
        ResultSet rs =stmt.executeQuery(sql);
        String sql1,bookMenu,bookCnt;
        if (rs.next()) {
            if (option == 'b') {
                bookMenu = rs.getString("bbook");
                bookCnt = rs.getString("bbookcnt");
                sql1 = "update canteen"+canteenid+"menu"+" set bbookcnt=? where date='"+date+"'";
            } else if (option == 'l') {
                bookMenu = rs.getString("lbook");
                bookCnt = rs.getString("lbookcnt");
                sql1 = "update canteen"+canteenid+"menu"+" set lbookcnt=? where date='"+date+"'";
            } else if (option == 'd') {
                bookMenu = rs.getString("dbook");
                bookCnt = rs.getString("dbookcnt");
                sql1 = "update canteen"+canteenid+"menu"+" set dbookcnt=? where date='"+date+"'";
            } else {
                return ;
            }
            int[] bookID = patternChange(bookMenu);
            int[] bookcnt = patternChange(bookCnt);
            int[] dishid = patternChange(dishidstr);
            int[] cnt = patternChange(cntstr);
            for (int i = 0;i<dishid.length;i++) {
                int index = Arrays.binarySearch(bookID,dishid[i]);
                bookcnt[index]+=cnt[i];
            }
            bookCnt = patternReverse(bookcnt);
            statement=conn.prepareStatement(sql1);
            statement.setString(1,bookCnt);
            statement.executeUpdate();
            //鏇存柊绠＄悊鍛橀璁㈣�?�涓俊鎭畬鎴�?
            sql = "insert into book (iduser,idcanteen,date,type," +
                    "iddish,cnt) values (?,?,?,?,?,?)";
            for (int i = 0;i<dishid.length;i++) {
                statement = conn.prepareStatement(sql);
                statement.setInt(1,userid);
                statement.setInt(2,canteenid);
                statement.setDate(3,date);
                statement.setString(4,String.valueOf(option));
                statement.setInt(5,dishid[i]);
                statement.setInt(6,cnt[i]);
                statement.executeUpdate();
            }
            //鏇存柊鐢ㄦ埛棰勮琛ㄤ腑淇℃伅�?�屾�?
            statement.close();
        }
        stmt.close(); rs.close(); conn.close();
    }

    /**
     * 管理员预订总数获取 (done)
     * @param canteenid 食堂id
     * @param date 预订日期
     * @param option 预订时间选择 b：早餐 l：午餐 d：晚餐
     * @return 预订信息list  格式为：菜名 数量 （可用表格方式展示）
     * @throws SQLException
     */
    public ArrayList<String> adminBookQuery(int canteenid,Date date,char option) throws SQLException {
        ArrayList<String> ret = new ArrayList<>();
        Statement stmt = conn.createStatement();
        String sql = "SELECT * FROM canteen"+canteenid+"menu"+" where date='"+date+"'";
        ResultSet rs =stmt.executeQuery(sql);
        rs.next();
        String bookMenu,bookCnt;
        if (option == 'b') {
            bookMenu = rs.getString("bbook");
            bookCnt = rs.getString("bbookcnt");
        } else if (option == 'l') {
            bookMenu = rs.getString("lbook");
            bookCnt = rs.getString("lbookcnt");
        } else if (option == 'd') {
            bookMenu = rs.getString("dbook");
            bookCnt = rs.getString("dbookcnt");
        } else {
            return ret;
        }
        if (bookMenu != null) {
            int[] bookmenu = patternChange(bookMenu);
            int[] bookcnt = patternChange(bookCnt);
            for (int i = 0;i<bookmenu.length;i++) {
                StringBuilder sb = new StringBuilder();
                sql = "SELECT * FROM canteen"+canteenid+"dishes"+" where iddishes='"+bookmenu[i]+"'";
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    sb.append(rs.getString("name"));
                    sb.append(" ");
                    sb.append(bookcnt[i]);
                }
                ret.add(sb.toString());
            }
        }
        rs.close(); stmt.close(); conn.close();
        return ret;
    }

    /**
     * 用户预订信息查询 （我的）
     * @param userid 用户id
     * @return 预订历史信息list 无历史信息返回空表 非null
     * @throws SQLException
     */
    public ArrayList<Book> userBookQuery(int userid) throws SQLException {
        ArrayList<Book> ret = new ArrayList<>();
        Statement stmt = conn.createStatement();
        String sql = "SELECT * FROM book where iduser='"+userid+"'";
        ResultSet rs =stmt.executeQuery(sql);
        while (rs.next()) {
            int canteenid = rs.getInt("idcanteen");
            Date date = rs.getDate("date");
            String type = rs.getString("type");
            int dishid = rs.getInt("iddish");
            int cnt = rs.getInt("cnt");
            Book b = new Book(canteenid,date,type,dishid,cnt);
            ret.add(b);
        }
        rs.close(); stmt.close(); conn.close();
        return ret;
    }

    /**
     * 用户评价提交 (done)
     * @param eva 评价信息
     * @throws SQLException
     */
    public void commitEva(Evaluation eva) throws SQLException {
        PreparedStatement stmt;
        String sql = "insert into evaluation (idcanteen,iddish,username,star,date,comment) values (?,?,?,?,?,?)";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1,eva.getCanteenid());
        stmt.setInt(2,eva.getDishid());
        stmt.setString(3,eva.getUsername());
        stmt.setInt(4,eva.getStar());
        stmt.setDate(5,eva.getDate());
        stmt.setString(6,eva.getComment());
        stmt.executeUpdate();
        stmt.close(); conn.close();
    }

    /**
     * 菜品评价查询 (done)
     * @param canteenid 食堂id
     * @param dishid 菜品id id==0 对食堂意见或建议
     * @return 评价信息list
     * @throws SQLException
     */
    public ArrayList<Evaluation> dishEvaQuery(int canteenid,int dishid) throws SQLException {
        ArrayList<Evaluation> ret = new ArrayList<>();
        Statement stmt = conn.createStatement();
        String sql = "SELECT * FROM evaluation where idcanteen='"+canteenid+"'"+" and iddish='"+dishid+"'";
        ResultSet rs =stmt.executeQuery(sql);
        while (rs.next()) {
            String username = rs.getString("username");
            int star = rs.getInt("star");
            Date date = rs.getDate("date");
            String comment = rs.getString("comment");
            Evaluation eva = new Evaluation(canteenid,dishid,username,star,date,comment);
            ret.add(eva);
        }
        stmt.close(); conn.close();
        return ret;
    }

    /**
     * 用户评价信息查看 （我的）
     * @param username 用户名
     * @return 评价list
     * @throws SQLException
     */
    public ArrayList<Evaluation> userEvaQuery(String username) throws SQLException {
        ArrayList<Evaluation> ret = new ArrayList<>();
        Statement stmt = conn.createStatement();
        String sql = "SELECT * FROM evaluation where username='"+username+"'";
        ResultSet rs =stmt.executeQuery(sql);
        while (rs.next()) {
            int canteenid = rs.getInt("idcanteen");
            int dishid = rs.getInt("iddish");
            int star = rs.getInt("star");
            Date date = rs.getDate("date");
            String comment = rs.getString("comment");
            Evaluation eva = new Evaluation(canteenid,dishid,username,star,date,comment);
            if(dishid!=0)
            ret.add(eva);
        }
        stmt.close(); conn.close();
        return ret;
    }

    /**
     * 用户添加预约信息
     * @param specialBook 添加详细信息
     * @throws SQLException
     */
    public void commitSBook(SpecialBook specialBook) throws SQLException {
        PreparedStatement stmt;
        Statement statement = conn.createStatement();
        String sql = "insert into sbook (canteenname,username,spot,num,other,deal,date,type,idadmin) values (?,?,?,?,?,?,?,?,?)";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1,specialBook.getCanteenName());
        stmt.setString(2,specialBook.getUsername());
        stmt.setString(3,specialBook.getSpot());
        stmt.setString(4,specialBook.getNum());
        stmt.setString(5,specialBook.getOther());
        stmt.setString(6,"w");
        stmt.setDate(7,specialBook.getDate());
        stmt.setString(8,specialBook.getType());
        stmt.setInt(9,specialBook.getAdminid());
        stmt.executeUpdate();
        stmt.close(); conn.close();
    }

    /**
     * 用户预约历史信息查询
     * @param username 用户名
     * @return 历史预约信息list
     * @throws SQLException
     */
    public ArrayList<SpecialBook> userSbookQuery(String username) throws SQLException {
        ArrayList<SpecialBook> ret = new ArrayList<>();
        Statement stmt = conn.createStatement();
        String sql = "SELECT * FROM sbook where username='"+username+"'";
        ResultSet rs =stmt.executeQuery(sql);
        while (rs.next()) {
            int sbookid = rs.getInt("idsbook");
            String canteenName= rs.getString("canteenname");
            String spot = rs.getString("spot");
            String num = rs.getString("num");
            String other = rs.getString("other");
            String deal = rs.getString("deal");
            Date date = rs.getDate("date");
            String type = rs.getString("type");
            int adminid = rs.getInt("idadmin");
            SpecialBook specialBook = new SpecialBook(sbookid,canteenName,username,date,type,spot,num,other,adminid,deal);
            ret.add(specialBook);
        }
        rs.close(); stmt.close(); conn.close();
        return ret;
    }

    /**
     * 管理员预约信息查看
     * @param adminid
     * @return 预约信息list
     * @throws SQLException
     */
    public ArrayList<SpecialBook> adminSbookQuery(int adminid) throws SQLException {
        ArrayList<SpecialBook> ret = new ArrayList<>();
        Statement stmt = conn.createStatement();
        String sql = "SELECT * FROM sbook where idadmin='"+adminid+"'";
        ResultSet rs =stmt.executeQuery(sql);
        while (rs.next()) {
            int sbookid = rs.getInt("idsbook");
            String canteenName= rs.getString("canteenname");
            String spot = rs.getString("spot");
            String num = rs.getString("num");
            String other = rs.getString("other");
            String deal = rs.getString("deal");
            Date date = rs.getDate("date");
            String type = rs.getString("type");
            String username = rs.getString("username");
            SpecialBook specialBook = new SpecialBook(sbookid,canteenName,username,date,type,spot,num,other,adminid,deal);
            ret.add(specialBook);
        }
        rs.close(); stmt.close(); conn.close();
        return ret;
    }

    /**
     * 管理员处理预约信息
     * @param sbookid 预约id
     * @param deal 处理结果 ac：接受 re：拒绝
     * @throws SQLException
     */
    public void dealBook(int sbookid,String deal) throws SQLException {
        PreparedStatement statement;
        String sql = "update sbook set deal=? where idsbook='"+sbookid+"'";
        statement = conn.prepareStatement(sql);
        statement.setString(1,deal);
        statement.executeUpdate();
        statement.close(); conn.close();
    }
    
    public ArrayList<Article> articleList() throws SQLException {
        ArrayList<Article> ret = new ArrayList<>();
        Statement stmt = conn.createStatement();
        String sql = "SELECT * FROM article";
        ResultSet rs =stmt.executeQuery(sql);
        while (rs.next()) {
            String url = rs.getString("url");
            String title = rs.getString("title");
            Date date = rs.getDate("date");
            String image = rs.getString("image");
            Article article = new Article(url,title,date,image);
            ret.add(article);
        }
        rs.close(); stmt.close(); conn.close();
        return ret;
    }
    
    public void closeconn( ) throws SQLException {
        conn.close();
    }

}