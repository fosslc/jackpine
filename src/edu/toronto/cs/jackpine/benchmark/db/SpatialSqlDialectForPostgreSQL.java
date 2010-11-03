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

import java.util.ResourceBundle;

import com.continuent.bristlecone.benchmark.db.SqlDialectForPostgreSQL;

import edu.toronto.cs.jackpine.benchmark.db.SpatialSqlDialect.SupportedSqlDialect;
import edu.toronto.cs.jackpine.benchmark.scenarios.macroscenario.VisitScenario;

/**
 * PostgreSQL spatial DBMS dialect information. 
 * 
 * @author sray
 */
public class SpatialSqlDialectForPostgreSQL extends SqlDialectForPostgreSQL implements SpatialSqlDialect
{
  static String SRID = "";
  
  static {
	  ResourceBundle rs = ResourceBundle.getBundle("connection_general");
	  SRID = rs.getString("POSTGRESQL_SRID").trim();
  }
  
  /**
   * 
   * @return
   */
  public SupportedSqlDialect getSqlDialectType() {
	  return SupportedSqlDialect.PostgreSQL;
  }
  
	
  public String getMaxRowidFromSpatialTableEdgesMerge(){return "";}
  public String getMaxRowidFromSpatialTableArealmMerge(){return "";}
  
  public String getSelectAllFeaturesWithinADistanceFromPoint()
  {
    StringBuffer sb = new StringBuffer();

    // Generate the join SQL statement. 32633
    sb.append("select count(*) from arealm_merge  where Distance (the_geom, ST_SetSRID(ST_MakePoint(-97.7,30.30), "+SRID+")) < 1000");
    String sql = sb.toString();
    // System.out.println("The query "+ sql);
    return sql;
  }
  
  public String getSelectBoundingBoxSearch(){ 
	  
	  StringBuffer sb = new StringBuffer();

	    // Generate the join SQL statement.
	    sb.append("select count(*) from edges_merge");
	    sb.append(" where Within( the_geom,  ST_SetSRID(PolyFromText('POLYGON((-97.7 30.30, -92.7 30.30, -92.7 27.30, -97.7 27.30, -97.7 30.30))') ,"+SRID+"))");
	    String sql = sb.toString();
	   // System.out.println("The query "+ sql);
	    return sql;
  }
  
  public String getSelectDimensionPolygon(){
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement.
	  sb.append("select ST_Dimension(a.the_geom) from  arealm_merge a");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
  }
  
  public String getSelectBufferPolygon(){
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement.
	  sb.append("select ST_Buffer(a.the_geom,5280) from  arealm_merge a");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
  }
  
  public String getSelectConvexHullPolygon(){
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement.
	  sb.append("select ST_ConvexHull(a.the_geom) from  arealm_merge a");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
  }
  
   
  
  public String getSelectEnvelopeLine(){
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement.
	  sb.append("select AsText(ST_Envelope(e.the_geom)) from  edges_merge e limit 1000");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
  }
  
  public String getSelectLongestLine(){
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement.
	  sb.append("SELECT  fullname, ST_Length(the_geom) AS line FROM edges_merge order BY line DESC limit 1");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
	  
  }
  
  public  String getSelectLargestArea(){ 
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement.
	  sb.append("SELECT  fullname, ST_Area(the_geom) AS area FROM areawater_merge order BY area DESC limit 1");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
  }
  
  public  String getSelectTotalLength(){ 
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement.
	  sb.append("SELECT sum(ST_Length(the_geom))/1000 AS km_roads FROM edges_merge");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
  }
 
  
  public String getSelectTotalArea(){
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement.
	  sb.append("SELECT sum(ST_Area(the_geom)) AS area FROM areawater_merge");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
	  
  }
  
  //////////////////////////////////////// SPATIAL JOIN//////////////////////////////////////////////////
  // all the polygons that are intersected by the longest line: 1
  public String[] getSelectLongestLineIntersectsArea(){ 
	  //StringBuffer sb = new StringBuffer();
	  //sb.append("select count(*) from  areawater_merge a, edges_merge e where ST_Intersects(e.the_geom, a.the_geom) and e.gid= (select gid from edges_merge order by ST_Length(the_geom) desc limit 1)");
	  //String sql = sb.toString();
	  
	  String[] queries =  new String[2]; 
	  queries[0] = "select gid as id from edges_merge order by ST_Length(the_geom) desc limit 1";
	  queries[1] = "select count(*) from  areawater_merge a, edges_merge e where ST_Intersects(e.the_geom, a.the_geom) and e.gid= ?";
			  
	  //// System.out.println("The query "+ sql);
	  return queries;
  }
  
  //all the lines that intersect the largest area: 371
  public String[] getSelectLineIntersectsLargestArea(){ 
	  //StringBuffer sb = new StringBuffer();
	  //sb.append("select count(*) from  arealm_merge a, edges_merge e where ST_Intersects(e.the_geom, a.the_geom) and a.gid= (select gid from arealm_merge order by ST_Area(the_geom) desc limit 1)");
	  //String sql = sb.toString();
	  
	  String[] queries =  new String[2]; 
	  queries[0] = "select gid from arealm_merge order by ST_Area(the_geom) desc limit 1";
	  queries[1] = "select count(*) from  arealm_merge a, edges_merge e where ST_Intersects(e.the_geom, a.the_geom) and a.gid= ?";
			  
	  //// System.out.println("The query "+ sql);
	  return queries;
  }
  
  
  //all the area overlaps the largest area:
  public String[] getSelectAreaOverlapsLargestArea(){ 
	  //StringBuffer sb = new StringBuffer();
	  //sb.append("select count(*) from  arealm_merge al, areawater_merge aw where ST_Overlaps(al.the_geom, aw.the_geom) and al.gid= (select gid from arealm_merge order by ST_Area(the_geom) desc limit 1)");
	  //String sql = sb.toString();
	  
	  String[] queries =  new String[2]; 
	  queries[0] = "select gid from arealm_merge order by ST_Area(the_geom) desc limit 1";
	  queries[1] = "select count(*) from  arealm_merge al, areawater_merge aw where ST_Overlaps(al.the_geom, aw.the_geom) and al.gid= ?";
			  
	  //// System.out.println("The query "+ sql);
	  return queries;
  
  }
  
  //all the point within the largest area:
  public String[] getSelectLargestAreaContainsPoint(){ 
	  //StringBuffer sb = new StringBuffer();
	  //sb.append("select count(*) from  pointlm_merge pl, areawater_merge aw where ST_Contains(aw.the_geom, pl.the_geom) and aw.gid= (select gid from areawater_merge order by ST_Area(the_geom) desc limit 1)");
	  //String sql = sb.toString();
	  
	  String[] queries =  new String[2]; 
	  queries[0] = "select gid from areawater_merge order by ST_Area(the_geom) desc limit 1";
	  queries[1] = "select count(*) from  pointlm_merge pl, areawater_merge aw where ST_Contains(aw.the_geom, pl.the_geom) and aw.gid= ?";
			  
	  //// System.out.println("The query "+ sql);
	  return queries;  
  }
  
  ////////////////////////////////////////ALLPAIR SPATIAL JOIN////////////////////////////////////////////
  
  ///////////////////////////////////////POLYGON AND POLYGON//////////////////////////////////////////////
  
  public String getSelectAreaOverlapsArea(){ 
	  StringBuffer sb = new StringBuffer();
	  sb.append("select a1.fullname from  arealm_merge a1 , arealm_merge a2 where ST_overlaps(a1.the_geom, a2.the_geom)");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
  }
  
  public String getSelectAreaContainsArea(){  
	  StringBuffer sb = new StringBuffer();
  	  sb.append("select a1.fullname from  arealm_merge a1 , arealm_merge a2 where ST_contains(a1.the_geom, a2.the_geom)");
  	  String sql = sb.toString();
  	  // System.out.println("The query "+ sql);
  	  return sql;
  }
  
  public String getSelectAreaWithinArea(){ 
	  StringBuffer sb = new StringBuffer();
  	  sb.append("select a1.fullname from  arealm_merge a1 , arealm_merge a2 where ST_within(a1.the_geom, a2.the_geom)");
  	  String sql = sb.toString();
  	  // System.out.println("The query "+ sql);
  	  return sql;
  }
  
  public String getSelectAreaTouchesArea(){ 
	  StringBuffer sb = new StringBuffer();
  	  sb.append("select a1.fullname from  arealm_merge a1 , arealm_merge a2 where ST_touches(a1.the_geom, a2.the_geom)");
  	  String sql = sb.toString();
  	  // System.out.println("The query "+ sql);
  	  return sql;
  }
  
  public String getSelectAreaEqualsArea(){ 
	  StringBuffer sb = new StringBuffer();
  	  sb.append("select a1.fullname from  arealm_merge a1 , arealm_merge a2 where ST_equals(a1.the_geom, a2.the_geom)");
  	  String sql = sb.toString();
  	  // System.out.println("The query "+ sql);
  	  return sql;
  }
  
  public String getSelectAreaDisjointArea(){ 
	  StringBuffer sb = new StringBuffer();
	  sb.append("select count(*) from  arealm_merge a1, arealm_merge a2 where ST_Disjoint(a1.the_geom, a2.the_geom)");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
  }
  
  ///////////////////////////////////////LINE AND POLYGON//////////////////////////////////////////////
  
  public  String getSelectLineIntersectsArea(){
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement.
	  sb.append("select a.fullname, e.fullname from  arealm_merge a, edges_merge e where ST_Intersects(e.the_geom, a.the_geom)");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
  }
  
  public  String getSelectLineOverlapsArea(){ 
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement.
	  sb.append("select a.fullname, e.fullname from  arealm_merge a, edges_merge e where ST_Overlaps(e.the_geom, a.the_geom)");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
  }
  
  public String getSelectLineCrossesArea(){
	  StringBuffer sb = new StringBuffer();
	  sb.append("select e.fullname from  edges_merge e , arealm_merge a where ST_crosses(e.the_geom, a.the_geom)");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
  }
  
  public String getSelectLineWithinArea(){
	  StringBuffer sb = new StringBuffer();
	  sb.append("select e.fullname from  edges_merge e , arealm_merge a where ST_within(e.the_geom, a.the_geom)");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
  }
  
  public String getSelectLineTouchesArea(){
	  StringBuffer sb = new StringBuffer();
	  sb.append("select e.fullname from  edges_merge e , arealm_merge a where ST_Touches(e.the_geom, a.the_geom)");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql; 
  }
  
  
  ///////////////////////////////////////LINE AND LINE//////////////////////////////////////////////
  
  public String getSelectLineOverlapsLine(){ 
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement. 
	  sb.append("select e1.fullname  from  edges_merge e1 , edges_merge e2 where ST_overlaps(e1.the_geom, e2.the_geom) limit 1 ");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
  }
  
  public String getSelectLineCrossesLine(){ 
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement. 
	  sb.append("select e1.fullname  from  edges_merge e1 , edges_merge e2 where ST_crosses(e1.the_geom, e2.the_geom) limit 1 ");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
  }
  
  ///////////////////////////////////////POINT AND //////////////////////////////////////////////
  
   
  public  String getSelectPointWithinArea(){ 
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement.
	  sb.append("select count(*) from  arealm_merge a, pointlm_merge p where ST_Within(p.the_geom, a.the_geom)");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
  }   
  
  public  String getSelectPointIntersectsArea(){
	  StringBuffer sb = new StringBuffer();
	  sb.append("select count(*) from  arealm_merge a, pointlm_merge p where ST_Intersects(p.the_geom, a.the_geom)");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
  }
  
  public  String getSelectPointIntersectsLine(){
	  StringBuffer sb = new StringBuffer();
	  sb.append("select count(*) from  edges_merge e, pointlm_merge p where ST_Intersects(p.the_geom, e.the_geom)");
	  String sql = sb.toString();
	  // System.out.println("The query "+ sql);
	  return sql;
  }
  
  public  String getSelectPointEqualsPoint(){
	  StringBuffer sb = new StringBuffer();
	  sb.append("select count(*) from  pointlm_merge p1, pointlm_merge p2 where ST_Equals(p1.the_geom, p2.the_geom)");
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
  
	  /*
	  sb.append("insert into edges_merge (statefp,countyfp,tlid, tfidl, tfidr, mtfcc, fullname, smid, lfromadd, ltoadd, rfromadd, rtoadd, zipl, zipr, featcat, hydroflg, railflg, roadflg, olfflg, passflg, divroad, exttyp, ttyp, deckedroad, artpath, persist, gcseflg, offsetl, offsetr, tnidf, tnidt, the_geom ) values ('48','001',74690317,205838157,205838661,'P0002','TEMP','447','','','','','','','','N','N','N','N','','','N','','','','','N','N','0',14062338,14062338,  ST_GeometryFromText('MULTILINESTRING((-95.43954 31.581223,").append(nxt1)
	  .append(" 31.581286,-95.439587 31.581385,-95.439555 31.581396,-95.439535 31.58139,-95.439452 31.581401,-95.439433 31.581412,-95.439433 31.581445,-95.439426 31.581463,-95.439407 31.581473,-95.439386 31.581471,").append(nxt2)
	  .append(" 31.581462,-95.439337 31.581445,-95.439285 31.581434,-95.439273 31.581418,-95.439274 31.5814,-95.439292 31.581385,-95.43933 31.581374,-95.439337 31.581346,-95.439329 31.581329,-95.439285 31.581269,-95.439215 31.581214,-95.439164 31.581187,-95.439144 31.581165,-95.439086 31.581121,-95.438952 31.581077,-95.438759 31.581028,-95.438714 31.581011,-95.438676 31.580989,-95.438643 31.580962,-95.438611 31.580912,").append(nxt3)
	  .append(" 31.580863,-95.438586 31.580813,-95.438566 31.580797,-95.438277 31.580709,-95.438263 31.580695,-95.438252 31.580676,-95.438245 31.580643,-95.438207 31.580593,-95.438181 31.580577,-95.438149 31.580566,-95.438104 31.58056,-95.438078 31.580533,-95.438073 31.580515,-95.43813 31.580478,-95.438277 31.580434,-95.438432  31.580401))',"+SRID+"));");	 
	 */
	  
	  sb.append("insert into edges_merge (statefp,countyfp,tlid, tfidl, tfidr, mtfcc, fullname, smid, lfromadd, ltoadd, rfromadd, rtoadd, zipl, zipr, featcat, hydroflg, railflg, roadflg, olfflg, passflg, divroad, exttyp, ttyp, deckedroad, artpath, persist, gcseflg, offsetl, offsetr, tnidf, tnidt, the_geom ) values ('48','001',74690317,205838157,205838661,'P0002','TEMP','447','','','','','','','','N','N','N','N','','','N','','','','','N','N','0',14062338,14062338,  ST_GeometryFromText('MULTILINESTRING((-95.43954 31.581223,-95.439587 31.581385,-95.439555 31.581396,-95.439535 31.58139,-95.439452 31.581401,-95.439433 31.581412,-95.439433 31.581445,-95.439426 31.581463,-95.439407 31.581473,-95.439386 31.581471,-95.439337 31.581445,-95.439285 31.581434,-95.439273 31.581418,-95.439274 31.5814,-95.439292 31.581385,-95.43933 31.581374,-95.439337 31.581346,-95.439329 31.581329,-95.439285 31.581269,-95.439215 31.581214,-95.439164 31.581187,-95.439144 31.581165,-95.439086 31.581121,-95.438952 31.581077,-95.438759 31.581028,-95.438714 31.581011,-95.438676 31.580989,-95.438643 31.580962,-95.438611 31.580912,-95.438586 31.580813,-95.438566 31.580797,-95.438277 31.580709,-95.438263 31.580695,-95.438252 31.580676,-95.438245 31.580643,-95.438207 31.580593,-95.438181 31.580577,-95.438149 31.580566,-95.438104 31.58056,-95.438078 31.580533,-95.438073 31.580515,-95.43813 31.580478,-95.438277 31.580434,-95.438432  31.580401))',"+SRID+"));");	 
	
	  String sql = sb.toString();
	  // System.out.println(sql);  
	  return sql;
  }
  
  public String getSpatialWriteCleanupEdgesMerge() {
	  StringBuffer sb = new StringBuffer();
	  //sb.append("delete from edges_merge where fullname='TEMP'; vacuum full analyze"); 
	  sb.append("delete from edges_merge where fullname='TEMP'");//TODO: change back to last line
	  String sql = sb.toString(); 
	  //System.out.println(sql);  
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
	  
	  /*
	  sb.append("insert into arealm_merge (statefp, countyfp, ansicode, areaid, fullname, mtfcc, the_geom) values ('48','001','','110223506483','TEMP','K1237', ST_GeometryFromText('MULTIPOLYGON(((-95.835919 31.758732,-95.83358 31.758691,-95.83335 31.758687,-95.831252 31.75865,").append(nxt1)
	  .append(" 31.760432,-95.831252 31.760502,-95.831243 31.761303,-95.830347 31.761285,-95.829896 31.760892,-95.829178 31.760883,-95.827652 31.760878,-95.827168 31.760874,-95.821293 31.76082,-95.81972 31.760805,-95.815488 31.760741,-95.815307 31.760741,-95.815244 31.76045,-95.815173 31.760274,-95.814993 31.759966,-95.814987 31.759873,-95.814935 31.759774,-95.81489 31.759625,-95.814852 31.759307,-95.814807 31.759043,-95.814877 31.758592,-95.814903 31.758328,-95.814948 31.758081,-95.814967 31.757839,-95.815077 31.757718,").append(nxt2)
	  .append(" 31.757614,-95.815373 31.757427,-95.815424 31.757328,-95.81545 31.757235,-95.815437 31.757153,-95.815373 31.757043,-95.815038 31.756592,-95.814987 31.756471,-95.81498 31.756411,-95.814993 31.756345,-95.81516 31.755806,-95.815225 31.755707,-95.81527 31.755586,-95.815295 31.75546,-95.815276 31.755202,-95.81534 31.754801,-95.815347 31.75457,-95.815289 31.754394,-95.815237 31.754317,-95.815077 31.754163,-95.814967 31.754042,-95.814877 31.753916,-95.814832 31.753833,-95.814832 31.75374,-95.814852 31.753581,-95.81489 31.75341,-95.814942 31.753267,-95.815019 31.753152,-95.815115 31.753048,-95.815225 31.752965,-95.81543 31.752866,-95.815675 31.752668,-95.815758 31.752614,-95.815842 31.75257,-95.816016 31.75252,-95.816099 31.752476,-95.816196 31.752416,-95.816466 31.752201,-95.816569 31.752146,-95.816671 31.752135,-95.816916 31.752157,-95.817006 31.752152,-95.817173 31.752086,-95.817707 31.751778,-95.817868 31.751701,-95.818208 31.751569,-95.818311 31.751575,-95.818369 31.751569,-95.818434 31.751547,-95.818498 31.751509,-95.818594 31.751421,-95.818633 31.751366,-95.818691 31.751201,-95.818755 31.75113,-95.818813 31.751053,-95.818839 31.750965,-95.818871 31.750762,-95.818909 31.750679,-95.818974 31.750619,-95.819012 31.750597,-95.81907 31.750586,-95.819218 31.75058,-95.819302 31.750613,-95.819359 31.750624,-95.819533 31.750619,-95.819752 31.750553,-95.819829 31.750448,-95.819784 31.750311,-95.819677 31.750171,-95.823082 31.750169,-95.82495 31.750169,-95.826887 31.750169,-95.831342 31.750169,-95.833195 31.750169,-95.835102 31.750163,-95.835147 31.750002,-95.835224 31.74964,-95.835308 31.749568,-95.835469 31.749513,-95.835694 31.749524,-95.844317 31.749351,-95.844664 31.749274,-95.844864 31.749126,-95.844986 31.748933,-95.845005 31.748791,-95.845307 31.748785,-95.845314 31.7489,-95.845307 31.749093,-95.845365 31.749461,-95.845475 31.749999,-95.8455 31.750252,-95.845488 31.75068,-95.845565 31.750939,-95.845629 31.751263,-95.845668 31.751681,").append(nxt3)
	  .append(" 31.752065,-95.845778 31.75262,-95.845778 31.753335,-95.845926 31.754181,-95.845991 31.754659,-95.846094 31.755087,-95.846351 31.755791,-95.84657 31.756164,-95.846744 31.756356,-95.846956 31.756675,-95.847207 31.756911,-95.847516 31.757257,-95.847677 31.757461,-95.84778 31.757538,-95.847831 31.757609,-95.848056 31.757702,-95.848134 31.757741,-95.848172 31.757779,-95.848172 31.75806,-95.848204 31.758334,-95.848404 31.758658,-95.848534 31.758758,-95.847873 31.758762,-95.847078 31.758759,-95.84697 31.758642,-95.846848 31.758533,-95.846738 31.758472,-95.846674 31.75845,-95.846597 31.758439,-95.846224 31.758423,-95.845285 31.758407,-95.845156 31.758423,-95.843921 31.75839,-95.843407 31.758396,-95.843195 31.75838,-95.840731 31.758353,-95.840101 31.758358,-95.837535 31.758309,-95.837413 31.758298,-95.836937 31.758315,-95.836377 31.758315,-95.836268 31.758326,-95.836223 31.758354,-95.836184 31.758403,-95.836133 31.758518,-95.836133 31.758579,-95.836152 31.758667,-95.836175 31.75874,-95.835919 31.758732)))',"+SRID+"))");
;
	 */
	  
	  sb.append("insert into arealm_merge (statefp, countyfp, ansicode, areaid, fullname, mtfcc, the_geom) values ('48','001','','110223506483','TEMP','K1237', ST_GeometryFromText('MULTIPOLYGON(((-95.835919 31.758732,-95.83358 31.758691,-95.83335 31.758687,-95.831252 31.75865,-95.831252 31.760502,-95.831243 31.761303,-95.830347 31.761285,-95.829896 31.760892,-95.829178 31.760883,-95.827652 31.760878,-95.827168 31.760874,-95.821293 31.76082,-95.81972 31.760805,-95.815488 31.760741,-95.815307 31.760741,-95.815244 31.76045,-95.815173 31.760274,-95.814993 31.759966,-95.814987 31.759873,-95.814935 31.759774,-95.81489 31.759625,-95.814852 31.759307,-95.814807 31.759043,-95.814877 31.758592,-95.814903 31.758328,-95.814948 31.758081,-95.814967 31.757839,-95.815077 31.757718,-95.815373 31.757427,-95.815424 31.757328,-95.81545 31.757235,-95.815437 31.757153,-95.815373 31.757043,-95.815038 31.756592,-95.814987 31.756471,-95.81498 31.756411,-95.814993 31.756345,-95.81516 31.755806,-95.815225 31.755707,-95.81527 31.755586,-95.815295 31.75546,-95.815276 31.755202,-95.81534 31.754801,-95.815347 31.75457,-95.815289 31.754394,-95.815237 31.754317,-95.815077 31.754163,-95.814967 31.754042,-95.814877 31.753916,-95.814832 31.753833,-95.814832 31.75374,-95.814852 31.753581,-95.81489 31.75341,-95.814942 31.753267,-95.815019 31.753152,-95.815115 31.753048,-95.815225 31.752965,-95.81543 31.752866,-95.815675 31.752668,-95.815758 31.752614,-95.815842 31.75257,-95.816016 31.75252,-95.816099 31.752476,-95.816196 31.752416,-95.816466 31.752201,-95.816569 31.752146,-95.816671 31.752135,-95.816916 31.752157,-95.817006 31.752152,-95.817173 31.752086,-95.817707 31.751778,-95.817868 31.751701,-95.818208 31.751569,-95.818311 31.751575,-95.818369 31.751569,-95.818434 31.751547,-95.818498 31.751509,-95.818594 31.751421,-95.818633 31.751366,-95.818691 31.751201,-95.818755 31.75113,-95.818813 31.751053,-95.818839 31.750965,-95.818871 31.750762,-95.818909 31.750679,-95.818974 31.750619,-95.819012 31.750597,-95.81907 31.750586,-95.819218 31.75058,-95.819302 31.750613,-95.819359 31.750624,-95.819533 31.750619,-95.819752 31.750553,-95.819829 31.750448,-95.819784 31.750311,-95.819677 31.750171,-95.836184 31.758403,-95.836133 31.758518,-95.836133 31.758579,-95.836152 31.758667,-95.836175 31.75874,-95.835919 31.758732)))',"+SRID+"))"); 
	  String sql = sb.toString();
	  
	  // System.out.println(sql);  
	  return sql;
	  
  }
  
  public String getSpatialWriteCleanupArealmMerge() {
	  StringBuffer sb = new StringBuffer();
	  //sb.append("delete from arealm_merge where fullname='TEMP'; vacuum full analyze");
	  sb.append("delete from arealm_merge where fullname='TEMP'"); //TODO: change back to last line
	  String sql = sb.toString();
	  // System.out.println(sql);  
	  return sql;
  }
  
  //////////////////////////////////////////MACRO BENCHMARK: REVERSE GEOCODING/////////////////////////////////
  
  public  String getCityStateForReverseGeocoding(){ 
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement.
	  sb.append("SELECT name, st, distance(the_geom, GeomFromText(?, "+SRID+" )) as dist FROM cityinfo order by dist limit 1 ");
	  String sql = sb.toString();

	  return sql;
  }
  
  public  String getStreetAddressForReverseGeocoding(){ 
	  StringBuffer sb = new StringBuffer();

	 
	  
	  // Generate the join SQL statement.
	  //sb.append("SELECT  fullname,lfromadd,ltoadd,rfromadd,rtoadd,zipl,zipr, distance(the_geom, GeomFromText(?,"+SRID+" )) " + " as d FROM edges_merge " +
      //          "  where the_geom && ?::box3d  " +
      //          "  and distance(the_geom, GeomFromText(?,"+SRID+" )) < 50 " +
      //          "  and  roadflg='Y' order by d limit 1;");
	  
	  sb.append("SELECT fullname,lfromadd,ltoadd,rfromadd,rtoadd,zipl,zipr, distance(the_geom, GeomFromText(?,"+SRID+" ))  as d FROM edges_merge   where St_Intersects(the_geom, GeomFromText(?,"+SRID+"))    and distance(the_geom, GeomFromText(?,"+SRID+" )) < 0.1   and  roadflg='Y' order by d limit 1 "); //'POINT(-97.74331964136722 30.41450086861228)','POLYGON ((-97.72610571362193 30.426874127719113, -97.74610571362194 30.426874127719113, -97.74610571362194 30.40687412771911, -97.72610571362193 30.40687412771911, -97.72610571362193 30.426874127719113))','POINT(-97.74331964136722 30.41450086861228)'
	  
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
  	String query = " select gid, name, astext(the_geom) as location from gnis_names09  where state='TX' and name='"+scenario.getChosenPoiName()+"' and class='"+scenario.getChosenPoiClassName()+"' limit 1";
  
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
  			queries[queryIndex]= " select name, astext(the_geom) as location, distance(the_geom, GeomFromText(?, "+SRID+" )) as dist from gnis_names09  where state='TX' and class like '%"+classOnce+"%' and " +
  			"(distance(the_geom, GeomFromText(?, "+SRID+" ))  <= "+VisitScenario.MAX_SEARCH_RADIUS+") and gid <> ? order by dist limit 1";
  		}
  	}
  	
  	String[] nearestEssentialPoiNameOnceMatchStrings=scenario.getNearestEssentialPoiNameOnceMatchStrings();
  	if (nearestEssentialPoiNameOnceMatchStrings!=null && nearestEssentialPoiNameOnceMatchStrings.length!=0 ) {
  		for (int i=0;i<nearestEssentialPoiNameOnceMatchStrings.length;i++) {
  			queryIndex++;
  			String nameOnce= nearestEssentialPoiNameOnceMatchStrings[i];
  			//?s: lon lat lon lat
  			queries[queryIndex]= " select name, astext(the_geom) as location, distance(the_geom, GeomFromText(?, "+SRID+" )) as dist from gnis_names09  where state='TX' and name like '%"+nameOnce+"%' and " +
  			"(distance(the_geom, GeomFromText(?, "+SRID+" ))  <= "+VisitScenario.MAX_SEARCH_RADIUS+") and gid <> ? order by dist limit 1";
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
			queries[queryIndex]= " select name, astext(the_geom) as location, distance(the_geom, GeomFromText(?, "+SRID+" )) as dist from gnis_names09  where state='TX' and ("+nameOrMatchingStrings+") and " +
			"(distance(the_geom, GeomFromText(?, "+SRID+" ))  <= "+VisitScenario.MAX_SEARCH_RADIUS+") and gid <> ? order by dist limit 1";
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
			queries[queryIndex]= " select name, astext(the_geom) as location, distance(the_geom, GeomFromText(?, "+SRID+" )) as dist from gnis_names09  where state='TX' and ("+classOrMatchingStrings+") and " +
			"(distance(the_geom, GeomFromText(?, "+SRID+" ))  <= "+VisitScenario.MAX_SEARCH_RADIUS+") and gid <> ? order by dist limit "+limit;
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
			queries[queryIndex]= " select name, astext(the_geom) as location, distance(the_geom, GeomFromText(?, "+SRID+" )) as dist from gnis_names09  where state='TX' and ("+nameOrMatchingStrings+") and " +
			"(distance(the_geom, GeomFromText(?, "+SRID+" ))  <= "+VisitScenario.MAX_SEARCH_RADIUS+") and gid <> ? order by dist limit "+limit;
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
			queries[queryIndex]= " select name, astext(the_geom) as location, distance(the_geom, GeomFromText(?, "+SRID+" )) as dist from gnis_names09  where state='TX' and ("+classOrMatchingStrings+") and " +
			"(distance(the_geom, GeomFromText(?, "+SRID+" ))  <= "+VisitScenario.MAX_SEARCH_RADIUS+") and gid <> ? order by dist limit 1";
  	}
  	
  	return queries;
  }
  
  public String[] getMapBrowseBoundingBoxQueries() {
  	
  	// 5 queries
  	String[] queries =  new String[5];
  	
  	queries[0] = "SELECT gid,encode(asBinary(force_2d(the_geom),'XDR'),'base64') as the_geom FROM gnis_names09 WHERE the_geom && GeomFromText(?, "+SRID+")";
  	queries[1] = "SELECT gid,encode(asBinary(force_2d(the_geom),'XDR'),'base64') as the_geom FROM arealm_merge WHERE the_geom && GeomFromText(?, "+SRID+")";
  	queries[2] = "SELECT gid,encode(asBinary(force_2d(the_geom),'XDR'),'base64') as the_geom FROM areawater_merge WHERE the_geom && GeomFromText(?, "+SRID+")";
  	queries[3] = "SELECT gid,encode(asBinary(force_2d(the_geom),'XDR'),'base64') as the_geom FROM pointlm_merge WHERE the_geom && GeomFromText(?, "+SRID+")";
  	queries[4] = "SELECT gid,encode(asBinary(force_2d(the_geom),'XDR'),'base64') as the_geom FROM edges_merge WHERE the_geom && GeomFromText(?, "+SRID+")";
  	
  	return queries;
  }
  
  //////////////////////////////////////////MACRO BENCHMARK: LAND USE /////////////////////////////////
  public String[] getLandUseQueries() { 
	  
	//43560 square foot = 1 acre ;  st_area gives area in square feet 
	/* gisdb=> select acreage, shape_area, marketvalu, st_area(pa.the_geom), pa.marketvalu/(st_area(pa.the_geom)/43560) from parcels2008 as pa where pa.land_state like 'A%' limit 2;
	  acreage  |  shape_area   | marketvalu |     st_area      |     ?column?     
			  ----------+---------------+------------+------------------+------------------
			    1.18252 | 51510.6709822 |     120393 | 51510.6709833145 | 101810.342981142
			   0.495791 | 21596.6594178 |     204116 | 21596.6594161987 | 411697.605108826
  	 */
	  
	//6 queries
	String[] queries =  new String[6]; 
	// queries
	
	//What is the average property value per acre for apartment/condos in Travis county  
	//queries[0] = "select sum(pa.marketvalu)/sum(st_area(pa.the_geom)/43560) from parcels2008 as pa, land_use_2006 as lu where lu.parcel_id_=pa.geo_id  and lu.land_use_2=220 and shape_sour='TCAD'";
    
	// rewrite: What is the average property value per sq foot for Single-Family Residential properties
	queries[0] = "select sum(pa.marketvalu)/sum(st_area(pa.the_geom)) from parcels2008 as pa where pa.land_state like 'A%'";
	  
	//How many residential properties have a  hospital within one mile radius in Austin 
	queries[1] = "select count(*) from  land_use_2006 as lu, hospitals as h where lu.general_la in (100,113,150,160,200) and  st_dwithin(lu.the_geom, h.the_geom, 5280)";
				  
	// Determine the average property values  within a one mile radius of all hospitals in Travis county 
	queries[2] = "select sc.name, avg(pa.marketvalu) as avg_property_value from parcels2008 as pa, hospitals as sc where st_dwithin(pa.the_geom, sc.the_geom, 5280) group by sc.name order by avg_property_value desc";

	//Projection not same: Determine the avg property values within a one mile radius of all schools in Travis county 
	//queries[0] = " select sc.name, avg(pa.marketvalu) as avg_property_value from parcels2008 as pa, gnis_names09  as sc where sc.class='School' and st_dwithin(pa.the_geom, sc.the_geom, 5280) group by sc.name order by avg_property_value ";

	
	//Slow query: Waterfront properties: show all the property values sorted by price within a 100 feet of the three major lakes in Travis county
	//queries[3] = "select lake.wtr_nm, geo_id, pa.marketvalu as property_value from parcels2008 as pa, s_wtr_ar as lake where " + 
	//			 " st_dwithin(lake.the_geom, pa.the_geom, 100) order by property_value desc";
   	
	//Rewritten query: Waterfront properties: show all the property values sorted by price within a 100 feet of the three major lakes in Travis county (first 10 matches)
	queries[3] = "select lake.wtr_nm, geo_id, pa.marketvalu as property_value from parcels2008 as pa, s_wtr_ar as lake where  st_dwithin(lake.the_geom, pa.the_geom, 100) and pa.marketvalu> 0  limit 10";
   	
	
	//Rich people: area in acres and property values of large lot single family properties with area greater than 100 acres
	//queries[4] = " select property_i,  ST_Area(lu.the_geom)/43560 as cal_area, pa.marketvalu as property_value from  land_use_2006 as lu, parcels2008 as pa  where lu.general_la=160  and lu.parcel_id_=pa.geo_id and ST_Area(lu.the_geom)/43560>100  order by cal_area desc";
  
	//Which office buildings have front yard parking restrictions 
	queries[4] = "select property_i from  land_use_2006 as lu, frontyard_parking_restrictions fypr where lu.general_la=400  and  ST_Overlaps(fypr.the_geom, lu.the_geom)";
				  
	
	// Is there any land boundary legal issues?  (Return the area of land we have, union dissolves overlaps)
	//queries[6] = " SELECT SUM(ST_Area(the_geom))/1000 As totalwithoverlap_km, ST_Area(ST_Union(the_geom))/1000 As no_overlap_km FROM land_use_2006";

	
	// Show the commercial properties that are built on un-permitted landfills
	queries[5] = "select lu.property_i from  land_use_2006 as lu, landfills lf where lu.general_la=300 and lf.permitted='UNPERMITTED' and ST_Intersects(lf.the_geom,lu.the_geom)";
				  

	///////////////////////////////////// EXTRA ////////////////////////////////////////////////////////////
	//what is the value per square foot 
	//queries[0] = "select sum(marketvalu)/sum(st_area(the_geom)/43560) from parcels2008"; 

	
	//10. Which parcels of land intersect others and with what (use new array_agg in 8.4)
	// --Return the pids of parcels that intersect other parcels

	//queries[5] = "SELECT p.parcel_id_, COUNT(o.parcel_id_) As totinter  FROM land_use_2006 As p " +
	//	" INNER JOIN land_use_2006 As O ON (p.parcel_id_ <> o.parcel_id_ AND ST_Intersects(p.the_geom, o.the_geom))  GROUP BY p.parcel_id_ ORDER BY p.parcel_id_";
  
	 //3. Determine the property value within a one mile radius for all schools in Travis county 
    //(projection different)
	//queries[1] = "select sc.campname, sum(pa.marketvalu)/sum(st_area(pa.the_geom)/43560) as avg_property_value from parcels2008 as pa, schools as sc where sc.cntyname like 'TRAVIS%' and st_within(pa.the_geom, sc.the_geom, 5280) " +
	//		"group by sc.campname order by avg_property_value desc";

	//5.  Determine the property value within a one mile radius for all parks in Austin 
	//queries[3] = "select sc.park_name, sum(pa.marketvalu)/sum(st_area(pa.the_geom)/43560) as avg_property_value from parcels2008 as pa, coa_parks as sc where " + 
	//			 " st_dwithin(pa.the_geom, sc.the_geom, 5280)   group by sc.park_name  order by avg_property_value desc";
    //
	// ERROR: pt_in_ring_2d: V[n] != V[0] (3.12691e+06 1.01175e+07 != 3.12676e+06 1.01175e+07)
	
	
	return queries;
  }
  
//////////////////////////////////////////MACRO BENCHMARK: ENVIRONMENTAL HAZARD /////////////////////////////////
  public String[] getEnvHazardQueries() { 
	// 5 queries
	String[] queries =  new String[4]; 
	
	//Ok - Show the flood risk areas that are protected by one or more dams.
	queries[0] = "select distinct fld_ar_id from s_gen_struct st, s_fld_haz_ar fa where st.struct_typ='DAM' and ST_Intersects(st.the_geom, fa.the_geom) ";
				  
	//Ok - Show the total flood risk area in acres  grouped by risk area category
	queries[1] = "select fld_zone, sum(st_area(s_fld_haz_ar.the_geom)/43560) as area from s_fld_haz_ar group by fld_zone ";
				  	
	//rewritten; wait:  Show the total high risk flood risk area in acres  within one mile of Lake Travis.
	//queries[2] = "select   sum(st_area(fa.the_geom)/43560) as hr_flood_area from s_fld_haz_ar fa, s_wtr_ar wa where wa.wtr_nm='Lake Travis' and (fld_zone='A' or fld_zone='AE' or fld_zone='AO' or fld_zone='V') and st_dwithin(fa.the_geom, wa.the_geom, 5280) ";
	//queries[2] = "select fld_ar_id, st_area(fa.the_geom)/43560 as flood_area from s_fld_haz_ar fa, s_wtr_ar wa where wa.wtr_nm='Lake Travis' and (fld_zone='A' or fld_zone='AE' or fld_zone='AO' or fld_zone='V') and st_dwithin(fa.the_geom, wa.the_geom, 5280)  limit 1 ";
	
	//rewritten; ok:  Owners of buildings located within an A or V Zone are required to carry flood insurance. 
	// Which residential property owners are requires to carry flood insurance?
	// queries[3] = "select lu.property_i from  land_use_2006 as lu,  s_fld_haz_ar fz where lu.general_la in (100,113,150,160,200) and (fld_zone='A' or fld_zone='AE' or fld_zone='AO' or fld_zone='V') and st_overlaps(lu.the_geom, fz.the_geom)";
	queries[2] = "select lu.property_i from  land_use_2006 as lu,  s_fld_haz_ar fz where lu.general_la in (100,113,150,160,200) and (fld_zone='A' or fld_zone='AE' or fld_zone='AO' or fld_zone='V') and st_overlaps(lu.the_geom, fz.the_geom) limit 10 ";
	
	//rewritten; wait: Which industrial complexes are in high risk flood area?
	//queries[] = "select lu.property_i from  land_use_2006 as lu,  s_fld_haz_ar fz where lu.general_la=500 and (fld_zone='A' or fld_zone='AE' or fld_zone='AO' or fld_zone='V') and st_overlaps(lu.the_geom, fz.the_geom)";
	 queries[3] = "select lu.property_i from  land_use_2006 as lu,  s_fld_haz_ar fz where lu.general_la=500 and (fld_zone='A' or fld_zone='AE' or fld_zone='AO' or fld_zone='V') and st_overlaps(lu.the_geom, fz.the_geom) limit 10 ";
	

	return queries;
  }
}