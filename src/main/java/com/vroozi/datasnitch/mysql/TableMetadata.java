package com.vroozi.datasnitch.mysql;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class TableMetadata {

  @Autowired
  private DataSource dataSource;

  @Autowired
  JdbcTemplate jdbcTemplate;

  public void loadMetadata() {

    String tableNamePattern = "budget";
//    DatabaseMetaData databaseMetaData = jdbcTemplate.getMetaData();
  }

}
