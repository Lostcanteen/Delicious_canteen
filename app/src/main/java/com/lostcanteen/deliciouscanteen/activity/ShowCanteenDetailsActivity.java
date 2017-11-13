package com.lostcanteen.deliciouscanteen.activity;

import com.lostcanteen.deliciouscanteen.Book;
import com.lostcanteen.deliciouscanteen.WebTrans;
import com.lostcanteen.deliciouscanteen.Dish;
import com.lostcanteen.deliciouscanteen.R;
import com.lostcanteen.deliciouscanteen.CanteenDetail;
import com.lostcanteen.deliciouscanteen.ClassifyMeal;
import com.squareup.picasso.Picasso;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ShowCanteenDetailsActivity extends AppCompatActivity {



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
    private Button reserveButton;
    private DatePickerDialog datePickerDialog;

    private List<ClassifyMeal> meals;
    private List<Dish> allFoods = new ArrayList<>();;
    private List<Dish> reserveFoods = new ArrayList<>();;




    private int everyClassifyMealsNum[]; // 数组，用于记录食堂早中晚特色菜的数量，来确定表单的位置
    private int NowSelected;
    private CanteenDetail canteenDetail;
    private String username;
    private int userid;

    private Calendar calendar;
    private int year,month,day;
    private Date nowDate;

    private int allFoodsPosition[];
    private int reserveFoodsPosition[];
    private int reserveFoodNum[];

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    everyClassifyMealsNum = allFoodsPosition;
                    foodListAdapter = new FoodAdapter(allFoods);
                    foodRecyclerView.setAdapter(foodListAdapter);
                    reserveButton.setVisibility(View.GONE);
                    break;
                case 1:
                    everyClassifyMealsNum =reserveFoodsPosition;
                    foodListAdapter = new FoodAdapter(reserveFoods);
                    foodRecyclerView.setAdapter(foodListAdapter);
                    reserveButton.setVisibility(View.VISIBLE);
                    break;
                default:
            }
        }
    };

    private void getList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                allFoods.clear();
                allFoods.addAll(WebTrans.listMenu(canteenDetail.getCanteenid(),nowDate,'B'));
                allFoodsPosition[1] = allFoods.size();
                allFoods.addAll(WebTrans.listMenu(canteenDetail.getCanteenid(),nowDate,'L'));
                allFoodsPosition[2] = allFoods.size();
                allFoods.addAll(WebTrans.listMenu(canteenDetail.getCanteenid(),nowDate,'D'));
                allFoodsPosition[3] = allFoods.size();

                reserveFoods.clear();
                reserveFoods.addAll( WebTrans.listMenu(canteenDetail.getCanteenid(),nowDate,'b'));
                reserveFoodsPosition[1] = reserveFoods.size();
                reserveFoods.addAll( WebTrans.listMenu(canteenDetail.getCanteenid(),nowDate,'l'));
                reserveFoodsPosition[2] = reserveFoods.size();
                reserveFoods.addAll( WebTrans.listMenu(canteenDetail.getCanteenid(),nowDate,'d'));
                reserveFoodsPosition[3] = reserveFoods.size();

                reserveFoodNum = new int[reserveFoods.size()];
                for(int i =0;i<reserveFoodNum.length;i++)
                {
                    reserveFoodNum[i] = 0;
                }
                Message message = new Message();
                message.what = NowSelected;
                handler.sendMessage(message);
            }
        }).start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_canteen_details);

        canteenDetail = (CanteenDetail)getIntent().getSerializableExtra("transCanteenDetail");
        username = getIntent().getStringExtra("username");
        userid = getIntent().getIntExtra("userid",-1);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        nowDate = java.sql.Date.valueOf(((Integer)year).toString()
                +"-"+((Integer)(month+1)).toString() + "-"+((Integer)day).toString());

        toolbar = (Toolbar) findViewById(R.id.show_detail_toolbar);
        collapsingToolbar = (CollapsingToolbarLayout)
                findViewById(R.id.canteen_toolbar);
        canteenImageView = (ImageView) findViewById(R.id.this_canteen_image);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        timeFloatActionButton = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        reserveButton = (Button) findViewById(R.id.addbook);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(canteenDetail.getName());
//图片***************************************************************8
        //canteenImageView.setImageResource(canteenDetail.getPicture());

        Picasso.with(canteenImageView.getContext())
                .load(canteenDetail.getPicture())
                .placeholder(R.color.colorPrimaryDark)
                .error(R.drawable.logo)
                .into(canteenImageView);

        classifyListRecyclerView = (RecyclerView) findViewById(R.id.classifyList);
        managerClassifyList = new LinearLayoutManager(this);
        classifyListRecyclerView.setLayoutManager(managerClassifyList);

        foodRecyclerView = (RecyclerView) findViewById(R.id.foodList);
        managerFoodList = new LinearLayoutManager(this);
        foodRecyclerView.setLayoutManager(managerFoodList);

        setMeals(getMeal());

        classifyListAdapter = new ClassifyListAdapter(meals);
        classifyListRecyclerView.setAdapter(classifyListAdapter);

        getList();

        timeFloatActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                datePickerDialog = new DatePickerDialog(v.getContext(), null,year, month, day);

                datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatePicker picker = datePickerDialog.getDatePicker();
                        year = picker.getYear();
                        month = picker.getMonth();
                        day = picker.getDayOfMonth();
                        nowDate = java.sql.Date.valueOf(((Integer)year).toString()
                                +"-"+((Integer)(month+1)).toString() + "-"+((Integer)day).toString());

                        getList();
                    }
                });
                datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                datePickerDialog.show();
            }
        });


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
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

        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<Integer> breakfastPosition = new ArrayList<>();
                        ArrayList<Integer> lunchPosition = new ArrayList<>();
                        ArrayList<Integer> dinnerPosition = new ArrayList<>();
                        for(int i = 0;i<reserveFoodNum.length;i++)
                        {
                            if(reserveFoodNum[i] >0)
                            {
                                if(i<reserveFoodsPosition[1])
                                {
                                    breakfastPosition.add(i);
                                }
                                else if(i<reserveFoodsPosition[2])
                                {
                                    lunchPosition.add(i);
                                }
                                else if(i<reserveFoodsPosition[3])
                                {
                                    dinnerPosition.add(i);
                                }
                            }
                        }
                        if(!breakfastPosition.isEmpty())
                        {
                            int[] breakcnt = new int[breakfastPosition.size()];
                            int[] breakfoodnum = new int[breakfastPosition.size()];
                            for(int i = 0;i<breakfastPosition.size();i++)
                            {
                                breakcnt[i] = reserveFoods.get(breakfastPosition.get(i)).getDishid();
                                breakfoodnum[i] = reserveFoodNum[breakfastPosition.get(i)];
                            }
                            WebTrans.commitBook(canteenDetail.getCanteenid(),new Date(2017,11,7),'b',userid,breakcnt,breakfoodnum);
                        }
                        if(!lunchPosition.isEmpty())
                        {
                            int[] lunchcnt = new int[lunchPosition.size()];
                            int[] lunchfoodnum = new int[lunchPosition.size()];
                            for(int i = 0;i<lunchPosition.size();i++)
                            {
                                lunchcnt[i] = reserveFoods.get(lunchPosition.get(i)).getDishid();
                                lunchfoodnum[i] = reserveFoodNum[lunchPosition.get(i)];
                            }

                            WebTrans.commitBook(canteenDetail.getCanteenid(),new Date(2017,11,7),'l',userid,lunchcnt,lunchfoodnum);


                        }

                        if(!dinnerPosition.isEmpty())
                        {
                            int[] dinnercnt = new int[dinnerPosition.size()];
                            int[] dinnerfoodnum = new int[dinnerPosition.size()];
                            for(int i = 0;i<dinnerPosition.size();i++)
                            {
                                dinnercnt[i] = reserveFoods.get(dinnerPosition.get(i)).getDishid();
                                dinnerfoodnum[i] = reserveFoodNum[dinnerPosition.get(i)];
                            }

                            WebTrans.commitBook(canteenDetail.getCanteenid(),new Date(2017,11,7),'d',userid,dinnercnt,dinnerfoodnum);
                        }


                    }
                }).start();
                finish();
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
        //ClassifyMeal meal4 = new ClassifyMeal(R.mipmap.specialmeal,"特色菜");
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
            TextView foodNum;
            ImageView reduceFood;
            public ViewHolder(View view){
                super(view);
                foodImage = (ImageView) view.findViewById(R.id.testFoodImage);
                foodName = (TextView) view.findViewById(R.id.testFood_name);
                foodPrice = (TextView) view.findViewById(R.id.testFood_price);
                foodEvaluate = (ImageView) view.findViewById(R.id.evaluate);
                foodNum = (TextView)view.findViewById(R.id.num);
                reduceFood = (ImageView) view.findViewById(R.id.delete);
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
                    Intent intent = new Intent(view.getContext(),DishEvaluationActivity.class);
                    intent.putExtra("canteenid",canteenDetail.getCanteenid());
                    intent.putExtra("food",foodList.get(holder.getAdapterPosition()));
                    startActivity(intent);
                }
            });
            return  holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final Dish food = foodList.get(position);
            //******************图片等************
           // holder.foodImage.setImageResource(food.getFoodImageId());

            Picasso.with(holder.foodImage.getContext())
                    .load(food.getImage())
                    .placeholder(R.color.white)
                    .error(R.drawable.logo)
                    .into(holder.foodImage);
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
                        intent.putExtra("username",username);
                        intent.putExtra("Date",nowDate);
                        startActivity(intent);

                       // ShowCanteenDetailsActivity.this.startActivityForResult(intent,1);


                    }
                });
            }
            else if(NowSelected == 1)
            {
                holder.foodEvaluate.setImageResource(R.mipmap.add_button);
                holder.reduceFood.setImageResource(R.mipmap.reduce_button);
                holder.foodNum.setText("0");
                holder.foodEvaluate.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v){
                        if(reserveFoodNum[position]<9)
                        {
                            reserveFoodNum[position]++;
                            holder.foodNum.setText(((Integer)reserveFoodNum[position]).toString());
                        }
                    }
                });
                holder.reduceFood.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v){
                        if(reserveFoodNum[position]>0)
                        {
                            reserveFoodNum[position]--;
                            holder.foodNum.setText(((Integer)reserveFoodNum[position]).toString());
                        }

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
