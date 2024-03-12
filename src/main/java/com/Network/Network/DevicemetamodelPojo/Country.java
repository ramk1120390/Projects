package com.Network.Network.DevicemetamodelPojo;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
public class Country implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "countryName", unique = true, nullable = false)
	private String countryName;
	
	@Column(name = "notes")
	private String notes;

	public Country() {
		super();
	}

	

	


	public Country(Long id, String countryName, String notes) {
		super();
		this.id = id;
		this.countryName = countryName;
		
		this.notes = notes;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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



	
	
}