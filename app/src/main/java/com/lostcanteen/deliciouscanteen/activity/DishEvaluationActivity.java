package com.lostcanteen.deliciouscanteen.activity;


import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.lostcanteen.deliciouscanteen.Dish;
import com.lostcanteen.deliciouscanteen.Evaluation;
import com.lostcanteen.deliciouscanteen.R;
import com.lostcanteen.deliciouscanteen.WebTrans;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DishEvaluationActivity extends AppCompatActivity {
    private ImageView foodImage;
    private Toolbar evaluationToolBar;
    private TextView title;
    private TextView foodName;
    private TextView foodPrice;
    private RecyclerView evaluationRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private EvaluationRecyclerViewAdapter adapter;

    private Dish food;
    private int canteenid;

    private List<Evaluation> dishEvaluationList = new ArrayList<>();

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what)
            {
                case 1:
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
        setContentView(R.layout.activity_dish_evaluation);
        food =(Dish) getIntent().getSerializableExtra("food");
        canteenid = getIntent().getIntExtra("canteenid",-1);

        evaluationToolBar = (Toolbar) findViewById(R.id.dishevaluationtoolbar);
        title = (TextView)findViewById(R.id.dishDetailTitle);

        title.setText("商品详情");
        setTitle("");
        setSupportActionBar(evaluationToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        foodImage = (ImageView) findViewById(R.id.dishfoodImage);
        foodName = (TextView)findViewById(R.id.dishfoodName);
        foodPrice = (TextView)findViewById(R.id.dishfoodPrice);

        evaluationRecyclerView = (RecyclerView)findViewById(R.id.evalutionList);
        linearLayoutManager = new LinearLayoutManager(this);
        evaluationRecyclerView.setLayoutManager(linearLayoutManager);

        foodName.setText(food.getName());
        foodPrice.setText(((Float)food.getPrice()).toString());

        Picasso.with(this)
                .load(food.getImage())
                .placeholder(R.color.white)
                .error(R.drawable.logo)
                .into(foodImage);
        new Thread(new Runnable() {
            @Override
            public void run() {
                dishEvaluationList = WebTrans.dishEvaQuery(canteenid,food.getDishid());
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);

            }
        }).start();

    }


    @Override
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
        public void onBindViewHolder(ViewHolder holder,int position) {
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
