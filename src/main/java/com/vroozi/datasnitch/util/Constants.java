package com.vroozi.datasnitch.util;

public class Constants {

  private Constants() {
  }

  public static final String COLLECTION_BUDGET = "budgets";
  public static final String COLLECTION_SYNC_TRACKER = "sync_tracker";

  public static class BudgetFields {

    public static final String UNIT_ID = "unitId";
    public static final String CURRENCY = "currency";
    public static final String OVER_BUDGET_ACTION = "overBudgetAction";
    public static final String ACTIVE = "active";
    public static final String DELETED = "deleted";
    public static final String APPROVER_ID_LIST = "approverIds";
  }
}
