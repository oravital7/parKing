package com.fun.parking.customer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.fun.parking.BaseActivity;
import com.fun.parking.R;
import com.fun.parking.customer.orderHistory.Order;
import com.fun.parking.customer.orderHistory.OrderAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HistoryOrdersActivity extends BaseActivity {

    private ArrayList<Order> mOrderList;
    private FirebaseFirestore mFstore;
    private OrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_history_orders);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mOrderList = new ArrayList<>();
        mFstore = FirebaseFirestore.getInstance();

        adapter = new OrderAdapter(mOrderList, this);
        recyclerView.setAdapter(adapter);
        fillOrderList();
    }

    private void fillOrderList()
    {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null)
        {
            mFstore.collection("orders")
                    .whereEqualTo("UserId", currentUser.getUid())
            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {

                    for (QueryDocumentSnapshot document : task.getResult())
                        mOrderList.add(Order.buildWithFireStore(document));

                    adapter.notifyDataSetChanged();
                }
            });
        }
    }


}
