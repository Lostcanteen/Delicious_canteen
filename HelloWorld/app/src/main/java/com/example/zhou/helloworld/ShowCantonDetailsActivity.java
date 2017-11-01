package com.example.zhou.helloworld;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ShowCantonDetailsActivity extends AppCompatActivity {

    public static void actionStart(Context context,CantonDetail  cantonDetail){
        Intent intent = new Intent(context,ShowCantonDetailsActivity.class);
        intent.putExtra("transCantonDetail",cantonDetail);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_canton_details);

        CantonDetail cantonDetail = (CantonDetail)getIntent().getParcelableExtra("transCantonDetail");

        Toolbar toolbar = (Toolbar) findViewById(R.id.show_detail_toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout)
                findViewById(R.id.canton_toolbar);
        ImageView cantonImageView = (ImageView) findViewById(R.id.this_canton_image);

        //TextView testText = (TextView) findViewById(R.id.test_text);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(cantonDetail.getCantonName());
        //代码给了一个Glide
        cantonImageView.setImageResource(cantonDetail.getCantonImageId());
        //testText.setText("测试");

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.classifyList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        TestMenuClassifyListAdapter adapter = new TestMenuClassifyListAdapter(getMeal());
        recyclerView.setAdapter(adapter);


        RecyclerView recyclerView1 = (RecyclerView) findViewById(R.id.foodList);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(linearLayoutManager1);
        TestFoodAdapter adapter1 = new TestFoodAdapter(getFood());
        recyclerView1.setAdapter(adapter1);


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

    public List<ClassifyMeal> getMeal(){
        List<ClassifyMeal> meals = new ArrayList<>();
        ClassifyMeal meal1 = new ClassifyMeal(R.mipmap.breakfastmeal,"早餐");
        ClassifyMeal meal2 = new ClassifyMeal(R.mipmap.lunchmeal,"午餐");
        ClassifyMeal meal3 = new ClassifyMeal(R.mipmap.dinner,"晚餐");
        ClassifyMeal meal4 = new ClassifyMeal(R.mipmap.specialmeal,"特色菜");
        meals.add(meal1);
        meals.add(meal2);
        meals.add(meal3);
        meals.add(meal4);

        return  meals;
    }

    public List<Food> getFood(){
        List<Food> foods = new ArrayList<>();
        for(int i =0 ;i<10;i++)
        {
            Food food = new Food(R.drawable.logo,i,"a"+i);
            foods.add(food);

        }
        return foods;
    }

}
