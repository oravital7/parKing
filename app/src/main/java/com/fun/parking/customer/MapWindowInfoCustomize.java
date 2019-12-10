package com.fun.parking.customer;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.fun.parking.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MapWindowInfoCustomize implements GoogleMap.InfoWindowAdapter {
    private Context context;

    public MapWindowInfoCustomize(Context ctx)
    {
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {return null;}

    @Override
    public View getInfoContents(Marker marker)
    {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.customer_map_window_info_customize, null);

        TextView price = view.findViewById(R.id.mapPrice);
        TextView address = view.findViewById(R.id.mapAddress);

        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

        price.setText(infoWindowData.getPrice());
        address.setText(infoWindowData.getAddress());

        return view;
    }
}
