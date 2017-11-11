package com.lostcanteen.deliciouscanteen.activity;


import com.lostcanteen.deliciouscanteen.Evaluation;
import com.lostcanteen.deliciouscanteen.WebTrans;
import com.lostcanteen.deliciouscanteen.Helper.CircleTransform;
import com.lostcanteen.deliciouscanteen.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>() {{
        add(new AllEvaluationFragment());
        add(new AllEvaluationFragment());
    }};

    private String username;

    private ImageView userImage;
    private TabLayout selectedTab;
    private ViewPager showOrder;
    private MyAdapter tabAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_order);
        username = getIntent().getStringExtra("username");


        // 未来可以实现网络拉取
        userImage = (ImageView)findViewById(R.id.user_image);
        Picasso.with(ShowMyOrderActivity.this).load(R.drawable.logo).transform(new CircleTransform()).into(userImage);

        selectedTab = (TabLayout)findViewById(R.id.tabLayout);
        showOrder = (ViewPager)findViewById(R.id.order);

        tabAdapter = new MyAdapter(getSupportFragmentManager(),tabLayoutList,fragmentList);

        showOrder.setAdapter(tabAdapter);
        selectedTab.setupWithViewPager(showOrder,true);
        selectedTab.setTabsFromPagerAdapter(tabAdapter);

    }

    public static class AllEvaluationFragment extends Fragment {
        private RecyclerView evaluateRecyclerView;
        private List<Evaluation> allEvaluation;
        private String username;

        public static AllEvaluationFragment newInstance(String text){
            AllEvaluationFragment fragment = new AllEvaluationFragment();
            Bundle args = new Bundle();
            args.putString("username",text);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.home, container, false);
            if(getArguments()!= null)
            {
                username = getArguments().getString("username");
            }

            evaluateRecyclerView = view.findViewById(R.id.canteenList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            evaluateRecyclerView.setLayoutManager(linearLayoutManager);

            //*************
//            EvaluateListAdapter adapter = new EvaluateListAdapter();
//            evaluateRecyclerView.setAdapter(adapter);


            new Thread(new Runnable() {
                @Override
                public void run() {

                }
            }).start();

            return view;
        }


        class EvaluateListAdapter extends RecyclerView.Adapter<EvaluateListAdapter.ViewHolder>{
            List<Evaluation> evaluateList;
            class ViewHolder extends RecyclerView.ViewHolder{
                ImageView mealImage;
                TextView mealName;
                TextView mealPrice;
                RatingBar mealStars;
                TextView evaluationText;
                public ViewHolder(View view){
                    super(view);
                    mealImage = (ImageView) view.findViewById(R.id.testImage);
                    mealName = (TextView) view.findViewById(R.id.testmeal_text);
                    mealPrice = (TextView) view.findViewById(R.id.testFood_price);
                    mealStars = (RatingBar) view.findViewById(R.id.foodStars);
                    evaluationText = (TextView) view.findViewById(R.id.textEvalaute);
                }
            }
            public EvaluateListAdapter(List<Evaluation> evaluateList){this.evaluateList = evaluateList;}

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
//                holder.mealName.setText();
//                //holder.mealImage.setImageResource();
//                //图片
//                holder.mealPrice.setText();
//                holder.mealStars.setRating();
//                holder.evaluationText.setText();
            }
            @Override
            public int getItemCount(){
                return evaluateList.size();
            }

        }
    }


    class MyAdapter extends FragmentPagerAdapter {

        private ArrayList<String> titleList;
        private ArrayList<Fragment> fragmentList;

        public MyAdapter(FragmentManager fm, ArrayList<String> titleList, ArrayList<Fragment> fragmentList) {
            super(fm);
            this.titleList = titleList;
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }

}
