package com.lostcanteen.deliciouscanteen.activity;

import android.content.Intent;
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
import com.lostcanteen.deliciouscanteen.R;
import com.lostcanteen.deliciouscanteen.WebTrans;
import com.lostcanteen.deliciouscanteen.activity.WebActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ArticleListActivity extends AppCompatActivity {
    public static final int UPDATE_ARTICLE = 1;

    private ArrayList<Article> articleList;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private Toolbar toolbar;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_ARTICLE:
                    ArticleAdapter adapter = new ArticleAdapter(articleList);
                    recyclerView.setAdapter(adapter);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = (RecyclerView) findViewById(R.id.article_list);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        toolbar = (Toolbar) findViewById(R.id.article_list_toolbar);
        setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                articleList = WebTrans.articleList();
                Message message = new Message();
                message.what = UPDATE_ARTICLE;
                handler.sendMessage(message);
            }
        }).start();

    }

    class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
        private ArrayList<Article> articles;

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

        public ArticleAdapter(ArrayList<Article> article) {
            articles = article;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            final View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item,parent,false);
            final ViewHolder holder = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(view.getContext(),WebActivity.class);
                    intent.putExtra("url",articles.get(holder.getAdapterPosition()).getUrl());
                    startActivity(intent);
                }
            });
            return  holder;
        }

        public void onBindViewHolder(ViewHolder holder,int position) {
            Article article = articles.get(position);
            Picasso.with(holder.articleImage.getContext())
                    .load(article.getImage())
                    .placeholder(R.color.white)
                    .error(R.drawable.logo)
                    .into(holder.articleImage);
            holder.articleTitle.setText(article.getTitle());
            holder.date.setText(article.getDate().toString());
        }

        public int getItemCount(){
            return articles.size();
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
