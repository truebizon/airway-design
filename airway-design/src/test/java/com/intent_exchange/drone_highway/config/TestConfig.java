package com.intent_exchange.drone_highway.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

@Configuration
public class TestConfig {
  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  public ModelMapperUtil modelMapperUtil(ModelMapper modelMapper) {
    return new ModelMapperUtil(modelMapper);
  }
}
