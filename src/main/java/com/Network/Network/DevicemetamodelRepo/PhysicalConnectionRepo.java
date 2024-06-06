package com.Network.Network.DevicemetamodelRepo;

import com.Network.Network.DevicemetamodelPojo.PhysicalConnection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PhysicalConnectionRepo extends JpaRepository<PhysicalConnection, Long> {
    PhysicalConnection findByName(String name);

    @Query(value = "CALL CreatePhysicalConnection(:p_name, :p_devicea, :p_devicez, :p_deviceaport, :p_devicezport, :p_connectionType, :p_bandwidth, :p_portnamea, :p_portnameb, :keys, :p_values, :success)", nativeQuery = true)
    int createPhysicalConnection(
            @Param("p_name") String p_name,
            @Param("p_devicea") String p_devicea,
            @Param("p_devicez") String p_devicez,
            @Param("p_deviceaport") String p_deviceaport,
            @Param("p_devicezport") String p_devicezport,
            @Param("p_connectionType") String p_connectionType,
            @Param("p_bandwidth") Integer p_bandwidth,
            @Param("p_portnamea") String p_portnamea,
            @Param("p_portnameb") String p_portnameb,
            @Param("keys") String[] keys,
            @Param("p_values") String[] p_values,
            @Param("success") Integer success
    );

    @Query(value = "SELECT CONCAT(pc.devicea, ',', pc.deviceb) FROM physical_connection pc WHERE pc.name = :name", nativeQuery = true)
    String findConcatenatedDevicesByName(@Param("name") String name);

    @Transactional
    void deleteByPhysicalconnection_id(Long id);
}
