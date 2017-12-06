package com.lostcanteen.deliciouscanteen.activity;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;

import com.lostcanteen.deliciouscanteen.Adapter.MyAdapter;
import com.lostcanteen.deliciouscanteen.Helper.CircleTransform;
import com.lostcanteen.deliciouscanteen.R;
import com.lostcanteen.deliciouscanteen.SpecialBook;
import com.lostcanteen.deliciouscanteen.WebTrans;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class UserSearchAllSbookActivity extends AppCompatActivity {

    private String username;
    private int userid;

    private Toolbar allSbookToolbar;
    private TextView title;
    private ImageView userImage;
    private TextView textUsername;
    private TabLayout selectedTab;
    private ViewPager allBooksBody;
    private MyAdapter tabAdapter;


    private ArrayList<String> tabLayoutList = new ArrayList<String>() {{
        add("全部");
        add("未审核");
        add("已同意");
        add("已拒绝");
    }};

    private SpecialBookFragment fragment1;
    private SpecialBookFragment fragment2;
    private SpecialBookFragment fragment3;
    private SpecialBookFragment fragment4;

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

                    fragment1 = SpecialBookFragment.newInstance(allSpecialBookList);
                    fragment2 = SpecialBookFragment.newInstance(notSpecialBookList);
                    fragment3 = SpecialBookFragment.newInstance(agreeSpecialBookList);
                    fragment4 = SpecialBookFragment.newInstance(refuseSpecialBookList);

                    fragmentList.add(fragment1);
                    fragmentList.add(fragment2);
                    fragmentList.add(fragment3);
                    fragmentList.add(fragment4);

                    tabAdapter = new MyAdapter(getSupportFragmentManager(),tabLayoutList,fragmentList);
                    allBooksBody.setAdapter(tabAdapter);
                    selectedTab.setupWithViewPager(allBooksBody,true);
                    selectedTab.setTabsFromPagerAdapter(tabAdapter);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search_all_sbook);

        username = getIntent().getStringExtra("username");
        userid = getIntent().getIntExtra("userid",-1);

        allSbookToolbar = (Toolbar) findViewById(R.id.allsbook_toolbar);
        title = (TextView) findViewById(R.id.allsbookTitle);
        userImage = (ImageView)findViewById(R.id.user_image);
        textUsername = (TextView) findViewById(R.id.username);
        selectedTab = (TabLayout) findViewById(R.id.tabLayout);
        allBooksBody = (ViewPager) findViewById(R.id.allsbook_body);


        Picasso.with(UserSearchAllSbookActivity.this).load(R.drawable.logo).transform(new CircleTransform()).into(userImage);

        textUsername.setText(username);

        title.setText("预约信息");

        setTitle("");
        setSupportActionBar(allSbookToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                allSpecialBookList = WebTrans.userSbookQuery(username);
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



    public static class SpecialBookFragment extends Fragment {
        private RecyclerView RecyclerView;
        private LinearLayoutManager linearLayoutManager;
        private SpecialBookAdapter adapter;

        private ArrayList<SpecialBook> thisSpecialBook = new ArrayList<>();

        public static SpecialBookFragment newInstance(ArrayList<SpecialBook> thisSpecialBook){
            SpecialBookFragment fragment = new SpecialBookFragment();

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
            adapter = new SpecialBookAdapter(thisSpecialBook);
            RecyclerView.setAdapter(adapter);
            return view;
        }

        class SpecialBookAdapter extends RecyclerView.Adapter<SpecialBookAdapter.ViewHolder> {
            private ArrayList<SpecialBook> thisSbookList;

            class ViewHolder extends RecyclerView.ViewHolder{
                TextView canteenName;
                TextView deal;
                TextView textDate;
                TextView type;
                TextView spot;
                TextView num;
                TextView other;
                public ViewHolder(View view){
                    super(view);
                    canteenName = (TextView)view.findViewById(R.id.canteen_Name);
                    deal = (TextView)view.findViewById(R.id.state);
                    textDate = (TextView)view.findViewById(R.id.date);
                    type = (TextView)view.findViewById(R.id.type);
                    spot = (TextView)view.findViewById(R.id.spot);
                    num = (TextView)view.findViewById(R.id.num);
                    other = (TextView)view.findViewById(R.id.other);
                }
            }

            public SpecialBookAdapter(ArrayList<SpecialBook> thisSbookList){
                this.thisSbookList = thisSbookList;
            }
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
                final View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.specialbook_item,parent,false);
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
                }
                else if(specialBook.getDealPattern().equals("接受"))
                {
                    holder.deal.setText("接受");
                    holder.deal.setTextColor(getResources().getColor(R.color.black));
                }
                else if(specialBook.getDealPattern().equals("拒绝"))
                {
                    holder.deal.setText("拒绝");
                    holder.deal.setTextColor(getResources().getColor(R.color.red));
                }

                holder.canteenName.setText(specialBook.getCanteenName());
                holder.textDate.setText("时间:"+specialBook.getDate().toString());

                if(specialBook.getType().equals("b"))
                {
                    holder.type.setText("用餐类型:早餐");
                }
                else if(specialBook.getType().equals("l"))
                {
                    holder.type.setText("用餐类型:午餐");
                }
                else if(specialBook.getType().equals("d"))
                {
                    holder.type.setText("用餐类型:晚餐");
                }
                else
                {
                    holder.type.setText("用餐类型:错误");
                }

                holder.spot.setText("使用情景:"+specialBook.getSpot());
                holder.num.setText("人数:"+specialBook.getNum());
                holder.other.setText("备注:"+specialBook.getOther());
            }
            @Override
            public int getItemCount(){
                return thisSbookList.size();
            }
        }


    }
}
