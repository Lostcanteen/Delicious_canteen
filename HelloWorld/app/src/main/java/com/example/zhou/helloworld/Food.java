package com.example.zhou.helloworld;

/**
 * Created by zhou on 2017/11/1.
 */

public class Food {
    private int foodImageId;
    private float foodPrice;
    private String foodIntroduction;

    public Food(){}
    public Food(int foodImageId,float foodPrice,String foodIntroduction){
        this.foodImageId = foodImageId;
        this.foodPrice = foodPrice;
        this.foodIntroduction = foodIntroduction;
    }

    public int getFoodImageId(){
        return foodImageId;
    }
    public float getFoodPrice(){
        return foodPrice;
    }
    public String getFoodIntroduction(){
        return foodIntroduction;
    }
}
