package com.intent_exchange.drone_highway.logic;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.drone_highway.dao.AirwayDeterminationMapper;
import com.intent_exchange.drone_highway.dao.FallToleranceRangeMapper;
import com.intent_exchange.drone_highway.dao.RailwayCrossingInfoMapper;
import com.intent_exchange.drone_highway.dto.response.RailwayRelativePositionResponseDto;
import com.intent_exchange.drone_highway.exception.DataNotFoundException;
import com.intent_exchange.drone_highway.logic.web.WebRailwayOperationLogic;
import com.intent_exchange.drone_highway.model.AirwayDetermination;
import com.intent_exchange.drone_highway.model.FallToleranceRange;
import com.intent_exchange.drone_highway.model.RailwayCrossingInfo;

/**
 * RailwayCrossingInfoLogicのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
public class RailwayCrossingInfoLogicTest {

  @Mock
  private AirwayDeterminationMapper airwayDeterminationMapper;

  @Mock
  private FallToleranceRangeMapper fallToleranceRangeMapper;

  @Mock
  private RailwayCrossingInfoMapper railwayCrossingInfoMapper;

  @Mock
  private WebRailwayOperationLogic webRailwayOperationLogic;

  @InjectMocks
  private RailwayCrossingInfoLogic railwayCrossingInfoLogic;

  @Test
  @DisplayName("路線との交点が正常に登録されること")
  public void testInsertRelativePosition1() {
    // テストデータの準備
    int airwayDeterminationId = 1;
    String airwayId = "airway123";
    LocalDateTime currentDateTimeUTC = LocalDateTime.now();

    AirwayDetermination airwayDetermination = new AirwayDetermination();
    airwayDetermination.setFallToleranceRangeId("fallToleranceRange123");

    FallToleranceRange fallToleranceRange = new FallToleranceRange();
    fallToleranceRange.setJson(
        "{\"type\":\"Polygon\",\"coordinates\":[[[135.760497,35.012033],[135.760497,30.011655],[130.76097,35.011655],[125.76097,25.012033],[135.760497,35.012033]]]}");

    RailwayRelativePositionResponseDto railwayRelativePositionResponseDto =
        new RailwayRelativePositionResponseDto();
    railwayRelativePositionResponseDto.setStation1("StationA");
    railwayRelativePositionResponseDto.setStation2("StationB");
    railwayRelativePositionResponseDto.setRelativePosition("relativePositionData");

    // モックの動作を定義
    when(airwayDeterminationMapper.selectByPrimaryKey(anyInt())).thenReturn(airwayDetermination);
    when(fallToleranceRangeMapper.selectByPrimaryKey(anyString())).thenReturn(fallToleranceRange);
    when(webRailwayOperationLogic.getRailwayRelativePosition(any()))
        .thenReturn(railwayRelativePositionResponseDto);

    // メソッドの呼び出し
    railwayCrossingInfoLogic.insertRelativePosition(airwayDeterminationId, airwayId,
        currentDateTimeUTC);

    // 結果の検証
    verify(railwayCrossingInfoMapper, times(1)).insert(any(RailwayCrossingInfo.class));
  }

  @Test
  @DisplayName("AirwayDeterminationがなく、路線との交点が正常に登録されないこと")
  public void testInsertRelativePosition2() {
    // テストデータの準備
    int airwayDeterminationId = 1;
    String airwayId = "airway123";
    LocalDateTime currentDateTimeUTC = LocalDateTime.now();

    // モックの動作を定義
    when(airwayDeterminationMapper.selectByPrimaryKey(anyInt())).thenReturn(null);

    // メソッドの呼び出しと例外の検証
    try {
      railwayCrossingInfoLogic.insertRelativePosition(airwayDeterminationId, airwayId,
          currentDateTimeUTC);
    } catch (DataNotFoundException e) {
      // 期待される例外が発生したことを確認
    }

    // 結果の検証
    verify(railwayCrossingInfoMapper, never()).insert(any(RailwayCrossingInfo.class));
  }

  @Test
  @DisplayName("FallToleranceRangeがなく、路線との交点が正常に登録されないこと")
  public void testInsertRelativePosition3() {
    // テストデータの準備
    int airwayDeterminationId = 1;
    String airwayId = "airway123";
    LocalDateTime currentDateTimeUTC = LocalDateTime.now();

    AirwayDetermination airwayDetermination = new AirwayDetermination();
    airwayDetermination.setFallToleranceRangeId("fallToleranceRange123");

    // モックの動作を定義
    when(airwayDeterminationMapper.selectByPrimaryKey(anyInt())).thenReturn(airwayDetermination);
    when(fallToleranceRangeMapper.selectByPrimaryKey(anyString())).thenReturn(null);

    // メソッドの呼び出しと例外の検証
    try {
      railwayCrossingInfoLogic.insertRelativePosition(airwayDeterminationId, airwayId,
          currentDateTimeUTC);
    } catch (DataNotFoundException e) {
      // 期待される例外が発生したことを確認
    }

    // 結果の検証
    verify(railwayCrossingInfoMapper, never()).insert(any(RailwayCrossingInfo.class));
  }

  @Test
  @DisplayName("ModelMapperUtil.stringToDoubleListでエラーが発生")
  public void testInsertRelativePosition4() {
    // テストデータの準備
    int airwayDeterminationId = 1;
    String airwayId = "airway123";
    LocalDateTime currentDateTimeUTC = LocalDateTime.now();

    AirwayDetermination airwayDetermination = new AirwayDetermination();
    airwayDetermination.setFallToleranceRangeId("fallToleranceRange123");

    FallToleranceRange fallToleranceRange = new FallToleranceRange();
    fallToleranceRange.setJson("geometryData");

    RailwayRelativePositionResponseDto railwayRelativePositionResponseDto = null;

    // モックの動作を定義
    when(airwayDeterminationMapper.selectByPrimaryKey(anyInt())).thenReturn(airwayDetermination);
    when(fallToleranceRangeMapper.selectByPrimaryKey(anyString())).thenReturn(fallToleranceRange);
    when(webRailwayOperationLogic.getRailwayRelativePosition(any()))
        .thenReturn(railwayRelativePositionResponseDto);

    // メソッドの呼び出し
    railwayCrossingInfoLogic.insertRelativePosition(airwayDeterminationId, airwayId,
        currentDateTimeUTC);

    // 結果の検証
    verify(railwayCrossingInfoMapper, never()).insert(any(RailwayCrossingInfo.class));
  }
}
