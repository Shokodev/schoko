package com.bacnetbrowser.schoko.databaseConfig;

import com.bacnetbrowser.schoko.model.models.BACnetEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;



@Repository
public interface EventRepository extends JpaRepository<BACnetEvent,Integer> {

    BACnetEvent findBACnetEventByRemoteDeviceNameAndOidAndAndTimeStamp(String remoteDevice, String oid, Timestamp timestamp);

    BACnetEvent findTopBACnetEventSQLByObjectNameOrderByTimeStampDesc(String objectName);

}
