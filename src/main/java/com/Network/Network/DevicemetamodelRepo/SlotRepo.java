package com.Network.Network.DevicemetamodelRepo;

import com.Network.Network.DevicemetamodelPojo.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlotRepo extends JpaRepository<Slot,Long> {
    Slot findBySlotname(String slotname);
}
