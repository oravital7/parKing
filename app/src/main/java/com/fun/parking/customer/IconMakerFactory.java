package com.fun.parking.customer;

import com.fun.parking.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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

    public MarkerOptions CreateIcon(QueryDocumentSnapshot document)
    {
        GeoPoint geoPoint = document.getGeoPoint("Location");
        LatLng latLng = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
        String title = makeTitle(document);
        return new MarkerOptions().
                icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_parking/*iconFactory.makeIcon(text)*/)).
                position(latLng).title(title).snippet("Price per hour: ₪" + document.get("Price"));
    }

    private String makeTitle(QueryDocumentSnapshot document)
    {
        String title = document.get("Address.City") + ", " + document.get("Address.Street");
        return title;
    }
}
