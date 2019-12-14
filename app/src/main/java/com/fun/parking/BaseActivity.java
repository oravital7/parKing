package com.fun.parking;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.fun.parking.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class BaseActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public void setContentView(int layoutResID)
    {
        DrawerLayout fullView = (DrawerLayout) getLayoutInflater().inflate(R.layout.base_activity, null);
        FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("parKing");
//        NavigationView navigationView = (NavigationView) findViewById(R.);
//        View headerView = navigationView.getHeaderView(0);
//        TextView navUsername = (TextView) headerView.findViewById(R.id.navUsername);
//        navUsername.setText("Your Text Here");
//        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        View vi = inflater.inflate(R.layout.business_nav_header, null); //log.xml is your file.
//        final TextView name = (TextView)vi.findViewById(R.id.nameProfile);
//        name.setText("vlvlvllvlvlvlvlvlv");//get a reference to the textview on the log.xml file.
//        final FirebaseAuth fAuth = FirebaseAuth.getInstance();
//        final FirebaseFirestore fStore = FirebaseFirestore.getInstance();
//        String userID = fAuth.getCurrentUser().getUid();
//        DocumentReference documentReference = fStore.collection("users").document(userID);
//        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
//                name.setText(documentSnapshot.getString("fName"));
//                String address=documentSnapshot.getString("address.street")+" "+documentSnapshot.getString("address.houseNumber")
//                        +" "+documentSnapshot.getString("address.city")+" "+documentSnapshot.getString("address.country");
//             //  adr.setText(address);
//
//            }
//        });


    }
}