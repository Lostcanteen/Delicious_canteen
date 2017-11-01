package com.example.zhou.helloworld;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zhou on 2017/11/1.
 */



public class TestMenuClassifyListAdapter extends RecyclerView.Adapter<TestMenuClassifyListAdapter.ViewHolder>{
    private List<ClassifyMeal> classifyList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mealImage;
        TextView mealName;
        public ViewHolder(View view){
            super(view);
            //*************在这里实例化
            mealImage = (ImageView) view.findViewById(R.id.testImage);
            mealName = (TextView) view.findViewById(R.id.testmeal_text);
        }
    }
    public TestMenuClassifyListAdapter(List<ClassifyMeal> classifyList){this.classifyList = classifyList;}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.testmeal,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){


            }
        });
        return  holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        ClassifyMeal meal = classifyList.get(position);
        holder.mealName.setText(meal.getClassifyName());
        holder.mealImage.setImageResource(meal.getClassifyImageId());
    }


    @Override
    public int getItemCount(){
        return classifyList.size();
    }
}
