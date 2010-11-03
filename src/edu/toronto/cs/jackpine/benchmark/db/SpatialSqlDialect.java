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

package  edu.toronto.cs.jackpine.benchmark.db;

import com.continuent.bristlecone.benchmark.db.SqlDialect;
import com.continuent.bristlecone.benchmark.db.Table;

import edu.toronto.cs.jackpine.benchmark.scenarios.macroscenario.VisitScenario;

/**
 * 
 * @author sray
 */
public interface SpatialSqlDialect extends SqlDialect
{
  public static  enum SupportedSqlDialect {Informix, Ingres, Mysql, PostgreSQL };
	
  public SupportedSqlDialect getSqlDialectType();
  /**
   * Returns the name of the JDBC driver class.
   */
    
 
  public abstract String getSelectAllFeaturesWithinADistanceFromPoint();
  //public abstract String getSelectAllFeaturesWithinADistanceFromPoint(Table t);
  public abstract String getSelectTotalLength();
  public abstract String getSelectTotalArea();
  public abstract String getSelectLongestLine();
  public abstract String getSelectLargestArea();
  public abstract String getSelectDimensionPolygon();
  public abstract String getSelectBufferPolygon(); 
  public abstract String getSelectConvexHullPolygon();
  public abstract String getSelectEnvelopeLine();
  public abstract String getSelectBoundingBoxSearch();
  
  public abstract String[] getSelectLongestLineIntersectsArea();
  public abstract String[] getSelectLineIntersectsLargestArea();
  public abstract String[] getSelectAreaOverlapsLargestArea();
  public abstract String[] getSelectLargestAreaContainsPoint();
  
  public abstract String getSelectAreaOverlapsArea();
  public abstract String getSelectAreaContainsArea();
  public abstract String getSelectAreaWithinArea();
  public abstract String getSelectAreaTouchesArea();
  public abstract String getSelectAreaEqualsArea();
  public abstract String getSelectAreaDisjointArea();

  public abstract String getSelectLineIntersectsArea();
  public abstract String getSelectLineCrossesArea();
  public abstract String getSelectLineWithinArea();
  public abstract String getSelectLineTouchesArea();
  public abstract String getSelectLineOverlapsArea();
  
  public abstract String getSelectLineOverlapsLine();
  public abstract String getSelectLineCrossesLine();
  
  public abstract String getSelectPointEqualsPoint();
  public abstract String getSelectPointWithinArea();
  public abstract String getSelectPointIntersectsArea();
  public abstract String getSelectPointIntersectsLine();
  
  public abstract String getMaxRowidFromSpatialTableEdgesMerge();
  public abstract String getMaxRowidFromSpatialTableArealmMerge();
  public abstract String getInsertIntoEdgesMerge();
  public abstract String getInsertIntoArealmMerge();
  public abstract String getSpatialWriteCleanupEdgesMerge();
  public abstract String getSpatialWriteCleanupArealmMerge();
  
  public abstract String getCityStateForReverseGeocoding();
  public abstract String getStreetAddressForReverseGeocoding();
  
  public abstract String getGeocodingQuery();
  
  public abstract String getMapSearchSiteSearchQuery(VisitScenario visitScenario);
  public abstract String[] getMapSearchScenarioQueries(VisitScenario visitScenario);
  public abstract String[] getMapBrowseBoundingBoxQueries();
  
  public abstract String[] getLandUseQueries();
  public abstract String[] getEnvHazardQueries();
}