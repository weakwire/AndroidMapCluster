package com.weakwire.mapclusterer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.weakwire.clusteredmapviewOLD.OverlayItemExtended;
import com.weakwire.clusteredmapviewOLD.PointCluster;
import com.weakwire.mapclustering.AndroidMapClusteringActivity;

import java.util.ArrayList;
import java.util.List;

public class CMapViewOverlay extends ItemizedOverlay<OverlayItemExtended> {
	/**
	 */
	private List<OverlayItemExtended> mOverlays = new ArrayList<OverlayItemExtended>();

	/**
	 */

	public CMapViewOverlay(Drawable defaultMarker, Context context) {
		super((defaultMarker));
		_init();
	}

	/**
	 */
	private Paint textPaint = new Paint();;
	/**
	 */
	private Paint pointPaint = new Paint();

	private void _init() {
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setTextSize(30);
		textPaint.setAntiAlias(true);
		textPaint.setARGB(150, 0, 0, 0);

		pointPaint.setAntiAlias(true);
		pointPaint.setColor(android.graphics.Color.BLUE);
		pointPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		pointPaint.setAlpha(130);
	}

	@Override
	protected OverlayItemExtended createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	public void addOverlayItem(OverlayItemExtended overlay) {
		mOverlays.add(overlay);
		populate();
	}

	// public void addOverlayItem(OverlayItemExtended thisOverlay,
	// MapView mapView, int totalPoints) {
	// mOverlays.add(thisOverlay);
	// populate();
	// }

	public void addOverlayItemClustered(OverlayItemExtended thisOverlay,
			MapView mapView, int totalPoints) {
		/*
		 * Parent o eautos sou
		 */
		thisOverlay.parent = thisOverlay;
		for (OverlayItemExtended otherOverlay : mOverlays) {
			/*
			 * Thresshold for the clustering
			 */
			/*
			 * Zoom level >15 don't cluster If less than Max_Visible_points
			 * don't cluster
			 */
			if ((mapView.getZoomLevel() >= 14 && PointCluster
					.getOverLayItemDistance(thisOverlay, otherOverlay, mapView) > 60)
					|| PointCluster.MAX_VISIBLE_POINTS > totalPoints) {
				mOverlays.add(thisOverlay);
				populate();
				return;
			}
			if (thisOverlay == otherOverlay)
				continue;
			if (PointCluster.getOverLayItemDistance(thisOverlay, otherOverlay,
					mapView) < 50 && !thisOverlay.isClustered) {

				if (otherOverlay.isMaster) {
					thisOverlay.isMaster = false;
					// otherOverlay.isMaster = false;
					thisOverlay.isClustered = true;
					otherOverlay.isClustered = true;
					otherOverlay.slaves.push(thisOverlay);
					thisOverlay.parent = otherOverlay;
				} else if (PointCluster.getOverLayItemDistance(thisOverlay,
						otherOverlay.parent, mapView) < 240
						&& otherOverlay.isClustered) {
					thisOverlay.isMaster = false;
					thisOverlay.isClustered = true;
					thisOverlay.parent = otherOverlay.parent;
					otherOverlay.parent.slaves.push(thisOverlay);
				}
				break;
			}
		}
		mOverlays.add(thisOverlay);
//		populate();
	}

	public void doCluster(MapView mapView) {
		// for (OverlayItemExtended otherOverlay : mOverlays) {
		if (mOverlays.size() < 2) {
			return;
		}
		for (int i = 1; i < mOverlays.size(); i++) {
			OverlayItemExtended thisOverlay = mOverlays.get(i);
			OverlayItemExtended otherOverlay = mOverlays.get(i - 1);
			/*
			 * Thresshold for the clustering
			 */
			/*
			 * Zoom level >15 don't cluster If less than Max_Visible_points
			 * don't cluster
			 */
			if ((mapView.getZoomLevel() >= 14 && PointCluster
					.getOverLayItemDistance(thisOverlay, otherOverlay, mapView) > 60)
					|| PointCluster.MAX_VISIBLE_POINTS > mOverlays.size()) {
				mOverlays.add(i,thisOverlay);
				populate();
				return;
			}
			if (thisOverlay == otherOverlay)
				continue;
			if (PointCluster.getOverLayItemDistance(thisOverlay, otherOverlay,
					mapView) < 50 && !thisOverlay.isClustered) {

				if (otherOverlay.isMaster) {
					thisOverlay.isMaster = false;
					// otherOverlay.isMaster = false;
					thisOverlay.isClustered = true;
					otherOverlay.isClustered = true;
					otherOverlay.slaves.push(thisOverlay);
					thisOverlay.parent = otherOverlay;
				} else if (PointCluster.getOverLayItemDistance(thisOverlay,
						otherOverlay.parent, mapView) < 240
						&& otherOverlay.isClustered) {
					thisOverlay.isMaster = false;
					thisOverlay.isClustered = true;
					thisOverlay.parent = otherOverlay.parent;
					otherOverlay.parent.slaves.push(thisOverlay);
				}
				break;
			}
		}
	}

	public void removeAllOverlays() {
		mOverlays.clear();
		populate();
	}

	@Override
	protected boolean onTap(int index) {
		if (mOverlays.get(index).isMe) {
			return super.onTap(index);
		} else if (mOverlays.get(index).isClustered) {
			zoomToField(mOverlays.get(index),
					AndroidMapClusteringActivity.mapView);
			return super.onTap(index);
		} else if (AndroidMapClusteringActivity.mapView.getZoomLevel() < 11) {
			AndroidMapClusteringActivity.mapView.getController().setCenter(
					mOverlays.get(index).getPoint());
			AndroidMapClusteringActivity.mapView.getController().setZoom(11);
			return super.onTap(index);
		}
		return false;
	}

	/**
	 * Otan patithei ena clustered item zoomarei se olo to node
	 */
	private void zoomToField(OverlayItemExtended itemPressed, final MapView map) {
		OverlayItemExtended master = itemPressed.parent;
		OverlayItemExtended slave;
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		maxX = Math.max(master.getPoint().getLatitudeE6(), maxX);
		minX = Math.min(master.getPoint().getLatitudeE6(), minX);
		maxY = Math.max(master.getPoint().getLongitudeE6(), maxY);
		minY = Math.min(master.getPoint().getLongitudeE6(), minY);
		while (!master.slaves.isEmpty()) {
			slave = master.slaves.pop();
			maxX = Math.max(slave.getPoint().getLatitudeE6(), maxX);
			minX = Math.min(slave.getPoint().getLatitudeE6(), minX);
			maxY = Math.max(slave.getPoint().getLongitudeE6(), maxY);
			minY = Math.min(slave.getPoint().getLongitudeE6(), minY);
		}

		map.getController().setCenter(
				new GeoPoint((maxX + minX) / 2, (maxY + minY) / 2));
		map.getController()
				.zoomToSpan((int) (maxX - minX), (int) (maxY - minY));
		map.getController().animateTo(
				new GeoPoint((maxX + minX) / 2, (maxY + minY) / 2));

	}

	/**
	 */
	private GeoPoint point;
	/**
	 */
	private Point ptScreenCoord = new Point();;

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {

		super.draw(canvas, mapView, shadow);
		// cycle through all overlays
		for (int index = 0; index < mOverlays.size(); index++) {
			OverlayItemExtended item = mOverlays.get(index);

			// Converts lat/lng-Point to coordinates on the screen
			point = item.getPoint();
			mapView.getProjection().toPixels(point, ptScreenCoord);

			// Paint
			if ((!item.isClustered && mapView.getZoomLevel() > 10) || item.isMe) {

				canvas.drawText("Title :" + item.getTitle(), ptScreenCoord.x,
						ptScreenCoord.y + 30, textPaint);
			}
			if (!item.isMaster || item.isMe)
				continue;
			/*
			 * Draw the fog beween the slaves
			 */
			float minX = Float.MAX_VALUE;
			float maxX = Float.MIN_VALUE;
			float minY = Float.MAX_VALUE;
			float maxY = Float.MIN_VALUE;
			maxX = Math.max(ptScreenCoord.x, maxX);
			minX = Math.min(ptScreenCoord.x, minX);
			maxY = Math.max(ptScreenCoord.y, maxY);
			minY = Math.min(ptScreenCoord.y, minY);
			for (int i = 0; i < item.slaves.size(); i++) {
				OverlayItemExtended slaveItem = item.slaves.get(i);
				GeoPoint slavePoint = slaveItem.getPoint();
				Point slavePtScreenCoord = new Point();
				mapView.getProjection()
						.toPixels(slavePoint, slavePtScreenCoord);
				float x = slavePtScreenCoord.x;
				float y = slavePtScreenCoord.y;

				maxX = Math.max(x, maxX);
				minX = Math.min(x, minX);
				maxY = Math.max(y, maxY);
				minY = Math.min(y, minY);

			}
			float centerX = (maxX + minX) / 2;
			float centerY = (maxY + minY) / 2;
			double distance = findDistance(minX, minY, maxX, maxY);
			canvas.drawCircle(centerX, centerY, (float) (distance / 2) + 10,
					pointPaint);
			if (item.slaves.size() > 0) {
				Paint paint = new Paint();
				paint.setTextAlign(Paint.Align.CENTER);
				paint.setTextSize(45);
				paint.setAntiAlias(true);
				paint.setARGB(255, 0, 0, 0);
				Paint boxPaint = new Paint();
				boxPaint.setColor(android.graphics.Color.WHITE);
				boxPaint.setStyle(Paint.Style.FILL);
				boxPaint.setAlpha(140);
				canvas.drawCircle(centerX, centerY - (paint.getTextSize() / 2),
						paint.getTextSize(), boxPaint);
				canvas.drawText(item.slaves.size() + 1 + "", centerX, centerY,
						paint);
			}

		}

	}

	private double findDistance(float x1, float y1, float x2, float y2) {
		return Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
	}

	public void populate2() {
		// TODO Auto-generated method stub
		populate();
	}
}
