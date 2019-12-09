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
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.ui.IconGenerator;

import java.util.Arrays;
import java.util.Calendar;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseFirestore mFstore;
    private FusedLocationProviderClient mfusedLocationClient;
    private Calendar mStartDate, mEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_activity_maps);
        mfusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFstore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mFstore.setFirestoreSettings(settings);

        resetTimes();

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
        Places.createClient(this);

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
        mStartDate = Calendar.getInstance(); // Need to be change to correct timeZone such utc + 2
        mEndDate = Calendar.getInstance();
        mEndDate.set(Calendar.HOUR_OF_DAY, mEndDate.get(Calendar.HOUR_OF_DAY) + 1);
    }

    public void UpdateTexts()
    {
        EditText startDate = findViewById(R.id.startDate);
        EditText endDate = findViewById(R.id.endDate);
        EditText startTime = findViewById(R.id.startTime);
        EditText endTime = findViewById(R.id.endTime);
        TextView hours = findViewById(R.id.hoursView);

        if (mEndDate.compareTo(mStartDate) < 0)
            mEndDate = (Calendar) mStartDate.clone();

        if (mStartDate.get(Calendar.YEAR) == mEndDate.get(Calendar.YEAR) &&
                mStartDate.get(Calendar.MONTH) == mEndDate.get(Calendar.MONTH))
        {
            hours.setText("" + ((mEndDate.get(Calendar.DAY_OF_MONTH) -
                    mStartDate.get(Calendar.DAY_OF_MONTH)) * 24 +
                    mEndDate.get(Calendar.HOUR_OF_DAY) - mStartDate.get(Calendar.HOUR_OF_DAY)));
        }
        else
            hours.setText("");

        String minutes = mStartDate.get(Calendar.MINUTE) < 10 ? "0" +
                mStartDate.get(Calendar.MINUTE) : "" + mStartDate.get(Calendar.MINUTE);

        startDate.setText(mStartDate.get(Calendar.DAY_OF_MONTH) + " / " +
                (mStartDate.get(Calendar.MONTH) + 1) + " / " + mStartDate.get(Calendar.YEAR));
        startTime.setText(mStartDate.get(Calendar.HOUR_OF_DAY) + " : " + minutes);

        minutes = mEndDate.get(Calendar.MINUTE) < 10 ? "0" +
                mEndDate.get(Calendar.MINUTE) : "" + mEndDate.get(Calendar.MINUTE);

        endDate.setText(mEndDate.get(Calendar.DAY_OF_MONTH) + " / " +
                (mEndDate.get(Calendar.MONTH) + 1) + " / " + mEndDate.get(Calendar.YEAR));
        endTime.setText(mEndDate.get(Calendar.HOUR_OF_DAY) + " : " + minutes);

        addAvailableParkingMarkers();
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

        UpdateTexts();

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


        LatLng test = new LatLng(32.1005821, 34.8817902);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(test, 16.0f));
    }

    private void addAvailableParkingMarkers()
    {
        mMap.clear();

        final IconMakerFactory iconMaker = new IconMakerFactory(new IconGenerator(this));


        map_window_info_customize customInfoWindow = new map_window_info_customize(this);
        mMap.setInfoWindowAdapter(customInfoWindow);


        mFstore.collection("availables parking")
                .whereGreaterThanOrEqualTo("Rent.Start", mStartDate.getTime())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                if (document.getDate("Rent.End").compareTo(mEndDate.getTime()) > 0)
                                {
                                    Marker m = mMap.addMarker(iconMaker.CreateIcon(document));
                                    InfoWindowData info = new InfoWindowData();
                                    info.setAddress(document.get("Address.City") + ", " +
                                            document.get("Address.Street"))
                                            .setPrice("₪" + document.get("Price") + " per hour");

                                    m.setTag(info);
                                    m.showInfoWindow();
                                }
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
        Calendar calendar = mStartDate;
        if (v.getId() == R.id.endDate) calendar = mEndDate;

        DialogFragment newFragment = new DatePickerFragment(this, calendar);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void TimePicker(View v)
    {
        Calendar calendar = mStartDate;
        if (v.getId() == R.id.endTime) calendar = mEndDate;

        DialogFragment newFragment = new TimePickerFragment(this, calendar);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
}