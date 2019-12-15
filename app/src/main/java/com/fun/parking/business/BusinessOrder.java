package com.fun.parking.business;

import android.os.Bundle;
import android.widget.TextView;

import com.fun.parking.BaseActivity;
import com.fun.parking.R;

public class BusinessOrder extends BaseActivity {
protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.business_order);
    String start=getIntent().getStringExtra("startDate");
    String end=getIntent().getStringExtra("endDate");
    final TextView info=(TextView)findViewById(R.id.info1);
    final TextView info1=(TextView)findViewById(R.id.info2);
    final TextView info3=(TextView)findViewById(R.id.info3);
    String good="You offer your parking spot between:\n";
    info.setText(start);
    info1.setText(good);
    info3.setText(end);

}
}
