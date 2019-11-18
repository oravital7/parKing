package com.fun.parking.business;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fun.parking.R;

public class PriceSetting extends Fragment {
    Button finish;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myVeiw = inflater.inflate(R.layout.business_price, container, false);
        finish=myVeiw.findViewById(R.id.ok);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return myVeiw;
    }
}