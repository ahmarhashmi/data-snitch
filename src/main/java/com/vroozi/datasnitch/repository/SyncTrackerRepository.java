package com.vroozi.datasnitch.repository;

import com.vroozi.datasnitch.model.CollectionName;
import com.vroozi.datasnitch.model.SyncTracker;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncTrackerRepository extends MongoRepository<SyncTracker, String> {

  SyncTracker findFirstByCollectionNameOrderByLastReadDateDesc(CollectionName collectionName);
}
