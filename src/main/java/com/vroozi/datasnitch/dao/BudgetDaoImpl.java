package com.vroozi.datasnitch.dao;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BudgetDaoImpl implements BudgetDao {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public void insertBudget(
      Pair<String, List<Object>> pair, String qMarks, String tableName, String parentId,
      boolean isChild
  ) {
    if (!isChild && StringUtils.isNotBlank(parentId)) {
      jdbcTemplate.execute(
          new StringBuilder()
              .append("delete from tech_hunter_hackathon.")
              .append(tableName)
              .append(" where id='")
              .append(parentId)
              .append("'")
              .toString()
      );
    }
    StringBuilder stringBuilder = new StringBuilder("INSERT INTO tech_hunter_hackathon.")
        .append(tableName)
        .append("(");
    jdbcTemplate.update(
        stringBuilder.append(pair.getLeft())
            .append(") VALUES (")
            .append(qMarks)
            .append(" )").toString(),
        pair.getRight().toArray()
    );
  }

  @Override
  public void deleteChildByParentId(String tableName, String parentId) {
    jdbcTemplate.execute(
        new StringBuilder()
            .append("delete from tech_hunter_hackathon.")
            .append(tableName)
            .append(" where parentId='")
            .append(parentId)
            .append("'")
            .toString()
    );
  }
}
