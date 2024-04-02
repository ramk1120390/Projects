package com.Network.Network.DevicemetamodelRepo;

import com.Network.Network.DevicemetamodelPojo.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardRepo extends JpaRepository<Card,String> {
    @Query(value = "SELECT * FROM card WHERE cardname = ?1 AND devicename = ?2", nativeQuery = true)
    Card findCardsByCardNameAndDeviceName(String cardName, String deviceName);
    @Query(value = "SELECT * FROM card c WHERE c.slotname = :slotName", nativeQuery = true)
    Card findCardsBySlotName(@Param("slotName") String slotName);

    @Query(value = "CALL insert_card(:i_cardname, :i_devicename, :i_shelfPosition, :i_slotPosition, "
            + ":i_vendor, :i_cardModel, :i_cardPartNumber, :i_operationalState, :i_administrativeState, "
            + ":i_usageState, :i_href, :i_orderid, :success)", nativeQuery = true)
    int insertCard(
            @Param("i_cardname") String cardName,
            @Param("i_devicename") String deviceName,
            @Param("i_shelfPosition") Integer shelfPosition,
            @Param("i_slotPosition") Integer slotPosition,
            @Param("i_vendor") String vendor,
            @Param("i_cardModel") String cardModel,
            @Param("i_cardPartNumber") String cardPartNumber,
            @Param("i_operationalState") String operationalState,
            @Param("i_administrativeState") String administrativeState,
            @Param("i_usageState") String usageState,
            @Param("i_href") String href,
            @Param("i_orderid") Long orderId,
            @Param("success") Integer success
    );

    @Query(value = "CALL update_card(:i_cardid, :i_cardname, :i_devicename, :i_shelfPosition, :i_slotPosition, "
            + ":i_vendor, :i_cardModel, :i_cardPartNumber, :i_operationalState, :i_administrativeState, "
            + ":i_usageState, :i_href, :i_orderid, :i_slotid, :success)", nativeQuery = true)
    int updateCard(
            @Param("i_cardid") Long cardId,
            @Param("i_cardname") String cardName,
            @Param("i_devicename") String deviceName,
            @Param("i_shelfPosition") Integer shelfPosition,
            @Param("i_slotPosition") Integer slotPosition,
            @Param("i_vendor") String vendor,
            @Param("i_cardModel") String cardModel,
            @Param("i_cardPartNumber") String cardPartNumber,
            @Param("i_operationalState") String operationalState,
            @Param("i_administrativeState") String administrativeState,
            @Param("i_usageState") String usageState,
            @Param("i_href") String href,
            @Param("i_orderid") Long orderId,
            @Param("i_slotid") Long slotId,
            @Param("success") Integer success
    );
    @Query(value = "SELECT * FROM card WHERE cardname = ?1", nativeQuery = true)
    List<Card> findCards(String cardName);
    Card findByCardid(Long cardid);

}
