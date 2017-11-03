package com.lostcanteen.deliciouscanteen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

//从前一页面接受spot canteenid username
//日历控件
public class AddSbook extends AppCompatActivity {

    FlowLayout mTagLayout;
    private ArrayList<TagItem> mAddTags = new ArrayList<TagItem>();
    private String[] spot = {"包厢预订","生日宴会订做"}; //从前一页传过来
    private String chooseSpot;
    ArrayList<String>  list = new ArrayList<String>();
    private int canteenid = 1; //前一页接受
    private String username = "root";//前一页接受

    private Spinner spinnerType;
    private EditText num;
    private EditText others;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sbook);

        //标签
        mTagLayout = (FlowLayout) findViewById(R.id.tag_layout);
        initList();
        initLayout(list);

        spinnerType = (Spinner) findViewById(R.id.spinnerType);
        num = (EditText) findViewById(R.id.num);
        others = (EditText) findViewById(R.id.others);
        Button commit = (Button) findViewById(R.id.commit);

        spinnerType.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = AddSbook.this.getResources().getStringArray(R.array.type)[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date(2017,11,2);  //日历控件获取
                String numstr = num.getText().toString();
                String otherstr = others.getText().toString();
                SpecialBook sb = new SpecialBook(canteenid,username,date,type,chooseSpot,numstr,otherstr);
                try {
                    new DBConnection().commitSBook(sb);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
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

            final View view = (View) LayoutInflater.from(AddSbook.this).inflate(R.layout.text_view_single, mTagLayout, false);

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
}
