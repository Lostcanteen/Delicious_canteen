package com.lostcanteen.deliciouscanteen;

import com.lostcanteen.deliciouscanteen.CanteenDetail;
import com.lostcanteen.deliciouscanteen.Dish;
import com.lostcanteen.deliciouscanteen.Evaluation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by yw199 on 2017/11/5.
 */

public class WebTrans {

    private static String basicurl = "http://canteen.applinzi.com/";

    public static User isAdmin(String username) {
        User ret = new User();
        try {
            URL url = new URL(basicurl+"IsAdmin");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setDoInput(true);  //可读可写
            http.setDoOutput(true);
            http.setUseCaches(false);  //不允许使用缓存
            http.setRequestMethod("POST");  //设置传输方式为 get
            http.connect();  //创建连接

            OutputStream os = http.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            JSONArray jsonArray = new JSONArray();  //创建 json 对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username",username);
            jsonArray.put(jsonObject);
            bw.write(jsonArray.toString());
            bw.flush();

            ObjectInputStream ois = new ObjectInputStream(http.getInputStream());
            ret = (User) ois.readObject();
            if (os != null)  os.close();
            if (osw != null)  osw.close();
            if (bw != null)  bw.close();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static boolean isUsernameExist(String username) {
        boolean flag=false;
        try {
            URL url = new URL(basicurl+"UsernameExist");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setDoInput(true);  //可读可写
            http.setDoOutput(true);
            http.setUseCaches(false);  //不允许使用缓存
            http.setRequestMethod("POST");  //设置传输方式为 get
            http.connect();  //创建连接

            OutputStream os = http.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            JSONArray jsonArray = new JSONArray();  //创建 json 对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username",username);
            jsonArray.put(jsonObject);
            bw.write(jsonArray.toString());
            bw.flush();

            InputStream is = http.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String result = br.readLine();  //获取web 端返回的数据
            if(result.equals("true")) {
                flag = true;
            }

            if (os != null)  os.close();
            if (osw != null)  osw.close();
            if (is != null)  is.close();
            if (isr != null)  isr.close();
            if (br != null)  br.close();
            if (bw != null)  bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static boolean addUser(String username,String password,boolean admin) {
        boolean flag=false;
        try {
            URL url = new URL(basicurl+"AddUser");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setDoInput(true);  //可读可写
            http.setDoOutput(true);
            http.setUseCaches(false);  //不允许使用缓存
            http.setRequestMethod("POST");  //设置传输方式为 get
            http.connect();  //创建连接

            OutputStream os = http.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            JSONArray jsonArray = new JSONArray();  //创建 json 对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username",username);
            jsonObject.put("password",password);
            jsonObject.put("admin",admin);
            jsonArray.put(jsonObject);
            bw.write(jsonArray.toString());
            bw.flush();

            InputStream is = http.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String result = br.readLine();  //获取web 端返回的数据
            if(result.equals("true")) {
                flag = true;
            }

            if (os != null)  os.close();
            if (osw != null)  osw.close();
            if (is != null)  is.close();
            if (isr != null)  isr.close();
            if (br != null)  br.close();
            if (bw != null)  bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static CanteenDetail queryCanteen(int canteenid) {
        CanteenDetail ret = new CanteenDetail();
        try {
            URL url = new URL(basicurl+"QueryCanteenid");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setDoInput(true);  //可读可写
            http.setDoOutput(true);
            http.setUseCaches(false);  //不允许使用缓存
            http.setRequestMethod("POST");  //设置传输方式为 get
            http.connect();  //创建连接

            OutputStream os = http.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            JSONArray jsonArray = new JSONArray();  //创建 json 对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("canteenid",canteenid);
            jsonArray.put(jsonObject);
            bw.write(jsonArray.toString());
            bw.flush();

            ObjectInputStream ois = new ObjectInputStream(http.getInputStream());
            ret = (CanteenDetail) ois.readObject();
            if (os != null)  os.close();
            if (osw != null)  osw.close();
            if (bw != null)  bw.close();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static Dish queryDish(int canteenid,int dishid) {
        Dish ret = new Dish();
        try {
            URL url = new URL(basicurl+"QueryDish");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setDoInput(true);  //可读可写
            http.setDoOutput(true);
            http.setUseCaches(false);  //不允许使用缓存
            http.setRequestMethod("POST");  //设置传输方式为 get
            http.connect();  //创建连接

            OutputStream os = http.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            JSONArray jsonArray = new JSONArray();  //创建 json 对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("canteenid",canteenid);
            jsonObject.put("dishid",dishid);
            jsonArray.put(jsonObject);
            bw.write(jsonArray.toString());
            bw.flush();

            ObjectInputStream ois = new ObjectInputStream(http.getInputStream());
            ret = (Dish) ois.readObject();
            if (os != null)  os.close();
            if (osw != null)  osw.close();
            if (bw != null)  bw.close();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static boolean isPasswordTrue(String username,String password) {
        boolean flag=false;
        try {
            URL url = new URL(basicurl+"LogLet");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setDoInput(true);  //可读可写
            http.setDoOutput(true);
            http.setUseCaches(false);  //不允许使用缓存
            http.setRequestMethod("POST");  //设置传输方式为 get
            http.connect();  //创建连接

            OutputStream os = http.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            JSONArray jsonArray = new JSONArray();  //创建 json 对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username",username);
            jsonObject.put("password",password);
            jsonArray.put(jsonObject);
            bw.write(jsonArray.toString());
            bw.flush();

            InputStream is = http.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String result = br.readLine();  //获取web 端返回的数据
            if(result.equals("true")) {
                flag = true;
            }

            if (os != null)  os.close();
            if (osw != null)  osw.close();
            if (is != null)  is.close();
            if (isr != null)  isr.close();
            if (br != null)  br.close();
            if (bw != null)  bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static ArrayList<CanteenDetail> allCantainDetail() {
        ArrayList<CanteenDetail> ret= new ArrayList<>();
        try {
            URL url = new URL(basicurl+"QueryCanteen");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setDoInput(true);  //可读可写
            http.setDoOutput(true);
            http.setUseCaches(false);  //不允许使用缓存
            http.setRequestMethod("POST");  //设置传输方式为 get
            http.connect();  //创建连接
            ObjectInputStream ois = new ObjectInputStream(http.getInputStream());
            ret = (ArrayList<CanteenDetail>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static ArrayList<CanteenDetail> adminCantainDetail(int adminid) {
        ArrayList<CanteenDetail> ret= new ArrayList<>();
        try {
            URL url = new URL(basicurl+"AdminQueryCanteen");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setDoInput(true);  //可读可写
            http.setDoOutput(true);
            http.setUseCaches(false);  //不允许使用缓存
            http.setRequestMethod("POST");  //设置传输方式为 get
            http.connect();  //创建连接

            OutputStream os = http.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            JSONArray jsonArray = new JSONArray();  //创建 json 对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("adminid",adminid);
            jsonArray.put(jsonObject);
            bw.write(jsonArray.toString());
            bw.flush();

            ObjectInputStream ois = new ObjectInputStream(http.getInputStream());
            ret = (ArrayList<CanteenDetail>) ois.readObject();
            if (os != null)  os.close();
            if (osw != null)  osw.close();
            if (bw != null)  bw.close();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static boolean addCanteen(CanteenDetail canteenDetail) {
        boolean flag=false;
        try {
            URL url = new URL(basicurl+"AddCanteen");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setDoInput(true);  //可读可写
            http.setDoOutput(true);
            http.setRequestMethod("POST");  //设置传输方式为 get

            ObjectOutputStream oos = new ObjectOutputStream(http.getOutputStream());
            oos.writeObject(canteenDetail);

            InputStream is = http.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String result = br.readLine();  //获取web 端返回的数据
            if(result.equals("true")) {
                flag = true;
            }
            if (is != null)  is.close();
            if (isr != null)  isr.close();
            if (br != null)  br.close();
            oos.close();
            http.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static boolean updateCanteen(CanteenDetail canteenDetail) {
        boolean flag=false;
        try {
            URL url = new URL(basicurl+"UpdateCanteen");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setDoInput(true);  //可读可写
            http.setDoOutput(true);
            http.setRequestMethod("POST");  //设置传输方式为 get

            ObjectOutputStream oos = new ObjectOutputStream(http.getOutputStream());
            oos.writeObject(canteenDetail);

            InputStream is = http.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String result = br.readLine();  //获取web 端返回的数据
            if(result.equals("true")) {
                flag = true;
            }
            if (is != null)  is.close();
            if (isr != null)  isr.close();
            if (br != null)  br.close();
            oos.close();
            http.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static boolean addDish(Dish dish) {
        boolean flag=false;
        try {
            URL url = new URL(basicurl+"AddDish");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setDoInput(true);  //可读可写
            http.setDoOutput(true);
            http.setRequestMethod("POST");  //设置传输方式为 get

            ObjectOutputStream oos = new ObjectOutputStream(http.getOutputStream());
            oos.writeObject(dish);

            InputStream is = http.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String result = br.readLine();  //获取web 端返回的数据
            if(result.equals("true")) {
                flag = true;
            }
            if (is != null)  is.close();
            if (isr != null)  isr.close();
            if (br != null)  br.close();
            if (oos != null) oos.close();
            http.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static boolean deleteDish(int canteenid,int dishid) {
        boolean flag=false;
        try {
            URL url = new URL(basicurl+"DeleteDish");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setDoInput(true);  //可读可写
            http.setDoOutput(true);
            http.setUseCaches(false);  //不允许使用缓存
            http.setRequestMethod("POST");  //设置传输方式为 get
            http.connect();  //创建连接

            OutputStream os = http.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            JSONArray jsonArray = new JSONArray();  //创建 json 对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("canteenid",canteenid);
            jsonObject.put("dishid",dishid);
            jsonArray.put(jsonObject);
            bw.write(jsonArray.toString());
            bw.flush();

            InputStream is = http.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String result = br.readLine();  //获取web 端返回的数据
            if(result.equals("true")) {
                flag = true;
            }

            if (os != null)  os.close();
            if (osw != null)  osw.close();
            if (is != null)  is.close();
            if (isr != null)  isr.close();
            if (br != null)  br.close();
            if (bw != null)  bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static boolean updateDish(Dish dish) {
        boolean flag=false;
        try {
            URL url = new URL(basicurl+"UpdateDish");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setDoInput(true);  //可读可写
            http.setDoOutput(true);
            http.setRequestMethod("POST");  //设置传输方式为 get

            ObjectOutputStream oos = new ObjectOutputStream(http.getOutputStream());
            oos.writeObject(dish);

            InputStream is = http.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String result = br.readLine();  //获取web 端返回的数据
            if(result.equals("true")) {
                flag = true;
            }
            if (is != null)  is.close();
            if (isr != null)  isr.close();
            if (br != null)  br.close();
            if (oos != null) oos.close();
            http.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static ArrayList<Dish> listDish(int canteenid,char option) {
        ArrayList<Dish> ret= new ArrayList<>();
        try {
            URL url = new URL(basicurl+"ListDish");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setDoInput(true);  //可读可写
            http.setDoOutput(true);
            http.setUseCaches(false);  //不允许使用缓存
            http.setRequestMethod("POST");  //设置传输方式为 get
            http.connect();  //创建连接

            OutputStream os = http.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            JSONArray jsonArray = new JSONArray();  //创建 json 对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("canteenid",canteenid);
            jsonObject.put("option",option);
            jsonArray.put(jsonObject);
            bw.write(jsonArray.toString());
            bw.flush();

            ObjectInputStream ois = new ObjectInputStream(http.getInputStream());
            ret = (ArrayList<Dish>) ois.readObject();
            if (os != null)  os.close();
            if (osw != null)  osw.close();
            if (bw != null)  bw.close();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    //添加或更新菜单
    public static boolean addMenu(int canteenid, Date date, int[] menu, char option) {
        boolean flag=false;
        try {
            URL url = new URL(basicurl+"AddMenu");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setDoInput(true);  //可读可写
            http.setDoOutput(true);
            http.setUseCaches(false);  //不允许使用缓存
            http.setRequestMethod("POST");  //设置传输方式为 get
            http.connect();  //创建连接

            OutputStream os = http.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            JSONArray jsonArray = new JSONArray();  //创建 json 对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("canteenid",canteenid);
            jsonObject.put("date",date);
            Arrays.sort(menu);
            String menustr = patternReverse(menu);
            jsonObject.put("menu",menustr);
            jsonObject.put("option",option);
            jsonArray.put(jsonObject);
            bw.write(jsonArray.toString());
            bw.flush();

            InputStream is = http.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String result = br.readLine();  //获取web 端返回的数据
            if(result.equals("true")) {
                flag = true;
            }

            if (os != null)  os.close();
            if (osw != null)  osw.close();
            if (is != null)  is.close();
            if (isr != null)  isr.close();
            if (br != null)  br.close();
            if (bw != null)  bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static ArrayList<Dish> listMenu(int canteenid,Date date,char option) {
        ArrayList<Dish> ret= new ArrayList<>();
        try {
            URL url = new URL(basicurl+"ListMenu");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setDoInput(true);  //可读可写
            http.setDoOutput(true);
            http.setUseCaches(false);  //不允许使用缓存
            http.setRequestMethod("POST");  //设置传输方式为 get
            http.connect();  //创建连接

            OutputStream os = http.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            JSONArray jsonArray = new JSONArray();  //创建 json 对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("canteenid",canteenid);
            jsonObject.put("date",date);
            jsonObject.put("option",option);
            jsonArray.put(jsonObject);
            bw.write(jsonArray.toString());
            bw.flush();

            ObjectInputStream ois = new ObjectInputStream(http.getInputStream());
            ret = (ArrayList<Dish>) ois.readObject();
            if (os != null)  os.close();
            if (osw != null)  osw.close();
            if (bw != null)  bw.close();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static boolean commitBook(int canteenid,Date date,char option,int userid,int[] dishid,int[] cnt) {
        boolean flag=false;
        try {
            URL url = new URL(basicurl+"CommitBook");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setDoInput(true);  //可读可写
            http.setDoOutput(true);
            http.setUseCaches(false);  //不允许使用缓存
            http.setRequestMethod("POST");  //设置传输方式为 get
            http.connect();  //创建连接

            OutputStream os = http.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            JSONArray jsonArray = new JSONArray();  //创建 json 对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("canteenid",canteenid);
            jsonObject.put("date",date);
            jsonObject.put("option",option);
            jsonObject.put("userid",userid);
            String dishidstr = patternReverse(dishid);
            jsonObject.put("dishid",dishidstr);
            String cntstr = patternReverse(cnt);
            jsonObject.put("cnt",cntstr);
            jsonArray.put(jsonObject);
            bw.write(jsonArray.toString());
            bw.flush();

            InputStream is = http.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String result = br.readLine();  //获取web 端返回的数据
            if(result.equals("true")) {
                flag = true;
            }

            if (os != null)  os.close();
            if (osw != null)  osw.close();
            if (is != null)  is.close();
            if (isr != null)  isr.close();
            if (br != null)  br.close();
            if (bw != null)  bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static ArrayList<String> adminBookQuery(int canteenid,Date date,char option) {
        ArrayList<String> ret= new ArrayList<>();
        try {
            URL url = new URL(basicurl+"AdminBookQuery");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setDoInput(true);  //可读可写
            http.setDoOutput(true);
            http.setUseCaches(false);  //不允许使用缓存
            http.setRequestMethod("POST");  //设置传输方式为 get
            http.connect();  //创建连接

            OutputStream os = http.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            JSONArray jsonArray = new JSONArray();  //创建 json 对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("canteenid",canteenid);
            jsonObject.put("date",date);
            jsonObject.put("option",option);
            jsonArray.put(jsonObject);
            bw.write(jsonArray.toString());
            bw.flush();

            ObjectInputStream ois = new ObjectInputStream(http.getInputStream());
            ret = (ArrayList<String>) ois.readObject();
            if (os != null)  os.close();
            if (osw != null)  osw.close();
            if (bw != null)  bw.close();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static boolean commitEva(Evaluation eva) {
        boolean flag=false;
        try {
            URL url = new URL(basicurl+"CommitEva");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setDoInput(true);  //可读可写
            http.setDoOutput(true);
            http.setRequestMethod("POST");  //设置传输方式为 get

            ObjectOutputStream oos = new ObjectOutputStream(http.getOutputStream());
            oos.writeObject(eva);

            InputStream is = http.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String result = br.readLine();  //获取web 端返回的数据
            if(result.equals("true")) {
                flag = true;
            }
            if (is != null)  is.close();
            if (isr != null)  isr.close();
            if (br != null)  br.close();
            oos.close();
            http.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static ArrayList<Evaluation> dishEvaQuery(int canteenid,int dishid) {
        ArrayList<Evaluation> ret= new ArrayList<>();
        try {
            URL url = new URL(basicurl+"DishEvaQuery");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setDoInput(true);  //可读可写
            http.setDoOutput(true);
            http.setUseCaches(false);  //不允许使用缓存
            http.setRequestMethod("POST");  //设置传输方式为 get
            http.connect();  //创建连接

            OutputStream os = http.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            JSONArray jsonArray = new JSONArray();  //创建 json 对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("canteenid",canteenid);
            jsonObject.put("dishid",dishid);
            jsonArray.put(jsonObject);
            bw.write(jsonArray.toString());
            bw.flush();

            ObjectInputStream ois = new ObjectInputStream(http.getInputStream());
            ret = (ArrayList<Evaluation>) ois.readObject();
            if (os != null)  os.close();
            if (osw != null)  osw.close();
            if (bw != null)  bw.close();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static ArrayList<Book> userBookQuery(int userid) {
        ArrayList<Book> ret= new ArrayList<>();
        try {
            URL url = new URL(basicurl+"UserBookQuery");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setDoInput(true);  //可读可写
            http.setDoOutput(true);
            http.setUseCaches(false);  //不允许使用缓存
            http.setRequestMethod("POST");  //设置传输方式为 get
            http.connect();  //创建连接

            OutputStream os = http.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            JSONArray jsonArray = new JSONArray();  //创建 json 对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userid",userid);
            jsonArray.put(jsonObject);
            bw.write(jsonArray.toString());
            bw.flush();

            ObjectInputStream ois = new ObjectInputStream(http.getInputStream());
            ret = (ArrayList<Book>) ois.readObject();
            if (os != null)  os.close();
            if (osw != null)  osw.close();
            if (bw != null)  bw.close();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static ArrayList<Evaluation> userEvaQuery(String username) {
        ArrayList<Evaluation> ret= new ArrayList<>();
        try {
            URL url = new URL(basicurl+"UserEvaQuery");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setDoInput(true);  //可读可写
            http.setDoOutput(true);
            http.setUseCaches(false);  //不允许使用缓存
            http.setRequestMethod("POST");  //设置传输方式为 get
            http.connect();  //创建连接

            OutputStream os = http.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            JSONArray jsonArray = new JSONArray();  //创建 json 对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username",username);
            jsonArray.put(jsonObject);
            bw.write(jsonArray.toString());
            bw.flush();

            ObjectInputStream ois = new ObjectInputStream(http.getInputStream());
            ret = (ArrayList<Evaluation>) ois.readObject();
            if (os != null)  os.close();
            if (osw != null)  osw.close();
            if (bw != null)  bw.close();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static boolean commitSBook(SpecialBook specialBook) {
        boolean flag=false;
        try {
            URL url = new URL(basicurl+"CommitSBook");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setDoInput(true);  //可读可写
            http.setDoOutput(true);
            http.setRequestMethod("POST");  //设置传输方式为 get

            ObjectOutputStream oos = new ObjectOutputStream(http.getOutputStream());
            oos.writeObject(specialBook);

            InputStream is = http.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String result = br.readLine();  //获取web 端返回的数据
            if(result.equals("true")) {
                flag = true;
            }
            if (is != null)  is.close();
            if (isr != null)  isr.close();
            if (br != null)  br.close();
            oos.close();
            http.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static ArrayList<SpecialBook> userSbookQuery(String username) {
        ArrayList<SpecialBook> ret= new ArrayList<>();
        try {
            URL url = new URL(basicurl+"UserSbookQuery");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setDoInput(true);  //可读可写
            http.setDoOutput(true);
            http.setUseCaches(false);  //不允许使用缓存
            http.setRequestMethod("POST");  //设置传输方式为 get
            http.connect();  //创建连接

            OutputStream os = http.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            JSONArray jsonArray = new JSONArray();  //创建 json 对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username",username);
            jsonArray.put(jsonObject);
            bw.write(jsonArray.toString());
            bw.flush();

            ObjectInputStream ois = new ObjectInputStream(http.getInputStream());
            ret = (ArrayList<SpecialBook>) ois.readObject();
            if (os != null)  os.close();
            if (osw != null)  osw.close();
            if (bw != null)  bw.close();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static ArrayList<SpecialBook> adminSbookQuery(int adminid) {
        ArrayList<SpecialBook> ret= new ArrayList<>();
        try {
            URL url = new URL(basicurl+"AdminSbookQuery");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setDoInput(true);  //可读可写
            http.setDoOutput(true);
            http.setUseCaches(false);  //不允许使用缓存
            http.setRequestMethod("POST");  //设置传输方式为 get
            http.connect();  //创建连接

            OutputStream os = http.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            JSONArray jsonArray = new JSONArray();  //创建 json 对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("adminid",adminid);
            jsonArray.put(jsonObject);
            bw.write(jsonArray.toString());
            bw.flush();

            ObjectInputStream ois = new ObjectInputStream(http.getInputStream());
            ret = (ArrayList<SpecialBook>) ois.readObject();
            if (os != null)  os.close();
            if (osw != null)  osw.close();
            if (bw != null)  bw.close();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static boolean dealBook(int sbookid,String deal) {
        boolean flag=false;
        try {
            URL url = new URL(basicurl+"DealBook");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setDoInput(true);  //可读可写
            http.setDoOutput(true);
            http.setUseCaches(false);  //不允许使用缓存
            http.setRequestMethod("POST");  //设置传输方式为 get
            http.connect();  //创建连接

            OutputStream os = http.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            JSONArray jsonArray = new JSONArray();  //创建 json 对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sbookid",sbookid);
            jsonObject.put("deal",deal);
            jsonArray.put(jsonObject);
            bw.write(jsonArray.toString());
            bw.flush();

            InputStream is = http.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String result = br.readLine();  //获取web 端返回的数据
            if(result.equals("true")) {
                flag = true;
            }

            if (os != null)  os.close();
            if (osw != null)  osw.close();
            if (is != null)  is.close();
            if (isr != null)  isr.close();
            if (br != null)  br.close();
            if (bw != null)  bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static ArrayList<Article> articleList() {
        ArrayList<Article> ret= new ArrayList<>();
        try {
            URL url = new URL(basicurl+"ArticleList");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setDoInput(true);  //可读可写
            http.setDoOutput(true);
            http.setUseCaches(false);  //不允许使用缓存
            http.setRequestMethod("POST");  //设置传输方式为 get
            http.connect();  //创建连接
            ObjectInputStream ois = new ObjectInputStream(http.getInputStream());
            ret = (ArrayList<Article>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    //菜单显示的格式转换
    static int[] patternChange(String str) {
        String[] arrStr=str.split("_");
        int[] ret=new int[arrStr.length];
        for (int i = 0;i < ret.length;i ++) {
            ret[i] = Integer.parseInt(arrStr[i]);
        }
        return ret;
    }

    static String patternReverse(int[] arrMenu) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i<arrMenu.length;i ++) {
            sb.append(arrMenu[i]);
            sb.append("_");
        }
        return sb.toString();
    }

    static String[] strpatternChange(String str) {
        return str.split("_");
    }

    static String strpatternReverse(String[] arrStr) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i<arrStr.length;i ++) {
            sb.append(arrStr[i]);
            sb.append("_");
        }
        return sb.toString();
    }
}
