package com.weakwire.clusteredmapviewOLD;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.weakwire.mapclustering.R;

import java.util.ArrayList;
import java.util.List;

public class MMapView extends MapView {

	private List<Overlay> mapOverlays;
	private Drawable drawable;
	private Drawable drawableMe;
	private MMapViewOverlay itemizedOverlay;
	private MMapViewOverlay myItemizedOverlay;
	private List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
	private List<GeoPoint> clusteredGeoPoints = new ArrayList<GeoPoint>();
	private GeoPoint myGeoPoint;
	private int oldZoomLevel = -1;
	private Context context;

	public MMapView(final Context context, String apiKey) {
		super(context, apiKey);
		this.context = context;
		_init();
	}

	public MMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
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
		itemizedOverlay = new MMapViewOverlay(drawable, context);
		myItemizedOverlay = new MMapViewOverlay(drawableMe, context);
	}

	private GeoPoint getPoint(double lat, double lon) {
		return (new GeoPoint((int) (lat * 1000000.0), (int) (lon * 1000000.0)));
	}

	public void putPoint(double lat, double lon, boolean isMe) {
		GeoPoint geoPoint = getPoint(lat, lon);

		/*
		 * Remove doubles
		 */
		Boolean alreadyExists = false;
		for (GeoPoint item : geoPoints) {
			if (item.getLatitudeE6() == geoPoint.getLatitudeE6()
					&& item.getLongitudeE6() == geoPoint.getLongitudeE6()) {
				alreadyExists = true;
			}
		}
		if (!alreadyExists && !isMe) {
			geoPoints.add(geoPoint);
		}
		/*
		 * Position
		 */
		if (isMe) {
			myGeoPoint = geoPoint;
			getController().animateTo(geoPoint);
			getController().setZoom(15);
		}
	}

	/*
	 * Place the overlays
	 */

	public void placeOverlays() {
		if (itemizedOverlay.size() > 0) {
			itemizedOverlay.doCluster(this);
			return;
		}
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
