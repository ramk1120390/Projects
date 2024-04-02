package com.Network.Network.DevicemetamodelRepo;

import com.Network.Network.DevicemetamodelPojo.Card;
import com.Network.Network.DevicemetamodelPojo.Port;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PortRepo extends JpaRepository<Port, Long> {
    @Query(value = "SELECT * FROM port WHERE portname = ?1 AND cardname = ?2", nativeQuery = true)
    Port findPortsByCardNameAndDeviceName(String portName, String cardName);

    @Query(value = "SELECT * FROM port WHERE cardlslotname = ?1 AND position_on_card = ?2", nativeQuery = true)
    Port findPortsByCardSlotNameAndPositionOnCard(String cardSlotName, Integer positionOnCard);

    @Query(value = "CALL insert_port(:i_portname, :i_positionOnCard, :i_positionOnDevice, :i_operationalState, :i_administrativeState, :i_usageState, :i_href, :i_PortSpeed, :i_Capacity, :i_managementIp, :i_relation, :i_cardname, :i_cardSlotName, :i_order_id, :i_devicename, :i_cardid, :success)", nativeQuery = true)
    int insertPort(
            @Param("i_portname") String portname,
            @Param("i_positionOnCard") Integer positionOnCard,
            @Param("i_positionOnDevice") Integer positionOnDevice,
            @Param("i_operationalState") String operationalState,
            @Param("i_administrativeState") String administrativeState,
            @Param("i_usageState") String usageState,
            @Param("i_href") String href,
            @Param("i_PortSpeed") String PortSpeed,
            @Param("i_Capacity") Integer Capacity,
            @Param("i_managementIp") String managementIp,
            @Param("i_relation") String relation,
            @Param("i_cardname") String cardname,
            @Param("i_cardSlotName") String cardSlotName,
            @Param("i_order_id") Long orderId,
            @Param("i_devicename") String devicename,
            @Param("i_cardid") Long cardid,
            @Param("success") Integer success
    );

    @Query(value = "SELECT p.* FROM port p " +
            "INNER JOIN card_slot cs ON p.cardlslotname = cs.name " +
            "INNER JOIN card c ON c.cardid = cs.cardid " +
            "WHERE c.devicename = :devicename AND p.portname = :portname " +
            "UNION " +
            "SELECT p.* FROM port p " +
            "WHERE p.devicename = :devicename AND p.portname = :portname ", nativeQuery = true)
    Port findPortByDeviceNameAndPortName(@Param("devicename") String devicename, @Param("portname") String portname);

    @Query(value = "SELECT * FROM port WHERE devicename = :devicename AND position_on_device = :position_on_device", nativeQuery = true)
    Port findByDeviceNameAndPositionOnDevice(@Param("devicename") String devicename, @Param("position_on_device") Integer positionOnDevice);

    @Query(value = "CALL insert_deviceport(:i_portname, :i_positionOnDevice, :i_operationalState, :i_administrativeState, :i_usageState, :i_href, :i_PortSpeed, :i_Capacity, :i_managementIp, :i_relation, :i_order_id, :i_devicename, :o_success)", nativeQuery = true)
    int insertDevicePort(@Param("i_portname") String portname,
                         @Param("i_positionOnDevice") Integer positionOnDevice,
                         @Param("i_operationalState") String operationalState,
                         @Param("i_administrativeState") String administrativeState,
                         @Param("i_usageState") String usageState,
                         @Param("i_href") String href,
                         @Param("i_PortSpeed") String portSpeed,
                         @Param("i_Capacity") Integer capacity,
                         @Param("i_managementIp") String managementIp,
                         @Param("i_relation") String relation,
                         @Param("i_order_id") Long orderId,
                         @Param("i_devicename") String devicename,
                         @Param("o_success") Integer success);
}
