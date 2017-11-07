package com.example.zhou.helloworld;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhou on 2017/10/29.
 */
public class HomeFragment extends Fragment {

    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        view = inflater.inflate(R.layout.home,container,false);
        RecyclerView cantonDetails = (RecyclerView) view.findViewById(R.id.cantonList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        cantonDetails.setLayoutManager(linearLayoutManager);
        CantonIntroductionAdapter adapter = new CantonIntroductionAdapter(getCanton());
        cantonDetails.setAdapter(adapter);

        return view;
    }

    public List<CantonDetail> getCanton(){
        List<CantonDetail> cantonDetails = new ArrayList<>();
        //可以从数据库读取全部食堂，这里就以两个为例
        {
            CantonDetail canton1 = new CantonDetail(1,R.drawable.test,"学苑食堂","正心旁边","5:00");
            CantonDetail canton2 = new CantonDetail(2,R.drawable.logo,"学士食堂","旁边","4:00");
            cantonDetails.add(canton1);
            cantonDetails.add(canton2);
        }

        return cantonDetails;
    }

//感觉没用，没那么实现。
//
//    public void refresh(int cantonImageId,String cantonName){
//
//
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState){
//        super.onActivityCreated(savedInstanceState);
//        //没学明白再看
//    }

}
