package com.Network.Network.DevicemetamodelApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.Network.Network.DevicemetamodelPojo.*;
import com.Network.Network.DevicemetamodelRepo.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Network.Network.Exception.AppExceptionHandler;

import jakarta.transaction.Transactional;

@RestController
public class LocationManagerApi {
    @Autowired
    private CountryRepo countryRepo;
    @Autowired
    private StateRepo stateRepo;
    @Autowired
    private AppExceptionHandler appExceptionHandler;
    @Autowired
    private CityRepo cityRepo;
    @Autowired
    private BuildingRepo buildingRepo;
    Logger logger = LoggerFactory.getLogger(LocationManagerApi.class);

    @PostMapping("/CreateCountry")
    public Country createCountry(@RequestBody Country country) {
        Country response = null;
        country.setCountryName(country.getCountryName().toLowerCase()); // corrected method name
        try {
            Country existingCountry = countryRepo.findByCountryName(country.getCountryName());
            if (existingCountry != null) {
                appExceptionHandler.raiseException("Given country is already available: " + existingCountry);
            }
            response = countryRepo.save(country);
        } catch (Exception e) {
            logger.error("Error occurred while creating country: {}", e.getMessage());
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @PostMapping("/CreateState")
    public State createState(@RequestParam("id") Long c_id, @RequestBody StateDto dto) {
        State response = null;
        try {
            Country existingCountry = countryRepo.findById(c_id);
            if (existingCountry == null) {
                appExceptionHandler.raiseException("Country with ID " + c_id + " not found");
            }

            if (dto.getCountryName() != null
                    && !existingCountry.getCountryName().equals(dto.getCountryName().toLowerCase())) {
                appExceptionHandler.raiseException("Country name must match with given ID");
            }

            State exState = stateRepo.findByStateName(dto.getStateName().toLowerCase());
            if (exState != null) {
                appExceptionHandler.raiseException("Given State is already available: " + exState);
            }

            State newState = new State();
            newState.setCountry(existingCountry);
            newState.setStateName(dto.getStateName().toLowerCase());
            String notes = dto.getNotes() != null ? dto.getNotes() : "Country_To_State";
            newState.setNotes(notes);
            response = stateRepo.save(newState);
            logger.info("Successfully inserted state: {}", response);
        } catch (Exception e) {
            logger.error("Error occurred while creating state: {}", e.getMessage());
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    //TODO with name
    @PostMapping("/CreateCity")
    public City createCity(@RequestParam("id") Long s_id, @RequestBody CityDto dto) {
        City response = null;
        try {
            Optional<State> existingStateOptional = stateRepo.findById(s_id);
            if (!existingStateOptional.isPresent()) {
                appExceptionHandler.raiseException("State with ID " + s_id + " not found");
            }

            State existingState = existingStateOptional.get();

            if (dto.getStateName() != null && !existingState.getStateName().equals(dto.getStateName().toLowerCase())) {
                appExceptionHandler.raiseException("State name must match with given ID");
            }
            City exCity = cityRepo.findByCityName(dto.getCityName().toLowerCase());
            if (exCity != null) {
                appExceptionHandler.raiseException("Given City is already available: " + exCity);
            }
            City newCity = new City();
            newCity.setState(existingState);
            newCity.setCityName(dto.getCityName().toLowerCase());
            String notes = dto.getNotes() != null ? dto.getNotes() : "State_To_City";
            newCity.setNotes(notes);

            response = cityRepo.save(newCity);
            logger.info("Successfully inserted city: {}", response);

        } catch (Exception e) {
            logger.error("Error occurred while creating city: {}", e.getMessage());
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @Autowired
    AdditionalAttributeRepo additionalAttributeRepo;

    @PostMapping("/CreateBuilding")
    public Building createCity(@RequestParam("id") Long c_id, @RequestBody BuildingDto dto) {
        Building response = null;
        try {
            Optional<City> existingcity = cityRepo.findById(c_id);
            if (!existingcity.isPresent()) {
                appExceptionHandler.raiseException("City with ID " + c_id + " not found");
            }
            City existingCity = existingcity.get();
            if (dto.getCityName() != null && !existingCity.getCityName().equals(dto.getCityName().toLowerCase())) {
                appExceptionHandler.raiseException("City name must match with given ID");
            }
            Building exBuilding = buildingRepo.findByBuildingName(dto.getBuildingName().toLowerCase());
            if (exBuilding != null) {
                appExceptionHandler.raiseException("Given Building is already available: " + exBuilding);
            }
            Building newBuilding = new Building();
            newBuilding.setBuildingName(dto.getBuildingName().toLowerCase());
            newBuilding.setCity(existingCity);
            String notes = dto.getNotes() != null ? dto.getNotes() : "City_To_Building";
            newBuilding.setNotes(notes);
            response = buildingRepo.save(newBuilding);
            List<AdditionalAttribute> additionalAttributes = new ArrayList<>();
            if (dto.getAdditionalAttributes() != null && !dto.getAdditionalAttributes().isEmpty()) {
                for (AdditionalAttribute additionalAttributeDTO : dto.getAdditionalAttributes()) {
                    AdditionalAttribute additionalAttribute = new AdditionalAttribute();
                    additionalAttribute.setKey(additionalAttributeDTO.getKey());
                    additionalAttribute.setValue(additionalAttributeDTO.getValue());
                    AdditionalAttribute savedAdditionalAttribute = additionalAttributeRepo.save(additionalAttribute);
                    additionalAttributes.add(savedAdditionalAttribute);
                }
            }
            newBuilding.setAdditionalAttributes(additionalAttributes);
            response = buildingRepo.save(newBuilding);
            logger.info("Successfully inserted Building: {}", response);
        } catch (Exception e) {
            logger.error("Error occurred while creating Building: {}", e.getMessage());
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @Transactional
    @PutMapping("/UpdateCountry")
    public Country updateCountry(@RequestParam("name") String name, @RequestBody Country country) {
        Country response = null;
        name = name.toLowerCase();

        try {
            // Find the existing country by name
            Country existingCountry = countryRepo.findByCountryName(name);
            if (existingCountry == null) {
                appExceptionHandler.raiseException("Given country is not available: " + name);
            }
            String CountryName = (country.getCountryName() == null) ? existingCountry.getCountryName()
                    : country.getCountryName().toLowerCase();
            // Check if the provided country name is being used by another country
            if (!name.equals(CountryName)) {
                Country conflictingCountry = countryRepo.findByCountryName(CountryName);
                if (conflictingCountry != null) {
                    appExceptionHandler.raiseException(
                            "Another country with the given name already exists: " + country.getCountryName());
                }
            }
            existingCountry.setCountryName(CountryName);
            existingCountry.setNotes(country.getNotes());
            response = countryRepo.save(existingCountry);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Unexpected error occurred while updating country: {}", e.getMessage());
            appExceptionHandler.raiseException("Unexpected error occurred while updating country: " + e.getMessage());
        }
        return response;
    }

    @PutMapping("/UpdateState")
    public State updateState(@RequestParam("name") String name, @RequestBody StateDto stateDto) {
        name = name.toLowerCase();
        try {
            // Check if the state exists
            State existingState = stateRepo.findByStateName(name);
            if (existingState == null) {
                appExceptionHandler.raiseException("Given State is not available: " + name);
            }

            // Get the updated state name and country name from the request body
            String updatedStateName = (stateDto.getStateName() == null) ? existingState.getStateName()
                    : stateDto.getStateName().toLowerCase();
            String updatedCountryName = (stateDto.getCountryName() == null)
                    ? existingState.getCountry().getCountryName()
                    : stateDto.getCountryName().toLowerCase();

            // If the state name is being updated, check for conflicts with existing state
            // names
            if (!name.equals(updatedStateName)) {
                State stateWithNewName = stateRepo.findByStateName(updatedStateName);
                if (stateWithNewName != null) {
                    appExceptionHandler.raiseException("State with given name already exists: " + updatedStateName);
                }
            }

            // If the country name is being updated, check if the country exists
            if (!updatedCountryName.equals(existingState.getCountry().getCountryName())) {
                Country existingCountry = countryRepo.findByCountryName(updatedCountryName);
                if (existingCountry == null) {
                    appExceptionHandler.raiseException("Given Country is not available: " + updatedCountryName);
                }
            }

            // Update the state entity with the new state name and country name
            existingState.setStateName(updatedStateName);
            existingState.getCountry().setCountryName(updatedCountryName);
            existingState.setNotes(stateDto.getNotes());

            // Save the updated state entity
            return stateRepo.save(existingState);

        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
            return null; // or throw an exception
        }
    }

    @PutMapping("/UpdateCity")
    public City updateCity(@RequestParam("name") String name, @RequestBody CityDto cityDto) {
        name = name.toLowerCase();
        try {
            // Check if the city exists
            City existingCity = cityRepo.findByCityName(name);
            if (existingCity == null) {
                appExceptionHandler.raiseException("Given City is not available: " + name);
            }

            // Get the updated city name and state name from the request body
            String updatedCityName = (cityDto.getCityName() == null) ? existingCity.getCityName()
                    : cityDto.getCityName().toLowerCase();
            String updatedStateName = (cityDto.getStateName() == null) ? existingCity.getState().getStateName()
                    : cityDto.getStateName().toLowerCase();

            // If the city name is being updated, check for conflicts with existing city
            // names
            if (!name.equals(updatedCityName)) {
                City cityWithNewName = cityRepo.findByCityName(updatedCityName);
                if (cityWithNewName != null) {
                    appExceptionHandler.raiseException("City with given name already exists: " + updatedCityName);
                }
            }

            // If the state name is being updated, check if the state exists
            if (!updatedStateName.equals(existingCity.getState().getStateName())) {
                State existingState = stateRepo.findByStateName(updatedStateName);
                if (existingState == null) {
                    appExceptionHandler.raiseException("Given State is not available: " + updatedStateName);
                }
            }

            // Update the city entity with the new city name and state name
            existingCity.setCityName(updatedCityName);
            existingCity.getState().setStateName(updatedStateName);
            existingCity.setNotes(cityDto.getNotes());
            // Save the updated city entity
            return cityRepo.save(existingCity);
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
            return null; // or throw an exception
        }
    }

    @Transactional
    @PutMapping("/UpdateBuilding")
    public Building updateBuilding(@RequestParam("name") String name, @RequestBody BuildingDto buildingDto) {
        name = name.toLowerCase();
        Building Response = new Building();
        try {
            // Check if the building exists
            Building existingBuilding = buildingRepo.findByBuildingName(name);
            if (existingBuilding == null) {
                appExceptionHandler.raiseException("Given Building is not available: " + name);
            }

            // Get the updated building name and city name from the request body
            String updatedBuildingName = (buildingDto.getBuildingName() == null) ? existingBuilding.getBuildingName()
                    : buildingDto.getBuildingName().toLowerCase();
            String updatedCityName = (buildingDto.getCityName() == null) ? existingBuilding.getCity().getCityName()
                    : buildingDto.getCityName().toLowerCase();
            String updatedNotes = (buildingDto.getNotes() == null) ? existingBuilding.getNotes()
                    : buildingDto.getNotes();
            String updatedClliCode = (buildingDto.getClliCode() == null) ? existingBuilding.getClliCode()
                    : buildingDto.getClliCode();
            String updatedPhoneNumber = (buildingDto.getPhoneNumber() == null) ? existingBuilding.getPhoneNumber()
                    : buildingDto.getPhoneNumber();
            String updatedContactPerson = (buildingDto.getContactPerson() == null) ? existingBuilding.getContactPerson()
                    : buildingDto.getContactPerson();
            String updatedAddress = (buildingDto.getAddress() == null) ? existingBuilding.getAddress()
                    : buildingDto.getAddress();
            String updatedLatitude = (buildingDto.getLatitude() == null) ? existingBuilding.getLatitude()
                    : buildingDto.getLatitude();
            String updatedLongitude = (buildingDto.getLongitude() == null) ? existingBuilding.getLongitude()
                    : buildingDto.getLongitude();
            String updatedDrivingInstructions = (buildingDto.getDrivingInstructions() == null)
                    ? existingBuilding.getDrivingInstructions()
                    : buildingDto.getDrivingInstructions();
            String updatedHref = (buildingDto.getHref() == null) ? existingBuilding.getHref() : buildingDto.getHref();

            // If the building name is being updated, check for conflicts with existing
            // building names
            if (!name.equals(updatedBuildingName)) {
                Building buildingWithNewName = buildingRepo.findByBuildingName(updatedBuildingName);
                if (buildingWithNewName != null) {
                    appExceptionHandler
                            .raiseException("Building with given name already exists: " + updatedBuildingName);
                }
            }

            // If the city name is being updated, check if the city exists
            if (!updatedCityName.equals(existingBuilding.getCity().getCityName())) {
                City existingCity = cityRepo.findByCityName(updatedCityName);
                if (existingCity == null) {
                    appExceptionHandler.raiseException("Given City is not available: " + updatedCityName);
                }
            }
            String finalName = name;
            List<Long> AAIds = buildingRepo.findAll().stream()
                    .filter(building -> building.getBuildingName().equals(finalName))
                    .flatMap(building -> building.getAdditionalAttributes().stream())
                    .map(AdditionalAttribute::getId)
                    .collect(Collectors.toList());
            existingBuilding.setBuildingName(updatedBuildingName);
            existingBuilding.getCity().setCityName(updatedCityName);
            existingBuilding.setNotes(updatedNotes);
            existingBuilding.setClliCode(updatedClliCode);
            existingBuilding.setPhoneNumber(updatedPhoneNumber);
            existingBuilding.setContactPerson(updatedContactPerson);
            existingBuilding.setAddress(updatedAddress);
            existingBuilding.setLatitude(updatedLatitude);
            existingBuilding.setLongitude(updatedLongitude);
            existingBuilding.setDrivingInstructions(updatedDrivingInstructions);
            existingBuilding.setHref(updatedHref);
            Response = buildingRepo.save(existingBuilding);
            List<AdditionalAttribute> additionalAttributes = new ArrayList<>();
            if (buildingDto.getAdditionalAttributes() != null && !buildingDto.getAdditionalAttributes().isEmpty()) {
                for (AdditionalAttribute additionalAttributeDTO : buildingDto.getAdditionalAttributes()) {
                    AdditionalAttribute additionalAttribute = new AdditionalAttribute();
                    additionalAttribute.setKey(additionalAttributeDTO.getKey());
                    additionalAttribute.setValue(additionalAttributeDTO.getValue());
                    AdditionalAttribute savedAdditionalAttribute = additionalAttributeRepo.save(additionalAttribute);
                    additionalAttributes.add(savedAdditionalAttribute);
                }
            }
            existingBuilding.setAdditionalAttributes(additionalAttributes);
            Response = buildingRepo.save(existingBuilding);
            additionalAttributeRepo.deleteAdditionalAttributesByIds(AAIds);
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
            return null; // or throw an exception
        }
        return Response;
    }

    @DeleteMapping("/deleteCountry")
    public JSONObject deleteCountry(@RequestParam(name = "countryName") String country) {
        country = country.toLowerCase().trim();
        logger.info("Inside deleteCountry, country value received: {}", country);
        JSONObject response = new JSONObject();
        try {
            logger.debug("Inside deleteCountry: {}", country);
            Country countryDetails = countryRepo.findByCountryName(country);
            if (countryDetails == null) {
                appExceptionHandler.raiseException("Given country not available");
            }
            List<State> exState = stateRepo.findByCountryCountryName(country);
            if (exState.size() > 0) {
                appExceptionHandler.raiseException("Country cannot be deleted as it is associated with State");
            }
            countryRepo.deleteByCountryName(country);
            response.put("status", "Success");
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @DeleteMapping("/deleteState")
    public JSONObject deleteState(@RequestParam(name = "stateName") String state) {
        state = state.toLowerCase().trim();
        logger.info("Inside deleteState, state value received: {}", state);
        JSONObject response = new JSONObject();
        try {
            logger.debug("Inside deleteState: {}", state);
            State stateDeatails = stateRepo.findByStateName(state);
            if (stateDeatails == null) {
                appExceptionHandler.raiseException("Given State not available");
            }
            List<City> cities = cityRepo.findByStateStateName(state);
            if (cities.size() > 0) {
                appExceptionHandler.raiseException("State cannot be deleted as it is associated with City");
            }
            stateRepo.deleteBystateByName(state);
            response.put("status", "Success");
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @DeleteMapping("/deleteCity")
    public JSONObject deleteCity(@RequestParam(name = "cityName") String city) {
        city = city.toLowerCase().trim();
        logger.info("Inside deleteCity, city value received: {}", city);
        JSONObject response = new JSONObject();
        try {
            logger.debug("Inside deleteCity: {}", city);
            City cityDeatails = cityRepo.findByCityName(city);
            if (cityDeatails == null) {
                appExceptionHandler.raiseException("Given City not available");
            }
            List<Building> exBuilding = buildingRepo.findByCityCityName(city);
            if (exBuilding.size() > 0) {
                appExceptionHandler.raiseException("City cannot be deleted as it is associated with Building");
            }
            cityRepo.deleteBycityByName(city);
            response.put("status", "Success");
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }

    @DeleteMapping("/deleteBuilding")
    public JSONObject deleteBuilding(@RequestParam(name = "buildingName") String building) {
        building = building.toLowerCase().trim();
        logger.info("Inside deleteBuilding, building value received: {}", building);
        JSONObject response = new JSONObject();
        try {
            logger.debug("Inside deleteBuilding: {}", building);
            Building buildingDetails = buildingRepo.findByBuildingName(building);
            if (buildingDetails == null) {
                appExceptionHandler.raiseException("Given Building not available");
            }
            List<String> exRackorDevice = buildingRepo.findBuildingNames(building);
            if (exRackorDevice.size() > 0) {
                appExceptionHandler.raiseException("Building cannot be deleted as it is associated with Device or Rack");
            }
            buildingRepo.deleteBybuildingByName(building);
            response.put("status", "Success");
            response.put("message", "Building deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            appExceptionHandler.raiseException(e.getMessage());
        }
        return response;
    }
}

