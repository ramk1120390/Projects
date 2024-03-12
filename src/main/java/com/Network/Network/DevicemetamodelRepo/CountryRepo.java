package com.Network.Network.DevicemetamodelRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Network.Network.DevicemetamodelPojo.Country;

public interface CountryRepo extends JpaRepository<Country, Integer> {
	Country findById(Long id);
	Country findByCountryName(String countryName);
	@Modifying
	@Query(value = "DELETE FROM country WHERE country_name = :countryName", nativeQuery = true)
	void deleteByCountryName(@Param("countryName") String countryName);


}
