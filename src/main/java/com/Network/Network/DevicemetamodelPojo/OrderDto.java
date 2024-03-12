package com.Network.Network.DevicemetamodelPojo;

import java.time.LocalDate;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


public class OrderDto {

	private Long id;

	private String status;
	private String category;
	private String description;
	private LocalDate updatedDate;
	private LocalDate createdDate;
	private String previousStatus;
	private String customer;
	
	public OrderDto() {
		super();
	}
	public OrderDto(Long id, String status, String category, String description, LocalDate updatedDate,
			LocalDate createdDate, String previousStatus, String customer) {
		super();
		this.id = id;
		this.status = status;
		this.category = category;
		this.description = description;
		this.updatedDate = updatedDate;
		this.createdDate = createdDate;
		this.previousStatus = previousStatus;
		this.customer = customer;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public LocalDate getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(LocalDate updatedDate) {
		this.updatedDate = updatedDate;
	}
	public LocalDate getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}
	public String getPreviousStatus() {
		return previousStatus;
	}
	public void setPreviousStatus(String previousStatus) {
		this.previousStatus = previousStatus;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	

}
