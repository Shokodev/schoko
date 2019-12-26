package com.bacnetbrowser.schoko.databaseConfig;

import com.bacnetbrowser.schoko.model.models.BACnetEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<BACnetEvent,Integer> {



}
