package com.lostcanteen.deliciouscanteen.activity;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lostcanteen.deliciouscanteen.Adapter.MyAdapter;
import com.lostcanteen.deliciouscanteen.Helper.CircleTransform;
import com.lostcanteen.deliciouscanteen.R;
import com.lostcanteen.deliciouscanteen.SpecialBook;
import com.lostcanteen.deliciouscanteen.WebTrans;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdminAllSbookActivity extends AppCompatActivity {

    private String adminname;
    private static int adminid;

    private Toolbar allSbookToolbar;
    private TextView title;
    private ImageView userImage;
    private TextView textUsername;
    private TabLayout selectedTab;
    private ViewPager allBooksBody;
    private static TestAdapter tabAdapter;


    private ArrayList<String> tabLayoutList = new ArrayList<String>() {{
        add("全部");
        add("未审核");
        add("已同意");
        add("已拒绝");
    }};


    private AdminSpecialBookFragment fragment1;
    private AdminSpecialBookFragment fragment2;
    private AdminSpecialBookFragment fragment3;
    private AdminSpecialBookFragment fragment4;

    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();


    private ArrayList<SpecialBook> allSpecialBookList = new ArrayList<>();
    private ArrayList<SpecialBook> notSpecialBookList = new ArrayList<>();
    private ArrayList<SpecialBook> agreeSpecialBookList = new ArrayList<>();
    private ArrayList<SpecialBook> refuseSpecialBookList = new ArrayList<>();


    private Handler loadTab = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what)
            {
                case 1:

                    fragment1 = AdminSpecialBookFragment.newInstance(allSpecialBookList);
                    fragment2 = AdminSpecialBookFragment.newInstance(notSpecialBookList);
                    fragment3 = AdminSpecialBookFragment.newInstance(agreeSpecialBookList);
                    fragment4 = AdminSpecialBookFragment.newInstance(refuseSpecialBookList);

                    fragmentList.add(fragment1);
                    fragmentList.add(fragment2);
                    fragmentList.add(fragment3);
                    fragmentList.add(fragment4);

                    tabAdapter = new TestAdapter(getSupportFragmentManager(),tabLayoutList,fragmentList);
                    allBooksBody.setAdapter(tabAdapter);
                    selectedTab.setupWithViewPager(allBooksBody,true);
                    selectedTab.setTabsFromPagerAdapter(tabAdapter);
                    break;
                case 2:

                    break;
                default:
            }
        }
    };

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

        public void updateDate(ArrayList<SpecialBook> allSpecialBookList,ArrayList<SpecialBook>
                notSpecialBookList,ArrayList<SpecialBook> agreeSpecialBookList,ArrayList<SpecialBook> refuseSpecialBookList){
            ArrayList<Fragment> fragments = new ArrayList<>();


            fragments.add(AdminSpecialBookFragment.newInstance(allSpecialBookList));
            fragments.add(AdminSpecialBookFragment.newInstance(notSpecialBookList));
            fragments.add(AdminSpecialBookFragment.newInstance(agreeSpecialBookList));
            fragments.add(AdminSpecialBookFragment.newInstance(refuseSpecialBookList));
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

               // fragmentList.clear();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_sbook);
        adminname = getIntent().getStringExtra("username");
        adminid = getIntent().getIntExtra("userid",-1);

        allSbookToolbar = (Toolbar) findViewById(R.id.allsbook_toolbar);
        title = (TextView) findViewById(R.id.allsbookTitle);
        userImage = (ImageView)findViewById(R.id.user_image);
        textUsername = (TextView) findViewById(R.id.username);
        selectedTab = (TabLayout) findViewById(R.id.tabLayout);
        allBooksBody = (ViewPager) findViewById(R.id.allsbook_body);


        Picasso.with(AdminAllSbookActivity.this).load(R.drawable.logo).transform(new CircleTransform()).into(userImage);

        textUsername.setText(adminname);

        title.setText("预约信息");

        setTitle("");
        setSupportActionBar(allSbookToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                allSpecialBookList = WebTrans.adminSbookQuery(adminid);
                for(int i = 0;i<allSpecialBookList.size();i++)
                {
                    SpecialBook specialBook = allSpecialBookList.get(i);
                    if(specialBook.getDealPattern().equals("等待处理"))
                    {
                        notSpecialBookList.add(specialBook);
                    }
                    else if(specialBook.getDealPattern().equals("接受"))
                    {
                        agreeSpecialBookList.add(specialBook);
                    }
                    else if(specialBook.getDealPattern().equals("拒绝"))
                    {
                        refuseSpecialBookList.add(specialBook);
                    }
                }
                Message message = new Message();
                message.what = 1;
                loadTab.sendMessage(message);


            }
        }).start();
    }

    public static class AdminSpecialBookFragment extends Fragment{
        private RecyclerView RecyclerView;
        private LinearLayoutManager linearLayoutManager;
        private AdminSpecialBookAdapter adminSpecialBookAdapter;

        private ArrayList<SpecialBook> thisSpecialBook = new ArrayList<>();

        private ArrayList<SpecialBook> allSpecialList = new ArrayList<>();
        private ArrayList<SpecialBook> notSpecialList = new ArrayList<>();
        private ArrayList<SpecialBook> agreeSpecialList = new ArrayList<>();
        private ArrayList<SpecialBook> refuseSpecialList = new ArrayList<>();

        public static  AdminSpecialBookFragment newInstance(ArrayList<SpecialBook> thisSpecialBook){
            AdminSpecialBookFragment fragment = new AdminSpecialBookFragment();

            Bundle args = new Bundle();

            args.putSerializable("thisSpecialBook",thisSpecialBook);

            fragment.setArguments(args);
            return fragment;

        }
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.home, container, false);
            Bundle bundle = getArguments();
            if (bundle != null) {
                thisSpecialBook = (ArrayList<SpecialBook>) bundle.getSerializable("thisSpecialBook");
            }
            RecyclerView = (RecyclerView)view.findViewById(R.id.canteenList);
            linearLayoutManager = new LinearLayoutManager(getActivity());
            RecyclerView.setLayoutManager(linearLayoutManager);
            adminSpecialBookAdapter = new AdminSpecialBookAdapter(thisSpecialBook);
            RecyclerView.setAdapter(adminSpecialBookAdapter);


            return view;
        }

        class AdminSpecialBookAdapter extends RecyclerView.Adapter<AdminSpecialBookAdapter.ViewHolder> {
            private ArrayList<SpecialBook> thisSbookList;

            class ViewHolder extends RecyclerView.ViewHolder{
                TextView canteenName;
                TextView deal;
                TextView textDate;
                TextView type;
                TextView spot;
                TextView num;
                TextView other;
                RelativeLayout waitfor;
                TextView agree;
                TextView refuse;

                public ViewHolder(View view){
                    super(view);
                    canteenName = (TextView)view.findViewById(R.id.canteen_Name);
                    deal = (TextView)view.findViewById(R.id.state);
                    textDate = (TextView)view.findViewById(R.id.date);
                    type = (TextView)view.findViewById(R.id.type);
                    spot = (TextView)view.findViewById(R.id.spot);
                    num = (TextView)view.findViewById(R.id.num);
                    other = (TextView)view.findViewById(R.id.other);
                    waitfor = (RelativeLayout)view.findViewById(R.id.waitfor);
                    agree = (TextView)view.findViewById(R.id.agree);
                    refuse = (TextView)view.findViewById(R.id.refuse);

                }
            }

            public AdminSpecialBookAdapter(ArrayList<SpecialBook> thisSbookList){
                this.thisSbookList = thisSbookList;
            }
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
                final View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.admin_sbook_item,parent,false);
                final ViewHolder holder = new ViewHolder(view);
                return  holder;
            }

            @Override
            public void onBindViewHolder(final ViewHolder holder, final int position) {
                final SpecialBook specialBook = thisSpecialBook.get(position);
                if(specialBook.getDealPattern().equals("等待处理"))
                {
                    holder.deal.setText("未审核");
                    holder.deal.setTextColor(getResources().getColor(R.color.orange));
                    holder.waitfor.setVisibility(View.VISIBLE);
                }
                else if(specialBook.getDealPattern().equals("接受"))
                {
                    holder.deal.setText("接受");
                    holder.deal.setTextColor(getResources().getColor(R.color.black));
                    holder.waitfor.setVisibility(View.GONE);
                }
                else if(specialBook.getDealPattern().equals("拒绝"))
                {
                    holder.deal.setText("拒绝");
                    holder.deal.setTextColor(getResources().getColor(R.color.red));
                    holder.waitfor.setVisibility(View.GONE);
                }
                else
                {
                    holder.deal.setText("错误");
                    holder.deal.setTextColor(getResources().getColor(R.color.red));
                    holder.waitfor.setVisibility(View.GONE);
                }

                holder.canteenName.setText(specialBook.getCanteenName());
                holder.textDate.setText("时间:"+specialBook.getDate().toString());
                holder.type.setText("用餐类型:"+specialBook.getType());
                holder.spot.setText("使用情景:"+specialBook.getSpot());
                holder.num.setText("人数:"+specialBook.getNum());
                holder.other.setText("备注:"+specialBook.getOther());

                final Handler myHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case 1:
                                tabAdapter.updateDate(allSpecialList,notSpecialList,agreeSpecialList
                                ,refuseSpecialList);

                                break;
                                default:
                        }
                    }
                };


                holder.agree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                WebTrans.dealBook(specialBook.getSbookid(),"ac");

                                allSpecialList = WebTrans.adminSbookQuery(adminid);

                                for(int i = 0;i<allSpecialList.size();i++)
                                {
                                    SpecialBook specialBook = allSpecialList.get(i);
                                    if(specialBook.getDealPattern().equals("等待处理"))
                                    {
                                        notSpecialList.add(specialBook);
                                    }
                                    else if(specialBook.getDealPattern().equals("接受"))
                                    {
                                        agreeSpecialList.add(specialBook);
                                    }
                                    else if(specialBook.getDealPattern().equals("拒绝"))
                                    {
                                        refuseSpecialList.add(specialBook);
                                    }
                                }
                                Message message = new Message();
                                message.what = 1;
                                myHandler.sendMessage(message);

                            }
                        }).start();
                    }
                });

                holder.refuse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                WebTrans.dealBook(specialBook.getSbookid(),"re");

                                allSpecialList = WebTrans.adminSbookQuery(adminid);

                                for(int i = 0;i<allSpecialList.size();i++)
                                {
                                    SpecialBook specialBook = allSpecialList.get(i);
                                    if(specialBook.getDealPattern().equals("等待处理"))
                                    {
                                        notSpecialList.add(specialBook);
                                    }
                                    else if(specialBook.getDealPattern().equals("接受"))
                                    {
                                        agreeSpecialList.add(specialBook);
                                    }
                                    else if(specialBook.getDealPattern().equals("拒绝"))
                                    {
                                        refuseSpecialList.add(specialBook);
                                    }
                                }
                                Message message = new Message();
                                message.what = 1;
                                myHandler.sendMessage(message);

                            }
                        }).start();



                    }
                });


            }
            @Override
            public int getItemCount(){
                return thisSbookList.size();
            }
        }
    }
}
