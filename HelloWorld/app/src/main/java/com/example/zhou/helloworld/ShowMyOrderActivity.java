package com.example.zhou.helloworld;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
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
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

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


    private ImageView userImage;
    private TabLayout selectedTab;
    private ViewPager showOrder;

    private MyAdapter tabAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_order);

        userImage = (ImageView)findViewById(R.id.user_image);
        Picasso.with(ShowMyOrderActivity.this).load(R.drawable.logo).transform(new CircleTransform()).into(userImage);

        selectedTab = (TabLayout)findViewById(R.id.tabLayout);
        showOrder = (ViewPager)findViewById(R.id.order);

        tabAdapter = new MyAdapter(getSupportFragmentManager(),tabLayoutList,fragmentList);

        showOrder.setAdapter(tabAdapter);
        selectedTab.setupWithViewPager(showOrder,true);
        selectedTab.setTabsFromPagerAdapter(tabAdapter);

    }

    public static class AllEvaluationFragment extends Fragment{
        private RecyclerView evaluateRecyclerView;

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.home, container, false);
            evaluateRecyclerView = view.findViewById(R.id.cantonList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            evaluateRecyclerView.setLayoutManager(linearLayoutManager);

            EvaluateListAdapter adapter = new EvaluateListAdapter(getMeal());

            evaluateRecyclerView.setAdapter(adapter);

            return view;
        }

        public List<ClassifyMeal> getMeal(){
            List<ClassifyMeal> meals = new ArrayList<>();
            ClassifyMeal meal1 = new ClassifyMeal(R.mipmap.breakfastmeal,"早餐");
            ClassifyMeal meal2 = new ClassifyMeal(R.mipmap.lunchmeal,"午餐");
            ClassifyMeal meal3 = new ClassifyMeal(R.mipmap.dinner,"晚餐");
            ClassifyMeal meal4 = new ClassifyMeal(R.mipmap.specialmeal,"特色菜");
            meals.add(meal1);
            meals.add(meal2);
            meals.add(meal3);
            meals.add(meal4);

            return  meals;
        }


        class EvaluateListAdapter extends RecyclerView.Adapter<EvaluateListAdapter.ViewHolder>{
            List<ClassifyMeal> evaluateList;
            class ViewHolder extends RecyclerView.ViewHolder{
                ImageView mealImage;
                TextView mealName;
                public ViewHolder(View view){
                    super(view);
                    mealImage = (ImageView) view.findViewById(R.id.testImage);
                    mealName = (TextView) view.findViewById(R.id.testmeal_text);
                }
            }
            public EvaluateListAdapter(List<ClassifyMeal> evaluateList){this.evaluateList = evaluateList;}

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
                final View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.meallist_item,parent,false);
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
                ClassifyMeal meal = evaluateList.get(position);
                holder.mealName.setText(meal.getClassifyName());
                holder.mealImage.setImageResource(meal.getClassifyImageId());
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
