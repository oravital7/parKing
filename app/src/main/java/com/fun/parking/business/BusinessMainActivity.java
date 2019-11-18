package com.fun.parking.business;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import com.fun.parking.R;
import com.google.android.material.navigation.NavigationView;

public class BusinessMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.business_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
         navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_frawer_open, R.string.navigation_frawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }
//define which fragment should be presented when selecting from the menu
    //not complete
    public boolean onNavigationItemSelected(@NonNull MenuItem mi) {
        switch (mi.getItemId()) {
            case R.id.nav_message:
                PickDate m = new PickDate();
                FragmentManager f = getSupportFragmentManager();
                f.beginTransaction().replace(R.id.fragment_container, m).commit();
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
