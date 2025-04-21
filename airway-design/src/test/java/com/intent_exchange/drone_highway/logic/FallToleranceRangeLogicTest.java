package com.intent_exchange.drone_highway.logic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intent_exchange.drone_highway.dao.AirwayDeterminationMapper;
import com.intent_exchange.drone_highway.dao.AirwayMapper;
import com.intent_exchange.drone_highway.dao.FallToleranceRangeMapper;
import com.intent_exchange.drone_highway.dto.request.FallToleranceRangeDeleteRequestDto;
import com.intent_exchange.drone_highway.dto.request.FallToleranceRangeDetailGetRequestDto;
import com.intent_exchange.drone_highway.dto.request.FallToleranceRangeGetRequestDto;
import com.intent_exchange.drone_highway.dto.request.FallToleranceRangePostRequestDto;
import com.intent_exchange.drone_highway.dto.request.FallToleranceRangePostRequestGeometryDto;
import com.intent_exchange.drone_highway.dto.response.FallToleranceRangeDetailGetResponseDto;
import com.intent_exchange.drone_highway.dto.response.FallToleranceRangeGetResponseDto;
import com.intent_exchange.drone_highway.dto.response.FallToleranceRangeGetResponseItemDto;
import com.intent_exchange.drone_highway.dto.response.FallToleranceRangePostResponseDto;
import com.intent_exchange.drone_highway.exception.DataInUseException;
import com.intent_exchange.drone_highway.exception.DataNotFoundException;
import com.intent_exchange.drone_highway.exception.DroneHighwayException;
import com.intent_exchange.drone_highway.model.AirwayDetermination;
import com.intent_exchange.drone_highway.model.FallToleranceRange;
import com.intent_exchange.drone_highway.model.FallToleranceRangeAirwayIdUse;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {FallToleranceRangeLogicTest.Config.class})
class FallToleranceRangeLogicTest {

  /** 落下許容範囲マッパー */
  @Mock
  private FallToleranceRangeMapper fallToleranceRangeMapper;

  /** 航路画定マッパー */
  @Mock
  private AirwayDeterminationMapper airwayDeterminationMapper;

  /** 航路マッパー */
  @Mock
  private AirwayMapper airwayMapper;

  /** クロック */
  @Mock
  private Clock clock;

  /** ロジック */
  @InjectMocks
  private FallToleranceRangeLogic fallToleranceRangeLogic;

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
  @DisplayName("最大落下許容範囲登録")
  void testInsert() {
    when(clock.instant()).thenReturn(Instant.now(Clock.systemUTC()));
    when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    FallToleranceRangePostRequestDto postRequestDto = getInsertRequestDto();
    FallToleranceRangePostResponseDto result = fallToleranceRangeLogic.insert(postRequestDto);
    assertNotNull(result);
    assertNotNull(result.getFallToleranceRangeId());
    assertNotEquals(result.getFallToleranceRangeId(), "");
    assertNotNull(result.getCreatedAt());
    assertNotNull(result.getName());
    assertEquals(result.getName(), postRequestDto.getName());
  }

  @Test
  @DisplayName("最大落下許容範囲登録 throw DroneHighwayException")
  void testInsertThrowDroneHighwayException() {
    when(clock.instant()).thenReturn(Instant.now(Clock.systemUTC()));
    when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    FallToleranceRangePostRequestDto postRequestDto = getInsertRequestDto();
    try (MockedConstruction<ObjectMapper> ignored = mockConstruction(ObjectMapper.class, (mock,
        ctx) -> doThrow(JsonProcessingException.class).when(mock).writeValueAsString(any()))) {
      assertThrows(DroneHighwayException.class,
          () -> fallToleranceRangeLogic.insert(postRequestDto));
    }
  }

  private FallToleranceRangePostRequestDto getInsertRequestDto() {
    String json =
        "{\"type\": \"Polygon\",\"coordinates\": [[[139.84425094162697,35.65649757318167],[139.8506696707106,35.65611144123251],[139.85103098834156,35.67572492318939],[139.8603064948997,35.69311183099994],[139.85495492073812,35.69407775293554],[139.84484594816934,35.67678697870946],[139.84425094162697,35.65649757318167]]]}";
    FallToleranceRangePostRequestDto result = new FallToleranceRangePostRequestDto();
    result.setBusinessNumber("businessNumber01");
    result.setAirwayOperatorId("airwayOperatorId01");
    result.setName("name01");
    result.setAreaName("areaName01");
    result.setElevationTerrain("elevationTerrain01");
    try {
      result.setGeometry(
          new ObjectMapper().readValue(json, FallToleranceRangePostRequestGeometryDto.class));
    } catch (JsonProcessingException e) {
      throw new DroneHighwayException(e.getMessage());
    }
    return result;
  }

  @Test
  @DisplayName("最大落下許容範囲一覧検索")
  void testSelect() {
    when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    when(fallToleranceRangeMapper.selectByConditions(Mockito.any()))
        .thenReturn(getFallToleranceRangeAirwayIdUse());
    FallToleranceRangeGetResponseDto result = fallToleranceRangeLogic.select(getSelectRequestDto());
    for (FallToleranceRangeGetResponseItemDto item : result.getFallToleranceRanges()) {
      assertNotNull(item.getFallToleranceRangeId());
      assertNotNull(item.getGeometry());
    }
  }

  @Test
  @DisplayName("最大落下許容範囲一覧検索  throw DroneHighwayException")
  void testSelectThrowDroneHighwayException() {
    when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    List<FallToleranceRangeAirwayIdUse> list = getFallToleranceRangeAirwayIdUse();
    list.get(0).setJson("aaaaaaaa");
    when(fallToleranceRangeMapper.selectByConditions(Mockito.any())).thenReturn(list);
    assertThrows(DroneHighwayException.class,
        () -> fallToleranceRangeLogic.select(getSelectRequestDto()));
  }

  private FallToleranceRangeGetRequestDto getSelectRequestDto() {
    FallToleranceRangeGetRequestDto result = new FallToleranceRangeGetRequestDto();
    result.setBusinessNumber("businessNumberSelect");
    result.setAreaName("areaNameSelect");
    return result;

  }


  private List<FallToleranceRangeAirwayIdUse> getFallToleranceRangeAirwayIdUse() {
    String json =
        "{\"type\": \"Polygon\",\"coordinates\": [[[139.84425094162697,35.65649757318167],[139.8506696707106,35.65611144123251],[139.85103098834156,35.67572492318939],[139.8603064948997,35.69311183099994],[139.85495492073812,35.69407775293554],[139.84484594816934,35.67678697870946],[139.84425094162697,35.65649757318167]]]}";
    List<FallToleranceRangeAirwayIdUse> result = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      FallToleranceRangeAirwayIdUse item = new FallToleranceRangeAirwayIdUse();
      item.setFallToleranceRangeId(UUID.randomUUID().toString());
      item.setCreatedAt(LocalDateTime.now());
      item.setUpdatedAt(item.getCreatedAt());
      item.setJson(json);
      result.add(item);
    }
    return result;
  }

  @Test
  @DisplayName("最大落下許容範囲削除")
  void testDelete() {
    FallToleranceRangeDeleteRequestDto requestDto = getFallToleranceRangeDeleteRequestDto();
    when(clock.instant()).thenReturn(Instant.now(Clock.systemUTC()));
    when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    when(fallToleranceRangeMapper.selectLockByPrimaryKey(requestDto.getFallToleranceRangeId()))
        .thenReturn(getFallToleranceRange(requestDto.getFallToleranceRangeId(),
            requestDto.getBusinessNumber()));
    assertDoesNotThrow(() -> fallToleranceRangeLogic.delete(requestDto));
  }

  @Test
  @DisplayName("最大落下許容範囲削除 航路使用中")
  void testDeleteAirwayInUse() {
    FallToleranceRangeDeleteRequestDto requestDto = getFallToleranceRangeDeleteRequestDto();
    when(fallToleranceRangeMapper.selectLockByPrimaryKey(requestDto.getFallToleranceRangeId()))
        .thenReturn(getFallToleranceRange(requestDto.getFallToleranceRangeId(),
            requestDto.getBusinessNumber()));
    when(airwayDeterminationMapper
        .selectLockByFallToleranceRangeId(requestDto.getFallToleranceRangeId()))
            .thenReturn(getAirwayDetermination(requestDto));
    when(airwayMapper.selectCountByAirwayDeterminationId(anyList())).thenReturn(1);
    assertThrows(DataInUseException.class, () -> fallToleranceRangeLogic.delete(requestDto));
  }

  @Test
  @DisplayName("最大落下許容範囲削除 航路画定削除")
  void testDeleteAndAirwayDeterminationDelete() {
    FallToleranceRangeDeleteRequestDto requestDto = getFallToleranceRangeDeleteRequestDto();
    when(clock.instant()).thenReturn(Instant.now(Clock.systemUTC()));
    when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    when(fallToleranceRangeMapper.selectLockByPrimaryKey(requestDto.getFallToleranceRangeId()))
        .thenReturn(getFallToleranceRange(requestDto.getFallToleranceRangeId(),
            requestDto.getBusinessNumber()));
    when(airwayDeterminationMapper
        .selectLockByFallToleranceRangeId(requestDto.getFallToleranceRangeId()))
            .thenReturn(getAirwayDetermination(requestDto));
    when(airwayMapper.selectCountByAirwayDeterminationId(anyList())).thenReturn(0);
    assertDoesNotThrow(() -> fallToleranceRangeLogic.delete(requestDto));
  }

  private FallToleranceRangeDeleteRequestDto getFallToleranceRangeDeleteRequestDto() {
    FallToleranceRangeDeleteRequestDto result = new FallToleranceRangeDeleteRequestDto();
    result.setFallToleranceRangeId(UUID.randomUUID().toString());
    result.setBusinessNumber("businessNumberDelete");
    return result;
  }

  private List<AirwayDetermination> getAirwayDetermination(FallToleranceRangeDeleteRequestDto dto) {
    AirwayDetermination airwayDetermination = new AirwayDetermination();
    airwayDetermination.setAirwayDeterminationId(1);
    airwayDetermination.setFallToleranceRangeId(dto.getFallToleranceRangeId());
    return Arrays.asList(airwayDetermination);
  }

  private FallToleranceRange getFallToleranceRange(String fallToleranceRangeId,
      String businessNumber) {
    FallToleranceRange result = new FallToleranceRange();
    String json =
        "{\"type\": \"Polygon\",\"coordinates\": [[[139.84425094162697,35.65649757318167],[139.8506696707106,35.65611144123251],[139.85103098834156,35.67572492318939],[139.8603064948997,35.69311183099994],[139.85495492073812,35.69407775293554],[139.84484594816934,35.67678697870946],[139.84425094162697,35.65649757318167]]]}";
    result.setFallToleranceRangeId(fallToleranceRangeId);
    result.setBusinessNumber(businessNumber);
    result.setAirwayOperatorId("airwayOperatorId01");
    result.setName("name01");
    result.setAreaName("areaName01");
    result.setElevationTerrain("elevationTerrain01");
    result.setDelete(false);
    result.setCreatedAt(LocalDateTime.now());
    result.setUpdatedAt(result.getCreatedAt());
    result.setJson(json);
    try {
      result.setGeometry(new ObjectMapper().readValue(result.getJson(),
          FallToleranceRangePostRequestGeometryDto.class));
    } catch (JsonProcessingException e) {
      throw new DroneHighwayException(e.getMessage());
    }
    return result;
  }

  @Test
  @DisplayName("最大落下許容範囲詳細検索")
  void testSelectDetail() {
    FallToleranceRangeDetailGetRequestDto requestDto = new FallToleranceRangeDetailGetRequestDto();
    requestDto.setFallToleranceRangeId(UUID.randomUUID().toString());
    requestDto.setBusinessNumber("businessNumberSelectDetail");
    FallToleranceRange queryResult =
        getFallToleranceRange(requestDto.getFallToleranceRangeId(), requestDto.getBusinessNumber());
    when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    when(fallToleranceRangeMapper.selectByPrimaryKey(requestDto.getFallToleranceRangeId()))
        .thenReturn(queryResult);
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic
          .when(
              () -> ModelMapperUtil.map(queryResult, FallToleranceRangeDetailGetResponseDto.class))
          .thenReturn(getSelectDetailResponseDto(queryResult));
      FallToleranceRangeDetailGetResponseDto result =
          fallToleranceRangeLogic.selectDetail(requestDto);
      assertEquals(result.getFallToleranceRangeId(), requestDto.getFallToleranceRangeId());
      assertEquals(result.getBusinessNumber(), requestDto.getBusinessNumber());
    }
  }

  @Test
  @DisplayName("最大落下許容範囲詳細検索 最大落下許容範囲 無し")
  void testSelectDetailFallToleranceRangeNull() {
    FallToleranceRangeDetailGetRequestDto requestDto = new FallToleranceRangeDetailGetRequestDto();
    requestDto.setFallToleranceRangeId(UUID.randomUUID().toString());
    requestDto.setBusinessNumber("businessNumberSelectDetail");
    when(fallToleranceRangeMapper.selectByPrimaryKey(requestDto.getFallToleranceRangeId()))
        .thenReturn(null);
    assertThrows(DataNotFoundException.class,
        () -> fallToleranceRangeLogic.selectDetail(requestDto));
  }

  @Test
  @DisplayName("最大落下許容範囲詳細検索 最大落下許容範囲 事業者番号 不一致")
  void testSelectDetailFallToleranceRangeBusinessNumberNotEquals() {
    FallToleranceRangeDetailGetRequestDto requestDto = new FallToleranceRangeDetailGetRequestDto();
    requestDto.setFallToleranceRangeId(UUID.randomUUID().toString());
    requestDto.setBusinessNumber("businessNumberSelectDetail");
    FallToleranceRange queryResult = getFallToleranceRange(requestDto.getFallToleranceRangeId(),
        requestDto.getBusinessNumber() + "test");
    when(fallToleranceRangeMapper.selectByPrimaryKey(requestDto.getFallToleranceRangeId()))
        .thenReturn(queryResult);
    assertThrows(DataNotFoundException.class,
        () -> fallToleranceRangeLogic.selectDetail(requestDto));

  }

  @Test
  @DisplayName("最大落下許容範囲詳細検索 最大落下許容範囲 削除済み")
  void testSelectDetailFallToleranceRangeBusinessDelete() {
    FallToleranceRangeDetailGetRequestDto requestDto = new FallToleranceRangeDetailGetRequestDto();
    requestDto.setFallToleranceRangeId(UUID.randomUUID().toString());
    requestDto.setBusinessNumber("businessNumberSelectDetail");
    FallToleranceRange queryResult =
        getFallToleranceRange(requestDto.getFallToleranceRangeId(), requestDto.getBusinessNumber());
    queryResult.setDelete(true);
    when(fallToleranceRangeMapper.selectByPrimaryKey(requestDto.getFallToleranceRangeId()))
        .thenReturn(queryResult);
    assertThrows(DataNotFoundException.class,
        () -> fallToleranceRangeLogic.selectDetail(requestDto));

  }

  @Test
  @DisplayName("最大落下許容範囲詳細検索 throw DroneHighwayException")
  void testSelectDetailThrowDroneHighwayException() {
    FallToleranceRangeDetailGetRequestDto requestDto = new FallToleranceRangeDetailGetRequestDto();
    requestDto.setFallToleranceRangeId(UUID.randomUUID().toString());
    requestDto.setBusinessNumber("businessNumberSelectDetail");
    FallToleranceRange queryResult =
        getFallToleranceRange(requestDto.getFallToleranceRangeId(), requestDto.getBusinessNumber());
    queryResult.setJson("aaaaaaaaaaaaaaaaaaaaa");
    when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    when(fallToleranceRangeMapper.selectByPrimaryKey(requestDto.getFallToleranceRangeId()))
        .thenReturn(queryResult);
    assertThrows(DroneHighwayException.class,
        () -> fallToleranceRangeLogic.selectDetail(requestDto));
  }

  private FallToleranceRangeDetailGetResponseDto getSelectDetailResponseDto(
      FallToleranceRange fallToleranceRange) {
    FallToleranceRangeDetailGetResponseDto result = new FallToleranceRangeDetailGetResponseDto();
    result.setFallToleranceRangeId(fallToleranceRange.getFallToleranceRangeId());
    result.setBusinessNumber(fallToleranceRange.getBusinessNumber());
    result.setAirwayOperatorId(fallToleranceRange.getAirwayOperatorId());
    result.setName(fallToleranceRange.getName());
    result.setAreaName(fallToleranceRange.getAreaName());
    result.setElevationTerrain(fallToleranceRange.getElevationTerrain());
    return result;
  }

}
