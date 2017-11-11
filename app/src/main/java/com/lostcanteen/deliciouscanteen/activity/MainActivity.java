package com.lostcanteen.deliciouscanteen.activity;

import com.lostcanteen.deliciouscanteen.Fragment.*;
import com.lostcanteen.deliciouscanteen.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private HomeFragment home;
    private MineMessageFragment mine;
    private Toolbar toolbar;
    private TextView title;

    private String usename;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    title.setText(R.string.title_home);
                    transaction.replace(R.id.content,home);
                    transaction.commit();
                    return true;
                case R.id.navigation_dashboard:
                    title.setText(R.string.title_find);
                    transaction.replace(R.id.content,home);
                    transaction.commit();
                    return true;
                case R.id.navigation_notifications:
                    title.setText(R.string.title_mine);
                    transaction.replace(R.id.content,mine);
                    transaction.commit();
                    return true;
            }
            return false;
        }

    };

    private void setDefaultFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content,home);
        transaction.commit();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = (TextView) findViewById(R.id.textMainTitle);
        title.setText(R.string.title_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("");
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        usename = intent.getStringExtra("loginUsername");
        mine = MineMessageFragment.newInstance(usename);

        home = HomeFragment.newInstance(usename);

        setDefaultFragment();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    public void onBackPressed(){
        moveTaskToBack(true);
    }

}
