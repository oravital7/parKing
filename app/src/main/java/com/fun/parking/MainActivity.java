package com.fun.parking;

import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser  =mAuth.getCurrentUser();
        if (currentUser != null)
            Toast.makeText(this, "in!", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "out!", Toast.LENGTH_LONG).show();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
       //DatabaseReference myRef = database.getReferenc

       //
//        myRef.setValue("Hello, World!");
    }
    @Override
    public void onStart() {
        super.onStart();
//         Check if user is signed in (non-null) and update UI accordingly.
        mAuth.createUserWithEmailAndPassword("dana@gmail.com", "1234")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                           @Override
                                           public void onComplete(@NonNull Task<AuthResult> task) {
                                               if (task.isSuccessful())
                                                   Toast.makeText(MainActivity.this, "connected!", Toast.LENGTH_LONG).show();
                                               else
                                               {
                                                   Toast.makeText(MainActivity.this, "not connected " + task.getException(), Toast.LENGTH_LONG).show();

                                               }
                                           }
                                       }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "not connected " + e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        FirebaseUser currentUser = mAuth.getCurrentUser();
       //  Check if user is signed in (non-null) and update UI accordingly.
//        mAuth.createUserWithEmailAndPassword("dana@gmai.com", "12Sidnid34")
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                           @Override
//                                           public void onComplete(@NonNull Task<AuthResult> task) {
//                                               if (task.isSuccessful())
//                                                   Toast.makeText(MainActivity.this, "connected!", Toast.LENGTH_LONG).show();
//                                               else
//                                                   Toast.makeText(MainActivity.this, "not connected", Toast.LENGTH_LONG).show();
//                                           }
//                                       });

        //mAuth.signOut();
//        mAuth.signInWithEmailAndPassword("dana@gmai.com", "12Sidnid34");
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if (currentUser != null)
//            Toast.makeText(this, "in!", Toast.LENGTH_LONG).show();
//        else
//            Toast.makeText(this, "out!", Toast.LENGTH_LONG).show();

    }

}
