package com.Network.Network.DevicemetamodelRepo;

import com.Network.Network.DevicemetamodelPojo.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShelfRepo extends JpaRepository<Shelf,Long> {
    Shelf findShelfByName(String name);
}
