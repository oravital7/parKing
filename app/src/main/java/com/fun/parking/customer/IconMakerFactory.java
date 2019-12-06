package com.fun.parking.customer;

import com.fun.parking.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.google.android.gms.maps.model.MarkerOptions;

public class IconMakerFactory {

    private IconGenerator iconFactory;

    public IconMakerFactory(IconGenerator iconFactory) {
        this.iconFactory = iconFactory;
        init();
    }

    private void init()
    {
        iconFactory.setStyle(IconGenerator.STYLE_BLUE);
    }

    public MarkerOptions CreateIcon(String text, LatLng latLng)
    {
        return new MarkerOptions().
                icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_parking/*iconFactory.makeIcon(text)*/)).
                position(latLng).title("Name: Or Avital, Address: Tel Aviv ").snippet("Hi there");
    }
}
