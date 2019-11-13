package com.fun.parking.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fun.parking.R;

public class customer_main_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_main_activity);
    }

    public void RentBtn(View v)
    {
        Toast.makeText(this, "RentBtn", Toast.LENGTH_SHORT).show();
    }
}
