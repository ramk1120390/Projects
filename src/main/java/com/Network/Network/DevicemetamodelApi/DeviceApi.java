package com.Network.Network.DevicemetamodelApi;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Network.Network.DevicemetamodelPojo.Building;
import com.Network.Network.DevicemetamodelPojo.Device;
import com.Network.Network.DevicemetamodelPojo.DeviceDto;
import com.Network.Network.DevicemetamodelPojo.DeviceMetaModel;
import com.Network.Network.DevicemetamodelPojo.Order;
import com.Network.Network.DevicemetamodelRepo.BuildingRepo;
import com.Network.Network.DevicemetamodelRepo.DeviceMetaModelRepo;
import com.Network.Network.DevicemetamodelRepo.DeviceRepo;
import com.Network.Network.DevicemetamodelRepo.OrderRepo;
import com.Network.Network.Exception.AppExceptionHandler;

@RestController
public class DeviceApi {
	@Autowired
	private AppExceptionHandler appExceptionHandler;
	@Autowired
	private BuildingRepo buildingRepo;
	@Autowired
	private OrderRepo orderRepo;
	@Autowired
	private DeviceRepo deviceRepo;
	@Autowired
	private DeviceMetaModelRepo deviceMetaModelRepo;

	@PostMapping("/insertDevice")
	public ResponseEntity<String> insertDeviceMetaModel(@RequestParam("name") String building,
			@RequestParam("orderid") Long orderid, @RequestBody DeviceDto dto) {
		building = building.toLowerCase();
		try {
			Building exBuilding = buildingRepo.findByBuildingName(building);
			Device device = deviceRepo.findByDevicename(dto.getDevicename());
			Optional<Order> order = orderRepo.findById(orderid);
			Order exOrder = order.orElse(null); // Using orElse to handle the optional
			DeviceMetaModel deviceMetaModel = deviceMetaModelRepo.findByDeviceModel(dto.getDeviceModel());

			if (exBuilding == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given building not found");
			}
			if (!order.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given order not found");
			}
			if (device != null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given device not found");
			}
			if (deviceMetaModel == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given device metamodel not found");
			}

			String customer = exOrder.getCustomer() != null ? exOrder.getCustomer().getCustomername() : null;
			int success = deviceRepo.insertDevice(dto.getDevicename(), dto.getDeviceModel(), dto.getLocation(),
					dto.getOrganisation(), customer, dto.getManagementIp(), dto.getRackPosition(),
					dto.getOperationalState(), dto.getAdministrativeState(), dto.getUsageState(), dto.getSerialNumber(),
					dto.getHref(), building, orderid, "building_to_device", 0);

			if (success == 1) {
				return ResponseEntity.ok("Device inserted successfully.");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Error occurred while inserting device.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			appExceptionHandler.raiseException(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while processing request.");
		}
	}

	@PutMapping("/updateDevice")
	public ResponseEntity<String> insertDeviceMetaModel(@RequestParam("name") String devicename,
			@RequestBody DeviceDto deviceDto) {
		devicename = devicename.toLowerCase();
		try {
			Device device = deviceRepo.findByDevicename(devicename);

			if (device == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given device not found");
			}

			devicename = (deviceDto.getDevicename() == null) ? device.getDevicename() : deviceDto.getDevicename();

			if (deviceDto.getDevicename() != null) {
				device = deviceRepo.findByDevicename(deviceDto.getDevicename());
				if (device == null) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given device not found");
				}
			}

			String devicemodel = (deviceDto.getDeviceModel() == null) ? device.getDeviceModel()
					: deviceDto.getDeviceModel();

			String buildingname = (deviceDto.getBuildingname() == null) ? device.getBuilding().getBuildingName()
					: deviceDto.getBuildingname();

			Long orderId = (deviceDto.getOrderid() == null) ? device.getOrder().getId() : deviceDto.getOrderid();

			String location = (deviceDto.getLocation() == null) ? device.getLocation() : deviceDto.getLocation();

			String organisation = (deviceDto.getOrganisation() == null) ? device.getOrganisation()
					: deviceDto.getOrganisation();

			Optional<Order> order = orderRepo.findById(orderId);
			Order exOrder = order.orElse(null);

			// Check if customer is null in DTO, if not, fetch it from exOrder
			String customer = (deviceDto.getCustomer() == null && exOrder != null)
					? exOrder.getCustomer().getCustomername()
					: deviceDto.getCustomer();

			String managementIp = (deviceDto.getManagementIp() == null) ? device.getManagementIp()
					: deviceDto.getManagementIp();

			String rackPosition = (deviceDto.getRackPosition() == null) ? device.getRackPosition()
					: deviceDto.getRackPosition();

			String operationalState = (deviceDto.getOperationalState() == null) ? device.getOperationalState()
					: deviceDto.getOperationalState();

			String administrativeState = (deviceDto.getAdministrativeState() == null) ? device.getAdministrativeState()
					: deviceDto.getAdministrativeState();

			String usageState = (deviceDto.getUsageState() == null) ? device.getUsageState()
					: deviceDto.getUsageState();

			String serialNumber = (deviceDto.getSerialNumber() == null) ? device.getSerialNumber()
					: deviceDto.getSerialNumber();

			String href = (deviceDto.getHref() == null) ? device.getHref() : deviceDto.getHref();

			String credentials = (deviceDto.getCredentials() == null) ? device.getCredentials()
					: deviceDto.getCredentials();

			String accessKey = (deviceDto.getAccessKey() == null) ? device.getAccessKey() : deviceDto.getAccessKey();

			String pollInterval = (deviceDto.getPollInterval() == null) ? device.getPollInterval()
					: deviceDto.getPollInterval();

			// Check if buildingname is null in DTO, if not, fetch it from repository
			if (deviceDto.getBuildingname() == null) {
				Building exBuilding = buildingRepo.findByBuildingName(deviceDto.getBuildingname());
				if (exBuilding == null) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given building not found");
				}
			}

			// Check if orderid is null in DTO, if not, fetch it from repository
			if (deviceDto.getOrderid() == null) {

				if (!order.isPresent()) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given order not found");
				}
			}

			// Check if devicemodel is null in DTO, if not, fetch it from repository
			if (deviceDto.getDeviceModel() == null) {
				DeviceMetaModel deviceMetaModel = deviceMetaModelRepo.findByDeviceModel(deviceDto.getDeviceModel());
				if (deviceMetaModel == null) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given device model not found");
					// TODO before update when device model update need check unders device cardslot or connected need to restrict
				}
			}
			
			//before update when device model update need check unders device cardslot or connected need to restrict

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	//TODO delete device and delete shelf need todo
	//TODO shelf to slot need to create card
	//
}
