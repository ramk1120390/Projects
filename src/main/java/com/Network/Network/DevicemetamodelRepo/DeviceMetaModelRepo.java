package com.Network.Network.DevicemetamodelRepo;


import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.Network.Network.DevicemetamodelPojo.DeviceMetaModel;


public interface DeviceMetaModelRepo extends JpaRepository<DeviceMetaModel, Long> {


    @Query(value = "SELECT dmm FROM DeviceMetaModel dmm WHERE dmm.id = (SELECT MAX(dmm2.id) FROM DeviceMetaModel dmm2)")
    DeviceMetaModel findLatestInsertedEntity();

    DeviceMetaModel findByDeviceModel(String deviceModel);


    @Query(value = "CALL insert(:deviceModel, :partNumber, :vendor, :height, :depth, :width, :shelvesContained, :numOfRackPositionOccupied, :allowedCardList, :success)", nativeQuery = true)
    int insert(
            @Param("deviceModel") String deviceModel,
            @Param("partNumber") String partNumber,
            @Param("vendor") String vendor,
            @Param("height") float height,
            @Param("depth") float depth,
            @Param("width") float width,
            @Param("shelvesContained") int shelvesContained,
            @Param("numOfRackPositionOccupied") int numOfRackPositionOccupied,
            @Param("allowedCardList") String[] allowedCardList,
            @Param("success") int success // Use Integer instead of Boolean
    );

    @Query(value = "DELETE FROM device_meta_model WHERE device_model = :deviceModel", nativeQuery = true)
    void deleteByDeviceModel(@Param("deviceModel") String deviceModel);

    @Query(value = "SELECT * FROM device_meta_model WHERE vendor = :vendor", nativeQuery = true)
    List<DeviceMetaModel> findByVendor(@Param("vendor") String vendor);

    @Query(value = "SELECT shelves_contained FROM device_meta_model WHERE device_model = :deviceModel", nativeQuery = true)
    Integer findShelvesContainedByDeviceModel(@Param("deviceModel") String deviceModel);


}
