package com.Network.Network.DevicemetamodelRepo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Network.Network.DevicemetamodelPojo.City;
import com.Network.Network.DevicemetamodelPojo.State;

public interface CityRepo extends JpaRepository<City, Long> {
	City findByCityName(String cityName);
	Optional<City>  findById(Long id);
	List<City> findByStateStateName(String stateName);
	@Modifying
	@Query(value = "DELETE FROM city WHERE city_name = :cityName", nativeQuery = true)
	void deleteBycityByName(@Param("cityName") String cityName);
}
