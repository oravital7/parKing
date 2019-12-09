package com.fun.parking.customer.pickers;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import com.fun.parking.customer.MapActivity;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private Calendar mCalendar;
    private MapActivity mMainMap;

    public TimePickerFragment(MapActivity mainMap, Calendar calendar)
    {
        mMainMap = mainMap;
        mCalendar = calendar;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this,
                mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE),
                true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute)
    {
        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
        mCalendar.set(Calendar.MINUTE, minute);

        mMainMap.UpdateTexts();
    }
}