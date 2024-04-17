package com.Network.Network.DevicemetamodelApi;

import java.sql.CallableStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Network.Network.DevicemetamodelPojo.CardModels;
import com.Network.Network.DevicemetamodelPojo.DeviceMetaModel;
import com.Network.Network.DevicemetamodelPojo.DeviceModel;
import com.Network.Network.DevicemetamodelPojo.Vendors;
import com.Network.Network.DevicemetamodelRepo.CardModelRepo;
import com.Network.Network.DevicemetamodelRepo.DeviceMetaModelRepo;
import com.Network.Network.DevicemetamodelRepo.DeviceModelRepo;
import com.Network.Network.DevicemetamodelRepo.VendorRepo;
import com.Network.Network.Exception.AppExceptionHandler;

import jakarta.transaction.Transactional;

@RestController
public class DeviceMetaModelApi {
    Logger logger = LoggerFactory.getLogger(DeviceMetaModelApi.class);
    @Autowired
    DeviceMetaModelRepo deviceMetaModelRepo;
    @Autowired
    AppExceptionHandler appExceptionHandler;
    @Autowired
    VendorRepo vendorRepo;
    @Autowired
    DeviceModelRepo deviceModelRepo;
    @Autowired
    CardModelRepo cardModelRepo;

    @PostMapping("/createDeviceMetaModel")
    public DeviceMetaModel createDeviceMetaModel(@RequestBody DeviceMetaModel deviceMetaModel) {
        DeviceMetaModel existingDeviceModel = null;
        String deviceModelLowerCase = deviceMetaModel.getDeviceModel().toLowerCase();
        try {
            deviceMetaModel.setDeviceModel(deviceModelLowerCase);
            logger.info("Inside createDeviceMetaModel for modelling: {}", deviceMetaModel);

            if (deviceMetaModel.getVendor() == null || deviceMetaModel.getVendor().trim().isEmpty()) {
                throw new IllegalArgumentException("Device meta model vendor cannot be empty");
            }

            existingDeviceModel = deviceMetaModelRepo.findByDeviceModel(deviceModelLowerCase);
            if (existingDeviceModel != null) {
                throw new IllegalArgumentException("Given device model already exists: " + existingDeviceModel.toString());
            }

            DeviceMetaModel savedDeviceMetaModel = deviceMetaModelRepo.save(deviceMetaModel);
            deviceModelRepo.save(new DeviceModel(savedDeviceMetaModel.getDeviceModel()));

            for (String cardModel : savedDeviceMetaModel.getAllowedCardList()) {
                cardModelRepo.save(new CardModels(cardModel));
            }

            vendorRepo.save(new Vendors(savedDeviceMetaModel.getVendor()));

            return savedDeviceMetaModel; // Return the newly created DeviceMetaModel
        } catch (Exception e) {
            logger.error("Error occurred while creating device meta model: {}", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


    @PostMapping("/device-meta-model")//update
    public ResponseEntity<String> insertDeviceMetaModel(@RequestParam("name") String model, @RequestBody DeviceMetaModel request) {
        model = model.toLowerCase();
        try {
            request.setDeviceModel(model);
            logger.info("Inside createDeviceMetaModel for modelling: {}", model);

            if (request.getVendor() == null || request.getVendor().trim().isEmpty()) {
                throw new IllegalArgumentException("Device meta model vendor cannot be empty");
            }

            // DeviceMetaModel existingModel = deviceMetaModelRepo.findByDeviceModel(model);
            //if (existingModel != null) {
            //appExceptionHandler.raiseException("Device model alredy presnt"+existingModel.toString());
            //}

            int success = deviceMetaModelRepo.insert(
                    request.getDeviceModel(),
                    request.getPartNumber(),
                    request.getVendor(),
                    request.getHeight(),
                    request.getDepth(),
                    request.getWidth(),
                    request.getShelvesContained(),
                    request.getNumOfRackPositionOccupied(),
                    request.getAllowedCardList().toArray(new String[0]),
                    1
            );

            if (success == 1) {
                deviceModelRepo.save(new DeviceModel(request.getDeviceModel()));
                for (String cardModel : request.getAllowedCardList()) {
                    cardModelRepo.save(new CardModels(cardModel));
                }
                vendorRepo.save(new Vendors(request.getVendor()));

                return ResponseEntity.ok("Device meta model inserted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error occurred while inserting device meta model.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while inserting device meta model.");
        }
    }

    @GetMapping("/getDeviceMetaModel")
    public DeviceMetaModel getDeviceMetaModel(@RequestParam(name = "deviceModel") String name) {
        String deviceModel = name.toLowerCase();
        DeviceMetaModel existingDeviceModel = null;
        try {
            logger.info("Inside getDeviceMetaModel for model: {}", deviceModel);
            existingDeviceModel = deviceMetaModelRepo.findByDeviceModel(deviceModel);
            if (existingDeviceModel == null) {
                appExceptionHandler.raiseException("Given Device Model doesn't Exist");
            }
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return existingDeviceModel;
    }

    @DeleteMapping("/deleteDeviceMetaModel")
    @Transactional
    public ResponseEntity<Map<String, Object>> deleteDeviceMetaModel(@RequestParam(name = "deviceModel") String deviceModel) {
        deviceModel = deviceModel.toLowerCase();
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK; // Default status is OK

        try {
            logger.info("Inside deleteDeviceMetaModel for model: {}", deviceModel);
            DeviceMetaModel existingDeviceModel = deviceMetaModelRepo.findByDeviceModel(deviceModel);
            if (existingDeviceModel == null) {
                appExceptionHandler.raiseException("Given Device Model doesn't Exist");
            }
            deviceMetaModelRepo.deleteByDeviceModel(deviceModel);
            response.put("status", "Success");
            response.put("message", "Device model deleted successfully");
            response.put("deletedDeviceModel", deviceModel);
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            response.put("status", "Error");
            response.put("message", "An error occurred while deleting device model");
            response.put("error", e.getMessage()); // Optionally include the error message for debugging
        }
        return new ResponseEntity<>(response, status);
    }

    @GetMapping("/getAllModels")
    public ArrayList<DeviceMetaModel> getAllModels() {
        ArrayList<DeviceMetaModel> allModels = new ArrayList<>();
        try {
            logger.info("Inside getAllModels");
            allModels = (ArrayList<DeviceMetaModel>) deviceMetaModelRepo.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return allModels;
    }

    @GetMapping("/getAllModelsOfVendor")
    public ArrayList<DeviceMetaModel> getAllModelsOfVendor(@RequestParam(name = "vendor") String vendor) {
        ArrayList<DeviceMetaModel> allModels = new ArrayList<>();
        try {
            logger.info("Inside getAllModelsOfVendor for vendor: {}", vendor);
            allModels = (ArrayList<DeviceMetaModel>) deviceMetaModelRepo.findByVendor(vendor);
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return allModels;
    }

    //TODO if plugable that device  name change that also i need change device name cardname position device....


}
	
	    
	
	
	


