package com.intent_exchange.drone_highway.logic.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.RestTemplate;
import com.intent_exchange.drone_highway.dto.request.RailwayRelativePositionRequestDto;
import com.intent_exchange.drone_highway.dto.response.RailwayRelativePositionResponseDto;
import com.intent_exchange.drone_highway.util.PropertyUtil;

/**
 * 空域デジタルツイン 鉄道運行情報 (A-1-1-1-5)への通信用ロジックのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class WebRailwayOperationLogicTest {

  @Mock
  @Qualifier("customRestTemplate")
  private RestTemplate restTemplate;

  @InjectMocks
  private WebRailwayOperationLogic logic;

  @Test
  @DisplayName("鉄道運行位置特定情報取得 正常終了")
  void testGetRailwayOperationInformation1() {
    // テストデータの準備
    RailwayRelativePositionRequestDto requestDto = new RailwayRelativePositionRequestDto();
    requestDto.setFallToleranceRange(
        Arrays.asList(Arrays.asList(135.760497, 35.012033), Arrays.asList(135.760497, 35.012033)));

    String url = PropertyUtil.getProperty("post.railway.relative.position.url");

    // レスポンス
    RailwayRelativePositionResponseDto responseDto = new RailwayRelativePositionResponseDto();
    responseDto.setStation1("station1");
    responseDto.setStation2("station2");
    responseDto.setRelativePosition("0.6");

    // モックのRestTemplateの動作を定義
    when(restTemplate.postForObject(url, requestDto, RailwayRelativePositionResponseDto.class))
        .thenReturn(responseDto);

    // メソッドの呼び出し
    RailwayRelativePositionResponseDto response = logic.getRailwayRelativePosition(requestDto);

    // 結果の検証
    assertNotNull(response);
    assertEquals("station1", response.getStation1());
    assertEquals("station2", response.getStation2());
    assertEquals("0.6", response.getRelativePosition());

  }
}
