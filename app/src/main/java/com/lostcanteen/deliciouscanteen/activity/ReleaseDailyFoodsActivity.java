package com.lostcanteen.deliciouscanteen.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.lostcanteen.deliciouscanteen.Adapter.MyAdapter;
import com.lostcanteen.deliciouscanteen.CanteenDetail;
import com.lostcanteen.deliciouscanteen.Dish;
import com.lostcanteen.deliciouscanteen.R;
import com.lostcanteen.deliciouscanteen.WebTrans;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class ReleaseDailyFoodsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView canteenImageView;
    private android.support.design.widget.FloatingActionButton timeFloatActionButton;
    private TabLayout tabLayout;
    private ViewPager releaseViewPager;
    private DatePickerDialog datePickerDialog;
    private TextView detail;

    private TestAdapter tabAdapter;


    private CanteenDetail canteenDetail;
    private int adminId;
    private String adminName;


    private Calendar calendar;
    private int year,month,day;
    private static Date nowDate;

    private ReleaseDailyFoodsFragment fragment1;
    private ReleaseDailyFoodsFragment fragment2;
    private ReleaseDailyFoodsFragment fragment3;

    private ArrayList<String> tabLayoutList = new ArrayList<String>() {{
        add("早餐");
        add("午餐");
        add("晚餐");
    }};

    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_daily_foods);

        canteenDetail = (CanteenDetail) getIntent().getSerializableExtra("transCanteenDetail");
        adminId = getIntent().getIntExtra("userid",-1);
        adminName = getIntent().getStringExtra("username");

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        nowDate = java.sql.Date.valueOf(((Integer)year).toString()
                +"-"+((Integer)(month+1)).toString() + "-"+((Integer)day).toString());

        toolbar = (Toolbar) findViewById(R.id.releaseFood_toolbar);
        collapsingToolbar = (CollapsingToolbarLayout)
                findViewById(R.id.releaseFood_ToolbarLayout);
        canteenImageView = (ImageView) findViewById(R.id.this_canteen_image);
        tabLayout = (TabLayout) findViewById(R.id.releaseFood_tabLayout);
        timeFloatActionButton = (android.support.design.widget.FloatingActionButton)findViewById(R.id.timefloatingActionButton);
        releaseViewPager = (ViewPager)findViewById(R.id.releaseFood_body);

        detail = (TextView) findViewById(R.id.details);
        detail.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG);




        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(canteenDetail.getName());

        Picasso.with(canteenImageView.getContext())
                .load(canteenDetail.getPicture())
                .placeholder(R.color.colorPrimaryDark)
                .error(R.drawable.logo)
                .into(canteenImageView);


        fragment1 = ReleaseDailyFoodsFragment.newInstance('B',canteenDetail.getCanteenid(),nowDate);
        fragment2 = ReleaseDailyFoodsFragment.newInstance('L',canteenDetail.getCanteenid(),nowDate);
        fragment3 = ReleaseDailyFoodsFragment.newInstance('D',canteenDetail.getCanteenid(),nowDate);
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        fragmentList.add(fragment3);

        tabAdapter = new TestAdapter(getSupportFragmentManager(),tabLayoutList,fragmentList);
        releaseViewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(releaseViewPager,true);
        tabLayout.setTabsFromPagerAdapter(tabAdapter);


        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReleaseDailyFoodsActivity.this,DetailActivity.class);
                intent.putExtra("canteenDetailid",canteenDetail.getCanteenid());
                intent.putExtra("username",adminName);
                intent.putExtra("isAdmin",true);
                startActivity(intent);
            }
        });


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

                        tabAdapter.updateDate(canteenDetail.getCanteenid(),nowDate);

                       // getList();
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

    }


    class TestAdapter  extends FragmentPagerAdapter {

        private ArrayList<String> titleList;
        private ArrayList<Fragment> fragmentList;

        private FragmentManager fragmentManager;

        public TestAdapter(FragmentManager fm, ArrayList<String> titleList, ArrayList<Fragment> fragmentList) {
            super(fm);
            this.titleList = titleList;
            this.fragmentList = fragmentList;
            this.fragmentManager = fm;
        }

        public void updateDate(int canteenId,Date nowDate){
            ArrayList<Fragment> fragments = new ArrayList<>();


            fragments.add(ReleaseDailyFoodsFragment.newInstance('B',canteenId,nowDate));
            fragments.add(ReleaseDailyFoodsFragment.newInstance('L',canteenId,nowDate));
            fragments.add(ReleaseDailyFoodsFragment.newInstance('D',canteenId,nowDate));

            setFragments(fragments);
        }

        private void setFragments(ArrayList<Fragment> mFragmentList) {
            if(this.fragmentList != null){
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                for(Fragment f:this.fragmentList){
                    fragmentTransaction.remove(f);
                }
                fragmentTransaction.commit();
                fragmentManager.executePendingTransactions();
            }
            this.fragmentList = mFragmentList;
            notifyDataSetChanged();
        }


        public int getItemPosition(Object object) {
            return POSITION_NONE;
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





    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class ReleaseDailyFoodsFragment extends Fragment{


        private RecyclerView leftRecyclerView;
        private RecyclerView rightRecyclerView;

        private FloatingActionsMenu selectButton;
        private com.getbase.floatingactionbutton.FloatingActionButton addDailyButton;
        private com.getbase.floatingactionbutton.FloatingActionButton addDishButton;

        private ArrayList<Dish> thisFragmentDish = new ArrayList<>();
        private ArrayList<Dish> someAddedDish = new ArrayList<>();
        private  ArrayList<Integer> getDishCanReserve = new ArrayList<>();;

        private HashSet<Integer> waitforCommitSet = new HashSet<Integer>();

        private char option;
        private int canteenId;

        private LinearLayoutManager leftLinearLayoutManager;
        private LinearLayoutManager rightLinearLayoutManager;
        private LeftListAdapter leftListAdapter;
        private RightListAdapter rightListAdapter;


        private Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                switch (msg.what)
                {
                    case 1:
                        leftListAdapter = new LeftListAdapter(thisFragmentDish);
                        leftRecyclerView.setAdapter(leftListAdapter);
                        rightListAdapter = new RightListAdapter(someAddedDish);
                        rightRecyclerView.setAdapter(rightListAdapter);

                        break;
                    case 2:
                        Toast.makeText(
                                getActivity(), "提交成功", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                }
            }

        };

        public void getExistDish(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    thisFragmentDish = WebTrans.listDish(canteenId,option);
                    someAddedDish = WebTrans.listMenu(canteenId,nowDate,option);
                    ArrayList<Dish> dishCanReserve = new ArrayList<>();
                    dishCanReserve = WebTrans.listMenu(canteenId,nowDate,(char) (option + 32));

                    for(int i =0;i<dishCanReserve.size();i++)
                    {
                        Dish dish = dishCanReserve.get(i);
                        getDishCanReserve.add(dish.getDishid());
                    }


                    Iterator<Dish> it = thisFragmentDish.iterator();
                    while (it.hasNext()){
                        Dish dish = (Dish) it.next();
                        Iterator<Dish> that = someAddedDish.iterator();
                        while (that.hasNext())
                        {
                            Dish thatDish = (Dish)that.next();
                            if(dish.getDishid() == thatDish.getDishid())
                            {
                                it.remove();
                            }


                        }
                    }

                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);

                }
            }).start();
        }

        public static ReleaseDailyFoodsFragment newInstance(char option,int canteenId,Date date)
        {
            ReleaseDailyFoodsFragment fragment = new ReleaseDailyFoodsFragment();
            Bundle args = new Bundle();
            args.putChar("option",option);
            args.putInt("canteenId",canteenId);

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.two_recycelerview, container, false);

            Bundle bundle = getArguments();
            if(bundle != null)
            {
                option = bundle.getChar("option");
                canteenId = bundle.getInt("canteenId");
            }

            leftRecyclerView = (RecyclerView)view.findViewById(R.id.leftList);
            rightRecyclerView = (RecyclerView)view.findViewById(R.id.rightList);

            selectButton = (FloatingActionsMenu)view.findViewById(R.id.select_add_release);
            addDailyButton = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.addRelease);
            addDishButton = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.addFood);

            leftLinearLayoutManager = new LinearLayoutManager(getActivity());
            leftRecyclerView.setLayoutManager(leftLinearLayoutManager);

            rightLinearLayoutManager = new LinearLayoutManager(getActivity());
            rightRecyclerView.setLayoutManager(rightLinearLayoutManager);


            getExistDish();


            addDishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),AddDishActivity.class);
                    intent.putExtra("canteenId",canteenId);
                    startActivity(intent);
                }
            });



            addDailyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int[] reserveMenu = new int[waitforCommitSet.size()];
                            int[] dailyFood = new int[someAddedDish.size()];

                            Iterator<Integer> it = waitforCommitSet.iterator();
                            int i = 0;
                            while (it.hasNext())
                            {
                                reserveMenu[i] = it.next();
                                i++;
                            }

                            for(int j = 0;j<dailyFood.length;j++)
                            {
                                dailyFood[j] = someAddedDish.get(j).getDishid();
                            }
                            WebTrans.addMenu(canteenId,nowDate,dailyFood,option);
                            WebTrans.addMenu(canteenId,nowDate,reserveMenu,(char) (option + 32));

                            Message message = new Message();
                            message.what = 2;
                            handler.sendMessage(message);

                            getExistDish();

                        }
                    }).start();
                }
            });

            return view;
        }


        class LeftListAdapter extends RecyclerView.Adapter<LeftListAdapter.ViewHolder>{
            private List<Dish> dishList;
            class ViewHolder extends RecyclerView.ViewHolder {
                ImageView foodImage;
                TextView foodName;
                TextView foodPrice;
                ImageView addreleaseFoodImage;

                public ViewHolder(View view) {
                    super(view);
                    foodImage = (ImageView)view.findViewById(R.id.releaseFood_Image);
                    foodName = (TextView)view.findViewById(R.id.releaseFood_name);
                    foodPrice = (TextView)view.findViewById(R.id.releaseFood_price);
                    addreleaseFoodImage = (ImageView)view.findViewById(R.id.evaluate);

                }
            }

            public LeftListAdapter(List<Dish> dishList){
                this.dishList = dishList;
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
                final View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.left_listitem,parent,false);
                final ViewHolder holder = new ViewHolder(view);
                return  holder;
            }
            @Override
            public void onBindViewHolder(final ViewHolder holder, final int position) {
                final Dish food = dishList.get(position);

                Picasso.with(holder.foodImage.getContext())
                        .load(food.getImage())
                        .placeholder(R.color.white)
                        .error(R.drawable.logo)
                        .into(holder.foodImage);
                holder.foodName.setText(food.getName());
                holder.foodPrice.setText(((Float)food.getPrice()).toString());

                holder.addreleaseFoodImage.setImageResource(R.mipmap.add_button);


                holder.addreleaseFoodImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        someAddedDish.add(food);
                        dishList.remove(position);
                        LeftListAdapter.this.notifyDataSetChanged();
                    }
                });



            }
            @Override
            public int getItemCount(){
                return dishList.size();
            }
        }

        class RightListAdapter extends RecyclerView.Adapter<RightListAdapter.ViewHolder>{
            private List<Dish> dishList;
            class ViewHolder extends RecyclerView.ViewHolder {

                ImageView foodImage;
                TextView foodName;
                TextView foodPrice;
                ImageView reduceReleaseFoodImage;
                Switch isReserve;
                TextView textIsReserve;
                public ViewHolder(View view) {
                    super(view);
                    foodImage = (ImageView)view.findViewById(R.id.releaseFood_Image);
                    foodName = (TextView)view.findViewById(R.id.releaseFood_name);
                    foodPrice = (TextView)view.findViewById(R.id.releaseFood_price);
                    reduceReleaseFoodImage = (ImageView)view.findViewById(R.id.evaluate);
                    isReserve = (Switch)view.findViewById(R.id.isReserve);
                    textIsReserve = (TextView)view.findViewById(R.id.textIsReserve);
                }
            }

            public RightListAdapter(List<Dish> dishList){
                this.dishList = dishList;
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
                final View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.right_listitem,parent,false);
                final ViewHolder holder = new ViewHolder(view);
                view.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(view.getContext(),DishEvaluationActivity.class);
                        intent.putExtra("canteenid",canteenId);
                        intent.putExtra("food",dishList.get(holder.getAdapterPosition()));
                        startActivity(intent);
                    }
                });

                return  holder;
            }
            @Override
            public void onBindViewHolder(final ViewHolder holder, final int position) {
                final Dish food = dishList.get(position);

                Picasso.with(holder.foodImage.getContext())
                        .load(food.getImage())
                        .placeholder(R.color.white)
                        .error(R.drawable.logo)
                        .into(holder.foodImage);
                holder.foodName.setText(food.getName());
                holder.foodPrice.setText(((Float)food.getPrice()).toString());

                holder.reduceReleaseFoodImage.setImageResource(R.mipmap.reduce_button);


                holder.reduceReleaseFoodImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        thisFragmentDish.add(food);
                        dishList.remove(position);
                        if(waitforCommitSet.contains(food.getDishid()))
                        {
                            waitforCommitSet.remove(food.getDishid());
                        }
                        RightListAdapter.this.notifyDataSetChanged();
                    }
                });

                holder.isReserve.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b)
                        {
                            holder.textIsReserve.setText("预定开启");
                            waitforCommitSet.add(food.getDishid());
                        }
                        else
                        {
                            holder.textIsReserve.setText("预定关闭");
                            waitforCommitSet.remove(food.getDishid());
                        }
                    }
                });
                if(getDishCanReserve.contains(food.getDishid()))
                {
                    holder.isReserve.setChecked(true);
                }


            }
            @Override
            public int getItemCount(){
                return dishList.size();
            }
        }

    }
}
