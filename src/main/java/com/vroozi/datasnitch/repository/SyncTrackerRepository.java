package com.vroozi.datasnitch.repository;

import com.vroozi.datasnitch.model.CollectionName;
import com.vroozi.datasnitch.model.SyncTracker;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncTrackerRepository extends MongoRepository<SyncTracker, String> {

  Optional<SyncTracker> findFirstByUnitIdAndCollectionNameOrderByLastReadDateDesc(
      String unitId, CollectionName collectionName
  );

  Optional<SyncTracker> findFirstByTriggeredNextFalseOrderByLastReadDateDesc();
}
