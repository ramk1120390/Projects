package com.Network.Network.DevicemetamodelPojo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class DeviceModel {
	@Id
	private String devicemodel;
	

	public DeviceModel() {
		super();
	}


	public DeviceModel(String devicemodel) {
		super();
		this.devicemodel = devicemodel;
	}


	public String getDevicemodel() {
		return devicemodel;
	}


	public void setDevicemodel(String devicemodel) {
		this.devicemodel = devicemodel;
	}
	

}
