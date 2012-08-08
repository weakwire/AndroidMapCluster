package com.weakwire.mapviewcluster;

import com.google.android.maps.GeoPoint;

/**
 * Created with IntelliJ IDEA.
 * User: weakwire
 * Date: 8/8/12
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClusteredGeoPoint extends GeoPoint {
    private int weight = 1;

    public int getWeight() {
        return weight;
    }

    public float getRadius(){
        if(weight==1){
            return 10;
        }else{
            return 10 + (float)Math.sqrt(weight);
        }
    }

    public GeoPoint getMainPoint() {
        return mainPoint;
    }
    public boolean isClustered(){
        return !equals(mainPoint) ;
    }

    private GeoPoint mainPoint;
    public ClusteredGeoPoint(int lat, int lng, int weight,GeoPoint mainPoint) {
        super(lat,lng);
        this.weight = weight;
        this.mainPoint=mainPoint;
    }
    public ClusteredGeoPoint(GeoPoint point, int weight) {
        super(point.getLatitudeE6(),point.getLongitudeE6());
        this.weight = weight;
    }

    public static GeoPoint GetPointFromDouble(double lat, double lng) {
        return new GeoPoint((int) (lat * 1e6), (int) (lng * 1e6));
    }
}
