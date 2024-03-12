package com.Network.Network.DevicemetamodelPojo;

public class StateDto {

	private Long id;
	private String stateName;
	private String countryName; // We're only using the country's name here, not the whole entity
	private String notes;

	// Default constructor
	public StateDto() {
	}

	// Constructor to initialize all fields
	public StateDto(Long id, String stateName, String countryName, String notes) {
		this.id = id;
		this.stateName = stateName;
		this.countryName = countryName;
		this.notes = notes;
	}

	// Getters and Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		return "StateDto [id=" + id + ", stateName=" + stateName + ", countryName=" + countryName + ", notes=" + notes
				+ "]";
	}}