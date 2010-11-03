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

public class TouristVisitScenario extends VisitScenario {


	/*
The Childrens Museum in New Braunfels                   | Building | POINT(-98.0686183 29.6924474)
The Childrens Museum of Houston                         | Building | POINT(-95.4082728 29.7591172)
Houston Museum of Natural Science                       | Building | POINT(-95.3893835 29.7216183)
 United States Army Air Defense Artillery Museum         | Building | POINT(-106.4263747 31.795108)
 Wilderness Park Museum                                  | Building | POINT(-106.4541541 31.9056601)
The Museum of Southern History                          | Building | POINT(-95.7682833 29.6269018)
Galveston County Historical Museum                      | Building | POINT(-94.7929736 29.3055145)
Ripleys Believe It Or Not Museum                        | Building | POINT(-98.4866836 29.425233)
 San Antonio Art League Museum                           | Building | POINT(-98.4914059 29.4171777)
	Republic of the Rio Grande Museum                       | Building | POINT(-99.5053198 27.5025182)
	Texas Air Command Museum                                | Building | POINT(-97.0908424 32.6634669)
 Texas State Museum of History                           | Building | POINT(-97.1300106 32.7362425)
	Austin Childrens Museum                                 | Building | POINT(-97.7625058 30.2729859)
 Austin History Center                                   | Building | POINT(-97.7458388 30.2715973)
 Austin Nature and Science Center                        | Building | POINT(-97.7777838 30.2693747)
George Washington Carver Museum                         | Building | POINT(-97.7238936 30.2693754)
 Grand Lake                | Lake  | POINT(-95.4757734 30.2657676)
 Kickapoo Falls    | Falls | POINT(-97.9878163 32.5454128)
 Pedernales Falls  | Falls | POINT(-98.2547401 30.3396432)
 Gorman Falls      | Falls | POINT(-98.4822578 31.0582263)
 Horseshoe Falls   | Falls | POINT(-98.1791772 29.858552)
 Table Mountain         | Summit | POINT(-99.8231411 32.0243099)
 Tackett Mountain       | Summit | POINT(-98.7806191 33.0028941)
 Tallow Face Mountain   | Summit | POINT(-98.4769961 32.413744)
 
 */


 
	final int TOTAL_POI = 12;  
	
	final PoiLatLon[] TouristSites = {
				new PoiLatLon("Johnson Space Center Museum","Building",-98.4546547, 29.5174215),
				new PoiLatLon("Armand Bayou Nature Center","Building",-95.0985417,29.5952301),
				new PoiLatLon("Texas State Capitol","Building",-97.7408387,30.274375),
				new PoiLatLon("Six Flags Over Texas","Park",-97.0730646,32.7548531),
				new PoiLatLon("Padre Island National Seashore","Park",-97.3835942,26.9503319),
				new PoiLatLon("Texas Motor Speedway","Park",-97.2808501,33.0351231),
				new PoiLatLon("Lake Worth Beach","Beach",-97.4530759,32.8181838),
				new PoiLatLon("Holiday Beach","Beach",-97.058877,27.8222496),
				new PoiLatLon("East Shore","Beach",-97.1141558,27.8611367),
				new PoiLatLon("Corpus Christi Beach","Beach",-97.3869366,27.8222484),
				new PoiLatLon("Guadalupe Mountains","Range",-104.8605035,31.891227),
				new PoiLatLon("Target Hill","Summit",-98.9942034, 29.9771596)
				/*
				new PoiLatLon("","",,),
				new PoiLatLon("","",,),
				new PoiLatLon("","",,),
				new PoiLatLon("","",,),
				new PoiLatLon("","",,),
				new PoiLatLon("","",,),
				new PoiLatLon("","",,),
				*/
		};
		
	    // 5 visits
		final String[] NearestEssentialPoiClassOnceMatch = {"Airport"};
		final String[] NearestEssentialPoiNameOnceMatch = {"Hotel"};
		
		//final String[] NearestEssentialPoiNameOrMatchOpt1 = {"Tank","Creek"};
		
		int NearestEssentialPoiClassOrMatchOptionsTotNum=4;
		final String[][] NearestEssentialPoiClassOrMatchOpts = { {"Park","Forest","Woods","Trail"},
																 {"Cliff","Summit","Crater","Valley"},
																 {"Lake","Spring","Stream","Swamp"},
																 {"Beach","Harbor","Island"}
															};
		
		//final String[] NearestOptionalPoiNameOrMatch = {"Museum","Art Gallery"};
		final String[] NearestOptionalPoiClassOrMatch = {"Church","Hospital","Post Office","Bar"};
		

		//final String[] NearestEssentialPoiClassOnceMatch = {"Airport","Church","Bar","Hospital","Post Office"};
		//final String[] NearestPoiTouristPlaceClasses = {"Beach","Cliff","Crater","Falls","Forest","Harbor","Island","Lake","Park","Spring","Stream","Summit","Swamp","Tower","Trail","Valley","Woods"};
		
		
		
		Random mRnd = null;
		PoiLatLon chosenPoi = null;
		
	public TouristVisitScenario() {
		mRnd = new Random();
		getNextChosenPoi();
	}
	
	public 	PoiLatLon getNextChosenPoi() {
    	int univIndex = mRnd.nextInt(TOTAL_POI-1);
		chosenPoi =  TouristSites[univIndex];
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
		return NearestEssentialPoiClassOnceMatch.length+NearestEssentialPoiNameOnceMatch.length+2+1;
	}
	public String[] getNearestEssentialPoiClassOnceMatchStrings() {
		return NearestEssentialPoiClassOnceMatch;
	}
	public String[] getNearestEssentialPoiNameOnceMatchStrings() {
		return NearestEssentialPoiNameOnceMatch;
	}
	
	public int getNearestEssentialPoiClassOrMatchOptionsNum(){
		return 2;
	}
	public String[] getNearestEssentialPoiClassOrMatchStrings() {
		Random rndClassOpt =  new Random(System.currentTimeMillis());
		int opt = rndClassOpt.nextInt(NearestEssentialPoiClassOrMatchOptionsTotNum);
		return NearestEssentialPoiClassOrMatchOpts[opt];
	}
    
	public String[] getNearestEssentialPoiNameOrMatchStrings() {
		return EmptyStrings;
	}
	public String[] getNearestOptionalPoiClassOrMatchStrings() {
		return NearestOptionalPoiClassOrMatch;
	}
	
	public int getNearestOptionalPoiNameOrMatchOptionsNum(){
		return 0;
	}
	public String[] getNearestOptionalPoiNameOrMatchStrings() {
		return EmptyStrings;
	}

}
