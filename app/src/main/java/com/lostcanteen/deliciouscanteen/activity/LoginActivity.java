package com.lostcanteen.deliciouscanteen.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.inputmethod.EditorInfo;

import com.lostcanteen.deliciouscanteen.Dish;
import com.lostcanteen.deliciouscanteen.R;
import com.lostcanteen.deliciouscanteen.WebTrans;

import java.sql.Date;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    public static final int LOGIN_FAIL = 1;
    private EditText myPassword;
    private EditText myEmail;
    private Button loginButton;

//    SharedPreferences sprfMain;
//    SharedPreferences.Editor editorMain;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what)
            {
                case LOGIN_FAIL:
                    Toast.makeText(
                            LoginActivity.this, "用户不存在或密码错误", Toast.LENGTH_SHORT).show();
                    break;
                default:
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        sprfMain= PreferenceManager.getDefaultSharedPreferences(this);
//        editorMain = sprfMain.edit();
//        editorMain.putBoolean("main",false);
//        editorMain.commit();
//        //.getBoolean("main",false)；当找不到"main"所对应的键值是默认返回false
//        if(sprfMain.getBoolean("main",false)){
//            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
//            startActivity(intent);
//            LoginActivity.this.finish();
//        }

        setContentView(R.layout.login_layout);

        myEmail = (EditText) findViewById(R.id.loginUsername);
        myPassword = (EditText) findViewById(R.id.loginPassword);

        myPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();

            }
        });
    }

    private void attemptLogin(){
        if(!TextUtils.isEmpty(myEmail.getText().toString())) // Email不空的时候
        {
            if(!TextUtils.isEmpty(myPassword.getText().toString()))
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Boolean b = WebTrans.isPasswordTrue(
                                myEmail.getText().toString(), myPassword.getText().toString());

                        if(b) { //加逻辑判断用户登录
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("loginUsername",myEmail.getText().toString());
                            startActivity(intent);
                        }
                        else {
                            Message message = new Message();
                            message.what = LOGIN_FAIL;
                            handler.sendMessage(message);
                        }
                    }
                }).start();
            }
            else {
                Toast.makeText(
                        LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            }
        }
        else// 空的时候提示
        {
            Toast.makeText(
                    LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
        }
    }
}