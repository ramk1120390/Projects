package com.Network.Network.DevicemetamodelPojo;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
@Data
@Entity
@Table(name = "orders")
public class Order implements Serializable {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String status;
	private String category;
	private String description;
	private LocalDate updatedDate;
	private LocalDate createdDate;
	private String previousStatus;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customername", referencedColumnName = "customername")
	private Customer customer;
	
	public Order() {
		super();
	}
	public Order(Long id, String status, String category, String description, LocalDate updatedDate,
			LocalDate createdDate, String previousStatus, Customer customer) {
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
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}
