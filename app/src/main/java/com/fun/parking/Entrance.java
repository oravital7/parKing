package com.fun.parking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fun.parking.customfonts.MyEditText;
import com.fun.parking.customfonts.MyTextView;

public class Entrance extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);

        final MyTextView signup = findViewById(R.id.signupforfree);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Entrance.this, Registration.class));
            }
        });

        final MyTextView sign = findViewById(R.id.sin);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Entrance.this, Login.class));
            }
        });

    }
}
