package com.fun.parking;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fun.parking.BaseActivity;
import com.fun.parking.customfonts.MyEditText;
import com.fun.parking.customfonts.MyTextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        final MyEditText mFullName = findViewById(R.id.name);
        final MyEditText mEmail      = findViewById(R.id.Email);
        final MyEditText mPassword   = findViewById(R.id.password);
        final MyEditText mPhone      = findViewById(R.id.phone);
        final MyEditText mCountry = findViewById(R.id.country);
        final MyEditText mStreet = findViewById(R.id.street);
        final MyEditText mCity = findViewById(R.id.city);
        final MyEditText mHouseNumber = findViewById(R.id.houseNumber);
        final MyTextView registerView = findViewById(R.id.sin);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        fStore.setFirestoreSettings(settings);
        final ProgressBar progressBar = findViewById(R.id.registraionProgress);


        registerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                final String name = mFullName.getText().toString();
                final String phone    = mPhone.getText().toString();
                final String country = mCountry.getText().toString();
                final String street    = mStreet.getText().toString();
                final String city = mCity.getText().toString();
                final String houseNumber    = mHouseNumber.getText().toString();

                //   final String
                if(TextUtils.isEmpty(email)|| !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmail.setError("Email is Required.");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password Must be >= 6 Characters");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }


                // register the user in firebase

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Registration.this, "User Created.", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users")
                                    .document(userID);
                            Map<String,Object> user = new HashMap<>();
                            Map<String,Object> address = new HashMap<>();
                            user.put("fName",name);
                            user.put("email",email);
                            user.put("phone",phone);
                            address.put("country",country);
                            address.put("street",street);
                            address.put("city",city);
                            address.put("houseNumber",houseNumber);
                            user.put("address",address);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
                            progressBar.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else {
                            Toast.makeText(Registration.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        ImageView sback = findViewById(R.id.sback);
        sback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}