package com.vroozi.datasnitch.service;

import com.vroozi.datasnitch.model.MetaData;
import com.vroozi.datasnitch.util.Converter;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BudgetJdbcServiceImpl implements BudgetJdbcService {

  @Autowired
  private JdbcTemplate jdbcTemplate;


  @Override
  public void createTable(Map<String, MetaData> dataMap) {

  }

  @Override
  public void insertBudget(Map<String, MetaData> dataMap) {
    Pair<String, List<Object>> pair = Converter.getColumns(dataMap);
    String qMarks = Converter.getQuestionMarks(pair.getRight());
    jdbcTemplate.update(
        "INSERT INTO tech_hunter_hackathon.budget(" + pair.getLeft() + ") VALUES (" +qMarks+" )",
        pair.getRight().toArray());
  }

//  @Autowired
//  public void test(){
//    jdbcTemplate.queryForList("select * from tech_hunter_hackathon.budget");
//  }
}
