package com.fun.parking.customer.pickers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import com.fun.parking.customer.MapActivity;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    private Calendar mCalendar;
    private MapActivity mMainMap;

    public DatePickerFragment(MapActivity mainMap, Calendar calendar)
    {
        mMainMap = mainMap;
        mCalendar = calendar;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day)
    {
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.YEAR, year);

        mMainMap.UpdateTexts();
    }
}