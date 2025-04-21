package com.intent_exchange.drone_highway.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intent_exchange.drone_highway.dto.request.FallToleranceRangeDetailGetRequestDto;
import com.intent_exchange.drone_highway.dto.request.FallToleranceRangeGetRequestDto;
import com.intent_exchange.drone_highway.dto.request.FallToleranceRangePostRequestDto;
import com.intent_exchange.drone_highway.dto.response.FallToleranceRangeDetailGetResponseDto;
import com.intent_exchange.drone_highway.dto.response.FallToleranceRangeDetailGetResponseGeometryDto;
import com.intent_exchange.drone_highway.dto.response.FallToleranceRangeGetResponseDto;
import com.intent_exchange.drone_highway.dto.response.FallToleranceRangeGetResponseItemDto;
import com.intent_exchange.drone_highway.dto.response.FallToleranceRangeGetResponseItemGeometryDto;
import com.intent_exchange.drone_highway.dto.response.FallToleranceRangePostResponseDto;
import com.intent_exchange.drone_highway.entity.FallToleranceRangeDeleteRequestEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangeDetailGetRequestEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangeDetailGetResponseEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangeGetRequestEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangeGetResponseEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangeGetResponseItemEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangePostRequestEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangePostRequestEntityGeometry;
import com.intent_exchange.drone_highway.entity.FallToleranceRangePostResponseEntity;
import com.intent_exchange.drone_highway.exception.DroneHighwayException;
import com.intent_exchange.drone_highway.logic.FallToleranceRangeLogic;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {FallToleranceRangeServiceTest.Config.class})
public class FallToleranceRangeServiceTest {

  @Mock
  private FallToleranceRangeLogic fallToleranceRangeLogic;

  @InjectMocks
  private FallToleranceRangeService fallToleranceRangeService;

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
  @DisplayName("最大落下許容範囲を取得する")
  public void fallToleranceRangeGet() {
    FallToleranceRangeGetRequestEntity fallToleranceRangeGetRequestEntity =
        new FallToleranceRangeGetRequestEntity();
    fallToleranceRangeGetRequestEntity.setBusinessNumber("businessNumber");
    FallToleranceRangeGetRequestDto fallToleranceRangeGetRequestDto =
        new FallToleranceRangeGetRequestDto();
    fallToleranceRangeGetRequestDto
        .setBusinessNumber(fallToleranceRangeGetRequestEntity.getBusinessNumber());
    FallToleranceRangeGetResponseItemGeometryDto fallToleranceRangeGetResponseItemGeometryDto =
        new FallToleranceRangeGetResponseItemGeometryDto();
    fallToleranceRangeGetResponseItemGeometryDto.setType("type");
    fallToleranceRangeGetResponseItemGeometryDto
        .setCoordinates(Arrays.asList(Arrays.asList(Arrays.asList(BigDecimal.ONE))));
    FallToleranceRangeGetResponseItemDto fallToleranceRangeGetResponseItemDto =
        new FallToleranceRangeGetResponseItemDto();
    fallToleranceRangeGetResponseItemDto.setFallToleranceRangeId("fallToleranceRangeId");
    fallToleranceRangeGetResponseItemDto.setName("name");
    fallToleranceRangeGetResponseItemDto.setAreaName("areaName");
    fallToleranceRangeGetResponseItemDto.setAirwayOperatorId("airwayOperatorId");
    fallToleranceRangeGetResponseItemDto.setAirwayIdUse(Arrays.asList("airwayIdUse"));
    fallToleranceRangeGetResponseItemDto.setGeometry(fallToleranceRangeGetResponseItemGeometryDto);
    fallToleranceRangeGetResponseItemDto.setCreatedAt(new Date());
    fallToleranceRangeGetResponseItemDto.setUpdatedAt(new Date());
    FallToleranceRangeGetResponseDto fallToleranceRangeGetResponseEntity =
        new FallToleranceRangeGetResponseDto();
    fallToleranceRangeGetResponseEntity
        .setFallToleranceRanges(Arrays.asList(fallToleranceRangeGetResponseItemDto));
    when(fallToleranceRangeLogic.select(fallToleranceRangeGetRequestDto))
        .thenReturn(fallToleranceRangeGetResponseEntity);
    FallToleranceRangeGetResponseEntity serviceResult =
        fallToleranceRangeService.fallToleranceRangeGet(fallToleranceRangeGetRequestEntity);
    for (FallToleranceRangeGetResponseItemEntity item : serviceResult.getFallToleranceRanges()) {
      assertNotNull(item.getFallToleranceRangeId());
      assertNotNull(item.getGeometry());
    }
  }

  @Test
  @DisplayName("最大落下許容範囲を登録する")
  public void fallToleranceRangePost() {
    FallToleranceRangePostRequestEntity fallToleranceRangePostRequestEntity =
        getInsertRequestEntity();
    FallToleranceRangePostRequestDto fallToleranceRangePostRequestDto = ModelMapperUtil
        .map(fallToleranceRangePostRequestEntity, FallToleranceRangePostRequestDto.class);

    FallToleranceRangePostResponseDto fallToleranceRangePostResponseDto =
        new FallToleranceRangePostResponseDto();
    fallToleranceRangePostResponseDto.setCreatedAt(new Date());
    fallToleranceRangePostResponseDto
        .setFallToleranceRangeId("123456789012345678901234567890123456");
    fallToleranceRangePostResponseDto.setName(fallToleranceRangePostRequestEntity.getName());
    when(fallToleranceRangeLogic.insert(fallToleranceRangePostRequestDto))
        .thenReturn(fallToleranceRangePostResponseDto);
    FallToleranceRangePostResponseEntity serviceResult =
        fallToleranceRangeService.fallToleranceRangePost(fallToleranceRangePostRequestEntity);
    assertNotNull(serviceResult);
    assertNotNull(serviceResult.getFallToleranceRangeId());
    assertNotEquals(serviceResult.getFallToleranceRangeId(), "");
    assertNotNull(serviceResult.getCreatedAt());
    assertNotNull(serviceResult.getName());
    assertEquals(serviceResult.getName(), fallToleranceRangePostRequestEntity.getName());
  }

  private FallToleranceRangePostRequestEntity getInsertRequestEntity() {
    String json =
        "{\"type\": \"Polygon\",\"coordinates\": [[[139.84425094162697,35.65649757318167],[139.8506696707106,35.65611144123251],[139.85103098834156,35.67572492318939],[139.8603064948997,35.69311183099994],[139.85495492073812,35.69407775293554],[139.84484594816934,35.67678697870946],[139.84425094162697,35.65649757318167]]]}";
    FallToleranceRangePostRequestEntity result = new FallToleranceRangePostRequestEntity();
    result.setBusinessNumber("businessNumber01");
    result.setAirwayOperatorId("airwayOperatorId01");
    result.setName("name01");
    result.setAreaName("areaName01");
    result.setElevationTerrain("elevationTerrain01");
    try {
      result.setGeometry(
          new ObjectMapper().readValue(json, FallToleranceRangePostRequestEntityGeometry.class));
    } catch (JsonProcessingException e) {
      throw new DroneHighwayException(e.getMessage());
    }
    return result;
  }

  @Test
  @DisplayName("最大落下許容範囲を削除する")
  public void fallToleranceRangeDelete() {
    FallToleranceRangeDeleteRequestEntity fallToleranceRangeDeleteRequestEntity =
        new FallToleranceRangeDeleteRequestEntity();
    fallToleranceRangeDeleteRequestEntity
        .setFallToleranceRangeId("123456789012345678901234567890123456");
    fallToleranceRangeDeleteRequestEntity.setBusinessNumber("businessNumber01");
    assertDoesNotThrow(() -> fallToleranceRangeService
        .fallToleranceRangeDelete(fallToleranceRangeDeleteRequestEntity));
  }

  @Test
  @DisplayName("最大落下許容範囲の詳細情報を取得する")
  public void fallToleranceRangeDetailGet() {
    FallToleranceRangeDetailGetRequestEntity fallToleranceRangeDetailGetRequestEntity =
        new FallToleranceRangeDetailGetRequestEntity();
    fallToleranceRangeDetailGetRequestEntity
        .setFallToleranceRangeId("123456789012345678901234567890123456");
    fallToleranceRangeDetailGetRequestEntity.setBusinessNumber("businessNumber01");
    FallToleranceRangeDetailGetRequestDto fallToleranceRangeDetailGetRequestDto = ModelMapperUtil
        .map(fallToleranceRangeDetailGetRequestEntity, FallToleranceRangeDetailGetRequestDto.class);
    FallToleranceRangeDetailGetResponseDto fallToleranceRangeDetailGetResponseDto =
        getFallToleranceRangeDetailGetResponseDto(fallToleranceRangeDetailGetRequestEntity);
    when(fallToleranceRangeLogic.selectDetail(fallToleranceRangeDetailGetRequestDto))
        .thenReturn(fallToleranceRangeDetailGetResponseDto);
    FallToleranceRangeDetailGetResponseEntity serviceResult = fallToleranceRangeService
        .fallToleranceRangeDetailGet(fallToleranceRangeDetailGetRequestEntity);
    assertEquals(serviceResult.getFallToleranceRangeId(),
        fallToleranceRangeDetailGetRequestEntity.getFallToleranceRangeId());
    assertEquals(serviceResult.getBusinessNumber(),
        fallToleranceRangeDetailGetRequestEntity.getBusinessNumber());
  }

  private FallToleranceRangeDetailGetResponseDto getFallToleranceRangeDetailGetResponseDto(
      FallToleranceRangeDetailGetRequestEntity fallToleranceRangeDetailGetRequestEntity) {
    FallToleranceRangeDetailGetResponseDto result = new FallToleranceRangeDetailGetResponseDto();
    String json =
        "{\"type\": \"Polygon\",\"coordinates\": [[[139.84425094162697,35.65649757318167],[139.8506696707106,35.65611144123251],[139.85103098834156,35.67572492318939],[139.8603064948997,35.69311183099994],[139.85495492073812,35.69407775293554],[139.84484594816934,35.67678697870946],[139.84425094162697,35.65649757318167]]]}";
    result.setFallToleranceRangeId(
        fallToleranceRangeDetailGetRequestEntity.getFallToleranceRangeId());
    result.setBusinessNumber(fallToleranceRangeDetailGetRequestEntity.getBusinessNumber());
    result.setAirwayOperatorId("airwayOperatorId01");
    result.setName("name01");
    result.setAreaName("areaName01");
    result.setElevationTerrain("elevationTerrain01");

    result.setCreatedAt(new Date());
    result.setUpdatedAt(result.getCreatedAt());
    try {
      result.setGeometry(
          new ObjectMapper().readValue(json, FallToleranceRangeDetailGetResponseGeometryDto.class));
    } catch (JsonProcessingException e) {
      throw new DroneHighwayException(e.getMessage());
    }
    return result;
  }

}
