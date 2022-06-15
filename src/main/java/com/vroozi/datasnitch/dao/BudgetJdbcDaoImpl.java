package com.vroozi.datasnitch.dao;

import com.vroozi.datasnitch.model.MetaData;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BudgetJdbcDaoImpl implements BudgetJdbcDao {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public void insertBudget(
      Map<String, MetaData> dataMap, Pair<String, List<Object>> pair, String qMarks,
      String tableName
  ) {
    StringBuilder stringBuilder = new StringBuilder("INSERT INTO tech_hunter_hackathon.")
        .append(tableName)
        .append("(");
    jdbcTemplate.update(
        stringBuilder.append(pair.getLeft())
            .append(") VALUES (")
            .append(qMarks)
            .append(" )").toString(),
        pair.getRight().toArray());
  }
}
