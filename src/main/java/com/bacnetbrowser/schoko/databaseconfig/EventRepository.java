package com.bacnetbrowser.schoko.databaseconfig;

import com.bacnetbrowser.schoko.bacnetutils.events.BACnetEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;



@Repository
public interface EventRepository extends JpaRepository<BACnetEvent,Integer> {

    BACnetEvent findBACnetEventByRemoteDeviceNameAndOidAndAndTimeStamp(String remoteDevice, String oid, Timestamp timestamp);

    BACnetEvent findTopByEventIDIs(String eventID);

}
