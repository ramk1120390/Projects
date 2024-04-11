//package com.Network.Network.excel;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class DeviceTypeApi {
//    @Autowired
//    private DeviceTypeRepo deviceTypeRepo;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @PostMapping("/action")
//    public JsonNode handleAction(@RequestBody DeviceType deviceDataRequest,
//                                 @RequestParam(value = "action") String action) {
//        ObjectNode response = objectMapper.createObjectNode();
//        DeviceType deviceData = null;
//        try {
//            switch (action) {
//                case "view":
//                    deviceData = viewDeviceData(deviceDataRequest);
//                    response.putPOJO("DeviceData", deviceData);
//                    response.put("status", deviceData != null ? "view" : "create"); // Predict status based on result
//                    break;
//                case "viewedit":
//                    deviceData = updateDeviceData(deviceDataRequest);
//                    response.putPOJO("DeviceData", deviceData);
//                    response.put("status", deviceData != null ? "update" : "create");
//                    break;
//                case "create":
//                    deviceData = createDeviceData(deviceDataRequest);
//                    response.putPOJO("DeviceData", deviceData);
//                    response.put("status", deviceData != null ? "update" : "create");
//                    break;
//                default:
//                    throw new IllegalArgumentException("Invalid action: " + action);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            // Handle exceptions appropriately
//            return response;
//        }
//
//        return response;
//    }
//
//    private DeviceType viewDeviceData(DeviceType deviceDataRequest) {
//        DeviceType existingDeviceData = deviceTypeRepo.findByDeviceTypes(deviceDataRequest.getDeviceType());
//        if (existingDeviceData == null) {
//            return createDeviceData(deviceDataRequest);
//        }
//        return existingDeviceData;
//    }
//
//    private DeviceType createDeviceData(DeviceType deviceDataRequest) {
//        DeviceType existingDeviceData = deviceTypeRepo.findByDeviceTypes(deviceDataRequest.getDeviceType());
//        if (existingDeviceData != null) {
//            return updateDeviceData(deviceDataRequest);
//        } else {
//            return deviceTypeRepo.save(deviceDataRequest);
//        }
//    }
//
//    private DeviceType updateDeviceData(DeviceType deviceDataRequest) {
//        DeviceType existingDeviceData = deviceTypeRepo.findByDeviceTypes(deviceDataRequest.getDeviceType());
//        if (existingDeviceData != null) {
//            existingDeviceData.setDeviceType(deviceDataRequest.getDeviceType());
//            existingDeviceData.setDescription(deviceDataRequest.getDescription());
//            existingDeviceData.setDeviceOS(deviceDataRequest.getDeviceOS());
//            return deviceTypeRepo.save(existingDeviceData);
//        } else {
//            return deviceTypeRepo.save(deviceDataRequest);
//        }
//    }
//}
