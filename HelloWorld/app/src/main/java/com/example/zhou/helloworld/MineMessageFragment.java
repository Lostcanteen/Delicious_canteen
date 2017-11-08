package com.example.zhou.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by zhou on 2017/10/29.
 */

public class MineMessageFragment extends Fragment {
    public static MineMessageFragment newInstance(String text){
        MineMessageFragment fragment = new MineMessageFragment();
        Bundle args = new Bundle();
        args.putString("textUsername",text);
        fragment.setArguments(args);
        return fragment;
    }

    private TextView textUsername;
    private TextView textAllOrder;


    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.mine,container,false);
        if(getArguments() != null)
        {
            textUsername = (TextView) view.findViewById(R.id.textUsername);
            textUsername.setText(getArguments().getString("textUsername"));
        }
        textAllOrder = (TextView) view.findViewById(R.id.textAllOrder);

        textAllOrder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(view.getContext(),ShowMyOrderActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
