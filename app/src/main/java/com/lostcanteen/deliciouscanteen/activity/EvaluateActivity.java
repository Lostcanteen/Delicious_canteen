package com.lostcanteen.deliciouscanteen.activity;

import com.lostcanteen.deliciouscanteen.WebTrans;
import com.lostcanteen.deliciouscanteen.Dish;
import com.lostcanteen.deliciouscanteen.Evaluation;
import com.lostcanteen.deliciouscanteen.R;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;

public class EvaluateActivity extends AppCompatActivity {



    private TextView evaluateTitle;
    private Toolbar evaluateToolbar;

    private TextView foodName;
    private TextView foodPrice;
    private ImageView foodImage;

    private RatingBar foodStars;
    private EditText foodEvaluate;
    private Button submitEvaluate;


    private int starsNum;
    private String evaluateText;

    private Dish food;
    private int canteenId;
    private Date date;
    private String username;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);


        food = (Dish) getIntent().getSerializableExtra("transFood");
        canteenId = (Integer) getIntent().getIntExtra("transCanteenId",-1);
        username = (String) getIntent().getStringExtra("username");

        date = (Date) getIntent().getSerializableExtra("Date");

        evaluateTitle = (TextView) findViewById(R.id.textEvaluateTitle);
        evaluateToolbar = (Toolbar) findViewById(R.id.evaluate_toolbar);
        foodName = (TextView) findViewById(R.id.testFood_name);
        foodImage = (ImageView)findViewById(R.id.testFoodImage);
        foodPrice = (TextView)findViewById(R.id.testFood_price);
        foodStars = (RatingBar)findViewById(R.id.foodStars);
        foodEvaluate = (EditText)findViewById(R.id.textEvalaute);
        submitEvaluate = (Button)findViewById(R.id.btnRight);

        evaluateTitle.setText("评价");
        setTitle("");
        setSupportActionBar(evaluateToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        foodName.setText(food.getName());
        foodPrice.setText(((Float)food.getPrice()).toString());
        if(food.getDishid()==0)
            foodPrice.setText("");
        //foodImage.setImageResource(food.getFoodImageId());
        Picasso.with(this)
                .load(food.getImage())
                .placeholder(R.color.white)
                .error(R.drawable.logo)
                .into(foodImage);

        submitEvaluate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                starsNum = ((Float)foodStars.getRating()).intValue();
                evaluateText = foodEvaluate.getText().toString();
                if(!evaluateText.isEmpty())
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("username",username);
                            Evaluation myEvluation = new Evaluation(canteenId,food.getDishid(),username,starsNum,date,evaluateText);
                            WebTrans.commitEva(myEvluation);
                            finish();
                        }
                    }).start();

                }
                else
                {
                    Toast.makeText(EvaluateActivity.this
                    ,"评价不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
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


}
