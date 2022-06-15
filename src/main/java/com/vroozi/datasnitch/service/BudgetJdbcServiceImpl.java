package com.vroozi.datasnitch.service;

import com.vroozi.datasnitch.dao.BudgetJdbcDao;
import com.vroozi.datasnitch.model.MetaData;
import com.vroozi.datasnitch.util.Converter;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetJdbcServiceImpl implements BudgetJdbcService {

  @Autowired
  private BudgetJdbcDao budgetJdbcDao;


  @Override
  public void createTable(Map<String, MetaData> dataMap) {

  }

  @Override
  public void insertBudget(
      Map<String, MetaData> dataMap, String tableName, String parentId, boolean isChild
  ) {
    Pair<String, List<Object>> pair = Converter.getColumns(dataMap, parentId, isChild);
    String qMarks = Converter.getQuestionMarks(pair.getRight());
    budgetJdbcDao.insertBudget(dataMap, pair, qMarks, tableName);
    Map<String, List<Map<String, MetaData>>> childDataMap = Converter.getChildren(dataMap);
    childDataMap.forEach((key, childDataMapList) ->
        childDataMapList.forEach(
            child -> insertBudget(child, String.format("%s%s%s", tableName, "_", key), parentId,
                true)
        ));
  }
}
