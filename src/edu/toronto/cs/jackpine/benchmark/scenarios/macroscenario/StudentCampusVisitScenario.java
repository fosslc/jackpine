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

import java.util.Random;

public class StudentCampusVisitScenario extends VisitScenario {

	
	public final int TOTAL_POI = 20; 
	
	public final PoiLatLon[] UniversityLocations = {
				new PoiLatLon("American International University","School",-98.5555716,29.5122018),
				new PoiLatLon("Angelo State University","School",-100.4653717, 31.4423837),
				new PoiLatLon("Baylor University","School",-97.119446, 31.5448888),
				new PoiLatLon("Concordia University at San Antonio","School",-98.4546547, 29.5174215),
				new PoiLatLon("Dallas Baptist University","School",-96.9472272, 32.7098543),
				new PoiLatLon("Hardin-Simmons University","School",-98.5960079,29.5079277),
				new PoiLatLon("Howard Payne University","School",-98.9869945,31.7157083),
				new PoiLatLon("Lamar University","School",-93.9268367, 29.8793818),
				new PoiLatLon("Midwestern University","School",-98.5194991, 33.8767648),
				new PoiLatLon("Rice University","School",-95.3974392, 29.7191184),
				new PoiLatLon("Saint Marys University","School",-98.5614079, 29.4530098),
				new PoiLatLon("Texas A and I University","School",-97.8822207,27.5253129),
				new PoiLatLon("Texas A and M University","School",-96.3382958, 30.6176996),
				new PoiLatLon("Texas Southern University","School",-95.3607716, 29.7227292),
				new PoiLatLon("University of Dallas","School",-96.9172268, 32.8456832),
				new PoiLatLon("University of Houston","School",-95.3435488, 29.720507),
				new PoiLatLon("University of Saint Thomas","School",-95.3916057, 29.7396732),
				new PoiLatLon("University of Texas","School",-97.7388943,30.2860412),
				new PoiLatLon("University of Texas at San Antonio","School",-98.622799, 29.5771721),
				new PoiLatLon("University of Texas El Paso","School",-106.5058209, 31.770386)
				//new PoiLatLon(),
		};
		// 5 visits
		final String[] NearestEssentialPoiClassOnceMatch = {"Airport"};
		final String[] NearestEssentialPoiNameOnceMatch = {"Hotel","Library"};

		final String[] NearestEssentialPoiNameOrMatch = {"Dormitory","Hall","Residence"};
		
		final String[] NearestOptionalPoiNameOrMatch = {"Museum","Art Gallery"};
		
		//final String[] NearestEssentialPoiClassOnceMatch = {"Airport","Church","Bar","Hospital","Post Office"};
		//final String[] NearestEssentialPoiNameOnceMatch = {"Hotel","Library"};
		//final String[] NearestEssentialPoiNameOrMatch = {"Dormitory","Hall","Residence"};
		//final String[] NearestOptionalPoiAnyNameMatch = {"Museum","Art Gallery"};
		//final String[] NearestPoiTouristPlaceClasses = {"Beach","Cliff","Crater","Falls","Forest","Harbor","Island","Lake","Park","Spring","Stream","Summit","Swamp","Tower","Trail","Valley","Woods"};
		
		Random mRnd = null;

		PoiLatLon chosenPoi = null;
		
	public StudentCampusVisitScenario() {
		mRnd = new Random();
		getNextChosenPoi();
	}
	
	public 	PoiLatLon getNextChosenPoi() {
    	int univIndex = mRnd.nextInt(TOTAL_POI-1);
		chosenPoi =  UniversityLocations[univIndex];
		return chosenPoi;
	}
	
	public String getChosenPoiName() {
		return chosenPoi.getPoiName();
	}
	public String getChosenPoiClassName(){
		return chosenPoi.getPoiClassName();
	}
	// abs
	public  int getTotalVisitSearches() {
		return NearestEssentialPoiClassOnceMatch.length+NearestEssentialPoiNameOnceMatch.length+1+1+1;
	}
	
	public String[] getNearestEssentialPoiClassOnceMatchStrings() {
		return NearestEssentialPoiClassOnceMatch;
	}
	public String[] getNearestEssentialPoiNameOnceMatchStrings() {
		return NearestEssentialPoiNameOnceMatch;
	}
	
	public int getNearestEssentialPoiClassOrMatchOptionsNum(){
		return 0;
	}
	public String[] getNearestEssentialPoiClassOrMatchStrings() {
		return EmptyStrings;
	}

	public String[] getNearestEssentialPoiNameOrMatchStrings() {
		return NearestEssentialPoiNameOrMatch;
	}
	public String[] getNearestOptionalPoiClassOrMatchStrings() {
		return EmptyStrings;
	}
	
	public int getNearestOptionalPoiNameOrMatchOptionsNum(){
		return 1;
	}
	public String[] getNearestOptionalPoiNameOrMatchStrings() {
		return NearestOptionalPoiNameOrMatch;
	}

}
