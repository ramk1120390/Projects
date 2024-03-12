package com.Network.Network.DevicemetamodelPojo;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
public class Building implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "buildingName", unique = true, nullable = false)
	private String buildingName;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cityName", referencedColumnName = "cityName")
	private City city;
	@Column(columnDefinition = "varchar(50) default 'City_To_Building'")
	private String notes;

	private String clliCode;

	private String phoneNumber;

	private String contactPerson;

	private String address;

	private String latitude;

	private String longitude;

	private String drivingInstructions;

	private String href;

	public Building() {
		super();
	}

	public Building(Long id, String buildingName, City city, String notes, String clliCode, String phoneNumber,
			String contactPerson, String address, String latitude, String longitude, String drivingInstructions,
			String href) {
		super();
		this.id = id;
		this.buildingName = buildingName;
		this.city = city;
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

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
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

	@Override
	public String toString() {
		return "Building [id=" + id + ", buildingName=" + buildingName + ", city=" + city + ", notes=" + notes
				+ ", clliCode=" + clliCode + ", phoneNumber=" + phoneNumber + ", contactPerson=" + contactPerson
				+ ", address=" + address + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", drivingInstructions=" + drivingInstructions + ", href=" + href + "]";
	}

	

}