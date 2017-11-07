package com.example.zhou.helloworld;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class EvaluateActivity extends AppCompatActivity {

    public static void actionStart(Context context, Food food){
        Intent intent = new Intent(context,EvaluateActivity.class);
        intent.putExtra("transFood",food);
        context.startActivity(intent);
    }

    private TextView evaluateTitle;
    private Toolbar evaluateToolbar;

    private TextView foodName;
    private TextView foodPrice;
    private ImageView foodImage;

    private RatingBar foodStars;
    private EditText foodEvaluate;
    private Button submitEvaluate;


    private float starsNum;
    private String evaluateText;

    private Food food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);


        food = (Food) getIntent().getParcelableExtra("transFood");

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

        foodName.setText(food.getFoodName());
        foodPrice.setText(((Float)food.getFoodPrice()).toString());
        foodImage.setImageResource(food.getFoodImageId());

        submitEvaluate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                starsNum = foodStars.getRating();
                evaluateText = foodEvaluate.getText().toString();
                Log.d("ssss",((Float)starsNum).toString());
                Log.d("sss",evaluateText);
                finish();
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
