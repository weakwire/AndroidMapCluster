package com.weakwire.mapclustering;

import android.os.Bundle;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.weakwire.mapclusterer.CMapView;
import com.weakwire.pointclusterer.ClusteredGeoPoint;
import com.weakwire.pointclusterer.DummyClusterer;

import java.util.ArrayList;
import java.util.List;

public class AndroidMapClusteringActivity extends MapActivity {
    public static CMapView mapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		mapView = new CMapView(this, "0C7QSs4CCB5WjrVf0RdQOWW1WYsyC3eAKdTtWSw");
		setContentView(mapView);
		placeDummyPoints();
        tests();
    }


    List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
    private void tests() {
        DummyClusterer.getClusteredPoints(geoPoints,10);
    }

    private void placeDummyPoints() {
        for (int i = -90; i < 90; i += 5) {
            for (int j = -90; j < 90; j += 5) {
                geoPoints.add(ClusteredGeoPoint.GetPointFromDouble(i, j));
            }
        }
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}