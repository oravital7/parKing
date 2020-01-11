package com.fun.parking.business;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.fun.parking.BaseActivity;
import com.fun.parking.R;
import com.fun.parking.customer.Orders;
import com.fun.parking.customfonts.MyEditText;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class BusinessMain extends BaseActivity {
    private String userID;
    private String cityS,countryS,streetS,houseNumberS,fullAddress;
    private final Calendar finalCalenderStart = new GregorianCalendar(TimeZone.getTimeZone("Israel"));;
    private final Calendar finalCalenderEnd = new GregorianCalendar(TimeZone.getTimeZone("Israel"));;
    private int Year, Month, day, hour, Min;
    private final Calendar cal = Calendar.getInstance();
    private String start,end;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_main);
        final FirebaseFirestore fStore = FirebaseFirestore.getInstance();        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        fStore.setFirestoreSettings(settings);
        final Button okB=findViewById(R.id.okButton);
        final MyEditText startDate=findViewById(R.id.StartDateText);
        startDate.setText(cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.MONTH)+1+"/"+cal.get(Calendar.YEAR));
        start=cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.MONTH)+1+"/"+cal.get(Calendar.YEAR);
        final MyEditText startTime=findViewById(R.id.StarTimeText);
        final MyEditText endDate=findViewById(R.id.EndDateText);
        endDate.setText(cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.MONTH)+1+"/"+cal.get(Calendar.YEAR));
        end=cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.MONTH)+1+"/"+cal.get(Calendar.YEAR);
        final MyEditText endTime=findViewById(R.id.endHourText);
        final MyEditText country=findViewById(R.id.countryText);
        final MyEditText city=findViewById(R.id.cityText);
        final MyEditText street=findViewById(R.id.streetText);
        final MyEditText houseNumber=findViewById(R.id.houseNumberText);
        final MyEditText price=findViewById(R.id.priceText);
        final FirebaseAuth fAuth = FirebaseAuth.getInstance();

        userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                cityS=documentSnapshot.getString("address.city");
                countryS=documentSnapshot.getString("address.country");
                streetS=documentSnapshot.getString("address.street");
                houseNumberS=documentSnapshot.getString("address.houseNumber");
                street.setText(streetS);
                city.setText(cityS);
                country.setText(countryS);
                houseNumber.setText(houseNumberS);


            }
        });
        okB.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(country.getText().toString())||TextUtils.isEmpty(city.getText().toString())||TextUtils.isEmpty(street.getText().toString())||TextUtils.isEmpty(houseNumber.getText().toString())
                        ||TextUtils.isEmpty(startDate.getText().toString())||TextUtils.isEmpty(startTime.getText().toString())||TextUtils.isEmpty(endTime.getText().toString())||TextUtils.isEmpty(endDate.getText().toString())||TextUtils.isEmpty(price.getText().toString())){
                    Toast.makeText(BusinessMain.this,"Please fill all details",Toast.LENGTH_LONG).show();
                }
                else{
                    CollectionReference documentReference = fStore.collection("availables parking");

                    boolean flag=true;
                    final Intent intent = new Intent(getApplicationContext(), BusinessOrder.class);
                    intent.putExtra("startDate", start);
                    intent.putExtra("endDate", end);


                    HashMap<String, Object> park = new HashMap<String, Object>();
                    Timestamp timestampStart = new Timestamp(finalCalenderStart.getTimeInMillis());
                    Timestamp timestampEnd = new Timestamp(finalCalenderEnd.getTimeInMillis());
                    HashMap<String, Object> addressMap=new HashMap<String, Object>();
                    addressMap.put("Street",street.getText().toString());
                    addressMap.put("City",city.getText().toString());
                    addressMap.put("HouseNumber",houseNumber.getText().toString());
                    addressMap.put("Country",country.getText().toString());
                    HashMap<String, Object> Rent = new HashMap<String, Object>();
                    Rent.put("Start", timestampStart);
                    Rent.put("End", timestampEnd);
                    park.put("Rent", Rent);
                    boolean available=true;
                    park.put("available",available);
                    park.put("Address",addressMap);
                    fullAddress=street.getText().toString()+" "+houseNumber.getText().toString()+" "+city.getText().toString()+" "+country.getText().toString();
                    HashMap<String, Object> location = new HashMap<String, Object>();
                    try {
                        GeoPoint point=new GeoPoint(getLocationFromAddress(BusinessMain.this,fullAddress).latitude,getLocationFromAddress(BusinessMain.this,fullAddress).longitude);

                        if(point==null)
                            flag=false;
                        else
                         park.put("Location",point);
                    } catch (Exception e) {

                    }
                    park.put("userID",userID);

                    park.put("Price",Double.parseDouble(price.getText().toString()));
                   final  StringBuilder parkingKey=new StringBuilder("");
                    if(flag) {

                        fStore.collection("availables parking").add(park).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {
                                    DocumentReference docRef = task.getResult();
                                    parkingKey.append(docRef.getId()) ;
                                    intent.putExtra("parkingId",parkingKey.toString());
                                    startActivity(intent);


                                }
                            }
                        });


                    }
//                    intent.putExtra("parkingId",parkingKey.toString());
//                    startActivity(intent);
                }


            }
        });
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Year = cal.get(Calendar.YEAR);

                Month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog d1 = new DatePickerDialog(BusinessMain.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        startDate.setText(i2 + "/" + i1+1 + "/" + i);
                        start=i2 + "/"+ i1+1 + "/"+ i;
                        finalCalenderStart.set(i, i1+1, i2);
//                        if(cal.after(finalCalenderStart)&&!(cal.equals(finalCalenderStart))){
//                            Toast.makeText(BusinessMain.this,"enter a  valid Date",Toast.LENGTH_LONG).show();
//
//                        }

                    }
                }, Year, Month, day);
                d1.show();
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Year = cal.get(Calendar.YEAR);

                Month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog d1 = new DatePickerDialog(BusinessMain.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        finalCalenderEnd.set(i, i1+1, i2);
                        if(finalCalenderEnd.before(finalCalenderStart)){
                            Toast.makeText(BusinessMain.this,"Please enter valid dates",Toast.LENGTH_LONG).show();
                        }
                        else
                         endDate.setText(i2 + "/" + (i1+1) + "/" + i);
                        end=i2 + "/"+ i1+1 + "/"+ i;

                    }
                }, Year, Month, day);
                d1.show();
            }
        });
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hour = cal.get(Calendar.HOUR_OF_DAY);
                Min = cal.get(Calendar.MINUTE);
                TimePickerDialog t1 = new TimePickerDialog(BusinessMain.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        finalCalenderStart.set(Calendar.HOUR_OF_DAY, i);
                        finalCalenderStart.set(Calendar.MINUTE, i1);
                        String minutes = finalCalenderStart.get(Calendar.MINUTE) < 10 ? "0" +
                                finalCalenderStart.get(Calendar.MINUTE) : "" + finalCalenderStart.get(Calendar.MINUTE);
                        startTime.setText(finalCalenderStart.get(Calendar.HOUR_OF_DAY)+ ":" +minutes);


                    }
                }, hour, Min, false);
                t1.show();
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hour = cal.get(Calendar.HOUR_OF_DAY);
                Min = cal.get(Calendar.MINUTE);
                TimePickerDialog t1 = new TimePickerDialog(BusinessMain.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {

                        finalCalenderEnd.set(Calendar.HOUR_OF_DAY, i);
                        finalCalenderEnd.set(Calendar.MINUTE, i1);
                        String minutes = finalCalenderEnd.get(Calendar.MINUTE) < 10 ? "0" +
                                finalCalenderEnd.get(Calendar.MINUTE) : "" + finalCalenderEnd.get(Calendar.MINUTE);
                        endTime.setText(finalCalenderEnd.get(Calendar.HOUR_OF_DAY)+ ":" +minutes);


                    }
                }, hour, Min, false);
                t1.show();
            }
        });
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
