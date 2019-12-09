package com.fun.parking.business;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fun.parking.R;

public class PopUpWindow extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View myVeiw = inflater.inflate(R.layout.business_pop_window, container, false);
        DisplayMetrics cm= new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(cm);
        int width=cm.widthPixels;
        int height=cm.heightPixels;
        getActivity().getWindow().setLayout((int)(width*.8),(int)(height*.6));


        return myVeiw;
    }
}