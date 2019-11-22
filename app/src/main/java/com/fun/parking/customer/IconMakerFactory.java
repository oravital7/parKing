package com.fun.parking.customer;

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
        MarkerOptions markerOptionsicon = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
                position(latLng).title("blab afa lfas dlas d\n adsa ");
         return markerOptionsicon;
    }
}
