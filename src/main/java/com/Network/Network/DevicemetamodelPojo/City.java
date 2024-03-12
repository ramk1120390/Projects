package com.Network.Network.DevicemetamodelPojo;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
public class City implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "cityName", unique = true, nullable = false)
	private String cityName;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stateName", referencedColumnName = "stateName")
	private State state;
	@Column(columnDefinition = "varchar(50) default 'State_To_City'")
	private String notes;


	public City() {
		super();
	}

	

	


	public City(Long id, String cityName, State state, String notes) {
		super();
		this.id = id;
		this.cityName = cityName;
		this.state = state;
		this.notes = notes;
		
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		return "City [id=" + id + ", cityName=" + cityName + ", state=" + state + ", notes=" + notes + "]";
	}

}