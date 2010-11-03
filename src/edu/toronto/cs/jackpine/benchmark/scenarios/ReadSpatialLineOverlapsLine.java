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

/**
 * 
 * @author sray
 */
public class ReadSpatialLineOverlapsLine extends SpatialScenarioBase
{
  private static final Logger logger = Logger.getLogger(ReadSpatialLineOverlapsLine.class);

  protected PreparedStatement[] pstmtArray;
  
  
  
  /** Create a prepared statement array. */
  public void prepare() throws Exception
  {
	  SpatialSqlDialect dialect = helper.getSpatialSqlDialect(); 
	    
	    pstmtArray = new PreparedStatement[1];
	    String sql = dialect.getSelectLineOverlapsLine();
	    pstmtArray[0] = conn.prepareStatement(sql);
  }

  /** Execute an interation. */
  public void iterate(long iterationCount) throws Exception
  {
    // Pick a table at random on which to operate.
    int index = (int) (Math.random() * pstmtArray.length);
    PreparedStatement pstmt = pstmtArray[index];
    
    // Do the query.
    logger.warn(pstmt.toString());
    pstmt.executeQuery();
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
