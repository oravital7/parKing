package com.fun.parking.business;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fun.parking.R;

public class PriceSetting extends Fragment {
    Button bPost;
    EditText et;
    TextView test;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myVeiw = inflater.inflate(R.layout.business_setting, container, false);
        bPost = myVeiw.findViewById(R.id.ok);
        et=myVeiw.findViewById(R.id.editText);
        Bundle bundle = this.getArguments();
        test=myVeiw.findViewById(R.id.textView3);
        if(bundle != null){
           // et.setText(bundle.getString("abc"));
            test.setText(bundle.getString("start")+bundle.getString("startDate")+" to "+bundle.getString("endDate"));
        }
        bPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             PopUpWindow p=new PopUpWindow();
                FragmentManager f = getActivity().getSupportFragmentManager();
                f.beginTransaction().replace(R.id.fragment_container,p).commit();
            }
        });
        return myVeiw;
    }
}