package com.lostcanteen.deliciouscanteen.activity;


import com.lostcanteen.deliciouscanteen.Adapter.MyAdapter;
import com.lostcanteen.deliciouscanteen.Book;
import com.lostcanteen.deliciouscanteen.Dish;
import com.lostcanteen.deliciouscanteen.Evaluation;
import com.lostcanteen.deliciouscanteen.WebTrans;
import com.lostcanteen.deliciouscanteen.Helper.CircleTransform;
import com.lostcanteen.deliciouscanteen.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ShowMyOrderActivity extends AppCompatActivity {

    private ArrayList<String> tabLayoutList = new ArrayList<String>() {{
        add("全部评价");
        add("全部预定");
    }};
    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();

    private static String username;
    private static int userid;

    private Toolbar myOrderToolbar;
    private TextView title;
    private ImageView userImage;
    private TextView textUsername;
    private TabLayout selectedTab;
    private ViewPager showOrder;
    private MyAdapter tabAdapter;

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_order);
        username = getIntent().getStringExtra("username");
        userid = getIntent().getIntExtra("userid",-1);

        // 未来可以实现网络拉取
        userImage = (ImageView)findViewById(R.id.user_image);
        Picasso.with(ShowMyOrderActivity.this).load(R.drawable.logo).transform(new CircleTransform()).into(userImage);
        textUsername = (TextView)findViewById(R.id.username);
        myOrderToolbar = (Toolbar)findViewById(R.id.my_order_toolbar);
        title = (TextView) findViewById(R.id.myorderTitle);

        title.setText("我的订单");
        setTitle("");
        setSupportActionBar(myOrderToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        selectedTab = (TabLayout)findViewById(R.id.tabLayout);
        showOrder = (ViewPager)findViewById(R.id.order);
        textUsername.setText(username);

        AllEvaluationFragment fragment1 = new AllEvaluationFragment();
        AllReserveFragment fragment2 = new AllReserveFragment();
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);

        tabAdapter = new MyAdapter(getSupportFragmentManager(),tabLayoutList,fragmentList);
        showOrder.setAdapter(tabAdapter);
        selectedTab.setupWithViewPager(showOrder,true);
        selectedTab.setTabsFromPagerAdapter(tabAdapter);

    }

    public static class AllEvaluationFragment extends Fragment {
        private RecyclerView evaluateRecyclerView;
        private LinearLayoutManager linearLayoutManager;
        private EvaluateListAdapter adapter;
        private List<Evaluation> allEvaluation;
        private List<Dish> allDish = new ArrayList<Dish>();

        private Handler allEvaluationHandler = new Handler(){
          @Override
          public void handleMessage(Message msg){
              switch (msg.what)
              {
                  case 1:
                      adapter = new EvaluateListAdapter(allEvaluation,allDish);
                      evaluateRecyclerView.setAdapter(adapter);
                      break;
                  default:
              }
          }
        };


        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.home, container, false);
            evaluateRecyclerView = view.findViewById(R.id.canteenList);
            linearLayoutManager = new LinearLayoutManager(getActivity());
            evaluateRecyclerView.setLayoutManager(linearLayoutManager);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    allEvaluation = WebTrans.userEvaQuery(username);
                    for(int i = 0;i<allEvaluation.size();i++)
                    {
                        allDish.add(WebTrans.queryDish(allEvaluation.get(i).getCanteenid(),allEvaluation.get(i).getDishid()));
                    }
                    Message message = new Message();
                    message.what = 1;
                    allEvaluationHandler.sendMessage(message);
                }
            }).start();

            return view;
        }


        class EvaluateListAdapter extends RecyclerView.Adapter<EvaluateListAdapter.ViewHolder>{
            List<Evaluation> evaluateList;
            List<Dish> dishList;
            class ViewHolder extends RecyclerView.ViewHolder{
                ImageView mealImage;
                TextView mealName;
                TextView mealPrice;
                RatingBar mealStars;
                TextView evaluationText;
                TextView date;
                public ViewHolder(View view){
                    super(view);
                    mealImage = (ImageView) view.findViewById(R.id.allEvaluationImage);
                    mealName = (TextView) view.findViewById(R.id.allEvaluationname);
                    mealPrice = (TextView) view.findViewById(R.id.allEvaluationprice);
                    mealStars = (RatingBar) view.findViewById(R.id.foodStars);
                    evaluationText = (TextView) view.findViewById(R.id.textEvalaute);
                    date = (TextView)view.findViewById(R.id.date);
                }
            }
            public EvaluateListAdapter(List<Evaluation> evaluateList,List<Dish> dishList){
                this.evaluateList = evaluateList;
                this.dishList = dishList;
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
                final View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.evaluate_listitem,parent,false);
                final ViewHolder holder = new ViewHolder(view);
                view.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                    }
                });
                return  holder;
            }

            @Override
            public void onBindViewHolder(final ViewHolder holder, final int position) {
                Evaluation evaluation = evaluateList.get(position);
                Dish dish = dishList.get(position);
                holder.mealName.setText(dish.getName());
////                //holder.mealImage.setImageResource();
////                //图片
                Picasso.with(holder.mealImage.getContext())
                        .load(dish.getImage())
                        .placeholder(R.color.white)
                        .error(R.drawable.logo)
                        .into(holder.mealImage);
                holder.mealPrice.setText(((Float)dish.getPrice()).toString());
                holder.mealStars.setRating(evaluation.getStar());
                holder.evaluationText.setText(evaluation.getComment());
                holder.date.setText(evaluation.getDate().toString());
            }
            @Override
            public int getItemCount(){
                return evaluateList.size();
            }

        }
    }



    public static class AllReserveFragment extends Fragment {
        private RecyclerView reserveListView;
        private LinearLayoutManager linearLayoutManager;
        private ReserveListAdapter adapter;

        private List<Book> allReserve;
        private List<Dish> allReserveDish = new ArrayList<Dish>();


        private Handler allReserveHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                switch (msg.what)
                {
                    case 1:
                        adapter = new ReserveListAdapter(allReserve,allReserveDish);
                        reserveListView.setAdapter(adapter);
                        break;
                    default:
                }
            }

        };

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.home, container, false);
            reserveListView = (RecyclerView)view.findViewById(R.id.canteenList);
            linearLayoutManager = new LinearLayoutManager(getActivity());
            reserveListView.setLayoutManager(linearLayoutManager);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    allReserve = WebTrans.userBookQuery(userid);
                    for(int i = 0;i<allReserve.size();i++)
                    {
                        allReserveDish.add(WebTrans.queryDish(allReserve.get(i).getCanteenid(),allReserve.get(i).getDishid()));
                    }
                    Message message = new Message();
                    message.what = 1;
                    allReserveHandler.sendMessage(message);
                }
            }).start();

            return view;
        }


        class ReserveListAdapter extends RecyclerView.Adapter<ReserveListAdapter.ViewHolder> {
            List<Book> allBooks;
            List<Dish> allReserveDish;

            public ReserveListAdapter(List<Book> allBooks,List<Dish> allReserveDish){
                this.allBooks = allBooks;
                this.allReserveDish = allReserveDish;
            }

            class ViewHolder extends RecyclerView.ViewHolder{
                TextView reserveFoodName;
                TextView reserveFoodPrice;
                ImageView reserveFoodImage;
                TextView reserveFoodNum;

                public ViewHolder(View view){
                    super(view);
                    reserveFoodImage = (ImageView)view.findViewById(R.id.reserveFoodImage);
                    reserveFoodName = (TextView)view.findViewById(R.id.reserveFood_name);
                    reserveFoodPrice = (TextView) view.findViewById(R.id.reserveFoodPrice);
                    reserveFoodNum = (TextView) view.findViewById(R.id.foodNum);
                }
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
                final View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.reserve_listitem,parent,false);
                final ViewHolder holder = new ViewHolder(view);
                return holder;
            }

            @Override
            public void onBindViewHolder(final ViewHolder holder, final int position) {
                //图片  url Image
                Book book = allBooks.get(position);
                Dish dish = allReserveDish.get(position);
                String b = book.getTypePattern();


                Picasso.with(holder.reserveFoodImage.getContext())
                        .load(dish.getImage())
                        .placeholder(R.color.white)
                        .error(R.drawable.logo)
                        .into(holder.reserveFoodImage);

                if(book.getTypePattern().equals("早餐"))
                {
                    holder.reserveFoodName.setText(dish.getName()+"(早餐)");
                }
                else if(book.getTypePattern().equals("午餐"))
                {
                    holder.reserveFoodName.setText(dish.getName()+"(午餐)");
                }
                else if(book.getTypePattern().equals("晚餐"))
                {
                    holder.reserveFoodName.setText(dish.getName()+"(晚餐)");
                }
                holder.reserveFoodPrice.setText(((Float)dish.getPrice()).toString());
                holder.reserveFoodNum.setText("数量:"+((Integer)book.getCnt()).toString());
            }


            @Override
            public int getItemCount(){
                return allBooks.size();
            }


        }


    }













}
