package com.vroozi.datasnitch.service;

import com.vroozi.datasnitch.dao.BudgetJdbcDao;
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
import java.util.AbstractMap;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetServiceImpl implements BudgetService {

  public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("ddMMyyyyhhmmss");
  private static final Logger LOGGER = LoggerFactory.getLogger(BudgetServiceImpl.class);


  private static final Map<String, String> FIELD_MAPPER_MAPPER = Stream
      .of(new AbstractMap.SimpleEntry<>("approverIds", "approverId"))
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));



  @Autowired
  private BudgetRepository budgetRepository;

  @Autowired
  private SyncTrackerRepository syncTrackerRepository;

  @Autowired
  private RestClient restClient;

  @Autowired
  private RestServiceUrl restServiceUrl;

  @Autowired
  private BudgetJdbcDao budgetJdbcDao;

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
      insertBudget(
          dataMap, "budget", dataMap.get("id").getValue().toString(), false
      );
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
      String fileName =
          "Budget_" + budget.getName() + "_" + DATE_TIME_FORMAT.format(new Date()) + ".json";
      if (uploadRecord(unitId, budget, fileName, bucketName, folderName)) {
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

  boolean uploadRecord(String unitId, Object record, String fileName, String bucketName,
      String folderName) {
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

  void insertBudget(
      Map<String, MetaData> dataMap, String tableName, String parentId, boolean isChild
  ) {
    Pair<String, List<Object>> pair = Converter.getColumnHeadersAndValues(dataMap, parentId,
        isChild);
    String qMarks = Converter.getQuestionMarks(pair.getRight());
    budgetJdbcDao.insertBudget(pair, qMarks, tableName);
    Map<String, List<Object>> childDataMap = Converter.getChildren(dataMap);
    childDataMap.forEach((key,value) -> {
      if (CollectionUtils.isNotEmpty(value)) {
        if (String.class.equals(value.get(0).getClass())) {
          List<String> childStringList = value.stream()
              .map(object -> Objects.toString(object, null))
              .filter(StringUtils::isNotBlank)
              .toList();
          insertChildHaveOnlyStringList(
              childStringList, String.format("%s%s%s", tableName, "_", key), key, parentId);
        } else {
          value.forEach(metaDataMap -> {
            Map<String, MetaData> childMap= new HashMap<>();
            try {
              childMap = (Map<String, MetaData>) metaDataMap;
            } catch (Exception e) {
              LOGGER.error("Exception occoured while parsing Object to metaData Map", e);
            }
            if (!childMap.isEmpty()) {
              insertBudget(childMap, String.format("%s%s%s", tableName, "_", key), parentId, true);
            }
          });
        }
      }
    });
  }

  private void insertChildHaveOnlyStringList(
      List<String> childStringList, String tableName, String key, String parentId
  ) {
    childStringList.forEach(value -> {
      StringJoiner keyJoiner = new StringJoiner(",");
      List<Object> values = new LinkedList<>();
      keyJoiner.add("parentId");
      values.add(parentId);
      keyJoiner.add(FIELD_MAPPER_MAPPER.get(key));
      values.add(value);
      Pair<String, List<Object>> pair = Pair.of(keyJoiner.toString(), values);
      String qMarks = Converter.getQuestionMarks(pair.getRight());
      budgetJdbcDao.insertBudget(pair, qMarks, tableName);
    });
  }

}
