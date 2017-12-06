package com.lostcanteen.deliciouscanteen.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.lostcanteen.deliciouscanteen.CanteenDetail;
import com.lostcanteen.deliciouscanteen.DBConnection;
import com.lostcanteen.deliciouscanteen.FlowLayout;
import com.lostcanteen.deliciouscanteen.R;
import com.lostcanteen.deliciouscanteen.SpecialBook;
import com.lostcanteen.deliciouscanteen.TagItem;
import com.lostcanteen.deliciouscanteen.WebTrans;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

//从前一页面接受spot canteenid username
//日历控件
public class AddSbookActivity extends AppCompatActivity {

    private TextView title;
    FlowLayout mTagLayout;
    private ArrayList<TagItem> mAddTags = new ArrayList<TagItem>();
    private String[] spot = {"包厢预订","生日宴会订做"}; //从前一页传过来
    private String chooseSpot;
    ArrayList<String>  list = new ArrayList<String>();
    private String canteenName = "学苑"; //前一页接受
    private String username = "root";//前一页接受
    private int adminid = 1;//前一页接受

    private Button datechoose;
    private Spinner spinnerType;
    private EditText num;
    private EditText others;
    private String type;
    private DatePickerDialog datePickerDialog;
    private Toolbar toolbar;

    private int year,month,day;
    private Date nowDate;

    private CanteenDetail canteenDetail;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sbook);

        canteenDetail = (CanteenDetail)getIntent().getSerializableExtra("canteenDetail");
        username = getIntent().getStringExtra("username");
        spot = canteenDetail.getSbookpattern();
        canteenName = canteenDetail.getName();
        adminid = canteenDetail.getAdminid();

        //标签
        mTagLayout = (FlowLayout) findViewById(R.id.tag_layout);
        initList();
        initLayout(list);

        datechoose = (Button) findViewById(R.id.datechoose);
        spinnerType = (Spinner) findViewById(R.id.spinnerType);
        num = (EditText) findViewById(R.id.num);
        others = (EditText) findViewById(R.id.others);
        Button commit = (Button) findViewById(R.id.commit);
        toolbar = (Toolbar) findViewById(R.id.addsbook_toolbar);
        title = (TextView) findViewById(R.id.title);
        title.setText("特殊预约");
        setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        nowDate = java.sql.Date.valueOf(((Integer)year).toString()
                +"-"+((Integer)(month+1)).toString() + "-"+((Integer)day).toString());

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE); // 画框
        drawable.setStroke(2, Color.BLACK); // 边框粗细及颜色
        datechoose.setBackgroundDrawable(drawable);
        datechoose.setText(nowDate.toString());

        spinnerType.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = AddSbookActivity.this.getResources().getStringArray(R.array.type)[i];
                if(type.equals("早餐")) type = "b";
                else if(type.equals("午餐")) type = "l";
                else type = "d";

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        datechoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = new DatePickerDialog(view.getContext(), null,year, month, day);

                datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatePicker picker = datePickerDialog.getDatePicker();
                        year = picker.getYear();
                        month = picker.getMonth();
                        day = picker.getDayOfMonth();
                        nowDate = java.sql.Date.valueOf(((Integer)year).toString()
                                +"-"+((Integer)(month+1)).toString() + "-"+((Integer)day).toString());
                        datechoose.setText(nowDate.toString());

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

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String numstr = num.getText().toString();
                        String otherstr = others.getText().toString();
                        SpecialBook sb = new SpecialBook(0,canteenName,username,nowDate,type,chooseSpot,numstr,otherstr,adminid,"w");
                        WebTrans.commitSBook(sb);
                        finish();
                    }
                }).start();
            }
        });


    }


    private void initList() {
        for(int i=0;i<spot.length;i++){
            list.add(spot[i]);
        }
    }

    private void initLayout(final ArrayList<String> arr) {

        mTagLayout.removeAllViewsInLayout();
        /**
         * 创建 textView数组
         */
        final TextView[] textViews = new TextView[arr.size()];


        for (int i = 0; i < arr.size(); i++) {

            final int pos = i;

            final View view = (View) LayoutInflater.from(AddSbookActivity.this).inflate(R.layout.text_view_single, mTagLayout, false);

            final TextView text = (TextView) view.findViewById(R.id.text);  //查找  到当前     textView

            // 将     已有标签设置成      可选标签
            text.setText(list.get(i));
            /**
             * 将当前  textView  赋值给    textView数组
             */
            textViews[i] = text;

            text.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    chooseSpot = list.get(pos);
                    text.setActivated(!text.isActivated()); // true是激活的

                    if (text.isActivated()) {
                        boolean bResult = doAddText(list.get(pos), false, pos);
                        text.setActivated(bResult);
                    }

                    /**
                     * 遍历  textView  满足   已经被选中     并且不是   当前对象的textView   则置为  不选
                     */
                    for(int j = 0;j< textViews.length;j++){
                        if(!text.equals(textViews[j])){//非当前  textView
                            textViews[j].setActivated(false); // true是激活的

                        }
                    }
                }
            });

            mTagLayout.addView(view);
        }
        //mTagLayout.removeAllViewsInLayout();
        //mTagLayout.addView(inputLabel);
    }

    // 标签索引文本
    protected int idxTextTag(String text) {
        int mTagCnt = mAddTags.size(); // 添加标签的条数
        for (int i = 0; i < mTagCnt; i++) {
            TagItem item = mAddTags.get(i);
            if (text.equals(item.tagText)) {
                return i;
            }
        }
        return -1;
    }

    // 标签添加文本状态
    private boolean doAddText(final String str, boolean bCustom, int idx) {
        int tempIdx = idxTextTag(str);
        if (tempIdx >= 0) {
            TagItem item = mAddTags.get(tempIdx);
            item.tagCustomEdit = false;
            item.idx = tempIdx;
            return true;
        }
        int tagCnt = mAddTags.size(); // 添加标签的条数
        TagItem item = new TagItem();
        item.tagText = str;
        item.tagCustomEdit = bCustom;
        item.idx = idx;
        mAddTags.add(item);
        tagCnt++;
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
