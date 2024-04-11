package com.Network.Network.DevicemetamodelPojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;

@Entity
public class Device implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    @Column(unique = true,nullable = false)
	private String devicename;

	private String deviceModel;

	private String location; 

	private String organisation;

	private String customer; //TODO using order finding with passing set customer name

	private String managementIp;

	private String rackPosition;

	private String operationalState;

	private String administrativeState;

	private String usageState;

	private String serialNumber;

	private String href;

	private String credentials;

	private String accessKey;
	
	private String pollInterval;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "buildingName", referencedColumnName = "buildingName")
	private Building building;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", referencedColumnName = "id")
	private Order order;
	
	@Column
	private String realtion;
	
	

	public Device() {
		super();
	}

	

	public Device(Long id, String devicename, String deviceModel, String location, String organisation, String customer,
			String managementIp, String rackPosition, String operationalState, String administrativeState,
			String usageState, String serialNumber, String href, String credentials, String accessKey,
			String pollInterval, Building building, Order order, String realtion) {
		super();
		this.id = id;
		this.devicename = devicename;
		this.deviceModel = deviceModel;
		this.location = location;
		this.organisation = organisation;
		this.customer = customer;
		this.managementIp = managementIp;
		this.rackPosition = rackPosition;
		this.operationalState = operationalState;
		this.administrativeState = administrativeState;
		this.usageState = usageState;
		this.serialNumber = serialNumber;
		this.href = href;
		this.credentials = credentials;
		this.accessKey = accessKey;
		this.pollInterval = pollInterval;
		this.building = building;
		this.order = order;
		this.realtion = realtion;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	public String getDevicename() {
		return devicename;
	}

	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getManagementIp() {
		return managementIp;
	}

	public void setManagementIp(String managementIp) {
		this.managementIp = managementIp;
	}

	public String getRackPosition() {
		return rackPosition;
	}

	public void setRackPosition(String rackPosition) {
		this.rackPosition = rackPosition;
	}

	public String getOperationalState() {
		return operationalState;
	}

	public void setOperationalState(String operationalState) {
		this.operationalState = operationalState;
	}

	public String getAdministrativeState() {
		return administrativeState;
	}

	public void setAdministrativeState(String administrativeState) {
		this.administrativeState = administrativeState;
	}

	public String getUsageState() {
		return usageState;
	}

	public void setUsageState(String usageState) {
		this.usageState = usageState;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getPollInterval() {
		return pollInterval;
	}

	public void setPollInterval(String pollInterval) {
		this.pollInterval = pollInterval;
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}



	public String getRealtion() {
		return realtion;
	}



	public void setRealtion(String realtion) {
		this.realtion = realtion;
	}
    

}
