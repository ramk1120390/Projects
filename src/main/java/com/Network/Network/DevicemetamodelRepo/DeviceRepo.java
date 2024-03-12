package com.Network.Network.DevicemetamodelRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Network.Network.DevicemetamodelPojo.Building;
import com.Network.Network.DevicemetamodelPojo.Device;

public interface DeviceRepo extends JpaRepository<Device, Long> {
	Device findByDevicename(String devicename);
	 @Query(value = "CALL insert_device(:iDevicename, :iDeviceModel, :iLocation, :iOrganisation, :iCustomer, :iManagementIp, :iRackPosition, :iOperationalState, :iAdministrativeState, :iUsageState, :iSerialNumber, :iHref, :iBuilding, :iOrderId, :i_realtion, :success)", nativeQuery = true)
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
	        @Param("success") int success
	    );
	

}
