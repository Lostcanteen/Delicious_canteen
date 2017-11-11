package com.lostcanteen.deliciouscanteen.Fragment;

import com.lostcanteen.deliciouscanteen.R;
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
    private String username;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.mine,container,false);
        textUsername = (TextView) view.findViewById(R.id.textUsername);
        textAllOrder = (TextView) view.findViewById(R.id.textAllOrder);
        if(getArguments() != null)
        {
            username = getArguments().getString("textUsername");
            textUsername.setText(username);
        }
        textAllOrder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(view.getContext(),com.lostcanteen.deliciouscanteen.activity.ShowMyOrderActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        return view;
    }
}
