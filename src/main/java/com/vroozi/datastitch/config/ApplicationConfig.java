package com.vroozi.datastitch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@PropertySource("classpath:data-stitch.properties")
public class ApplicationConfig {

  @Value("${file.storage.service.path}")
  public String fileStorageServicePath;

  public String getFileStorageServicePath() {
    return fileStorageServicePath;
  }
}
