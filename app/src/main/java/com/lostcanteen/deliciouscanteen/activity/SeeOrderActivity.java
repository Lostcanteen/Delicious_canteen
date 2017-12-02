package com.lostcanteen.deliciouscanteen.activity;

import com.lostcanteen.deliciouscanteen.R;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

public class SeeOrderActivity extends AppCompatActivity {
    private Toolbar seeOrderToolbar;
    private TextView seeOrderTitle;
    private ImageView adminImage;
    private TextView adminName;
    private TabLayout tabLayout;
    private ViewPager seeOrderBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_order);











    }
}
