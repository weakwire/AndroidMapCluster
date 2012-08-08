package com.weakwire.mapclustering;

import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.weakwire.clusteredmapview.MMapView2;

public class AndroidMapClusteringActivity extends MapActivity {
	public static MMapView2 mapView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mapView = new MMapView2(this, "0C7QSs4CCB5WjrVf0RdQOWW1WYsyC3eAKdTtWSw");
		setContentView(mapView);
		// mapView.putPoint(21, 22, false);
		// mapView.putPoint(22, 22, false);
		// mapView.putPoint(23, 22, false);
		// mapView.putPoint(24, 22, false);
		// mapView.putPoint(25, 22, false);
		// mapView.putPoint(26, 22, false);
		// mapView.putPoint(27, 22, false);
		// mapView.putPoint(28, 22, false);
		//
		// mapView.putPoint(22, 21, false);
		// mapView.putPoint(22, 22, false);
		// mapView.putPoint(22, 23, false);
		// mapView.putPoint(22, 24, false);
		// mapView.putPoint(22, 25, false);
		// mapView.putPoint(22, 26, false);
		// mapView.putPoint(22, 27, false);
		// mapView.putPoint(22, 28, false);
		// mapView.putPoint(22, 29, false);
		//
		// mapView.putPoint(31, 33, false);
		// mapView.putPoint(33, 33, false);
		// mapView.putPoint(33, 33, false);
		// mapView.putPoint(34, 33, false);
		// mapView.putPoint(35, 33, false);
		// mapView.putPoint(36, 33, false);
		// mapView.putPoint(37, 33, false);
		// mapView.putPoint(38, 33, false);
		//
		// mapView.putPoint(33, 31, false);
		// mapView.putPoint(33, 33, false);
		// mapView.putPoint(33, 33, false);
		// mapView.putPoint(33, 34, false);
		// mapView.putPoint(33, 35, false);
		// mapView.putPoint(33, 36, false);
		// mapView.putPoint(33, 37, false);
		// mapView.putPoint(33, 38, false);
		// mapView.putPoint(33, 39, false);
		placeDummyPoints();
	}

	private void placeDummyPoints() {
		for (int i = -90; i < 90; i += 20) {
			for (int j = -90; j < 90; j += 20) {
				mapView.putPoint(i, j, false);
			}
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}