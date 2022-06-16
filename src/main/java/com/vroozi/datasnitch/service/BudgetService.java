package com.vroozi.datasnitch.service;

import com.vroozi.datasnitch.model.CollectionName;
import com.vroozi.datasnitch.model.MetaData;
import java.util.Map;

public interface BudgetService {

  Integer readAndPost(String unitId, CollectionName collectionName);

  void insertBudget(
      Map<String, MetaData> dataMap, String tableName, String parentId, boolean isChild
  );

  Integer readAndPostAllRecords(String unitId, CollectionName collectionName);
}
