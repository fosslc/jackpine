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

package  edu.toronto.cs.jackpine.benchmark;

import org.apache.log4j.Logger;

import com.continuent.bristlecone.benchmark.Benchmark;
import com.continuent.bristlecone.benchmark.BenchmarkException;
import com.continuent.bristlecone.benchmark.ResultLogger;

/**
 * 
 */
public class JackpineBenchmark extends Benchmark
{
    static {
    	logger = Logger.getLogger(JackpineBenchmark.class);
    }
    
	protected void addLoggers() {
		// Add loggers as specified by client.  
	    /*
		if (textOutputFile != null)
	      addLogger(new TextLogger(textOutputFile));
	    if (this.csvOutputFile != null)
	      addLogger(new CsvLogger(csvOutputFile));
	    */
	    if (this.htmlOutputFile != null)
	      addLogger(new JackpineHtmlLogger(htmlOutputFile));
	    
	}
	 
}