package com.Network.Network.DevicemetamodelRepo;

import java.util.List;
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

    @Query(value = "SELECT o FROM orders o WHERE o.id BETWEEN :startId AND :endId", nativeQuery = true)
    List<Order> findOrdersInRange(Long startId, Long endId);

    @Query(value = "SELECT o.id AS id, " +
            "COUNT(DISTINCT d.id) AS deviceCount, " +
            "COUNT(DISTINCT c2.cardid) AS cardCount, " +
            "COUNT(DISTINCT sl.portid) AS portCount, " +
            "COUNT(DISTINCT c.id) AS pluggableCount, " +
            "COUNT(DISTINCT p.logicalportid) AS logicalPortCount, " +
            "COUNT(DISTINCT s.id) AS serviceCount " +
            "FROM orders o " +
            "LEFT JOIN device d ON o.id = d.order_id  " +
            "LEFT JOIN Card c2 ON o.id = c2.order_id  " +
            "LEFT JOIN port sl ON o.id = sl.order_id  " +
            "LEFT JOIN pluggable c ON o.id = c.order_id  " +
            "LEFT JOIN logical_port p ON o.id = p.order_id  " +
            "LEFT JOIN service s ON o.id = s.order_id  " +
            "WHERE o.id = :orderId  " +
            "GROUP BY o.id", nativeQuery = true)
    List<Object[]> getOrderStatistics(@Param("orderId") Long orderId);
}
