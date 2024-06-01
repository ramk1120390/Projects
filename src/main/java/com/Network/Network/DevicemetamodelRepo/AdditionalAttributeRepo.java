package com.Network.Network.DevicemetamodelRepo;

import com.Network.Network.DevicemetamodelPojo.AdditionalAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdditionalAttributeRepo extends JpaRepository<AdditionalAttribute,Long> {
    @Modifying
    @Query(value = "DELETE FROM additional_attribute a WHERE a.id IN :ids",nativeQuery = true)
    void deleteAdditionalAttributesByIds(@Param("ids") List<Long> ids);
}
