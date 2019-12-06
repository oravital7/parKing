package com.fun.parking.customer;

import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.fun.parking.R;
import com.fun.parking.customer.pickers.DatePickerFragment;
import com.fun.parking.customer.pickers.TimePickerFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.ui.IconGenerator;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Calendar;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mfusedLocationClient;
    private int mStartDate[], mEndDate[], mStartTime[], mEndTime[];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_activity_maps);
        mfusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mStartDate = new int[3];
        mEndDate = new int[3];
        mStartTime = new int[2];
        mEndTime = new int[2];
        resetTimes();
        UpdateTexts();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void resetTimes()
    {
        Calendar calendar = Calendar.getInstance();
        mEndDate[0] = mStartDate[0] = calendar.get(Calendar.DAY_OF_MONTH);
        mEndDate[1] = mStartDate[1] = calendar.get(Calendar.MONTH) + 1;
        mEndDate[2] = mStartDate[2] = calendar.get(Calendar.YEAR);

        mEndTime[0] = mStartTime[0] = calendar.get(Calendar.HOUR_OF_DAY);
        mEndTime[1] = mStartTime[1] = calendar.get(Calendar.MINUTE);
        mEndTime[0]++;
    }

    public void UpdateTexts()
    {
        EditText startDate = findViewById(R.id.startDate);
        EditText endDate = findViewById(R.id.endDate);
        EditText startTime = findViewById(R.id.startTime);
        EditText endTime = findViewById(R.id.endTime);
        TextView hours = findViewById(R.id.hoursView);

        checkDateAndTime();

        if (mStartDate[2] == mEndDate[2] && mStartDate[1] == mEndDate[1])
            hours.setText("" + ((mEndDate[0] - mStartDate[0]) * 24 + mEndTime[0] - mStartTime[0]));
        else
            hours.setText("");

        String minutes = mStartTime[1] < 10 ? "0" + mStartTime[1] : "" + mStartTime[1];

        startDate.setText(mStartDate[0] + " / " + mStartDate[1] + " / " + mStartDate[2]);
        startTime.setText(mStartTime[0] + " : " + minutes);

        minutes = mEndTime[1] < 10 ? "0" + mEndTime[1] : "" + mEndTime[1];

        endDate.setText(mEndDate[0] + " / " + mEndDate[1] + " / " + mEndDate[2]);
        endTime.setText(mEndTime[0] + " : " + minutes);
    }

    private void checkDateAndTime()
    {
        if (mStartDate[2] > mEndDate[2] || (mStartDate[2] == mEndDate[2] &&
                (mStartDate[1] > mEndDate[1] || (mStartDate[1] == mEndDate[1] &&
                        (mStartDate[0] > mEndDate[0] || (mStartDate[0]  == mEndDate[0] && (mStartTime[0] > mEndTime[0]
                        || (mStartTime[0] == mEndTime[0] && mStartTime[1] > mStartTime[1]))))))))
        {
            for (int i = 0; i < mStartDate.length; i++)
            {
                mEndDate[i] = mStartDate[i];

                if (i < mStartTime.length)
                    mEndTime[i] = mStartTime[i];
            }

        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        Log.d("onMapReady", "pressed");
        mMap = googleMap;
        try {
            mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.customer_map));
        } catch (Resources.NotFoundException e)
        {
            Log.e("customer_map", "Can't find style. Error: ", e);
        }


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(MapActivity.this, "Click on: " +
                        marker.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Add a marker in Sydney and move the camera
        LatLng roshHaain = new LatLng(32.091410, 34.976780);
        LatLng telAviv = new LatLng(31.928289, 34.796131);

        mfusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Toast.makeText(MapActivity.this, ""+ location.getLatitude()
                                + " " + location.getLongitude(), Toast.LENGTH_SHORT);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(
                                new LatLng(location.getLatitude(), location.getLongitude())));
                    }
                });
        IconMakerFactory iconMaker = new IconMakerFactory(new IconGenerator(this));

        InfoWindowData info = new InfoWindowData();
        info.setImage("snowqualmie")
        .setHotel("Hotel : excellent hotels available")
        .setFood("Food : all types of restaurants available")
        .setTransport("Reach the site by bus, car and train.");

        map_window_info_customize customInfoWindow = new map_window_info_customize(this);
//        mMap.setInfoWindowAdapter(customInfoWindow);


        mMap.addMarker(iconMaker.CreateIcon("500$", telAviv)).showInfoWindow();
        mMap.addMarker(iconMaker.CreateIcon("300", roshHaain)).showInfoWindow();
        mMap.addMarker(iconMaker.CreateIcon("250$", new LatLng(31.929125, 34.794872))).showInfoWindow();
        mMap.addMarker(iconMaker.CreateIcon("100$", new LatLng(31.928371, 34.793839)));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(telAviv, 16.0f));
    }

    public void ShowHideBtn(View v)
    {
        Toast.makeText(this, "Show/Hide", Toast.LENGTH_SHORT).show();
        RelativeLayout layout = findViewById(R.id.customer_options_layout);
        if (layout.getVisibility() == View.VISIBLE)
            layout.setVisibility(View.INVISIBLE);
        else
            layout.setVisibility(View.VISIBLE);
    }

    public void DatePicker(View v)
    {
        int date[] = mStartDate;
        if (v.getId() == R.id.endDate) date = mEndDate;

        DialogFragment newFragment = new DatePickerFragment(this, date);
        newFragment.show(getSupportFragmentManager(), "timePicker");

    }

    public void TimePicker(View v)
    {
        int time[] = mStartTime;
        if (v.getId() == R.id.endTime) time = mEndTime;

        DialogFragment newFragment = new TimePickerFragment(this, time);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
}
