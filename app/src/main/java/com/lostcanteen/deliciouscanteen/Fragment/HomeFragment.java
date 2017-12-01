package com.lostcanteen.deliciouscanteen.Fragment;

import com.lostcanteen.deliciouscanteen.Helper.CircleTransform;
import com.lostcanteen.deliciouscanteen.WebTrans;
import com.lostcanteen.deliciouscanteen.R;
import com.lostcanteen.deliciouscanteen.CanteenDetail;
import com.lostcanteen.deliciouscanteen.activity.ReleaseDailyFoodsActivity;
import com.lostcanteen.deliciouscanteen.activity.ShowCanteenDetailsActivity;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhou on 2017/10/29.
 */
public class HomeFragment extends Fragment {
    public static final int UPDATE_CANTEENDETAILS = 1;

    private View view;
    private List<CanteenDetail> canteenDetailsList;
    private RecyclerView canteenDetailsRecyeclerView;
    private LinearLayoutManager linearLayoutManager;
    private String username;
    private int userid;
    private boolean isAdmin;

    public static HomeFragment newInstance(String text,int userid,boolean isAdmin){
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("username",text);
        args.putInt("userid",userid);
        args.putBoolean("isAdmin",isAdmin);
        fragment.setArguments(args);
        return fragment;
    }


    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what)
            {
                case UPDATE_CANTEENDETAILS:
                    CanteenIntroductionAdapter adapter = new CanteenIntroductionAdapter(canteenDetailsList);
                    canteenDetailsRecyeclerView.setAdapter(adapter);
                    break;
                default:
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.home,container,false);
        Bundle bundle = getArguments();
        if(bundle != null)
        {
            username = bundle.getString("username");
            userid = bundle.getInt("userid");
            isAdmin = bundle.getBoolean("isAdmin");
        }

        canteenDetailsRecyeclerView = (RecyclerView) view.findViewById(R.id.canteenList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        canteenDetailsRecyeclerView.setLayoutManager(linearLayoutManager);
        canteenDetailsList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(isAdmin)
                {
                    canteenDetailsList = WebTrans.adminCantainDetail(userid);
                }
                else {
                    canteenDetailsList = WebTrans.allCantainDetail();
                }

                Message message = new Message();
                message.what = UPDATE_CANTEENDETAILS;
                handler.sendMessage(message);

            }
        }).start();
        return view;
    }


    class CanteenIntroductionAdapter extends RecyclerView.Adapter<CanteenIntroductionAdapter.ViewHolder>{
        private List<CanteenDetail> myList;
        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView canteenImage;
            TextView canteenName;
            public ViewHolder(View view){
                super(view);
                //*************在这里实例化
                canteenImage = (ImageView) view.findViewById(R.id.canteen_image);
                canteenName = (TextView) view.findViewById(R.id.canteen_name);
            }
        }
        public CanteenIntroductionAdapter(List<CanteenDetail> list){
            myList = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
            final View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.canteen_list_item,parent,false);
            final ViewHolder holder = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    CanteenDetail transData = myList.get(holder.getAdapterPosition());
                    Intent intent;
                    if(isAdmin)
                    {
                        intent = new Intent(view.getContext(), ReleaseDailyFoodsActivity.class);
                    }
                    else
                    {
                        intent = new Intent(view.getContext(), ShowCanteenDetailsActivity.class);
                    }

                    intent.putExtra("transCanteenDetail",transData);
                    intent.putExtra("username",username);
                    intent.putExtra("userid",userid);
                    startActivity(intent);
                }
            });

            return  holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder,int position){
            CanteenDetail canteenDetail = myList.get(position);

            Picasso.with(holder.canteenImage.getContext())
                    .load(canteenDetail.getPicture())
                    .placeholder(R.color.colorPrimaryDark)
                    .error(R.drawable.logo)
                    .into(holder.canteenImage);
            holder.canteenName.setText(canteenDetail.getName());
        }

        @Override
        public int getItemCount(){
            return myList.size();
        }
    }



}
