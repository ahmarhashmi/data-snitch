package com.vroozi.datasnitch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@PropertySource("classpath:data-snitch.properties")
@ComponentScan(basePackages = {"com.vroozi.datasnitch"})
@AutoConfiguration
public class ApplicationConfig {

  @Value("${file.storage.service.path}")
  public String fileStorageServicePath;

  public String getFileStorageServicePath() {
    return fileStorageServicePath;
  }
}
