package com.example.zhou.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

public class TestMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testmenu);



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
