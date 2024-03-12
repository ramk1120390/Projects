package com.Network.Network.DevicemetamodelPojo;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
public class State implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "stateName", unique = true, nullable = false)
	private String stateName;

	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	@JoinColumn(name = "countryName", referencedColumnName = "countryName")
	private Country country;
	@Column(columnDefinition = "varchar(50) default 'Country_To_State'")
    private String notes;
	 

	public State() {
		super();
	}

	

	



	public State(Long id, String stateName, Country country, String notes) {
		super();
		this.id = id;
		this.stateName = stateName;
		this.country = country;
		this.notes = notes;
		
	}



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

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		return "State [id=" + id + ", stateName=" + stateName + ", country=" + country + ", notes=" + notes + "]";
	}
	



	

}