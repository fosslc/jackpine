/**
 * Jackpine Spatial Database Benchmark 
 *  Copyright (C) 2010 University of Toronto
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
import java.sql.Types;
import java.util.Properties;
import java.util.Enumeration;

import org.apache.log4j.Logger;

import com.continuent.bristlecone.benchmark.Scenario;
import com.continuent.bristlecone.benchmark.db.Column;
import com.continuent.bristlecone.benchmark.db.SpatialTableSet;
import com.continuent.bristlecone.benchmark.db.TableSetHelper;

import edu.toronto.cs.jackpine.benchmark.db.SpatialTableHelper;

/**
 * @author sray
 * TODO: further refactor
 */
public abstract class SpatialScenarioBase implements Scenario
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
  //protected int    datarows = 1;
  
  /** Datatype of the payload column in the benchmark table. */
  protected String datatype = "varchar";
  
  /** Column width of the payload column, e.g., 10 for varchar equates to varchar(10). */
  protected int   datawidth = 10;
  
  /** Analyze command that should be run after initializing data and before starting test. */
  protected String analyzeCmd = null;
  
  /** If true reuse existing data tables. */
  protected boolean reusedata = false;
  
  // Implementation data for scenario
  protected SpatialTableSet tableSet;
  protected SpatialTableHelper helper;
  protected Connection conn = null;

  // Setters for properties. 
  public void setDatarows(int datarows)
  {
    //this.datarows = datarows;
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

  //enum SPATIAL_TABLES {arealm, areawater, edges, pointlm, tl_2008_48_place}
  enum SPATIAL_TABLES {arealm, areawater, edges, pointlm}
  
  /**
   * Perform basic initialization. 
   */
  public void initialize(Properties properties) throws Exception
  {
	tableSet = new SpatialTableSet(SPATIAL_TABLES.values().length);  
	// String name, int type, int length, int precision,boolean isPrimaryKey, boolean isAutoIncrement
      
    Column[] arealmColumns = new Column[] {
        new Column("OGR_FID", Types.INTEGER, -1, -1, true, true),
        new Column("SHAPE", Types.STRUCT), //geometry
        new Column("statefp", Types.VARCHAR, 3),
        new Column("countyfp", Types.VARCHAR, 3),
        new Column("ansicode", Types.VARCHAR, 8),
        new Column("areaid", Types.VARCHAR, 22),
        new Column("fullname", Types.VARCHAR, 100),
        new Column("mtfcc", Types.VARCHAR, 5),
      };
    
    tableSet.addTable(SPATIAL_TABLES.arealm.toString(), SPATIAL_TABLES.arealm.ordinal(), arealmColumns); //TODO: set name based on SQL dialect
    
    
    Column[] areawaterColumns = new Column[] {
            new Column("OGR_FID", Types.INTEGER, -1, -1, true, true),
            new Column("SHAPE", Types.STRUCT), //geometry
            new Column("statefp", Types.VARCHAR, 3),
            new Column("countyfp", Types.VARCHAR, 3),
            new Column("ansicode", Types.VARCHAR, 8),
            new Column("hydroid", Types.VARCHAR, 22),
            new Column("fullname", Types.VARCHAR, 100),
            new Column("mtfcc", Types.VARCHAR, 5),
          };
        
        tableSet.addTable(SPATIAL_TABLES.areawater.toString(), SPATIAL_TABLES.areawater.ordinal(), areawaterColumns);
        
        
     Column[] edgesColumns = new Column[] {
           new Column("OGR_FID", Types.INTEGER, -1, -1, true, true),
           new Column("SHAPE", Types.STRUCT), //geometry
           new Column("statefp", Types.VARCHAR, 3),
                new Column("countyfp", Types.VARCHAR, 3),
                new Column("tlid", Types.DECIMAL, 10),
                new Column("tfidl", Types.DECIMAL, 10),
                new Column("tfidr", Types.DECIMAL, 10),
                new Column("mtfcc", Types.VARCHAR, 5),
                new Column("fullname", Types.VARCHAR, 100),
                new Column("smid", Types.VARCHAR, 22),
                new Column("lfromadd", Types.VARCHAR, 12),
                new Column("ltoadd", Types.VARCHAR, 12),
                new Column("rfromadd", Types.VARCHAR, 12),
                new Column("rtoadd", Types.VARCHAR, 12),
                new Column("zipl", Types.VARCHAR, 5),
                new Column("zipr", Types.VARCHAR, 5),
                new Column("featcat", Types.VARCHAR, 1),
                new Column("hydroflg", Types.VARCHAR, 1),
                new Column("railflg", Types.VARCHAR, 1),
                new Column("roadflg", Types.VARCHAR, 1),
                new Column("olfflg", Types.VARCHAR, 1),
                new Column("passflg", Types.VARCHAR, 1),
                new Column("divroad", Types.VARCHAR, 1),
                new Column("exttyp", Types.VARCHAR, 1),
                new Column("ttyp", Types.VARCHAR, 1),
                new Column("deckedroad", Types.VARCHAR, 1),
                new Column("artpath", Types.VARCHAR, 1),
                new Column("persist", Types.VARCHAR, 1),
                new Column("gcseflg", Types.VARCHAR, 1),
                new Column("offsetl", Types.VARCHAR, 1),
                new Column("offsetr", Types.VARCHAR, 1),
                new Column("tnidf", Types.DECIMAL, 10),
                new Column("tnidt", Types.DECIMAL, 10),
                  
              };
            
       tableSet.addTable(SPATIAL_TABLES.edges.toString(), SPATIAL_TABLES.edges.ordinal(), edgesColumns);
    
            
       Column[] pointlmColumns = new Column[] {
           new Column("OGR_FID", Types.INTEGER, -1, -1, true, true),
           new Column("SHAPE", Types.STRUCT), //geometry
           new Column("statefp", Types.VARCHAR, 3),
           new Column("countyfp", Types.VARCHAR, 3),
           new Column("ansicode", Types.VARCHAR, 8),
           new Column("areaid", Types.VARCHAR, 22),
           new Column("fullname", Types.VARCHAR, 100),
           new Column("mtfcc", Types.VARCHAR, 5),
        };
                
        tableSet.addTable(SPATIAL_TABLES.pointlm.toString(),  SPATIAL_TABLES.pointlm.ordinal(), pointlmColumns); //TODO: set name based on SQL dialect
                
    
    helper = new SpatialTableHelper(url, user, password); 
    conn = helper.getConnection();
  }
  
  /** Configure test tables. */
  public void globalPrepare() throws Exception
  {
	/*
    // Create and populate tables. 
    if (reusedata)
    {
      logger.info("Reusing existing test tables...");
    }
    else
    {
      logger.info("Creating and populating test tables...");
      helper.createAll(tableSet);
      helper.populateAll(tableSet);
    }
    */
    
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
  
  /** Execute an interation. */
  public  void iterate(long iterationCount, int statementIndex) {} //sray
}