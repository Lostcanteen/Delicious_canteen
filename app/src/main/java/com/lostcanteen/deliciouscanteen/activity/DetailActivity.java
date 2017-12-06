package com.lostcanteen.deliciouscanteen.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lostcanteen.deliciouscanteen.CanteenDetail;
import com.lostcanteen.deliciouscanteen.Dish;
import com.lostcanteen.deliciouscanteen.Evaluation;
import com.lostcanteen.deliciouscanteen.R;
import com.lostcanteen.deliciouscanteen.SpecialBook;
import com.lostcanteen.deliciouscanteen.WebTrans;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    public static final int UPDATE_MAINDISH = 1;
    public static final int UPDATE_CANTEEN = 2;
    public static final int UPDATE_EVALUATION=3;

    private TextView title;
    private Toolbar toolbar;
    private Button modifybutton;

    private TextView position;
    private TextView time;
    private TextView mainDishTitle;
    private TextView mainDish;
    private TextView suggestion;

    private int canteenDetailid;
    private String username;
    private boolean isadmin;
    private String dishstr;
    ArrayList<Dish> mainDishArr;
    private CanteenDetail canteenDetail;

    private List<Evaluation> dishEvaluationList = new ArrayList<>();
    private RecyclerView evaluationRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private EvaluationRecyclerViewAdapter adapter;

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
                    break;
                case UPDATE_EVALUATION:
                    adapter = new EvaluationRecyclerViewAdapter(dishEvaluationList);
                    evaluationRecyclerView.setAdapter(adapter);
                    break;
                default:
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        canteenDetailid =  getIntent().getIntExtra("canteenDetailid",-1);
        username = getIntent().getStringExtra("username");
        isadmin =  getIntent().getBooleanExtra("isAdmin",false);

        title = (TextView) findViewById(R.id.title);
        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        modifybutton = (Button) findViewById(R.id.modify);
        position = (TextView) findViewById(R.id.position);
        time = (TextView) findViewById(R.id.time);
        mainDishTitle = (TextView) findViewById(R.id.mainDishTitle);
        mainDish = (TextView) findViewById(R.id.mainDish);
        suggestion = (TextView) findViewById(R.id.suggestionTitle);
        evaluationRecyclerView = (RecyclerView)findViewById(R.id.canteenevalutionList);
        linearLayoutManager = new LinearLayoutManager(this);
        evaluationRecyclerView.setLayoutManager(linearLayoutManager);

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
                dishEvaluationList = WebTrans.dishEvaQuery(canteenDetailid,0);
                Message message = new Message();
                message.what = UPDATE_EVALUATION;
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

        mainDishTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this,MainDishListActivity.class);
                intent.putExtra("dishlist",mainDishArr);
                startActivity(intent);
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

    class EvaluationRecyclerViewAdapter extends RecyclerView.Adapter<EvaluationRecyclerViewAdapter.ViewHolder>{
        private List<Evaluation> dishEvaluationList;

        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView userImage;
            TextView userName;
            RatingBar dishStars;
            TextView userEvaluation;
            public ViewHolder(View view){
                super(view);
                userImage = view.findViewById(R.id.userImage);
                userName = view.findViewById(R.id.username);
                dishStars = view.findViewById(R.id.dishStars);
                userEvaluation = view.findViewById(R.id.evalauteText);
            }
        }
        public EvaluationRecyclerViewAdapter(List<Evaluation> dishEvaluationList){
            this.dishEvaluationList = dishEvaluationList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_evaluation_listitem, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(EvaluationRecyclerViewAdapter.ViewHolder holder, int position) {
            //未来可实现用户头像拉取
            Evaluation evaluation = dishEvaluationList.get(position);

            holder.userName.setText(evaluation.getUsername()+"("+evaluation.getDate().toString()+")");
            holder.dishStars.setRating(evaluation.getStar());
            holder.userEvaluation.setText(evaluation.getComment());
        }

        @Override
        public int getItemCount(){
            return dishEvaluationList.size();
        }

    }
}
