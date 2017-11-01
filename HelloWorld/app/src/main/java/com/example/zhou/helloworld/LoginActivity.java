package com.example.zhou.helloworld;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.preference.PreferenceManager;

public class LoginActivity extends AppCompatActivity {
    private EditText myPassword;
    private EditText myEmail;
    private Button loginButton;

    SharedPreferences sprfMain;
    SharedPreferences.Editor editorMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sprfMain= PreferenceManager.getDefaultSharedPreferences(this);
        editorMain = sprfMain.edit();
        editorMain.putBoolean("main",false);
        editorMain.commit();
        //.getBoolean("main",false)；当找不到"main"所对应的键值是默认返回false
        if(sprfMain.getBoolean("main",false)){
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        }

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
                if(true) { //加逻辑判断用户登录
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("loginUsername",myEmail.getText().toString());
                    editorMain.putBoolean("main",true);
                    editorMain.commit();

                    startActivity(intent);
                }
                else
                {

                }
            }
            else {
                Toast.makeText(LoginActivity.this, "Your password is null", Toast.LENGTH_SHORT).show();
            }
        }
        else// 空的时候提示
        {
            Toast.makeText(LoginActivity.this, "Your email is null", Toast.LENGTH_SHORT).show();
        }
    }
}
