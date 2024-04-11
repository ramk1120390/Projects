package com.Network.Network.DevicemetamodelApi;

import com.Network.Network.DevicemetamodelPojo.*;
import com.Network.Network.DevicemetamodelRepo.*;
import com.Network.Network.Exception.AppExceptionHandler;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ServiceApi {
    @Autowired
    private ServiceRepo serviceRepo;
    @Autowired
    private AdditionalAttributeRepo additionalAttributeRepo;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private DeviceRepo deviceRepo;
    @Autowired
    private AppExceptionHandler appExceptionHandler;
    @Autowired
    OrderRepo orderRepo;

    @PostMapping("/create")
    public Service createServiceWithAdditionalAttribute(@RequestParam("orderid") Long orderid, @RequestBody ServiceDto serviceDTO) {
        Service savedService = null;
        try {
            Optional<Order> order = orderRepo.findById(orderid);
            Order exOrder=order.get();
            if (!order.isPresent()) {
                appExceptionHandler.raiseException("given order is not found");
            }
            Service existingService = serviceRepo.findByName(serviceDTO.getName());
            if (existingService != null) {
                appExceptionHandler.raiseException("Service with the given name already exists.");
            }
            Customer customer = customerRepo.findByCustomername(serviceDTO.getCustomer());
            if (customer == null) {
                appExceptionHandler.raiseException("Customer with the given name not found.");
            }
            List<Device> devices = new ArrayList<>();
            if (serviceDTO.getDevices() != null && !serviceDTO.getDevices().isEmpty()) {
                for (String deviceName : serviceDTO.getDevices()) {
                    Device device = deviceRepo.findByDevicename(deviceName);
                    if (device == null) {
                        appExceptionHandler.raiseException("Device with name " + deviceName + " not found.");
                    }
                    devices.add(device);
                }
            }
            Service service = new Service();
            service.setName(serviceDTO.getName());
            service.setType(serviceDTO.getType());
            service.setOperationalState(serviceDTO.getOperationalState());
            service.setAdministrativeState(serviceDTO.getAdministrativeState());
            service.setNotes(serviceDTO.getNotes());
            service.setCustomer(customer);
            service.setDevices(devices);
            service.setOrder(exOrder);
            savedService = serviceRepo.save(service);
            List<AdditionalAttribute> additionalAttributes = new ArrayList<>();
            if (serviceDTO.getAdditionalAttributes() != null && !serviceDTO.getAdditionalAttributes().isEmpty()) {
                for (AdditionalAttribute additionalAttributeDTO : serviceDTO.getAdditionalAttributes()) {
                    AdditionalAttribute additionalAttribute = new AdditionalAttribute();
                    additionalAttribute.setKey(additionalAttributeDTO.getKey());
                    additionalAttribute.setValue(additionalAttributeDTO.getValue());
                    AdditionalAttribute savedAdditionalAttribute = additionalAttributeRepo.save(additionalAttribute);
                    additionalAttributes.add(savedAdditionalAttribute);
                }
            }
            savedService.setAdditionalAttributes(additionalAttributes);
            savedService = serviceRepo.save(savedService);
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return savedService;
    }

    @GetMapping("/service/{id}")
    public Service getServiceById(@PathVariable Long id) {
        // Retrieve the Service entity by its ID
        Optional<Service> optionalService = serviceRepo.findById(id);
        if (optionalService.isPresent()) {
            // Get the Service entity
            Service service = optionalService.get();

            // Initialize the lazy-loaded properties (devices and additionalAttributes)
            // Hibernate.initialize(service.getDevices());
            //Hibernate.initialize(service.getAdditionalAttributes());

            // Return the initialized Service entity
            return service;
        } else {
            // Handle the case where the Service entity with the given ID does not exist
            throw new EntityNotFoundException("Service not found with id: " + id);
        }
    }

    @PostMapping("/update/{serviceId}")
    public Service updateServiceWithAdditionalAttribute(@PathVariable Long serviceId, @RequestBody ServiceDto serviceDTO) {
        Service savedService = null;
        try {
            // Find the service by ID
            Optional<Service> serviceOptional = serviceRepo.findById(serviceId);
            if (!serviceOptional.isPresent()) {
                appExceptionHandler.raiseException("Service with ID " + serviceId + " not found.");
            }
            Service existingService = serviceOptional.get();
            if (!serviceDTO.getName().equals(existingService.getName()))
            {
               existingService=serviceRepo.findByName(serviceDTO.getName());
               if (existingService!=null)
               {
                   appExceptionHandler.raiseException("given Service name  already exists");
               }
            }
            // Update service attributes
            existingService.setName(serviceDTO.getName());
            existingService.setType(serviceDTO.getType());
            existingService.setOperationalState(serviceDTO.getOperationalState());
            existingService.setAdministrativeState(serviceDTO.getAdministrativeState());
            existingService.setNotes(serviceDTO.getNotes());
            // Fetch Customer object by name
            Customer customer = customerRepo.findByCustomername(serviceDTO.getCustomer());
            if (customer == null) {
                appExceptionHandler.raiseException("Customer with the given name not found.");
            }
            existingService.setCustomer(customer);
            // Fetch Device objects if devices are provided
            List<Device> devices = new ArrayList<>();
            if (serviceDTO.getDevices() != null && !serviceDTO.getDevices().isEmpty()) {
                for (String deviceName : serviceDTO.getDevices()) {
                    Device device = deviceRepo.findByDevicename(deviceName);
                    if (device == null) {
                        appExceptionHandler.raiseException("Device with name " + deviceName + " not found.");
                    }
                    devices.add(device);
                }
            }
            existingService.setDevices(devices);
            savedService = serviceRepo.save(existingService);
            // Update additional attributes
            List<AdditionalAttribute> additionalAttributes = new ArrayList<>();
            if (serviceDTO.getAdditionalAttributes() != null && !serviceDTO.getAdditionalAttributes().isEmpty()) {
                for (AdditionalAttribute additionalAttributeDTO : serviceDTO.getAdditionalAttributes()) {
                    AdditionalAttribute additionalAttribute = new AdditionalAttribute();
                    additionalAttribute.setKey(additionalAttributeDTO.getKey());
                    additionalAttribute.setValue(additionalAttributeDTO.getValue());
                    AdditionalAttribute savedAdditionalAttribute = additionalAttributeRepo.save(additionalAttribute);
                    additionalAttributes.add(savedAdditionalAttribute);
                }
            }
            savedService.setAdditionalAttributes(additionalAttributes);
            savedService = serviceRepo.save(savedService);
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return savedService;
    }



}
