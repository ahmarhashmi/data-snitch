package com.vroozi.datasnitch.service;

import com.vroozi.datasnitch.repository.SyncTrackerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataSnitchScheduler {

  @Autowired
  private SyncTrackerRepository syncTrackerRepository;

  //@Scheduled(cron = "${sync-tracker-scheduler:0/30 * * * * *}")
  public void scheduledRun() {
//    syncTrackerRepository.findFirstByUnitIdAndCollectionNameOrderByLastReadDateDesc(
//        unitId, collectionName).map(syncTracker -> {
//      List<Budget> budgets = budgetRepository.findByLastModifiedDateGreaterThan(
//          syncTracker.getLastReadDate());
//      if (CollectionUtils.isNotEmpty(budgets)) {
//        SyncTracker tracker = createSyncTracker(unitId, collectionName, budgets.size());
//        //return uploadAll(unitId, budgets, collectionName, bucketName, folderName, tracker);
//        return true;
//      }
//      return false;
//    }).orElseGet(() -> uploadAllBudgets(unitId, collectionName, bucketName, folderName));
  }
}
