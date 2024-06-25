package com.Network.Network.DevicemetamodelApi;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.Network.Network.DevicemetamodelPojo.*;
import com.Network.Network.DevicemetamodelRepo.DeviceRepo;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.Network.Network.DevicemetamodelRepo.CustomerRepo;
import com.Network.Network.DevicemetamodelRepo.OrderRepo;
import com.Network.Network.Exception.AppExceptionHandler;

@RestController
public class OrderApi {
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private AppExceptionHandler appExceptionHandler;
    Logger logger = LoggerFactory.getLogger(OrderApi.class);


    @GetMapping("hi")
    public String test() {
        return "hi";
    }

    @PostMapping("/CreateOrder")
    public Order createOrder(@RequestParam("customerName") String customerName, @RequestBody OrderDto dto) {
        customerName = customerName.replaceAll("\\s+", "").toLowerCase();
        Order response = null;
        try {
            Customer existingCustomer = customerRepo.findByCustomername(customerName);
            if (existingCustomer == null) {
                appExceptionHandler.raiseException("Given Customer with name " + customerName + " not found");
            }

            // Check if the customer name provided in the request body matches the provided
            // customer name in the URL
            if (dto.getCustomer() != null && !existingCustomer.getCustomername().equalsIgnoreCase(dto.getCustomer())) {
                appExceptionHandler.raiseException(
                        "Customer name in the request body must match with the provided customer name in the URL");
            }
            Order newOrder = new Order();
            newOrder.setStatus(dto.getStatus());
            newOrder.setCategory(dto.getCategory());
            newOrder.setDescription(dto.getDescription());
            // newOrder.setUpdatedDate(dto.getUpdatedDate());
            newOrder.setCreatedDate(LocalDate.now());
            // newOrder.setPreviousStatus(dto.getPreviousStatus());
            newOrder.setCustomer(existingCustomer);
            // Save the new order
            response = orderRepo.save(newOrder);
            logger.info("Successfully created order: {}", response);
        } catch (Exception e) {
            logger.error("Error occurred while creating order: {}", e.getMessage());
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @PostMapping("/UpdateOrder")
    public Order updateOrder(@RequestParam("o_id") Long orderId, @RequestBody OrderDto dto) {
        Order responce = null;
        try {
            Optional<Order> existingOrderOpt = orderRepo.findById(orderId);

            if (!existingOrderOpt.isPresent()) {
                appExceptionHandler.raiseException("Order with ID " + orderId + " not found");
            }

            Order existingOrder = existingOrderOpt.get();

            // Update status
            String updatedStatus = (dto.getStatus() == null) ? existingOrder.getStatus() : dto.getStatus();
            existingOrder.setStatus(updatedStatus);

            // Update category
            String updatedCategory = (dto.getCategory() == null) ? existingOrder.getCategory() : dto.getCategory();
            existingOrder.setCategory(updatedCategory);

            // Update description
            String updatedDescription = (dto.getDescription() == null) ? existingOrder.getDescription()
                    : dto.getDescription();
            existingOrder.setDescription(updatedDescription);

            existingOrder.setUpdatedDate(LocalDate.now());

            // Update createdDate
            LocalDate updatedCreatedDate = (dto.getCreatedDate() == null) ? existingOrder.getCreatedDate()
                    : dto.getCreatedDate();
            existingOrder.setCreatedDate(updatedCreatedDate);

            // Update previousStatus
            String updatedPreviousStatus = (dto.getPreviousStatus() == null) ? existingOrder.getPreviousStatus()
                    : dto.getPreviousStatus();
            existingOrder.setPreviousStatus(updatedPreviousStatus);

            // Update customer
            Customer updatedCustomer = (dto.getCustomer() == null) ? existingOrder.getCustomer()
                    : customerRepo.findByCustomername(dto.getCustomer());
            if (updatedCustomer == null) {
                appExceptionHandler.raiseException("Customer with name " + dto.getCustomer() + " not found");
            }
            existingOrder.setCustomer(updatedCustomer);

            // Save the updated order
            responce = orderRepo.save(existingOrder);

            logger.info("Successfully updated order with ID {}: {}", orderId, responce);

        } catch (Exception e) {
            logger.error("Error occurred while updating order with ID {}: {}", orderId, e.getMessage());
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return responce;
    }

    @Autowired
    private DeviceRepo deviceRepo;

    @DeleteMapping("/deleteOrder")
    public JSONObject deleteOrder(@RequestParam(name = "orderId") Long orderid) {
        logger.info("Inside deleteOrder, order value received: {}", orderid);
        JSONObject response = new JSONObject();
        try {
            logger.debug("Inside deleteOrder: {}", orderid);
            Optional<Order> orderDeatails = orderRepo.findById(orderid);
            if (orderDeatails.isPresent()) {
                appExceptionHandler.raiseException("Given order id  not available");
            }
            List<String> Devicedata = deviceRepo.findAll().stream()
                    .filter(device -> device.getOrder().getId().equals(orderid))
                    .map(Device::getDevicename)
                    .collect(Collectors.toList());
            if (!Devicedata.isEmpty()) {
                appExceptionHandler.raiseException("Given order related with Device");
            }
            orderRepo.deleteByOrderId(orderid);
            response.put("status", "Success");
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    //TODO implement get all orders ,getby id,getby orders by customer name,need find with conataing order id
}
