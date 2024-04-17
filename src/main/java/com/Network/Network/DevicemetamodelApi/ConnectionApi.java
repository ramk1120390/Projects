package com.Network.Network.DevicemetamodelApi;

import com.Network.Network.DevicemetamodelPojo.*;
import com.Network.Network.DevicemetamodelRepo.*;
import com.Network.Network.Exception.AppExceptionHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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

            JSONObject details = new JSONObject();
            details.put("A-PortName", aEndPortName);
            details.put("zEndPortName", zEndPortName);
            details.put("aEndPluggableName", aEndPluggableName);
            details.put("zEndPluggableName", zEndPluggableName);

            String portnamea = aEndPortName == null ? aEndPluggableName : zEndPortName;
            String portnameb = zEndPortName == null ? zEndPluggableName : zEndPortName;

            int success = physicalConnectionRepo.createPhysicalConnection(physicalConnection.getName(),
                    physicalConnection.getDevicea(), physicalConnection.getDeviceb(), physicalConnection.getDeviceaport(),
                    physicalConnection.getDevicezport(), physicalConnection.getConnectionType(),
                    physicalConnection.getBandwidth(), portnamea, portnameb, A_A_K.toArray(new String[0]),
                    A_A_V.toArray(new String[0]), 0);
            response.put("response", success);
            return objectMapper.valueToTree(response);
        } catch (Exception e) {
            logger.error("Error occurred while CreatePhysicalConnection : {}", e.getMessage());
            e.printStackTrace(); // You may remove this line if you don't need stack trace in console
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("response", "Error: " + e.getMessage());
            return objectMapper.valueToTree(errorResponse);
        }

    }
}


