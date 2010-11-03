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

import org.apache.log4j.Logger;

import edu.toronto.cs.jackpine.benchmark.db.SpatialSqlDialect;

import edu.toronto.cs.jackpine.benchmark.scenarios.macroscenario.AddressPicker;

/**
 * @author sray
 */
public class GeocodingMacroScenario extends SpatialScenarioBase
{
  private static final Logger logger = Logger.getLogger(GeocodingMacroScenario.class);

  protected PreparedStatement[] pstmtArray;
  
  protected AddressPicker addressPicker= null;
  
  
  /** Create a prepared statement array. */
  public void prepare() throws Exception
  {
	  SpatialSqlDialect dialect = helper.getSpatialSqlDialect(); 
	  
	  pstmtArray = new PreparedStatement[1];
	    
      String sql = dialect. getGeocodingQuery();
      pstmtArray[0] = conn.prepareStatement(sql);
      
      addressPicker =  new AddressPicker();
      
  }

  /** Execute an interation. */
  public void iterate(long iterationCount) throws Exception
  {
	  PreparedStatement pstmt = pstmtArray[0]; 
	  AddressPicker[] addresses= addressPicker.getAllAddresses();
	  for (int i=0;i<addresses.length;i++) {
	  
		  AddressPicker address = addresses[i];
		  pstmt.setString(1, address.getRoadName());
		  
		  pstmt.setInt(2, address.getRoadNumber());
		  pstmt.setInt(3, address.getRoadNumber());
		  pstmt.setInt(4, address.getRoadNumber());
		  pstmt.setInt(5, address.getRoadNumber());
		  pstmt.setInt(6, address.getRoadNumber());
		  pstmt.setInt(7, address.getRoadNumber());
		  pstmt.setInt(8, address.getRoadNumber());
		  pstmt.setInt(9, address.getRoadNumber());
	
		  pstmt.setString(10, address.getZipcode());
		  pstmt.setString(11, address.getZipcode());
			
		  if (i==0) {
			  System.out.println(pstmt.toString());
			  logger.warn(pstmt.toString());
		  }
	      // Do the query.
	      pstmt.executeQuery();
	  }
	  logger.warn(addresses.length + " addresses resolved");
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