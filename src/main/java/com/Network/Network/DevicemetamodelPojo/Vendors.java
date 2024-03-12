package com.Network.Network.DevicemetamodelPojo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Vendors {
	@Id
	private String vendorName;

	public Vendors(String vendorName) {
		super();
		this.vendorName = vendorName;
	}

	public Vendors() {
		super();
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	@Override
	public String toString() {
		return "Vendors [vendorName=" + vendorName + "]";
	}
	

}
