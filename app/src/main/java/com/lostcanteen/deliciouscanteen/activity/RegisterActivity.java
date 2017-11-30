package com.lostcanteen.deliciouscanteen.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lostcanteen.deliciouscanteen.R;
import com.lostcanteen.deliciouscanteen.WebTrans;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

    private Toolbar registerToolbar;
    private TextView registerTitle;
    private EditText userName;
    private EditText passWord;
    private TextView lengthError;
    private Button registerButton;
    private RadioGroup typeSelect;
    private RadioButton typeAd;
    private RadioButton typeNormal;


    private boolean isQualified;
    private boolean isadmin;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what)
            {
                case 1:
                    Toast.makeText(
                            RegisterActivity.this, "用户已经存在了", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    Toast.makeText(
                            RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    break;

                default:
            }
        }
    };
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerToolbar = (Toolbar)findViewById(R.id.register_toolbar);
        registerTitle =(TextView) findViewById(R.id.registerTitle);
        userName = (EditText) findViewById(R.id.registerUsername);
        passWord = (EditText) findViewById(R.id.registerPassword);
        lengthError = (TextView) findViewById(R.id.lengthError);
        registerButton = (Button) findViewById(R.id.registerButton);
        typeSelect = (RadioGroup) findViewById(R.id.typeSelect);
        typeAd = (RadioButton) findViewById(R.id.typeAd);
        typeNormal = (RadioButton) findViewById(R.id.typeNormal);

        registerTitle.setText("注册");
        setTitle("");
        setSupportActionBar(registerToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        typeSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int select = radioGroup.getCheckedRadioButtonId();
                if(select == R.id.typeAd)
                {
                    isadmin = true;
                }
                else
                {
                    isadmin = false;
                }
            }
        });

        passWord.addTextChangedListener(new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
                if(passWord.getText().length() <4)
                {
                    lengthError.setText(R.string.lengthError);
                    isQualified = false;
                }
                else
                {
                    lengthError.setText("");
                    isQualified = true;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // 输入前的监听

            }
            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Boolean isRepeat = WebTrans.isUsernameExist(userName.getText().toString());
                        if(isQualified)
                        {
                            Message message = new Message();
                            if(isRepeat)
                            {
                                message.what = 1;
                                handler.sendMessage(message);
                            }
                            else
                            {
                                WebTrans.addUser(userName.getText().toString(),passWord.getText().toString(),isadmin);
                                message.what = 0;
                                handler.sendMessage(message);

                                finish();
                            }

                        }
                    }
                }).start();

            }
        });


    }






}
