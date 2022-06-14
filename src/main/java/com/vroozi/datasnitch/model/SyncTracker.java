package com.vroozi.datasnitch.model;

import static com.vroozi.datasnitch.util.Constants.COLLECTION_SYNC_TRACKER;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collection = COLLECTION_SYNC_TRACKER)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SyncTracker implements Serializable {

  @Id
  private String id;
  private String unitId;
  private CollectionName collectionName;

  @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
  private LocalDateTime createdDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
  private LocalDateTime lastReadDate;

  private boolean uploaded;

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

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(final LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  public LocalDateTime getLastReadDate() {
    return lastReadDate;
  }

  public void setLastReadDate(LocalDateTime lastReadDate) {
    this.lastReadDate = lastReadDate;
  }

  public boolean isUploaded() {
    return uploaded;
  }

  public void setUploaded(boolean uploaded) {
    this.uploaded = uploaded;
  }

  public CollectionName getCollectionName() {
    return collectionName;
  }

  public void setCollectionName(CollectionName collectionName) {
    this.collectionName = collectionName;
  }
}
