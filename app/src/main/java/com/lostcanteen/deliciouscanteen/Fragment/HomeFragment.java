package com.lostcanteen.deliciouscanteen.Fragment;

import com.lostcanteen.deliciouscanteen.WebTrans;
import com.lostcanteen.deliciouscanteen.R;
import com.lostcanteen.deliciouscanteen.CanteenDetail;
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


    public static HomeFragment newInstance(String text){
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("username",text);
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
        if(getArguments() != null)
        {
            username = getArguments().getString("username");
        }

        canteenDetailsRecyeclerView = (RecyclerView) view.findViewById(R.id.canteenList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        canteenDetailsRecyeclerView.setLayoutManager(linearLayoutManager);
        canteenDetailsList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                canteenDetailsList = WebTrans.allCantainDetail();
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
                    Intent intent = new Intent(view.getContext(), ShowCanteenDetailsActivity.class);
                    intent.putExtra("transCanteenDetail",transData);
                    intent.putExtra("username",username);
                    startActivity(intent);
                }
            });

            return  holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder,int position){
            CanteenDetail canteenDetail = myList.get(position);

            Picasso.with(holder.canteenImage.getContext())
                    .load("http://canteen-canteen.stor.sinaapp.com/12572201_1343370270884.jpg")
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.canteenImage);
            // holder.canteenImage.setImageResource(canteenDetail.getCanteenImageId());
            //****************************************食堂图片数据库拉取***********************



            holder.canteenName.setText(canteenDetail.getName());
        }

        @Override
        public int getItemCount(){
            return myList.size();
        }
    }



}
