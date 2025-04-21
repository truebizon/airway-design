package com.intent_exchange.drone_highway.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import com.intent_exchange.drone_highway.dto.request.DronePortsMappingPostRequestDto;
import com.intent_exchange.drone_highway.entity.DronePortsMappingPostEntity;
import com.intent_exchange.drone_highway.entity.DronePortsMappingPostEntityAirwaySectionsInner;
import com.intent_exchange.drone_highway.exception.DataNotFoundException;
import com.intent_exchange.drone_highway.logic.DronePortsMappingLogic;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {DronePortsMappingServiceTest.Config.class})
public class DronePortsMappingServiceTest {

  @InjectMocks
  private DronePortsMappingService dronePortsMappingService;

  @Mock
  private DronePortsMappingLogic dronePortsMappingLogic;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
  }

  @Configuration
  static class Config {
    @Bean
    public ModelMapper modelMapper() {
      return new ModelMapper();
    }

    @Bean
    public ModelMapperUtil modelMapperUtil(ModelMapper modelMapper) {
      return new ModelMapperUtil(modelMapper);
    }
  }

  @Test
  @DisplayName("ドローンポートマッピングロジックのメソッドが正常に動作すること")
  public void testDronePortsMappingPost_success() {
    DronePortsMappingPostEntity requestEntity = createValidRequestEntity();
    dronePortsMappingService.dronePortsMappingPost(requestEntity);
    DronePortsMappingPostRequestDto requestDto =
        ModelMapperUtil.map(requestEntity, DronePortsMappingPostRequestDto.class);
    verify(dronePortsMappingLogic).dronePortsMappingPost(requestDto);
  }

  @Test
  @DisplayName("データが存在しないことを表す例外が返却された場合、サービスが例外を受け取れること")
  public void testDronePortsMappingPost_notFoundException() {
    DronePortsMappingPostEntity requestEntity = createValidRequestEntity();
    DronePortsMappingPostRequestDto requestDto =
        ModelMapperUtil.map(requestEntity, DronePortsMappingPostRequestDto.class);
    doThrow(new DataNotFoundException()).when(dronePortsMappingLogic)
        .dronePortsMappingPost(requestDto);

    assertThrows(DataNotFoundException.class, () -> {
      dronePortsMappingService.dronePortsMappingPost(requestEntity);
    });
  }

  private DronePortsMappingPostEntity createValidRequestEntity() {
    DronePortsMappingPostEntity request = new DronePortsMappingPostEntity();
    request.setAirwayId("airway-TestId");
    DronePortsMappingPostEntityAirwaySectionsInner section =
        new DronePortsMappingPostEntityAirwaySectionsInner();
    section.setAirwaySectionId("section-TestId");
    section.setDroneportIds(Arrays.asList("dronePortTestId1", "dronePortTestId2"));
    request.setAirwaySections(Arrays.asList(section));
    return request;
  }
}
