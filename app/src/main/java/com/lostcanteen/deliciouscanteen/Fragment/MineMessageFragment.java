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

import com.lostcanteen.deliciouscanteen.activity.AdminAllSbookActivity;
import com.lostcanteen.deliciouscanteen.activity.LoginActivity;
import com.lostcanteen.deliciouscanteen.activity.MainActivity;
import com.lostcanteen.deliciouscanteen.activity.SeeOrderActivity;
import com.lostcanteen.deliciouscanteen.activity.ShowMyOrderActivity;
import com.lostcanteen.deliciouscanteen.activity.UserSearchAllSbookActivity;

/**
 * Created by zhou on 2017/10/29.
 */

public class MineMessageFragment extends Fragment {
    public static MineMessageFragment newInstance(String text,int userid,boolean isAdmin){
        MineMessageFragment fragment = new MineMessageFragment();
        Bundle args = new Bundle();
        args.putString("textUsername",text);
        args.putInt("userid",userid);
        args.putBoolean("isAdmin",isAdmin);
        fragment.setArguments(args);
        return fragment;
    }

    private TextView textUsername;
    private TextView textAllOrder;
    private TextView textReserve;

    private Button logout;
    private String username;
    private int userid;
    private boolean isAdmin;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.mine,container,false);
        textUsername = (TextView) view.findViewById(R.id.textUsername);
        textAllOrder = (TextView) view.findViewById(R.id.textAllOrder);
        textReserve = (TextView) view.findViewById(R.id.textMyReserve);
        logout = (Button) view.findViewById(R.id.cancellation);
        Bundle args = getArguments();
        if(args != null)
        {
            username = args.getString("textUsername");
            userid = args.getInt("userid");
            isAdmin = args.getBoolean("isAdmin");
            textUsername.setText(username);
        }
        if(isAdmin)
        {
            textAllOrder.setText("查看菜品预定");
            textReserve.setText("全部预约");

        }

        textReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if(isAdmin)
                {
                    intent = new Intent(view.getContext(),AdminAllSbookActivity.class);
                    //这一行要改的
                }
                else
                {
                    intent = new Intent(view.getContext(),UserSearchAllSbookActivity.class);
                }
                intent.putExtra("username",username);
                intent.putExtra("userid",userid);
                startActivity(intent);
            }
        });




        textAllOrder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent;
                if(isAdmin)
                {
                    intent = new Intent(view.getContext(),SeeOrderActivity.class);
                }
                else
                {
                    intent = new Intent(view.getContext(),ShowMyOrderActivity.class);
                }
                intent.putExtra("username",username);
                intent.putExtra("userid",userid);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logoutIntent = new Intent(getActivity(), LoginActivity.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
            }
        });

        return view;
    }
}
