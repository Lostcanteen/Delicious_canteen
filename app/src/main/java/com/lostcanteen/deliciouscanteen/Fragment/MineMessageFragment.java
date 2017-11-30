package com.lostcanteen.deliciouscanteen.Fragment;

import com.lostcanteen.deliciouscanteen.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lostcanteen.deliciouscanteen.activity.MainActivity;
import com.lostcanteen.deliciouscanteen.activity.ShowMyOrderActivity;

/**
 * Created by zhou on 2017/10/29.
 */

public class MineMessageFragment extends Fragment {
    public static MineMessageFragment newInstance(String text,int userid){
        MineMessageFragment fragment = new MineMessageFragment();
        Bundle args = new Bundle();
        args.putString("textUsername",text);
        args.putInt("userid",userid);
        fragment.setArguments(args);
        return fragment;
    }

    private TextView textUsername;
    private TextView textAllOrder;
    private Button logout;
    private String username;
    private int userid;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.mine,container,false);
        textUsername = (TextView) view.findViewById(R.id.textUsername);
        textAllOrder = (TextView) view.findViewById(R.id.textAllOrder);
        logout = (Button) view.findViewById(R.id.cancellation);
        Bundle args = getArguments();
        if(args != null)
        {
            username = args.getString("textUsername");
            userid = args.getInt("userid");
            textUsername.setText(username);
        }
        textAllOrder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(view.getContext(),ShowMyOrderActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("userid",userid);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        return view;
    }
}
