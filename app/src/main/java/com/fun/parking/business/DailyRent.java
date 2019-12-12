package com.fun.parking.business;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fun.parking.R;

import java.util.Calendar;

public class DailyRent extends Fragment {
ImageButton returnB,date;
EditText dateText;
private int mYear, mMonth, mday, mhour, mMin;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View myVeiw = inflater.inflate(R.layout.business_fragment_daily, container, false);
        returnB=myVeiw.findViewById(R.id.returnB);
        returnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //return to parent activity
               Activity context=getActivity();
                Intent intent=new Intent(context, BusinessMainActivity.class);
                startActivity(intent);
            }
        });
        dateText=myVeiw.findViewById(R.id.dateT);
        final Calendar today = Calendar.getInstance();
        dateText.setText(today.get(Calendar.DAY_OF_MONTH)+"/"+(today.get(Calendar.MONTH)+1)+"/"+today.get(Calendar.YEAR));
        date=myVeiw.findViewById(R.id.startTime);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mYear = today.get(Calendar.YEAR);
                mMonth = today.get(Calendar.MONTH);
                mday = today.get(Calendar.DAY_OF_MONTH);
                System.out.println(today.getTime());
                DatePickerDialog d1 = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dateText.setText(i2 + "/" + (i1 + 1) + "/" + i);
                    }
                }, mYear, mMonth, mday);
                d1.show();
            }
        });
        return myVeiw;
    }
}
