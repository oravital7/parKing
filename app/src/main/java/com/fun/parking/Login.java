
package com.fun.parking;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fun.parking.customfonts.MyEditText;
import com.fun.parking.customfonts.MyTextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
//    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logint);

       final MyEditText email = findViewById(R.id.Email);
       final MyEditText password = findViewById(R.id.password);
//        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();

        MyTextView loginBtn = findViewById(R.id.loginBtn);

        ImageView sinb = findViewById(R.id.sinb);
        sinb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailStr = email.getText().toString().trim();
                String passwordStr = password.getText().toString().trim();

                if(TextUtils.isEmpty(emailStr)){
                    email.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(passwordStr)){
                    password.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    password.setError("Password Must be >= 6 Characters");
                    return;
                }

//                progressBar.setVisibility(View.VISIBLE);

                // authenticate the user

                fAuth.signInWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else
                        {
                            Toast.makeText(Login.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });

            }
        });

        if(fAuth.getCurrentUser() != null){
           startActivity(new Intent(getApplicationContext(),MainActivity.class));
           finish();
        }
    }
}