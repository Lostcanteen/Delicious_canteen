package com.example.zhou.helloworld;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/* Created by zhou on 2017/10/31.
 */


public class CantonIntroductionAdapter extends RecyclerView.Adapter<CantonIntroductionAdapter.ViewHolder>{
    private List<CantonDetail> myList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView cantonImage;
        TextView cantonName;
        public ViewHolder(View view){
            super(view);
            //*************在这里实例化
            cantonImage = (ImageView) view.findViewById(R.id.canton_image);
            cantonName = (TextView) view.findViewById(R.id.canton_name);
        }
    }
    public CantonIntroductionAdapter(List<CantonDetail> list){
        myList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.caton_list_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                CantonDetail transData = myList.get(holder.getAdapterPosition());
                ShowCantonDetailsActivity.actionStart(view.getContext(),transData);
//                ShowCantonDetailsActivity.actionStart(view.getContext(),transData.getCantonId(),transData.getCantonImageId()
//                ,transData.getCantonName(),transData.getCantonLocation(),transData.getCantonHours());
            }
        });

        return  holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        CantonDetail cantonDetail = myList.get(position);
        holder.cantonImage.setImageResource(cantonDetail.getCantonImageId());
        holder.cantonName.setText(cantonDetail.getCantonName());
    }

    @Override
    public int getItemCount(){
        return myList.size();
    }
}
