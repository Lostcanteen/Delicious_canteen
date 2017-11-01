package com.example.zhou.helloworld;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhou on 2017/10/31.
 */

public class CantonDetail implements Parcelable {
    private int cantonId;
    private int cantonImageId; // 这里是为了获得图片的整型id
    private String cantonName;
    private String cantonLocation;
    private String cantonHours;

    public CantonDetail(){}


    public CantonDetail(int cantonId,int cantonImageId,String cantonName,
                        String cantonLocation,String cantonHours){
        this.cantonId = cantonId;
        this.cantonImageId =cantonImageId;
        this.cantonName = cantonName;
        this.cantonLocation = cantonLocation;
        this.cantonHours =cantonHours;
    }

    public int getCantonId(){
        return cantonId;
    }
    public int getCantonImageId(){
        return cantonImageId;
    }
    public String getCantonName(){
        return cantonName;
    }
    public String getCantonLocation(){
        return cantonLocation;
    }
    public String getCantonHours(){
        return cantonHours;
    }

    public void setCantonId(int cantonId){
        this.cantonId = cantonId;
    }
    public void setCantonImageId(int cantonImageId){
        this.cantonImageId = cantonImageId;
    }
    public void setCantonName(String cantonName){
        this.cantonName = cantonName;
    }
    public void setCantonLocation(String cantonLocation){
        this.cantonLocation = cantonLocation;
    }
    public void setCantonHours(String cantonHours){
        this.cantonHours = cantonHours;
    }

    //下面是为了传对象给intent写的
    @Override
    public int describeContents(){
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest,int flags){
        dest.writeInt(cantonId);
        dest.writeInt(cantonImageId);
        dest.writeString(cantonName);
        dest.writeString(cantonLocation);
        dest.writeString(cantonHours);
    }
    public static final Parcelable.Creator<CantonDetail> CREATOR = new Parcelable.
        Creator<CantonDetail>(){
        @Override
        public CantonDetail createFromParcel(Parcel source){
            CantonDetail cantonDetail = new CantonDetail();
            cantonDetail.cantonId = source.readInt();
            cantonDetail.cantonImageId = source.readInt();
            cantonDetail.cantonName = source.readString();
            cantonDetail.cantonLocation = source.readString();
            cantonDetail.cantonHours = source.readString();
            return cantonDetail;
        }
        @Override
        public CantonDetail[] newArray(int size){
            return new CantonDetail[size];
        }
    };

}
