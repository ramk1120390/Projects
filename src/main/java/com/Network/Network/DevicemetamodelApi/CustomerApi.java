package com.Network.Network.DevicemetamodelApi;

import java.util.regex.Pattern;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.Network.Network.DevicemetamodelPojo.Customer;

import com.Network.Network.DevicemetamodelRepo.CustomerRepo;
import com.Network.Network.Exception.AppExceptionHandler;

@RestController
public class CustomerApi {
	@Autowired
	private CustomerRepo customerRepo;
	@Autowired
	private AppExceptionHandler appExceptionHandler;
	Logger logger = LoggerFactory.getLogger(CustomerApi.class);
	
	String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	String phoneRegex = "\\d{10}"; // assuming a 10-digit phone number
	// Compile the regex patterns
	Pattern emailPattern = Pattern.compile(emailRegex);
	Pattern phonePattern = Pattern.compile(phoneRegex);

	@PostMapping("/CreateCustomer")
	public Customer createCustomer(@RequestBody Customer customer) {
		Customer responce = null;
		try {
			// Check if mandatory fields are filled
			if (customer.getCustomername() == null || customer.getEmail() == null || customer.getContactNo() == null) {
				appExceptionHandler.raiseException("Fill all the mandatory fields");
			}
			

			// Convert customer name, email, and contact number to lowercase and trim
			String name = customer.getCustomername().replaceAll("\\s+", "");
			name = name.toLowerCase();
			String email = customer.getEmail().replaceAll("\\s", "").toLowerCase();
			String contact = customer.getContactNo().replaceAll("\\s", "").toLowerCase();
			if (!emailPattern.matcher(email).matches()) {
				appExceptionHandler.raiseException("Invalid email format: " + email);
			}

			// Validate phone number using regex pattern
			if (!phonePattern.matcher(contact).matches()) {
				appExceptionHandler.raiseException("Invalid phone number format: " + contact);
			}
			// Check if customer with the same name already exists
			Customer existingCustomer = customerRepo.findByCustomername(name);
			if (existingCustomer != null) {
				appExceptionHandler.raiseException("Customer with the same name already exists: " + existingCustomer);
			}

			// Check if customer with the same email already exists
			existingCustomer = customerRepo.findByEmail(email);
			if (existingCustomer != null) {
				appExceptionHandler.raiseException("Customer with the same email already exists: " + existingCustomer);
			}
			// Check if customer with the same contact number already exists
			existingCustomer = customerRepo.findByContactNo(contact);
			if (existingCustomer != null) {
				appExceptionHandler
						.raiseException("Customer with the same contact number already exists: " + existingCustomer);
			}
			Customer insert = new Customer();
			insert.setCustomername(name);
			insert.setContactNo(contact);
			insert.setEmail(email);
			insert.setDescription(customer.getDescription());
			responce = customerRepo.save(insert);
		} catch (Exception e) {
			e.printStackTrace();
			appExceptionHandler.raiseException(e.getMessage());
		}
		return responce;
	}

	@PutMapping("/UpdateCustomer")
	public Customer updateCustomer(@RequestParam("name") String name, @RequestBody Customer customer) {
	    Customer response = null;
	    name = name.replaceAll("\\s+", "").toLowerCase(); // Trim and convert to lowercase
	    try {
	        // Find the existing customer by name
	        Customer existingCustomer = customerRepo.findByCustomername(name);
	        if (existingCustomer == null) {
	            appExceptionHandler.raiseException("Given customer name not found");
	        }

	        // Update customer details
	        String updatedCustomerName = (customer.getCustomername() == null) ? existingCustomer.getCustomername()
	                : customer.getCustomername().replaceAll("\\s+", "").toLowerCase(); // Trim and convert to lowercase

	        String updatedEmail = (customer.getEmail() == null) ? existingCustomer.getEmail()
	                : customer.getEmail().replaceAll("\\s+", "").toLowerCase(); // Trim and convert to lowercase

	        String updatedPhone = (customer.getContactNo() == null) ? existingCustomer.getContactNo()
	                : customer.getContactNo().replaceAll("\\s+", "").toLowerCase(); // Trim and convert to lowercase

	        String updatedDesc = (customer.getDescription() == null) ? existingCustomer.getDescription()
	                : customer.getDescription();

	        // Validate email using regex pattern
	        if (!emailPattern.matcher(updatedEmail).matches()) {
	            appExceptionHandler.raiseException("Invalid email format: " + updatedEmail);
	        }

	        // Validate phone number using regex pattern
	        if (!phonePattern.matcher(updatedPhone).matches()) {
	            appExceptionHandler.raiseException("Invalid phone number format: " + updatedPhone);
	        }

	        // Check if updated customer name already exists
	        if (!name.equals(updatedCustomerName)) {
	            Customer customerWithNewName = customerRepo.findByCustomername(updatedCustomerName);
	            if (customerWithNewName != null) {
	                appExceptionHandler.raiseException("Customer with given name already exists: " + customerWithNewName);
	            }
	        }

	        // Check if updated email already exists
	        if (!updatedEmail.equals(existingCustomer.getEmail())) {
	            Customer customerWithNewEmail = customerRepo.findByEmail(updatedEmail);
	            if (customerWithNewEmail != null) {
	                appExceptionHandler.raiseException("Customer with given email already exists: " + customerWithNewEmail);
	            }
	        }

	        // Check if updated phone number already exists
	        if (!updatedPhone.equals(existingCustomer.getContactNo())) {
	            Customer customerWithNewPhone = customerRepo.findByContactNo(updatedPhone);
	            if (customerWithNewPhone != null) {
	                appExceptionHandler.raiseException("Customer with given phone number already exists: " + customerWithNewPhone);
	            }
	        }

	        // Update existing customer's details
	        existingCustomer.setCustomername(updatedCustomerName);
	        existingCustomer.setEmail(updatedEmail);
	        existingCustomer.setContactNo(updatedPhone);
	        existingCustomer.setDescription(updatedDesc);

	        // Save the updated customer
	        response = customerRepo.save(existingCustomer);
	    } catch (Exception e) {
	        e.printStackTrace();
	        appExceptionHandler.raiseException(e.getMessage());
	    }
	    return response;
	}

	@DeleteMapping("/deleteCustomer")
	public JSONObject deleteCustomer(@RequestParam(name = "customerName") String customer) {
	    customer = customer.replaceAll("\\s+", "").toLowerCase();
	    logger.info("Inside deleteCustomer, customer value received: {}", customer);
	    JSONObject response = new JSONObject();
	    try {
	        logger.debug("Inside deleteCustomer: {}", customer);
	        Customer customerDetails = customerRepo.findByCustomername(customer);
	        if (customerDetails == null) {
	          appExceptionHandler.raiseException("given customer not found");
	        }
            //TODO with given customer have order id validation
	        // Delete the customer
	        customerRepo.deleteByCustomerName(customer);

	        // Prepare response
	        response.put("status", "Success");
	        response.put("message", "Customer deleted successfully");
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("status", "Error");
	        response.put("message", e.getMessage()); // Include the error message in the response
	    }
	    return response;
	}


	
}
