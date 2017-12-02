package com.lostcanteen.deliciouscanteen.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lostcanteen.deliciouscanteen.Article;
import com.lostcanteen.deliciouscanteen.Dish;
import com.lostcanteen.deliciouscanteen.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainDishListActivity extends AppCompatActivity {

    private ArrayList<Dish> dishesList;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        dishesList = (ArrayList<Dish>) getIntent().getSerializableExtra("dishlist");

        recyclerView = (RecyclerView) findViewById(R.id.article_list);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DishAdapter adapter = new DishAdapter(dishesList);
        recyclerView.setAdapter(adapter);
        toolbar = (Toolbar) findViewById(R.id.article_list_toolbar);
        setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    class DishAdapter extends RecyclerView.Adapter<DishAdapter.ViewHolder>  {
        private ArrayList<Dish> dishes;

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView articleImage;
            TextView articleTitle;
            TextView date;

            public ViewHolder(View view) {
                super(view);
                articleImage = (ImageView) view.findViewById(R.id.listImage);
                articleTitle = (TextView) view.findViewById(R.id.listTitle);
                date = (TextView) view.findViewById(R.id.listSubTitle);
            }
        }

        public DishAdapter(ArrayList<Dish> article) {
            dishes = article;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            final View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item,parent,false);
            final ViewHolder holder = new ViewHolder(view);

            return  holder;
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            Dish dish = dishes.get(position);
            Picasso.with(holder.articleImage.getContext())
                    .load(dish.getImage())
                    .placeholder(R.color.white)
                    .error(R.drawable.logo)
                    .into(holder.articleImage);
            holder.articleTitle.setText(dish.getName());
            holder.date.setText(((Float)dish.getPrice()).toString());
            holder.date.setTextColor(Color.RED);
        }

        public int getItemCount(){
            return dishes.size();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
