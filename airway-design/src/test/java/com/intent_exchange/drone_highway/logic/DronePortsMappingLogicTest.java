package com.intent_exchange.drone_highway.logic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.intent_exchange.drone_highway.dao.AirwayMapper;
import com.intent_exchange.drone_highway.dao.AirwaySequenceMapper;
import com.intent_exchange.drone_highway.dao.MappingDroneportSectionMapper;
import com.intent_exchange.drone_highway.dto.request.DronePortsMappingPostRequestDto;
import com.intent_exchange.drone_highway.dto.request.DronePortsMappingPostRequestDtoAirwaySectionsInner;
import com.intent_exchange.drone_highway.exception.DataNotFoundException;
import com.intent_exchange.drone_highway.model.Airway;
import com.intent_exchange.drone_highway.model.MappingDroneportSection;

public class DronePortsMappingLogicTest {

  @InjectMocks
  private DronePortsMappingLogic dronePortsMappingLogic;

  @Mock
  private AirwayMapper airwayMapper;

  @Mock
  private MappingDroneportSectionMapper mappingDroneportSectionMapper;

  @Mock
  private AirwaySequenceMapper airwaySequenceMapper;

  @Mock
  private AirwayDesignLogic airwayDesignLogic;

  @Mock
  private Clock clock;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("正常系:登録(ドローンポートマッピング情報1つ、ドローンポートID1つ)")
  public void testDronePortsMappingPost_singleAirwaySection() {
    DronePortsMappingPostRequestDto request = createRequestWithOneSection();
    // ドローンポートマッピング情報は1つで、存在チェック、整合性チェックのカウントも1件検出
    when(mappingDroneportSectionMapper.countAirwaySectionIds(anyList(), anyString())).thenReturn(1);

    // MQTT情報の設定
    when(clock.instant()).thenReturn(LocalDateTime.now()
        .toInstant(Clock.systemUTC().getZone().getRules().getOffset(LocalDateTime.now())));
    when(clock.getZone()).thenReturn(Clock.systemUTC().getZone());
    when(airwayMapper.selectByPrimaryKey(anyString())).thenReturn(new Airway());

    dronePortsMappingLogic.dronePortsMappingPost(request);

    // 削除処理が正しく呼ばれたことを確認
    verify(mappingDroneportSectionMapper).deleteByAirwaySectionId(anyList());
    // 挿入処理が1件正しく呼ばれたことを確認
    verify(mappingDroneportSectionMapper, times(1)).insert(any(MappingDroneportSection.class));
    // MQTT更新通知処理が呼ばれたことを確認
    verify(airwayDesignLogic).notifyAirwayUpdated(anyString(), any(), any(),
        eq(AirwayDesignLogic.AIRWAY_STATUS_UPDATED));
  }

  @Test
  @DisplayName("正常系:登録(ドローンポートマッピング情報2つ、ドローンポートID3つ)")
  public void testDronePortsMappingPost_multipleAirwaySections() {
    DronePortsMappingPostRequestDto request = createRequestWithMultipleSections();
    // ドローンポートマッピング情報は2つで、存在チェック、整合性チェックのカウントも2件検出
    when(mappingDroneportSectionMapper.countAirwaySectionIds(anyList(), anyString())).thenReturn(2);

    // MQTT情報の設定
    when(clock.instant()).thenReturn(LocalDateTime.now()
        .toInstant(Clock.systemUTC().getZone().getRules().getOffset(LocalDateTime.now())));
    when(clock.getZone()).thenReturn(Clock.systemUTC().getZone());
    when(airwayMapper.selectByPrimaryKey(anyString())).thenReturn(new Airway());

    dronePortsMappingLogic.dronePortsMappingPost(request);

    // 削除処理が正しく呼ばれたことを確認
    verify(mappingDroneportSectionMapper).deleteByAirwaySectionId(anyList());
    // 挿入処理が3件正しく呼ばれたことを確認
    verify(mappingDroneportSectionMapper, times(3)).insert(any(MappingDroneportSection.class));
    // MQTT更新通知処理が呼ばれたことを確認
    verify(airwayDesignLogic).notifyAirwayUpdated(anyString(), any(), any(),
        eq(AirwayDesignLogic.AIRWAY_STATUS_UPDATED));
  }

  @Test
  @DisplayName("正常系:削除(ドローンポートIDが空)")
  public void testDronePortsMappingPost_dronePortZero() {
    DronePortsMappingPostRequestDto request = createRequestWithZeroDronePortId();
    // ドローンポートマッピング情報は1つで、存在チェック、整合性チェックのカウントも1件検出
    when(mappingDroneportSectionMapper.countAirwaySectionIds(anyList(), anyString())).thenReturn(1);

    // MQTT情報の設定
    when(clock.instant()).thenReturn(LocalDateTime.now()
        .toInstant(Clock.systemUTC().getZone().getRules().getOffset(LocalDateTime.now())));
    when(clock.getZone()).thenReturn(Clock.systemUTC().getZone());
    when(airwayMapper.selectByPrimaryKey(anyString())).thenReturn(new Airway());

    dronePortsMappingLogic.dronePortsMappingPost(request);

    // 削除処理が正しく呼ばれたことを確認
    verify(mappingDroneportSectionMapper).deleteByAirwaySectionId(anyList());
    // 挿入処理が呼ばれていないことを確認
    verify(mappingDroneportSectionMapper, never()).insert(any());
    // MQTT更新通知処理が呼ばれたことを確認
    verify(airwayDesignLogic).notifyAirwayUpdated(anyString(), any(), any(),
        eq(AirwayDesignLogic.AIRWAY_STATUS_UPDATED));
  }

  @Test
  @DisplayName("正常系:削除(ドローンポートIDがNull)")
  public void testDronePortsMappingPost_dronePortNull() {
    DronePortsMappingPostRequestDto request = createRequestWithNullDronePortId();
    // ドローンポートマッピング情報は1つで、存在チェック、整合性チェックのカウントも1件検出
    when(mappingDroneportSectionMapper.countAirwaySectionIds(anyList(), anyString())).thenReturn(1);

    // MQTT情報の設定
    when(clock.instant()).thenReturn(LocalDateTime.now()
        .toInstant(Clock.systemUTC().getZone().getRules().getOffset(LocalDateTime.now())));
    when(clock.getZone()).thenReturn(Clock.systemUTC().getZone());
    when(airwayMapper.selectByPrimaryKey(anyString())).thenReturn(new Airway());

    dronePortsMappingLogic.dronePortsMappingPost(request);

    // 削除処理が正しく呼ばれたことを確認
    verify(mappingDroneportSectionMapper).deleteByAirwaySectionId(anyList());
    // 挿入処理が呼ばれていないことを確認
    verify(mappingDroneportSectionMapper, never()).insert(any());
    // MQTT更新通知処理が呼ばれたことを確認
    verify(airwayDesignLogic).notifyAirwayUpdated(anyString(), any(), any(),
        eq(AirwayDesignLogic.AIRWAY_STATUS_UPDATED));
  }

  @Test
  @DisplayName("存在チェック、整合性チェックにてNG")
  public void testDronePortsMappingPost_dataNotFound() {
    DronePortsMappingPostRequestDto request = createRequestWithOneSection();
    // 航路ID、航路区画IDにて航路区画テーブルを検索し、1件返ってくる想定が0件で返ってきたことを想定
    when(mappingDroneportSectionMapper.countAirwaySectionIds(anyList(), anyString())).thenReturn(0);
    // DataNotFoundExceptionが発生していること
    assertThrows(DataNotFoundException.class, () -> {
      dronePortsMappingLogic.dronePortsMappingPost(request);
    });
  }

  private DronePortsMappingPostRequestDto createRequestWithOneSection() {
    // ドローンポートマッピング情報が1つのリクエスト
    DronePortsMappingPostRequestDto request = new DronePortsMappingPostRequestDto();
    request.setAirwayId("airway-TestId");
    DronePortsMappingPostRequestDtoAirwaySectionsInner section =
        new DronePortsMappingPostRequestDtoAirwaySectionsInner();
    section.setAirwaySectionId("section-TestId");
    section.setDroneportIds(Arrays.asList("dronePortTestId1"));
    request.setAirwaySections(Collections.singletonList(section));
    return request;
  }

  private DronePortsMappingPostRequestDto createRequestWithMultipleSections() {
    // ドローンポートマッピング情報が2つのリクエスト
    DronePortsMappingPostRequestDto request = new DronePortsMappingPostRequestDto();
    request.setAirwayId("airway-TestId");

    DronePortsMappingPostRequestDtoAirwaySectionsInner section1 =
        new DronePortsMappingPostRequestDtoAirwaySectionsInner();
    section1.setAirwaySectionId("section-TestId1");
    section1.setDroneportIds(Arrays.asList("dronePortTestId1", "dronePortTestId2"));

    DronePortsMappingPostRequestDtoAirwaySectionsInner section2 =
        new DronePortsMappingPostRequestDtoAirwaySectionsInner();
    section2.setAirwaySectionId("section-TestId2");
    section2.setDroneportIds(Arrays.asList("dronePortTestId3"));

    request.setAirwaySections(Arrays.asList(section1, section2));
    return request;
  }

  private DronePortsMappingPostRequestDto createRequestWithZeroDronePortId() {
    // リクエスト情報生成にてドローンポートIDを空にする
    DronePortsMappingPostRequestDto request = new DronePortsMappingPostRequestDto();
    request.setAirwayId("airway-TestId");
    DronePortsMappingPostRequestDtoAirwaySectionsInner section =
        new DronePortsMappingPostRequestDtoAirwaySectionsInner();
    section.setAirwaySectionId("section-TestId");
    section.setDroneportIds(Collections.emptyList());
    request.setAirwaySections(Collections.singletonList(section));
    return request;
  }

  private DronePortsMappingPostRequestDto createRequestWithNullDronePortId() {
    // リクエスト情報生成にてドローンポートIDをnullにする
    DronePortsMappingPostRequestDto request = new DronePortsMappingPostRequestDto();
    request.setAirwayId("airway-TestId");
    DronePortsMappingPostRequestDtoAirwaySectionsInner section =
        new DronePortsMappingPostRequestDtoAirwaySectionsInner();
    section.setAirwaySectionId("section-TestId");
    section.setDroneportIds(null);
    request.setAirwaySections(Collections.singletonList(section));
    return request;
  }
}
