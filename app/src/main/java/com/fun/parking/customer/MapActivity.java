package com.fun.parking.customer;

import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.fun.parking.R;
import com.fun.parking.customer.pickers.DatePickerFragment;
import com.fun.parking.customer.pickers.TimePickerFragment;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.ui.IconGenerator;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseFirestore mFstore;
    private FusedLocationProviderClient mfusedLocationClient;
    private Calendar mStartDatec, mEndDatec;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_activity_maps);
        mfusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFstore = FirebaseFirestore.getInstance();

        resetTimes();
        UpdateTexts();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initAutoCompletePlaces();
    }

    private void initAutoCompletePlaces()
    {
        String apiKey = "AIzaSyAarkT-eBapC-ZCiJfGxvJGgOZlxfjMXlc";

        // Initialize the SDK
        Places.initialize(getApplicationContext(), apiKey);

        // Create a new Places client instance
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("infoOR", "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("infoOR", "An error occurred: " + status);
            }
        });
    }

    private void resetTimes()
    {
        mStartDatec = Calendar.getInstance(); // Need to be change to correct timeZone such utc + 2
        mEndDatec = Calendar.getInstance();
        mEndDatec.set(Calendar.HOUR_OF_DAY, mEndDatec.get(Calendar.HOUR_OF_DAY) + 1);
    }

    public void UpdateTexts()
    {
        EditText startDate = findViewById(R.id.startDate);
        EditText endDate = findViewById(R.id.endDate);
        EditText startTime = findViewById(R.id.startTime);
        EditText endTime = findViewById(R.id.endTime);
        TextView hours = findViewById(R.id.hoursView);

        if (mEndDatec.compareTo(mStartDatec) < 0)
            mEndDatec = (Calendar) mStartDatec.clone();

        if (mStartDatec.get(Calendar.YEAR) == mEndDatec.get(Calendar.YEAR) &&
                mStartDatec.get(Calendar.MONTH) == mEndDatec.get(Calendar.MONTH))
        {
            hours.setText("" + ((mEndDatec.get(Calendar.DAY_OF_MONTH) -
                    mStartDatec.get(Calendar.DAY_OF_MONTH)) * 24 +
                    mEndDatec.get(Calendar.HOUR_OF_DAY) - mStartDatec.get(Calendar.HOUR_OF_DAY)));
        }
        else
            hours.setText("");

        String minutes = mStartDatec.get(Calendar.MINUTE) < 10 ? "0" +
                mStartDatec.get(Calendar.MINUTE) : "" + mStartDatec.get(Calendar.MINUTE);

        startDate.setText(mStartDatec.get(Calendar.DAY_OF_MONTH) + " / " +
                (mStartDatec.get(Calendar.MONTH) + 1) + " / " + mStartDatec.get(Calendar.YEAR));
        startTime.setText(mStartDatec.get(Calendar.HOUR_OF_DAY) + " : " + minutes);

        minutes = mEndDatec.get(Calendar.MINUTE) < 10 ? "0" +
                mEndDatec.get(Calendar.MINUTE) : "" + mEndDatec.get(Calendar.MINUTE);

        endDate.setText(mEndDatec.get(Calendar.DAY_OF_MONTH) + " / " +
                (mEndDatec.get(Calendar.MONTH) + 1) + " / " + mEndDatec.get(Calendar.YEAR));
        endTime.setText(mEndDatec.get(Calendar.HOUR_OF_DAY) + " : " + minutes);
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

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(MapActivity.this, "Click on: " +
                        marker.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        addAvailableParkingMarkers();

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

    private void addAvailableParkingMarkers()
    {
        mMap.clear();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mFstore.setFirestoreSettings(settings);


//        mFstore.collection("availables parking")
//                .whereGreaterThan("Rent.Start", mStartDatec.getTimeInMillis())
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("Or Map: ", document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.d("Or Map: ", "Error getting documents: ", task.getException());
//                        }
//                    }
//                });

        mFstore.collection("availables parking")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Or Map: ", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("Or Map: ", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void ShowHideBtn(View v)
    {
        Button showHideBtn = (Button)v;
        RelativeLayout layout = findViewById(R.id.customer_options_layout);

        if (layout.getVisibility() == View.VISIBLE)
        {
            layout.setVisibility(View.INVISIBLE);
            showHideBtn.setText("show");
        }
        else
        {
            layout.setVisibility(View.VISIBLE);
            showHideBtn.setText("hide");
        }
    }

    public void DatePicker(View v)
    {
        Calendar calendar = mStartDatec;
        if (v.getId() == R.id.endDate) calendar = mEndDatec;

        DialogFragment newFragment = new DatePickerFragment(this, calendar);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void TimePicker(View v)
    {
        Calendar calendar = mStartDatec;
        if (v.getId() == R.id.endTime) calendar = mEndDatec;

        DialogFragment newFragment = new TimePickerFragment(this, calendar);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
}