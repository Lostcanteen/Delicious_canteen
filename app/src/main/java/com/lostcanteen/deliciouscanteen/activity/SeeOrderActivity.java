package com.lostcanteen.deliciouscanteen.activity;

import com.lostcanteen.deliciouscanteen.CanteenDetail;
import com.lostcanteen.deliciouscanteen.Helper.CircleTransform;
import com.lostcanteen.deliciouscanteen.R;
import com.lostcanteen.deliciouscanteen.WebTrans;
import com.squareup.picasso.Picasso;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeeOrderActivity extends AppCompatActivity {
    private Toolbar seeOrderToolbar;
    private TextView seeOrderTitle;
    private ImageView adminImage;
    private TextView adminNameTextView;
    private TabLayout tabLayout;
    private ViewPager seeOrderBody;

    private TestAdapter tabAdapter;

    private FloatingActionButton floatingActionButton;
    private DatePickerDialog datePickerDialog;

    private String adminName;
    private int adminId;


    private Calendar calendar;
    private int year,month,day;
    private static Date nowDate;

    private ArrayList<String> tabLayoutList = new ArrayList<String>() {{
        add("早餐");
        add("午餐");
        add("晚餐");
    }};

    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

        public void updateDate(){
            ArrayList<Fragment> fragments = new ArrayList<>();


            fragments.add(SeeOrderFragment.newInstance('b',canteenDetails));
            fragments.add(SeeOrderFragment.newInstance('l',canteenDetails));
            fragments.add(SeeOrderFragment.newInstance('d',canteenDetails));

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

    private SeeOrderFragment fragment1;
    private SeeOrderFragment fragment2;
    private SeeOrderFragment fragment3;



    private Handler readCanteenList = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what)
            {
                case 1:
                    fragment1 = SeeOrderFragment.newInstance('b',canteenDetails);
                    fragment2 = SeeOrderFragment.newInstance('l',canteenDetails);
                    fragment3 = SeeOrderFragment.newInstance('d',canteenDetails);

                    fragmentList.add(fragment1);
                    fragmentList.add(fragment2);
                    fragmentList.add(fragment3);

                    tabAdapter = new TestAdapter(getSupportFragmentManager(),tabLayoutList,fragmentList);
                    seeOrderBody.setAdapter(tabAdapter);
                    tabLayout.setupWithViewPager(seeOrderBody,true);
                    tabLayout.setTabsFromPagerAdapter(tabAdapter);
                    break;
                default:
            }
        }
    };
    ArrayList<CanteenDetail> canteenDetails = new ArrayList<CanteenDetail>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_order);
        adminName = getIntent().getStringExtra("username");
        adminId = getIntent().getIntExtra("userid",-1);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        nowDate = java.sql.Date.valueOf(((Integer)year).toString()
                +"-"+((Integer)(month+1)).toString() + "-"+((Integer)day).toString());

        adminImage = (ImageView)findViewById(R.id.admin_image);
        Picasso.with(SeeOrderActivity.this).load(R.drawable.logo).transform(new CircleTransform()).into(adminImage);

        seeOrderToolbar = (Toolbar)findViewById(R.id.seeOrder_toolbar);
        seeOrderTitle = (TextView) findViewById(R.id.seeOrderTitle);

        seeOrderTitle.setText("查看餐品预定");
        setTitle("");
        setSupportActionBar(seeOrderToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adminNameTextView = (TextView)findViewById(R.id.adminname);
        adminNameTextView.setText(adminName);

        tabLayout = (TabLayout)findViewById(R.id.seeordertabLayout);
        seeOrderBody = (ViewPager)findViewById(R.id.seeOrder_body);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.floatingActionButton);


        new Thread(new Runnable() {
            @Override
            public void run() {
                canteenDetails = WebTrans.adminCantainDetail(adminId);
                Message message = new Message();
                message.what = 1;
                readCanteenList.sendMessage(message);

            }
        }).start();



        floatingActionButton.setOnClickListener(new View.OnClickListener(){
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

                        tabAdapter.updateDate();

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


    public static class SeeOrderFragment extends Fragment {
        private ArrayList<CanteenDetail> canteenDetails = new ArrayList<>();
        private Map<Integer,ArrayList<String>> reserveMap = new HashMap<>();
        private ArrayList<String> reserveList = new ArrayList<>();

        private char option;

        private RecyclerView leftRecyclerView;
        private RecyclerView rightRecyclerView;
        private LinearLayoutManager leftLinearLayoutManager;
        private LinearLayoutManager rightLinearLayoutManager;
        private LeftListAdapter leftListAdapter;
        private RightListAdapter rightListAdapter;

        public static SeeOrderFragment newInstance(char option,
                                                   ArrayList<CanteenDetail> canteenDetailArrayList){
            SeeOrderFragment fragment = new SeeOrderFragment();
            Bundle args = new Bundle();
            args.putChar("option",option);
            args.putSerializable("canteenDetailsList",canteenDetailArrayList);

            fragment.setArguments(args);
            return fragment;
        }

        private Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        leftListAdapter = new LeftListAdapter(canteenDetails);
                        leftRecyclerView.setAdapter(leftListAdapter);


                        break;
                    default:
                }

            }
        };


        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.see_order_body, container, false);
            Bundle bundle = getArguments();
            if (bundle != null) {
                option = bundle.getChar("option");
                canteenDetails = (ArrayList<CanteenDetail>)bundle.getSerializable("canteenDetailsList");
            }
            leftRecyclerView = (RecyclerView)view.findViewById(R.id.leftList);
            rightRecyclerView = (RecyclerView)view.findViewById(R.id.rightList);

            leftLinearLayoutManager = new LinearLayoutManager(getActivity());
            leftRecyclerView.setLayoutManager(leftLinearLayoutManager);

            rightLinearLayoutManager = new LinearLayoutManager(getActivity());
            rightRecyclerView.setLayoutManager(rightLinearLayoutManager);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(int i = 0;i<canteenDetails.size();i++) {
                          ArrayList<String> temp = WebTrans.adminBookQuery(canteenDetails.get(i).getCanteenid(),nowDate,option);
                          reserveMap.put(canteenDetails.get(i).getCanteenid(),temp);

                        }
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                }).start();



            return view;
        }
        class LeftListAdapter extends RecyclerView.Adapter<LeftListAdapter.ViewHolder> {
            private List<CanteenDetail> canteenDetailList;
            class ViewHolder extends RecyclerView.ViewHolder {
                TextView canteenName;
                public ViewHolder(View view) {
                    super(view);
                    canteenName = (TextView)view.findViewById(R.id.canteen_name);
                }
            }

            public LeftListAdapter(List<CanteenDetail> canteenDetailList){
                this.canteenDetailList = canteenDetailList;
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
                final View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.left_order_item,parent,false);
                final ViewHolder holder = new ViewHolder(view);
                view.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        reserveList = reserveMap.get(canteenDetails.get(holder.getAdapterPosition()).getCanteenid());
                        rightListAdapter = new RightListAdapter(reserveList);
                        rightRecyclerView.setAdapter(rightListAdapter);

                        notifyDataSetChanged();
                    }
                });

                return  holder;
            }
            @Override
            public void onBindViewHolder(final ViewHolder holder, final int position) {
                final CanteenDetail canteenDetail = canteenDetailList.get(position);
                holder.canteenName.setText(canteenDetail.getName());

            }
            @Override
            public int getItemCount(){
                return canteenDetailList.size();
            }
        }


        class RightListAdapter extends RecyclerView.Adapter<RightListAdapter.ViewHolder> {
            private List<String> reserveList;
            class ViewHolder extends RecyclerView.ViewHolder {
                TextView reserveDetail;
                public ViewHolder(View view) {
                    super(view);
                    reserveDetail = (TextView)view.findViewById(R.id.canteen_name);
                }
            }

            public RightListAdapter(List<String> reserveList){
                this.reserveList = reserveList;
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
                final View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.rigth_order_item,parent,false);
                final ViewHolder holder = new ViewHolder(view);


                return  holder;
            }
            @Override
            public void onBindViewHolder(final ViewHolder holder, final int position) {
                final String reserveDetail = reserveList.get(position);

                String [] temp = reserveDetail.split(" ");
                holder.reserveDetail.setText("名称:"+temp[0]+"   数量:"+temp[1]);

            }
            @Override
            public int getItemCount(){
                return reserveList.size();
            }
        }
    }
}
