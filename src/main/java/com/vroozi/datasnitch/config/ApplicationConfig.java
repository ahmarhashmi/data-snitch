package com.vroozi.datasnitch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@ComponentScan(basePackages = "com.vroozi.datasnitch")
@Configuration
@EnableScheduling
public class ApplicationConfig {

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

//  @Bean
//  public static PropertySourcesPlaceholderConfigurer properties() {
//    PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
//    ClassPathResource classPathResource = new ClassPathResource("data-snitch.properties");
//    Resource[] resources = new ClassPathResource[]{classPathResource};
//    ppc.setLocations(resources);
//    ppc.setIgnoreResourceNotFound(false);
//    ppc.setIgnoreUnresolvablePlaceholders(false);
//    return ppc;
//  }
}
