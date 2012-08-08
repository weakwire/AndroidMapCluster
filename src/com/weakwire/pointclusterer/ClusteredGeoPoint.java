package com.weakwire.pointclusterer;

import com.google.android.maps.GeoPoint;

/**
 * Created with IntelliJ IDEA.
 * User: weakwire
 * Date: 8/8/12
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClusteredGeoPoint extends GeoPoint {
    private GeoPoint point;
    private double weight = 1.0;

    public ClusteredGeoPoint(double lat, double lng, double weight) {
        super((int) (lat * 1e6), (int) (lng * 1e6));
        this.weight = weight;
    }

    public ClusteredGeoPoint(int lat, int lng, double weight) {
        super(lat,lng);
        this.weight = weight;
    }
    public ClusteredGeoPoint(GeoPoint point, double weight) {
        super(point.getLatitudeE6(),point.getLongitudeE6());
        this.weight = weight;
    }

    public static GeoPoint GetPointFromDouble(double lat, double lng) {
        return new GeoPoint((int) (lat * 1e6), (int) (lng * 1e6));
    }
}
