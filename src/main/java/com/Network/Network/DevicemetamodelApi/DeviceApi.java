package com.Network.Network.DevicemetamodelApi;

import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.Network.Network.DevicemetamodelPojo.*;
import com.Network.Network.DevicemetamodelRepo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Autowired
    private CarslotRepo carslotRepo;
    @Autowired
    private PhysicalConnectionRepo physicalConnectionRepo;
    @Autowired
    private LogicalConnectionRepo logicalConnectionRepo;
    Logger logger = LoggerFactory.getLogger(DeviceApi.class);


    @PostMapping("/insertDeviceOnBuilding")
    public ResponseEntity<String> insertDeviceOnBuilding(@RequestParam("BuildingName") String building,
                                                         @RequestParam("orderid") Long orderid, @RequestBody DeviceDto dto) {
        building = building.toLowerCase();
        try {
            Building exBuilding = buildingRepo.findByBuildingName(building);
            Device device = deviceRepo.findByDevicename(dto.getDevicename().toLowerCase());
            Optional<Order> order = orderRepo.findById(orderid);
            Order exOrder = order.orElse(null); // Using orElse to handle the optional
            DeviceMetaModel deviceMetaModel = deviceMetaModelRepo.findByDeviceModel(dto.getDeviceModel().toLowerCase());
            List<String> A_A_V = new ArrayList<>();
            List<String> A_A_K = new ArrayList<>();

            if (exBuilding == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given building not found");
            }
            if (!order.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given order not found");
            }
            if (device != null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given device Already found");
            }
            if (deviceMetaModel == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given device metamodel not found");
            }
            if (dto.getAdditionalAttributes() != null && !dto.getAdditionalAttributes().isEmpty()) {
                for (AdditionalAttribute additionalAttributeDTO : dto.getAdditionalAttributes()) {
                    A_A_K.add(additionalAttributeDTO.getKey());
                    A_A_V.add(additionalAttributeDTO.getValue());
                }
            }
            //TODO with Naming Converion Country_State_city_buildingName

            String customer = exOrder.getCustomer() != null ? exOrder.getCustomer().getCustomername() : "NA";
            int success = deviceRepo.insertDevice(dto.getDevicename().toLowerCase(), dto.getDeviceModel().toLowerCase(), dto.getLocation(),
                    dto.getOrganisation(), customer, dto.getManagementIp(), dto.getRackPosition(),
                    dto.getOperationalState(), dto.getAdministrativeState(), dto.getUsageState(), dto.getSerialNumber(),
                    dto.getHref(), building, orderid, "BUILDING_TO_DEVICE", A_A_K.toArray(new String[0]),
                    A_A_V.toArray(new String[0]), 0);

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
            List<String> A_A_V = new ArrayList<>();
            List<String> A_A_K = new ArrayList<>();

            String newDeviceName = (deviceDto.getDevicename() != null) ? deviceDto.getDevicename() : device.getDevicename();
            String deviceModel = (deviceDto.getDeviceModel() != null) ? deviceDto.getDeviceModel() : device.getDeviceModel();

            // Fetch the existing device's building and order details
            String buildingName = (deviceDto.getBuildingname() != null) ? deviceDto.getBuildingname() : device.getBuilding().getBuildingName();
            Long orderId = (deviceDto.getOrderid() != null) ? deviceDto.getOrderid() : device.getOrder().getId();

            // Fetch the customer details from existing order
            Order existingOrder = orderRepo.findById(orderId).orElse(null);
            // String customer = (deviceDto.getCustomer() != null && existingOrder != null) ? deviceDto.getCustomer() : existingOrder.getCustomer().getCustomername();
            String customer = existingOrder.getCustomer() != null ? existingOrder.getCustomer().getCustomername() : "Na";
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
            if (device.getBuilding().getBuildingName() != deviceDto.getBuildingname()) {
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
            if (deviceDto.getDeviceModel() != device.getDeviceModel()) {
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
            if (deviceDto.getAdditionalAttributes() != null && !deviceDto.getAdditionalAttributes().isEmpty()) {
                for (AdditionalAttribute additionalAttributeDTO : deviceDto.getAdditionalAttributes()) {
                    A_A_K.add(additionalAttributeDTO.getKey());
                    A_A_V.add(additionalAttributeDTO.getValue());
                }
            }

            int success = deviceRepo.updateDevice(administrativeState, credentials, customer, deviceModel,
                    newDeviceName, href, location, managementIp, operationalState, organisation, pollInterval,
                    rackPosition, device.getRealtion(), serialNumber, usageState, buildingName, accessKey,
                    orderId, device.getId(), A_A_K.toArray(new String[0]),
                    A_A_V.toArray(new String[0]), 0);
            if (success == 1) {
                return ResponseEntity.ok("Device updated successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error occurred while updating device.");
            }
            //TODO relation might if Device update in rack "RACK-TO-DEVICE"
        } catch (Exception e) {
            if (e instanceof DataAccessException) {
                // Handle data access exception (which may include SQL exceptions)
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Data access exception occurred: " + e.getMessage());
            } else {
                // Handle other exceptions
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while updating device.");
            }
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
            List<String> A_A_V = new ArrayList<>();
            List<String> A_A_K = new ArrayList<>();
            if (cardDto.getAdditionalAttributes() != null && !cardDto.getAdditionalAttributes().isEmpty()) {
                for (AdditionalAttribute additionalAttributeDTO : cardDto.getAdditionalAttributes()) {
                    A_A_K.add(additionalAttributeDTO.getKey());
                    A_A_V.add(additionalAttributeDTO.getValue());
                }
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
                    orderid,
                    A_A_K.toArray(new String[0]),
                    A_A_V.toArray(new String[0]),
                    0
            );
            if (success == 1) {
                return ResponseEntity.ok("Card created successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create card");
            }
        } catch (Exception e) {
            if (e instanceof DataAccessException) {
                // Handle data access exception (which may include SQL exceptions)
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Data access exception occurred: " + e.getMessage());
            } else {
                // Handle other exceptions
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while Creating Card.");
            }
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
            List<String> A_A_V = new ArrayList<>();
            List<String> A_A_K = new ArrayList<>();
            if (cardDto.getAdditionalAttributes() != null && !cardDto.getAdditionalAttributes().isEmpty()) {
                for (AdditionalAttribute additionalAttributeDTO : cardDto.getAdditionalAttributes()) {
                    A_A_K.add(additionalAttributeDTO.getKey());
                    A_A_V.add(additionalAttributeDTO.getValue());
                }
            }
            // Call the repository method to update the card
            int success = cardRepo.updateCard(cardId, updatedCardName, updatedDeviceName, shelfPosition, slotPosition, vendor,
                    cardModel, cardPartNumber, operationalState, administrativeState, usageState, href,
                    orderId, slotId, A_A_K.toArray(new String[0]),
                    A_A_V.toArray(new String[0]), 0);

            // Check the success flag returned by the repository method
            if (success == 1) {
                return ResponseEntity.ok("Card updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update card");
            }

        } catch (Exception e) {
            if (e instanceof DataAccessException) {
                // Handle data access exception (which may include SQL exceptions)
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Data access exception occurred: " + e.getMessage());
            } else {
                // Handle other exceptions
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while Creating Card.");
            }
        }
    }
//TODO passsing Device Name get card pass cardid

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
            List<String> A_A_V = new ArrayList<>();
            List<String> A_A_K = new ArrayList<>();
            if (portDTO.getAdditionalAttributes() != null && !portDTO.getAdditionalAttributes().isEmpty()) {
                for (AdditionalAttribute additionalAttributeDTO : portDTO.getAdditionalAttributes()) {
                    A_A_K.add(additionalAttributeDTO.getKey());
                    A_A_V.add(additionalAttributeDTO.getValue());
                }
            }
            int success = portRepo.insertPort(portDTO.getPortname(), portDTO.getPositionOnCard(), 0,
                    portDTO.getOperationalState(), portDTO.getAdministrativeState(), portDTO.getUsageState(),
                    portDTO.getHref(), portDTO.getPortSpeed(), portDTO.getCapacity(), portDTO.getManagementIp(),
                    portDTO.getRelation(), cardName, cardslotname, orderid, null, cardid, A_A_K.toArray(new String[0]),
                    A_A_V.toArray(new String[0]), 0);
            // Check the success flag returned by the repository method
            if (success == 1) {
                return ResponseEntity.ok("port created successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create port");
            }
        } catch (Exception e) {
            if (e instanceof DataAccessException) {
                // Handle data access exception (which may include SQL exceptions)
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Data access exception occurred: " + e.getMessage());
            } else {
                // Handle other exceptions
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while Creating Card.");
            }
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
            List<String> A_A_V = new ArrayList<>();
            List<String> A_A_K = new ArrayList<>();
            if (portDTO.getAdditionalAttributes() != null && !portDTO.getAdditionalAttributes().isEmpty()) {
                for (AdditionalAttribute additionalAttributeDTO : portDTO.getAdditionalAttributes()) {
                    A_A_K.add(additionalAttributeDTO.getKey());
                    A_A_V.add(additionalAttributeDTO.getValue());
                }
            }
            int success = portRepo.insertDevicePort(portDTO.getPortname(), portDTO.getPositionOnDevice(),
                    portDTO.getOperationalState(), portDTO.getAdministrativeState(), portDTO.getUsageState(),
                    portDTO.getHref(), portDTO.getPortSpeed(), portDTO.getCapacity(), portDTO.getManagementIp(),
                    portDTO.getRelation(), orderid, devicename, A_A_K.toArray(new String[0]),
                    A_A_V.toArray(new String[0]), 0);

            if (success == 1) {
                return ResponseEntity.ok("Port created successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create port");
            }
        } catch (Exception e) {
            if (e instanceof DataAccessException) {
                // Handle data access exception (which may include SQL exceptions)
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Data access exception occurred: " + e.getMessage());
            } else {
                // Handle other exceptions
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while Creating Card.");
            }
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
            List<String> A_A_V = new ArrayList<>();
            List<String> A_A_K = new ArrayList<>();
            if (pluggableDTO.getAdditionalAttributes() != null && !pluggableDTO.getAdditionalAttributes().isEmpty()) {
                for (AdditionalAttribute additionalAttributeDTO : pluggableDTO.getAdditionalAttributes()) {
                    A_A_K.add(additionalAttributeDTO.getKey());
                    A_A_V.add(additionalAttributeDTO.getValue());
                }
            }
            int success = pluggableRepo.insertPluggable(pluggableDTO.getPlugablename(), pluggableDTO.getPositionOnCard(),
                    0, pluggableDTO.getVendor(), pluggableDTO.getPluggableModel(), pluggableDTO.getPluggablePartNumber(),
                    pluggableDTO.getOperationalState(), pluggableDTO.getAdministrativeState(), pluggableDTO.getUsageState(),
                    pluggableDTO.getHref(), pluggableDTO.getManagementIp(), pluggableDTO.getRelation(), cardName, cardslotname,
                    orderid, null, cardid, A_A_K.toArray(new String[0]),
                    A_A_V.toArray(new String[0]), 0);
            if (success == 1) {
                return ResponseEntity.ok("Pluggable created successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create Pluggable");
            }

        } catch (Exception e) {
            if (e instanceof DataAccessException) {
                // Handle data access exception (which may include SQL exceptions)
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Data access exception occurred: " + e.getMessage());
            } else {
                // Handle other exceptions
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while Creating Card.");
            }
        }
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
            List<String> A_A_V = new ArrayList<>();
            List<String> A_A_K = new ArrayList<>();
            if (pluggableDTO.getAdditionalAttributes() != null && !pluggableDTO.getAdditionalAttributes().isEmpty()) {
                for (AdditionalAttribute additionalAttributeDTO : pluggableDTO.getAdditionalAttributes()) {
                    A_A_K.add(additionalAttributeDTO.getKey());
                    A_A_V.add(additionalAttributeDTO.getValue());
                }
            }
            int success = pluggableRepo.insertPluggabledevice(pluggableDTO.getPlugablename(), 0,
                    pluggableDTO.getPositionOnDevice(), pluggableDTO.getVendor(), pluggableDTO.getPluggableModel(),
                    pluggableDTO.getPluggablePartNumber(), pluggableDTO.getOperationalState(),
                    pluggableDTO.getAdministrativeState(), pluggableDTO.getUsageState(), pluggableDTO.getHref(),
                    pluggableDTO.getManagementIp(), orderid, devicename, A_A_K.toArray(new String[0]),
                    A_A_V.toArray(new String[0]), 0);
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
            String cardname = exPort.getCardname() == null ? portDto.getCardname() : exPort.getCardname();
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
                    Card exCard = cardRepo.findCardsByCardNameAndDeviceName(portDto.getCardname(), portDto.getDeviceName());
                    if (exCard == null) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("Given card with device not found");
                    }
                    Long cardid = exCard.getCardid();
                    // Construct the full card slot name
                    updatedCardSlotName = portDto.getCardname() + cardid + "/" + portDto.getPositionOnCard();
                    // Check if positionOnCard is updated
                    if (!portDto.getPositionOnCard().equals(exPortPositionOnCard)) {
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
                List<String> A_A_V = new ArrayList<>();
                List<String> A_A_K = new ArrayList<>();
                if (portDto.getAdditionalAttributes() != null && !portDto.getAdditionalAttributes().isEmpty()) {
                    for (AdditionalAttribute additionalAttributeDTO : portDto.getAdditionalAttributes()) {
                        A_A_K.add(additionalAttributeDTO.getKey());
                        A_A_V.add(additionalAttributeDTO.getValue());
                    }
                }
                if (portDto.getPositionOnDevice() != 0 && portDto.getDeviceName() != null) {
                    success = portRepo.updatePortOnDevice(portid, portDto.getPortname(), portDto.getPositionOnDevice()
                            , portDto.getPortType(), portDto.getOperationalState(), portDto.getAdministrativeState(),
                            portDto.getUsageState(), portDto.getHref(), portDto.getPortSpeed(),
                            portDto.getCapacity(), portDto.getManagementIp(),
                            orderid, portDto.getDeviceName(), A_A_K.toArray(new String[0]),
                            A_A_V.toArray(new String[0]), 0);
                } else {
                    success = portRepo.updatePortOnCard(portid, portDto.getPortname(), portDto.getPositionOnCard(), portDto.getPortType(), portDto.getOperationalState(),
                            portDto.getAdministrativeState(), portDto.getUsageState(), portDto.getHref(), portDto.getPortSpeed(),
                            cardname, updatedCardSlotName, portDto.getCapacity(), portDto.getManagementIp(),
                            orderid, portDto.getDeviceName(), A_A_K.toArray(new String[0]),
                            A_A_V.toArray(new String[0]), 0);
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
    //Name suppose not enter autmatice Genarate using slot card and after Name need To Sepfic position based in insertion append

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
            String cardname = exPluggable.getCardname() == null ? portDto.getCardname() : exPluggable.getCardname();
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
                    Card exCard = cardRepo.findCardsByCardNameAndDeviceName(portDto.getCardname(), portDto.getDeviceName());
                    if (exCard == null) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("Given card with device not found");
                    }
                    Long cardid = exCard.getCardid();
                    // Construct the full card slot name
                    updatedCardSlotName = portDto.getCardname() + cardid + "/" + portDto.getPositionOnCard();
                    // Check if positionOnCard is updated
                    if (!portDto.getPositionOnCard().equals(exPluggablePositionOnCard)) {
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
                List<String> A_A_V = new ArrayList<>();
                List<String> A_A_K = new ArrayList<>();
                if (portDto.getAdditionalAttributes() != null && !portDto.getAdditionalAttributes().isEmpty()) {
                    for (AdditionalAttribute additionalAttributeDTO : portDto.getAdditionalAttributes()) {
                        A_A_K.add(additionalAttributeDTO.getKey());
                        A_A_V.add(additionalAttributeDTO.getValue());
                    }
                }
                if (portDto.getPositionOnDevice() != 0 && portDto.getDeviceName() != null) {
                    success = pluggableRepo.updatePluggableOnDevice(plugableid, portDto.getPlugablename(),
                            portDto.getPluggableModel(), portDto.getPluggablePartNumber(), portDto.getPositionOnDevice(),
                            portDto.getOperationalState(), portDto.getAdministrativeState(), portDto.getUsageState(),
                            portDto.getHref(), portDto.getManagementIp(), portDto.getVendor(), orderid,
                            portDto.getDeviceName(), A_A_K.toArray(new String[0]),
                            A_A_V.toArray(new String[0]), 0);
                } else {
                    success = pluggableRepo.updatePluggableOnCard(plugableid, portDto.getPlugablename(),
                            portDto.getPluggableModel(), portDto.getPluggablePartNumber(), portDto.getPositionOnCard(),
                            portDto.getOperationalState(), portDto.getAdministrativeState(), portDto.getUsageState(),
                            portDto.getHref(), portDto.getManagementIp(), portDto.getVendor(), orderid,
                            portDto.getCardname(), updatedCardSlotName, portDto.getDeviceName(), A_A_K.toArray(new String[0]),
                            A_A_V.toArray(new String[0]), 0);
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
                existingLogicalPort = logicalPortRepo.findLogicalPortByPositionvalidate(logicalPortDTO.getPositionOnPort(),
                        logicalPortDTO.getDeviceName());
                if (existingLogicalPort != null) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(" position Port is already occupied");
                }

            } else if (type.equals("Pluggable")) {
                Pluggable exPluggable = pluggableRepo.findPluggableByIdAndPositionOnDeviceAndDeviceName(plugableid,
                        logicalPortDTO.getPositionOnDevice(), logicalPortDTO.getDeviceName());
                if (exPluggable == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Given Pluggable not found");
                }
                existingLogicalPort = logicalPortRepo.findLogicalPortByPositionvalidate(logicalPortDTO.getPositionOnPort(),
                        logicalPortDTO.getDeviceName());
                if (existingLogicalPort != null) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Position on port is already occupied");
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
                           @RequestParam String searchTerm, @RequestParam String typedata,
                           @RequestParam(defaultValue = "contains") String filterType) {
        try {
            JsonNode result = globalSearch(tableName, columnName, searchTerm, typedata, filterType);
            if (result == null || result.isEmpty()) {
                return objectMapper.createObjectNode().put("empty", true);
            } else {
                return result;
            }
        } catch (SQLException | JsonProcessingException e) {
            throw new RuntimeException("An error occurred while processing the request.", e);
        }
    }

    public JsonNode globalSearch(String tableName, String columnName, String searchTerm, String typedata, String filterType) throws
            SQLException, JsonProcessingException {
        String query = "SELECT global_search(?, ?, ?, ?, ?)";
        String jsonString = jdbcTemplate.queryForObject(query, new Object[]{tableName, columnName, searchTerm, typedata, filterType}, String.class);
        return objectMapper.readTree(jsonString);
    }

    private List<Integer> AvilableShef(@RequestParam("Name") String Name) {

        return null;
    }

    @GetMapping("/getDeviceByName")
    public Device getDeviceByName(@RequestParam("DeviceName") String name) {
        Device response = new Device();
        try {
            Optional<Device> deviceOptional = deviceRepo.findAll().stream()
                    .filter(device -> device.getDevicename().equals(name.toLowerCase()))
                    .findFirst();
            if (deviceOptional.isPresent()) {
                response = deviceOptional.get();
            } else {
                appExceptionHandler.raiseException("Given Device Not Found" + name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @GetMapping("/getDevicesInBuilding")
    public List<Device> getDevicesInBuilding(@RequestParam("BuildingName") String name) {
        List<Device> response = new ArrayList<>();
        try {
            Building building = buildingRepo.findByBuildingName(name.toLowerCase());
            if (building == null) {
                appExceptionHandler.raiseException("Given Building is not found: " + name);
            }
            response = deviceRepo.findAll().stream()
                    .filter(device -> device.getBuilding().getBuildingName().equalsIgnoreCase(name))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @GetMapping("/getCard")
    public List<Card> getCard(@RequestParam(name = "cardName") String name) {
        List<Card> response = new ArrayList<>();
        try {
            response = cardRepo.findAll().stream().filter(card -> card.getId().getCardname().
                            equalsIgnoreCase(name.toLowerCase())).
                    collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @GetMapping("/getAllCardsOnDevice")
    public List<Card> getAllCardsOnDevice(@RequestParam(name = "DeviceName") String name) {
        List<Card> response = new ArrayList<>();
        try {
            response = cardRepo.findAll().stream().filter(card -> card.getId().getDevice().getDevicename().
                    equalsIgnoreCase(name.toLowerCase())).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @GetMapping("/getCardInDevice")
    public Card getCardInDevice(@RequestParam("cardName") String cardName, @RequestParam("DeviceName") String deviceName) {
        Card response = new Card();
        try {
            Optional<Device> deviceOptional = deviceRepo.findAll().stream()
                    .filter(device -> device.getDevicename().equals(deviceName.toLowerCase()))
                    .findFirst();
            if (!deviceOptional.isPresent()) {
                appExceptionHandler.raiseException("Device not found: " + deviceName);
            }
            Optional<Card> cardOptional = cardRepo.findAll().stream()
                    .filter(card -> card.getId().getCardname().equalsIgnoreCase(cardName.toLowerCase()) &&
                            card.getId().getDevice().getDevicename().equalsIgnoreCase(deviceName.toLowerCase()))
                    .findFirst();
            if (!cardOptional.isPresent()) {
                appExceptionHandler.raiseException("Card not found: " + cardName + " in device: " + deviceName);
            }
            response = cardOptional.get();
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @GetMapping("/getShelfByName")
    public Shelf getShelfByName(@RequestParam("ShelfName") String name) {
        Shelf response = new Shelf();
        try {

            Optional<Shelf> shelfOptional = shelfRepo.findAll().stream()
                    .filter(shelf -> shelf.getName().equals(name.toLowerCase()))
                    .findFirst();
            if (shelfOptional.isPresent()) {
                response = shelfOptional.get();
            } else {
                appExceptionHandler.raiseException("Given Shelf Not Found" + name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @GetMapping("/getShelfSOnDevice")
    public List<Shelf> getShelfSOnDevice(@RequestParam(name = "DeviceName") String DeviceName) {
        List<Shelf> response = new ArrayList<>();
        try {
            Optional<Device> deviceOptional = deviceRepo.findAll().stream()
                    .filter(device -> device.getDevicename().equals(DeviceName.toLowerCase()))
                    .findFirst();
            if (!deviceOptional.isPresent()) {
                appExceptionHandler.raiseException("Device not found: " + DeviceName.toLowerCase());
            }
            response = shelfRepo.findAll().stream().filter(shelf -> shelf.getDevice().getDevicename()
                    .equals(DeviceName.toLowerCase())).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @GetMapping("/getSlotSOnShelf")
    public List<Slot> getSlotSOnShelf(@RequestParam(name = "ShelfName") String ShelfName) {
        List<Slot> response = new ArrayList<>();
        try {
            Optional<Shelf> shelfOptional = shelfRepo.findAll().stream()
                    .filter(shelf -> shelf.getName().equals(ShelfName.toLowerCase()))
                    .findFirst();
            if (!shelfOptional.isPresent()) {
                appExceptionHandler.raiseException("Shelf not found: " + ShelfName.toLowerCase());
            }
            response = slotRepo.findAll().stream().filter(shelf -> shelf.getShelf().getName()
                    .equals(ShelfName.toLowerCase())).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @GetMapping("/getCardSlotSOnCardId")
    public List<CardSlot> getCardSlotSOnCardId(@RequestParam(name = "CardName") String cardName,
                                               @RequestParam(name = "DeviceName") String deviceName) {
        List<CardSlot> response = new ArrayList<>();
        try {
            Optional<Card> cardOptional = cardRepo.findAll().stream()
                    .filter(card -> card.getId().getCardname().equals(cardName.toLowerCase()))
                    .findFirst();
            if (!cardOptional.isPresent()) {
                appExceptionHandler.raiseException("Card not found: " + cardName.toLowerCase());
            }
            List<Long> cardIds = cardRepo.findAll().stream()
                    .filter(card -> card.getId().getCardname().equals(cardName.toLowerCase())
                            && card.getId().getDevice().getDevicename().equals(deviceName.toLowerCase()))
                    .map(Card::getCardid)
                    .collect(Collectors.toList());

            if (cardIds.isEmpty()) {
                appExceptionHandler.raiseException("Given card with Device not found: " +
                        cardName.toLowerCase() + deviceName.toLowerCase());
            }
            response = carslotRepo.findAll().stream()
                    .filter(cardSlot -> cardIds.contains(cardSlot.getCard().getCardid()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @GetMapping("/getCardSlotSOnCard")
    public List<CardSlot> getCardSlotSOnCard(@RequestParam(name = "CardName") String cardName) {
        List<CardSlot> response = new ArrayList<>();
        try {
            Optional<Card> cardOptional = cardRepo.findAll().stream()
                    .filter(card -> card.getId().getCardname().equals(cardName.toLowerCase()))
                    .findFirst();
            if (!cardOptional.isPresent()) {
                appExceptionHandler.raiseException("Card not found: " + cardName.toLowerCase());
            }
            response = carslotRepo.findAll().stream()
                    .filter(cardSlot -> cardSlot.getCardname().equals(cardName.toLowerCase()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @GetMapping("/getCardsOnSlots")
    public List<Card> getCardsOnSlots(@RequestParam(name = "SlotName") String SlotName) {
        List<Card> response = new ArrayList<>();
        try {
            Optional<Slot> SlotOptional = slotRepo.findAll().stream()
                    .filter(slot -> slot.getSlotname().equals(SlotName.toLowerCase()))
                    .findFirst();
            if (!SlotOptional.isPresent()) {
                appExceptionHandler.raiseException("SlotName not found: " + SlotName.toLowerCase());
            }
            response = cardRepo.findAll().stream()
                    .filter(card -> card.getSlot().getSlotname().equals(SlotName.toLowerCase()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @GetMapping("/getPortsByName")
    public List<Port> getPortsByName(@RequestParam("PortName") String PortName) {
        List<Port> response = new ArrayList<>();
        try {
            response = portRepo.findAll().stream()
                    .filter(port -> port.getPortname().equals(PortName.toLowerCase()))
                    .collect(Collectors.toList());
            if (response.isEmpty()) {
                appExceptionHandler.raiseException("Given Port Name Not Found" + PortName.toLowerCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @GetMapping("/getPluggableByName")
    public List<Pluggable> getPluggableByName(@RequestParam("PluggableName") String PluggableName) {
        List<Pluggable> response = new ArrayList<>();
        try {
            response = pluggableRepo.findAll().stream()
                    .filter(pluggable -> pluggable.getPlugablename().equals
                            (PluggableName.toLowerCase()))
                    .collect(Collectors.toList());
            if (response.isEmpty()) {
                appExceptionHandler.raiseException("Given Pluggable Name Not Found" + PluggableName.toLowerCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @GetMapping("/getPortsByDeviceName")
    public List<Port> getPortsByDeviceName(@RequestParam("DeviceName") String DeviceName,
                                           @RequestParam("CardName") String CardName) {
        List<Port> response = new ArrayList<>();
        try {
            List<Card> cards = cardRepo.findAll();
            Optional<Device> deviceOptional = deviceRepo.findAll().stream()
                    .filter(device -> device.getDevicename().equals(DeviceName.toLowerCase()))
                    .findFirst();
            if (!deviceOptional.isPresent()) {
                appExceptionHandler.raiseException("Device not found: " + DeviceName.toLowerCase());
            }
            Optional<Card> cardOptional = cards.stream()
                    .filter(card -> card.getId().getCardname().equals(CardName.toLowerCase()))
                    .findFirst();
            if (!cardOptional.isPresent()) {
                appExceptionHandler.raiseException("Card not found: " + CardName.toLowerCase());
            }
            List<Long> CardID = cardRepo.findAll().stream()
                    .filter(card -> card.getId().getCardname().equals(CardName.toLowerCase()
                    ) && (card.getId().getDevice().getDevicename().equals(DeviceName.toLowerCase())))
                    .map(Card::getCardid).collect(Collectors.toList());

            List<String> cardSlotNames = carslotRepo.findAll().stream()
                    .filter(cardSlot -> CardID.contains(cardSlot.getCard().getCardid()))
                    .map(CardSlot::getName)
                    .collect(Collectors.toList());
            response = portRepo.findPortsByDeviceNameAndCardSlotNames(DeviceName.toLowerCase(), cardSlotNames);
            if (response.isEmpty()) {
                appExceptionHandler.raiseException("Given Device and Card Have No Port Found: " +
                        DeviceName.toLowerCase() + " " + CardName.toLowerCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @GetMapping("/getPluggablesByDeviceName")
    public List<Pluggable> getPluggablesByDeviceName(@RequestParam("DeviceName") String DeviceName,
                                                     @RequestParam("CardName") String CardName) {
        List<Pluggable> response = new ArrayList<>();
        try {
            List<Card> cards = cardRepo.findAll();
            Optional<Device> deviceOptional = deviceRepo.findAll().stream()
                    .filter(device -> device.getDevicename().equals(DeviceName.toLowerCase()))
                    .findFirst();
            if (!deviceOptional.isPresent()) {
                appExceptionHandler.raiseException("Device not found: " + DeviceName.toLowerCase());
            }
            Optional<Card> cardOptional = cards.stream()
                    .filter(card -> card.getId().getCardname().equals(CardName.toLowerCase()))
                    .findFirst();
            if (!cards.isEmpty()) {
                appExceptionHandler.raiseException("Card not found: " + CardName.toLowerCase());
            }
            List<Long> CardID = cardRepo.findAll().stream()
                    .filter(card -> card.getId().getCardname().equals(CardName.toLowerCase()
                    ) && (card.getId().getDevice().getDevicename().equals(DeviceName.toLowerCase())))
                    .map(Card::getCardid).collect(Collectors.toList());

            List<String> cardSlotNames = carslotRepo.findAll().stream()
                    .filter(cardSlot -> CardID.contains(cardSlot.getCard().getCardid()))
                    .map(CardSlot::getName)
                    .collect(Collectors.toList());
            response = pluggableRepo.findPluggablesByDeviceNameAndCardSlotNames(DeviceName.toLowerCase(), cardSlotNames);
            if (response.isEmpty()) {
                appExceptionHandler.raiseException("Given Device and Card Have No Pluggable Found: " +
                        DeviceName.toLowerCase() + " " + CardName.toLowerCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @GetMapping("/getPortOnCardSlot")
    public Port getPortOnCardSlot(@RequestParam("CardSlotName") String CardSlotName) {
        Port response = new Port();
        try {
            Optional<Port> PortOptional = portRepo.findAll().stream()
                    .filter(port -> port.getCardSlot().getName().equals(CardSlotName.toLowerCase()))
                    .findFirst();
            if (!PortOptional.isPresent()) {
                appExceptionHandler.raiseException("Given CardSlot has no Port found: " + CardSlotName.toLowerCase());
            }
            response = PortOptional.get();
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @GetMapping("/getPluggableOnCardSlot")
    public Pluggable getPluggableOnCardSlot(@RequestParam("CardSlotName") String CardSlotName) {
        Pluggable response = new Pluggable();
        try {
            Optional<Pluggable> PluggableOptional = pluggableRepo.findAll().stream()
                    .filter(pluggable -> pluggable.getCardSlot().getName().equals(CardSlotName.toLowerCase()))
                    .findFirst();
            if (!PluggableOptional.isPresent()) {
                appExceptionHandler.raiseException("Given CardSlot has no Pluggable found: " +
                        CardSlotName.toLowerCase());
            }
            response = PluggableOptional.get();
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @GetMapping("/getAllPortsOnCard")
    public List<Port> getAllPortsOnCard(@RequestParam("CardName") String CardName) {
        List<Port> response = new ArrayList<>();
        try {
            response = portRepo.findAll().stream()
                    .filter(port -> port.getCardname().equals(CardName.toLowerCase()))
                    .collect(Collectors.toList());
            if (response.isEmpty()) {
                appExceptionHandler.raiseException("Given Card Have No Port Found" + CardName.toLowerCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @GetMapping("/getAllPluggablesOnCard")
    public List<Pluggable> getAllPluggablesOnCard(@RequestParam("CardName") String CardName) {
        List<Pluggable> response = new ArrayList<>();
        try {
            response = pluggableRepo.findAll().stream()
                    .filter(pluggable -> pluggable.getCardname().equals(CardName.toLowerCase()))
                    .collect(Collectors.toList());
            if (response.isEmpty()) {
                appExceptionHandler.raiseException("Given Card Have No Pluggable Found" + CardName.toLowerCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @GetMapping("/getAvailableShelvesOnDevice")
    public List<Integer> getAvailableShelvesOnDevice(@RequestParam(name = "DeviceName") String DeviceName) {
        List<Integer> response = new ArrayList<>();
        try {
            Optional<Device> deviceOptional = deviceRepo.findAll().stream()
                    .filter(device -> device.getDevicename().equals(DeviceName.toLowerCase()))
                    .findFirst();
            if (!deviceOptional.isPresent()) {
                appExceptionHandler.raiseException("Device not found: " + DeviceName.toLowerCase());
            }
            response = shelfRepo.findAll().stream().filter(shelf -> shelf.getDevice().getDevicename()
                    .equals(DeviceName.toLowerCase())).map(Shelf::getShelfPosition).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @GetMapping("/findLogicalPortByName")
    public List<LogicalPort> findLogicalPortByName(@RequestParam(name = "LogicalPortName") String LogicalPortName) {
        List<LogicalPort> response = new ArrayList<>();
        try {
            response = logicalPortRepo.findAll().stream()
                    .filter(logicalPort -> logicalPort.getName().equals(LogicalPortName.toLowerCase()))
                    .collect(Collectors.toList());
            if (response.isEmpty()) {
                appExceptionHandler.raiseException("LogicalPortName not found: " + LogicalPortName.toLowerCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @GetMapping("/getCardModelsForDevice")
    public List<String> getCardModelsForDevice(@RequestParam(name = "DeviceName") String DeviceName) {
        List<String> response = new ArrayList<>();
        try {
            Optional<String> deviceOptional = deviceRepo.findAll().stream()
                    .filter(d -> d.getDevicename().equals(DeviceName.toLowerCase()))
                    .map(Device::getDeviceModel)
                    .findFirst();
            if (!deviceOptional.isPresent()) {
                appExceptionHandler.raiseException("Device not found: " + DeviceName.toLowerCase());
            }
            String metamodelName = deviceOptional.get();
            response = deviceMetaModelRepo.findAll().stream().filter
                    (deviceMetaModel -> deviceMetaModel.getDeviceModel().equals(metamodelName)).map(
                    DeviceMetaModel::getDeviceModel).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @GetMapping("/findLogicalPortsOnCardRelation")
    public List<LogicalPort> findLogicalPortsOnCardRelation(@RequestParam(name = "Device") String DeviceName) {
        List<LogicalPort> response = new ArrayList<>();
        try {
            Optional<String> deviceOptional = deviceRepo.findAll().stream()
                    .filter(device -> device.getDevicename().equals(DeviceName.toLowerCase()))
                    .map(Device::getDevicename)
                    .findFirst();
            if (!deviceOptional.isPresent()) {
                appExceptionHandler.raiseException("Device not found: " + DeviceName.toLowerCase());
            }
            response = logicalPortRepo.findAll().stream().filter(logicalPort -> logicalPort.getPositionOnCard() != 0)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @GetMapping("/findLogicalPortsOnDeviceRelation")
    public List<LogicalPort> findLogicalPortsOnDeviceRelation(@RequestParam(name = "Device") String DeviceName) {
        List<LogicalPort> response = new ArrayList<>();
        try {
            Optional<String> deviceOptional = deviceRepo.findAll().stream()
                    .filter(device -> device.getDevicename().equals(DeviceName.toLowerCase()))
                    .map(Device::getDevicename)
                    .findFirst();
            if (!deviceOptional.isPresent()) {
                appExceptionHandler.raiseException("Device not found: " + DeviceName.toLowerCase());
            }
            response = logicalPortRepo.findAll().stream().filter(logicalPort -> logicalPort.getPositionOnCard() == 0)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @Autowired
    AdditionalAttributeRepo additionalAttributeRepo;

    @Transactional
    @GetMapping("/UpdateShelf")
    public Shelf UpdateShelf(@RequestParam("ShelfName") String ShelfName,
                             @RequestBody ShelfDto shelfDto) {
        Shelf exShelf = new Shelf();
        try {
            Optional<Shelf> ShlefOptional = shelfRepo.findAll().stream()
                    .filter(shelf -> shelf.getName().equals(ShelfName.toLowerCase()))
                    .findFirst();
            if (!ShlefOptional.isPresent()) {
                appExceptionHandler.raiseException("Given Shelf has not found: " + ShelfName.toLowerCase());
            }
            exShelf = ShlefOptional.get();
            String finalName = ShelfName;
            List<Long> AAIds = shelfRepo.findAll().stream()
                    .filter(shelf -> shelf.getName().equals(finalName.toLowerCase()))
                    .flatMap(shelf -> shelf.getAdditionalAttributes().stream())
                    .map(AdditionalAttribute::getId)
                    .collect(Collectors.toList());
            exShelf.setOperationalState(shelfDto.getOperationalState());
            exShelf.setAdministrativeState(shelfDto.getAdministrativeState());
            exShelf.setUsageState(shelfDto.getUsageState());
            exShelf.setHref(shelfDto.getHref());
            exShelf = shelfRepo.save(exShelf);
            List<AdditionalAttribute> additionalAttributes = new ArrayList<>();
            if (shelfDto.getAdditionalAttributes() != null && !shelfDto.getAdditionalAttributes().isEmpty()) {
                for (AdditionalAttribute additionalAttributeDTO : shelfDto.getAdditionalAttributes()) {
                    AdditionalAttribute additionalAttribute = new AdditionalAttribute();
                    additionalAttribute.setKey(additionalAttributeDTO.getKey());
                    additionalAttribute.setValue(additionalAttributeDTO.getValue());
                    AdditionalAttribute savedAdditionalAttribute = additionalAttributeRepo.save(additionalAttribute);
                    additionalAttributes.add(savedAdditionalAttribute);
                }
            }
            exShelf.setAdditionalAttributes(additionalAttributes);
            exShelf = shelfRepo.save(exShelf);
            additionalAttributeRepo.deleteAdditionalAttributesByIds(AAIds);
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return exShelf;
    }

    @Transactional
    @GetMapping("/UpdateSlot")
    public Slot UpdateSlot(@RequestParam("SlotName") String SlotName,
                           @RequestBody SlotDto slotDto) {
        Slot exSlot = new Slot();
        try {
            Optional<Slot> SlotOptional = slotRepo.findAll().stream()
                    .filter(slot -> slot.getSlotname().equals(SlotName.toLowerCase()))
                    .findFirst();
            if (!SlotOptional.isPresent()) {
                appExceptionHandler.raiseException("Given Slot has not found: " + SlotName.toLowerCase());
            }
            exSlot = SlotOptional.get();
            String finalName = SlotName;
            List<Long> AAIds = slotRepo.findAll().stream()
                    .filter(slot -> slot.getSlotname().equals(SlotName.toLowerCase()))
                    .flatMap(slot -> slot.getAdditionalAttributes().stream())
                    .map(AdditionalAttribute::getId)
                    .collect(Collectors.toList());
            exSlot.setOperationalState(slotDto.getOperationalState());
            exSlot.setAdministrativeState(slotDto.getAdministrativeState());
            exSlot.setUsageState(slotDto.getUsageState());
            exSlot.setHref(slotDto.getHref());
            exSlot = slotRepo.save(exSlot);
            List<AdditionalAttribute> additionalAttributes = new ArrayList<>();
            if (slotDto.getAdditionalAttributes() != null && !slotDto.getAdditionalAttributes().isEmpty()) {
                for (AdditionalAttribute additionalAttributeDTO : slotDto.getAdditionalAttributes()) {
                    AdditionalAttribute additionalAttribute = new AdditionalAttribute();
                    additionalAttribute.setKey(additionalAttributeDTO.getKey());
                    additionalAttribute.setValue(additionalAttributeDTO.getValue());
                    AdditionalAttribute savedAdditionalAttribute = additionalAttributeRepo.save(additionalAttribute);
                    additionalAttributes.add(savedAdditionalAttribute);
                }
            }
            exSlot.setAdditionalAttributes(additionalAttributes);
            exSlot = slotRepo.save(exSlot);
            additionalAttributeRepo.deleteAdditionalAttributesByIds(AAIds);
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return exSlot;
    }

    @Transactional
    @GetMapping("/UpdateCardSlot")
    public CardSlot UpdateCardSlot(@RequestParam("CardSlotName") String CardSlotName,
                                   @RequestBody CardSlotDto cardSlotDto) {
        CardSlot exCardSlot = new CardSlot();
        try {
            Optional<CardSlot> CardSlotOptional = carslotRepo.findAll().stream()
                    .filter(cardSlot -> cardSlot.getName().equals(CardSlotName.toLowerCase()))
                    .findFirst();
            if (!CardSlotOptional.isPresent()) {
                appExceptionHandler.raiseException("Given Card Slot has not found: " + CardSlotName.toLowerCase());
            }
            exCardSlot = CardSlotOptional.get();
            String finalName = CardSlotName;
            List<Long> AAIds = carslotRepo.findAll().stream()
                    .filter(cardSlot -> cardSlot.getName().equals(CardSlotName.toLowerCase()))
                    .flatMap(cardSlot -> cardSlot.getAdditionalAttributes().stream())
                    .map(AdditionalAttribute::getId)
                    .collect(Collectors.toList());
            exCardSlot.setOperationalState(cardSlotDto.getOperationalState());
            exCardSlot.setAdministrativeState(cardSlotDto.getAdministrativeState());
            exCardSlot.setUsageState(cardSlotDto.getUsageState());
            exCardSlot.setHref(cardSlotDto.getHref());
            exCardSlot = carslotRepo.save(exCardSlot);
            List<AdditionalAttribute> additionalAttributes = new ArrayList<>();
            if (cardSlotDto.getAdditionalAttributes() != null && !cardSlotDto.getAdditionalAttributes().isEmpty()) {
                for (AdditionalAttribute additionalAttributeDTO : cardSlotDto.getAdditionalAttributes()) {
                    AdditionalAttribute additionalAttribute = new AdditionalAttribute();
                    additionalAttribute.setKey(additionalAttributeDTO.getKey());
                    additionalAttribute.setValue(additionalAttributeDTO.getValue());
                    AdditionalAttribute savedAdditionalAttribute = additionalAttributeRepo.save(additionalAttribute);
                    additionalAttributes.add(savedAdditionalAttribute);
                }
            }
            exCardSlot.setAdditionalAttributes(additionalAttributes);
            exCardSlot = carslotRepo.save(exCardSlot);
            additionalAttributeRepo.deleteAdditionalAttributesByIds(AAIds);
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return exCardSlot;
    }

    @DeleteMapping("/deleteDevice")
    @Transactional
    public JsonNode deleteDevice(@RequestParam(name = "DeviceName") String deviceName) {
        JsonNode response;

        try {
            String deviceNameLower = deviceName.toLowerCase();
            logger.info("Inside deleteDevice for Name: {}", deviceNameLower);

            Device exDevice = deviceRepo.findByDevicename(deviceNameLower);
            if (exDevice == null) {
                logger.warn("No device found with the name: {}", deviceNameLower);
                appExceptionHandler.raiseException("Given Device " + deviceName + " doesn't Exist");
            }

            Device connectData = deviceRepo.findDevicesByName(deviceNameLower);
            if (connectData != null) {
                logger.warn("Cannot delete device {} as it has associated ports or cards", deviceNameLower);
                appExceptionHandler.raiseException("Given Device Cannot Delete Associated Ports or Cards");
            }

            List<Long> aaIds = deviceRepo.findAll().stream()
                    .filter(device -> device.getDevicename().equals(deviceNameLower))
                    .flatMap(device -> device.getAdditionalAttributes().stream())
                    .map(AdditionalAttribute::getId)
                    .collect(Collectors.toList());

            logger.info("Deleting device with name: {}", deviceNameLower);
            deviceRepo.deleteByDevicename(deviceNameLower);

            if (!aaIds.isEmpty()) {
                logger.info("Deleting additional attributes associated with device: {}", deviceNameLower);
                additionalAttributeRepo.deleteAdditionalAttributesByIds(aaIds);
            }

            response = objectMapper.createObjectNode()
                    .put("status", "Success")
                    .put("message", "Device deleted successfully")
                    .put("deletedDevice", deviceNameLower);
            logger.info("Device deleted successfully for Name: {}", deviceNameLower);

        } catch (Exception e) {
            logger.error("An error occurred while deleting device with name: {}", deviceName, e);
            response = objectMapper.createObjectNode()
                    .put("status", "Error")
                    .put("message", "An error occurred while deleting device")
                    .put("error", e.getMessage());
        }

        return response;
    }

    @DeleteMapping("/deleteCard")
    @Transactional
    public JsonNode deleteCard(@RequestParam(name = "CardName") String cardName, @RequestParam("DeviceName") String deviceName) {
        JsonNode response;
        try {
            String cardNameLower = cardName.toLowerCase();
            String deviceNameLower = deviceName.toLowerCase();
            logger.info("Inside deleteCard for CardName: {} and DeviceName: {}", cardNameLower, deviceNameLower);

            Card exDeviceCard = cardRepo.findCardsByCardNameAndDeviceName(cardNameLower, deviceNameLower);
            if (exDeviceCard == null) {
                logger.warn("No card found for CardName: {} and DeviceName: {}", cardNameLower, deviceNameLower);
                appExceptionHandler.raiseException("Given device matching with cardName not found");
            }
            Long cardid = exDeviceCard.getCardid();
            List<String> CardSlots = carslotRepo.findAll().stream().filter(cardSlot -> cardSlot.getCard().getCardid().
                    equals(cardid)).map(CardSlot::getName).collect(Collectors.toList());
            if (!CardSlots.isEmpty()) {
                logger.warn("Cannot delete Card with CardID: {} as it is connected to CardSlots : {}", cardid, CardSlots);
                appExceptionHandler.raiseException("Given Card cannot be deleted as it is connected to  CardSlot");
            }
            List<Long> aaIds = cardRepo.findAll().stream()
                    .filter(card -> card.getId().getCardname().equals(cardNameLower) &&
                            card.getId().getDevice().getDevicename().equals(deviceNameLower))
                    .flatMap(card -> card.getAdditionalAttributes().stream())
                    .map(AdditionalAttribute::getId)
                    .collect(Collectors.toList());
            logger.info("Deleting card with CardName: {} and DeviceName: {}", cardNameLower, deviceNameLower);
            cardRepo.deleteByCardnameAndDevicename(cardNameLower, deviceNameLower);
            if (!aaIds.isEmpty()) {
                logger.info("Deleting additional attributes associated with card");
                additionalAttributeRepo.deleteAdditionalAttributesByIds(aaIds);
            }
            response = objectMapper.createObjectNode()
                    .put("status", "Success")
                    .put("message", "Card deleted successfully")
                    .put("deletedCard", cardNameLower);
            logger.info("Card deleted successfully for CardName: {} and DeviceName: {}", cardNameLower, deviceNameLower);

        } catch (Exception e) {
            logger.error("An error occurred while deleting card with CardName: {} and DeviceName: {}", cardName, deviceName, e);
            response = objectMapper.createObjectNode()
                    .put("status", "Error")
                    .put("message", "An error occurred while deleting card")
                    .put("error", e.getMessage());
        }
        return response;
    }

    @DeleteMapping("/deletePort")
    @Transactional
    public JsonNode deletePort(@RequestParam(name = "CarSlotName", required = false) String carSlotName,
                               @RequestParam("PortName") String portName,
                               @RequestParam(value = "DeviceName", required = false) String deviceName) {
        JsonNode response;
        try {
            String carSlotNameLower = carSlotName != null ? carSlotName.toLowerCase() : null;
            String portNameLower = portName != null ? portName.toLowerCase() : null;
            String deviceNameLower = deviceName != null ? deviceName.toLowerCase() : null;

            if ((carSlotNameLower == null && deviceNameLower == null) || (carSlotNameLower != null && deviceNameLower != null)) {
                logger.warn("Invalid input: CarSlotName={}, DeviceName={}, PortName={}", carSlotName, deviceName, portName);
                appExceptionHandler.raiseException("Please provide valid input where either CarSlotName or DeviceName is null and the other is not null");
            }

            logger.info("Inside deletePort for CarSlotName: {}, DeviceName: {}, PortName: {}", carSlotNameLower, deviceNameLower, portNameLower);

            Port exPort = portRepo.findPortsByCardSlotNameAndPortNameAndDeviceName(carSlotNameLower, portNameLower, deviceNameLower);
            if (exPort == null) {
                logger.warn("No port found for CarSlotName: {}, DeviceName: {}, PortName: {}", carSlotNameLower, deviceNameLower, portNameLower);
                appExceptionHandler.raiseException("Port matching the given criteria not found");
            }
            Long portId = exPort.getPortid();

            List<String> logicalPortNames = logicalPortRepo.findAll().stream()
                    .filter(logicalPort -> logicalPort.getPort().getPortid().equals(portId))
                    .map(LogicalPort::getName)
                    .collect(Collectors.toList());

            List<String> physicalConnections = physicalConnectionRepo.findAll().stream()
                    .filter(physicalConnection ->
                            physicalConnection.getDeviceaport().equals(portNameLower) ||
                                    physicalConnection.getDevicezport().equals(portNameLower))
                    .map(PhysicalConnection::getName)
                    .collect(Collectors.toList());

            if (!logicalPortNames.isEmpty() || !physicalConnections.isEmpty()) {
                logger.warn("Cannot delete port with portId: {} as it is connected to logical ports: {} and physical connections: {}",
                        portId, logicalPortNames, physicalConnections);
                appExceptionHandler.raiseException("Given Port cannot be deleted as it is connected to logical ports or physical connections");
            }

            List<Long> aaIds = portRepo.findAll().stream()
                    .filter(port -> port.getPortid().equals(portId))
                    .flatMap(port -> port.getAdditionalAttributes().stream())
                    .map(AdditionalAttribute::getId)
                    .collect(Collectors.toList());
            logger.info("Deleting port with CarSlotName: {}, DeviceName: {}, PortName: {}", carSlotNameLower, deviceNameLower, portNameLower);
            portRepo.deleteByPortid(portId);

            if (!aaIds.isEmpty()) {
                logger.info("Deleting additional attributes associated with port");
                additionalAttributeRepo.deleteAdditionalAttributesByIds(aaIds);
            }

            response = objectMapper.createObjectNode()
                    .put("status", "Success")
                    .put("message", "Port deleted successfully")
                    .put("deletedPort", portNameLower);
            logger.info("Port deleted successfully for CarSlotName: {} and DeviceName: {}", carSlotNameLower, deviceNameLower);

        } catch (Exception e) {
            logger.error("An error occurred while deleting port with CarSlotName: {} and DeviceName: {}", carSlotName, deviceName, e);
            response = objectMapper.createObjectNode()
                    .put("status", "Error")
                    .put("message", "An error occurred while deleting port")
                    .put("error", e.getMessage());
        }
        return response;
    }

    @DeleteMapping("/deletePluggable")
    @Transactional
    public JsonNode deletePluggable(@RequestParam(name = "CarSlotName", required = false) String carSlotName,
                                    @RequestParam("Pluggable") String PluggableName,
                                    @RequestParam(value = "DeviceName", required = false) String deviceName) {
        JsonNode response;
        try {
            String carSlotNameLower = carSlotName != null ? carSlotName.toLowerCase() : null;
            String PluggableNameLower = PluggableName != null ? PluggableName.toLowerCase() : null;
            String deviceNameLower = deviceName != null ? deviceName.toLowerCase() : null;

            if ((carSlotNameLower == null && deviceNameLower == null) || (carSlotNameLower != null && deviceNameLower != null)) {
                logger.warn("Invalid input: CarSlotName={}, DeviceName={}, PortName={}", carSlotName, deviceName, PluggableName);
                appExceptionHandler.raiseException("Please provide valid input where either CarSlotName or DeviceName is null and the other is not null");
            }
            logger.info("Inside deletePort for CarSlotName: {}, DeviceName: {}, PortName: {}", carSlotNameLower, deviceNameLower, PluggableNameLower);
            Pluggable exPluggable = pluggableRepo.findPluggableByCardSlotNameAndPlugableNameAndDeviceName(carSlotNameLower, PluggableNameLower, deviceNameLower);
            if (exPluggable == null) {
                logger.warn("No Pluggable found for CarSlotName: {}, DeviceName: {}, PluggableName: {}", carSlotNameLower, deviceNameLower, PluggableNameLower);
                appExceptionHandler.raiseException("Pluggable matching the given criteria not found");
            }
            Long PluggableId = exPluggable.getId();

            List<String> logicalPortNames = logicalPortRepo.findAll().stream()
                    .filter(logicalPort -> logicalPort.getPluggable().getId().equals(PluggableId))
                    .map(LogicalPort::getName)
                    .collect(Collectors.toList());
            List<String> physicalConnections = physicalConnectionRepo.findAll().stream()
                    .filter(physicalConnection ->
                            physicalConnection.getDeviceaport().equals(PluggableNameLower) ||
                                    physicalConnection.getDevicezport().equals(PluggableNameLower))
                    .map(PhysicalConnection::getName)
                    .collect(Collectors.toList());

            if (!logicalPortNames.isEmpty() || !physicalConnections.isEmpty()) {
                logger.warn("Cannot delete Pluggable with PluggableId: {} as it is connected to logical ports: {} and physical connections: {}",
                        PluggableId, logicalPortNames, physicalConnections);
                appExceptionHandler.raiseException("Given Port cannot be deleted as it is connected to logical ports or physical connections");
            }

            List<Long> aaIds = pluggableRepo.findAll().stream()
                    .filter(pluggable -> pluggable.getId().equals(PluggableId))
                    .flatMap(pluggable -> pluggable.getAdditionalAttributes().stream())
                    .map(AdditionalAttribute::getId)
                    .collect(Collectors.toList());
            logger.info("Deleting Pluggable with CarSlotName: {}, DeviceName: {}, PortName: {}", carSlotNameLower, deviceNameLower, PluggableNameLower);
            pluggableRepo.deleteBypluggableByid(PluggableId);
            if (!aaIds.isEmpty()) {
                logger.info("Deleting additional attributes associated with Pluggable");
                additionalAttributeRepo.deleteAdditionalAttributesByIds(aaIds);
            }
            response = objectMapper.createObjectNode()
                    .put("status", "Success")
                    .put("message", "Pluggable deleted successfully")
                    .put("deletedPort", PluggableNameLower);
            logger.info("Pluggable deleted successfully for CarSlotName: {} and DeviceName: {}", carSlotNameLower, deviceNameLower);

        } catch (Exception e) {
            logger.error("An error occurred while deleting Pluggable with CarSlotName: {} and DeviceName: {}", carSlotName, deviceName, e);
            response = objectMapper.createObjectNode()
                    .put("status", "Error")
                    .put("message", "An error occurred while deleting port")
                    .put("error", e.getMessage());
        }
        return response;
    }

    @DeleteMapping("/deleteLogicalPort")
    @Transactional
    public JsonNode deleteLogicalPort(@RequestParam("LogicalPort") String LogicalPortName,
                                      @RequestParam(value = "DeviceName") String deviceName) {
        JsonNode response;
        try {

            String LogicalPortNameLower = LogicalPortName.toLowerCase();
            String deviceNameLower = deviceName.toLowerCase();
            logger.info("Inside deleteLogicalPort for LogicalPortName: {}, DeviceName: {}", LogicalPortNameLower,
                    deviceNameLower);
            LogicalPort exLogicalPort = logicalPortRepo.findLogicalPortByNameAndDeviceName(LogicalPortNameLower, deviceNameLower);
            if (exLogicalPort == null) {
                logger.warn("No LogicalPort found for DeviceName: {}, LogicalPort: {}", deviceNameLower, LogicalPortNameLower);
                appExceptionHandler.raiseException("LogicalPort matching the given criteria not found");
            }
            Long LogicalPortid = exLogicalPort.getLogicalportid();
            List<String> LogicalConnection = logicalConnectionRepo.findAll().stream()
                    .filter(logicalConnection -> logicalConnection.getDeviceALogicalPort().
                            equals(LogicalPortNameLower) || logicalConnection.getDeviceZLogicalPort().
                            equals(LogicalPortNameLower)).map(logicalConnection -> logicalConnection.getName()).
                    collect(Collectors.toList());
            if (!LogicalConnection.isEmpty()) {
                logger.warn("Cannot delete deleteLogicalPort with LogicalPortNameLower: {} as it is connected to LogicalConnections: {}",
                        LogicalPortNameLower, LogicalConnection);
                appExceptionHandler.raiseException("Given Pluggable cannot be deleted as it is connected to logical ports");
            }
            List<Long> aaIds = logicalPortRepo.findAll().stream()
                    .filter(logicalPort -> logicalPort.getLogicalportid().equals(LogicalPortid))
                    .flatMap(logicalPort -> logicalPort.getAdditionalAttributes().stream())
                    .map(AdditionalAttribute::getId)
                    .collect(Collectors.toList());
            logger.info("Deleting LogicalPort with , DeviceName: {}, LogicalPortName: {}", deviceNameLower, LogicalPortNameLower);
            logicalPortRepo.deleteLogicalPortByLogicalportid(LogicalPortid);
            if (!aaIds.isEmpty()) {
                logger.info("Deleting additional attributes associated with LogicalPort");
                additionalAttributeRepo.deleteAdditionalAttributesByIds(aaIds);
            }
            response = objectMapper.createObjectNode()
                    .put("status", "Success")
                    .put("message", "LogicalPort deleted successfully")
                    .put("deletedPort", LogicalPortNameLower);
            logger.info("LogicalPort deleted successfully for LogicalPortName: {} and DeviceName: {}", LogicalPortNameLower,
                    deviceNameLower);

        } catch (Exception e) {
            logger.error("An error occurred while deleting LogicalPort with LogicalPortName: {} and DeviceName: {}",
                    LogicalPortName, deviceName, e);
            response = objectMapper.createObjectNode()
                    .put("status", "Error")
                    .put("message", "An error occurred while deleting LogicalPort")
                    .put("error", e.getMessage());
        }
        return response;
    }


    //TODO TMF Api create and Update Device

    @PostMapping("TMFAPI")
    @Transactional
    public String CreateDeviceTMF(@RequestBody TmfResponce device) {
        logger.info("Received request to create device: {}", device.getDeviceDto().getDevicename());
        DeviceMetaModel deviceMetaModel = deviceMetaModelRepo.findByDeviceModel(device.getDeviceDto().getDeviceModel());
        if (deviceMetaModel == null) {
            logger.error("Device MetaModel not found for model: {}", device.getDeviceDto().getDeviceModel());
            appExceptionHandler.raiseException("Given Device Metamodel Not Found");
        }

      /*  HashSet<Long> orderIds = (HashSet<Long>) DeviceApi.getOrderIdSet(device);
        Long minOrderId = (orderIds.size() == 1) ? orderIds.iterator().next() : Collections.min(orderIds);
        Long maxOrderId = (orderIds.size() == 1) ? orderIds.iterator().next() : Collections.max(orderIds);

        List<Long> ordersInRange = orderRepo.findOrdersInRange(minOrderId, maxOrderId).stream().map(Order::getId).collect(Collectors.toList());
        if (!ordersInRange.containsAll(orderIds)) {
            logger.error("Invalid Order IDs provided: {}", orderIds);
            appExceptionHandler.raiseException("Please give Valid Order id");
        }

       */

        Optional<Order> optionalOrder = orderRepo.findById(device.getDeviceDto().getOrderid());
        if (!optionalOrder.isPresent()) {
            logger.error("Order not found for ID: {}", device.getDeviceDto().getOrderid());
            appExceptionHandler.raiseException("Given Order is Not found");
        }

        Order order = optionalOrder.get();
        Building building = buildingRepo.findByBuildingName(device.getDeviceDto().getBuildingname());
        if (building == null) {
            logger.error("Building not found for name: {}", device.getDeviceDto().getBuildingname());
            appExceptionHandler.raiseException("Given Building Not Found");
        }

        Device existingDevice = deviceRepo.findByDevicename(device.getDeviceDto().getDevicename().toLowerCase());
        if (existingDevice != null) {
            logger.error("Device already exists with name: {}", device.getDeviceDto().getDevicename().toLowerCase());
            appExceptionHandler.raiseException("Given Device Already Found");
        }

        Device newDevice = new Device();
        newDevice.setDevicename(device.getDeviceDto().getDevicename().toLowerCase());
        newDevice.setDeviceModel(device.getDeviceDto().getDeviceModel());
        newDevice.setBuilding(building);
        newDevice.setLocation(device.getDeviceDto().getLocation());
        newDevice.setOrganisation(device.getDeviceDto().getOrganisation());
        newDevice.setCustomer(order.getCustomer().getCustomername());
        newDevice.setManagementIp(device.getDeviceDto().getManagementIp());
        newDevice.setRackPosition(device.getDeviceDto().getRackPosition());
        newDevice.setOperationalState(device.getDeviceDto().getOperationalState());
        newDevice.setAdministrativeState(device.getDeviceDto().getAdministrativeState());
        newDevice.setUsageState(device.getDeviceDto().getUsageState());
        newDevice.setSerialNumber(device.getDeviceDto().getSerialNumber());
        newDevice.setHref(device.getDeviceDto().getHref());
        newDevice.setAccessKey(device.getDeviceDto().getAccessKey());
        newDevice.setPollInterval(device.getDeviceDto().getPollInterval());
        newDevice.setOrder(order);
        newDevice.setRealtion("BUILDING_TO_DEVICE");

        deviceRepo.save(newDevice);
        logger.info("Device saved successfully: {}", newDevice.getDevicename());

        List<AdditionalAttribute> additionalAttributes = new ArrayList<>();
        if (device.getDeviceDto().getAdditionalAttributes() != null && !device.getDeviceDto().getAdditionalAttributes().isEmpty()) {
            for (AdditionalAttribute additionalAttributeDTO : device.getDeviceDto().getAdditionalAttributes()) {
                AdditionalAttribute additionalAttribute = new AdditionalAttribute();
                additionalAttribute.setKey(additionalAttributeDTO.getKey());
                additionalAttribute.setValue(additionalAttributeDTO.getValue());
                AdditionalAttribute savedAdditionalAttribute = additionalAttributeRepo.save(additionalAttribute);
                additionalAttributes.add(savedAdditionalAttribute);
            }
            newDevice.setAdditionalAttributes(additionalAttributes);
            deviceRepo.save(newDevice);
            logger.info("Additional attributes saved for device: {}", newDevice.getDevicename());
        }
        additionalAttributes.clear();

        int shelfContained = deviceMetaModel.getShelvesContained();
        logger.info("Device {} contains {} shelves.", device.getDeviceDto().getDevicename(), shelfContained);
        ArrayList<Shelf> shelves = new ArrayList<>();
        if (shelfContained > 0) {
            try {
                for (int i = 1; i <= shelfContained; i++) {
                    Shelf shelf = new Shelf();
                    shelf.setName(device.getDeviceDto().getDevicename().toLowerCase() + "_shelf" + i);
                    shelf.setRealtion("DEVICE_TO_SHELF");
                    shelf.setOperationalState(device.getDeviceDto().getOperationalState());
                    shelf.setAdministrativeState(device.getDeviceDto().getAdministrativeState());
                    shelf.setUsageState(device.getDeviceDto().getUsageState());
                    shelf.setDevice(existingDevice);
                    shelf.setHref(device.getDeviceDto().getHref());
                    shelf.setShelfPosition(i);
                    shelves.add(shelf);
                }
                shelfRepo.saveAll(shelves);
                // logger.info("Shelf {} saved successfully for device {}", shelf.getName(), device.getDeviceDto().getDevicename());

            } catch (Exception e) {
                logger.error("Error occurred while saving shelves for device {}: {}", device.getDeviceDto().getDevicename(), e.getMessage());
                e.printStackTrace();
                appExceptionHandler.raiseException(e.getMessage());
            }
        }
        if (!device.getCardDtos().isEmpty()) {
            logger.info("Processing cards for device {}", device.getDeviceDto().getDevicename());
            try {
                for (CardDto cardDto : device.getCardDtos()) {
                    Device cardDevice = deviceRepo.findByDevicename(cardDto.getDevicename());
                    if (cardDevice == null) {
                        logger.error("Device not found: {}", cardDto.getDevicename());
                        appExceptionHandler.raiseException("Given Device is Not Found");
                    }

                    String deviceMetamodel = cardDevice.getDeviceModel();
                    ArrayList<String> allowedCards = deviceMetaModelRepo.findAll().stream()
                            .filter(deviceMetaModel1 -> deviceMetaModel1.getDeviceModel().equals(deviceMetamodel))
                            .flatMap(deviceMetaModel1 -> deviceMetaModel1.getAllowedCardList().stream())
                            .collect(Collectors.toCollection(ArrayList::new));

                    Card existingCard = cardRepo.findCardsByCardNameAndDeviceName(cardDto.getCardname(), cardDto.getDevicename());
                    if (existingCard != null) {
                        logger.error("Card already exists: {} for device {}", cardDto.getCardname(), cardDto.getDevicename());
                        appExceptionHandler.raiseException("Given Card name is Already Found");
                    }

                    Optional<Order> cardOrder = orderRepo.findById(cardDto.getOrderId());
                    if (!cardOrder.isPresent()) {
                        logger.error("Order not found: {}", cardDto.getOrderId());
                        appExceptionHandler.raiseException("Given Order is not Found");
                    }

                    if (!allowedCards.contains(cardDto.getCardModel())) {
                        logger.error("Card model {} is not compatible with device {}", cardDto.getCardModel(), cardDto.getDevicename());
                        appExceptionHandler.raiseException("Given Card is not Compatible");
                    }

                    String shelfName = cardDto.getDevicename().toLowerCase() + "_shelf" + cardDto.getShelfPosition();
                    Shelf existingShelf = shelfRepo.findShelfByName(shelfName);
                    if (existingShelf == null) {
                        logger.error("Shelf not found or is unavailable: {}", shelfName);
                        appExceptionHandler.raiseException("Given Shelf is not found or unavailable at position " + cardDto.getShelfPosition());
                    }

                    String slotName = cardDto.getDevicename().toLowerCase() + "/" + cardDto.getShelfPosition() + "/" + cardDto.getSlotPosition();
                    Slot existingSlot = slotRepo.findBySlotname(slotName);
                    if (existingSlot != null) {
                        logger.error("Slot already occupied: {}", slotName);
                        appExceptionHandler.raiseException("Given Slot Already Occupied");
                    }

                    Slot newSlot = new Slot();
                    newSlot.setSlotname(slotName);
                    newSlot.setSlotPosition(cardDto.getSlotPosition());
                    newSlot.setOperationalState(cardDto.getOperationalState());
                    newSlot.setAdministrativeState(cardDto.getAdministrativeState());
                    newSlot.setUsageState(cardDto.getUsageState());
                    newSlot.setHref(cardDto.getHref());
                    newSlot.setRelation("SHELF_TO_SLOT");
                    newSlot.setShelf(existingShelf);
                    slotRepo.save(newSlot);

                    if (existingCard == null) {
                        CardId cardId = new CardId();
                        cardId.setDevice(cardDevice);
                        cardId.setCardname(cardDto.getCardname());
                        Long id = cardRepo.findAll().stream()
                                .map(Card::getCardid)  // Map each Card to its cardid
                                .max(Long::compare)  // Find the maximum cardid
                                .orElse(1L);  // Return 1 if no cards are present
                        existingCard = new Card();
                        existingCard.setCardid(id + 1);
                        existingCard.setId(cardId);
                        existingCard.setSlot(newSlot);
                        existingCard.setShelfPosition(cardDto.getShelfPosition());
                        existingCard.setSlotPosition(cardDto.getSlotPosition());
                        existingCard.setVendor(cardDto.getVendor());
                        existingCard.setCardModel(cardDto.getCardModel());
                        existingCard.setCardPartNumber(cardDto.getCardPartNumber());
                        existingCard.setOperationalState(cardDto.getOperationalState());
                        existingCard.setAdministrativeState(cardDto.getAdministrativeState());
                        existingCard.setUsageState(cardDto.getUsageState());
                        existingCard.setHref(cardDto.getHref());
                        existingCard.setRealation("SLOT_TO_CARD");
                        cardRepo.save(existingCard);
                    }

                    if (cardDto.getAdditionalAttributes() != null && !cardDto.getAdditionalAttributes().isEmpty()) {
                        for (AdditionalAttribute additionalAttributeDTO : cardDto.getAdditionalAttributes()) {
                            AdditionalAttribute additionalAttribute = new AdditionalAttribute();
                            additionalAttribute.setKey(additionalAttributeDTO.getKey());
                            additionalAttribute.setValue(additionalAttributeDTO.getValue());
                            AdditionalAttribute savedAdditionalAttribute = additionalAttributeRepo.save(additionalAttribute);
                            additionalAttributes.add(savedAdditionalAttribute);
                        }
                        existingCard.setAdditionalAttributes(additionalAttributes);
                        cardRepo.save(existingCard);
                        logger.info("Additional attributes saved for card {} in device {}", cardDto.getCardname(), cardDto.getDevicename());
                    }
                }
            } catch (Exception e) {
                logger.error("Error processing cards for device {}: {}", device.getDeviceDto().getDevicename(), e.getMessage());
                e.printStackTrace();
                appExceptionHandler.raiseException(e.getMessage());
            }
        }


        CardSlot newCardSlot = new CardSlot();
        Device exDevice = new Device();

        if (!device.getPortDTOS().isEmpty()) {
            logger.info("Processing ports for device {}", device.getDeviceDto().getDevicename());

            try {
                String portType = null;
                HashSet<String> deviceNames = device.getPortDTOS().stream().map(PortDTO::getDeviceName)
                        .collect(Collectors.toCollection(HashSet::new));
                List<String> buildingDeviceNames = deviceRepo.findAll().stream()
                        .filter(device2 -> device2.getBuilding().getBuildingName().contains(device.getDeviceDto().getDevicename()))
                        .map(Device::getDevicename)
                        .collect(Collectors.toList());
                if (!deviceNames.containsAll(buildingDeviceNames)) {
                    logger.error("Device names must be within the building");
                    appExceptionHandler.raiseException("Device Name must be Within the Building");
                }
                for (PortDTO portDTO : device.getPortDTOS()) {
                    if ((portDTO.getPositionOnDevice() == 0 && portDTO.getPositionOnCard() == 0) ||
                            (portDTO.getPositionOnDevice() != 0 && portDTO.getPositionOnCard() != 0)) {
                        logger.error("Invalid format input for port: {}", portDTO);
                        appExceptionHandler.raiseException("Please give Valid Format Input");
                    }
                    Port exPort = portRepo.findPortByDeviceNameAndPortName(portDTO.getDeviceName(), portDTO.getPortname());
                    if (exPort != null) {
                        logger.error("Given port already exists: {}", portDTO.getPortname());
                        appExceptionHandler.raiseException("Given port already exists");
                    }
                    if (portDTO.getPositionOnDevice() != 0) {
                        portType = "Device";
                        Pluggable exPluggable = pluggableRepo.findByDeviceNameAndPositionOnDevice(portDTO.getDeviceName(),
                                portDTO.getPositionOnDevice());
                        if (exPluggable != null) {
                            logger.error("Position already assigned to a pluggable device: {}", portDTO.getPositionOnDevice());
                            appExceptionHandler.raiseException("Given position already assigned to a pluggable device");
                        }
                        exPort = portRepo.findByDeviceNameAndPositionOnDevice(portDTO.getDeviceName(), portDTO.getPositionOnDevice());
                        if (exPort != null) {
                            logger.error("Position already assigned to another port: {}", portDTO.getPositionOnDevice());
                            appExceptionHandler.raiseException("Given position already assigned to another port");
                        }
                    } else {
                        portType = "Card";
                        Card exCard = cardRepo.findCardsByCardNameAndDeviceName(portDTO.getCardname(), portDTO.getDeviceName());
                        if (exCard == null) {
                            logger.error("Card not found for device: {}", portDTO.getDeviceName());
                            appExceptionHandler.raiseException("Given Card with Device Name Not Found");
                        }
                        Long cardId = exCard.getCardid();
                        String cardName = exCard.getId().getCardname();
                        String cardSlotName = cardName + cardId + "/" + portDTO.getPositionOnCard();
                        Pluggable exPluggable = pluggableRepo.findPortsByCardSlotNameAndPositionOnCard(cardSlotName, portDTO.getPositionOnCard());
                        if (exPluggable != null) {
                            logger.error("Given card slot already found: {}", cardSlotName);
                            appExceptionHandler.raiseException("Given cardslot already found");
                        }
                        exPort = portRepo.findPortsByCardSlotNameAndPositionOnCard(cardSlotName, portDTO.getPositionOnCard());
                        if (exPort != null) {
                            logger.error("Given card slot already found: {}", cardSlotName);
                            appExceptionHandler.raiseException("Given cardslot already found");
                        }
                        newCardSlot = new CardSlot();
                        newCardSlot.setName(cardSlotName);
                        newCardSlot.setOperationalState(portDTO.getOperationalState());
                        newCardSlot.setAdministrativeState(portDTO.getAdministrativeState());
                        newCardSlot.setUsageState(portDTO.getUsageState());
                        newCardSlot.setHref(portDTO.getHref());
                        newCardSlot.setRealation("CARD-TO-CARDSLOT");
                        newCardSlot.setSlotPosition(portDTO.getPositionOnCard());
                        newCardSlot.setCardname(portDTO.getCardname());
                        newCardSlot.setCard(exCard);
                        carslotRepo.save(newCardSlot);
                    }
                    Port newPort = new Port();
                    exDevice = deviceRepo.findDevicesByName(portDTO.getDeviceName().toLowerCase());
                    Optional<Order> portOrder = orderRepo.findById(portDTO.getOrderId());
                    if (portOrder.isPresent()) {
                        newPort.setOrder(portOrder.get());
                    }
                    newPort.setPortname(portDTO.getPortname());
                    newPort.setPositionOnCard(portDTO.getPositionOnCard());
                    newPort.setPositionOnDevice(portDTO.getPositionOnDevice());
                    newPort.setPortType(portType.equals("Device") ? "DEVICE_TO_PORT" : "CARDSLOT_TO_PORT");
                    newPort.setOperationalState(portDTO.getOperationalState());
                    newPort.setAdministrativeState(portDTO.getAdministrativeState());
                    newPort.setUsageState(portDTO.getUsageState());
                    newPort.setHref(portDTO.getHref());
                    newPort.setPortSpeed(portDTO.getPortSpeed());
                    newPort.setCapacity(portDTO.getCapacity());
                    newPort.setManagementIp(portDTO.getManagementIp());
                    newPort.setRelation(portType.equals("Device") ? "DEVICE_TO_PORT" : "CARDSLOT_TO_PORT");
                    newPort.setCardname(portType.equals("Device") ? null : portDTO.getCardname());
                    newPort.setCardSlot(portType.equals("Device") ? null : newCardSlot);
                    newPort.setDevice(portType.equals("Device") ? exDevice : null);
                    portRepo.save(newPort);
                    logger.info("New port saved: {}", newPort);

                    if (portDTO.getAdditionalAttributes() != null && !portDTO.getAdditionalAttributes().isEmpty()) {
                        for (AdditionalAttribute additionalAttributeDTO : portDTO.getAdditionalAttributes()) {
                            AdditionalAttribute additionalAttribute = new AdditionalAttribute();
                            additionalAttribute.setKey(additionalAttributeDTO.getKey());
                            additionalAttribute.setValue(additionalAttributeDTO.getValue());
                            AdditionalAttribute savedAdditionalAttribute = additionalAttributeRepo.save(additionalAttribute);
                            additionalAttributes.add(savedAdditionalAttribute);
                        }
                        newPort.setAdditionalAttributes(additionalAttributes);
                        portRepo.save(newPort);
                        logger.info("Additional attributes saved for port {}", portDTO.getPortname());
                    }
                    additionalAttributes.clear();
                }
            } catch (Exception e) {
                logger.error("Error processing ports for device {}: {}", device.getDeviceDto().getDevicename(), e.getMessage());
                e.printStackTrace();
                appExceptionHandler.raiseException(e.getMessage());
            }
        }

        String PluggableType = null;
        if (!device.getPluggableDTOS().isEmpty()) {
            logger.info("Processing pluggables for device {}", device.getDeviceDto().getDevicename());

            try {
                HashSet<String> deviceNames = device.getPluggableDTOS().stream()
                        .map(PluggableDTO::getDeviceName)
                        .collect(Collectors.toCollection(HashSet::new));
                List<String> buildingDeviceNames = deviceRepo.findAll().stream()
                        .filter(device2 -> device2.getBuilding().getBuildingName().contains(device.getDeviceDto().getDevicename()))
                        .map(Device::getDevicename)
                        .collect(Collectors.toList());
                if (!deviceNames.containsAll(buildingDeviceNames)) {
                    logger.error("Device names must be within the building");
                    appExceptionHandler.raiseException("Device Name must be Within the Building");
                }
                for (PluggableDTO pluggableDTO : device.getPluggableDTOS()) {
                    if ((pluggableDTO.getPositionOnDevice() == 0 && pluggableDTO.getPositionOnCard() == 0) ||
                            (pluggableDTO.getPositionOnDevice() != 0 && pluggableDTO.getPositionOnCard() != 0)) {
                        logger.error("Invalid format input for pluggable: {}", pluggableDTO);
                        appExceptionHandler.raiseException("Please give Valid Format Input");
                    }
                    if (pluggableDTO.getPositionOnDevice() != 0) {
                        PluggableType = "Device";
                        Pluggable existingPluggable = pluggableRepo.findByDeviceNameAndPluggableName(pluggableDTO.getDeviceName(),
                                pluggableDTO.getPlugablename());
                        if (existingPluggable != null) {
                            logger.error("Given pluggable already exists: {}", pluggableDTO.getPlugablename());
                            appExceptionHandler.raiseException("Given Pluggable already exists");
                        }
                        Port existingPort = portRepo.findByDeviceNameAndPositionOnDevice(pluggableDTO.getDeviceName(),
                                pluggableDTO.getPositionOnDevice());
                        if (existingPort != null) {
                            logger.error("Position already assigned to another port: {}", pluggableDTO.getPositionOnDevice());
                            appExceptionHandler.raiseException("Given position already assigned to another port");
                        }
                        Pluggable existingPluggableByPosition = pluggableRepo.findByDeviceNameAndPositionOnDevice(pluggableDTO.getDeviceName(),
                                pluggableDTO.getPositionOnDevice());
                        if (existingPluggableByPosition != null) {
                            logger.error("Position already assigned to a pluggable device: {}", pluggableDTO.getPositionOnDevice());
                            appExceptionHandler.raiseException("Given position already assigned to a pluggable device");
                        }
                    } else {
                        PluggableType = "Card";
                        Card existingCard = cardRepo.findCardsByCardNameAndDeviceName(pluggableDTO.getCardname(), pluggableDTO.getDeviceName());
                        if (existingCard == null) {
                            logger.error("Card not found for device: {}", pluggableDTO.getDeviceName());
                            appExceptionHandler.raiseException("Given Card with Device Name Not Found");
                        }
                        Long cardId = existingCard.getCardid();
                        String cardName = existingCard.getId().getCardname();
                        String cardSlotName = cardName + cardId + "/" + pluggableDTO.getPositionOnCard();
                        Pluggable existingPluggable = pluggableRepo.findPluggableByCardNameAndDeviceName(pluggableDTO.getPlugablename(), cardName);
                        if (existingPluggable != null) {
                            logger.error("Given pluggable already found: {}", cardSlotName);
                            appExceptionHandler.raiseException("Given Pluggable already found");
                        }
                        Port existingPort = portRepo.findPortsByCardSlotNameAndPositionOnCard(cardSlotName, pluggableDTO.getPositionOnCard());
                        if (existingPort != null) {
                            logger.error("Position already assigned to another port: {}", cardSlotName);
                            appExceptionHandler.raiseException("Given cardslot already found");
                        }
                        existingPluggable = pluggableRepo.findPortsByCardSlotNameAndPositionOnCard(cardSlotName, pluggableDTO.getPositionOnCard());
                        if (existingPluggable != null) {
                            logger.error("Position already assigned to another pluggable device: {}", cardSlotName);
                            appExceptionHandler.raiseException("Given cardslot already found");
                        }
                        newCardSlot = new CardSlot();
                        newCardSlot.setName(cardSlotName);
                        newCardSlot.setOperationalState(pluggableDTO.getOperationalState());
                        newCardSlot.setAdministrativeState(pluggableDTO.getAdministrativeState());
                        newCardSlot.setUsageState(pluggableDTO.getUsageState());
                        newCardSlot.setHref(pluggableDTO.getHref());
                        newCardSlot.setRealation("CARD-TO-CARDSLOT");
                        newCardSlot.setSlotPosition(pluggableDTO.getPositionOnCard());
                        newCardSlot.setCardname(pluggableDTO.getCardname());
                        newCardSlot.setCard(existingCard);
                        carslotRepo.save(newCardSlot);
                    }
                    Pluggable newPluggable = new Pluggable();
                    exDevice = deviceRepo.findDevicesByName(pluggableDTO.getDeviceName().toLowerCase());
                    Optional<Order> portOrder = orderRepo.findById(pluggableDTO.getOrderId());
                    if (portOrder.isPresent()) {
                        newPluggable.setOrder(portOrder.get());
                    }
                    newPluggable.setPlugablename(pluggableDTO.getPlugablename());
                    newPluggable.setPositionOnCard(pluggableDTO.getPositionOnCard());
                    newPluggable.setPositionOnDevice(pluggableDTO.getPositionOnDevice());
                    newPluggable.setPluggableModel(pluggableDTO.getPluggableModel());
                    newPluggable.setPluggablePartNumber(pluggableDTO.getPluggablePartNumber());
                    newPluggable.setOperationalState(pluggableDTO.getOperationalState());
                    newPluggable.setAdministrativeState(pluggableDTO.getAdministrativeState());
                    newPluggable.setUsageState(pluggableDTO.getUsageState());
                    newPluggable.setHref(pluggableDTO.getHref());
                    newPluggable.setManagementIp(pluggableDTO.getManagementIp());
                    newPluggable.setRelation(PluggableType.equals("Device") ? "DEVICE_TO_PORT" : "CARDSLOT_TO_PORT");
                    newPluggable.setCardname(PluggableType.equals("Device") ? null : pluggableDTO.getCardname());
                    newPluggable.setCardSlot(PluggableType.equals("Device") ? null : newCardSlot);
                    newPluggable.setDevice(PluggableType.equals("Device") ? exDevice : null);
                    pluggableRepo.save(newPluggable);
                    logger.info("New pluggable saved: {}", newPluggable);

                    if (pluggableDTO.getAdditionalAttributes() != null && !pluggableDTO.getAdditionalAttributes().isEmpty()) {
                        for (AdditionalAttribute additionalAttributeDTO : pluggableDTO.getAdditionalAttributes()) {
                            AdditionalAttribute additionalAttribute = new AdditionalAttribute();
                            additionalAttribute.setKey(additionalAttributeDTO.getKey());
                            additionalAttribute.setValue(additionalAttributeDTO.getValue());
                            AdditionalAttribute savedAdditionalAttribute = additionalAttributeRepo.save(additionalAttribute);
                            additionalAttributes.add(savedAdditionalAttribute);
                        }
                        newPluggable.setAdditionalAttributes(additionalAttributes);
                        pluggableRepo.save(newPluggable);
                        logger.info("Additional attributes saved for pluggable {}", pluggableDTO.getPlugablename());
                    }
                    additionalAttributes.clear();
                }
            } catch (Exception e) {
                logger.error("Error processing pluggable for device {}: {}", device.getDeviceDto().getDevicename(), e.getMessage());
                e.printStackTrace();
                appExceptionHandler.raiseException(e.getMessage());
            }
        }
        return "SuccessFullyInserted";
    }

    public Function<String, List<String>> getShelfNames = device -> {
        List<Shelf> shelves = shelfRepo.findAvailableShelvesInDevice(device);
        return shelves.stream()
                .map(Shelf::getName)
                .collect(Collectors.toList());
    };

    public static String extractNumberAfterLastUnderscore(String input) {
        int lastUnderscoreIndex = input.lastIndexOf('_');
        if (lastUnderscoreIndex != -1 && lastUnderscoreIndex < input.length() - 1) {
            return input.substring(lastUnderscoreIndex + 1);
        }
        return "";
    }

   /* public static Set<Long> getOrderIdSet(TmfResponce device) {
        return (Set<Long>) Stream.of(
                        device.getDeviceDto() != null ? Stream.of(device.getDeviceDto().getOrderid()) : Stream.empty(),
                        device.getCardDtos() != null ? device.getCardDtos().stream().map(CardDto::getOrderId) : Stream.empty(),
                        device.getPortDTOS() != null ? device.getPortDTOS().stream().map(PortDTO::getOrderId) : Stream.empty(),
                        device.getPluggableDTOS() != null ? device.getPluggableDTOS().stream().map(PluggableDTO::getOrderId) : Stream.empty(),
                        device.getLogicalPortDTOS() != null ? device.getLogicalPortDTOS().stream().map(LogicalPortDTO::getOrderId) : Stream.empty()
                )
                .flatMap(stream -> stream)
                .filter(orderId -> orderId != null)
                .collect(Collectors.toCollection(HashSet::new));
    }

    */
}




