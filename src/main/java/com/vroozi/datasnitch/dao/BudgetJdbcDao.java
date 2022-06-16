package com.vroozi.datasnitch.dao;

import com.vroozi.datasnitch.model.MetaData;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;

public interface BudgetJdbcDao {

  void insertBudget(Map<String, MetaData> dataMap, Pair<String, List<Object>> pair, String qMarks,
      String tableName);
}
