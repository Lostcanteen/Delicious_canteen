package com.lostcanteen.deliciouscanteen.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lostcanteen.deliciouscanteen.CanteenDetail;
import com.lostcanteen.deliciouscanteen.Dish;
import com.lostcanteen.deliciouscanteen.R;
import com.lostcanteen.deliciouscanteen.WebTrans;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class DetailActivity extends AppCompatActivity {
    public static final int UPDATE_MAINDISH = 1;
    public static final int UPDATE_CANTEEN = 2;

    private TextView title;
    private Toolbar toolbar;
    private Button modifybutton;

    private TextView position;
    private TextView time;
    private TextView mainDish;
    private TextView suggestion;

    private int canteenDetailid;
    private String username;
    private boolean isadmin=false; //从前页传来
    private String dishstr;
    ArrayList<Dish> mainDishArr;
    private CanteenDetail canteenDetail;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_MAINDISH:
                    mainDish.setText(dishstr);
                    break;
                case UPDATE_CANTEEN:
                    title.setText(canteenDetail.getName());
                    setTitle("");
                    setSupportActionBar(toolbar);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    position.setText(canteenDetail.getLocation());
                    time.setText(canteenDetail.getHours());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        canteenDetailid =  getIntent().getIntExtra("canteenDetailid",-1);
        username = getIntent().getStringExtra("username");
        //isadmin = //从前页传来

        title = (TextView) findViewById(R.id.title);
        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        modifybutton = (Button) findViewById(R.id.modify);
        position = (TextView) findViewById(R.id.position);
        time = (TextView) findViewById(R.id.time);
        mainDish = (TextView) findViewById(R.id.mainDish);
        suggestion = (TextView) findViewById(R.id.suggestionTitle);

        if(!isadmin) {
            modifybutton.setVisibility(View.INVISIBLE);
            Drawable drawableright= getResources().getDrawable(R.mipmap.add_button);
            drawableright.setBounds(0, 0, drawableright.getMinimumWidth(), drawableright.getMinimumHeight());
            Drawable drawableleft = getResources().getDrawable(R.drawable.suggestion);
            drawableleft.setBounds(0, 0, drawableleft.getMinimumWidth(), drawableleft.getMinimumHeight());
            suggestion.setCompoundDrawables(drawableleft,null,drawableright,null);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                mainDishArr = WebTrans.listDish(canteenDetailid,'M');
                StringBuilder sb = new StringBuilder();
                for(Dish dish:mainDishArr) {
                    sb.append(dish.getName()+" ");
                }
                dishstr = sb.toString();
                Message message = new Message();
                message.what = UPDATE_MAINDISH;
                handler.sendMessage(message);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                canteenDetail= WebTrans.queryCanteen(canteenDetailid);
                Message message = new Message();
                message.what = UPDATE_CANTEEN;
                handler.sendMessage(message);
            }
        }).start();

        modifybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this,UpdateCanteenActivity.class);
                intent.putExtra("canteenDetail",canteenDetail);
                startActivity(intent);
                finish();
            }
        });

        suggestion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Drawable drawable = suggestion.getCompoundDrawables()[2];
                if(drawable==null) return false;
                if(motionEvent.getX()>suggestion.getWidth()-suggestion.getPaddingRight()-
                        drawable.getIntrinsicWidth()) {
                    Intent intent = new Intent(DetailActivity.this,EvaluateActivity.class);
                    Dish dish = new Dish(0,0,canteenDetail.getName(),canteenDetail.getPicture(),0,false,false,false,false);
                    intent.putExtra("transFood",dish);
                    intent.putExtra("transCanteenId",canteenDetail.getCanteenid());
                    intent.putExtra("username",username);
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    Date nowDate = java.sql.Date.valueOf(((Integer)year).toString()
                            +"-"+((Integer)(month+1)).toString() + "-"+((Integer)day).toString());
                    intent.putExtra("Date",nowDate);
                    startActivity(intent);
                }

                return false;
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
