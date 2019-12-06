package com.fun.parking.customer.pickers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import com.fun.parking.customer.MapActivity;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    private int mDate[];
    private MapActivity mMainMap;

    public DatePickerFragment(MapActivity mainMap, int date[])
    {
        mMainMap = mainMap;
        mDate = date;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, mDate[2], mDate[1] - 1, mDate[0]);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day)
    {
        mDate[0] = day;
        mDate[1] = month + 1;
        mDate[2] = year;
        mMainMap.UpdateTexts();
    }
}