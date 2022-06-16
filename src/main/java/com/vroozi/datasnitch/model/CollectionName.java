package com.vroozi.datasnitch.model;

public enum CollectionName {
  BUDGET("budget");

  CollectionName(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }
}
