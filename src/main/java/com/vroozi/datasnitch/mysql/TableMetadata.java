package com.vroozi.datasnitch.mysql;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class TableMetadata {

  @Autowired
  JdbcTemplate jdbcTemplate;

  public void loadMetadata() {

    String tableNamePattern = "budget";
    DatabaseMetaData databaseMetaData = jdbcTemplate.queryForO.getMetaData();

    try {
      ResultSet columns =
          databaseMetaData.getColumns(null, null, tableNamePattern, null);
      ResultSet primaryKeys =
          databaseMetaData.getPrimaryKeys(null, null, tableNamePattern);

      ResultSet indexInfo =
          databaseMetaData.getIndexInfo(null, null, tableNamePattern, false, false);
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

}
