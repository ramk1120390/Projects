package com.Network.Network.DevicemetamodelRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Network.Network.DevicemetamodelPojo.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
	 Customer findByCustomername(String customerName);
	    Customer findByEmail(String email);
	    Customer findByContactNo(String contactNo);
	    @Query(value = "DELETE FROM customer WHERE customername = :customerName", nativeQuery = true)
	    void deleteByCustomerName(@Param("customerName") String customerName); // Corrected parameter name
}