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


public abstract class VisitScenario {
	public static final int MAX_SEARCH_RADIUS=5280;
 	public final String[] EmptyStrings={};
	
	public abstract int getTotalVisitSearches(); 
	public abstract PoiLatLon getNextChosenPoi();
	public abstract String getChosenPoiName();
	public abstract String getChosenPoiClassName();
	
	public abstract String[] getNearestEssentialPoiClassOnceMatchStrings();
	public abstract String[] getNearestEssentialPoiNameOnceMatchStrings();
	
	public abstract int getNearestOptionalPoiNameOrMatchOptionsNum();
	public abstract String[] getNearestEssentialPoiNameOrMatchStrings();
	public abstract int getNearestEssentialPoiClassOrMatchOptionsNum();
	public abstract String[] getNearestEssentialPoiClassOrMatchStrings();
	
	public abstract String[] getNearestOptionalPoiNameOrMatchStrings();
	public abstract String[] getNearestOptionalPoiClassOrMatchStrings();
	
	public int getTotalMapLayers() {
		return 5;
	}
	
	
}
