package com.vroozi.datasnitch.model;

import java.io.Serializable;

public class BudgetAssociation implements Serializable {

  private String companyCode;
  private String companyCodeId;
  private String plantCode;
  private String plantCodeId;
  private String purchasingOrganizationCode;
  private String purchasingOrganizationId;
  private AccountingCategory accountingCategory;
  private String accountingElementId;
  private String accountingElementCode;
  private String glAccountCode;
  private String glAccountId;

  public String getCompanyCode() {
    return companyCode;
  }

  public void setCompanyCode(String companyCode) {
    this.companyCode = companyCode;
  }

  public String getCompanyCodeId() {
    return companyCodeId;
  }

  public void setCompanyCodeId(String companyCodeId) {
    this.companyCodeId = companyCodeId;
  }

  public String getPlantCode() {
    return plantCode;
  }

  public void setPlantCode(String plantCode) {
    this.plantCode = plantCode;
  }

  public String getPlantCodeId() {
    return plantCodeId;
  }

  public void setPlantCodeId(String plantCodeId) {
    this.plantCodeId = plantCodeId;
  }

  public String getPurchasingOrganizationCode() {
    return purchasingOrganizationCode;
  }

  public void setPurchasingOrganizationCode(String purchasingOrganizationCode) {
    this.purchasingOrganizationCode = purchasingOrganizationCode;
  }

  public String getPurchasingOrganizationId() {
    return purchasingOrganizationId;
  }

  public void setPurchasingOrganizationId(String purchasingOrganizationId) {
    this.purchasingOrganizationId = purchasingOrganizationId;
  }

  public AccountingCategory getAccountingCategory() {
    return accountingCategory;
  }

  public void setAccountingCategory(AccountingCategory accountingCategory) {
    this.accountingCategory = accountingCategory;
  }

  public String getAccountingElementId() {
    return accountingElementId;
  }

  public void setAccountingElementId(String accountingElementId) {
    this.accountingElementId = accountingElementId;
  }

  public String getAccountingElementCode() {
    return accountingElementCode;
  }

  public void setAccountingElementCode(String accountingElementCode) {
    this.accountingElementCode = accountingElementCode;
  }

  public String getGlAccountCode() {
    return glAccountCode;
  }

  public void setGlAccountCode(String glAccountCode) {
    this.glAccountCode = glAccountCode;
  }

  public String getGlAccountId() {
    return glAccountId;
  }

  public void setGlAccountId(String glAccountId) {
    this.glAccountId = glAccountId;
  }
}