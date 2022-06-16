package com.vroozi.datasnitch.service;

import com.vroozi.datasnitch.dao.BudgetDao;
import com.vroozi.datasnitch.model.Budget;
import com.vroozi.datasnitch.model.CollectionName;
import com.vroozi.datasnitch.model.MetaData;
import com.vroozi.datasnitch.model.SyncTracker;
import com.vroozi.datasnitch.repository.BudgetRepository;
import com.vroozi.datasnitch.repository.SyncTrackerRepository;
import com.vroozi.datasnitch.util.Converter;
import com.vroozi.datasnitch.util.CreatorUtil;
import java.util.AbstractMap;
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

  private static final Logger LOGGER = LoggerFactory.getLogger(BudgetServiceImpl.class);

  private static final Map<String, String> FIELD_MAPPER_MAPPER = Stream
      .of(new AbstractMap.SimpleEntry<>("approverIds", "approverId"))
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


  @Autowired
  private BudgetRepository budgetRepository;

  @Autowired
  private SyncTrackerRepository syncTrackerRepository;

  @Autowired
  private BudgetDao budgetDao;

  @Override
  public Integer readAndPost(String unitId, CollectionName collectionName) {
    MutableInt postedRecordCount = new MutableInt(0);
    syncTrackerRepository.findFirstByUnitIdAndCollectionNameOrderByLastReadDateDesc(
        unitId, collectionName).ifPresent(syncTracker -> {
      List<Budget> budgets = budgetRepository.findByLastModifiedDateGreaterThan(
          syncTracker.getLastReadDate());
      if (CollectionUtils.isNotEmpty(budgets)) {
        postedRecordCount.add(postRecords(unitId, collectionName, budgets));
      }
    });
    return postedRecordCount.intValue();
  }

  @Override
  public Integer readAndPostAllRecords(String unitId, CollectionName collectionName) {
    return postAllRecords(unitId, collectionName);
  }

  Integer postAllRecords(String unitId, CollectionName collectionName) {
    List<Budget> budgets = budgetRepository.findByUnitId(unitId);
    if (CollectionUtils.isNotEmpty(budgets)) {
      return postRecords(unitId, collectionName, budgets);
    }
    return 0;
  }

  Integer postRecords(String unitId, CollectionName collectionName, List<Budget> budgets) {
    SyncTracker tracker = CreatorUtil.createSyncTracker(unitId, collectionName, budgets.size());
    MutableInt postedRecordCount = new MutableInt(0);
    budgets.forEach(budget -> {
      Map<String, MetaData> dataMap = Converter.convertToMetaDataMap(budget);
      insertBudget(dataMap, "budget", dataMap.get("id").getValue().toString(), false);
      postedRecordCount.increment();
    });
    if (postedRecordCount.intValue() > 0) {
      tracker.setReadRecordCount(postedRecordCount.intValue());
      syncTrackerRepository.save(tracker);
      return tracker.getPostedRecordCount();
    }
    return 0;
  }

  @Override
  public void insertBudget(
      Map<String, MetaData> dataMap, String tableName, String parentId, boolean isChild
  ) {
    Pair<String, List<Object>> pair = Converter.getColumnHeadersAndValues(dataMap, parentId,
        isChild);
    String qMarks = Converter.getQuestionMarks(pair.getRight());
    budgetDao.insertBudget(pair, qMarks, tableName, parentId, isChild);
    Map<String, List<Object>> childDataMap = Converter.getChildren(dataMap);
    childDataMap.forEach((key, value) -> {
      String childTableName = String.format("%s%s%s", tableName, "_", key);
      if (CollectionUtils.isNotEmpty(value)) {
        if (String.class.equals(value.get(0).getClass())) {
          List<String> childStringList = value.stream()
              .map(object -> Objects.toString(object, null))
              .filter(StringUtils::isNotBlank)
              .toList();
          insertChildHaveOnlyStringList(
              childStringList, String.format("%s%s%s", tableName, "_", key), key, parentId);
        } else {
          budgetDao.deleteChildByParentId(childTableName, parentId);
          value.forEach(metaDataMap -> {
            Map<String, MetaData> childMap = new HashMap<>();
            try {
              childMap = (Map<String, MetaData>) metaDataMap;
            } catch (Exception e) {
              LOGGER.error("Exception occoured while parsing Object to metaData Map", e);
            }
            if (!childMap.isEmpty()) {
              insertBudget(childMap, childTableName, parentId, true);
            }
          });
        }
      }
    });
  }

  private void insertChildHaveOnlyStringList(
      List<String> childStringList, String tableName, String key, String parentId
  ) {
    budgetDao.deleteChildByParentId(tableName, parentId);
    childStringList.forEach(value -> {
      StringJoiner keyJoiner = new StringJoiner(",");
      List<Object> values = new LinkedList<>();
      keyJoiner.add("parentId");
      values.add(parentId);
      keyJoiner.add(FIELD_MAPPER_MAPPER.get(key));
      values.add(value);
      Pair<String, List<Object>> pair = Pair.of(keyJoiner.toString(), values);
      String qMarks = Converter.getQuestionMarks(pair.getRight());
      budgetDao.insertBudget(pair, qMarks, tableName, parentId, true);
    });
  }

}
