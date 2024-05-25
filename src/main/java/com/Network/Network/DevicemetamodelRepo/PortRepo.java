package com.Network.Network.DevicemetamodelRepo;

import com.Network.Network.DevicemetamodelPojo.Card;
import com.Network.Network.DevicemetamodelPojo.CardSlot;
import com.Network.Network.DevicemetamodelPojo.Pluggable;
import com.Network.Network.DevicemetamodelPojo.Port;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PortRepo extends JpaRepository<Port, Long> {
    @Query(value = "SELECT * FROM port WHERE cardlslotname = :cardSlotName", nativeQuery = true)
    Port findByCardSlotName(@Param("cardSlotName") String cardSlotName);


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

    Port findByPortid(Long portid);

    @Query(value = "CALL update_port_on_device(:p_portid, :p_portname, :p_positionOnDevice, :p_portType, :p_operationalState, :p_administrativeState, "
            + ":p_usageState, :p_href, :p_portSpeed, :p_capacity, :p_managementIp, :p_orderId, :p_deviceName, :success)",
            nativeQuery = true)
    int updatePortOnDevice(@Param("p_portid") Long portId,
                           @Param("p_portname") String portName,
                           @Param("p_positionOnDevice") Integer positionOnDevice,
                           @Param("p_portType") String portType,
                           @Param("p_operationalState") String operationalState,
                           @Param("p_administrativeState") String administrativeState,
                           @Param("p_usageState") String usageState,
                           @Param("p_href") String href,
                           @Param("p_portSpeed") String portSpeed,
                           @Param("p_capacity") Integer capacity,
                           @Param("p_managementIp") String managementIp,
                           @Param("p_orderId") Long orderId,
                           @Param("p_deviceName") String deviceName,
                           @Param("success") Integer success);

    @Query(value = "CALL update_port_on_card(:p_portid, :p_portname, :p_positionOnCard, :p_portType, :p_operationalState, :p_administrativeState, "
            + ":p_usageState, :p_href, :p_portSpeed, :p_cardname, :p_cardslotname, :p_capacity, :p_managementIp, :p_orderId, :p_deviceName, :success)",
            nativeQuery = true)
    int updatePortOnCard(
            @Param("p_portid") Long p_portid,
            @Param("p_portname") String p_portname,
            @Param("p_positionOnCard") Integer p_positionOnCard,
            @Param("p_portType") String p_portType,
            @Param("p_operationalState") String p_operationalState,
            @Param("p_administrativeState") String p_administrativeState,
            @Param("p_usageState") String p_usageState,
            @Param("p_href") String p_href,
            @Param("p_portSpeed") String p_portSpeed,
            @Param("p_cardname") String p_cardname,
            @Param("p_cardslotname") String p_cardslotname,
            @Param("p_capacity") Integer p_capacity,
            @Param("p_managementIp") String p_managementIp,
            @Param("p_orderId") Long p_orderId,
            @Param("p_deviceName") String p_deviceName,
            @Param("success") Integer success
    );

    @Query(value = "SELECT p.* " +
            "FROM port p " +
            "INNER JOIN card_slot cs ON p.cardlslotname = cs.name " +
            "INNER JOIN card c ON c.cardid = cs.cardid " +
            "WHERE c.devicename = :deviceName AND c.cardid = :cardId AND p.portid = :portId",
            nativeQuery = true)
    Port findByDeviceNameAndCardIdAndPortId(@Param("deviceName") String deviceName,
                                            @Param("cardId") Long cardId,
                                            @Param("portId") Long portId);

    @Query(value = "SELECT * FROM port p WHERE p.position_on_card = :positionOnCard AND p.portid = :portId", nativeQuery = true)
    Port findByPositionOnCardAndPortId(
            @Param("positionOnCard") Integer positionOnCard,
            @Param("portId") Long portId
    );

    @Query(value = "SELECT * FROM port WHERE portid = :portId AND position_on_device = :positionOnDevice AND devicename = :deviceName", nativeQuery = true)
    Port findPortByPortIdAndPositionOnDeviceAndDeviceName(
            @Param("portId") long portId,
            @Param("positionOnDevice") int positionOnDevice,
            @Param("deviceName") String deviceName
    );

    @Query("SELECT p FROM Device d INNER JOIN d.ports p WHERE p.devicename = :deviceName " +
            "UNION " +
            "SELECT p2 FROM CardSlot cs INNER JOIN cs.ports p2 WHERE p2.cardlslotname IN :cardSlotNames")
    List<Port> findPortsByDeviceNameAndCardSlotNames(@Param("deviceName") String deviceName,
                                                     @Param("cardSlotNames") List<String> cardSlotNames);
}
