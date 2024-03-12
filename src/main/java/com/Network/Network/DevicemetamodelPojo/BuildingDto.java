package com.Network.Network.DevicemetamodelPojo;

public class BuildingDto {
	private Long id;
	private String buildingName;
	private String cityName; // This will be used to relate the building with a specific city.
	private String notes;

	private String clliCode;

	private String phoneNumber;

	private String contactPerson;

	private String address;

	private String latitude;

	private String longitude;

	private String drivingInstructions;

	private String href;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getClliCode() {
		return clliCode;
	}

	public void setClliCode(String clliCode) {
		this.clliCode = clliCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getDrivingInstructions() {
		return drivingInstructions;
	}

	public void setDrivingInstructions(String drivingInstructions) {
		this.drivingInstructions = drivingInstructions;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public BuildingDto(Long id, String buildingName, String cityName, String notes, String clliCode, String phoneNumber,
			String contactPerson, String address, String latitude, String longitude, String drivingInstructions,
			String href) {
		super();
		this.id = id;
		this.buildingName = buildingName;
		this.cityName = cityName;
		this.notes = notes;
		this.clliCode = clliCode;
		this.phoneNumber = phoneNumber;
		this.contactPerson = contactPerson;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.drivingInstructions = drivingInstructions;
		this.href = href;
	}

	public BuildingDto() {
		super();
	}
	

}
