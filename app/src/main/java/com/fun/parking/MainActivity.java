package com.fun.parking;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    Button mRent, mFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRent= findViewById(R.id.rent);
        mFind = findViewById(R.id.findParking);

//        mFind.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, buisness.BuisnessMainActivity.class));
//            }
//        });



    }

}
