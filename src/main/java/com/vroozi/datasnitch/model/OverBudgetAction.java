package com.vroozi.datasnitch.model;

public enum OverBudgetAction {
  /**
   * Restrict submitting the PR/PO
   */
  RESTRICT,
  /**
   * Submit PR/PO for Approval
   */
  ALLOW_WITH_APPROVAL,
  /**
   * Allow PR/PO for Submission
   */
  ALLOW_WITHOUT_APPROVAL;
}
