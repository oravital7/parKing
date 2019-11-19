package com.fun.parking.business;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fun.parking.R;

import java.util.Calendar;

public class PickDate extends Fragment {
    EditText dateStartText, timeStartText, dateEndText, timeEndText;
    ImageButton dateB1, timeB1, dateBend, timeBend;
    Button price;
    private int mYear, mMonth, mday, mhour, mMin;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myVeiw = inflater.inflate(R.layout.business_fragment_mess, container, false);
        dateB1 = (ImageButton) myVeiw.findViewById(R.id.dateBStart);
        timeB1 = (ImageButton) myVeiw.findViewById(R.id.timeBStart);
        dateStartText = (EditText) myVeiw.findViewById(R.id.DtextStart);
        timeStartText = (EditText) myVeiw.findViewById(R.id.timeTStart);
        dateEndText = (EditText) myVeiw.findViewById(R.id.Dtextend);
        timeEndText = (EditText) myVeiw.findViewById(R.id.timeTend);
        dateBend = (ImageButton) myVeiw.findViewById(R.id.dateBend);
        timeBend = (ImageButton) myVeiw.findViewById(R.id.timeBend);
        price = myVeiw.findViewById(R.id.price);
        timeStartText.setText("Time:");
        dateStartText.setText("Date:");
        timeEndText.setText("Time:");
        dateEndText.setText("Date:");
        final Calendar cal = Calendar.getInstance();
        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PriceSetting P = new PriceSetting();
                FragmentManager f = getActivity().getSupportFragmentManager();
                f.beginTransaction().replace(R.id.fragment_container, P).commit();
            }
        });
        dateB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mYear = cal.get(Calendar.YEAR);
                mMonth = cal.get(Calendar.MONTH);
                mday = cal.get(Calendar.DAY_OF_MONTH);
                //System.out.println(cal.getTime());
                DatePickerDialog d1 = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dateStartText.setText(i2 + "/" + (i1 + 1) + "/" + i);
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
                    }
                }, mhour, mMin, false);
                t1.show();
            }
        });
        timeB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c2 = Calendar.getInstance();
                mhour = c2.get(Calendar.HOUR_OF_DAY);
                mMin = c2.get(Calendar.MINUTE);
                TimePickerDialog t1 = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        timeStartText.setText(i + ":" + i1);
                    }
                }, mhour, mMin, false);
                t1.show();
            }
        });
        return myVeiw;
    }
}
