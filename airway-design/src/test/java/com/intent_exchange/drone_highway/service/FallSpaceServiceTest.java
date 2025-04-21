package com.intent_exchange.drone_highway.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import com.intent_exchange.drone_highway.dto.request.FallSpaceCrossSectionPostRequestDto;
import com.intent_exchange.drone_highway.dto.request.FallSpacePostRequestDto;
import com.intent_exchange.drone_highway.dto.response.FallSpaceCrossSectionPostResponseDto;
import com.intent_exchange.drone_highway.dto.response.FallSpacePostResponseDto;
import com.intent_exchange.drone_highway.entity.FallSpaceCrossSectionPostRequestEntity;
import com.intent_exchange.drone_highway.entity.FallSpaceCrossSectionPostRequestEntityGeometry;
import com.intent_exchange.drone_highway.entity.FallSpaceCrossSectionPostResponseEntity;
import com.intent_exchange.drone_highway.entity.FallSpacePostRequestDroneEntity;
import com.intent_exchange.drone_highway.entity.FallSpacePostRequestEntity;
import com.intent_exchange.drone_highway.entity.FallSpacePostResponseEntity;
import com.intent_exchange.drone_highway.logic.FallSpaceLogic;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {FallSpaceServiceTest.Config.class})
public class FallSpaceServiceTest {

  @Mock
  private FallSpaceLogic fallSpaceLogic;

  @InjectMocks
  private FallSpaceService fallSpaceService;

  @Configuration
  static class Config {
    @Bean
    protected ModelMapper modelMapper() {
      return new ModelMapper();
    }

    @Bean
    protected ModelMapperUtil modelMapperUtil(ModelMapper modelMapper) {
      return new ModelMapperUtil(modelMapper);
    }
  }

  @Test
  @DisplayName("落下空間に関する基本情報を登録する")
  public void fallSpacePost() {
    FallSpacePostRequestDroneEntity fallSpacePostRequestDroneEntity =
        new FallSpacePostRequestDroneEntity();
    fallSpacePostRequestDroneEntity.setAircraftInfoId(1);
    fallSpacePostRequestDroneEntity.setMaker("maker");
    fallSpacePostRequestDroneEntity.setModelNumber("modelNumber");
    fallSpacePostRequestDroneEntity.setName("name");
    fallSpacePostRequestDroneEntity.setType("type");
    fallSpacePostRequestDroneEntity.setIp("ip");
    fallSpacePostRequestDroneEntity.setLength(1);
    fallSpacePostRequestDroneEntity.setWeight(1);
    fallSpacePostRequestDroneEntity.setMaximumTakeoffWeight(1);
    fallSpacePostRequestDroneEntity.setMaximumFlightTime(1);
    FallSpacePostRequestEntity fallSpacePostRequestEntity = new FallSpacePostRequestEntity();
    fallSpacePostRequestEntity.setFallToleranceRangeId("123456789012345678901234567890123456");
    fallSpacePostRequestEntity.setNumCrossSectionDivisions(20);
    fallSpacePostRequestEntity.setDroneList(Arrays.asList(fallSpacePostRequestDroneEntity));
    FallSpacePostRequestDto fallSpacePostRequestDto =
        ModelMapperUtil.map(fallSpacePostRequestEntity, FallSpacePostRequestDto.class);
    FallSpacePostResponseDto fallSpacePostResponseDto = new FallSpacePostResponseDto();
    fallSpacePostResponseDto.setAirwayDeterminationId(1);
    when(fallSpaceLogic.basicInfoRegistration(fallSpacePostRequestDto))
        .thenReturn(fallSpacePostResponseDto);
    FallSpacePostResponseEntity serviceResult =
        fallSpaceService.fallSpacePost(fallSpacePostRequestEntity);
    assertNotNull(serviceResult);
    assertNotNull(serviceResult.getAirwayDeterminationId());
  }

  @Test
  @DisplayName("落下空間(断面)を取得する")
  public void fallSpaceCrossSectionPost() {
    FallSpaceCrossSectionPostRequestEntityGeometry fallSpaceCrossSectionPostRequestEntityGeometry =
        new FallSpaceCrossSectionPostRequestEntityGeometry();
    fallSpaceCrossSectionPostRequestEntityGeometry.setType("type");
    fallSpaceCrossSectionPostRequestEntityGeometry
        .setCoordinates(Arrays.asList(Arrays.asList(BigDecimal.ONE)));
    FallSpaceCrossSectionPostRequestEntity fallSpaceCrossSectionPostRequestEntity =
        new FallSpaceCrossSectionPostRequestEntity();
    fallSpaceCrossSectionPostRequestEntity.setAirwayDeterminationId(1);
    fallSpaceCrossSectionPostRequestEntity
        .setGeometry(fallSpaceCrossSectionPostRequestEntityGeometry);
    FallSpaceCrossSectionPostRequestDto fallSpaceCrossSectionPostRequestDto = ModelMapperUtil
        .map(fallSpaceCrossSectionPostRequestEntity, FallSpaceCrossSectionPostRequestDto.class);
    FallSpaceCrossSectionPostResponseDto fallSpaceCrossSectionPostResponseDto =
        new FallSpaceCrossSectionPostResponseDto();
    fallSpaceCrossSectionPostResponseDto.setFallSpaceCrossSectionId(1);
    fallSpaceCrossSectionPostResponseDto.setData(Arrays.asList(Arrays.asList(BigDecimal.ONE)));
    when(fallSpaceLogic.getFallSpaceCrossSection(fallSpaceCrossSectionPostRequestDto))
        .thenReturn(fallSpaceCrossSectionPostResponseDto);
    FallSpaceCrossSectionPostResponseEntity serviceResult =
        fallSpaceService.fallSpaceCrossSectionPost(fallSpaceCrossSectionPostRequestEntity);
    assertNotNull(serviceResult);
    assertNotNull(serviceResult.getFallSpaceCrossSectionId());
    assertNotNull(serviceResult.getData());
  }


}
