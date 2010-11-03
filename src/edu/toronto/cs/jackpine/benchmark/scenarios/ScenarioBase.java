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
 * Initial developer(s): Robert Hodges and Ralph Hannus.
 * Contributor(s): 
 */

package edu.toronto.cs.jackpine.benchmark.scenarios;

import java.sql.Connection;
import java.sql.Types;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.continuent.bristlecone.benchmark.Scenario;
import com.continuent.bristlecone.benchmark.db.Column;
import com.continuent.bristlecone.benchmark.db.TableSet;
import com.continuent.bristlecone.benchmark.db.TableSetHelper;

/**
 * Base class for scenarios that share a common set of properties and tables. 
 * 
 * @author rhodges, sray
 */
public abstract class ScenarioBase implements Scenario
{
  private static final Logger logger = Logger.getLogger(ScenarioBase.class);

  // Scenario properties. 
  /** Url of the database on which we are running the test. */
  protected String url;
  
  /** Database user name. */
  protected String user;
  
  /** Database password (leaving it null equates to empty password). */
  protected String password = "";
  
  /** Number of tables over which benchmark operations are to be distributed. */
  protected int    tables = 1;
  
  /** Number of data rows to create in each benchmark table. */
  protected int    datarows = 1;
  
  /** Datatype of the payload column in the benchmark table. */
  protected String datatype = "varchar";
  
  /** Column width of the payload column, e.g., 10 for varchar equates to varchar(10). */
  protected int   datawidth = 10;
  
  /** Analyze command that should be run after initializing data and before starting test. */
  protected String analyzeCmd = null;
  
  /** If true reuse existing data tables. */
  protected boolean reusedata = false;
  
  // Implementation data for scenario
  protected TableSet tableSet;
  protected TableSetHelper helper;
  protected Connection conn = null;

  // Setters for properties. 
  public void setDatarows(int datarows)
  {
    this.datarows = datarows;
  }

  public void setDatatype(String datatype)
  {
    this.datatype = datatype;
  }

  public void setDatawidth(int datawidth)
  {
    this.datawidth = datawidth;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  public void setUrl(String url)
  {
    this.url = url;
  }

  public void setUser(String user)
  {
    this.user = user;
  }
  
  public void setTables(int tables)
  {
    this.tables = tables;
  }
  
  public void setAnalyzeCmd(String analyzeCmd)
  {
    this.analyzeCmd = analyzeCmd;
  }
  
  public void setReusedata(boolean reusedata)
  {
    this.reusedata = reusedata;
  }

  /**
   * Perform basic initialization. 
   */
  public void initialize(Properties properties) throws Exception
  {
    Column[] columns = new Column[] {
        new Column("mykey", Types.INTEGER, -1, -1, true, true),
        new Column("mydata", Types.INTEGER),
        new Column("mypayload", Types.VARCHAR, (int) datawidth)
      };
    tableSet = new TableSet("benchmark_scenario_", tables, 
        datarows, columns);
    helper = new TableSetHelper(url, user, password); 
    conn = helper.getConnection();
  }
  
  /** Configure test tables. */
  public void globalPrepare() throws Exception
  {
    // Create and populate tables. 
    if (reusedata)
    {
      logger.info("Reusing existing test tables...");
    }
    else
    {
      logger.info("Creating and populating test tables...");
      //sray: comment the 2 following lines while running any spatial scenarios
      helper.createAll(tableSet);
      helper.populateAll(tableSet);
    }
    
    // Run analyze command if supplied. 
    if (analyzeCmd != null)
    {
      logger.info("Running analyze command: " + analyzeCmd);
      helper.execute(analyzeCmd); 
    }
  }

  /** Create a prepared statement array. */
  public abstract void prepare() throws Exception;

  /** Execute an interation. */
  public abstract void iterate(long iterationCount) throws Exception;

  /** Clean up resources used by scenario. */
  public abstract void cleanup() throws Exception;

  /** Global cleanup does nothing. */
  public void globalCleanup()
  {
  }
}