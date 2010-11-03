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
import java.sql.Types;
import java.util.Properties;

import org.apache.log4j.Logger;

import edu.toronto.cs.jackpine.benchmark.db.SpatialSqlDialect;

/** 
 * 
 * @author sray
 */
public class WriteSpatialPolygonScenario extends SpatialScenarioBase
{
	  private static final Logger logger = Logger.getLogger(WriteSpatialPolygonScenario.class);

	  protected PreparedStatement[] pstmtArray;
	  protected PreparedStatement cleanUpPstmt;
	  
	  static final int MAXNUM=1000;
	  SpatialSqlDialect dialect = null;
	  static int maxRowId=-1;
	  
	  
	  /** Create a prepared statement array. */
	  public void prepare() throws Exception
	  {
		  dialect = helper.getSpatialSqlDialect(); 
		    
		  if (dialect.getSqlDialectType() == SpatialSqlDialect.SupportedSqlDialect.Informix) {
			  String mriSql= dialect.getMaxRowidFromSpatialTableArealmMerge();
			  PreparedStatement maxRowIdPstmt = conn.prepareStatement(mriSql);
			  logger.warn(maxRowIdPstmt.toString());

			  try {  
				   logger.warn(maxRowIdPstmt.toString());
				   ResultSet rs= maxRowIdPstmt.executeQuery();
				   if (rs.next()) {
					   maxRowId = rs.getInt(1);
					   logger.warn("Max row count :" + maxRowId);
				   }
				   rs.close();
				   maxRowIdPstmt.close();
				}
				catch (Exception e) {
				   	e.printStackTrace();
				    logger.error(e.toString());
				   	throw new Exception("WriteSpatialPolygonScenario");
				}  
			  
		  }

		  pstmtArray = new PreparedStatement[MAXNUM];
		  for (int i=0;i<MAXNUM;i++) {
			  //System.out.print(i+" \t");
			  String sql = dialect.getInsertIntoArealmMerge();
			  pstmtArray[i] = conn.prepareStatement(sql);
		  }
			  

		  String sql = dialect.getSpatialWriteCleanupArealmMerge();
		  cleanUpPstmt = conn.prepareStatement(sql);
		  
		  try {  
			  logger.warn(cleanUpPstmt.toString());	
			  cleanUpPstmt.executeUpdate();
			}
		    catch (Exception e) {
		    	e.printStackTrace();
		    	 logger.debug(e.toString());
		    	throw new Exception("Cleanup WriteSpatialPolygonScenario");
		    }  
		    
	  }

	  /** Execute an interation. */
	  public void iterate(long iterationCount) throws Exception
	  {
	    try {
		int index = 0;
		for (int i=0;i<MAXNUM;i++) {
			PreparedStatement pstmt = pstmtArray[index];
			if (dialect.getSqlDialectType() == SpatialSqlDialect.SupportedSqlDialect.Informix) {
				pstmt.setInt(1, ++maxRowId);
			}
			if (i==0) 
				logger.warn(pstmt.toString());
			
			try {
				pstmt.executeUpdate();
			}
			catch (Exception e) {
				logger.error("count: " + i + " stmt:" + pstmt.toString());
				logger.error(e.toString());
				e.printStackTrace();
				
			}
			
		}
	    // Do the query.
	    
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	 logger.debug(e.toString());
	    	throw new Exception("WriteSpatialPolygonScenario");
	    }
	  }

	  /** Clean up resources used by scenario. */
	  public void cleanup() throws Exception
	  {
		// clean up the inserted records
		try {  
		  logger.warn(cleanUpPstmt.toString());	
		  cleanUpPstmt.executeUpdate();
		  cleanUpPstmt.close();
		}
	    catch (Exception e) {
	    	e.printStackTrace();
	    	 logger.error(e.toString());
	    	throw new Exception("Cleanup WriteSpatialPolygonScenario");
	    }  
	    
	    // Clean up connections. 
	    for (int i = 0; i < pstmtArray.length; i++)
	      pstmtArray[i].close();
	    if (conn != null)
	      conn.close();
	  }
	 
}