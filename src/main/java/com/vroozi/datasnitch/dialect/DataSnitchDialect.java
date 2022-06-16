package com.vroozi.datasnitch.dialect;

import org.hibernate.dialect.PostgreSQL9Dialect;

public class DataSnitchDialect extends PostgreSQL9Dialect {

  @Override
  public String getQuerySequencesString() {
    return null;
  }
}
