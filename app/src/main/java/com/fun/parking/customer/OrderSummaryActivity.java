package com.fun.parking.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fun.parking.MainActivity;
import com.fun.parking.R;
import com.fun.parking.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrderSummaryActivity extends BaseActivity {
    private TextView orders,mCity, mStreet, mStartTime,mEndTime, mDates,mPrice;
    private Button mMenu;
    private FirebaseAuth fAuth;
    private String userId,mess,mParkingID;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_orders);
        //orders=findViewById(R.id.street);
        mCity =findViewById(R.id.city);
        mStreet =findViewById(R.id.street);
        mEndTime =findViewById(R.id.endTime);
        mStartTime =findViewById(R.id.startTime);
        mPrice =findViewById(R.id.price);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
 //       mParkingID = getIntent().getStringExtra("ParkingID");


        mCity.setText("City:  "+getIntent().getStringExtra("city"));
        mStreet.setText("Street:  "+getIntent().getStringExtra("street"));
        mStartTime.setText("Start:  "+getIntent().getStringExtra("startDate"));
        mEndTime.setText("End:  "+getIntent().getStringExtra("endDate"));
        mPrice.setText("Total price:  "+getIntent().getStringExtra("total price"));

        mMenu = findViewById(R.id.rent);
//
//
        mMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


//        mess="";
//        mess+=" \n"+"you rent a parking spot in "+getIntent().getStringExtra("address")+" between "+getIntent().getStringExtra("startDate")+
//                " to "+getIntent().getStringExtra("endDate")+"\n"+"Total price:"+getIntent().getStringExtra("total price")+"\n"+"Thank you for using parKing";
//        orders.setText(mess);


    }
}
