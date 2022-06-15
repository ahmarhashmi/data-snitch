package com.vroozi.datasnitch.model;

import java.io.Serializable;

public class MetaData implements Serializable {

  private Object value;
  private Class type;
  private Integer length;

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public Class getType() {
    return type;
  }

  public void setType(Class type) {
    this.type = type;
  }

  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
  }
}
