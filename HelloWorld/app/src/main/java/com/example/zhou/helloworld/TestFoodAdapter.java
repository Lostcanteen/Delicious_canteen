package com.example.zhou.helloworld;

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

public class TestFoodAdapter extends RecyclerView.Adapter<TestFoodAdapter.ViewHolder> {
    private List<Food> foodList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView foodImage;
        TextView foodName;
        TextView foodPrice;
        public ViewHolder(View view){
            super(view);
            //*************在这里实例化
            foodImage = (ImageView) view.findViewById(R.id.testFoodImage);
            foodName = (TextView) view.findViewById(R.id.testFood_name);
            foodPrice = (TextView) view.findViewById(R.id.testFood_price);
        }
    }
    public TestFoodAdapter(List<Food> foodList){this.foodList = foodList;}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.testfood,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });
        return  holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position) {
        Food food = foodList.get(position);
        holder.foodImage.setImageResource(food.getFoodImageId());
        holder.foodName.setText(food.getFoodIntroduction());
        holder.foodPrice.setText(((Float)food.getFoodPrice()).toString());
    }

    @Override
    public int getItemCount(){
        return foodList.size();
    }
}
