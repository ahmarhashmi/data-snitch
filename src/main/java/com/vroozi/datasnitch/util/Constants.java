package com.vroozi.datasnitch.util;

public class Constants {

  private Constants() {
  }

  public static final String COLLECTION_BUDGET = "budgets";
  public static final String COLLECTION_SYNC_TRACKER = "sync_tracker";
  public static final String COLLECTION_PERIODIC_BUDGET_ALLOCATION = "periodic_budget_allocation";
  public static final String COLLECTION_BUDGET_UPDATE_LOGS = "budget_update_logs";
  public static final String COLLECTION_DOCUMENT_BUDGET_HISTORY = "document_budget_history";
  public static final String BEARER = "Bearer";

  public static class BudgetFields {

    public static final String UNIT_ID = "unitId";
    public static final String CURRENCY = "currency";
    public static final String OVER_BUDGET_ACTION = "overBudgetAction";
    public static final String ACTIVE = "active";
    public static final String DELETED = "deleted";
    public static final String APPROVER_ID_LIST = "approverIds";
  }
}
