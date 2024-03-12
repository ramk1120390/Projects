package com.Network.Network.DevicemetamodelRepo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Network.Network.DevicemetamodelPojo.Country;
import com.Network.Network.DevicemetamodelPojo.Order;

public interface OrderRepo extends JpaRepository<Order, Long> {
	Optional<Order> findById(Long id);
	@Modifying
	@Query(value = "DELETE FROM orders WHERE id = :orderId", nativeQuery = true)
	void deleteByOrderId(@Param("orderId") Long orderId);
}
