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
    final TextView info=(TextView)findViewById(R.id.info1);
    String good="You offer your parking spot between\n"+start;
    info.setText(good);

}
}
