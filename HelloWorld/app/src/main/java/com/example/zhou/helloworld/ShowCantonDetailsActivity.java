package com.example.zhou.helloworld;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ShowCantonDetailsActivity extends AppCompatActivity {
    public final static int REQUESTCODE = 1;

    public static void actionStart(Context context,CantonDetail  cantonDetail){
        Intent intent = new Intent(context,ShowCantonDetailsActivity.class);
        intent.putExtra("transCantonDetail",cantonDetail);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode)
        {
            case 1:
                if(resultCode == RESULT_OK){
                    //可以获得评星的个数
                    //评论

                }
                break;
            default:
        }
    }



    public enum TabSelected{ALLFOODS,RESERVE };


    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView cantonImageView;
    private FloatingActionButton timeFloatActionButton;

    private RecyclerView classifyListRecyclerView;
    private RecyclerView foodRecyclerView;
    private LinearLayoutManager managerClassifyList;
    private LinearLayoutManager managerFoodList;
    private ClassifyListAdapter classifyListAdapter;
    private FoodAdapter foodListAdapter;
    private TabLayout tabLayout;

    private List<ClassifyMeal> meals;
    private List<Food> foods;
    private int everyClassifyMealsNum[]; // 数组，用于记录食堂早中晚特色菜的数量，来确定表单的位置

    private TabSelected NowSelected;

    private CantonDetail cantonDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_canton_details);

        cantonDetail = (CantonDetail)getIntent().getParcelableExtra("transCantonDetail");

        toolbar = (Toolbar) findViewById(R.id.show_detail_toolbar);
        collapsingToolbar = (CollapsingToolbarLayout)
                findViewById(R.id.canton_toolbar);
        cantonImageView = (ImageView) findViewById(R.id.this_canton_image);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        timeFloatActionButton = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(cantonDetail.getCantonName());

        cantonImageView.setImageResource(cantonDetail.getCantonImageId());
        classifyListRecyclerView = (RecyclerView) findViewById(R.id.classifyList);
        managerClassifyList = new LinearLayoutManager(this);
        classifyListRecyclerView.setLayoutManager(managerClassifyList);

        foodRecyclerView = (RecyclerView) findViewById(R.id.foodList);
        managerFoodList = new LinearLayoutManager(this);
        foodRecyclerView.setLayoutManager(managerFoodList);

        setMeals(getMeal());
        setFoods(getAllFood());
        setDefaultRecyclerView();


        timeFloatActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
              //  foodListAdapter = new FoodAdapter(foods);
               // foodRecyclerView.setAdapter(foodListAdapter);
            }
        });


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0)
                {
                    NowSelected = TabSelected.ALLFOODS;
                    setFoods(getAllFood());

                }
                else if(tab.getPosition() == 1)
                {
                    NowSelected = TabSelected.RESERVE;
                    setFoods(getFoodForReserve());
                }
                foodListAdapter = new FoodAdapter(foods);
                foodRecyclerView.setAdapter(foodListAdapter);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });

        foodRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,int newState){
                super.onScrollStateChanged(recyclerView,newState);
            }
            @Override
            public  void onScrolled(RecyclerView recyclerView,int dx,int dy){
                super.onScrolled(recyclerView,dx,dy);
                int firstPosition = managerFoodList.findFirstVisibleItemPosition();
                for(int i=0;i<everyClassifyMealsNum.length-1;i++)
                {
                    if(classifyListRecyclerView.getChildAt(i) != null)
                    {
                        if(firstPosition>=everyClassifyMealsNum[i] && firstPosition <everyClassifyMealsNum[i+1])
                        {
                            classifyListRecyclerView.getChildAt(i).setBackgroundColor(Color.parseColor("#ff8c6a"));
                        }
                        else{
                            classifyListRecyclerView.getChildAt(i).setBackgroundColor(Color.parseColor("#FFFFFF"));
                        }
                    }
                }
            }
        });
   }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setDefaultRecyclerView(){
        NowSelected = TabSelected.ALLFOODS;
        classifyListAdapter = new ClassifyListAdapter(meals);
        classifyListRecyclerView.setAdapter(classifyListAdapter);
        foodListAdapter = new FoodAdapter(foods);
        foodRecyclerView.setAdapter(foodListAdapter);

    }

    //***********************从数据库拿数据并赋值，get是从数据库get，set是给到当前活动变量，写成一个给当前活动变量也可以
    public void setMeals(List<ClassifyMeal> transMeals){
        // 从数据库获得一个食品分类表；
        //*************************以下是假设获得的表
        meals = transMeals;
        everyClassifyMealsNum = new int[meals.size()+1];

    }

    public void setFoods(List<Food> transFoods){
        foods = transFoods;
        // 下面就要获得每种食物的个数(位置);
        for(int i = 0;i<everyClassifyMealsNum.length;i++)
        {
            everyClassifyMealsNum[i] = 0;
        }

        for(int i = 0;i<foods.size();i++)
        {
            everyClassifyMealsNum[foods.get(i).getWhatFood()+1]++;
        }
        for(int i =1;i<everyClassifyMealsNum.length;i++)
        {
            everyClassifyMealsNum[i] = everyClassifyMealsNum[i]+everyClassifyMealsNum[i-1];
        }
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

    public List<Food> getAllFood(){
        List<Food> foods = new ArrayList<>();
        for(int i =0 ;i<3;i++)
        {
            Food food = new Food(i,R.drawable.logo,i,((Integer)i).toString(),0);
            foods.add(food);

        }
        for(int i =3 ;i<10;i++)
        {
            Food food = new Food(i,R.drawable.logo,i,((Integer)i).toString(),1);
            foods.add(food);

        }

        for(int i =10 ;i<20;i++)
        {
            Food food = new Food(i,R.drawable.logo,i,((Integer)i).toString(),2);
            foods.add(food);

        }
        for(int i =20 ;i<30;i++)
        {
            Food food = new Food(i,R.drawable.logo,i,((Integer)i).toString(),3);
            foods.add(food);

        }
        return foods;
    }

    public List<Food> getFoodForReserve(){
        List<Food> foods = new ArrayList<>();
        for(int i =0 ;i<2;i++)
        {
            Food food = new Food(i,R.drawable.logo,i,((Integer)i).toString(),0);
            foods.add(food);

        }
        for(int i =2 ;i<8;i++)
        {
            Food food = new Food(i,R.drawable.logo,i,((Integer)i).toString(),1);
            foods.add(food);

        }

        for(int i =8 ;i<10;i++)
        {
            Food food = new Food(i,R.drawable.logo,i,((Integer)i).toString(),2);
            foods.add(food);

        }
        for(int i =10 ;i<20;i++)
        {
            Food food = new Food(i,R.drawable.logo,i,((Integer)i).toString(),3);
            foods.add(food);

        }

        return foods;
    }


    //*****************************获得数据

    public class ClassifyListAdapter extends RecyclerView.Adapter<ClassifyListAdapter.ViewHolder> {
        private List<ClassifyMeal> classifyList;
        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView mealImage;
            TextView mealName;
            public ViewHolder(View view){
                super(view);
                mealImage = (ImageView) view.findViewById(R.id.testImage);
                mealName = (TextView) view.findViewById(R.id.testmeal_text);
            }
        }
        public ClassifyListAdapter(List<ClassifyMeal> classifyList){this.classifyList = classifyList;}

        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.meallist_item,parent,false);
            final ViewHolder holder = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                   managerFoodList.scrollToPositionWithOffset(everyClassifyMealsNum[holder.getAdapterPosition()],0);
                }
            });
            return  holder;
        }
        @Override
        public void onBindViewHolder(ViewHolder holder,int position){
            ClassifyMeal meal = classifyList.get(position);
            holder.mealName.setText(meal.getClassifyName());
            holder.mealImage.setImageResource(meal.getClassifyImageId());
        }

        @Override
        public int getItemCount(){
            return classifyList.size();
        }

    }

    public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
        private List<Food> foodList;
        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView foodImage;
            TextView foodName;
            TextView foodPrice;
            ImageView foodEvaluate;
            public ViewHolder(View view){
                super(view);
                foodImage = (ImageView) view.findViewById(R.id.testFoodImage);
                foodName = (TextView) view.findViewById(R.id.testFood_name);
                foodPrice = (TextView) view.findViewById(R.id.testFood_price);
                foodEvaluate = (ImageView) view.findViewById(R.id.evaluate);
            }
        }
        public FoodAdapter(List<Food> foodList){this.foodList = foodList;}

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            final View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.foodlist_item,parent,false);
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
            final Food food = foodList.get(position);
            holder.foodImage.setImageResource(food.getFoodImageId());
            holder.foodName.setText(food.getFoodName());
            holder.foodPrice.setText(((Float)food.getFoodPrice()).toString());

            if(NowSelected == TabSelected.ALLFOODS)
            {
                holder.foodEvaluate.setImageResource(R.mipmap.evaluate);
                holder.foodEvaluate.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(ShowCantonDetailsActivity.this,EvaluateActivity.class);
                        intent.putExtra("transFood",food);
                        ShowCantonDetailsActivity.this.startActivityForResult(intent,1);
                    }
                });
            }
            else if(NowSelected == TabSelected.RESERVE)
            {
                holder.foodEvaluate.setImageResource(R.mipmap.evaluate);
                holder.foodEvaluate.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v){
                        foods.get(position).num++;
                        Toast.makeText(ShowCantonDetailsActivity.this,
                                food.getFoodName()+"数量+1",Toast.LENGTH_SHORT).show();
                    }
                });


            }

        }

        @Override
        public int getItemCount(){
            return foodList.size();
        }
    }

}
