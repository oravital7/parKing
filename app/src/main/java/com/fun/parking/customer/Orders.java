package com.fun.parking.customer;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fun.parking.R;
import com.fun.parking.business.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Orders extends BaseActivity {
    private TextView orders,mCity, mStreet, mStartTime,mEndTime, mDates,mPrice;
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
        mParkingID = getIntent().getStringExtra("ParkingID");

//        DocumentReference documentReference = fStore.collection("available parking").document(mParkingID);
//
//        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                //mCity.setText(documentSnapshot.getString("Address.City"));
//                //mStreet.setText(documentSnapshot.getString("Address.Street"));
//                mTime.setText(documentSnapshot.getString("email"));
//                mDates.setText(documentSnapshot.getString("fName"));
//                mPrice.setText(documentSnapshot.getString("email"));
//
//            }
//        });
   // getIntent().getex
        mCity.setText(getIntent().getStringExtra("city"));
        mStreet.setText(getIntent().getStringExtra("street"));
        mStartTime.setText(getIntent().getStringExtra("startDate"));
        mEndTime.setText(getIntent().getStringExtra("endDate"));
        mPrice.setText(getIntent().getStringExtra("total price"));
//        mess="";
//        mess+=" \n"+"you rent a parking spot in "+getIntent().getStringExtra("address")+" between "+getIntent().getStringExtra("startDate")+
//                " to "+getIntent().getStringExtra("endDate")+"\n"+"Total price:"+getIntent().getStringExtra("total price")+"\n"+"Thank you for using parKing";
//        orders.setText(mess);


    }
}
