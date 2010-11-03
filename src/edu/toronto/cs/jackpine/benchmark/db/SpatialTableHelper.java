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

package  edu.toronto.cs.jackpine.benchmark.db;

import org.apache.log4j.Logger;

import com.continuent.bristlecone.benchmark.db.SqlDialectFactory;
import com.continuent.bristlecone.benchmark.db.TableHelper;

/**
 * Implements methods to create, drop, and populate individual tables. 
 * 
 */
public class SpatialTableHelper extends TableHelper
{
  private static Logger logger = Logger.getLogger(SpatialTableHelper.class);

  protected final SpatialSqlDialect spatialSqlDialect;
  
  /** 
   * Creates a new instance. 
   * 
   * @param url JDBC URL of database where tables live 
   * @param login
   * @param password
   * @throws BenchmarkException If JDBC driver cannot be loaded or we can't 
   *         find the SqlDialect. 
   */
  public SpatialTableHelper(String url, String login, String password)
  {
    super(url,login,password);
    this.spatialSqlDialect = SpatialSqlDialectFactory.getInstance().getDialect(url);
  }
  
  
  
  /** 
   * Returns the SQLDialect used by this helper. 
   */
  public SpatialSqlDialect getSpatialSqlDialect()
  {
    return spatialSqlDialect;
  }
}