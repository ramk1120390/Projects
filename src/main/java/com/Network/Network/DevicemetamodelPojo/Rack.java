package com.Network.Network.DevicemetamodelPojo;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
public class Rack implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "rackName", nullable = false)
	private String rackName;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "buildingName", referencedColumnName = "buildingName")
	private Building building;
	@Column(columnDefinition = "varchar(50) default 'Building_To_Rack'")
	private String notes;

	public Rack() {
		super();
	}

	public Rack(Long id, String rackName, String notes) {
		super();
		this.id = id;
		this.rackName = rackName;
		this.notes = notes;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRackName() {
		return rackName;
	}

	public void setRackName(String rackName) {
		this.rackName = rackName;
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		return "Rack [id=" + id + ", rackName=" + rackName + ", building=" + building + ", notes=" + notes + "]";
	}

}