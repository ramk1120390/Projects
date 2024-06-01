package com.Network.Network.DevicemetamodelPojo;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.ArrayList;

public class DeviceDto {

	private Long id;

	private String devicename;

	private String deviceModel;

	private String location;

	private String organisation;

	private String customer; // TODO using order finding with passing set customer name

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

	private String buildingname;

	private Long orderid;
	private String realtion;
	private ArrayList<AdditionalAttribute> additionalAttributes;

	public DeviceDto(Long id, String devicename, String deviceModel, String location, String organisation, String customer, String managementIp, String rackPosition, String operationalState, String administrativeState, String usageState, String serialNumber, String href, String credentials, String accessKey, String pollInterval, String buildingname, Long orderid, String realtion, ArrayList<AdditionalAttribute> additionalAttributes) {
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
		this.buildingname = buildingname;
		this.orderid = orderid;
		this.realtion = realtion;
		this.additionalAttributes = additionalAttributes;
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

	public String getBuildingname() {
		return buildingname;
	}

	public void setBuildingname(String buildingname) {
		this.buildingname = buildingname;
	}

	public Long getOrderid() {
		return orderid;
	}

	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}

	public String getRealtion() {
		return realtion;
	}

	public void setRealtion(String realtion) {
		this.realtion = realtion;
	}

	public ArrayList<AdditionalAttribute> getAdditionalAttributes() {
		return additionalAttributes;
	}

	public void setAdditionalAttributes(ArrayList<AdditionalAttribute> additionalAttributes) {
		this.additionalAttributes = additionalAttributes;
	}

	public DeviceDto() {
	}
}
