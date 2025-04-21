package com.intent_exchange.drone_highway.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
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
import com.intent_exchange.drone_highway.dto.request.AirwayListRequestDto;
import com.intent_exchange.drone_highway.dto.response.AirwayListResponseDto;
import com.intent_exchange.drone_highway.entity.AirwayEntity;
import com.intent_exchange.drone_highway.entity.AirwayListRequestEntity;
import com.intent_exchange.drone_highway.logic.AirwayDesignLogic;
import com.intent_exchange.drone_highway.logic.AirwayListLogic;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {AirwayListServiceTest.Config.class})
public class AirwayListServiceTest {

  @Mock
  private AirwayListLogic airwayListLogic;

  @Mock
  private AirwayDesignLogic airwayDesignLogic;

  @InjectMocks
  private AirwayListService airwayListService;

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
  public void testAirwayListGet() {
    // テスト用のリクエストエンティティを作成
    AirwayListRequestEntity requestEntity = new AirwayListRequestEntity();
    requestEntity.setPoint1("125.75,33.45");
    requestEntity.setPoint2("127.55,33.45");
    requestEntity.setPoint3("127.55,36.45");
    requestEntity.setPoint4("125.75,36.45");

    // モックレスポンスの準備
    AirwayListResponseDto mockResponseDto = new AirwayListResponseDto();
    List<String> airwayIdList = Arrays.asList("airwayId1", "airwayId2");
    mockResponseDto.setAirwayIdList(airwayIdList);

    // モック設定
    AirwayListRequestDto requestDto =
        ModelMapperUtil.map(requestEntity, AirwayListRequestDto.class);
    when(airwayListLogic.selectAirways(requestDto)).thenReturn(mockResponseDto);

    AirwayEntity expectedEntity = new AirwayEntity();
    when(airwayDesignLogic.airwayGet(airwayIdList, false)).thenReturn(expectedEntity);

    // サービスのテスト実行
    AirwayEntity result = airwayListService.airwayListGet(requestEntity);

    // 結果のアサーション
    assertEquals(expectedEntity, result);
  }
}
