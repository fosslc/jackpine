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

import java.util.Random;

public class AddressPicker {

	int roadNumber=0;
	String roadName="";
	//String roadType="";
	String zipcode="";
	Random rnd= null;
	
	static final int TOTAL_ADDRESSES=50;
	static final AddressPicker[] addresses= {
		new AddressPicker(1743,"SOUTH","19146"),
		new AddressPicker(1743,"SOUTH","19146"),
		new AddressPicker(8128, "Rowland", "19136"),
		new AddressPicker(773, "N Pallas", "19104"),
		new AddressPicker(262, "E Cliveden", "19119"),
		new AddressPicker(7010, "Keystone", "19135"),
		new AddressPicker(1300, "Lindley", "19141"),
		new AddressPicker(6009, "Bingham", "19111"),
		new AddressPicker(2131, "Christian", "19146"),
		new AddressPicker(1311, "Arrott", "19124"),
		new AddressPicker(5008, "Chestnut", "19139"),
		new AddressPicker(5338, "Darrah", "19124"),
		new AddressPicker(532, "Turner", "19122"),
		new AddressPicker(5457, "Wayne", "19144"),
		new AddressPicker(5912, "Washington", "19143"),
		
		new AddressPicker(7010, "Keystone", "19135"),
		new AddressPicker(1300, "Lindley", "19141"),
		new AddressPicker(6009, "Bingham", "19111"),
		new AddressPicker(2131, "Christian", "19146"),
		new AddressPicker(1743,"SOUTH","19146"),
		new AddressPicker(1743,"SOUTH","19146"),
		new AddressPicker(5008, "Chestnut", "19139"),
		new AddressPicker(5338, "Darrah", "19124"),
		new AddressPicker(532, "Turner", "19122"),
		new AddressPicker(5457, "Wayne", "19144"),
		new AddressPicker(5912, "Washington", "19143"),
		new AddressPicker(8128, "Rowland", "19136"),
		new AddressPicker(773, "N Pallas", "19104"),
		new AddressPicker(262, "E Cliveden", "19119"),
		new AddressPicker(1311, "Arrott", "19124"),
		
		new AddressPicker(7010, "Keystone", "19135"),
		new AddressPicker(1300, "Lindley", "19141"),
		new AddressPicker(6009, "Bingham", "19111"),
		new AddressPicker(2131, "Christian", "19146"),
		new AddressPicker(1743,"SOUTH","19146"),
		new AddressPicker(5912, "Washington", "19143"),
		new AddressPicker(8128, "Rowland", "19136"),
		new AddressPicker(773, "N Pallas", "19104"),
		new AddressPicker(262, "E Cliveden", "19119"),
		new AddressPicker(1311, "Arrott", "19124"),
		new AddressPicker(1743,"SOUTH","19146"),
		new AddressPicker(5008, "Chestnut", "19139"),
		new AddressPicker(5338, "Darrah", "19124"),
		new AddressPicker(532, "Turner", "19122"),
		new AddressPicker(5457, "Wayne", "19144"),
		
		new AddressPicker(773, "N Pallas", "19104"),
		new AddressPicker(262, "E Cliveden", "19119"),
		new AddressPicker(1311, "Arrott", "19124"),
		new AddressPicker(1743,"SOUTH","19146"),
		new AddressPicker(5008, "Chestnut", "19139"),
		
	};
	
	
	AddressPicker(int roadNumber,String roadName, String zipcode) {
		this.roadNumber=roadNumber;
		this.roadName=roadName;
		//this.roadType=roadType;
		this.zipcode=zipcode;
		
	}
	 
	public AddressPicker() {
		rnd = new Random(System.currentTimeMillis());
		
	}
	
	public int getTotalAddresses() {
		return TOTAL_ADDRESSES;
	}
	
	public AddressPicker[] getAllAddresses() {
		return addresses;
	}
	
	
	public AddressPicker nextAddress() {
		return addresses[rnd.nextInt(TOTAL_ADDRESSES)];
	}
	
	public int getRoadNumber() {
		return roadNumber;
	}
	
	public String getRoadName() {
		return roadName.toUpperCase();
	}
	
	public String getZipcode() {
		return zipcode;
	}
}
