package com.Network.Network.DevicemetamodelRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Network.Network.DevicemetamodelPojo.Building;
import java.util.List;
import java.util.Optional;

public interface BuildingRepo extends JpaRepository<Building, Long> {
	Building findByBuildingName(String buildingName);

	Optional<Building> findById(Long id);

	List<Building> findByCityCityName(String cityName);

	@Modifying
	@Query(value = "DELETE FROM building WHERE building_name = :buildingName", nativeQuery = true)
	void deleteBybuildingByName(@Param("buildingName") String buildingName);

	@Query(value = "SELECT d.building_name FROM device d WHERE d.building_name = :buildingName " +
			"UNION " +
			"SELECT r.building_name FROM rack r WHERE r.building_name = :buildingName", nativeQuery = true)
	List<String> findBuildingNames(@Param("buildingName") String buildingName);
}
