package com.weakwire.mapclusterer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.weakwire.clusteredmapviewOLD.OverlayItemExtended;
import com.weakwire.clusteredmapviewOLD.PointCluster;
import com.weakwire.mapclustering.R;
import com.weakwire.pointclusterer.ClusteredGeoPoint;

import java.util.ArrayList;
import java.util.List;

public class CMapView extends MapView {

    private List<Overlay> mapOverlays;
    private Drawable drawable;
    private Drawable drawableMe;
    private CMapViewOverlay itemizedOverlay;
    private CMapViewOverlay myItemizedOverlay;
    private List<ClusteredGeoPoint> geoPoints = new ArrayList<ClusteredGeoPoint>();
    private GeoPoint myGeoPoint;
    private int oldZoomLevel = -1;
    private Context context;

    public CMapView(final Context context, String apiKey) {
        super(context, apiKey);
        this.context = context;
        _init();
    }

    private void _init() {
        setClickable(true);
        setBuiltInZoomControls(true);
        mapOverlays = getOverlays();
        drawable = context.getResources().getDrawable(R.drawable.pin);
        drawableMe = context.getResources().getDrawable(
                android.R.drawable.btn_radio);
        itemizedOverlay = new CMapViewOverlay(drawable, context);
        myItemizedOverlay = new CMapViewOverlay(drawableMe, context);
    }


    public void setPoints(List<ClusteredGeoPoint> geoPoints) {
        this.geoPoints=geoPoints;
    }

    /*
      * Place the overlays
      */

    public void placeOverlays() {
        itemizedOverlay.removeAllOverlays();
        myItemizedOverlay.removeAllOverlays();
        getOverlays().clear();
        mapOverlays.clear();
        clusteredGeoPoints = PointCluster.getPoints(geoPoints, this);
        for (GeoPoint item : clusteredGeoPoints) {
            OverlayItemExtended overlayitem = new OverlayItemExtended(item,
                    null, null);
            // itemizedOverlay.addOverlayItemClustered(overlayitem, this,
            // clusteredGeoPoints.size());
            // itemizedOverlay.addOverlayItem(overlayitem, this,
            // clusteredGeoPoints.size());
            itemizedOverlay.addOverlayItem(overlayitem);

        }
        mapOverlays.add(itemizedOverlay);
        if (myGeoPoint != null) {
            OverlayItemExtended myoverlayitem = new OverlayItemExtended(
                    myGeoPoint, null, null);

            /*
                * Enable click
                */
            myoverlayitem.isMe = true;
            myItemizedOverlay.addOverlayItem(myoverlayitem);
            mapOverlays.add(myItemizedOverlay);
        }
    }

    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (getZoomLevel() != oldZoomLevel) {
            if (getZoomLevel() < 14) {
                // itemizedOverlay = new CMapViewOverlay(drawableMe, context);
            } else {
                // itemizedOverlay = new CMapViewOverlay(drawable, context);
            }
            oldZoomLevel = getZoomLevel();
        }
        placeOverlays();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                placeOverlays();
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

}
