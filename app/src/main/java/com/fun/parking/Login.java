package com.fun.parking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        Toast.makeText(this,"onCreate", Toast.LENGTH_SHORT).show();
        Log.d("a", "adasdasdas");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this,"onStart", Toast.LENGTH_SHORT).show();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        if(currentUser!=null)
        {
            Toast.makeText(this,"connected honey", Toast.LENGTH_SHORT).show();
            Log.d("a", "connected honey");
        }
        else
        {
            Log.d("a", "not connected");
        }
    }
}
