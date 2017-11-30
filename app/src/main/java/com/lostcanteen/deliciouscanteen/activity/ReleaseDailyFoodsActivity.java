package com.lostcanteen.deliciouscanteen.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.lostcanteen.deliciouscanteen.CanteenDetail;
import com.lostcanteen.deliciouscanteen.R;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class ReleaseDailyFoodsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView canteenImageView;
    private FloatingActionButton timeFloatActionButton;
    private TabLayout tabLayout;
    private DatePickerDialog datePickerDialog;



    private CanteenDetail canteenDetail;
    private int adminId;
    private String adminName;


    private Calendar calendar;
    private int year,month,day;
    private Date nowDate;



    private ArrayList<String> tabLayoutList = new ArrayList<String>() {{
        add("早餐");
        add("午餐");
        add("晚餐");
    }};

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
        timeFloatActionButton = (FloatingActionButton)findViewById(R.id.timefloatingActionButton);


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }





}
