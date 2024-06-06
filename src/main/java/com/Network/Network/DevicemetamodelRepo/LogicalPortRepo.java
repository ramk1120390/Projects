package com.Network.Network.DevicemetamodelRepo;

import com.Network.Network.DevicemetamodelPojo.LogicalPort;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LogicalPortRepo extends JpaRepository<LogicalPort, Long> {
    LogicalPort findByLogicalportid(Long logicalportid);


    @Query(value = "SELECT * FROM logical_port lp " +
            "WHERE lp.plugableid = :plugableId " +
            "AND lp.position_on_port = :positionOnPort " +
            "AND lp.devicename = :devicename", nativeQuery = true)
    LogicalPort findLogicalPortByPlugableIdAndPositionOnPortAndDeviceName(
            @Param("plugableId") Long plugableId,
            @Param("positionOnPort") Integer positionOnPort,
            @Param("devicename") String devicename);

    @Query(value = "SELECT * FROM logical_port lp " +
            "WHERE lp.portid = :portId " +
            "AND lp.position_on_port = :positionOnPort " +
            "AND lp.devicename = :devicename ", nativeQuery = true)
    LogicalPort findLogicalPortByPortIdAndPositionOnPortAndDeviceName(
            @Param("portId") Long portId,
            @Param("positionOnPort") Integer positionOnPort,
            @Param("devicename") String devicename);


    @Query(value = "SELECT * FROM logical_port lp " +
            "WHERE lp.position_on_port = :positionOnPort " +
            "AND lp.devicename = :devicename", nativeQuery = true)
    LogicalPort findLogicalPortByPositionvalidate(
            @Param("positionOnPort") Integer positionOnPort,
            @Param("devicename") String devicename);


    @Query(value = "CALL insert_logical_portoncard(:p_logicalportName, :p_positionOnCard, :p_positionOnDevice, "
            + ":p_portType, :p_OperationalState, :p_AdministrativeState, :p_UsageState, :p_Href, :p_PortSpeed, "
            + ":p_Capacity, :p_PositionOnPort, :p_ManagementIP, :p_DeviceName, :p_OrderId, :p_PlugableId, "
            + ":p_PortId, :success, :keys, :p_values)", nativeQuery = true)
    int insertLogicalPortOnCard(@Param("p_logicalportName") String p_logicalportName,
                                @Param("p_positionOnCard") int p_positionOnCard,
                                @Param("p_positionOnDevice") int p_positionOnDevice,
                                @Param("p_portType") String p_portType,
                                @Param("p_OperationalState") String p_OperationalState,
                                @Param("p_AdministrativeState") String p_AdministrativeState,
                                @Param("p_UsageState") String p_UsageState,
                                @Param("p_Href") String p_Href,
                                @Param("p_PortSpeed") String p_PortSpeed,
                                @Param("p_Capacity") int p_Capacity,
                                @Param("p_PositionOnPort") int p_PositionOnPort,
                                @Param("p_ManagementIP") String p_ManagementIP,
                                @Param("p_DeviceName") String p_DeviceName,
                                @Param("p_OrderId") long p_OrderId,
                                @Param("p_PlugableId") Long p_PlugableId,
                                @Param("p_PortId") Long p_PortId,
                                @Param("success") int success,
                                @Param("keys") String[] keys,
                                @Param("p_values") String[] p_values);


    @Query(value =
            "SELECT lp.* " +
                    "FROM logical_port lp " +
                    "INNER JOIN port p ON lp.portid = p.portid " +
                    "INNER JOIN card_slot cs ON p.cardlslotname = cs.name " +
                    "INNER JOIN card c ON c.cardid = cs.cardid " +
                    "WHERE c.devicename = :deviceName AND lp.name = :logicalPortName " +
                    "UNION " +
                    "SELECT lp.* " +
                    "FROM logical_port lp " +
                    "INNER JOIN pluggable p2 ON lp.plugableid = p2.id " +
                    "INNER JOIN card_slot cs2 ON p2.cardlslotname = cs2.name " +
                    "INNER JOIN card c2 ON c2.cardid = cs2.cardid " +
                    "WHERE c2.devicename = :deviceName AND lp.name = :logicalPortName " +
                    "UNION " +
                    "SELECT lp.* " +
                    "FROM logical_port lp " +
                    "INNER JOIN pluggable p3 ON lp.plugableid = p3.id " +
                    "WHERE p3.devicename = :deviceName AND lp.name = :logicalPortName " +
                    "UNION " +
                    "SELECT lp.* " +
                    "FROM logical_port lp " +
                    "INNER JOIN port p4 ON lp.portid = p4.portid " +
                    "WHERE p4.devicename = :deviceName AND lp.name = :logicalPortName ",
            nativeQuery = true)
    LogicalPort findLogicalPortsByDeviceNameAndLogicalPortName(
            @Param("deviceName") String deviceName,
            @Param("logicalPortName") String logicalPortName
    );

    @Query(value = "SELECT * FROM logical_port lp WHERE lp.devicename = :deviceName AND " +
            "lp.position_on_device = :positionOnDevice AND " +
            "lp.position_on_port = :positionOnPort", nativeQuery = true)
    LogicalPort findByDeviceNameAndPosition(@Param("deviceName") String deviceName,
                                            @Param("positionOnDevice") int positionOnDevice,
                                            @Param("positionOnPort") int positionOnPort);

    @Query(value = "SELECT * FROM logical_port lp WHERE lp.devicename = :deviceName AND" +
            " lp.position_on_card = :positionOnCard AND lp.position_on_port = :positionOnPort", nativeQuery = true)
    LogicalPort findBylogicalPort(@Param("deviceName") String deviceName,
                                  @Param("positionOnCard") int positionOnCard,
                                  @Param("positionOnPort") int positionOnPort);

    @Query(value = "SELECT lp FROM LogicalPort lp WHERE lp.name = :name AND lp.deviceName = :deviceName",
            nativeQuery = true)
    LogicalPort findLogicalPortByNameAndDeviceName(@Param("name") String name,
                                                   @Param("deviceName") String deviceName);
    @Transactional
    void deleteLogicalPortByLogicalportid(Long logicalPortId);

    //TODO future add card name validate composite plugable name
    //if maintain unique both plugable and lp have same realtion name

}

