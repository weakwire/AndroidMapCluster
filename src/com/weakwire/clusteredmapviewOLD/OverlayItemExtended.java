package com.weakwire.clusteredmapviewOLD;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

import java.util.Stack;

public class OverlayItemExtended extends OverlayItem {
	/**
	 */
	public boolean isMe = false;
	/**
	 */
	public boolean isClustered = false;
	/**
	 */
	public boolean isMaster = true;
	/**
	 */
	public OverlayItemExtended parent;
	/**
	 */
	public Stack<OverlayItemExtended> slaves = new Stack<OverlayItemExtended>();

	public OverlayItemExtended(GeoPoint point, String title, String snippet) {
		super(point, title, snippet);
	}


}
