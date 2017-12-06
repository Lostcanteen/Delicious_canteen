package com.lostcanteen.deliciouscanteen.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lostcanteen.deliciouscanteen.activity.ArticleListActivity;
import com.lostcanteen.deliciouscanteen.R;
import com.lostcanteen.deliciouscanteen.activity.AddSbookActivity;

/**
 * Created by zhou on 2017/11/13.
 */

public class FindFragment extends Fragment {
    private LinearLayout health;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.find, container, false);
        health = (LinearLayout)view.findViewById(R.id.health);

        health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ArticleListActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
