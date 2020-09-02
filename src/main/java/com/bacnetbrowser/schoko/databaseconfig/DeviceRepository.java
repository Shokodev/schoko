package com.bacnetbrowser.schoko.databaseconfig;


import com.bacnetbrowser.schoko.bacnetutils.devices.PermanentDevices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface DeviceRepository extends JpaRepository<PermanentDevices,Integer> {

    PermanentDevices findByDeviceId (int deviceId);

}
