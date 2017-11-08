package com.lostcanteen.deliciouscanteen;

import java.io.Serializable;

/**
 * Created by yw199 on 2017/10/22.
 */

public class Dish implements Serializable {
    private int dishid;
    private int canteenid;
    private String name;
    private String image;
    private float price;
    private boolean breakfast;
    private boolean lunch;
    private boolean dinner;
    private boolean main; //是否主营

    public Dish(int canteenid,int dishid, String name, String image, float price, boolean breakfast, boolean lunch, boolean dinner, boolean main) {
        this.canteenid = canteenid;
        this.dishid = dishid;
        this.name = name;
        this.image = image;
        this.price = price;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.main = main;
    }

    public int getCanteenid() {
        return canteenid;
    }

    public void setBreakfast(boolean breakfast) {
        this.breakfast = breakfast;
    }

    public void setLunch(boolean lunch) {
        this.lunch = lunch;
    }

    public void setDinner(boolean dinner) {
        this.dinner = dinner;
    }

    public boolean isBreakfast() {
        return breakfast;
    }

    public boolean isLunch() {
        return lunch;
    }

    public boolean isDinner() {
        return dinner;
    }

    public void setDishid(int dishid) {
        this.dishid = dishid;
    }

    public int getDishid() {

        return dishid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public float getPrice() {
        return price;
    }

    public boolean isMain() {
        return main;
    }
}
