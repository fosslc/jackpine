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
 * Initial developer(s): Robert Hodges and Ralph Hannus.
 * Contributor(s): 
 */

package  edu.toronto.cs.jackpine.benchmark.db;

import com.continuent.bristlecone.benchmark.db.SqlDialectForIngres;

import edu.toronto.cs.jackpine.benchmark.db.SpatialSqlDialect.SupportedSqlDialect;
import edu.toronto.cs.jackpine.benchmark.scenarios.macroscenario.VisitScenario;

/**
 * Ingres spatial DBMS dialect information.
 * 
 * @author sray
 */
public class SpatialSqlDialectForIngres extends SqlDialectForIngres implements SpatialSqlDialect
{
	
  
  /**
   * 
   * @return
   */
  public SupportedSqlDialect getSqlDialectType() {
	  return SupportedSqlDialect.Ingres;
  }
  
	
  public String getMaxRowidFromSpatialTableEdgesMerge(){return "";}
  public String getMaxRowidFromSpatialTableArealmMerge(){return "";}
  
  
  public String getSelectAllFeaturesWithinADistanceFromPoint()
  {
	  StringBuffer sb = new StringBuffer();

	    // Generate the join SQL statement.
	    sb.append("select count(*) from arealm_merge ");	    
	    sb.append(" where Distance (shape, PointFromText('POINT(-97.7 30.30)')) < 1000");
	    String sql = sb.toString();
	   // System.out.println("The query "+ sql);
	    return sql;
  }
  
 public String getSelectBoundingBoxSearch(){ 
	  
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement.
      sb.append("select count(*) from edges_merge");
      sb.append("  where Within( shape, PolyFromText('POLYGON((-97.7 30.30, -92.7 30.30, -92.7 27.30, -97.7 27.30, -97.7 30.30))')) = 1");
      ////sb.append("  where Within( shape, PolyFromText('POLYGON((-97.7 30.30, -92.7 30.30, -92.7 27.30, -97.7 27.30, -97.7 30.30))'))");
      String sql = sb.toString();
      // System.out.println("The query "+ sql);
      return sql;

  }
  
 public String getSelectDimensionPolygon(){
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement.
	  sb.append("select Dimension(a.shape) from  arealm_merge a");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
 }
 
 public String getSelectBufferPolygon(){
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement.
	  sb.append("select Buffer(a.shape,5280) from  arealm_merge a");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
 }
 
 public String getSelectConvexHullPolygon(){
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement.
	  sb.append("select ConvexHull(a.shape) from  arealm_merge a");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
 }
 
 public String getSelectEnvelopeLine(){
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement.
	  sb.append("select first 1000 AsText(Envelope(e.shape)) from  edges_merge e");
	  String sql = sb.toString();
	  //// System.out.println("The query "+ sql);
	  return sql;
 }
 
 //didn't work, now works
 public String getSelectLongestLine(){
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement.
	  //No GLength. Can't get length of line. 
	  // didn't work, now works
	  sb.append("SELECT first 1 fullname, St_Length(shape) AS line FROM edges_merge order BY line DESC");
//	  sb.append("SELECT  fullname, GLength(shape) AS line FROM edges_merge order BY line DESC limit 1");
	  String sql = sb.toString();
	  //// System.out.println("The query "+ sql);
	  return sql;
	  
 }

 public  String getSelectLargestArea(){ 
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement.
	  sb.append("SELECT first 1 fullname, Area(shape) AS area FROM areawater_merge order BY area DESC");
//	  sb.append("SELECT  fullname, Area(shape) AS area FROM areawater_merge order BY area DESC limit 1");
	  String sql = sb.toString();
	  //// System.out.println("The query "+ sql);
	  return sql;
 }
 
 // didn't work, now works
 public  String getSelectTotalLength(){ 
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement.
      //Can't get length from a line.
	  sb.append("SELECT sum(St_Length(shape))/1000 AS km_roads FROM edges_merge");
	  String sql = sb.toString();
	  //// System.out.println("The query "+ sql);
	  return sql;
 }

 public String getSelectTotalArea(){
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement.
	  sb.append("SELECT sum(Area(shape)) AS area FROM areawater_merge");
	  String sql = sb.toString();

	  return sql;
	  
 }
 
 ////////////////////////////////////////SPATIAL JOIN//////////////////////////////////////////////////
 // all the polygons that are intersected by the longest line: 1
 public String[] getSelectLongestLineIntersectsArea(){ 
	 String[] queries =  new String[2]; 
	 queries[0] = "select first 1 ogr_fid from edges_merge order by ST_Length(shape) desc";
	 queries[1] = "select count(*) from areawater_merge a, edges_merge e where  Intersects(e.shape,a.shape)=0 and e.ogr_fid= ?";
			  
	  return queries;
 }

 
 //all the lines that intersect the largest area: 371
 public String[] getSelectLineIntersectsLargestArea(){ 
	 String[] queries =  new String[2]; 
	 queries[0] = "select first 1 ogr_fid from arealm_merge order by Area(shape) desc";
	 queries[1] = "select count(*) from  arealm_merge a, edges_merge e where Intersects(e.shape, a.shape)=0 and a.ogr_fid= ?";
			  
	  return queries;
 }
 


 //all the area overlaps the largest area:
 public String[] getSelectAreaOverlapsLargestArea(){ 
	 String[] queries =  new String[2]; 
	 queries[0] = "select first 1 ogr_fid from arealm_merge order by Area(shape) desc";
	 queries[1] = "select count(*) from  arealm_merge al, areawater_merge aw where Overlaps(al.shape, aw.shape)=0 and al.ogr_fid= ?";
			  
	  return queries;
 }
 
 
 
 //all the point within the largest area:
 public String[] getSelectLargestAreaContainsPoint(){ 

	 String[] queries =  new String[2]; 
	 queries[0] = " select first 1 ogr_fid from areawater_merge order by Area(shape) desc ";
	 queries[1] = "select count(*) from  pointlm_merge pl, areawater_merge aw where Contains(aw.shape, pl.shape)=1 and aw.ogr_fid= ?";
			  
	  return queries;
 
 }
 

 
 //////////////////////////////////////////ALLPAIR SPATIAL JOIN//////////////////////////////////////////////////
 
 /////////////////////////////////////////POLYGON AND POLYGON //////////////////////////////////////////////
 
 public String getSelectAreaOverlapsArea(){ 
	  StringBuffer sb = new StringBuffer();
	  sb.append("select a1.fullname from  arealm_merge a1 , arealm_merge a2 where overlaps(a1.shape, a2.shape) = 1");
//	  sb.append("select a1.fullname from  arealm_merge a1 , arealm_merge a2 where overlaps(a1.shape, a2.shape)");
	  String sql = sb.toString();
	  //// System.out.println("The query "+ sql);
	  return sql;
 }
 
 public String getSelectAreaContainsArea(){  
	  StringBuffer sb = new StringBuffer();
 	  sb.append("select a1.fullname from  arealm_merge a1 , arealm_merge a2 where contains(a1.shape, a2.shape) = 1");
// 	  sb.append("select a1.fullname from  arealm_merge a1 , arealm_merge a2 where contains(a1.shape, a2.shape)");
 	  String sql = sb.toString();
 	  //// System.out.println("The query "+ sql);
 	  return sql;
 }
 
 public String getSelectAreaWithinArea(){ 
	  StringBuffer sb = new StringBuffer();
 	  sb.append("select a1.fullname from  arealm_merge a1 , arealm_merge a2 where within(a1.shape, a2.shape) = 1");
// 	  sb.append("select a1.fullname from  arealm_merge a1 , arealm_merge a2 where within(a1.shape, a2.shape)");
 	  String sql = sb.toString();
 	  //// System.out.println("The query "+ sql);
 	  return sql;
 }
 
 public String getSelectAreaTouchesArea(){ 
	  StringBuffer sb = new StringBuffer();
 	  sb.append("select a1.fullname from  arealm_merge a1 , arealm_merge a2 where touches(a1.shape, a2.shape) = 1");
// 	  sb.append("select a1.fullname from  arealm_merge a1 , arealm_merge a2 where touches(a1.shape, a2.shape)");
 	  String sql = sb.toString();
 	  //// System.out.println("The query "+ sql);
 	  return sql;
 }
 
 public String getSelectAreaEqualsArea(){ 
	  StringBuffer sb = new StringBuffer();
 	  sb.append("select a1.fullname from  arealm_merge a1 , arealm_merge a2 where equals(a1.shape, a2.shape) = 1");
// 	  sb.append("select a1.fullname from  arealm_merge a1 , arealm_merge a2 where equals(a1.shape, a2.shape)");
 	  String sql = sb.toString();
 	  //// System.out.println("The query "+ sql);
 	  return sql;
 }
 
 public String getSelectAreaDisjointArea(){ 
	  StringBuffer sb = new StringBuffer();
	  sb.append("select count(*) from  arealm_merge a1, arealm_merge a2 where disjoint(a1.shape, a2.shape) = 1");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
 }
 
 /////////////////////////////////////////LINE AND POLYGON //////////////////////////////////////////////

 public  String getSelectLineIntersectsArea(){
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement. 
	  //sb.append("select a.fullname, e.fullname from  arealm_merge a, edges_merge e where Intersects(e.shape, a.shape)");
	  sb.append("select e.fullname from  edges_merge e, arealm_merge a where Intersects(e.shape, a.shape) = 1");
//	  sb.append("select e.fullname from  edges_merge e, arealm_merge a where Intersects(e.shape, a.shape)");
	  String sql = sb.toString();
	  //// System.out.println("The query "+ sql);
	  return sql;
 }
 
 public  String getSelectLineOverlapsArea(){ 
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement. 
	  sb.append("select a.fullname, e.fullname from  arealm_merge a, edges_merge e where Overlaps(e.shape, a.shape) = 1");
//	  sb.append("select a.fullname, e.fullname from  arealm_merge a, edges_merge e where Overlaps(e.shape, a.shape)");
	  String sql = sb.toString();
	  //// System.out.println("The query "+ sql);
	  return sql;
}
 
 public String getSelectLineCrossesArea(){
	  StringBuffer sb = new StringBuffer();
	  sb.append("select e.fullname from  edges_merge e , arealm_merge a where crosses(e.shape, a.shape) = 1");
//	  sb.append("select e.fullname from  edges_merge e , arealm_merge a where crosses(e.shape, a.shape)");
	  String sql = sb.toString();
	  //// System.out.println("The query "+ sql);
	  return sql;
 }
 
 public String getSelectLineWithinArea(){
	  StringBuffer sb = new StringBuffer();
	  sb.append("select e.fullname from  edges_merge e , arealm_merge a where within(e.shape, a.shape) = 1");
//	  sb.append("select e.fullname from  edges_merge e , arealm_merge a where within(e.shape, a.shape)");
	  String sql = sb.toString();
	  //// System.out.println("The query "+ sql);
	  return sql;
 }
 
 public String getSelectLineTouchesArea(){
	  StringBuffer sb = new StringBuffer();
	  sb.append("select e.fullname from  edges_merge e , arealm_merge a where Touches(e.shape, a.shape) = 1");
//	  sb.append("select e.fullname from  edges_merge e , arealm_merge a where Touches(e.shape, a.shape)");
	  String sql = sb.toString();
	  //// System.out.println("The query "+ sql);
	  return sql; 
 }
 
 
 
 ///////////////////////////////////////LINE AND LINE//////////////////////////////////////////////
 public String getSelectLineOverlapsLine(){ 
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement. 
	  sb.append("select first 1  e1.fullname from  edges_merge e1 , edges_merge e2 where overlaps(e1.shape, e2.shape) = 1");
//	  sb.append("select e1.fullname from  edges_merge e1 , edges_merge e2 where overlaps(e1.shape, e2.shape)");
	  String sql = sb.toString();
	  //// System.out.println("The query "+ sql);
	  return sql;
 }
 
 public String getSelectLineCrossesLine(){ 
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement. 
	  sb.append("select first 1  e1.fullname from  edges_merge e1 , edges_merge e2 where crosses(e1.shape, e2.shape) = 1");
//	  sb.append("select e1.fullname from  edges_merge e1 , edges_merge e2 where crosses(e1.shape, e2.shape)");
	  String sql = sb.toString();
	  //// System.out.println("The query "+ sql);
	  return sql;
 }
 
 
 ///////////////////////////////////////POINT AND others//////////////////////////////////////////////
 
 public  String getSelectAreaContainsPoint(){ 
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement.
	  sb.append("select a.fullname, p.fullname from  arealm_merge a, pointlm_merge p where Within(p.shape, a.shape) = 1");
//	  sb.append("select a.fullname, p.fullname from  arealm_merge a, pointlm_merge p where Within(p.shape, a.shape)");
	  String sql = sb.toString();
	  //// System.out.println("The query "+ sql);
	  return sql;
 }
 
 public  String getSelectPointTouchesLine(){ 
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement.
	  sb.append("select a.fullname, e.fullname from  arealm_merge a, edges_merge e where Touches(e.shape, a.shape) = 1");
//	  sb.append("select a.fullname, e.fullname from  arealm_merge a, edges_merge e where Touches(e.shape, a.shape)");
	  String sql = sb.toString();
	  //// System.out.println("The query "+ sql);
	  return sql;
 }
 
 public  String getPointIntersectsArea(){
	  StringBuffer sb = new StringBuffer();
	  sb.append("select a.fullname, p.fullname from  arealm_merge a, pointlm_merge p where Intersects(p.shape, a.shape) = 1");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
 }
 
 public  String getPointIntersectsLine(){
	  StringBuffer sb = new StringBuffer();
	  sb.append("select e.fullname, p.fullname from  edges_merge e, pointlm_merge p where Intersects(p.shape, e.shape) = 1");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
 }
 
///////////////////////////////////////POINT AND //////////////////////////////////////////////
 
 
 public  String getSelectPointWithinArea(){ 
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement.
	  sb.append("select count(*) from  arealm_merge a, pointlm_merge p where Within(p.shape, a.shape) =1");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
 }   
 
 public  String getSelectPointIntersectsArea(){
	  StringBuffer sb = new StringBuffer();
	  sb.append("select count(*) from  arealm_merge a, pointlm_merge p where Intersects(p.shape, a.shape) =1");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
 }
 
 public  String getSelectPointIntersectsLine(){
	  StringBuffer sb = new StringBuffer();
	  sb.append("select count(*) from  edges_merge e, pointlm_merge p where Intersects(p.shape, e.shape) =1");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
 }
 
 public  String getSelectPointEqualsPoint(){
	  StringBuffer sb = new StringBuffer();
	  sb.append("select count(*) from  pointlm_merge p1, pointlm_merge p2 where Equals(p1.shape, p2.shape) =1");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
 }
 
 //////////////////////////////////////////INSERT RECORDS/////////////////////////////////
 java.util.Random rnd = new java.util.Random(System.currentTimeMillis());
 
 public String getInsertIntoEdgesMerge(){
	  StringBuffer sb = new StringBuffer();
	  
	  int nxt1 = rnd.nextInt(180);
	  if (nxt1==0) nxt1=1;
	  nxt1 =  nxt1 * (-1);
	  
	  int nxt2 = rnd.nextInt(180);
	  if (nxt2==0) nxt2=1;
	  nxt2 =  nxt2 * (-1);
	  
	  int nxt3 = rnd.nextInt(180);
	  if (nxt3==0) nxt3=1;
	  nxt3 =  nxt3 * (-1);

	  //sb.append("insert into edges_merge values (0, GeomFromText('LINESTRING(-95.43987 31.581214,-95.439728 31.581286,-95.439587 31.581385,-95.439555 31.581396,-95.439535 31.58139,-95.439452 31.581401,-95.439433 31.581412,-95.439433 31.581445,-95.439426 31.581463,-95.439407 31.581473,-95.439386 31.581471,-95.439356 31.581462,-95.439337 31.581445,-95.439285 31.581434,-95.439273 31.581418,-95.439274 31.5814,-95.439292 31.581385,-95.43933 31.581374,-95.439337 31.581346,-95.439329 31.581329,-95.439285 31.581269,-95.439215 31.581214,-95.439164 31.581187,-95.439144 31.581165,-95.439086 31.581121,-95.438952 31.581077,-95.438759 31.581028,-95.438714 31.581011,-95.438676 31.580989,-95.438643 31.580962,-95.438611 31.580912,-95.438618 31.580863,-95.438586 31.580813,-95.438566 31.580797,-95.438277 31.580709,-95.438263 31.580695,-95.438252 31.580676,-95.438245 31.580643,-95.438207 31.580593,-95.438181 31.580577,-95.438149 31.580566,-95.438104 31.58056,-95.438078 31.580533,-95.438073 31.580515,-95.43813 31.580478,-95.438277 31.580434,-95.438432  31.580401)'),'48','001',74690317,205838157,205838661,'P0002','TEMP','447','','','','','','','','N','N','N','N','','','N','','','','','N','N','N','14062338','14062338')");
	  sb.append("insert into edges_merge values (0, GeometryFromText('LINESTRING(").append(nxt1)
	  .append(" 31.581214,-95.439728 31.581286,-95.439587 31.581385,-95.439555 31.581396,-95.439535 31.58139,-95.439452 31.581401,-95.439433 31.581412,-95.439433 31.581445,-95.439426 31.581463,").append(nxt2)
	  .append(" 31.581473,-95.439386 31.581471,-95.439356 31.581462,").append(nxt3)
	  .append(" 31.581445,-95.439285 31.581434,-95.439273 31.581418,-95.439274 31.5814,-95.439292 31.581385,-95.43933 31.581374,-95.439337 31.581346,-95.439329 31.581329,-95.439285 31.581269,-95.439215 31.581214,-95.439164 31.581187,-95.439144 31.581165,-95.439086 31.581121,-95.438952 31.581077,-95.438759 31.581028,-95.438714 31.581011,-95.438676 31.580989,-95.438643 31.580962,-95.438611 31.580912,-95.438618 31.580863,-95.438586 31.580813,-95.438566 31.580797,-95.438277 31.580709,-95.438263 31.580695,-95.438252 31.580676,-95.438245 31.580643,-95.438207 31.580593,-95.438181 31.580577,-95.438149 31.580566,-95.438104 31.58056,-95.438078 31.580533,-95.438073 31.580515,-95.43813 31.580478,-95.438277 31.580434,-95.438432  31.580401)'),'48','001',74690317,205838157,205838661,'P0002','TEMP','447','','','','','','','','N','N','N','N','','','N','','','','','N','N','N','14062338','14062338')");	 
	 
	  String sql = sb.toString();
	// //// System.out.println(sql);  
	  return sql;
 }
 
 public String getSpatialWriteCleanupEdgesMerge() {
	  StringBuffer sb = new StringBuffer();
	  sb.append("delete from edges_merge where fullname='TEMP'");
	  String sql = sb.toString();
	  // //// System.out.println(sql);  
	  return sql;
 }
 
 public String getInsertIntoArealmMerge(){
	  StringBuffer sb = new StringBuffer();

	  int nxt1 = rnd.nextInt(180);
	  if (nxt1==0) nxt1=1;
	  nxt1 =  nxt1 * (-1);
	  
	  int nxt2 = rnd.nextInt(180);
	  if (nxt2==0) nxt2=1;
	  nxt2 =  nxt2 * (-1);
	  
	  int nxt3 = rnd.nextInt(180);
	  if (nxt3==0) nxt3=1;
	  nxt3 =  nxt3 * (-1);
	  
	  sb.append("insert into arealm_merge values (0,PolyFromText('POLYGON((-95.835919 31.758732,").append(nxt1)
	  .append(" 31.758732,-95.83358 31.758691,-95.83335 31.758687,-95.831252 31.75865,-95.831253 31.760432,-95.831252 31.760502,").append(nxt2)
	  .append(" 31.761303,-95.830347 31.761285,-95.829896 31.760892,-95.829178 31.760883,-95.827652 31.760878,-95.827168 31.760874,-95.821293 31.76082,-95.81972 31.760805,-95.815488 31.760741,").append(nxt3)
	  .append(" 31.760741,-95.815244 31.76045,-95.815173 31.760274,-95.814993 31.759966,-95.814987 31.759873,-95.814935 31.759774,-95.81489 31.759625,-95.814852 31.759307,-95.814807 31.759043,-95.814877 31.758592,-95.814903 31.758328,-95.814948 31.758081,-95.814967 31.757839,-95.815077 31.757718,-95.815205 31.757614,-95.815373 31.757427,-95.815424 31.757328,-95.81545 31.757235,-95.815437 31.757153,-95.815373 31.757043,-95.815038 31.756592,-95.814987 31.756471,-95.81498 31.756411,-95.814993 31.756345,-95.81516 31.755806,-95.815225 31.755707,-95.81527 31.755586,-95.815295 31.75546,-95.815276 31.755202,-95.81534 31.754801,-95.815347 31.75457,-95.815289 31.754394,-95.815237 31.754317,-95.815077 31.754163,-95.814967 31.754042,-95.814877 31.753916,-95.814832 31.753833,-95.814832 31.75374,-95.814852 31.753581,-95.81489 31.75341,-95.814942 31.753267,-95.815019 31.753152,-95.815115 31.753048,-95.815225 31.752965,-95.81543 31.752866,-95.815675 31.752668,-95.815758 31.752614,-95.815842 31.75257,-95.816016 31.75252,-95.816099 31.752476,-95.816196 31.752416,-95.816466 31.752201,-95.816569 31.752146,-95.816671 31.752135,-95.816916 31.752157,-95.817006 31.752152,-95.817173 31.752086,-95.817707 31.751778,-95.817868 31.751701,-95.818208 31.751569,-95.818311 31.751575,-95.818369 31.751569,-95.818434 31.751547,-95.818498 31.751509,-95.818594 31.751421,-95.818633 31.751366,-95.818691 31.751201,-95.818755 31.75113,-95.818813 31.751053,-95.818839 31.750965,-95.818871 31.750762,-95.818909 31.750679,-95.818974 31.750619,-95.819012 31.750597,-95.81907 31.750586,-95.819218 31.75058,-95.819302 31.750613,-95.819359 31.750624,-95.819533 31.750619,-95.819752 31.750553,-95.819829 31.750448,-95.819784 31.750311,-95.819677 31.750171,-95.823082 31.750169,-95.82495 31.750169,-95.826887 31.750169,-95.831342 31.750169,-95.833195 31.750169,-95.835102 31.750163,-95.835147 31.750002,-95.835224 31.74964,-95.835308 31.749568,-95.835469 31.749513,-95.835694 31.749524,-95.844317 31.749351,-95.844664 31.749274,-95.844864 31.749126,-95.844986 31.748933,-95.845005 31.748791,-95.845307 31.748785,-95.845314 31.7489,-95.845307 31.749093,-95.845365 31.749461,-95.845475 31.749999,-95.8455 31.750252,-95.845488 31.75068,-95.845565 31.750939,-95.845629 31.751263,-95.845668 31.751681,-95.845681 31.752065,-95.845778 31.75262,-95.845778 31.753335,-95.845926 31.754181,-95.845991 31.754659,-95.846094 31.755087,-95.846351 31.755791,-95.84657 31.756164,-95.846744 31.756356,-95.846956 31.756675,-95.847207 31.756911,-95.847516 31.757257,-95.847677 31.757461,-95.84778 31.757538,-95.847831 31.757609,-95.848056 31.757702,-95.848134 31.757741,-95.848172 31.757779,-95.848172 31.75806,-95.848204 31.758334,-95.848404 31.758658,-95.848534 31.758758,-95.847873 31.758762,-95.847078 31.758759,-95.84697 31.758642,-95.846848 31.758533,-95.846738 31.758472,-95.846674 31.75845,-95.846597 31.758439,-95.846224 31.758423,-95.845285 31.758407,-95.845156 31.758423,-95.843921 31.75839,-95.843407 31.758396,-95.843195 31.75838,-95.840731 31.758353,-95.840101 31.758358,-95.837535 31.758309,-95.837413 31.758298,-95.836937 31.758315,-95.836377 31.758315,-95.836268 31.758326,-95.836223 31.758354,-95.836184 31.758403,-95.836133 31.758518,-95.836133 31.758579,-95.836152 31.758667,-95.836175 31.75874,-95.835919 31.758732))'),'48','001','','110223506483','TEMP','K1237')");
	  String sql = sb.toString();
	 
	//  //// System.out.println(sql);  
	  return sql;
	  
 }
 
 public String getSpatialWriteCleanupArealmMerge() {
	  StringBuffer sb = new StringBuffer();
	  sb.append("delete from arealm_merge where fullname='TEMP'");
	  String sql = sb.toString();
	  // //// System.out.println(sql);  
	  return sql;
 }

//////////////////////////////////////////MACRO BENCHMARK: REVERSE GEOCODING/////////////////////////////////
 
 public  String getCityStateForReverseGeocoding(){ 
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement.
	  sb.append("SELECT first 1 name, st, distance(shape, GeometryFromText(?)) as dist FROM cityinfo order by dist ");	             
	  String sql = sb.toString();

	  return sql;
 }
 
 public  String getStreetAddressForReverseGeocoding(){ 
	  StringBuffer sb = new StringBuffer();

	 
	  
	  // Generate the join SQL statement.
	  sb.append("SELECT first 1 fullname,lfromadd,ltoadd,rfromadd,rtoadd,zipl,zipr, distance(shape, GeometryFromText(?))  as d FROM edges_merge   where Intersects(shape, GeometryFromText(?))=1    and distance(shape, GeometryFromText(?)) < 0.1   and  roadflg='Y' order by d   ");
	  String sql = sb.toString();

	  return sql;
 }
 
 //////////////////////////////////////////MACRO BENCHMARK: GEOCODING/////////////////////////////////
 
 public  String getGeocodingQuery(){ 
	 
	  String sql = "select t.tlid, t.fraddr, t.fraddl, t.toaddr, t.toaddl,"+ 
	  " t.zipL, t.zipR, t.tolat, t.tolong, t.frlong, t.frlat,"+  
	  " t.long1, t.lat1, t.long2, t.lat2, t.long3, t.lat3, t.long4, t.lat4,"+
	  " t.long5, t.lat5, t.long6, t.lat6, t.long7, t.lat7, t.long8, t.lat8,"+
	  " t.long9, t.lat9, t.long10, t.lat10, t.fedirp, t.fetype, t.fedirs from " +
	  " geocoder_address "+
	  " t where t.fename = ? and "+
	  "(" + 
	  "       (t.fraddL <= ? and t.toaddL >= ?) or (t.fraddL >= ? and t.toaddL <= ?) "+
	  "    or (t.fraddR <= ? and t.toaddR >= ?) or (t.fraddR >= ? and t.toaddR <= ?) "+
	  ")" +  
	  "  and (t.zipL = ? or t.zipR = ?)";

	  return sql;
 }
 
 
 //////////////////////////////////////////MACRO BENCHMARK: MAP SEARCH AND BROWSE /////////////////////////////////
 
 public String getMapSearchSiteSearchQuery(VisitScenario scenario) {
 	
 	// the landmark itself
 	String query = " select first 1 ogr_fid, name, astext(shape) as location from gnis_names09  where state='TX' and name='"+scenario.getChosenPoiName()+"' and class='"+scenario.getChosenPoiClassName()+"' ";
 
 	return query;
 }

 public String[] getMapSearchScenarioQueries(VisitScenario scenario) {
 	String[] queries =  new String[scenario.getTotalVisitSearches()];
 	int queryIndex=-1;
 	
 	String[] nearestEssentialPoiClassOnceMatchStrings=scenario.getNearestEssentialPoiClassOnceMatchStrings();
 	if (nearestEssentialPoiClassOnceMatchStrings!=null && nearestEssentialPoiClassOnceMatchStrings.length!=0) {
 		for (int i=0;i<nearestEssentialPoiClassOnceMatchStrings.length;i++) {
 			queryIndex++;
 			String classOnce= nearestEssentialPoiClassOnceMatchStrings[i];
 			//?s: lon lat lon lat
 			queries[queryIndex]= " select first 1 name, astext(shape) as location, distance(shape, GeometryFromText(?)) as dist from gnis_names09  where state='TX' and class like '%"+classOnce+"%' and " +
 			"(distance(shape, GeometryFromText(?))  <= "+VisitScenario.MAX_SEARCH_RADIUS+") and gid <> ? order by dist ";
 		}
 	}
 	
 	String[] nearestEssentialPoiNameOnceMatchStrings=scenario.getNearestEssentialPoiNameOnceMatchStrings();
 	if (nearestEssentialPoiNameOnceMatchStrings!=null && nearestEssentialPoiNameOnceMatchStrings.length!=0 ) {
 		for (int i=0;i<nearestEssentialPoiNameOnceMatchStrings.length;i++) {
 			queryIndex++;
 			String nameOnce= nearestEssentialPoiNameOnceMatchStrings[i];
 			//?s: lon lat lon lat
 			queries[queryIndex]= " select first 1 name, astext(shape) as location, distance(shape, GeometryFromText(?)) as dist from gnis_names09  where state='TX' and name like '%"+nameOnce+"%' and " +
 			"(distance(shape, GeometryFromText(?))  <= "+VisitScenario.MAX_SEARCH_RADIUS+") and gid <> ? order by dist ";
 		}
 	}
 	
 	String[] nearestEssentialPoiNameOrMatchStrings= scenario.getNearestEssentialPoiNameOrMatchStrings();
 	if (nearestEssentialPoiNameOrMatchStrings!=null && nearestEssentialPoiNameOrMatchStrings.length!=0) {
 		String nameOrMatchingStrings = "";
 		for (int i=0;i<nearestEssentialPoiNameOrMatchStrings.length;i++) {
 			String nameOr= nearestEssentialPoiNameOrMatchStrings[i];
 			if (i>0)
 				nameOrMatchingStrings	+=" or ";
 			nameOrMatchingStrings += "name like '%"+nameOr+"%'";
 		}
 		//?s: lon lat lon lat
 		queryIndex++;
			queries[queryIndex]= " select first 1 name, astext(shape) as location, distance(shape, GeometryFromText(?)) as dist from gnis_names09  where state='TX' and ("+nameOrMatchingStrings+") and " +
			"(distance(shape, GeometryFromText(?))  <= "+VisitScenario.MAX_SEARCH_RADIUS+") and gid <> ? order by dist ";
 	}
 	
 	int limit=scenario.getNearestEssentialPoiClassOrMatchOptionsNum();
 	  
 	String[] nearestEssentialPoiClassOrMatchStrings= scenario.getNearestEssentialPoiClassOrMatchStrings();
 	if (nearestEssentialPoiClassOrMatchStrings!=null && nearestEssentialPoiClassOrMatchStrings.length!=0) {
 		String classOrMatchingStrings = "";
 		for (int i=0;i<nearestEssentialPoiClassOrMatchStrings.length;i++) {
 			String classOr= nearestEssentialPoiClassOrMatchStrings[i];
 			if (i>0)
 				classOrMatchingStrings	+=" or ";
 			classOrMatchingStrings += "class like '%"+classOr+"%'";
 		}
 		//?s: lon lat lon lat
 		queryIndex++;
			queries[queryIndex]= " select first "+limit+" name, astext(shape) as location, distance(shape, GeometryFromText(?)) as dist from gnis_names09  where state='TX' and ("+classOrMatchingStrings+") and " +
			"(distance(shape, GeometryFromText(?))  <= "+VisitScenario.MAX_SEARCH_RADIUS+") and gid <> ? order by dist ";
 	}
 	
 	limit=scenario.getNearestOptionalPoiNameOrMatchOptionsNum();
 	
 	String[] nearestOptionalPoiNameOrMatchStrings= scenario.getNearestOptionalPoiNameOrMatchStrings();
 	if (nearestOptionalPoiNameOrMatchStrings!=null && nearestOptionalPoiNameOrMatchStrings.length!=0) {
 		String nameOrMatchingStrings = "";
 		for (int i=0;i<nearestOptionalPoiNameOrMatchStrings.length;i++) {
 			String nameOr= nearestOptionalPoiNameOrMatchStrings[i];
 			if (i>0)
 				nameOrMatchingStrings	+=" or ";
 			nameOrMatchingStrings += "name like '%"+nameOr+"%'";
 		}
 		//?s: lon lat lon lat
 		queryIndex++;
			queries[queryIndex]= " select first "+limit+" name, astext(shape) as location, distance(shape, GeometryFromText(?)) as dist from gnis_names09  where state='TX' and ("+nameOrMatchingStrings+") and " +
			"(distance(shape, GeometryFromText(?))  <= "+VisitScenario.MAX_SEARCH_RADIUS+") and gid <> ? order by dist ";
 	}
 	
 	String[] nearestOptionalPoiClassAnyMatchStrings= scenario.getNearestOptionalPoiClassOrMatchStrings();
 	if (nearestOptionalPoiClassAnyMatchStrings!=null && nearestOptionalPoiClassAnyMatchStrings.length!=0) {
 		String classOrMatchingStrings = "";
 		for (int i=0;i<nearestOptionalPoiClassAnyMatchStrings.length;i++) {
 			String classOr= nearestOptionalPoiClassAnyMatchStrings[i];
 			if (i>0)
 				classOrMatchingStrings	+=" or ";
 			classOrMatchingStrings += "class like '%"+classOr+"%'";
 		}
 		//?s: lon lat lon lat
 		queryIndex++;
			queries[queryIndex]= " select first 1 name, astext(shape) as location, distance(shape, GeometryFromText(?)) as dist from gnis_names09  where state='TX' and ("+classOrMatchingStrings+") and " +
			"(distance(shape, GeometryFromText(?))  <= "+VisitScenario.MAX_SEARCH_RADIUS+") and gid <> ? order by dist ";
 	}
 	
 	return queries;
 }
 
 public String[] getMapBrowseBoundingBoxQueries() {
 	
 	// 5 queries
 	String[] queries =  new String[5];
 	
 	queries[0] = "SELECT ogr_fid, asBinary(shape) as shp FROM gnis_names09 WHERE Intersects(GeometryFromText(?), shape)=1 ";
  	queries[1] = "SELECT ogr_fid, asBinary(shape) as shp FROM arealm_merge WHERE Intersects(GeometryFromText(?), shape)=1 ";
  	queries[2] = "SELECT ogr_fid, asBinary(shape) as shp FROM areawater_merge WHERE Intersects(GeometryFromText(?), shape)=1 ";
  	queries[3] = "SELECT ogr_fid, asBinary(shape) as shp FROM pointlm_merge WHERE Intersects(GeometryFromText(?), shape)=1 ";
  	queries[4] = "SELECT ogr_fid, asBinary(shape) as shp FROM edges_merge WHERE Intersects(GeometryFromText(?), shape)=1 ";
  	
  	
 	return queries;
 }
 
 //////////////////////////////////////////MACRO BENCHMARK: LAND USE /////////////////////////////////
 public String[] getLandUseQueries() { 
	  
	//6 queries
		String[] queries =  new String[6]; 
		// queries
		
		// What is the average property value per sq foot for Single-Family Residential properties
		queries[0] = "select sum(pa.marketvalu)/sum(area(pa.shape)) from parcels2008 as pa where pa.land_state like 'A%'";
		  
		//How many residential properties have a  hospital within one mile radius in Austin 
		queries[1] = "select count(*) from  land_use_2006 as lu, hospitals as h where lu.general_la in (100,113,150,160,200) and  Distance(lu.shape, h.shape) <= 5280 ";
					  
		// Determine the average property values  within a one mile radius of all hospitals in Travis county 
		queries[2] = "select sc.name, avg(pa.marketvalu) as avg_property_value from parcels2008 as pa, hospitals as sc where Distance(pa.shape, sc.shape) <= 5280 group by sc.name order by avg_property_value desc ";

		//Rewritten query: Waterfront properties: show all the property values sorted by price within a 100 feet of the three major lakes in Travis county (first 10 matches)
		queries[3] = "select first 10 lake.wtr_nm, geo_id, pa.marketvalu as property_value from parcels2008 as pa, s_wtr_ar as lake where  Distance(lake.shape, pa.shape) <= 100 and pa.marketvalu> 0 ";
	   	
		//Which office buildings have front yard parking restrictions 
		queries[4] = "select property_i from  land_use_2006 as lu, frontyard_parking_restrictions fypr where lu.general_la=400  and  Overlaps(fypr.shape, lu.shape)=1";
					  
				
		// Show the commercial properties that are built on un-permitted landfills
		queries[5] = "select lu.property_i from  land_use_2006 as lu, landfills lf where lu.general_la=300 and lf.permitted='UNPERMITTED' and Intersects(lf.shape,lu.shape)=1";
					  
	
	return queries;
 }
 
 //////////////////////////////////////////MACRO BENCHMARK: ENVIRONMENTAL HAZARD /////////////////////////////////
 public String[] getEnvHazardQueries() { 
	 // 4 queries
		String[] queries =  new String[4]; 
		
		//Ok - Show the flood risk areas that are protected by one or more dams.
		queries[0] = "select distinct fld_ar_id from s_gen_struct st, s_fld_haz_ar fa where st.struct_typ='DAM' and Intersects(st.shape, fa.shape)=1 ";
		  			  			  
		
		//Ok - Show the total flood risk area in acres  grouped by risk area category
		queries[1] = "select fld_zone, sum(Area(s_fld_haz_ar.shape)/43560) as area from s_fld_haz_ar group by fld_zone ";
					  
		
		//rewritten; wait:  Show the total high risk flood risk area in acres  within one mile of Lake Travis.	
		//queries[2] = "select first 1 fld_ar_id, ST_Area(fa.shape)/43560 as flood_area from s_fld_haz_ar fa, s_wtr_ar wa where wa.wtr_nm='Lake Travis' and (fld_zone='A' or fld_zone='AE' or fld_zone='AO' or fld_zone='V') and ST_Distance(fa.shape, wa.shape) <= 5280 ";
		
		//rewritten; ok:  Owners of buildings located within an A or V Zone are required to carry flood insurance. 
		// Which residential property owners are requires to carry flood insurance?
		queries[2] = "select first 10 lu.property_i from  land_use_2006 as lu,  s_fld_haz_ar fz where lu.general_la in (100,113,150,160,200) and (fld_zone='A' or fld_zone='AE' or fld_zone='AO' or fld_zone='V') and Overlaps(lu.shape, fz.shape)=1  ";
		
		//rewritten; wait: Which industrial complexes are in high risk flood area?
		queries[3] = "select first 10 lu.property_i from  land_use_2006 as lu,  s_fld_haz_ar fz where lu.general_la=500 and (fld_zone='A' or fld_zone='AE' or fld_zone='AO' or fld_zone='V') and Overlaps(lu.shape, fz.shape)=1 ";
		
	return queries;
 }
  
}
