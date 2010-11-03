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

package edu.toronto.cs.jackpine.benchmark.scenarios.macroscenario;

public class ZoomLevel {
	final double[] ScaleDenominators = {313562.9883854061,158544.31983237443,79321.27086307846,
			   39599.27150781185,19772.040748579664,9874.52936126395,4935.5416457795045,
			   2468.2015645037545,1234.10078225187725,617.050391125938625}; 

	double getScaleDenominators(int zoomLevel) {
		if (zoomLevel>10 || zoomLevel<1)
			return -1;
		else
			return ScaleDenominators[zoomLevel-1];
	}
}
