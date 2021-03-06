package com.fun.parking.customer;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.fun.parking.R;
import com.fun.parking.BaseActivity;
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
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

public class MapActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseFirestore mFstore;
    private FirebaseAuth mFAuth;
    private Calendar mStartDate, mEndDate;
    private String userId, StringStartDate, StringEndDate;

    private LatLng mCurrentLocation;
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_activity_maps);
        mFstore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mFstore.setFirestoreSettings(settings);
        resetTimes();

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mCurrentLocation = new LatLng(32.1039, 35.2082);

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
        mStartDate = new GregorianCalendar(TimeZone.getTimeZone("Israel"));
        mEndDate = new GregorianCalendar(TimeZone.getTimeZone("Israel"));
        mEndDate.set(Calendar.HOUR_OF_DAY, mEndDate.get(Calendar.HOUR_OF_DAY) + 1);
    }

    public void UpdateTexts()
    {
        TextView startDate = findViewById(R.id.startDate);
        TextView endDate = findViewById(R.id.endDate);
        TextView startTime = findViewById(R.id.startTime);
        TextView endTime = findViewById(R.id.endTime);
        TextView hours = findViewById(R.id.hoursView);

        if (mEndDate.compareTo(mStartDate) < 0)
            mEndDate = (Calendar) mStartDate.clone();

        if (mStartDate.get(Calendar.YEAR) == mEndDate.get(Calendar.YEAR) &&
                mStartDate.get(Calendar.MONTH) == mEndDate.get(Calendar.MONTH))
        {
            long hoursDiff[] = getHours();
            String result = "" + hoursDiff[0];
            if (hoursDiff[1] > 0) result += "." + hoursDiff[1];
            hours.setText(result);
        }
        else
            hours.setText("");

        String minutes = mStartDate.get(Calendar.MINUTE) < 10 ? "0" +
                mStartDate.get(Calendar.MINUTE) : "" + mStartDate.get(Calendar.MINUTE);

        startDate.setText(mStartDate.get(Calendar.DAY_OF_MONTH) + " / " +
                (mStartDate.get(Calendar.MONTH) + 1) + " / " + mStartDate.get(Calendar.YEAR));
        startTime.setText(mStartDate.get(Calendar.HOUR_OF_DAY) + " : " + minutes);
        StringStartDate =mStartDate.get(Calendar.DAY_OF_MONTH) + " / " +
                (mStartDate.get(Calendar.MONTH) + 1) + " / " + mStartDate.get(Calendar.YEAR) +
                " "+mStartDate.get(Calendar.HOUR_OF_DAY) + " : " + minutes;
        minutes = mEndDate.get(Calendar.MINUTE) < 10 ? "0" +
                mEndDate.get(Calendar.MINUTE) : "" + mEndDate.get(Calendar.MINUTE);

        endDate.setText(mEndDate.get(Calendar.DAY_OF_MONTH) + " / " +
                (mEndDate.get(Calendar.MONTH) + 1) + " / " + mEndDate.get(Calendar.YEAR));
        endTime.setText(mEndDate.get(Calendar.HOUR_OF_DAY) + " : " + minutes);

        StringEndDate = mEndDate.get(Calendar.DAY_OF_MONTH) + " / " +
                (mEndDate.get(Calendar.MONTH) + 1) + " / " + mEndDate.get(Calendar.YEAR) +
                " " + mEndDate.get(Calendar.HOUR_OF_DAY) + " : " + minutes;
        addAvailableParkingMarkers();

    }
    public long[] getHours()
    {
        long diff = mEndDate.getTimeInMillis() - mStartDate.getTimeInMillis();
        long minutes = (diff / 1000) / 60;
        long hours = minutes / 60;
        minutes %= 60;

        long result[] = {hours, minutes};

        return result;
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

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                mFAuth = FirebaseAuth.getInstance();
                userId = mFAuth.getCurrentUser().getUid();
                final String parkingId=marker.getTitle();
                //update the firebase
                boolean taken=false;
                final Intent intent = new Intent(getApplicationContext(), OrderSummaryActivity.class);
                long hoursDiff[] = getHours();
                final double pricePerHour = Double.parseDouble(marker.getSnippet());
                final double totalPrice = pricePerHour * (hoursDiff[0] + hoursDiff[1] / 60.0);
                DocumentReference documentReference = mFstore.collection("availables parking").document(marker.getTitle());

                documentReference.get().addOnCompleteListener(
                        new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot document = task.getResult();
                                String city = document.getString("Address.City");
                                String street = document.getString("Address.Street");
                                String country = document.getString("Address.Country");
                                String houseNum = document.getString("Address.HouseNumber");
                                final HashMap<String, Object> orders = new HashMap<String, Object>();
                                final HashMap<String, Object> rent = new HashMap<String, Object>();
                                final HashMap<String, Object> priceMap = new HashMap<String, Object>();
                                final HashMap<String, Object> adr = new HashMap<String, Object>();
                                adr.put("City", city);
                                adr.put("Country", country);
                                adr.put("HouseNumber", houseNum);
                                adr.put("Street", street);
                                priceMap.put("Price per hour", pricePerHour);
                                priceMap.put("Total Price", totalPrice);
                                rent.put("End", new Timestamp(mEndDate.getTimeInMillis()));
                                rent.put("Start", new Timestamp(mStartDate.getTimeInMillis()));
                                orders.put("Rent", rent);
                                orders.put("Price", priceMap);
                                orders.put("Address", adr);
                                orders.put("Parking id: ", parkingId);
                                orders.put("UserId", userId);
                                mFstore.collection("orders").add(orders);

                                intent.putExtra("startDate", StringStartDate);
                                intent.putExtra("endDate", StringEndDate);
                                intent.putExtra("city", adr.get("City").toString());
                                intent.putExtra("street", adr.get("Street").toString());
                                intent.putExtra("houseNumber", adr.get("HouseNumber").toString());
                                intent.putExtra("total price", "" + totalPrice);
                                startActivity(intent);
                            }});
                documentReference.update("available",false);
            }});

        try {
            Location location =  mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null)
                mCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (SecurityException e){}

        UpdateTexts();

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLocation, 16.0f));
    }


    private void addAvailableParkingMarkers()
    {
        mMap.clear();

        final IconMakerFactory iconMaker = new IconMakerFactory();

        MapWindowInfoCustomize customInfoWindow = new MapWindowInfoCustomize(this);
        mMap.setInfoWindowAdapter(customInfoWindow);
        mMap.addMarker(iconMaker.createCurrentLocationIcon(mCurrentLocation)).setTag("currentLocation");

        mFstore.collection("availables parking")
                .whereLessThanOrEqualTo("Rent.Start", mStartDate.getTime())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null)
                        {
                            int parkingSum = 0;
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                if (document.getDate("Rent.End").compareTo(mEndDate.getTime()) >= 0
                                        && document.getBoolean("available"))
                                {
                                    parkingSum++;
                                    Marker m = mMap.addMarker(iconMaker.CreateIcon(document));
                                    InfoWindowData info = new InfoWindowData();
                                    info.setAddress(document.get("Address.City") + ", " +
                                            document.get("Address.Street")+" "+document.get("Address.HouseNumber"))
                                            .setPrice("₪" + document.get("Price") + " per hour");

                                    m.setTag(info);
                                    m.setSnippet("" + document.get("Price"));
                                    m.showInfoWindow();
                                }
                            }

                            TextView parkingCount = findViewById(R.id.parkingCount);
                            if (parkingSum > 0)
                                parkingCount.setText("Found " + parkingSum + " parking in your area");
                            else
                                parkingCount.setText("We couldn't find any parking in your area, Try different dates");

                        } else {
                            Log.d("Map: ", "Error getting documents: ", task.getException());
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
            showHideBtn.setText("Show");
        }
        else
        {
            layout.setVisibility(View.VISIBLE);
            showHideBtn.setText("Hide");
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