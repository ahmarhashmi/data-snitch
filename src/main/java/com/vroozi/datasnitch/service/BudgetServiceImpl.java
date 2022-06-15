package com.vroozi.datasnitch.service;

import com.vroozi.datasnitch.model.Budget;
import com.vroozi.datasnitch.model.CollectionName;
import com.vroozi.datasnitch.model.MetaData;
import com.vroozi.datasnitch.model.SyncTracker;
import com.vroozi.datasnitch.repository.BudgetRepository;
import com.vroozi.datasnitch.repository.SyncTrackerRepository;
import com.vroozi.datasnitch.service.rest.RestClient;
import com.vroozi.datasnitch.util.Converter;
import com.vroozi.datasnitch.util.FileUtils;
import com.vroozi.datasnitch.util.JsonUtils;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetServiceImpl implements BudgetService {

  public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("ddMMyyyyhhmmss");

  @Autowired
  private BudgetRepository budgetRepository;

  @Autowired
  private SyncTrackerRepository syncTrackerRepository;

  @Autowired
  private RestClient restClient;

  @Autowired
  private RestServiceUrl restServiceUrl;

  @Autowired
  private BudgetJdbcService budgetJdbcService;

  @Override
  public void readAndPost(String unitId, CollectionName collectionName) {
    String bucketName = restServiceUrl.getBucketName();
    String folderName = restServiceUrl.getFolderName();
    syncTrackerRepository.findFirstByUnitIdAndCollectionNameOrderByLastReadDateDesc(
        unitId, collectionName).map(syncTracker -> {
      List<Budget> budgets = budgetRepository.findByLastModifiedDateGreaterThan(
          syncTracker.getLastReadDate());
      if (CollectionUtils.isNotEmpty(budgets)) {
        SyncTracker tracker = createSyncTracker(unitId, collectionName, budgets.size());
        //return uploadAll(unitId, budgets, collectionName, bucketName, folderName, tracker);
        return true;
      }
      return false;
    }).orElseGet(() -> uploadAllBudgets(unitId, collectionName, bucketName, folderName));
  }

  boolean uploadAllBudgets(
      String unitId, CollectionName collectionName, String bucketName, String folderName
  ) {
    List<Budget> budgets = budgetRepository.findByUnitId(unitId);
    if (CollectionUtils.isNotEmpty(budgets)) {
      SyncTracker tracker = createSyncTracker(unitId, collectionName, budgets.size());
      //return uploadAll(unitId, budgets, collectionName, bucketName, folderName, tracker);
      Map<String, MetaData> dataMap = Converter.convertToMetaDataMap(budgets.get(0));
      budgetJdbcService.insertBudget(dataMap, "budget");
      //String headerColumns = Converter.concatenateColumns(dataMap);
      return true;
    }
    return false;
  }

  boolean uploadAll(
      String unitId, List<Budget> budgets, CollectionName collectionName, String bucketName,
      String folderName, SyncTracker tracker
  ) {
    MutableInt uploadedRecord = new MutableInt(0);
    budgets.forEach(budget -> {
      String fileName = "Budget_" + budget.getName() + "_" + DATE_TIME_FORMAT.format(new Date()) + ".json";
      if (uploadRecord(unitId, budget, fileName, bucketName, folderName)){
        uploadedRecord.increment();
      }
    });
    if (uploadedRecord.intValue() > 0) {
      tracker.setPostedRecordCount(uploadedRecord.intValue());
      syncTrackerRepository.save(tracker);
      return true;
    }
    return false;
  }

  boolean uploadRecord(String unitId, Object record, String fileName, String bucketName, String folderName) {
    try {
      String jsonString = JsonUtils.toSafeJsonString(record);
      if (StringUtils.isNotBlank(jsonString)) {
        File jsonFile = generateJsonFile(jsonString, fileName);
        if (Objects.nonNull(jsonFile)) {
          return restClient.upload(jsonFile, bucketName, unitId, folderName) != null;
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return false;
  }

  SyncTracker createSyncTracker(
      String unitId, CollectionName collectionName, Integer recordReadCount
  ) {
    SyncTracker recordTracker = new SyncTracker();
    Date date = Calendar.getInstance().getTime();
    recordTracker.setLastReadDate(date);
    recordTracker.setCreatedDate(date);
    recordTracker.setReadRecordCount(recordReadCount);
    recordTracker.setCollectionName(collectionName);
    recordTracker.setUnitId(unitId);
    return recordTracker;
  }

  protected File generateJsonFile(String data, String fileName) throws IOException {
    return FileUtils.writeStringToFile(data, restServiceUrl.getReportPath(), fileName);
  }
}
