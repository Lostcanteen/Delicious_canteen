package com.example.zhou.helloworld;

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

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.mine,container,false);
        if(getArguments() != null)
        {
            TextView textView = (TextView) view.findViewById(R.id.textUsername);

            textView.setText(getArguments().getString("textUsername"));

        }
        return view;
    }
}
