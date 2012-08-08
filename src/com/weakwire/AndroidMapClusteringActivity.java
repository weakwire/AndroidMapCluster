package com.weakwire;

import android.os.Bundle;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.weakwire.mapviewcluster.CMapView;
import com.weakwire.mapviewcluster.ClusteredGeoPoint;

import java.util.ArrayList;
import java.util.List;

public class AndroidMapClusteringActivity extends MapActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CMapView mapView = new CMapView(this, "0C7QSs4CCB5WjrVf0RdQOWW1WYsyC3eAKdTtWSw");
        setContentView(mapView);
        mapView.setPoints(generateDummyPoints()).setMaxPoints(20);
    }


    private List<GeoPoint> generateDummyPoints() {
        List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
        for (int i = -130; i < 130; i += 40) {
            for (int j = -130; j < 130; j += 40) {
                geoPoints.add(ClusteredGeoPoint.GetPointFromDouble(i, j));
            }
        }
        return geoPoints;
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}