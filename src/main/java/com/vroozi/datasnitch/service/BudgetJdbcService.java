package com.vroozi.datasnitch.service;

import com.vroozi.datasnitch.model.MetaData;
import java.util.Map;

public interface BudgetJdbcService {

  void createTable(Map<String, MetaData> dataMap);

  void insertBudget(
      Map<String, MetaData> dataMap, String tableName, String parentId, boolean isChild
  );

}
