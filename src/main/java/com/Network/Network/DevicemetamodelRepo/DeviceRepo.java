package com.Network.Network.DevicemetamodelRepo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.Network.Network.DevicemetamodelPojo.Building;
import com.Network.Network.DevicemetamodelPojo.Device;

public interface DeviceRepo extends JpaRepository<Device, Long> {
    Device findByDevicename(String devicename);

    @Query(value = "CALL insert_device(:iDevicename, :iDeviceModel, :iLocation, :iOrganisation, :iCustomer, :iManagementIp, :iRackPosition, :iOperationalState, :iAdministrativeState, :iUsageState, :iSerialNumber, :iHref, :iBuilding, :iOrderId, :i_realtion, :keys, :p_values , :success)", nativeQuery = true)
    int insertDevice(
            @Param("iDevicename") String iDevicename,
            @Param("iDeviceModel") String iDeviceModel,
            @Param("iLocation") String iLocation,
            @Param("iOrganisation") String iOrganisation,
            @Param("iCustomer") String iCustomer,
            @Param("iManagementIp") String iManagementIp,
            @Param("iRackPosition") String iRackPosition,
            @Param("iOperationalState") String iOperationalState,
            @Param("iAdministrativeState") String iAdministrativeState,
            @Param("iUsageState") String iUsageState,
            @Param("iSerialNumber") String iSerialNumber,
            @Param("iHref") String iHref,
            @Param("iBuilding") String iBuilding,
            @Param("iOrderId") Long iOrderId,
            @Param("i_realtion") String iRelation,
            @Param("keys") String[] keys,
            @Param("p_values") String[] p_values,
            @Param("success") int success
    );

    @Query(value = "CALL update_device(:i_administrative_state, :i_credentials, :i_customer, :i_device_model, :i_devicename, :i_href, :i_location, :i_management_ip, :i_operational_state, :i_organisation, :i_poll_interval, :i_rack_position, :i_relation, :i_serial_number, :i_usage_state, :i_building_name, :i_access_key, :i_order_id, :i_id, :keys, :p_values, :success)", nativeQuery = true)
    int updateDevice(
            @Param("i_administrative_state") String administrativeState,
            @Param("i_credentials") String credentials,
            @Param("i_customer") String customer,
            @Param("i_device_model") String deviceModel,
            @Param("i_devicename") String deviceName,
            @Param("i_href") String href,
            @Param("i_location") String location,
            @Param("i_management_ip") String managementIp,
            @Param("i_operational_state") String operationalState,
            @Param("i_organisation") String organisation,
            @Param("i_poll_interval") String pollInterval,
            @Param("i_rack_position") String rackPosition,
            @Param("i_relation") String relation,
            @Param("i_serial_number") String serialNumber,
            @Param("i_usage_state") String usageState,
            @Param("i_building_name") String buildingName,
            @Param("i_access_key") String accessKey,
            @Param("i_order_id") Long orderId,
            @Param("i_id") Long id,
            @Param("keys") String[] keys,
            @Param("p_values") String[] p_values,
            @Param("success") Integer success
    );

    @Query(value = "SELECT d.* " +
            "FROM device d " +
            "WHERE d.devicename IN (" +
            "    SELECT c.devicename " +
            "    FROM card c " +
            "    WHERE c.devicename = :deviceName" +
            ")" +
            "UNION " +
            "SELECT d2.* " +
            "FROM device d2 " +
            "WHERE d2.devicename IN (" +
            "    SELECT p.devicename " +
            "    FROM port p " +
            "    WHERE p.devicename = :deviceName " +
            "    UNION " +
            "    SELECT p2.devicename " +
            "    FROM pluggable p2 " +
            "    WHERE p2.devicename = :deviceName" +
            ")", nativeQuery = true)
    Device findDevicesByName(@Param("deviceName") String deviceName);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Device d WHERE d.devicename = :deviceName", nativeQuery = true)
    void deleteByDevicename(@Param("deviceName") String deviceName);

}
