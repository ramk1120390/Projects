package com.Network.Network.DevicemetamodelRepo;

import com.Network.Network.DevicemetamodelPojo.Pluggable;
import com.Network.Network.DevicemetamodelPojo.Port;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface PluggableRepo extends JpaRepository<Pluggable,Long> {


    @Query(value = "SELECT * FROM pluggable WHERE cardlslotname = ?1 AND position_on_card = ?2", nativeQuery = true)
    Pluggable findPortsByCardSlotNameAndPositionOnCard(String cardSlotName, Integer positionOnCard);
    @Query(value = "SELECT * FROM pluggable WHERE devicename = :devicename AND position_on_device = :position_on_device", nativeQuery = true)
    Pluggable findByDeviceNameAndPositionOnDevice(@Param("devicename") String devicename, @Param("position_on_device") Integer positionOnDevice);

    @Query(value = "SELECT * FROM pluggable WHERE plugablename = ?1 AND cardname = ?2", nativeQuery = true)
    Pluggable findPluggableByCardNameAndDeviceName(String portName, String cardName);

    @Query(value = "CALL insert_pluggable(:i_pluggablename, :i_positionOnCard, :i_positionOnDevice, :i_vendor, :i_pluggableModel, :i_pluggablePartNumber, :i_operationalState, :i_administrativeState, :i_usageState, :i_href, :i_managementIp, :i_relation, :i_cardname, :i_cardSlotName, :i_order_id, :i_devicename, :i_cardid, :o_success)", nativeQuery = true)
    int insertPluggable(
            @Param("i_pluggablename") String iPortname,
            @Param("i_positionOnCard") Integer iPositionOnCard,
            @Param("i_positionOnDevice") Integer iPositionOnDevice,
            @Param("i_vendor") String iVendor,
            @Param("i_pluggableModel") String iPluggableModel,
            @Param("i_pluggablePartNumber") String iPluggablePartNumber,
            @Param("i_operationalState") String iOperationalState,
            @Param("i_administrativeState") String iAdministrativeState,
            @Param("i_usageState") String iUsageState,
            @Param("i_href") String iHref,
            @Param("i_managementIp") String iManagementIp,
            @Param("i_relation") String iRelation,
            @Param("i_cardname") String iCardname,
            @Param("i_cardSlotName") String iCardSlotName,
            @Param("i_order_id") Long iOrderId,
            @Param("i_devicename") String iDevicename,
            @Param("i_cardid") Long iCardid,
            @Param("o_success") int oSuccess
    );
    @Query(value = "SELECT p.* FROM pluggable p " +
            "INNER JOIN card_slot cs ON p.cardlslotname = cs.name " +
            "INNER JOIN card c ON c.cardid = cs.cardid " +
            "WHERE c.devicename = :deviceName AND p.plugablename = :pluggableName " +
            "UNION " +
            "SELECT p.* FROM pluggable p " +
            "WHERE p.devicename = :deviceName AND p.plugablename = :pluggableName", nativeQuery = true)
    Pluggable findByDeviceNameAndPluggableName(@Param("deviceName") String deviceName, @Param("pluggableName") String pluggableName);



    @Query(value = "CALL insert_pluggabledevice(:i_pluggablename, :i_positionOnCard, :i_positionOnDevice, :i_vendor, :i_pluggableModel, :i_pluggablePartNumber, :i_operationalState, :i_administrativeState, :i_usageState, :i_href, :i_managementIp, :i_order_id, :i_devicename, :o_success)", nativeQuery = true)
    int insertPluggabledevice(
            @Param("i_pluggablename") String i_pluggablename,
            @Param("i_positionOnCard") Integer i_positionOnCard,
            @Param("i_positionOnDevice") Integer i_positionOnDevice,
            @Param("i_vendor") String i_vendor,
            @Param("i_pluggableModel") String i_pluggableModel,
            @Param("i_pluggablePartNumber") String i_pluggablePartNumber,
            @Param("i_operationalState") String i_operationalState,
            @Param("i_administrativeState") String i_administrativeState,
            @Param("i_usageState") String i_usageState,
            @Param("i_href") String i_href,
            @Param("i_managementIp") String i_managementIp,
            @Param("i_order_id") Long i_order_id,
            @Param("i_devicename") String i_devicename,
            @Param("o_success") Integer o_success
    );




}

