package com.weakwire.mapviewcluster;

import android.util.Log;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.tomgibara.cluster.gvm.intgr.IntClusters;
import com.tomgibara.cluster.gvm.intgr.IntResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: weakwire
 * Date: 8/8/12
 * Time: 3:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class DVMClusterer {
    private static final String TAG = "DVMClusterer";

    public static List<ClusteredGeoPoint> getClusteredPoints(List<GeoPoint> geoPoints, int maxClusters) {

        int[] xs = new int[2];
        IntClusters<GeoPoint> clusters = new IntClusters<GeoPoint>(2, maxClusters);

        //This is too slow!
        for (GeoPoint point : geoPoints) {
            xs[0] = point.getLongitudeE6();
            xs[1] = point.getLatitudeE6();
            clusters.add(1, xs, point);
        }

        List<ClusteredGeoPoint> clusteredPoints = new ArrayList<ClusteredGeoPoint>();

        List<IntResult<GeoPoint>> results = clusters.results();
        for (IntResult<GeoPoint> clusteredPoint : results) {

            Log.i(TAG, "lng= "
                    + clusteredPoint.getCoords()[0] + " lat= "
                    + clusteredPoint.getCoords()[1]
                    + " weight= "
                    + clusteredPoint.getMass()
                    + "variance = " + clusteredPoint.getKey().getLongitudeE6());

            clusteredPoints.add(
                    new ClusteredGeoPoint(
                            clusteredPoint.getCoords()[1],
                            clusteredPoint.getCoords()[0],
                            clusteredPoint.getMass(),
                            clusteredPoint.getKey())
            );

        }
        return clusteredPoints;
    }

    private static List<GeoPoint> getVisiblePoints(MapView mapView) {
        return new ArrayList<GeoPoint>();
    }

}
