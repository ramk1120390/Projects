package com.Network.Network.DevicemetamodelPojo;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "DeviceMetaModel")
public class DeviceMetaModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="deviceModel",unique = true,nullable = false)
    private String deviceModel;

    @Column(name="partNumber")
    private String partNumber;

    @Column(name="vendor")
    private String vendor;

    @Column(name="height")
    private float height;

    @Column(name="depth")
    private float depth;

    @Column(name="width")
    private float width;

    @Column(name="shelvesContained")
    private int shelvesContained;


    @Column(name="numOfRackPositionOccupied")
    private int numOfRackPositionOccupied;

    @Column(name = "allowed_card_list")
    private List<String> allowedCardList;

	public DeviceMetaModel(Long id, String deviceModel, String partNumber, String vendor, float height, float depth,
			float width, int shelvesContained, int numOfRackPositionOccupied, ArrayList<String> allowedCardList) {
		super();
		this.id = id;
		this.deviceModel = deviceModel;
		this.partNumber = partNumber;
		this.vendor = vendor;
		this.height = height;
		this.depth = depth;
		this.width = width;
		this.shelvesContained = shelvesContained;
		this.numOfRackPositionOccupied = numOfRackPositionOccupied;
		this.allowedCardList = allowedCardList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getDepth() {
		return depth;
	}

	public void setDepth(float depth) {
		this.depth = depth;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public int getShelvesContained() {
		return shelvesContained;
	}

	public void setShelvesContained(int shelvesContained) {
		this.shelvesContained = shelvesContained;
	}

	public int getNumOfRackPositionOccupied() {
		return numOfRackPositionOccupied;
	}

	public void setNumOfRackPositionOccupied(int numOfRackPositionOccupied) {
		this.numOfRackPositionOccupied = numOfRackPositionOccupied;
	}

	public List<String> getAllowedCardList() {
		return allowedCardList;
	}

	public void setAllowedCardList(ArrayList<String> allowedCardList) {
		this.allowedCardList = allowedCardList;
	}

	public DeviceMetaModel() {
		super();
	}

	@Override
	public String toString() {
		return "DeviceMetaModel [id=" + id + ", deviceModel=" + deviceModel + ", partNumber=" + partNumber + ", vendor="
				+ vendor + ", height=" + height + ", depth=" + depth + ", width=" + width + ", shelvesContained="
				+ shelvesContained + ", numOfRackPositionOccupied=" + numOfRackPositionOccupied + ", allowedCardList="
				+ allowedCardList + "]";
	}
    
    

   

}