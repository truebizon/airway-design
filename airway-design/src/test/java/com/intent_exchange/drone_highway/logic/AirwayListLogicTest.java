package com.intent_exchange.drone_highway.logic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.drone_highway.dao.AirwaySectionGeometryMapper;
import com.intent_exchange.drone_highway.dto.request.AirwayListRequestDto;
import com.intent_exchange.drone_highway.dto.response.AirwayListResponseDto;

@ExtendWith(MockitoExtension.class)
public class AirwayListLogicTest {

  @Mock
  private AirwaySectionGeometryMapper airwaySectionGeometryMapper;

  @InjectMocks
  private AirwayListLogic airwayListLogic;

  @BeforeEach
  public void setUp() {
    // 追加の初期化が必要であればここに書く
  }

  @Test
  public void testSelectAirways() {
    // テスト用のリクエストDTOの準備
    AirwayListRequestDto requestDto = new AirwayListRequestDto();
    requestDto.setPoint1("125.75,33.45");
    requestDto.setPoint2("127.55,33.45");
    requestDto.setPoint3("127.55,36.45");
    requestDto.setPoint4("125.75,36.45");

    // 予測される座標文字列
    String expectedCoordinates =
        "125.75 33.45, 127.55 33.45, 127.55 36.45, 125.75 36.45, 125.75 33.45";

    // モックするリストを準備
    List<String> expectedAirwayIds = Arrays.asList("airwayId1", "airwayId2");

    // モック設定
    when(airwaySectionGeometryMapper.selectAirwayIdsByPolygon(expectedCoordinates))
        .thenReturn(expectedAirwayIds);

    // ロジックのメソッドを呼び出し
    AirwayListResponseDto result = airwayListLogic.selectAirways(requestDto);

    // アサーション
    assertEquals(expectedAirwayIds, result.getAirwayIdList());

    // モックが正しく呼び出されたかを検証
    verify(airwaySectionGeometryMapper).selectAirwayIdsByPolygon(expectedCoordinates);
  }
}
