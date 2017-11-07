package com.example.zhou.helloworld;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhou on 2017/11/1.
 */

public class Food implements Parcelable {
    private int foodId;
    private int foodImageId;
    private float foodPrice;
    private String foodName;
    private int whatFood ;

    public int num = 0;

    public Food(){}
    public Food(int foodId,int foodImageId,float foodPrice,String foodName,int whatFood){
        this.foodId = foodId;
        this.foodImageId = foodImageId;
        this.foodPrice = foodPrice;
        this.foodName = foodName;
        this.whatFood = whatFood;
    }

    public int getFoodId(){return foodId;}
    public int getFoodImageId(){
        return foodImageId;
    }
    public float getFoodPrice(){
        return foodPrice;
    }
    public String getFoodName(){
        return foodName;
    }
    public int getWhatFood(){return whatFood;}




    @Override
    public int describeContents(){
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(foodId);
        dest.writeInt(foodImageId);
        dest.writeFloat(foodPrice);
        dest.writeString(foodName);
        dest.writeInt(whatFood);
        dest.writeInt(num);
    }
    public static final Parcelable.Creator<Food> CREATOR = new Parcelable.
            Creator<Food>(){
        @Override
        public Food createFromParcel(Parcel source){
            Food food = new Food();
            food.foodId = source.readInt();
            food.foodImageId = source.readInt();
            food.foodPrice = source.readFloat();
            food.foodName = source.readString();
            food.whatFood = source.readInt();
            food.num = source.readInt();
            return food;
        }
        @Override
        public Food[] newArray(int size){
            return new Food[size];
        }
    };


}
