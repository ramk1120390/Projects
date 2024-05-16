package com.Network.Network.DevicemetamodelApi;

import java.sql.SQLException;
import java.util.*;

import com.Network.Network.DevicemetamodelPojo.*;
import com.Network.Network.DevicemetamodelRepo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private ShelfRepo shelfRepo;
    @Autowired
    private CardRepo cardRepo;
    @Autowired
    private SlotRepo slotRepo;
    @Autowired
    private PortRepo portRepo;
    @Autowired
    private PluggableRepo pluggableRepo;


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
    public ResponseEntity<String> updateDevice(@RequestParam("name") String deviceName,
                                               @RequestBody DeviceDto deviceDto) {
        deviceName = deviceName.toLowerCase();
        try {
            Device device = deviceRepo.findByDevicename(deviceName);
            if (device == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given device not found");
            }

            // Check if new devicename is already used
            if (deviceDto.getDevicename() != null && !deviceDto.getDevicename().equalsIgnoreCase(device.getDevicename())) {
                Device existingDevice = deviceRepo.findByDevicename(deviceDto.getDevicename());
                if (existingDevice != null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Given device name already exists");
                }
            }


            String newDeviceName = (deviceDto.getDevicename() != null) ? deviceDto.getDevicename() : device.getDevicename();
            String deviceModel = (deviceDto.getDeviceModel() != null) ? deviceDto.getDeviceModel() : device.getDeviceModel();

            // Fetch the existing device's building and order details
            String buildingName = (deviceDto.getBuildingname() != null) ? deviceDto.getBuildingname() : device.getBuilding().getBuildingName();
            Long orderId = (deviceDto.getOrderid() != null) ? deviceDto.getOrderid() : device.getOrder().getId();

            // Fetch the customer details from existing order
            Order existingOrder = orderRepo.findById(orderId).orElse(null);
            String customer = (deviceDto.getCustomer() != null && existingOrder != null) ? deviceDto.getCustomer() : existingOrder.getCustomer().getCustomername();

            // Fetch other details from DTO or use existing values
            String location = (deviceDto.getLocation() != null) ? deviceDto.getLocation() : device.getLocation();
            String organisation = (deviceDto.getOrganisation() != null) ? deviceDto.getOrganisation() : device.getOrganisation();
            String managementIp = (deviceDto.getManagementIp() != null) ? deviceDto.getManagementIp() : device.getManagementIp();
            String rackPosition = (deviceDto.getRackPosition() != null) ? deviceDto.getRackPosition() : device.getRackPosition();
            String operationalState = (deviceDto.getOperationalState() != null) ? deviceDto.getOperationalState() : device.getOperationalState();
            String administrativeState = (deviceDto.getAdministrativeState() != null) ? deviceDto.getAdministrativeState() : device.getAdministrativeState();
            String usageState = (deviceDto.getUsageState() != null) ? deviceDto.getUsageState() : device.getUsageState();
            String serialNumber = (deviceDto.getSerialNumber() != null) ? deviceDto.getSerialNumber() : device.getSerialNumber();
            String href = (deviceDto.getHref() != null) ? deviceDto.getHref() : device.getHref();
            String credentials = (deviceDto.getCredentials() != null) ? deviceDto.getCredentials() : device.getCredentials();
            String accessKey = (deviceDto.getAccessKey() != null) ? deviceDto.getAccessKey() : device.getAccessKey();
            String pollInterval = (deviceDto.getPollInterval() != null) ? deviceDto.getPollInterval() : device.getPollInterval();

            // Fetch the building details from repository if new building name is provided
            if (deviceDto.getBuildingname() != null) {
                Building existingBuilding = buildingRepo.findByBuildingName(deviceDto.getBuildingname());
                if (existingBuilding == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given building not found");
                }
            }

            // Check if order exists if new order id is provided
            if (deviceDto.getOrderid() != null) {
                if (existingOrder == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given order not found");
                }
            }

            // Check if device model exists
            if (deviceDto.getDeviceModel() != null) {
                DeviceMetaModel deviceMetaModel = deviceMetaModelRepo.findByDeviceModel(deviceDto.getDeviceModel());
                if (deviceMetaModel == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given device model not found");
                } else {
                    int newShelvesContained = deviceMetaModelRepo.findShelvesContainedByDeviceModel(deviceDto.getDeviceModel());

                    int oldShelvesContained = deviceMetaModelRepo.findShelvesContainedByDeviceModel(device.getDeviceModel());
                    System.out.println("old shelf" + oldShelvesContained + "new shelf" + newShelvesContained);
                    if (newShelvesContained != oldShelvesContained) {
                        throw new RuntimeException("Shelf position must match with available shelf positions for the device model");
                    }
                    //TODO if possible change Same position need check allowed card list also if card under device
                }
            }
            int success = deviceRepo.updateDevice(administrativeState, credentials, customer, deviceModel,
                    newDeviceName, href, location, managementIp, operationalState, organisation, pollInterval,
                    rackPosition, device.getRealtion(), serialNumber, usageState, buildingName, accessKey,
                    orderId, device.getId(), 0);
            System.out.println("success: " + success);

            if (success == 1) {
                return ResponseEntity.ok("Device updated successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error occurred while updating device.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while updating device.");
        }
    }

    @PostMapping("/CreateCard")
    public ResponseEntity<String> createCard(@RequestParam("name") String deviceName,
                                             @RequestParam("orderid") Long orderid,
                                             @RequestBody CardDto cardDto) {
        deviceName = deviceName.toLowerCase();
        try {
            Device device = deviceRepo.findByDevicename(deviceName);
            Optional<Order> order = orderRepo.findById(orderid);
            if (device == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given device not found");
            }
            if (!order.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given order not found");
            }
            Card exCard = cardRepo.findCardsByCardNameAndDeviceName(cardDto.getCardname(), deviceName);
            System.out.println("excard" + exCard);
            if (exCard != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Given card already exists ");
            }
            DeviceMetaModel deviceModel = deviceMetaModelRepo.findByDeviceModel(device.getDeviceModel().toLowerCase());
            if (deviceModel == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Device Meta-model doesn't exist for the device type: " +
                        device.getDeviceModel());
            }
            // Check if the card model exists on the device model
            if (!deviceModel.getAllowedCardList().contains(cardDto.getCardModel())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Given card model: " + cardDto.getCardModel() +
                        " is not compatible with the device model, AllowedCardList on the device: " +
                        deviceModel.getAllowedCardList());
            }
            String shelfname = deviceName + "_shelf_" + cardDto.getShelfPosition();
            Shelf shelf = shelfRepo.findShelfByName(shelfname);
            if (shelf == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given shelf doesn't exist");
            }
            Card cardOnSlot = cardRepo.findCardsBySlotName(deviceName + "/" +
                    cardDto.getShelfPosition() + "/" + cardDto.getSlotPosition());
            if (cardOnSlot != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("A card already exists on the given shelf, slot position. " +
                        "Existing card details: " + cardOnSlot);
            }

            // Call the insertCard method from the repository
            int success = cardRepo.insertCard(
                    cardDto.getCardname(),
                    deviceName,
                    cardDto.getShelfPosition(),
                    cardDto.getSlotPosition(),
                    cardDto.getVendor(),
                    cardDto.getCardModel(),
                    cardDto.getCardPartNumber(),
                    cardDto.getOperationalState(),
                    cardDto.getAdministrativeState(),
                    cardDto.getUsageState(),
                    cardDto.getHref(),
                    orderid, 0
            );
            if (success == 1) {
                return ResponseEntity.ok("Card created successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create card");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @PutMapping("/updateCard")
    public ResponseEntity<String> updateDevice(
            @RequestParam("cardName") String cardName,
            @RequestParam("deviceName") String deviceName,
            @RequestBody CardDto cardDto) {

        cardName = cardName.toLowerCase();
        deviceName = deviceName.toLowerCase();

        try {
            Device device = deviceRepo.findByDevicename(deviceName);
            if (device == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given Device not found");
            }

            Card card = cardRepo.findCardsByCardNameAndDeviceName(cardName, deviceName);
            if (card == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given card not found");
            }
            String existingSlotName = card.getSlot().getSlotname();
            Slot slots = slotRepo.findBySlotname(existingSlotName);
            Long slotId = slots.getId();

            Long cardId = card.getCardid();
            System.out.println("id of card" + cardId);
            String updatedCardName = (cardDto.getCardname() == null) ? card.getId().getCardname() : cardDto.getCardname();
            String updatedDeviceName = (cardDto.getDevicename() == null) ? card.getId().getDevice().getDevicename() : cardDto.getDevicename();
            Integer shelfPosition = (cardDto.getShelfPosition() == null) ? card.getShelfPosition() : cardDto.getShelfPosition();
            Integer slotPosition = (cardDto.getSlotPosition() == null) ? card.getSlotPosition() : cardDto.getSlotPosition();
            String vendor = (cardDto.getVendor() == null) ? card.getVendor() : cardDto.getVendor();
            String cardModel = (cardDto.getCardModel() == null) ? card.getCardModel() : cardDto.getCardModel();
            String cardPartNumber = (cardDto.getCardPartNumber() == null) ? card.getCardPartNumber() : cardDto.getCardPartNumber();
            String operationalState = (cardDto.getOperationalState() == null) ? card.getOperationalState() : cardDto.getOperationalState();
            String administrativeState = (cardDto.getAdministrativeState() == null) ? card.getAdministrativeState() : cardDto.getAdministrativeState();
            String usageState = (cardDto.getUsageState() == null) ? card.getUsageState() : cardDto.getUsageState();
            String href = (cardDto.getHref() == null) ? card.getHref() : cardDto.getHref();
            Long orderId = (cardDto.getOrderId() == null) ? card.getOrder().getId() : cardDto.getOrderId();


            // If device name changed, check if a device with the new name exists
            if (cardDto.getDevicename() != null && !updatedDeviceName.equals(deviceName)) {
                device = deviceRepo.findByDevicename(updatedDeviceName);
                if (device == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No device found with name: " + updatedDeviceName);
                }

                // If a device with the new name exists, check if there's already a card associated with it
                Card existingCardWithDevice = cardRepo.findCardsByCardNameAndDeviceName(updatedCardName, updatedDeviceName);
                if (existingCardWithDevice != null && !existingCardWithDevice.getCardid().equals(cardId)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Already one card exists with that device: " + existingCardWithDevice);
                }
            }

            // If card name changed, check if the new name is available
            if (cardDto.getCardname() != null && !cardName.equals(updatedCardName)) {
                // Check if there's already a card with the new card name and the same device name
                Card newCard = cardRepo.findCardsByCardNameAndDeviceName(cardDto.getCardname(), deviceName);
                if (newCard != null && !newCard.getCardid().equals(cardId)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Already one card exists with that name: " + newCard);
                }
            }
            System.out.println("Device Name: " + deviceName);
            System.out.println("Shelf Position: " + shelfPosition);

// Update shelf name based on device and shelf position
            String shelfName = null;
            if (!deviceName.equals(cardDto.getDevicename()) && cardDto.getDevicename() != null) {
                shelfName = cardDto.getDevicename() + "_shelf_" + shelfPosition;
                System.out.println(cardDto.getDevicename());
            } else {
                System.out.println("deviceName" + deviceName);
                shelfName = deviceName + "_shelf_" + shelfPosition;
            }
            System.out.println("Shelf Name: " + shelfName);


            Shelf shelf = shelfRepo.findShelfByName(shelfName);
            if (shelf == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given shelf doesn't exist");
            }


            // Check if the slot position needs to be updated
            String slotName = card.getSlot().getSlotname();
            String newSlotName = updatedDeviceName + "/" + shelfPosition + "/" + slotPosition;
            if (!slotName.equals(newSlotName)) {
                // If the slot position is different, check if there's
                // Check if there's already a card on the new slot
                Card cardOnSlot = cardRepo.findCardsBySlotName(newSlotName);
                if (cardOnSlot != null && !cardOnSlot.getCardid().equals(cardId)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("A card already exists on the given shelf, slot position. Existing card details: " + cardOnSlot);
                }
            }

            // Check if the orderId needs to be updated
            if (cardDto.getOrderId() != null && !card.getOrder().getId().equals(cardDto.getOrderId())) {
                Order existingOrder = orderRepo.findById(orderId).orElse(null);
                if (existingOrder == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given order id is not found" + card);
                }
            }

            // Check if the card model is associated with the device
            if (cardDto.getCardModel() != null && !card.getCardModel().equals(cardDto.getCardModel())) {
                if (!device.getDeviceModel().contains(cardDto.getCardModel())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Given card model is not associated with that device");
                }
            }

            // Call the repository method to update the card
            int success = cardRepo.updateCard(cardId, updatedCardName, updatedDeviceName, shelfPosition, slotPosition, vendor,
                    cardModel, cardPartNumber, operationalState, administrativeState, usageState, href,
                    orderId, slotId, 0);

            // Check the success flag returned by the repository method
            if (success == 1) {
                return ResponseEntity.ok("Card updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update card");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
        }
    }


    @PostMapping("/CreatePortOnCard")
    public ResponseEntity<String> CreatePortOnCard(@RequestParam("cardid") Long cardid,
                                                   @RequestParam("orderid") Long orderid,
                                                   @RequestBody PortDTO portDTO) {
        try {
            Optional<Order> order = orderRepo.findById(orderid);
            if (!order.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given order not found");
            }
            Card exCard = cardRepo.findByCardid(cardid);
            if (exCard == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given card not found");
            }
            //TODO need code with cardslot insted check cardname or cardid
            String cardName = exCard.getId().getCardname();
            Port exPort = portRepo.findPortsByCardNameAndDeviceName(portDTO.getPortname(), cardName);
            if (exPort != null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given port already found");
            }
            String cardslotname = cardName + cardid + "/" + portDTO.getPositionOnCard();
            Pluggable exPluggable = pluggableRepo.findPortsByCardSlotNameAndPositionOnCard(cardslotname, portDTO.getPositionOnCard());
            if (exPluggable != null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given cardslot already found");
            }
            exPort = portRepo.findPortsByCardSlotNameAndPositionOnCard(cardslotname, portDTO.getPositionOnCard());
            if (exPort != null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given cardslot already found");
            }
            int success = portRepo.insertPort(portDTO.getPortname(), portDTO.getPositionOnCard(), 0,
                    portDTO.getOperationalState(), portDTO.getAdministrativeState(), portDTO.getUsageState(),
                    portDTO.getHref(), portDTO.getPortSpeed(), portDTO.getCapacity(), portDTO.getManagementIp(),
                    portDTO.getRelation(), cardName, cardslotname, orderid, null, cardid, 0);
            // Check the success flag returned by the repository method
            if (success == 1) {
                return ResponseEntity.ok("port created successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create port");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
        }
    }


    @PostMapping("/CreatePortOnDevice")
    public ResponseEntity<String> createPortOnDevice(@RequestParam("devicename") String devicename,
                                                     @RequestParam("orderid") Long orderid,
                                                     @RequestBody PortDTO portDTO) {
        try {
            Optional<Order> order = orderRepo.findById(orderid);
            if (order.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given order not found");
            }

            Device exDevice = deviceRepo.findByDevicename(devicename);
            if (exDevice == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given device not found");
            }

            Port exPort = portRepo.findPortByDeviceNameAndPortName(devicename, portDTO.getPortname());
            if (exPort != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Given port already exists");
            }

            if (portDTO.getPositionOnDevice() == 0 || portDTO.getPositionOnCard() != 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input received. Please provide a valid port number for PositionOnDevice.");
            }

            Pluggable exPluggable = pluggableRepo.findByDeviceNameAndPositionOnDevice(devicename, portDTO.getPositionOnDevice());
            if (exPluggable != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Given position already assigned to a pluggable device");
            }

            exPort = portRepo.findByDeviceNameAndPositionOnDevice(devicename, portDTO.getPositionOnDevice());
            if (exPort != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Given position already assigned to another port");
            }

            int success = portRepo.insertDevicePort(portDTO.getPortname(), portDTO.getPositionOnDevice(),
                    portDTO.getOperationalState(), portDTO.getAdministrativeState(), portDTO.getUsageState(),
                    portDTO.getHref(), portDTO.getPortSpeed(), portDTO.getCapacity(), portDTO.getManagementIp(),
                    portDTO.getRelation(), orderid, devicename, 0);

            if (success == 1) {
                return ResponseEntity.ok("Port created successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create port");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
        }
    }
//TODO if card name changed in card update need find with card id and update
    //and if card  inside cardslot name is changes associated plugable also need change

    @PostMapping("/CreatePluggableOnCard")
    public ResponseEntity<String> CreatePluggableOnCard(@RequestParam("cardid") Long cardid,
                                                        @RequestParam("orderid") Long orderid,
                                                        @RequestBody PluggableDTO pluggableDTO) {
        try {
            Optional<Order> order = orderRepo.findById(orderid);
            if (!order.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given order not found");
            }
            Card exCard = cardRepo.findByCardid(cardid);
            if (exCard == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given card not found");
            }
            //TODO need code with cardslot insted check cardname or cardid
            String cardName = exCard.getId().getCardname();
            Pluggable exPluggable = pluggableRepo.findPluggableByCardNameAndDeviceName(pluggableDTO.getPlugablename(), cardName);
            if (exPluggable != null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given Pluggable already found");
            }
            String cardslotname = cardName + cardid + "/" + pluggableDTO.getPositionOnCard();
            Port exPort = portRepo.findPortsByCardSlotNameAndPositionOnCard(cardslotname, pluggableDTO.getPositionOnCard());
            if (exPort != null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given cardslot already found");
            }
            exPluggable = pluggableRepo.findPortsByCardSlotNameAndPositionOnCard(cardslotname, pluggableDTO.getPositionOnCard());
            if (exPluggable != null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given cardslot already found");
            }
            int success = pluggableRepo.insertPluggable(pluggableDTO.getPlugablename(), pluggableDTO.getPositionOnCard(),
                    0, pluggableDTO.getVendor(), pluggableDTO.getPluggableModel(), pluggableDTO.getPluggablePartNumber(),
                    pluggableDTO.getOperationalState(), pluggableDTO.getAdministrativeState(), pluggableDTO.getUsageState(),
                    pluggableDTO.getHref(), pluggableDTO.getManagementIp(), pluggableDTO.getRelation(), cardName, cardslotname,
                    orderid, null, cardid, 0);
            if (success == 1) {
                return ResponseEntity.ok("Pluggable created successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create Pluggable");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/CreatePlugableOnDevice")
    public ResponseEntity<String> CreatePlugableOnDevice(@RequestParam("devicename") String devicename,
                                                         @RequestParam("orderid") Long orderid,
                                                         @RequestBody PluggableDTO pluggableDTO) {
        try {
            Optional<Order> order = orderRepo.findById(orderid);
            if (order.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given order not found");
            }
            Device exDevice = deviceRepo.findByDevicename(devicename);
            if (exDevice == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given device not found");
            }
            Pluggable explPluggable = pluggableRepo.findByDeviceNameAndPluggableName(devicename, pluggableDTO.getPlugablename());
            if (explPluggable != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Given Pluggable already exists");
            }
            if (pluggableDTO.getPositionOnDevice() == 0 || pluggableDTO.getPositionOnCard() != 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input received. Please provide a valid port number for PositionOnDevice.");
            }
            Port exPort = portRepo.findByDeviceNameAndPositionOnDevice(devicename, pluggableDTO.getPositionOnDevice());
            if (exPort != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Given position already assigned to another port");
            }
            Pluggable exPluggable = pluggableRepo.findByDeviceNameAndPositionOnDevice(devicename, pluggableDTO.getPositionOnDevice());
            if (exPluggable != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Given position already assigned to a pluggable device");
            }
            int success = pluggableRepo.insertPluggabledevice(pluggableDTO.getPlugablename(), 0,
                    pluggableDTO.getPositionOnDevice(), pluggableDTO.getVendor(), pluggableDTO.getPluggableModel(),
                    pluggableDTO.getPluggablePartNumber(), pluggableDTO.getOperationalState(),
                    pluggableDTO.getAdministrativeState(), pluggableDTO.getUsageState(), pluggableDTO.getHref(),
                    pluggableDTO.getManagementIp(), orderid, devicename, 0);
            if (success == 1) {
                return ResponseEntity.ok("Pluggable created successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create Pluggable");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
        }

    }


    @PostMapping("/updatePortbyid")
    public ResponseEntity<String> updatePortbyid(@RequestParam("portid") Long portid,
                                                 @RequestParam("orderid") Long orderid,
                                                 @RequestBody PortDTO portDto) {
        try {
            int success = 0;
            String updatedCardSlotName = null;
            Device exDevice = null;
            Port exPort = portRepo.findByPortid(portid);
            System.out.println("position on card" + exPort.getPositionOnCard());
            if (exPort == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given port id not found");
            }
            String cardname = exPort.getCardname() == null ? "Na" : exPort.getCardname();
            String deviceName = exPort.getDevice() == null || exPort.getDevice().getDevicename() == null ? "Na" : exPort.getDevice().getDevicename();
            String cardSlotName = exPort.getCardSlot() == null || exPort.getCardSlot().getName() == null ? "Na" : exPort.getCardSlot().getName();
            String existingportname = exPort.getPortname();
            int exPortPositionOnCard = exPort.getPositionOnCard();
            int exPortPositionOnDevice = exPort.getPositionOnDevice();
            if (portDto.getDeviceName() != null) {
                exDevice = deviceRepo.findByDevicename(portDto.getDeviceName());
                if (exDevice == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given device not found");
                }
            }
            Optional<Order> order = orderRepo.findById(orderid);
            if (order.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given order not found");
            }
            if (portDto.getCardname() != null && !cardname.equals(portDto.getCardname())) {
                List<Card> cards = cardRepo.findCards(portDto.getCardname());
                if (cards.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given card name not found");
                }
            }

            if (portDto.getPositionOnDevice() == 0 && portDto.getPositionOnCard() == 0 ||
                    portDto.getPositionOnDevice() != 0 && portDto.getPositionOnCard() != 0) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Please provide valid input");
            }
            if (portDto.getPortname() != null) {
                // Check if the port name is being updated
                if (!portDto.getPortname().equals(exPort.getPortname())) {
                    // Find the port by the new port name and device name
                    exPort = portRepo.findPortByDeviceNameAndPortName(portDto.getDeviceName(), portDto.getPortname());
                    if (exPort != null) {
                        // If the port already exists, return conflict
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("Given Port already found");
                    }
                }
                if (portDto.getPositionOnDevice() != 0) {
                    if (portDto.getPositionOnCard() != 0 || portDto.getCardslotname() != null || portDto.getCardname() != null
                            || portDto.getDeviceName() == null) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("Please provide valid input for portdevice");
                    }
                    if (!portDto.getPositionOnDevice().equals(exPortPositionOnDevice) || exPortPositionOnDevice == 0
                            || !existingportname.equals(portDto.getPortname())) {
                        Pluggable exPluggable = pluggableRepo.findByDeviceNameAndPositionOnDevice(portDto.getDeviceName(), portDto.getPositionOnDevice());
                        if (exPluggable != null) {
                            return ResponseEntity.status(HttpStatus.CONFLICT).body("Given position already assigned to a pluggable device");
                        }
                        exPort = portRepo.findByDeviceNameAndPositionOnDevice(portDto.getDeviceName(), portDto.getPositionOnDevice());
                        if (exPort != null) {
                            return ResponseEntity.status(HttpStatus.CONFLICT).body("Given position already assigned to another port");
                        }
                    }
                }

                if (portDto.getPositionOnCard() != 0) {
                    if (portDto.getPositionOnDevice() != 0 || portDto.getCardslotname() == null || portDto.getCardname() == null
                    ) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("Please provide valid input for port card");
                    }
                    if (portDto.getCardname() != null && !portDto.getCardname().equals(cardname) && portDto.getDeviceName() == null) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("Device name must be provided if card name is not null");
                    }
                    // Check if positionOnCard is updated
                    if (!portDto.getPositionOnCard().equals(exPortPositionOnCard)) {
                        Card exCard = cardRepo.findCardsByCardNameAndDeviceName(portDto.getCardname(), portDto.getDeviceName());
                        if (exCard == null) {
                            return ResponseEntity.status(HttpStatus.CONFLICT).body("Given card with device not found");
                        }
                        Long cardid = exCard.getCardid();
                        // Construct the full card slot name
                        updatedCardSlotName = portDto.getCardname() + cardid + "/" + portDto.getPositionOnCard();
                        // Check if a pluggable device or port already exists at the specified position
                        Pluggable exPluggable = pluggableRepo.findPortsByCardSlotNameAndPositionOnCard(updatedCardSlotName, portDto.getPositionOnCard());
                        if (exPluggable != null) {
                            return ResponseEntity.status(HttpStatus.CONFLICT).body("Given cardslot already found");
                        }
                        exPort = portRepo.findPortsByCardSlotNameAndPositionOnCard(updatedCardSlotName, portDto.getPositionOnCard());
                        if (exPort != null) {
                            return ResponseEntity.status(HttpStatus.CONFLICT).body("Given cardslot already found");
                        }
                    }
                }

                if (portDto.getPositionOnDevice() != 0 && portDto.getDeviceName() != null) {
                    success = portRepo.updatePortOnDevice(portid, portDto.getPortname(), portDto.getPositionOnDevice()
                            , portDto.getPortType(), portDto.getOperationalState(), portDto.getAdministrativeState(),
                            portDto.getUsageState(), portDto.getHref(), portDto.getPortSpeed(),
                            portDto.getCapacity(), portDto.getManagementIp(),
                            orderid, portDto.getDeviceName(), 0);
                } else {
                    success = portRepo.updatePortOnCard(portid, portDto.getPortname(), portDto.getPositionOnCard(), portDto.getPortType(), portDto.getOperationalState(),
                            portDto.getAdministrativeState(), portDto.getUsageState(), portDto.getHref(), portDto.getPortSpeed(),
                            cardname, updatedCardSlotName, portDto.getCapacity(), portDto.getManagementIp(),
                            orderid, portDto.getDeviceName(), 0);
                }

            }

            if (success == 1) {
                return ResponseEntity.ok("port updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to updated port");
            }
        } catch (DataAccessException dataAccessException) {
            // Handle data access exception (which may include SQL exceptions)
            dataAccessException.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Data access exception occurred: " + dataAccessException.getMessage());
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request: " + e.getMessage());
        }
    }

    @PostMapping("/updatePluggablebyid")
    public ResponseEntity<String> updatePluggablebyid(@RequestParam("plugableid") Long plugableid,
                                                      @RequestParam("orderid") Long orderid,
                                                      @RequestBody PluggableDTO portDto) {
        try {
            int success = 0;
            String updatedCardSlotName = null;
            Device exDevice = null;
            Optional<Pluggable> exPluggableOptional = pluggableRepo.findById(plugableid);
            if (exPluggableOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given Pluggable id not found");
            }
            Pluggable exPluggable = exPluggableOptional.get();
            String cardname = exPluggable.getCardname() == null ? "Na" : exPluggable.getCardname();
            String deviceName = exPluggable.getDevice() == null || exPluggable.getDevice().getDevicename() == null ? "Na" :
                    exPluggable.getDevice().getDevicename();
            String cardSlotName = exPluggable.getCardSlot() == null || exPluggable.getCardSlot().getName() == null ? "Na" :
                    exPluggable.getCardSlot().getName();
            String existingPluggableName = exPluggable.getPlugablename();
            int exPluggablePositionOnCard = exPluggable.getPositionOnCard();
            int exPluggablePositionOnDevice = exPluggable.getPositionOnDevice();

            if (portDto.getDeviceName() != null) {
                exDevice = deviceRepo.findByDevicename(portDto.getDeviceName());
                if (exDevice == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given device not found");
                }
            }
            Optional<Order> order = orderRepo.findById(orderid);
            if (order.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given order not found");
            }
            if (portDto.getCardname() != null && !cardname.equals(portDto.getCardname())) {
                List<Card> cards = cardRepo.findCards(portDto.getCardname());
                if (cards.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given card name not found");
                }
            }

            if (portDto.getPositionOnDevice() == 0 && portDto.getPositionOnCard() == 0 ||
                    portDto.getPositionOnDevice() != 0 && portDto.getPositionOnCard() != 0) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Please provide valid input");
            }
            if (portDto.getPlugablename() != null) {
                // Check if the port name is being updated
                if (!portDto.getPlugablename().equals(exPluggable.getPlugablename())) {
                    // Find the port by the new port name and device name
                    exPluggable = pluggableRepo.findByDeviceNameAndPluggableName(portDto.getDeviceName(), portDto.getPlugablename());
                    if (exPluggable != null) {
                        // If the port already exists, return conflict
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("Given Pluggable already found");
                    }
                }
                if (portDto.getPositionOnDevice() != 0) {
                    if (portDto.getPositionOnCard() != 0 || portDto.getCardSlotName() != null || portDto.getCardname() != null
                            || portDto.getDeviceName() == null) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("Please provide valid input for portdevice");
                    }
                    if (!portDto.getPositionOnDevice().equals(exPluggablePositionOnDevice) ||
                            exPluggablePositionOnDevice == 0 || !existingPluggableName.equals(portDto.getPlugablename())) {
                        exPluggable = pluggableRepo.findByDeviceNameAndPositionOnDevice(portDto.getDeviceName(), portDto.getPositionOnDevice());
                        if (exPluggable != null) {
                            return ResponseEntity.status(HttpStatus.CONFLICT).body("Given position already assigned to a pluggable device");
                        }
                        Port exPort = portRepo.findByDeviceNameAndPositionOnDevice(portDto.getDeviceName(), portDto.getPositionOnDevice());
                        if (exPort != null) {
                            return ResponseEntity.status(HttpStatus.CONFLICT).body("Given position already assigned to another port");
                        }
                    }
                }

                if (portDto.getPositionOnCard() != 0) {
                    if (portDto.getPositionOnDevice() != 0 || portDto.getCardSlotName() == null || portDto.getCardname() == null
                    ) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("Please provide valid input for port card");
                    }
                    if (portDto.getCardname() != null && !portDto.getCardname().equals(cardname) && portDto.getDeviceName() == null) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("Device name must be provided if card name is not null");
                    }
                    // Check if positionOnCard is updated
                    if (!portDto.getPositionOnCard().equals(exPluggablePositionOnCard)) {
                        Card exCard = cardRepo.findCardsByCardNameAndDeviceName(portDto.getCardname(), portDto.getDeviceName());
                        if (exCard == null) {
                            return ResponseEntity.status(HttpStatus.CONFLICT).body("Given card with device not found");
                        }
                        Long cardid = exCard.getCardid();
                        // Construct the full card slot name
                        updatedCardSlotName = portDto.getCardname() + cardid + "/" + portDto.getPositionOnCard();
                        // Check if a pluggable device or port already exists at the specified position
                        exPluggable = pluggableRepo.findPortsByCardSlotNameAndPositionOnCard(updatedCardSlotName, portDto.getPositionOnCard());
                        if (exPluggable != null) {
                            return ResponseEntity.status(HttpStatus.CONFLICT).body("Given cardslot already found");
                        }
                        Port exPort = portRepo.findPortsByCardSlotNameAndPositionOnCard(updatedCardSlotName, portDto.getPositionOnCard());
                        if (exPort != null) {
                            return ResponseEntity.status(HttpStatus.CONFLICT).body("Given cardslot already found");
                        }
                    }
                }

                if (portDto.getPositionOnDevice() != 0 && portDto.getDeviceName() != null) {
                    success = pluggableRepo.updatePluggableOnDevice(plugableid, portDto.getPlugablename(),
                            portDto.getPluggableModel(), portDto.getPluggablePartNumber(), portDto.getPositionOnDevice(),
                            portDto.getOperationalState(), portDto.getAdministrativeState(), portDto.getUsageState(),
                            portDto.getHref(), portDto.getManagementIp(), portDto.getVendor(), orderid,
                            portDto.getDeviceName(), 0);
                } else {
                    success = pluggableRepo.updatePluggableOnCard(plugableid, portDto.getPlugablename(),
                            portDto.getPluggableModel(), portDto.getPluggablePartNumber(), portDto.getPositionOnCard(),
                            portDto.getOperationalState(), portDto.getAdministrativeState(), portDto.getUsageState(),
                            portDto.getHref(), portDto.getManagementIp(), portDto.getVendor(), orderid,
                            portDto.getCardname(), updatedCardSlotName, portDto.getDeviceName(), 0);
                }
            }
            if (success == 1) {
                return ResponseEntity.ok("Pluggable updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to updated Pluggable");
            }
        } catch (DataAccessException dataAccessException) {
            // Handle data access exception (which may include SQL exceptions)
            dataAccessException.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Data access exception occurred: " + dataAccessException.getMessage());
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request: " + e.getMessage());
        }
    }

    @Autowired
    LogicalPortRepo logicalPortRepo;
    //TODO db based trigger update delete for addtional attribute

    @PostMapping("/CreateLogicalPortOnCard")
    public ResponseEntity<String> createLogicalPortOnCard(@RequestParam("id") Long id,
                                                          @RequestParam("device") String deviceName,
                                                          @RequestParam(name = "card") String cardName,
                                                          @RequestParam("orderid") Long orderid,
                                                          @RequestBody LogicalPortDTO logicalPortDTO,
                                                          @RequestParam("type") String type) {
        try {
            Long portid = null;
            Long plugableid = null;
            if (type.equals("Port")) {
                portid = id;
            } else if (type.equals("Pluggable")) {
                plugableid = id;
            }
            int success = 0;
            String lptype = type.equals("Port") ? "PORT_TO_LogicalPort" : "PLUGGABLE_TO_LogicalPort";
            List<String> A_A_V = new ArrayList<>();
            List<String> A_A_K = new ArrayList<>();

            Optional<Order> order = orderRepo.findById(orderid);
            if (order.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given order not found");
            }
            if (!deviceName.equals(logicalPortDTO.getDeviceName())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("must match device with Req param ");
            }

            Device exDevice = deviceRepo.findByDevicename(deviceName);
            if (exDevice == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given device not found");
            }

            Card exCard = cardRepo.findCardsByCardNameAndDeviceName(cardName, deviceName);
            if (exCard == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given Card not found");
            }
            if (logicalPortDTO.getPositionOnPort() == 0 && logicalPortDTO.getPositionOnCard() == 0
                    || logicalPortDTO.getPositionOnDevice() != 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("provide valid input");
            }

            Long cardid = exCard.getCardid();

            // Validate logical port name
            LogicalPort existingLogicalPort = logicalPortRepo.findLogicalPortsByDeviceNameAndLogicalPortName(
                    logicalPortDTO.getDeviceName(), logicalPortDTO.getName()
            );

            if (existingLogicalPort != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Logical port name already exists");
            }

            // Fetch existing port/pluggable based on type
            if (type.equals("Port")) {
                Port exPort = portRepo.findByDeviceNameAndCardIdAndPortId(deviceName, cardid, id);
                if (exPort == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given PortId not found");
                }

                exPort = portRepo.findByPositionOnCardAndPortId(logicalPortDTO.getPositionOnCard(), portid);
                if (exPort == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given position on card not found");
                }
                existingLogicalPort = logicalPortRepo.findLogicalPortByPortIdAndPositionOnPortAndDeviceName(
                        portid,
                        logicalPortDTO.getPositionOnPort(),
                        deviceName
                );
                if (existingLogicalPort != null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given port position on position on port " +
                            "occupied ");
                }

            } else if (type.equals("Pluggable")) {
                Pluggable exPluggable = pluggableRepo.findByDeviceNameAndCardIdAndPluggableId(deviceName, cardid, id);
                if (exPluggable == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given Pluggable not found");
                }

                exPluggable = pluggableRepo.findByPositionOnCardAndPluggableId(logicalPortDTO.getPositionOnCard(), plugableid);
                if (exPluggable == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given Pluggable position on card not found");
                }
                existingLogicalPort = logicalPortRepo.findLogicalPortByPlugableIdAndPositionOnPortAndDeviceName(
                        plugableid,
                        logicalPortDTO.getPositionOnPort(),
                        deviceName
                );
                if (existingLogicalPort != null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given Pluggable position on position on port " +
                            "occupied ");
                }

            } else {
                throw new IllegalArgumentException("Invalid type selected");
            }
            if (logicalPortDTO.getAdditionalAttribute() != null && !logicalPortDTO.getAdditionalAttribute().isEmpty()) {
                for (AdditionalAttribute additionalAttributeDTO : logicalPortDTO.getAdditionalAttribute()) {
                    A_A_K.add(additionalAttributeDTO.getKey());
                    A_A_V.add(additionalAttributeDTO.getValue());
                }
            }
            success = logicalPortRepo.insertLogicalPortOnCard(logicalPortDTO.getName(), logicalPortDTO.getPositionOnCard(),
                    0, lptype, logicalPortDTO.getOperationalState(), logicalPortDTO.getAdministrativeState(),
                    logicalPortDTO.getUsageState(), logicalPortDTO.getHref(), logicalPortDTO.getPortSpeed(),
                    logicalPortDTO.getCapacity(), logicalPortDTO.getPositionOnPort(), logicalPortDTO.getManagementIp(),
                    logicalPortDTO.getDeviceName(), orderid, plugableid, portid, 0, A_A_K.toArray(new String[0]),
                    A_A_V.toArray(new String[0]));
            if (success == 1) {
                return ResponseEntity.ok("Logical port created  successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to updated Pluggable");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/CreateLogicalPortOnDevice")
    public ResponseEntity<String> createLogicalPortOnDevice(@RequestParam("id") Long id,
                                                            @RequestParam("device") String deviceName,
                                                            @RequestParam("orderid") Long orderid,
                                                            @RequestBody LogicalPortDTO logicalPortDTO,
                                                            @RequestParam("type") String type) {
        try {
            Long portid = null;
            Long plugableid = null;
            if (type.equals("Port")) {
                portid = id;
            } else if (type.equals("Pluggable")) {
                plugableid = id;
            }
            int success = 0;
            String lptype = type.equals("Port") ? "PORT_TO_LogicalPort" : "PLUGGABLE_TO_LogicalPort";
            List<String> A_A_V = new ArrayList<>();
            List<String> A_A_K = new ArrayList<>();

            Optional<Order> order = orderRepo.findById(orderid);
            if (order.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given order not found");
            }

            // Ensure device name matches the one in the request body
            if (!deviceName.equals(logicalPortDTO.getDeviceName())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Device name in the request doesn't match.");
            }

            Device exDevice = deviceRepo.findByDevicename(deviceName);
            if (exDevice == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given device not found");
            }

            // Validate logical port name
            LogicalPort existingLogicalPort = logicalPortRepo.findLogicalPortsByDeviceNameAndLogicalPortName(
                    logicalPortDTO.getDeviceName(), logicalPortDTO.getName()
            );
            if (existingLogicalPort != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Logical port name already exists");
            }

            // Fetch existing port/pluggable based on type
            if (type.equals("Port")) {
                Port exPort = portRepo.findPortByPortIdAndPositionOnDeviceAndDeviceName(portid, logicalPortDTO.getPositionOnDevice(),
                        logicalPortDTO.getDeviceName());
                if (exPort == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given Port id not found");
                }
                //Todo Change logical port position based one Device no neeed check port id
                existingLogicalPort = logicalPortRepo.findLogicalPortByPortIdAndPositionOnPortAndDeviceName(
                        portid, logicalPortDTO.getPositionOnPort(), deviceName);
                if (existingLogicalPort != null) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Port position on position on port is already occupied");
                }

            } else if (type.equals("Pluggable")) {
                Pluggable exPluggable = pluggableRepo.findPluggableByIdAndPositionOnDeviceAndDeviceName(plugableid,
                        logicalPortDTO.getPositionOnDevice(), logicalPortDTO.getDeviceName());
                if (exPluggable == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given Pluggable not found");
                }
                existingLogicalPort = logicalPortRepo.findLogicalPortByPlugableIdAndPositionOnPortAndDeviceName(
                        plugableid, logicalPortDTO.getPositionOnPort(), deviceName
                );
                if (existingLogicalPort != null) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Pluggable position on position on port is already occupied");
                }
            } else {
                throw new IllegalArgumentException("Invalid type selected");
            }

            // Populate additional attributes if provided
            if (logicalPortDTO.getAdditionalAttribute() != null && !logicalPortDTO.getAdditionalAttribute().isEmpty()) {
                for (AdditionalAttribute additionalAttributeDTO : logicalPortDTO.getAdditionalAttribute()) {
                    A_A_K.add(additionalAttributeDTO.getKey());
                    A_A_V.add(additionalAttributeDTO.getValue());
                }
            }

            // Call the repository method to insert the logical port
            success = logicalPortRepo.insertLogicalPortOnCard(logicalPortDTO.getName(), 0,
                    logicalPortDTO.getPositionOnDevice(), lptype, logicalPortDTO.getOperationalState(), logicalPortDTO.getAdministrativeState(),
                    logicalPortDTO.getUsageState(), logicalPortDTO.getHref(), logicalPortDTO.getPortSpeed(),
                    logicalPortDTO.getCapacity(), logicalPortDTO.getPositionOnPort(), logicalPortDTO.getManagementIp(),
                    logicalPortDTO.getDeviceName(), orderid, plugableid, portid, 0, A_A_K.toArray(new String[0]),
                    A_A_V.toArray(new String[0]));

            if (success == 1) {
                return ResponseEntity.ok("Logical port created successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update Pluggable");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


    @GetMapping("/getLogicalPort")
    public Pluggable getLogicalPort(@RequestParam(name = "devicename") String devicename,
                                    @RequestParam(name = "position_on_device") int position) {
        Pluggable logicalPort = new Pluggable();
        try {
            logicalPort = pluggableRepo.findByDeviceNameAndPositionOnDevice(devicename, position);
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return logicalPort;
    }


    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/search")
    public JsonNode search(@RequestParam String tableName, @RequestParam String columnName,
                           @RequestParam String searchTerm, @RequestParam(defaultValue = "contains") String filterType) {
        try {
            JsonNode result = globalSearch(tableName, columnName, searchTerm, filterType);
            if (result == null || result.isEmpty()) {
                return objectMapper.createObjectNode().put("empty", true);
            } else {
                return result;
            }
        } catch (SQLException | JsonProcessingException e) {
            throw new RuntimeException("An error occurred while processing the request.", e);
        }
    }

    public JsonNode globalSearch(String tableName, String columnName, String searchTerm, String filterType) throws
            SQLException, JsonProcessingException {
        String query = "SELECT global_search(?, ?, ?, ?)";
        String jsonString = jdbcTemplate.queryForObject(query, new Object[]{tableName, columnName, searchTerm, filterType}, String.class);
        return objectMapper.readTree(jsonString);
    }

    private List<Integer> AvilableShef(@RequestParam("Name") String Name) {

        return null;
    }
}


//TODO delete device and delete shelf need todo
//TODO shelf to slot need to create card
//TODO update shelf api need create
//

