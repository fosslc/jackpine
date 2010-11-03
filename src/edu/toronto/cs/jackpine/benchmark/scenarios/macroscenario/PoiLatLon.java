/**
 * Jackpine Spatial Database Benchmark 
 *  Copyright (C) 2010 University of Toronto
 * 
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of version 2 of the GNU General Public License as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA
 *
 * Developer: S. Ray
 * Contributor(s): 
 */

package edu.toronto.cs.jackpine.benchmark.scenarios.macroscenario;

public class PoiLatLon {

	String mPoiName;
	String mPoiClassName;
	double mLon;
	double mLat;
	
	public PoiLatLon(String poiName, String className, double lon, double lat) {
		mPoiName = poiName;
		mPoiClassName= className;
		mLon = lon;
		mLat = lat;
	}
	
	public String getPoiName() {
		return mPoiName;
	}
	
	public String getPoiClassName() {
		return mPoiClassName;
	}

	public void setPoiName(String mPoiName) {
		this.mPoiName = mPoiName;
	}

	private double getLon() {
		return mLon;
	}

	private void setLon(double mLon) {
		this.mLon = mLon;
	}

	private double getLat() {
		return mLat;
	}

	private void setLat(double mLat) {
		this.mLat = mLat;
	}

}
