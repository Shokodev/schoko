package com.bacnetbrowser.schoko.databaseConfig;

import com.bacnetbrowser.schoko.model.models.BACnetEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<BACnetEvent,Integer> {

    BACnetEvent findTopBACnetEventByRemoteDeviceNameAndOid(String remoteDevice, String oid);

    List<BACnetEvent> findAllByVisableInFrontend(boolean visableInFrontend);

}
