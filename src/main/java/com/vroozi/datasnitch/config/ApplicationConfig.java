package com.vroozi.datasnitch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;

@ComponentScan(basePackages = "com.vroozi.datasnitch")
@Configuration
public class ApplicationConfig {

  @Value("${file.storage.service.path}")
  public String fileStorageServicePath;
  @Value("${bucket.name}")
  public String bucketName;
  @Value("${folder.name}")
  public String folderName;
  @Value("${purchase.reports.path}")
  public String reportPath;
  @Value("${file.storage.service.auth.token}")
  public String fileStorageServiceAuthToken;
  @Value("${file.storage.protocol}")
  public String fileStorageProtocol;
  @Value("${file.storage.host}")
  public String fileStorageHost;

  @Value("${spring.datasource.url}")
  public String dataSourceUrl;
  @Value("${spring.datasource.username}")
  public String dataSourceUsername;
  @Value("${spring.datasource.password}")
  public String getDataSourcePassword;
  @Value("${spring.datasource.driver-class-name}")
  public String driverClassName;


  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public static PropertySourcesPlaceholderConfigurer properties() {
    PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
    ClassPathResource classPathResource = new ClassPathResource("data-snitch.properties");
    Resource[] resources = new ClassPathResource[]{classPathResource};
    ppc.setLocations(resources);
    ppc.setIgnoreResourceNotFound(false);
    ppc.setIgnoreUnresolvablePlaceholders(false);
    return ppc;
  }
}
