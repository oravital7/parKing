package com.fun.parking.business;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fun.parking.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class VacationRent extends Fragment {
    EditText dateStartText, timeStartText, dateEndText, timeEndText, street,city,country,price;
    ImageButton dateStart, timeStart, dateBend, timeBend, returnB;
    Button ok;
    final Calendar finalCalenderStart = Calendar.getInstance();
    final Calendar finalCalenderEnd = Calendar.getInstance();
    private int mYear, mMonth, mday, mhour, mMin;
    private String address,userID;
    private int pricePerHour;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myVeiw = inflater.inflate(R.layout.business_fragment_vacation, container, false);
        final FirebaseAuth fAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                street.setText(documentSnapshot.getString("address.street")+" "+documentSnapshot.getString("address.houseNumber"));
                city.setText(documentSnapshot.getString("address.city"));
                country.setText(documentSnapshot.getString("address.country"));
                address=documentSnapshot.getString("address.street")+" "+documentSnapshot.getString("address.houseNumber")+" "+documentSnapshot.getString("address.city")+" "+documentSnapshot.getString("address.country");
            }
        });
        street = (EditText) myVeiw.findViewById(R.id.street);
        street.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    address="";
                    address = street.getText().toString()+" ";
                }

            }
        });
        city = (EditText) myVeiw.findViewById(R.id.city);
        city.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    address += city.getText().toString()+" ";
                }

            }
        });
        country = (EditText) myVeiw.findViewById(R.id.country);
        country.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    address += country.getText().toString()+" ";
                }

            }
        });

        dateStart = (ImageButton) myVeiw.findViewById(R.id.dateBStart);
        timeStart = (ImageButton) myVeiw.findViewById(R.id.timeBStart);
        dateStartText = (EditText) myVeiw.findViewById(R.id.DtextStart);
        timeStartText = (EditText) myVeiw.findViewById(R.id.timeTStart);
        dateEndText = (EditText) myVeiw.findViewById(R.id.Dtextend);
        price=(EditText)myVeiw.findViewById(R.id.price);
        price.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    pricePerHour=Integer.parseInt( price.getText().toString());

                }

            }
        });
        timeEndText = (EditText) myVeiw.findViewById(R.id.timeTend);
        dateBend = (ImageButton) myVeiw.findViewById(R.id.dateBend);
        timeBend = (ImageButton) myVeiw.findViewById(R.id.timeBend);
        ok=(Button)myVeiw.findViewById(R.id.ok);
        final Calendar cal = Calendar.getInstance();
        String final_dates = "you offer your spot for the following dates:";
        final Bundle bundle = new Bundle();
        bundle.putString("start", final_dates);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionReference documentReference = fStore.collection("availables parking");
                HashMap<String, Object> park = new HashMap<String, Object>();
                Timestamp timestampStart = new Timestamp(finalCalenderStart.getTimeInMillis());
                Timestamp timestampEnd = new Timestamp(finalCalenderEnd.getTimeInMillis());
                HashMap<String, Object> addressMap=new HashMap<String, Object>();
                addressMap.put("Street",street.getText().toString());
                addressMap.put("City",city.getText().toString());
                addressMap.put("Country",country.getText().toString());
                HashMap<String, Object> Rent = new HashMap<String, Object>();
                Rent.put("Start", timestampStart);
                Rent.put("End", timestampEnd);
                park.put("Rent", Rent);
                park.put("Address",addressMap);

                HashMap<String, Object> location = new HashMap<String, Object>();
                try {
                    GeoPoint point=new GeoPoint(getLocationFromAddress(getActivity(),address).latitude,getLocationFromAddress(getActivity(),address).longitude);

                    park.put("Location",point);
                } catch (Exception e) {

                }
                park.put("userID",userID);
                park.put("Price",pricePerHour);
                park.put("available", true);
                fStore.collection("availables parking").add(park);
//                FragmentManager f = getActivity().getSupportFragmentManager();
//                f.beginTransaction().replace(R.id.fragment_container, P).commit();
            }
        });
        dateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mYear = cal.get(Calendar.YEAR);

                mMonth = cal.get(Calendar.MONTH);
                mday = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog d1 = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dateStartText.setText(i2 + "/" + (i1 + 1) + "/" + i);
                        finalCalenderStart.set(i, i1, i2);
                        bundle.putString("startDate", i2 + "/" + (i1 + 1) + "/" + i);
                    }
                }, mYear, mMonth, mday);
                d1.show();

            }
        });

        dateBend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal2 = Calendar.getInstance();
                mYear = cal2.get(Calendar.YEAR);
                mMonth = cal2.get(Calendar.MONTH);
                mday = cal2.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog d1 = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//                        int j = cal.compareTo(cal2);
//                        System.out.println(j);
//                        if(cal2.get(Calendar.MONTH)<cal.get(Calendar.MONTH)){
//
//                            Toast.makeText(getActivity(),"Please choose a valid date",Toast.LENGTH_LONG).show();
//                        }


                        dateEndText.setText(i2 + "/" + (i1 + 1) + "/" + i);
                        bundle.putString("endDate", i2 + "/" + (i1 + 1) + "/" + i);
                        finalCalenderEnd.set(i, i1, i2);
                    }
                }, mYear, mMonth, mday);
                d1.show();
            }
        });
        timeBend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c2 = Calendar.getInstance();
                mhour = c2.get(Calendar.HOUR_OF_DAY);
                mMin = c2.get(Calendar.MINUTE);
                TimePickerDialog t1 = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        timeEndText.setText(i + ":" + i1);
                        finalCalenderEnd.set(Calendar.HOUR_OF_DAY, i);
                        finalCalenderEnd.set(Calendar.MINUTE, i1);
                        System.out.println("start: " + finalCalenderStart.getTime().toString() + "end:" + finalCalenderEnd.getTime().toString());


                    }
                }, mhour, mMin, false);
                t1.show();
            }
        });
        timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c2 = Calendar.getInstance();
                mhour = c2.get(Calendar.HOUR_OF_DAY);
                mMin = c2.get(Calendar.MINUTE);
                TimePickerDialog t1 = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        timeStartText.setText(i + ":" + i1);
                        finalCalenderStart.set(Calendar.HOUR_OF_DAY, i);
                        finalCalenderStart.set(Calendar.MINUTE, i1);


                    }
                }, mhour, mMin, false);
                t1.show();
            }
        });


//wait 3 min and add to firebase
//        Handler handler = new Handler();
//
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                CollectionReference documentReference = fStore.collection("availables parking");
//                HashMap<String, Object> park = new HashMap<String, Object>();
//                Timestamp timestampStart = new Timestamp(finalCalenderStart.getTimeInMillis());
//                Timestamp timestampEnd = new Timestamp(finalCalenderEnd.getTimeInMillis());
//                HashMap<String, Object> addressMap=new HashMap<String, Object>();
//                addressMap.put("Street",street.getText().toString());
//                addressMap.put("City",city.getText().toString());
//                addressMap.put("Country",country.getText().toString());
//                HashMap<String, Object> Rent = new HashMap<String, Object>();
//                Rent.put("Start", timestampStart);
//                Rent.put("End", timestampEnd);
//                park.put("Rent", Rent);
//                park.put("Address",addressMap);
//                HashMap<String, Object> location = new HashMap<String, Object>();
//                try {
//                    GeoPoint point=new GeoPoint(getLocationFromAddress(getActivity(),address).latitude,getLocationFromAddress(getActivity(),address).longitude);
//
//                    park.put("Location",point);
//                } catch (Exception e) {
//
//                }
//                park.put("userID",userID);
//                fStore.collection("availables parking").add(park);
//
//            }
//        }, 30000);


        returnB = myVeiw.findViewById(R.id.returnB);
        returnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity context = getActivity();
                Intent intent = new Intent(context, BusinessMainActivity.class);
                startActivity(intent);
            }
        });
        final PriceSetting P = new PriceSetting();

        P.setArguments(bundle);

        return myVeiw;
    }

    //converting address to latlng
    public LatLng getLocationFromAddress(Context context, String inputtedAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng resLatLng = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(inputtedAddress, 5);
            if (address == null) {
                return null;
            }

            if (address.size() == 0) {
                return null;
            }

            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            resLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        return resLatLng;
    }

}
