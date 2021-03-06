package com.fun.parking.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fun.parking.R;
import com.fun.parking.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_main_activity);
    }

    public void RentBtn(View v)
    {
        Log.d("RentBtn", "pressed");
        Toast.makeText(this, "RentBtn", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MapActivity.class));
    }
}
