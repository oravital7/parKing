package com.fun.parking.business;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fun.parking.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class BusinessMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    TextView welcome_user;
    Button onVacationB,weeklyB,dailyB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.business_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        //NavigationView navigationView = findViewById(R.id.nav_view);
       //  navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_frawer_open, R.string.navigation_frawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        onVacationB=findViewById(R.id.off_on_vacation);
        onVacationB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                VacationRent m = new VacationRent();
                FragmentManager f = getSupportFragmentManager();

                f.beginTransaction().replace(R.id.fragment_container, m).commit();
            }
        });
        welcome_user=findViewById(R.id.welcome);
        welcome_user.setText("ffhhfhfh");
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                welcome_user.setText("welcome back "+(documentSnapshot.getString("fName")));
            }
        });

    }
//define which fragment should be presented when selecting from the menu
    //not complete
    public boolean onNavigationItemSelected(@NonNull MenuItem mi) {

        switch (mi.getItemId()) {
            case R.id.nav_message:

                VacationRent v = new VacationRent();
                FragmentManager f1 = getSupportFragmentManager();
                f1.beginTransaction().replace(R.id.fragment_container,v).commit();

                break;
            case R.id.setting:
                PriceSetting p=new PriceSetting();
                FragmentTransaction f2 = getSupportFragmentManager().beginTransaction();
                f2.replace(R.id.fragment_container,p).commit();


                break;

//            case  R.id.nav_chat:
//                CalendarFragment c=new CalendarFragment();
//                FragmentManager f1 = getSupportFragmentManager();
             //   f1.beginTransaction().replace(R.id.fragment_container, c).commit();



        }
       drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    public void onBackPressed() {
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else
//            super.onBackPressed();
//    }

}
