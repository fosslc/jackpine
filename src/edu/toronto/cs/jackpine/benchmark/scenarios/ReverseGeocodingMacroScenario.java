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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

import org.apache.log4j.Logger;

import edu.toronto.cs.jackpine.benchmark.db.SpatialSqlDialect;

/** 
 * 
 * @author sray
 */
public class ReverseGeocodingMacroScenario extends SpatialScenarioBase
{
  private static final Logger logger = Logger.getLogger(ReverseGeocodingMacroScenario.class);

  protected PreparedStatement[] pstmtArray;
  
  double mLon =-97.74449; 
  double mLat =30.413524; 

  Random rnd = new Random();
  
  SpatialSqlDialect dialect = null;
  
  /** Create a prepared statement array. */
  public void prepare() throws Exception
  {
	  dialect = helper.getSpatialSqlDialect();
	  
      pstmtArray = new PreparedStatement[2];
    
      String sql = dialect.getCityStateForReverseGeocoding();
      pstmtArray[0] = conn.prepareStatement(sql);
      
      sql = dialect.getStreetAddressForReverseGeocoding();
      pstmtArray[1] = conn.prepareStatement(sql);

  }

  /** Execute an iteration. */
  public void iterate(long iterationCount) throws Exception
  {
     double OFFSET =  0.01;
	 double lon = mLon + rnd.nextDouble()/100;
	 double lat =  mLat + rnd.nextDouble()/100;
	 
	 //Postgresql
	 String box = ""; 
	 
	 //if (dialect.getSqlDialectType() == SqlDialect.SupportedSqlDialect.PostgreSQL) 
	 //	 box = "BOX3D("+(lon - OFFSET) + " " + (lat - OFFSET) + "," + (lon + OFFSET) + " " + (lat + OFFSET)+")";
	 //else {
		 
	   	 double topright_lat = lat + OFFSET; 
	   	 double topright_lon = lon + OFFSET; 
	   	   
	   	 double topleft_lat = lat + OFFSET; 
	   	 double topleft_lon = lon - OFFSET; 
	   	   
	   	 double bottomleft_lat = lat - OFFSET; 
	   	 double bottomleft_lon = lon - OFFSET;  
	   	   
	   	 double bottomright_lat = lat - OFFSET; 
	   	 double bottomright_lon = lon + OFFSET;
	   	    
		 box =  "POLYGON (("+topright_lon+" "+topright_lat+", "+topleft_lon+" "+topleft_lat+", "+bottomleft_lon+" "+bottomleft_lat+", "+bottomright_lon+" "+bottomright_lat+", "+topright_lon+" "+topright_lat+"))"; 
	 //}	 
		 
     PreparedStatement pstmt1 = pstmtArray[0]; //city state
     
     String pointParam = "POINT("+lon+" "+lat+")";
     pstmt1.setString(1, pointParam);

         
     PreparedStatement pstmt2 = pstmtArray[1]; //Street Address
     pointParam = "POINT("+lon+" "+lat+")";
     pstmt2.setString(1, pointParam);
     pstmt2.setString(2, box);
     pstmt2.setString(3, pointParam);
    
     
     logger.warn(pstmt1.toString());
     logger.warn(pstmt2.toString());
     
     // Do the query.
     pstmt1.executeQuery();
     pstmt2.executeQuery();
  }

  /** Clean up resources used by scenario. */
  public void cleanup() throws Exception
  {
    // Clean up connections. 
    for (int i = 0; i < pstmtArray.length; i++)
      pstmtArray[i].close();
    if (conn != null)
      conn.close();
  }
 
}