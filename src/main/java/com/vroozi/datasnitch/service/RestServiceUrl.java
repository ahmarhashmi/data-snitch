package com.vroozi.datasnitch.service;

import com.vroozi.datasnitch.config.ApplicationConfig;
import org.springframework.stereotype.Component;

@Component
public class RestServiceUrl {

  private ApplicationConfig applicationConfig;

  public RestServiceUrl(ApplicationConfig applicationConfig) {
    this.applicationConfig = applicationConfig;
  }

  public String getFileStorageServicePath() {
    return applicationConfig.fileStorageServicePath;
  }

  public String getBucketName() {
    return applicationConfig.bucketName;
  }

  public String getFolderName() {
    return applicationConfig.folderName;
  }

  public String getReportPath() {
    return applicationConfig.reportPath;
  }

  public String getFileStorageServiceAuthToken() {
    return applicationConfig.fileStorageServiceAuthToken;
  }

  public String getFileStorageProtocol() {
    return applicationConfig.fileStorageProtocol;
  }

  public String getFileStorageHost() {
    return applicationConfig.fileStorageHost;
  }

  public String getFileStorageUrl() {
    return getFileStorageProtocol() + "://" + getFileStorageHost();
  }

  public String getSecureUploadFileURI() {
    return getFileStorageUrl() + getFileStorageServicePath();
  }
}
