package com.vroozi.datasnitch.service;

import com.vroozi.datasnitch.model.Budget;
import com.vroozi.datasnitch.model.CollectionName;
import com.vroozi.datasnitch.model.SyncTracker;
import com.vroozi.datasnitch.repository.BudgetRepository;
import com.vroozi.datasnitch.repository.SyncTrackerRepository;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetServiceImpl implements BudgetService {

  @Autowired
  private BudgetRepository budgetRepository;

  @Autowired
  private SyncTrackerRepository syncTrackerRepository;

  @Override
  public void readAndPost(CollectionName collectionName) {
    SyncTracker syncTracker = syncTrackerRepository.findFirstByCollectionNameOrderByLastReadDateDesc(
        collectionName);
    if (Objects.nonNull(syncTracker)) {
      ZonedDateTime zdt = syncTracker.getLastReadDate().atZone(ZoneId.systemDefault());
      Date convertedDate = Date.from(zdt.toInstant());
      List<Budget> budgets = budgetRepository.findByLastModifiedDateGreaterThan(convertedDate);
    }
  }
}
