package com.weakwire.mapviewcluster;

import android.content.Context;
import android.graphics.*;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

import java.util.ArrayList;
import java.util.List;

public class CMapView extends MapView {

    private List<ClusteredGeoPoint> geoPoints = new ArrayList<ClusteredGeoPoint>();
    private List<GeoPoint> originalPoints = new ArrayList<GeoPoint>();
    private int maxPoints=10;
    private static final String TAG = "CMapView";
    private MyOverlay overlay = new MyOverlay();

    public CMapView(final Context context, String apiKey) {
        super(context, apiKey);
        setClickable(true);
        setBuiltInZoomControls(true);
        getOverlays().add((overlay));
    }

    public CMapView setPoints(List<GeoPoint> geoPoints) {
        originalPoints = geoPoints;
        refreshPoints();
        invalidate();
        return this;
    }
    public CMapView setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
        refreshPoints();
        invalidate();
        return this;
    }

    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        refreshPoints();
    }

    private List<GeoPoint> getVisiblePoints(List<GeoPoint> geoPoints) {
        ArrayList<GeoPoint> visiblePoints = new ArrayList<GeoPoint>();
        for (GeoPoint point : geoPoints) {
            if (isCurrentLocationVisible(point)) {
                visiblePoints.add(point);
            }
        }
        return visiblePoints;
    }

    private boolean isCurrentLocationVisible(GeoPoint point) {
        Rect currentMapBoundsRect = new Rect();
        Point currentDevicePosition = new Point();

        getProjection().toPixels(point, currentDevicePosition);
        getDrawingRect(currentMapBoundsRect);

        return currentMapBoundsRect.contains(currentDevicePosition.x,
                currentDevicePosition.y);

    }

    private static double findDistance(float x1, float y1, float x2, float y2) {
        return Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
    }

    private void refreshPoints() {
        this.geoPoints = DVMClusterer.getClusteredPoints(getVisiblePoints(originalPoints), maxPoints);
    }

    private class MyOverlay extends Overlay {

        private Point pointC = new Point();
        private Point pointO = new Point();
        private Paint solidPaint = new Paint();
        private Paint redPaint = new Paint();
        private Paint textPaint = new Paint();

        public MyOverlay() {
            solidPaint.setColor(Color.BLUE);
            solidPaint.setAntiAlias(true);
            redPaint.setColor(Color.RED);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(22);
        }

        @Override
        public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
            Projection projection = mapView.getProjection();
            for (ClusteredGeoPoint geoPoint : geoPoints) {
                projection.toPixels(geoPoint, pointC);
                projection.toPixels(geoPoint.getMainPoint(), pointO);
                double radius = findDistance(pointC.x, pointC.y, pointO.x, pointO.y);
                if (geoPoint.isClustered()) {
                    solidPaint.setAlpha(50);
                    canvas.drawCircle(pointC.x, pointC.y, 10 + (float) radius / 2, solidPaint);
                    canvas.drawText(geoPoint.getWeight() + "", pointC.x, pointC.y, textPaint);
                } else {
                    solidPaint.setAlpha(255);
                    canvas.drawCircle(pointC.x, pointC.y, 10 + (float) radius / 2, solidPaint);
                }
            }
            return super.draw(canvas, mapView, shadow, when);
        }

    }


}
