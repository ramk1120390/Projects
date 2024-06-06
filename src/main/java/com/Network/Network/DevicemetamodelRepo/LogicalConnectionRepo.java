package com.Network.Network.DevicemetamodelRepo;

import com.Network.Network.DevicemetamodelPojo.LogicalConnection;
import com.Network.Network.DevicemetamodelPojo.LogicalPort;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LogicalConnectionRepo extends JpaRepository<LogicalConnection, Long> {
    LogicalConnection findByName(String name);
    @Query(value = "CALL CreateLogicalConnection(:p_name, :p_devicea, :p_devicez, :p_deviceaport, :p_devicezport, :p_connectionType, :p_bandwidth, :p_devicesConnected, :p_physicalconnection, :p_logicalportnamea, :p_logicalportnameb, :keys, :p_values, :success)", nativeQuery = true)
    int createLogicalConnection(
            @Param("p_name") String name,
            @Param("p_devicea") String deviceA,
            @Param("p_devicez") String deviceZ,
            @Param("p_deviceaport") String deviceAPort,
            @Param("p_devicezport") String deviceZPort,
            @Param("p_connectionType") String connectionType,
            @Param("p_bandwidth") int bandwidth,
            @Param("p_devicesConnected") String[] devicesConnected,
            @Param("p_physicalconnection") String[] physicalConnections,
            @Param("p_logicalportnamea") String logicalPortNameA,
            @Param("p_logicalportnameb") String logicalPortNameB,
            @Param("keys") String[] keys,
            @Param("p_values") String[] values,
            @Param("success") int success);
    @Transactional
    void deleteByLogicalconnection_id(Long id);
}



