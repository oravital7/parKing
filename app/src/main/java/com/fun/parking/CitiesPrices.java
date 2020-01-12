package com.fun.parking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.fun.parking.customer.orderHistory.Order;
import com.fun.parking.customer.orderHistory.OrderAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CitiesPrices extends AppCompatActivity {
    private ArrayList<String> mOrderList;
    private FirebaseFirestore mFstore;
    private OrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities_prices);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mOrderList = new ArrayList<>();
        mFstore = FirebaseFirestore.getInstance();

//        adapter = new OrderAdapter(mOrderList, this);
//        recyclerView.setAdapter(adapter);
        mOrderList.add("Tel Aviv");
    }
}
