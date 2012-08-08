package com.weakwire.clusteredmapview;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.graphics.Rect;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class PointCluster {
	public final static int MAX_VISIBLE_POINTS = 4;

	private static List<GeoPoint> clusteredGeoPoints;

	public static List<GeoPoint> getVisiblePoints(List<GeoPoint> geoPoints,
			MapView mapView) {
		clusteredGeoPoints = new ArrayList<GeoPoint>();
		// int count = 0;
		for (GeoPoint point : geoPoints) {
			// if (count >= MAX_VISIBLE_POINTS) {
			// return clusteredGeoPoints;
			// }
			if (isCurrentLocationVisible(point, mapView)) {
				clusteredGeoPoints.add(point);
				// count++;
			}
		}
		return clusteredGeoPoints;
	}
	
	public static List<GeoPoint> getPoints(List<GeoPoint> geoPoints,
			MapView mapView) {
		clusteredGeoPoints = new ArrayList<GeoPoint>();
		// int count = 0;
		for (GeoPoint point : geoPoints) {
//			// if (count >= MAX_VISIBLE_POINTS) {
//			// return clusteredGeoPoints;
//			// }
//			if (isCurrentLocationVisible(point, mapView)) {
				clusteredGeoPoints.add(point);
				// count++;
//			}
		}
		return clusteredGeoPoints;
	}

	private static boolean isCurrentLocationVisible(GeoPoint point,
			MapView mapView) {
		Rect currentMapBoundsRect = new Rect();
		Point currentDevicePosition = new Point();

		mapView.getProjection().toPixels(point, currentDevicePosition);
		mapView.getDrawingRect(currentMapBoundsRect);

		return currentMapBoundsRect.contains(currentDevicePosition.x,
				currentDevicePosition.y);

	}

	public static double getOverLayItemDistance(OverlayItemExtended item1,
			OverlayItemExtended item2, MapView mapView) {
		try {
			GeoPoint point = item1.getPoint();
			Point ptScreenCoord = new Point();
			mapView.getProjection().toPixels(point, ptScreenCoord);

			GeoPoint slavePoint = item2.getPoint();
			Point slavePtScreenCoord = new Point();
			mapView.getProjection().toPixels(slavePoint, slavePtScreenCoord);
			return findDistance(ptScreenCoord.x, ptScreenCoord.y,
					slavePtScreenCoord.x, slavePtScreenCoord.y);
		} catch (Exception e) {
			return 400;
		}
	}

	private static double findDistance(float x1, float y1, float x2, float y2) {
		return Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
	}
}
