package com.Network.Network.DevicemetamodelApi;

import com.Network.Network.DevicemetamodelPojo.*;
import com.Network.Network.DevicemetamodelRepo.*;
import com.Network.Network.Exception.AppExceptionHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController

public class ConnectionApi {
    @Autowired
    private AppExceptionHandler appExceptionHandler;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private DeviceRepo deviceRepo;
    @Autowired
    private ShelfRepo shelfRepo;
    @Autowired
    private SlotRepo slotRepo;
    @Autowired
    private PluggableRepo pluggableRepo;
    @Autowired
    private PortRepo portRepo;
    @Autowired
    private PhysicalConnectionRepo physicalConnectionRepo;
    @Autowired
    private CardRepo cardRepo;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LogicalConnectionRepo logicalConnectionRepo;
    @Autowired
    private LogicalPortRepo logicalPortRepo;
    @Autowired
    private AdditionalAttributeRepo additionalAttributeRepo;

    Logger logger = LoggerFactory.getLogger(ConnectionApi.class);

    @PostMapping("/CreatePhysicalConnection")
    public JsonNode CreatePhysicalConnection(@RequestParam(name = "orderId") Long orderId, @RequestBody PhysicalConnection physicalConnection) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Order> order = orderRepo.findById(orderId);
            if (order.isEmpty()) {
                appExceptionHandler.raiseException("Given Order not Found");
            }
            PhysicalConnection exPhysicalConnection = physicalConnectionRepo.findByName(physicalConnection.getName());
            if (exPhysicalConnection != null) {
                appExceptionHandler.raiseException("Given Physical Connection Already Found");
            }

            String aEndPortName = null;
            String zEndPortName = null;
            String aEndPluggableName = null;
            String zEndPluggableName = null;
            List<String> A_A_V = new ArrayList<>();
            List<String> A_A_K = new ArrayList<>();
            if (physicalConnection.getDevicea().equals(physicalConnection.getDeviceb())) {
                appExceptionHandler.raiseException("Device names cannot be the same to establish a connection");
            }
            Device exDeviceA = deviceRepo.findByDevicename(physicalConnection.getDevicea());
            Device exDeviceB = deviceRepo.findByDevicename(physicalConnection.getDeviceb());
            if (exDeviceA == null && exDeviceB == null) {
                appExceptionHandler.raiseException("Both devices not found: DeviceA=" + physicalConnection.getDevicea() + ", DeviceB=" + physicalConnection.getDeviceb());
            } else if (exDeviceA == null) {
                appExceptionHandler.raiseException("DeviceA not found: " + physicalConnection.getDevicea());
            } else if (exDeviceB == null) {
                appExceptionHandler.raiseException("DeviceB not found: " + physicalConnection.getDeviceb());
            }
            if (physicalConnection.getDeviceaport().contains("/")) {
                String[] aEndPortDetails = physicalConnection.getDeviceaport().split("/");
                int aEndShelfNo = Integer.parseInt(aEndPortDetails[0]);
                int aEndSlotNo = Integer.parseInt(aEndPortDetails[1]);
                int aEndPortNoOnCard = Integer.parseInt(aEndPortDetails[2]);
                String ShelfName = physicalConnection.getDevicea() + "_shelf_" + aEndShelfNo;
                Shelf exShelf = shelfRepo.findShelfByName(ShelfName);
                if (exShelf == null) {
                    appExceptionHandler.raiseException("Given Shelf not found: aEndShelfNo=" + aEndShelfNo);
                }
                String SlotName = physicalConnection.getDevicea() + "/" + aEndShelfNo + "/" + aEndSlotNo;
                System.out.println("SlotName" + SlotName);
                Card exCard = cardRepo.findCardsBySlotName(SlotName);
                if (exCard == null) {
                    appExceptionHandler.raiseException("No card exists on the given aEndSlotNo: " + aEndSlotNo);
                }
                Long cardid = exCard.getCardid();
                String cardName = exCard.getId().getCardname();
                String CardSlotName = cardName + cardid + "/" + aEndPortNoOnCard;
                Pluggable aEndPluggable = pluggableRepo.findByCardSlotName(CardSlotName);
                if (aEndPluggable != null) {
                    aEndPluggableName = aEndPluggable.getPlugablename();
                } else {
                    Port aEndDevicePort = portRepo.findByCardSlotName(CardSlotName);
                    if (aEndDevicePort != null) {
                        aEndPortName = aEndDevicePort.getPortname();
                    } else {
                        appExceptionHandler.raiseException("No Pluggable or Port exists on the given A-End shelf/slot/port position");
                    }
                }
            } else {
                Pluggable aEndPluggable = pluggableRepo.findByDeviceNameAndPositionOnDevice(physicalConnection.getDevicea(), Integer.parseInt(physicalConnection.getDeviceaport()));
                if (aEndPluggable != null) {
                    aEndPluggableName = aEndPluggable.getPlugablename();
                } else {
                    Port aEndDevicePort = portRepo.findByDeviceNameAndPositionOnDevice(physicalConnection.getDevicea(), Integer.parseInt(physicalConnection.getDeviceaport()));
                    if (aEndDevicePort != null) {
                        aEndPortName = aEndDevicePort.getPortname();
                    } else {
                        appExceptionHandler.raiseException("No Pluggable or Port exists on the given A-End shelf/slot/port position");
                    }
                }
            }
            if (physicalConnection.getDevicezport().contains("/")) {
                String[] aEndPortDetails = physicalConnection.getDevicezport().split("/");
                int aEndShelfNo = Integer.parseInt(aEndPortDetails[0]);
                int aEndSlotNo = Integer.parseInt(aEndPortDetails[1]);
                int aEndPortNoOnCard = Integer.parseInt(aEndPortDetails[2]);
                String ShelfName = physicalConnection.getDeviceb() + "_shelf_" + aEndShelfNo;
                Shelf exShelf = shelfRepo.findShelfByName(ShelfName);
                if (exShelf == null) {
                    appExceptionHandler.raiseException("Given Shelf not found: aEndShelfNo=" + aEndShelfNo);
                }
                String SlotName = physicalConnection.getDeviceb() + "/" + aEndShelfNo + "/" + aEndSlotNo;
                Card exCard = cardRepo.findCardsBySlotName(SlotName);
                if (exCard == null) {
                    appExceptionHandler.raiseException("No card exists on the given aEndSlotNo: " + aEndSlotNo);
                }
                Long cardid = exCard.getCardid();
                String cardName = exCard.getId().getCardname();
                String CardSlotName = cardName + cardid + "/" + aEndPortNoOnCard;
                Pluggable aEndPluggable = pluggableRepo.findByCardSlotName(CardSlotName);
                if (aEndPluggable != null) {
                    aEndPluggableName = aEndPluggable.getPlugablename();
                } else {
                    Port aEndDevicePort = portRepo.findByCardSlotName(CardSlotName);
                    if (aEndDevicePort != null) {
                        aEndPortName = aEndDevicePort.getPortname();
                    } else {
                        appExceptionHandler.raiseException("No Pluggable or Port exists on the given A-End shelf/slot/port position");
                    }
                }
            } else {
                Pluggable aEndPluggable = pluggableRepo.findByDeviceNameAndPositionOnDevice(physicalConnection.getDeviceb()
                        , Integer.parseInt(physicalConnection.getDevicezport()));
                if (aEndPluggable != null) {
                    zEndPluggableName = aEndPluggable.getPlugablename();
                } else {
                    Port aEndDevicePort = portRepo.findByDeviceNameAndPositionOnDevice(physicalConnection.getDeviceb()
                            , Integer.parseInt(physicalConnection.getDevicezport()));
                    if (aEndDevicePort != null) {
                        zEndPortName = aEndDevicePort.getPortname();
                    } else {
                        appExceptionHandler.raiseException("No Pluggable or Port exists on the given A-End shelf/slot/port position");
                    }
                }
            }
            if (physicalConnection.getAdditionalAttributes() != null && !physicalConnection.getAdditionalAttributes().isEmpty()) {
                for (AdditionalAttribute additionalAttributeDTO : physicalConnection.getAdditionalAttributes()) {
                    A_A_K.add(additionalAttributeDTO.getKey());
                    A_A_V.add(additionalAttributeDTO.getValue());
                }
            }
            logger.info("Creating Physical connection between devices {} and {} on ports {} and {} respectively",
                    physicalConnection.getDevicea(), physicalConnection.getDeviceb(), aEndPortName, zEndPortName);


            String portnamea = aEndPortName == null ? aEndPluggableName : zEndPortName;
            String portnameb = zEndPortName == null ? zEndPluggableName : zEndPortName;

            int success = physicalConnectionRepo.createPhysicalConnection(physicalConnection.getName(),
                    physicalConnection.getDevicea(), physicalConnection.getDeviceb(), physicalConnection.getDeviceaport(),
                    physicalConnection.getDevicezport(), physicalConnection.getConnectionType(),
                    physicalConnection.getBandwidth(), portnamea, portnameb, A_A_K.toArray(new String[0]),
                    A_A_V.toArray(new String[0]), 0);
            response.put("response", success);
            response.put("A-PortName", aEndPortName);
            response.put("zEndPortName", zEndPortName);
            response.put("aEndPluggableName", aEndPluggableName);
            response.put("zEndPluggableName", zEndPluggableName);
            return objectMapper.valueToTree(response);
        } catch (Exception e) {
            logger.error("Error occurred while CreatePhysicalConnection : {}", e.getMessage());
            e.printStackTrace(); // You may remove this line if you don't need stack trace in console
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("response", "Error: " + e.getMessage());
            return objectMapper.valueToTree(errorResponse);
        }

    }

    @PostMapping("/CreateLogicalConnection")
    public JsonNode CreateLogicalConnection(@RequestParam(name = "orderId") Long orderId, @RequestBody LogicalConnection logicalConnection) {
        Map<String, Object> response = new HashMap<>();
        try {
            LogicalPort logicalPortA = null;
            LogicalPort logicalPortZ = null;
            List<String> A_A_V = new ArrayList<>();
            List<String> A_A_K = new ArrayList<>();
            Long aEndPortId = null;
            Long zEndPortId = null;
            Long aEndPluggableId = null;
            Long zEndPluggableId = null;
            Optional<Order> order = orderRepo.findById(orderId);
            if (order.isEmpty()) {
                appExceptionHandler.raiseException("Given Order not Found");
            }
            LogicalConnection exLogicalConnection = logicalConnectionRepo.findByName(logicalConnection.getName());
            if (exLogicalConnection != null) {
                appExceptionHandler.raiseException("Given Logical Connection Already Found");
            }
            ArrayList<String> physicalConnections = logicalConnection.getPhysicalConnections();
            Set<String> seenConnections = new HashSet<>();
            for (String physicalConnection : physicalConnections) {
                if (!seenConnections.add(physicalConnection)) {
                    appExceptionHandler.raiseException("Duplicate physical connection: " + physicalConnection);
                }
            }
            Set<String> deviceNames = new HashSet<>();
            for (String physicalConnection : logicalConnection.getPhysicalConnections()) {
                if (physicalConnectionRepo.findByName(physicalConnection) == null) {
                    appExceptionHandler.raiseException("Physical connection with name " + physicalConnection + " doesn't exist");
                }
                String deviceNamesString = physicalConnectionRepo.findConcatenatedDevicesByName(physicalConnection);
                String[] deviceNamesArray = deviceNamesString.split(",");
                deviceNames.addAll(Arrays.asList(deviceNamesArray));
            }
            if (logicalConnection.getDeviceA().equals(logicalConnection.getDeviceZ())) {
                appExceptionHandler.raiseException("Device names cannot be the same to establish a connection");
            }
            Device exDeviceA = deviceRepo.findByDevicename(logicalConnection.getDeviceA());
            Device exDeviceB = deviceRepo.findByDevicename(logicalConnection.getDeviceZ());
            if (exDeviceA == null && exDeviceB == null) {
                appExceptionHandler.raiseException("Both devices not found: DeviceA=" + logicalConnection.getDeviceA() + ", DeviceB=" + logicalConnection.getDeviceZ());
            } else if (exDeviceA == null) {
                appExceptionHandler.raiseException("DeviceA not found: " + logicalConnection.getDeviceA());
            } else if (exDeviceB == null) {
                appExceptionHandler.raiseException("DeviceB not found: " + logicalConnection.getDeviceZ());
            }
            if (!deviceNames.contains(logicalConnection.getDeviceA()) || !deviceNames.contains(logicalConnection.getDeviceZ())) {
                appExceptionHandler.raiseException("The given list of physical connections do not connect Device A and Device Z");
            }
            String[] aEndDetails = logicalConnection.getDeviceALogicalPort().split("/");
            if (aEndDetails.length == 2) {
                logger.debug("creating logical connection on logical port {} of port/pluggable {}", aEndDetails[1], aEndDetails[0]);
                logicalPortA = logicalPortRepo.findByDeviceNameAndPosition(logicalConnection.getDeviceA(),
                        Integer.parseInt(aEndDetails[0]), Integer.parseInt(aEndDetails[1]));
            } else if (aEndDetails.length == 4) {
                int aEndShelfNo = Integer.parseInt(aEndDetails[0]);
                int aEndSlotNo = Integer.parseInt(aEndDetails[1]);
                int aEndPortNoOnCard = Integer.parseInt(aEndDetails[2]);
                int aEndPositionOnPort = Integer.parseInt(aEndDetails[3]);
                logger.debug("creating logical connection on logical port {} of port/pluggable {} of card slot {} of shelf slot {} ",
                        aEndDetails[3], aEndDetails[2], aEndDetails[1], aEndDetails[0]);
                String ShelfName = logicalConnection.getDeviceA() + "_shelf_" + aEndShelfNo;
                Shelf exShelf = shelfRepo.findShelfByName(ShelfName);
                if (exShelf == null) {
                    appExceptionHandler.raiseException("Given Shelf not found: aEndShelfNo=" + aEndShelfNo);
                }
                String SlotName = logicalConnection.getDeviceA() + "/" + aEndShelfNo + "/" + aEndSlotNo;
                Card exCard = cardRepo.findCardsBySlotName(SlotName);
                if (exCard == null) {
                    appExceptionHandler.raiseException("No card exists on the given aEndSlotNo: " + aEndSlotNo);
                }
                Long cardid = exCard.getCardid();
                String cardName = exCard.getId().getCardname();
                String CardSlotName = cardName + cardid + "/" + aEndPortNoOnCard;
                Pluggable aEndPluggable = pluggableRepo.findByCardSlotName(CardSlotName);
                if (aEndPluggable != null) {
                    aEndPluggableId = aEndPluggable.getId();
                } else {
                    Port aEndDevicePort = portRepo.findByCardSlotName(CardSlotName);
                    if (aEndDevicePort != null) {
                        aEndPortId = aEndDevicePort.getPortid();
                    } else {
                        appExceptionHandler.raiseException("No Pluggable or Port exists on the given A-End shelf/slot/port position");
                    }
                }
                logicalPortA = logicalPortRepo.findBylogicalPort(logicalConnection.getDeviceA(), aEndPortNoOnCard,
                        aEndPositionOnPort);
            } else {
                appExceptionHandler.raiseException("Invalid logical port format on A end");
            }
            if (logicalPortA == null) {
                appExceptionHandler.raiseException("Logical port on A end does not exist at the given position on the given " +
                        "physical port/pluggable");
            }
            System.out.println("crosed");
            //get z end details
            String[] zEndDetails = logicalConnection.getDeviceZLogicalPort().split("/");
            if (zEndDetails.length == 2) {
                System.out.println(zEndDetails[0] + ", " + zEndDetails[1] + ", output: " + logicalConnection.getDeviceZ());
                logger.info("creating logical connection on logical port {} of port/pluggable {}", aEndDetails[1], aEndDetails[0]);
                logicalPortZ = logicalPortRepo.findByDeviceNameAndPosition(logicalConnection.getDeviceZ(),
                        Integer.parseInt(zEndDetails[0]), Integer.parseInt(zEndDetails[1]));
            } else if (zEndDetails.length == 4) {
                int aEndShelfNo = Integer.parseInt(zEndDetails[0]);
                int aEndSlotNo = Integer.parseInt(zEndDetails[1]);
                int aEndPortNoOnCard = Integer.parseInt(zEndDetails[2]);
                int aEndPositionOnPort = Integer.parseInt(zEndDetails[3]);
                logger.debug("creating logical connection on logical port {} of port/pluggable {} of card slot {} of shelf slot {} ",
                        aEndDetails[3], aEndDetails[2], aEndDetails[1], aEndDetails[0]);
                String ShelfName = logicalConnection.getDeviceZ() + "_shelf_" + aEndShelfNo;
                Shelf exShelf = shelfRepo.findShelfByName(ShelfName);
                if (exShelf == null) {
                    appExceptionHandler.raiseException("Given Shelf not found: aEndShelfNo=" + aEndShelfNo);
                }
                String SlotName = logicalConnection.getDeviceZ() + "/" + aEndShelfNo + "/" + aEndSlotNo;
                Card exCard = cardRepo.findCardsBySlotName(SlotName);
                if (exCard == null) {
                    appExceptionHandler.raiseException("No card exists on the given aEndSlotNo: " + aEndSlotNo);
                }
                Long cardid = exCard.getCardid();
                String cardName = exCard.getId().getCardname();
                String CardSlotName = cardName + cardid + "/" + aEndPortNoOnCard;
                Pluggable aEndPluggable = pluggableRepo.findByCardSlotName(CardSlotName);
                if (aEndPluggable != null) {
                    aEndPluggableId = aEndPluggable.getId();
                } else {
                    Port aEndDevicePort = portRepo.findByCardSlotName(CardSlotName);
                    if (aEndDevicePort != null) {
                        aEndPortId = aEndDevicePort.getPortid();
                    } else {
                        appExceptionHandler.raiseException("No Pluggable or Port exists on the given A-End shelf/slot/port position");
                    }
                }

                logicalPortZ = logicalPortRepo.findBylogicalPort(logicalConnection.getDeviceZ(), aEndPortNoOnCard,
                        aEndPositionOnPort);
            } else {
                appExceptionHandler.raiseException("Invalid logical port format on A end");
            }
            if (logicalPortZ == null) {
                appExceptionHandler.raiseException("Logical port on A end does not exist at the given position on the given " +
                        "physical port/pluggable");
            }
            if (logicalConnection.getAdditionalAttributes() != null && !logicalConnection.getAdditionalAttributes().isEmpty()) {
                for (AdditionalAttribute additionalAttributeDTO : logicalConnection.getAdditionalAttributes()) {
                    A_A_K.add(additionalAttributeDTO.getKey());
                    A_A_V.add(additionalAttributeDTO.getValue());
                }
            }
            logger.info("Creating Physical connection between devices {} and {} on ports {} and {} respectively",
                    logicalConnection.getDeviceA(), logicalConnection.getDeviceZ(), logicalPortA.getName(), logicalPortZ.getName());
            int success = logicalConnectionRepo.createLogicalConnection(
                    logicalConnection.getName(),
                    logicalConnection.getDeviceA(),
                    logicalConnection.getDeviceZ(),
                    logicalConnection.getDeviceALogicalPort(),
                    logicalConnection.getDeviceZLogicalPort(),
                    logicalConnection.getConnectionType(),
                    logicalConnection.getBandwidth(),
                    deviceNames.toArray(new String[0]),
                    physicalConnections.toArray(new String[0]),
                    logicalPortA.getName(),
                    logicalPortZ.getName(),
                    A_A_K.toArray(new String[0]),
                    A_A_V.toArray(new String[0]),
                    0 // Assuming this is the default value for success
            );
            response.put("response", success);
            response.put("A-PortName", logicalPortA.getName());
            response.put("zEndPortName", logicalPortZ.getName());
            return objectMapper.valueToTree(response);
        } catch (Exception e) {
            logger.error("Error occurred while CreatePhysicalConnection : {}", e.getMessage());
            e.printStackTrace(); // You may remove this line if you don't need stack trace in console
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("response", "Error: " + e.getMessage());
            return objectMapper.valueToTree(errorResponse);
        }
    }

   /* @DeleteMapping("/deletePhysicalConnection")
    @Transactional
    public JsonNode deletePhysicalConnection(@RequestParam(name = "PhysicalConnection") String PhysicalConnection) {
        JsonNode response;
        try {
            String PhysicalConnectionNameLower = PhysicalConnection.toLowerCase();
            logger.info("Inside deletePhysicalConnection for Name: {}", PhysicalConnectionNameLower);

            PhysicalConnection exPhysical = physicalConnectionRepo.findByName(PhysicalConnectionNameLower);
            if (exPhysical == null) {
                logger.warn("No PhysicalConnection found with the name: {}", PhysicalConnectionNameLower);
                appExceptionHandler.raiseException("Given PhysicalConnectionName " + PhysicalConnection + " doesn't Exist");
            }
            Long ConnectionId = exPhysical.getPhysicalconnection_id();
            List<String> connectData = logicalConnectionRepo.findAll().stream()
                    .filter(logicalConnection ->
                            logicalConnection.getName().equals(PhysicalConnectionNameLower) &&
                                    logicalConnection.getPhysicalConnections().contains(PhysicalConnectionNameLower))
                    .flatMap(logicalConnection -> logicalConnection.getPhysicalConnections().stream())
                    .collect(Collectors.toList());
            if (connectData != null && !connectData.isEmpty()) {
                String joinedConnectData = connectData.stream().collect(Collectors.joining(", "));
                logger.warn("Cannot delete PhysicalConnection {} as it has associated PhysicalConnection: {}", PhysicalConnectionNameLower, joinedConnectData);
                appExceptionHandler.raiseException("Cannot delete device " + PhysicalConnectionNameLower + " as it has associated PhysicalConnection: " + joinedConnectData);
            }
            List<Long> aaIds = physicalConnectionRepo.findAll().stream()
                    .filter(physicalConnection -> physicalConnection.getPhysicalconnection_id().equals(ConnectionId))
                    .flatMap(device -> device.getAdditionalAttributes().stream())
                    .map(AdditionalAttribute::getId)
                    .collect(Collectors.toList());
            logger.info("Deleting PhysicalConnection with Name: {}", PhysicalConnectionNameLower);
            physicalConnectionRepo.deleteByPhysicalconnection_id(ConnectionId);
            if (!aaIds.isEmpty()) {
                logger.info("Deleting additional attributes associated with PhysicalConnection: {}", PhysicalConnectionNameLower);
                additionalAttributeRepo.deleteAdditionalAttributesByIds(aaIds);
            }

            response = objectMapper.createObjectNode()
                    .put("status", "Success")
                    .put("message", "PhysicalConnection deleted successfully")
                    .put("deletedPhysicalConnectionName", PhysicalConnectionNameLower);
            logger.info("PhysicalConnection deleted successfully for Name: {}", PhysicalConnectionNameLower);

        } catch (Exception e) {
            logger.error("An error occurred while deleting PhysicalConnection with name: {}", PhysicalConnection, e);
            response = objectMapper.createObjectNode()
                    .put("status", "Error")
                    .put("message", "An error occurred while deleting PhysicalConnection")
                    .put("error", e.getMessage());
        }
        return response;
    }

    */

   /* @DeleteMapping("/deleteLogicalConnection")
    @Transactional
    public JsonNode deleteLogicalConnection(@RequestParam(name = "LogicalConnection") String LogicalConnection) {
        JsonNode response;
        try {
            String LogicalConnectionNameLower = LogicalConnection.toLowerCase();
            logger.info("Inside deleteLogicalConnection for Name: {}", LogicalConnectionNameLower);
            LogicalConnection exLogical = logicalConnectionRepo.findByName(LogicalConnectionNameLower);
            if (exLogical == null) {
                logger.warn("No LogicalConnection found with the name: {}", LogicalConnectionNameLower);
                appExceptionHandler.raiseException("Given LogicalConnectionName " + LogicalConnectionNameLower + " doesn't Exist");
            }
            Long ConnectionId = exLogical.getLogicalconnection_id();

            List<Long> aaIds = logicalConnectionRepo.findAll().stream()
                    .filter(logicalConnection -> logicalConnection.getLogicalconnection_id().equals(ConnectionId))
                    .flatMap(logicalConnection -> logicalConnection.getAdditionalAttributes().stream())
                    .map(AdditionalAttribute::getId)
                    .collect(Collectors.toList());
            logger.info("Deleting deleteLogicalConnection with Name: {}", LogicalConnectionNameLower);
            physicalConnectionRepo.deleteByPhysicalconnection_id(ConnectionId);
            if (!aaIds.isEmpty()) {
                logger.info("Deleting additional attributes associated with LogicalConnection: {}", LogicalConnectionNameLower);
                additionalAttributeRepo.deleteAdditionalAttributesByIds(aaIds);
            }
            logicalConnectionRepo.deleteByLogicalconnection_id(ConnectionId);
            response = objectMapper.createObjectNode()
                    .put("status", "Success")
                    .put("message", "PhysicalConnection deleted successfully")
                    .put("LogicalConnectionName", LogicalConnectionNameLower);
            logger.info("LogicalConnection deleted successfully for Name: {}", LogicalConnectionNameLower);

        } catch (Exception e) {
            logger.error("An error occurred while deleting LogicalConnection with name: {}", LogicalConnection, e);
            response = objectMapper.createObjectNode()
                    .put("status", "Error")
                    .put("message", "An error occurred while deleting LogicalConnection")
                    .put("error", e.getMessage());
        }
        return response;
    }


    */

}

//TODO update PhysicalConnection Logical Connection


