package com.lonetiger.onetracker.Other;

import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class AnimateCamera {


    public void animateMarker(final GoogleMap map, final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = map.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
// anim start latlng
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);



        final long duration = 1000;

        final LinearInterpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * toPosition.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t) * startLatLng.latitude;

                marker.setPosition(new LatLng(lat, lng));

                /*
                if(followCameraNow == true){

                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(userMarker.get(tempCameraFollowMarker).getPosition()));

                }
                */

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {

                        //marker.setVisible(false);

                    } else {

                        //  marker.setVisible(true);

                    }
                }
            }
        });
    }

}
