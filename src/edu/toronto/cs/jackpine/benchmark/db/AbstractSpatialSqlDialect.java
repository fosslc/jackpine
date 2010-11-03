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

import com.continuent.bristlecone.benchmark.db.AbstractSqlDialect;


/**
 * 
 * @author sray 
 */
public abstract class AbstractSpatialSqlDialect extends AbstractSqlDialect implements SpatialSqlDialect 
{
	 SupportedSqlDialect sqlDialectType;

	
  
   
}
