package com.Network.Network.DevicemetamodelRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Network.Network.DevicemetamodelPojo.Country;
import com.Network.Network.DevicemetamodelPojo.State;
import java.util.List;
import java.util.Optional;


public interface StateRepo extends JpaRepository<State, Long> {
	State findTopByOrderByIdDesc();
	State findByStateName(String stateName);
	Optional<State> findById(Long id);
	List<State> findByCountryCountryName(String countryName);
	@Modifying
	@Query(value = "DELETE FROM state WHERE state_name = :stateName", nativeQuery = true)
	void deleteBystateByName(@Param("stateName") String stateName);
	

}
