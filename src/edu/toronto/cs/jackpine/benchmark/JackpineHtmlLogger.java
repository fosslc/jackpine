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
 * Original developer(s): Robert Hodges and Ralph Hannus.
 * Contributor(s):
 */


package  edu.toronto.cs.jackpine.benchmark;

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.continuent.bristlecone.benchmark.impl.Config;
import com.continuent.bristlecone.benchmark.impl.ConfigMetadata;
import com.continuent.bristlecone.benchmark.impl.ConfigPropertyMetadata;


import com.continuent.bristlecone.benchmark.HtmlLogger;

/**
 * Implements a logger that prints benchmark results in a nice HTML format. 
 * 
 */
public class JackpineHtmlLogger extends HtmlLogger
{
  private static Logger logger = Logger.getLogger(JackpineHtmlLogger.class);  

  
  /** Creates a new logger. */
  public JackpineHtmlLogger(String outputFileName)
  {
    super(outputFileName);
  }

  
  /* (non-Javadoc)
   * @see com.continuent.bristlecone.benchmark.BenchmarkResultLogger#resultGenerated(com.continuent.bristlecone.benchmark.Tuple)
   */
  public void resultGenerated(Config config)
  {
    ConfigMetadata metadata = config.getMetadata();

    // Print header with fixed fields the first time through. 
    if (! printedHeader)
    {
      htmlOut.println("<html>");
      htmlOut.println("<head>");
      htmlOut.println("<title> Jackpine Benchmark Report</title>");
      htmlOut.println("<style type=\"text/css\">");
      htmlOut.println(STYLES);
      htmlOut.println("</style>");
      htmlOut.println("</head>");
      htmlOut.println("<body>");
      htmlOut.println("<h1>Jackpine Benchmark Report</h1>");
      
      // Write Values for fixed fields.
      htmlOut.println("<h2>Fixed Benchmark Configuration Values</h2>");
      htmlOut.println("<table class=\"details\" border=\"0\" cellpadding=\"5\" cellspacing=\"2\" width=\"95%\">");
      htmlOut.println("<tr>");
      htmlOut.println("<th align=\"left\">Name</th><th>Value</th>");
      htmlOut.println("</tr>");
      
      Iterator<String> iter = metadata.propertyNames(); 
      while (iter.hasNext())
      {
    	// SKIP datarows,datatype,datawidth,password,url,   
        String name = iter.next();
        String value = config.getProperty(name);
        
        if (name.equals("datarows") || name.equals("datatype") || name.equals("datawidth") || name.equals("password") || name.equals("url") || name.equals("include")) 
        	continue;
        
        // scenario - needs to trim the name
        if (name.equals("scenario")) {
        	int len = (new String("com.continuent.bristlecone.benchmark.scenarios.")).length();
        	value = value.substring(len);
        }
        
        if (name.equals("include")) {
        	System.out.println(value); 
        }
        
        
        ConfigPropertyMetadata cpm = metadata.getPropertyMetadataAsserted(name);
        if (! cpm.isVariable() && ! cpm.isOutput())
        {
          htmlOut.println("<tr>");
          htmlOut.println("<td>" + name + "</td><td>" + value + "</td>");
          htmlOut.println("</tr>");
        }
      }
      htmlOut.println("</table>");

      // Print header row for remaining properties. 
      htmlOut.println("<h2>Benchmark Runs</h2>");
      htmlOut.println("<table class=\"details\" border=\"0\" cellpadding=\"5\" cellspacing=\"2\" width=\"95%\">");
      htmlOut.println("<tr>");

      iter = metadata.propertyNames();
      while (iter.hasNext())
      {
        String name = iter.next();
        if (name.equals("datarows") || name.equals("datatype") || name.equals("datawidth") || name.equals("password") || name.equals("url") || name.equals("include")) 
        	continue;
        
        ConfigPropertyMetadata cpm = metadata.getPropertyMetadataAsserted(name);
        if (cpm.isVariable() || cpm.isOutput())
        {
          htmlOut.println("<th align=\"left\">" + name + "</th>");
        }
      }
      htmlOut.println("</tr>");
      
      printedHeader = true;
    }
    
    // Write data for non-fixed or output fields.   
    htmlOut.println("<tr>");
    Iterator<String> iter = metadata.propertyNames();
    while (iter.hasNext())
    {
      String name = iter.next();
      if (name.equals("datarows") || name.equals("datatype") || name.equals("datawidth") || name.equals("password") || name.equals("url") || name.equals("include")) 
      	continue;
      
      ConfigPropertyMetadata cpm = metadata.getPropertyMetadataAsserted(name);
      if (cpm.isVariable() || cpm.isOutput())
      {
        htmlOut.println("<td>" + config.getProperty(name) + "</td>");
      }
    }
    htmlOut.println("</tr>");
  }
  
}