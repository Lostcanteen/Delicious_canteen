package com.lostcanteen.deliciouscanteen.activity;

import com.lostcanteen.deliciouscanteen.WebTrans;
import com.lostcanteen.deliciouscanteen.Dish;
import com.lostcanteen.deliciouscanteen.R;
import com.lostcanteen.deliciouscanteen.CanteenDetail;
import com.lostcanteen.deliciouscanteen.ClassifyMeal;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBar;
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
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ShowCanteenDetailsActivity extends AppCompatActivity {
    public final static int REQUESTCODE = 1;



    //最开始穿了个对象
    public static void actionStart(Context context,CanteenDetail  canteenDetail){
        Intent intent = new Intent(context,ShowCanteenDetailsActivity.class);
        intent.putExtra("transCanteenDetail",canteenDetail);
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


    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView canteenImageView;
    private FloatingActionButton timeFloatActionButton;

    private RecyclerView classifyListRecyclerView;
    private RecyclerView foodRecyclerView;
    private LinearLayoutManager managerClassifyList;
    private LinearLayoutManager managerFoodList;
    private ClassifyListAdapter classifyListAdapter;
    private FoodAdapter foodListAdapter;
    private TabLayout tabLayout;

    private List<ClassifyMeal> meals;
    private List<Dish> allFoods = new ArrayList<>();
    private List<Dish> reserveFoods = new ArrayList<>();




    private int everyClassifyMealsNum[]; // 数组，用于记录食堂早中晚特色菜的数量，来确定表单的位置
    private int NowSelected;
    private CanteenDetail canteenDetail;


    private int allFoodsPosition[];
    private int reserveFoodsPosition[];

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    if(allFoods.size() != 0){
                        everyClassifyMealsNum = allFoodsPosition;
                        foodListAdapter = new FoodAdapter(allFoods);
                        foodRecyclerView.setAdapter(foodListAdapter);
                    }
                    break;
                case 1:
                    if(reserveFoods.size() != 0)
                    {
                        everyClassifyMealsNum =reserveFoodsPosition;
                        foodListAdapter = new FoodAdapter(reserveFoods);
                        foodRecyclerView.setAdapter(foodListAdapter);
                    }
                    break;
                default:
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_canteen_details);

        canteenDetail = (CanteenDetail)getIntent().getSerializableExtra("transCanteenDetail");

        toolbar = (Toolbar) findViewById(R.id.show_detail_toolbar);
        collapsingToolbar = (CollapsingToolbarLayout)
                findViewById(R.id.canteen_toolbar);
        canteenImageView = (ImageView) findViewById(R.id.this_canteen_image);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        timeFloatActionButton = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(canteenDetail.getName());
//图片***************************************************************8
        //canteenImageView.setImageResource(canteenDetail.getPicture());
        classifyListRecyclerView = (RecyclerView) findViewById(R.id.classifyList);
        managerClassifyList = new LinearLayoutManager(this);
        classifyListRecyclerView.setLayoutManager(managerClassifyList);

        foodRecyclerView = (RecyclerView) findViewById(R.id.foodList);
        managerFoodList = new LinearLayoutManager(this);
        foodRecyclerView.setLayoutManager(managerFoodList);

        setMeals(getMeal());

        classifyListAdapter = new ClassifyListAdapter(meals);
        classifyListRecyclerView.setAdapter(classifyListAdapter);


        new Thread(new Runnable() {
            @Override
            public void run() {

                allFoods.addAll(WebTrans.listMenu(11,new Date(2017,11,7),'B'));
                allFoodsPosition[1] = allFoods.size();
                allFoods.addAll(WebTrans.listMenu(11,new Date(2017,11,7),'L'));
                allFoodsPosition[2] = allFoods.size();
                allFoods.addAll(WebTrans.listMenu(11,new Date(2017,11,7),'D'));
                allFoodsPosition[3] = allFoods.size();

                reserveFoods.addAll( WebTrans.listMenu(11,new Date(2017,11,7),'b'));
                reserveFoodsPosition[1] = reserveFoods.size();
                reserveFoods.addAll( WebTrans.listMenu(11,new Date(2017,11,7),'l'));
                reserveFoodsPosition[2] = reserveFoods.size();
                reserveFoods.addAll( WebTrans.listMenu(11,new Date(2017,11,7),'d'));
                reserveFoodsPosition[3] = reserveFoods.size();

                Message message = new Message();
                message.what = NowSelected;
                handler.sendMessage(message);
            }
        }).start();

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
                Log.e("TAG","tab:"+tab.getPosition());

                NowSelected = tab.getPosition();
                Message message = new Message();
                message.what = tab.getPosition();
                handler.sendMessage(message);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });

        tabLayout.addTab(tabLayout.newTab().setText(R.string.daily_foods),true);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.reserve_foods));

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

    public void showRecyeclerView(int position){
        if(position == 0)
        {

        }
        else if(position == 1) {

        }
    }



    //***********************从数据库拿数据并赋值，get是从数据库get，set是给到当前活动变量，写成一个给当前活动变量也可以
    public void setMeals(List<ClassifyMeal> transMeals){
        // 从数据库获得一个食品分类表；
        //*************************以下是假设获得的表
        meals = transMeals;
        everyClassifyMealsNum = new int[meals.size()+1];
        allFoodsPosition = new int[meals.size()+1];
        reserveFoodsPosition = new int[meals.size()+1];

    }

//    public void setFoods(List<Dish> transFoods){
//        for(int i = 0;i<everyClassifyMealsNum.length;i++)
//        {
//            everyClassifyMealsNum[i] = 0;
//        }
//
//        for(int i = 0;i<transFoods.size();i++)
//        {
//            if(transFoods.get(i).isBreakfast())
//            {
//                everyClassifyMealsNum[0]++;
//            }
//            else if(transFoods.get(i).isLunch())
//            {
//                everyClassifyMealsNum[1]++;
//            }
//            else if(transFoods.get(i).isDinner())
//            {
//                everyClassifyMealsNum[2]++;
//            }
//            else if(transFoods.get(i).isMain())
//            {
//                everyClassifyMealsNum[3]++;
//            }
//        }
//        for(int i =1;i<everyClassifyMealsNum.length;i++)
//        {
//            everyClassifyMealsNum[i] = everyClassifyMealsNum[i]+everyClassifyMealsNum[i-1];
//        }
//    }



    public List<ClassifyMeal> getMeal(){
        List<ClassifyMeal> meals = new ArrayList<>();
        ClassifyMeal meal1 = new ClassifyMeal(R.mipmap.breakfastmeal,"早餐");
        ClassifyMeal meal2 = new ClassifyMeal(R.mipmap.lunchmeal,"午餐");
        ClassifyMeal meal3 = new ClassifyMeal(R.mipmap.dinner,"晚餐");
      //  ClassifyMeal meal4 = new ClassifyMeal(R.mipmap.specialmeal,"特色菜");
        meals.add(meal1);
        meals.add(meal2);
        meals.add(meal3);
       // meals.add(meal4);

        return  meals;
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
        private List<Dish> foodList;
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
        public FoodAdapter(List<Dish> foodList){this.foodList = foodList;}

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
            final Dish food = foodList.get(position);
            //******************图片等************
           // holder.foodImage.setImageResource(food.getFoodImageId());
            holder.foodName.setText(food.getName());
            holder.foodPrice.setText(((Float)food.getPrice()).toString());

            if(NowSelected == 0)
            {
                holder.foodEvaluate.setImageResource(R.mipmap.evaluate);
                holder.foodEvaluate.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(ShowCanteenDetailsActivity.this,EvaluateActivity.class);
                        intent.putExtra("transFood",food);
                        intent.putExtra("transCanteenId",canteenDetail.getCanteenid());
                        intent.putExtra("Date",new Date(2017,12,7));
                        startActivity(intent);

                       // ShowCanteenDetailsActivity.this.startActivityForResult(intent,1);


                    }
                });
            }
            else if(NowSelected == 1)
            {
                holder.foodEvaluate.setImageResource(R.mipmap.myreserve);
                holder.foodEvaluate.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v){
//                        foods.get(position).num++;
//                        Toast.makeText(ShowCanteenDetailsActivity.this,
//                                food.getFoodName()+"数量+1",Toast.LENGTH_SHORT).show();
                        //数量+;

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
