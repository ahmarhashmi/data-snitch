package com.vroozi.datasnitch.service;

import com.vroozi.datasnitch.model.Budget;
import com.vroozi.datasnitch.model.MetaData;
import com.vroozi.datasnitch.model.SyncTracker;
import com.vroozi.datasnitch.repository.BudgetRepository;
import com.vroozi.datasnitch.repository.SyncTrackerRepository;
import com.vroozi.datasnitch.util.Converter;
import com.vroozi.datasnitch.util.CreatorUtil;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DataSnitchScheduler {

  private static final Logger LOG = LoggerFactory.getLogger(DataSnitchScheduler.class);

  @Autowired
  private BudgetRepository budgetRepository;

  @Autowired
  private SyncTrackerRepository syncTrackerRepository;

  @Autowired
  private BudgetService budgetService;

  @Scheduled(cron = "${sync-tracker-scheduler:0/30 * * * * *}")
  public void scheduledRun() {
    syncTrackerRepository.findFirstByTriggeredNextFalseOrderByLastReadDateDesc()
        .ifPresent(syncTracker -> {
          List<Budget> budgetList = budgetRepository.findByLastModifiedDateGreaterThan(
              syncTracker.getLastReadDate());
          if (CollectionUtils.isNotEmpty(budgetList)) {
            MutableInt postedCount = new MutableInt(0);
            SyncTracker tracker = CreatorUtil.createSyncTracker(syncTracker.getUnitId(),
                syncTracker.getCollectionName(), budgetList.size());
            budgetList.forEach(budget -> {
              Map<String, MetaData> dataMap = Converter.convertToMetaDataMap(budget);
              budgetService.insertBudget(dataMap, syncTracker.getCollectionName().value,
                  dataMap.get("id").getValue().toString(), false);
              postedCount.increment();
            });
            if (postedCount.intValue() > 0) {
              tracker.setPostedRecordCount(postedCount.getValue());
              syncTracker.setTriggeredNext(true);
              syncTrackerRepository.save(syncTracker);
              syncTrackerRepository.save(tracker);
            }
          }
        });
  }
}
