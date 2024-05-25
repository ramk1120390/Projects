package com.Network.Network.DevicemetamodelRepo;

import com.Network.Network.DevicemetamodelPojo.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShelfRepo extends JpaRepository<Shelf,Long> {
    Shelf findShelfByName(String name);
    @Query("SELECT s FROM Shelf s LEFT JOIN s.slots s2 WHERE s2.shelfname IS NULL AND s.devicename = :devicename")
    List<Shelf> findAvailableShelvesInDevice(@Param("devicename") String devicename);
}
