package com.vroozi.datasnitch.model;

import static com.vroozi.datasnitch.util.Constants.COLLECTION_SYNC_TRACKER;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = COLLECTION_SYNC_TRACKER)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SyncTracker implements Serializable {

  @Id
  private String id;
  private String unitId;
  private CollectionName collectionName;
  private Date lastReadDate;
  private Date createdDate;
  private Integer readRecordCount;
  private Integer postedRecordCount;

  private boolean triggered;

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getUnitId() {
    return unitId;
  }

  public void setUnitId(final String unitId) {
    this.unitId = unitId;
  }

  public boolean isTriggered() {
    return triggered;
  }

  public void setTriggered(boolean triggered) {
    this.triggered = triggered;
  }

  public CollectionName getCollectionName() {
    return collectionName;
  }

  public void setCollectionName(CollectionName collectionName) {
    this.collectionName = collectionName;
  }

  public Date getLastReadDate() {
    return lastReadDate;
  }

  public void setLastReadDate(Date lastReadDate) {
    this.lastReadDate = lastReadDate;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Integer getReadRecordCount() {
    return readRecordCount;
  }

  public void setReadRecordCount(Integer readRecordCount) {
    this.readRecordCount = readRecordCount;
  }

  public Integer getPostedRecordCount() {
    return postedRecordCount;
  }

  public void setPostedRecordCount(Integer postedRecordCount) {
    this.postedRecordCount = postedRecordCount;
  }
}
