package com.fun.parking.customer;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fun.parking.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class Orders extends AppCompatActivity {
    private TextView orders;
    private FirebaseAuth fAuth;
    private String userId,mess;
    private FirebaseFirestore fStore;
    @Override

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_orders);
        orders=findViewById(R.id.order);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        mess="";
        mess+=" \n"+"you rent a parking spot in "+getIntent().getStringExtra("address")+" between "+getIntent().getStringExtra("startDate")+
                " to "+getIntent().getStringExtra("endDate")+"\n"+"Total price:"+getIntent().getStringExtra("total price")+"\n"+"Thank you for using parKing";
        orders.setText(mess);
    }
}
