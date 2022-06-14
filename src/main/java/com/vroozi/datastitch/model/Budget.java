package com.vroozi.datastitch.model;

import static com.vroozi.datastitch.util.Constants.COLLECTION_BUDGET;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = COLLECTION_BUDGET)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Budget extends BaseModel implements Serializable {

  private String name;
  private String description;
  private BigDecimal totalAmount;
  private List<BudgetAssociation> budgetAssociations = new ArrayList<>();
  private DurationType durationType;
  @JsonFormat(shape = JsonFormat.Shape.NUMBER)
  private Date fromDate;
  @JsonFormat(shape = JsonFormat.Shape.NUMBER)
  private Date toDate;
  private PeriodicAllocationType periodicAllocationType = PeriodicAllocationType.NONE;
  private OverBudgetAction overBudgetAction;
  private String currency;
  private boolean rollForward;
  private int warningThresholdPercentage;
  private String externalId;
  private boolean active;
  private List<String> approverIds = new ArrayList<>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

  public List<BudgetAssociation> getBudgetAssociations() {
    return budgetAssociations;
  }

  public void setBudgetAssociations(List<BudgetAssociation> budgetAssociations) {
    this.budgetAssociations = budgetAssociations;
  }

  public DurationType getDurationType() {
    return durationType;
  }

  public void setDurationType(DurationType durationType) {
    this.durationType = durationType;
  }

  public Date getFromDate() {
    return fromDate;
  }

  public void setFromDate(Date fromDate) {
    this.fromDate = fromDate;
  }

  public Date getToDate() {
    return toDate;
  }

  public void setToDate(Date toDate) {
    this.toDate = toDate;
  }

  public OverBudgetAction getOverBudgetAction() {
    return overBudgetAction;
  }

  public void setOverBudgetAction(OverBudgetAction overBudgetAction) {
    this.overBudgetAction = overBudgetAction;
  }

  public PeriodicAllocationType getPeriodicAllocationType() {
    return periodicAllocationType;
  }

  public void setPeriodicAllocationType(PeriodicAllocationType periodicAllocationType) {
    this.periodicAllocationType = periodicAllocationType;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public boolean isRollForward() {
    return rollForward;
  }

  public void setRollForward(boolean rollForward) {
    this.rollForward = rollForward;
  }

  public int getWarningThresholdPercentage() {
    return warningThresholdPercentage;
  }

  public void setWarningThresholdPercentage(int warningThresholdPercentage) {
    this.warningThresholdPercentage = warningThresholdPercentage;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public String getExternalId() {
    return externalId;
  }

  public void setExternalId(String externalId) {
    this.externalId = externalId;
  }

  public List<String> getApproverIds() {
    return approverIds;
  }

  public void setApproverIds(List<String> approverIds) {
    this.approverIds = approverIds;
  }
}
