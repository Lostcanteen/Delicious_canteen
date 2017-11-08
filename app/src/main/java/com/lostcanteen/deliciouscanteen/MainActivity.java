package com.lostcanteen.deliciouscanteen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.sql.Date;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Boolean b = WebTrans.isPasswordTrue("18845787905","123456");
                        //ArrayList<CanteenDetail> canteen = WebTrans.allCantainDetail();
                        //System.out.println(canteen.size());
                        //String[] a = new String[1];
                        //a[0] = "a";
                       // Boolean b =WebTrans.addCanteen(new CanteenDetail(0,"image","天香"," "," ",true,a,1));
                      //  ArrayList<CanteenDetail> canteen = WebTrans.adminCantainDetail(1);
                       // System.out.println(canteen.size());
                       // Boolean b = WebTrans.addDish(new Dish(11,0,"锅包肉","image",23,true,true,true,true));
                        //ArrayList<Dish> canteen = WebTrans.listDish(11,'M');
                        //System.out.println(canteen.size());
                        //int[] a={1,2};
                        //Boolean b = WebTrans.addMenu(11,new Date(2017,11,7),a,'D');
                        //ArrayList<Dish> dish = WebTrans.listMenu(11,new Date(2017,11,7),'B');
                        //System.out.println(dish.size());
                        //int[] a={1,2}; int[] c = {3,4};
                        //Boolean b = WebTrans.commitBook(11,new Date(2017,11,7),'b',1,a,c);
                        //System.out.println(WebTrans.adminBookQuery(11,new Date(2017,11,7),'b').get(0));
                        //Boolean b = WebTrans.commitEva(new Evaluation(11,1,"18845787905",5,new Date(2017,11,7),"非常好吃"));
                        /*if(b){
                                 Intent intent = new Intent(MainActivity.this, AddCanteen.class);
                                  startActivity(intent);
                              }*/
                        System.out.println(WebTrans.dishEvaQuery(11,1).size());
                    }
                }).start();
            }
        });
    }
}
