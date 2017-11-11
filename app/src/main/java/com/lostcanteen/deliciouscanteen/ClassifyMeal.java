package com.lostcanteen.deliciouscanteen;

/**
 * Created by zhou on 2017/11/1.
 */

public class ClassifyMeal {
    int classifyImageId;
    String classifyName;

    public ClassifyMeal(){}
    public ClassifyMeal(int classifyImageId, String classifyName){
        this.classifyImageId = classifyImageId;
        this.classifyName = classifyName;
    }

    public void setClassifyImageId(int classifyImageId){
        this.classifyImageId = classifyImageId;
    }

    public void setClassifyName(String classifyName){
        this.classifyName = classifyName;
    }

    public int getClassifyImageId(){
        return this.classifyImageId;
    }
    public String getClassifyName(){
        return this.classifyName;
    }


}