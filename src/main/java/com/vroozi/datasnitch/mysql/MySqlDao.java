package com.vroozi.datasnitch.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class MySqlDao {

  private static final String COUNT_QUERY =
      "SELECT count(*) "
          + "FROM information_schema.tables "
          + "WHERE table_name = ? and table_schema = 'test'";

  @Autowired
  JdbcTemplate jdbcTemplate;

  public boolean getData() {
    Integer result = jdbcTemplate.queryForObject(COUNT_QUERY, Integer.class, "tableName");
    return result > 0;
  }
}
