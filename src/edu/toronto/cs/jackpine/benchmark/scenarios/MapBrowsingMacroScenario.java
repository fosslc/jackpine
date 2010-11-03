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

package edu.toronto.cs.jackpine.benchmark.scenarios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Random;
import java.util.StringTokenizer;

import edu.toronto.cs.jackpine.benchmark.scenarios.macroscenario.*;

import org.apache.log4j.Logger;

import edu.toronto.cs.jackpine.benchmark.db.SpatialSqlDialect;

import com.ibm.spatial.geom.Geometry;
import com.ibm.spatial.io.IfxSQLData;
import com.ibm.spatial.srs.CoordRefManager;

/**
 * Implements a scenario where a user searches for a point of interest (such as restaurant, hospital).
 * Given a zoom level, the map displays that region with the point of interest as the center.
 * The user can zoom in/out or pan.
 * 
 * Assumption: image width is 800px. a pixel on a screen is 0.28mm on each side.
 * For a map scale of 1:2000 and an image width of 800px, the UTM bounding box width will be:
 *  map_realwidth_m = 2000 x (800 x 0.00028) = 448 meters
 *  
 *  � P0(lat0,lon0) : initial position (unit : degrees) 
 *	� dx,dy : random offsets from your initial position in meters
 *	An approximation to compute the position of the randomized position:
 *	lat = lat0 + (180/pi)*(dy/6378137) 
 *	lon = lon0 + (180/pi)*(dx/6378137)/cos(lat0) 
 *	This is quite precise as long as the random distance offset is below 10-100 km
 *
 * Useful references:
 *  http://www.britishideas.com/2009/09/22/map-scales-and-printing-with-mapnik/
 *  http://www.sharpgis.net/post/2007/10/14/Accurate-distance-calculations-in-UTM-projections.aspx
 *  http://stackoverflow.com/questions/2839533/adding-distance-to-a-gps-coordinate
 *  
 * 
 * @author  sray
 */
public class MapBrowsingMacroScenario extends SpatialScenarioBase
{
  private static final Logger logger = Logger.getLogger(MapBrowsingMacroScenario.class);

  private static final int MAX_NUM_SCENARIOS = 2;

  private Random scenarioPickerRnd = null;
  
  private VisitScenario visitScenario = null;
  
  protected PreparedStatement siteSearchPstmt;
  protected PreparedStatement[] searchScenarioPstmtArray;
  protected PreparedStatement[] boundingBoxPstmtArray;
  
  SpatialSqlDialect dialect = null;
  /** Create a prepared statement array. */
  public void prepare() throws Exception
  {
	 scenarioPickerRnd = new Random(System.currentTimeMillis());
	 //int pickedScenario = scenarioPickerRnd.nextInt(MAX_NUM_SCENARIOS); 
	 int pickedScenario = 1; //TODO: change it to previous line
	 
	 if (pickedScenario==0) {
		 visitScenario =  new StudentCampusVisitScenario();
	 }
	 else if (pickedScenario==1) {
		 visitScenario =  new TouristVisitScenario();
	 }
	 //if more scenarios...
	 
	  dialect = helper.getSpatialSqlDialect(); 
	  
	 // prepare the queries
	 String sql = dialect.getMapSearchSiteSearchQuery(visitScenario);
	 logger.warn(" ????? Preparing: " + sql);
	 siteSearchPstmt  =  conn.prepareStatement(sql);
	 
	 int numQry= visitScenario.getTotalVisitSearches();
	 searchScenarioPstmtArray =  new PreparedStatement[numQry];
	 
	 String[] sqls = dialect.getMapSearchScenarioQueries(visitScenario);
	 for (int i=0;i<numQry;i++) {
		 if (sqls[i] != null) {
			 logger.warn(" ????? Preparing: " + sqls[i]);		 
			 searchScenarioPstmtArray[i]=conn.prepareStatement(sqls[i]);
		 }
	 }
	 
	 numQry= visitScenario.getTotalMapLayers();
	 boundingBoxPstmtArray=  new PreparedStatement[numQry];
	 
	 sqls = dialect.getMapBrowseBoundingBoxQueries();
	 for (int i=0;i<numQry;i++) {
		 if (sqls[i] != null) {
			 logger.warn(" ????? Preparing: " + sqls[i]);
			 boundingBoxPstmtArray[i]=conn.prepareStatement(sqls[i]);
		 }
	 }

  }

  /** Execute an interation. */
  public void iterate(long iterationCount) throws Exception
  {
	  if (dialect.getSqlDialectType() == SpatialSqlDialect.SupportedSqlDialect.Informix) {
		  runMapBrowsingScenario4Informix(conn);
	  }
	  else 
		  runMapBrowsingScenario(conn);
  }

  /** Clean up resources used by scenario. */
  public void cleanup() throws Exception
  {
    // Clean up connections. 
	 siteSearchPstmt.close();
	  
    for (int i = 0; i < searchScenarioPstmtArray.length; i++) {
    	if (searchScenarioPstmtArray[i] != null)
    		searchScenarioPstmtArray[i].close();
    }
    
    for (int i = 0; i < boundingBoxPstmtArray.length; i++) {
    	if (boundingBoxPstmtArray[i] != null)
    		boundingBoxPstmtArray[i].close();
    }
    
    if (conn != null)
      conn.close();
  }
  
  private void runMapBrowsingScenario4Informix(Connection conn) throws SQLException, ClassNotFoundException
  {	  
	  java.util.Map typeMap =  null;
	  try {
		  typeMap=IfxSQLData.enableTypes(conn);
		  // set the CoordRefManager connection
  		  CoordRefManager crm=CoordRefManager.getInstance();
  		  crm.setConnection(conn);
	  }
	  catch (Exception e) {
		  e.printStackTrace();
	  }
	  
	 logger.warn(siteSearchPstmt.toString()); 
  	 ResultSet r = siteSearchPstmt.executeQuery();
  	 String location = "";
  	 String name = "";
  	 double locLon = 0;
  	 double locLat = 0;
  	 
  	 int siteGid = 0;
  	 double siteFoundLocLon = 0;
  	 double siteFoundLocLat = 0;
  	 
  	 if (r.next())   {
  		   siteGid = r.getInt(1);	 //gid or 
  	       name = r.getString("name");
  	       //location = r.getString("location");
  	       Geometry g = (Geometry)r.getObject("location", typeMap);
  	       location =  g.asText();
  	       
  	       logger.warn("\nFound:"+name+" at:" + location);
  	       if (!location.equals("")) {
  	        		location = location.substring(location.indexOf("(")+1, location.indexOf(")"));
  	        		StringTokenizer st = new  StringTokenizer(location," ");
  	        		locLon = Double.parseDouble(st.nextToken());
  	        		locLat = Double.parseDouble(st.nextToken());
  	        		
  	        		siteFoundLocLon = locLon;
  	        	    siteFoundLocLat = locLat;	
  	        		displayMap(locLon,locLat,conn);
  	       }
  	       
  	 }
  	 r.close();
  	 
  	 	if (siteFoundLocLon != 0 &&  siteFoundLocLat != 0) {
  	 		 PreparedStatement searchScenarioPstmt = null;
  	 		 
	    	 String pointParam = "POINT("+siteFoundLocLon+" "+siteFoundLocLat+")";
	    	 for (int i=0;i<searchScenarioPstmtArray.length;i++) {
	    		 searchScenarioPstmt = searchScenarioPstmtArray[i];
		    	 if ( searchScenarioPstmt != null ) {	 
		    		 searchScenarioPstmt.setString(1, pointParam);
		    		 searchScenarioPstmt.setString(2, pointParam);
		    		 searchScenarioPstmt.setInt(3,siteGid);
		    		 
		    		 logger.warn(searchScenarioPstmt.toString());
		    	     r = searchScenarioPstmt.executeQuery();
		    	     location = "";
		    	     name = "";
		    	     locLon = 0;
		    	     locLat = 0;
		    	     while (r.next())   {
		    	    	 name = r.getString("name");
		    	    	 //location = r.getString("location");
		    	    	 Geometry g = (Geometry)r.getObject("location", typeMap);
		    	  	     location =  g.asText();
		    	    	 logger.warn("Found:"+name+" at:" + location);
		    	    	 if (!location.equals("")) {
		    	    		 location = location.substring(location.indexOf("(")+1, location.indexOf(")"));
		    	    	     StringTokenizer st = new  StringTokenizer(location," ");
		    	    	     locLon = Double.parseDouble(st.nextToken());
		    	    	     locLat = Double.parseDouble(st.nextToken());
		    	    	        		
		    	    	     displayMap(locLon,locLat,conn);
		    	    	 }      
		    	     }
		    	     r.close();
		    	 }
	    	 } // end for
	    }
		 
  }
  
  private void runMapBrowsingScenario(Connection conn) throws SQLException, ClassNotFoundException
  {	  
	 logger.warn(siteSearchPstmt.toString()); 
  	 ResultSet r = siteSearchPstmt.executeQuery();
  	 String location = "";
  	 String name = "";
  	 double locLon = 0;
  	 double locLat = 0;
  	 
  	 int siteGid = 0;
  	 double siteFoundLocLon = 0;
  	 double siteFoundLocLat = 0;
  	 
  	 if (r.next())   {
  		   siteGid = r.getInt(1);	 //gid or 
  	       name = r.getString("name");
  	       location = r.getString("location");
  	       logger.warn("\nFound:"+name+" at:" + location);
  	       if (!location.equals("")) {
  	        		location = location.substring(location.indexOf("(")+1, location.indexOf(")"));
  	        		StringTokenizer st = new  StringTokenizer(location," ");
  	        		locLon = Double.parseDouble(st.nextToken());
  	        		locLat = Double.parseDouble(st.nextToken());
  	        		
  	        		siteFoundLocLon = locLon;
  	        	    siteFoundLocLat = locLat;	
  	        		displayMap(locLon,locLat,conn);
  	       }
  	       
  	 }
  	 r.close();
  	 
  	 	if (siteFoundLocLon != 0 &&  siteFoundLocLat != 0) {
  	 		 PreparedStatement searchScenarioPstmt = null;
  	 		 
	    	 String pointParam = "POINT("+siteFoundLocLon+" "+siteFoundLocLat+")";
	    	 for (int i=0;i<searchScenarioPstmtArray.length;i++) {
	    		 searchScenarioPstmt = searchScenarioPstmtArray[i];
		    	 if ( searchScenarioPstmt != null ) {	 
		    		 searchScenarioPstmt.setString(1, pointParam);
		    		 searchScenarioPstmt.setString(2, pointParam);
		    		 searchScenarioPstmt.setInt(3,siteGid);
		    		 
		    		 logger.warn(searchScenarioPstmt.toString());
		    	     r = searchScenarioPstmt.executeQuery();
		    	     location = "";
		    	     name = "";
		    	     locLon = 0;
		    	     locLat = 0;
		    	     while (r.next())   {
		    	    	 name = r.getString("name");
		    	    	 location = r.getString("location");
		    	    	 logger.warn("Found:"+name+" at:" + location);
		    	    	 if (!location.equals("")) {
		    	    		 location = location.substring(location.indexOf("(")+1, location.indexOf(")"));
		    	    	     StringTokenizer st = new  StringTokenizer(location," ");
		    	    	     locLon = Double.parseDouble(st.nextToken());
		    	    	     locLat = Double.parseDouble(st.nextToken());
		    	    	        		
		    	    	     displayMap(locLon,locLat,conn);
		    	    	 }      
		    	     }
		    	     r.close();
		    	 }
	    	 } // end for
	    }
		 
  }
  
  private void displayMap(double locLon, double locLat, Connection conn) throws SQLException {
	    
  	  double scale = 4935.5416457795045;
      double map_realwidth_mtr = scale * (4000 * 0.00028);
      double map_realheight_mtr = scale * (2600 * 0.00028);
      
      double lon_dx=map_realwidth_mtr/2;
      double lat_dy=map_realheight_mtr/2; 
   	   
      double topright_lat=0;
      double topright_lon=0;
      
      double topleft_lat=0;
      double topleft_lon =0;
   	   
      double bottomleft_lat =0;
      double bottomleft_lon =0; 
   	  
      double bottomright_lat =0;
      double bottomright_lon =0;
   		   
      if (locLon!=0 && locLat!=0) {
   	   double lat0 = locLat;
   	   double lon0 = locLon;
   	   
   	    topright_lat = lat0 + (180/Math.PI)*(lat_dy/6378137); 
   	    topright_lon = lon0 + (180/Math.PI)*(lon_dx/6378137)/Math.cos(lat0); 
   	   
   	    topleft_lat = lat0 + (180/Math.PI)*(lat_dy/6378137); 
   	    topleft_lon = lon0 - (180/Math.PI)*(lon_dx/6378137)/Math.cos(lat0); 
   	   
   	    bottomleft_lat = lat0 - (180/Math.PI)*(lat_dy/6378137); 
   	    bottomleft_lon = lon0 - (180/Math.PI)*(lon_dx/6378137)/Math.cos(lat0);  
   	   
   	    bottomright_lat = lat0 - (180/Math.PI)*(lat_dy/6378137); 
   	    bottomright_lon = lon0 + (180/Math.PI)*(lon_dx/6378137)/Math.cos(lat0); 
   	    
   	   String verifyURL = "http://www.openstreetmap.org/?minlon="+bottomleft_lon+"&minlat="+bottomleft_lat+"&maxlon="+topright_lon+"&maxlat="+topright_lat+"&box=yes";
   	   logger.warn("verifyURL: "+ verifyURL);
  
   	    for (int i=0;i<boundingBoxPstmtArray.length;i++) {
   	    	 PreparedStatement boundingBoxPstmt = boundingBoxPstmtArray[i];
   	    	 String geomFromTextParam = "POLYGON (("+topright_lon+" "+topright_lat+", "+topleft_lon+" "+topleft_lat+", "+bottomleft_lon+" "+bottomleft_lat+", "+bottomright_lon+" "+bottomright_lat+", "+topright_lon+" "+topright_lat+"))";	 
   	    	 boundingBoxPstmt.setString(1, geomFromTextParam);
   	    	 logger.warn(boundingBoxPstmt.toString());
   	    	 boundingBoxPstmt.executeQuery();

   	    }
      }
  }
 
}