package com.Network.Network.DevicemetamodelRepo;

import com.Network.Network.DevicemetamodelPojo.CardSlot;
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

public interface PluggableRepo extends JpaRepository<Pluggable, Long> {
    @Query(value = "SELECT * FROM pluggable WHERE cardlslotname = :cardSlotName", nativeQuery = true)
    Pluggable findByCardSlotName(@Param("cardSlotName") String cardSlotName);


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

    Pluggable findById(long id);

    @Query(value = "CALL update_pluggable_on_device(:p_pluggableid, :p_pluggablename, :p_pluggableModel, :p_pluggablePartNumber, :p_positionOnDevice, :p_operationalState, :p_administrativeState, :p_usageState, :p_href, :p_managementIp, :p_vendor, :p_orderId, :p_deviceName, :success)", nativeQuery = true)
    int updatePluggableOnDevice(
            @Param("p_pluggableid") Long pluggableId,
            @Param("p_pluggablename") String pluggableName,
            @Param("p_pluggableModel") String pluggableModel,
            @Param("p_pluggablePartNumber") String pluggablePartNumber,
            @Param("p_positionOnDevice") Integer positionOnDevice,
            @Param("p_operationalState") String operationalState,
            @Param("p_administrativeState") String administrativeState,
            @Param("p_usageState") String usageState,
            @Param("p_href") String href,
            @Param("p_managementIp") String managementIp,
            @Param("p_vendor") String vendor,
            @Param("p_orderId") Long orderId,
            @Param("p_deviceName") String deviceName,
            @Param("success") Integer success);

    @Query(value = "CALL update_pluggable_on_card(:p_pluggableid, :p_pluggablename, :p_pluggableModel, :p_pluggablePartNumber, :p_positionOnCard, :p_operationalState, :p_administrativeState, :p_usageState, :p_href, :p_managementIp, :p_vendor, :p_orderId, :p_cardname, :p_cardslotname, :p_deviceName, :success)", nativeQuery = true)
    int updatePluggableOnCard(
            @Param("p_pluggableid") Long pluggableId,
            @Param("p_pluggablename") String pluggableName,
            @Param("p_pluggableModel") String pluggableModel,
            @Param("p_pluggablePartNumber") String pluggablePartNumber,
            @Param("p_positionOnCard") Integer positionOnCard,
            @Param("p_operationalState") String operationalState,
            @Param("p_administrativeState") String administrativeState,
            @Param("p_usageState") String usageState,
            @Param("p_href") String href,
            @Param("p_managementIp") String managementIp,
            @Param("p_vendor") String vendor,
            @Param("p_orderId") Long orderId,
            @Param("p_cardname") String cardName,
            @Param("p_cardslotname") String cardSlotName,
            @Param("p_deviceName") String deviceName,
            @Param("success") Integer success);

    @Query(value = "SELECT p.* " +
            "FROM pluggable p " +
            "INNER JOIN card_slot cs ON p.cardlslotname = cs.name " +
            "INNER JOIN card c ON c.cardid = cs.cardid " +
            "WHERE c.devicename = :deviceName AND c.cardid = :cardId AND p.id = :pluggableId",
            nativeQuery = true)
    Pluggable findByDeviceNameAndCardIdAndPluggableId(@Param("deviceName") String deviceName,
                                                      @Param("cardId") Long cardId,
                                                      @Param("pluggableId") Long pluggableId);

    @Query(value = "SELECT * FROM pluggable p WHERE p.position_on_card = :positionOnCard AND p.id = :id", nativeQuery = true)
    Pluggable findByPositionOnCardAndPluggableId(
            @Param("positionOnCard") Integer positionOnCard,
            @Param("id") Long id
    );

    @Query(value = "SELECT * FROM pluggable p WHERE p.id = :pluggableId AND p.position_on_device = :positionOnDevice AND p.devicename = :deviceName", nativeQuery = true)
    Pluggable findPluggableByIdAndPositionOnDeviceAndDeviceName(
            @Param("pluggableId") long pluggableId,
            @Param("positionOnDevice") int positionOnDevice,
            @Param("deviceName") String deviceName
    );

    @Query(value = "SELECT p FROM Device d INNER JOIN d.pluggables p WHERE p.devicename = :deviceName " +
            "UNION " +
            "SELECT p2 FROM CardSlot cs INNER JOIN cs.pluggables p2 WHERE p2.cardlslotname IN :cardSlotNames",nativeQuery = true)
    List<Pluggable> findPluggablesByDeviceNameAndCardSlotNames(@Param("deviceName") String deviceName,
                                                               @Param("cardSlotNames") List<String> cardSlotNames);


}

