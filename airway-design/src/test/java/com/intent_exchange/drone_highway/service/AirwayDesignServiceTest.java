package com.intent_exchange.drone_highway.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.web.servlet.NoHandlerFoundException;
import com.intent_exchange.drone_highway.dto.request.AirwayDeleteRequestDto;
import com.intent_exchange.drone_highway.entity.AirwayDeleteRequestEntity;
import com.intent_exchange.drone_highway.entity.AirwayEntity;
import com.intent_exchange.drone_highway.entity.AirwayEntityAirway;
import com.intent_exchange.drone_highway.entity.AirwayJunctionsEntity;
import com.intent_exchange.drone_highway.entity.AirwayJunctionsEntityAirwaysInner;
import com.intent_exchange.drone_highway.entity.AirwayJunctionsEntityAirwaysInnerAirway;
import com.intent_exchange.drone_highway.entity.AirwayJunctionsEntityAirwaysInnerAirwayGeometry;
import com.intent_exchange.drone_highway.entity.AirwayJunctionsEntityAirwaysInnerDeviation;
import com.intent_exchange.drone_highway.entity.AirwaySectionsEntity;
import com.intent_exchange.drone_highway.entity.AirwaysEntity;
import com.intent_exchange.drone_highway.entity.AirwaysGSWPostRequestAirwayPartsEntity;
import com.intent_exchange.drone_highway.entity.AirwaysGSWPostRequestEntity;
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestAirwayJunctionEntity;
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestAirwayJunctionEntityDeviationGeometry;
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestAirwayJunctionEntityGeometry;
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestAirwayPartsEntity;
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestAirwaySectionEntity;
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestDespersionNodeEntity;
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestDespersionNodeEntityGeometry;
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestDespersionNodeWithFallSpaceEntity;
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestDespersionNodeWithFallSpaceEntityGeometry;
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestEntity;
import com.intent_exchange.drone_highway.entity.AirwaysPostResponseEntity;
import com.intent_exchange.drone_highway.entity.FallSpacePostRequestDroneEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangePostRequestEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangePostRequestEntityGeometry;
import com.intent_exchange.drone_highway.logic.AirwayDesignLogic;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AirwayDesignServiceTest {

  @Mock
  private AirwayDesignLogic airwayDesignLogic;

  @InjectMocks
  private AirwayDesignService airwayDesignService;

  private AirwayEntity testEntity;

  @BeforeEach
  public void setUp() {
    testEntity = createAirwayEntityWithAirways();
  }


  @Test
  @DisplayName("全件取得が正常に動作すること")
  public void testAirwayGetAll() throws NoHandlerFoundException {
    // モック設定：全件取得時にすべてのエンティティをリストとして返す
    when(airwayDesignLogic.airwayGet(any(), eq(true), any(), any(), any(), any(), any()))
        .thenReturn(testEntity);

    // テスト実行: 全件取得フラグをtrueにして呼び出す
    AirwayEntity result = airwayDesignService.airwayGet(null, true, null, null, null, null, null);

    // 結果確認
    assertNotNull(result);
    assertNotNull(result.getAirway());
    assertEquals(3, result.getAirway().getAirways().size(), "全件取得テストでは3件の航路情報が含まれていること");
  }

  @Test
  @DisplayName("特定の航路IDの取得が正常に動作すること")
  public void testAirwayGetById() throws NoHandlerFoundException {
    // フィルタリング: 特定のIDを持つエンティティを選択
    String targetId = "2";
    AirwayEntity filteredEntity = new AirwayEntity();
    AirwayEntityAirway filteredAirway = new AirwayEntityAirway()
        .airwayAdministratorId(testEntity.getAirway().getAirwayAdministratorId())
        .businessNumber(testEntity.getAirway().getBusinessNumber())
        .airways(testEntity.getAirway()
            .getAirways()
            .stream()
            .filter(a -> a.getAirwayId().equals(targetId))
            .collect(Collectors.toList()));
    filteredEntity.setAirway(filteredAirway);

    // モック設定: 特定IDにフィルタリングされたエンティティを返すよう設定
    when(airwayDesignLogic.airwayGet(anyList(), eq(false), any(), any(), any(), any(), any()))
        .thenReturn(filteredEntity);

    // テスト実行: 特定IDで呼び出す
    AirwayEntity result =
        airwayDesignService.airwayGet(Arrays.asList(targetId), false, null, null, null, null, null);

    // 結果確認
    assertNotNull(result);
    assertNotNull(result.getAirway());
    assertEquals(1, result.getAirway().getAirways().size(), "特定のID用のテスト：1件の航路情報を含まれていること");
    assertEquals(targetId, result.getAirway().getAirways().get(0).getAirwayId(), "特定の航路IDが一致すること");
  }

  @Test
  @DisplayName("オプション検索が正常に動作すること")
  public void testAirwayGetWithOptionalSearch() throws NoHandlerFoundException {
    // createAirwayEntityWithAirwaysを呼び出してすべてのデータを取得
    AirwayEntity fullEntity = createAirwayEntityWithAirways();

    // オプション検索用のフィルタリング処理を行う
    AirwayEntity filteredEntity = new AirwayEntity();
    AirwayEntityAirway filteredAirway = new AirwayEntityAirway()
        .airwayAdministratorId(fullEntity.getAirway().getAirwayAdministratorId())
        .businessNumber(fullEntity.getAirway().getBusinessNumber())
        .airways(fullEntity.getAirway()
            .getAirways()
            .stream()
            .filter(a -> Arrays.asList("物資運搬", "送電線点検").contains(a.getFlightPurpose()))
            .collect(Collectors.toList()));

    filteredEntity.setAirway(filteredAirway);

    // モック設定：フィルタされたデータを返すように設定
    when(airwayDesignLogic.airwayGet(any(), eq(false), any(), any(), any(), any(), any()))
        .thenReturn(filteredEntity);

    // テスト実行
    AirwayEntity result = airwayDesignService.airwayGet(null, false, List.of("物資運搬", "送電線点検"),
        "2023-01-01T00:00:00Z", "2023-12-31T23:59:59Z", "BN-123", "Area A");

    // 結果確認
    assertNotNull(result);
    assertNotNull(result.getAirway());
    List<String> resultIds = result.getAirway()
        .getAirways()
        .stream()
        .map(AirwaysEntity::getAirwayId)
        .collect(Collectors.toList());
    assertTrue(resultIds.contains("1"), "オプション検索テスト: ID 1 が含まれていること");
    assertTrue(resultIds.contains("2"), "オプション検索テスト: ID 2 が含まれていること");
    assertFalse(resultIds.contains("3"), "オプション検索テスト: ID 3 は含まれていないこと");
  }

  @Test
  @DisplayName("航路登録のPOSTリクエストが正常に処理されること")
  public void testAirwayPost() {
    AirwaysPostRequestEntity requestEntity = createAirwayPostRequest();

    AirwaysPostResponseEntity responseEntity = new AirwaysPostResponseEntity();
    responseEntity.setAirwayId("registeredAirwayId");

    when(airwayDesignLogic.airwayPost(requestEntity)).thenReturn(responseEntity);

    AirwaysPostResponseEntity result = airwayDesignService.airwayPost(requestEntity);

    assertEquals(responseEntity, result);
    verify(airwayDesignLogic, times(1)).airwayPost(requestEntity);
  }

  @Test
  @DisplayName("航路登録(GSW)のPOSTリクエストが正常に処理されること")
  public void testAirwayPostGSW() {
    AirwaysGSWPostRequestEntity requestEntity = createAirwayPostGSWRequest();

    AirwaysPostResponseEntity responseEntity = new AirwaysPostResponseEntity();
    responseEntity.setAirwayId("registeredAirwayId(GSW)");

    when(airwayDesignLogic.airwayPostGSW(requestEntity)).thenReturn(responseEntity);

    AirwaysPostResponseEntity result = airwayDesignService.airwayPostGSW(requestEntity);

    assertEquals(responseEntity, result);
    verify(airwayDesignLogic, times(1)).airwayPostGSW(requestEntity);
  }

  @Test
  @DisplayName("航路削除のリクエストが正常に処理されること")
  public void testAirwayDelete() throws NoSuchFieldException, SecurityException,
      IllegalArgumentException, IllegalAccessException {

    Field modelMapperField = ModelMapperUtil.class.getDeclaredField("modelMapper");
    modelMapperField.setAccessible(true);
    modelMapperField.set(null, new ModelMapper());
    AirwayDeleteRequestEntity deleteRequestEntity = createAirwayDeleteRequestEntity();

    airwayDesignService.airwayDelete(deleteRequestEntity);

    ArgumentCaptor<AirwayDeleteRequestDto> captor =
        ArgumentCaptor.forClass(AirwayDeleteRequestDto.class);
    verify(airwayDesignLogic, times(1)).delete(captor.capture());

    AirwayDeleteRequestDto capturedDto = captor.getValue();
    assertNotNull(capturedDto);
    assertEquals(deleteRequestEntity.getAirwayId(), capturedDto.getAirwayId());
  }

  private AirwayEntity createAirwayEntityWithAirways() {
    AirwaysEntity airwaysEntity1 = createAirwaysEntity("1", "UnitTest-Airway-1");
    AirwaysEntity airwaysEntity2 = createAirwaysEntity("2", "UnitTest-Airway-2");
    AirwaysEntity airwaysEntity3 = createAirwaysEntity("3", "UnitTest-Airway-3");

    airwaysEntity2.setFlightPurpose("送電線点検");
    airwaysEntity3.setFlightPurpose("河川監視");

    AirwayEntityAirway airwayEntityAirway =
        new AirwayEntityAirway().airwayAdministratorId("AdminID")
            .businessNumber("businessNumber")
            .airways(Arrays.asList(airwaysEntity1, airwaysEntity2, airwaysEntity3));

    return new AirwayEntity().airway(airwayEntityAirway);
  }

  private AirwaysEntity createAirwaysEntity(String airwayId, String airwayName) {
    return new AirwaysEntity().airwayId(airwayId)
        .airwayName(airwayName)
        .flightPurpose("物資運搬")
        .createdAt(new Date())
        .updatedAt(new Date())
        .airwayJunctions(Arrays.asList(createAirwayJunctionsEntity()))
        .airwaySections(Arrays.asList(createAirwaySectionsEntity()));
  }

  private static AirwayJunctionsEntity createAirwayJunctionsEntity() {
    AirwayJunctionsEntityAirwaysInner airwaysInner = new AirwayJunctionsEntityAirwaysInner()
        .airway(createAirwayJunctionsEntityAirwaysInnerAirway())
        .deviation(createAirwayJunctionsEntityAirwaysInnerDeviation());

    return new AirwayJunctionsEntity().airwayJunctionId("1")
        .airwayJunctionName("福岡タワー")
        .airways(Arrays.asList(airwaysInner));
  }

  private static AirwayJunctionsEntityAirwaysInnerAirway createAirwayJunctionsEntityAirwaysInnerAirway() {
    return new AirwayJunctionsEntityAirwaysInnerAirway()
        .geometry(createAirwayJunctionsEntityAirwaysInnerAirwayGeometry());
  }

  private static AirwayJunctionsEntityAirwaysInnerDeviation createAirwayJunctionsEntityAirwaysInnerDeviation() {
    return new AirwayJunctionsEntityAirwaysInnerDeviation()
        .geometry(createAirwayJunctionsEntityAirwaysInnerAirwayGeometry());
  }

  private static AirwayJunctionsEntityAirwaysInnerAirwayGeometry createAirwayJunctionsEntityAirwaysInnerAirwayGeometry() {
    List<BigDecimal> coordinates1 = Arrays.asList(new BigDecimal("130.34985374430653"),
        new BigDecimal("33.593513745109334"), new BigDecimal("130.00"));
    List<BigDecimal> coordinates2 = Arrays.asList(new BigDecimal("130.34985374430653"),
        new BigDecimal("33.59207488455793"), new BigDecimal("120.00"));

    return new AirwayJunctionsEntityAirwaysInnerAirwayGeometry().type("Polygon")
        .coordinates(Arrays.asList(coordinates1, coordinates2));
  }

  private static AirwaySectionsEntity createAirwaySectionsEntity() {
    return new AirwaySectionsEntity().airwaySectionId("1")
        .airwaySectionName("A-1")
        .airwayJunctionIds(Arrays.asList("1", "2"));
  }

  private AirwaysPostRequestEntity createAirwayPostRequest() {
    AirwaysPostRequestEntity requestEntity = new AirwaysPostRequestEntity();
    requestEntity.setAirwayDeterminationId(1);
    requestEntity.setAirwayName("航路A");
    requestEntity.setFlightPurpose("空撮");

    // 1つ目の航路パーツを生成
    AirwaysPostRequestAirwayPartsEntity part1 = new AirwaysPostRequestAirwayPartsEntity();
    part1.setPrevAirwayPartsIndex(null);

    AirwaysPostRequestDespersionNodeEntity despersionNode1 =
        new AirwaysPostRequestDespersionNodeEntity();
    despersionNode1.setName("despersionNodeA");
    AirwaysPostRequestDespersionNodeEntityGeometry geometry1 =
        new AirwaysPostRequestDespersionNodeEntityGeometry();
    geometry1.setType("LineString");
    geometry1.setCoordinates(Arrays.asList(
        Arrays.asList(BigDecimal.valueOf(130.41554149543992), BigDecimal.valueOf(33.4058179898002)),
        Arrays.asList(BigDecimal.valueOf(130.41981239018116),
            BigDecimal.valueOf(33.410868696816124))));
    despersionNode1.setGeometry(geometry1);
    despersionNode1.setFallSpaceCrossSectionId(1);
    part1.setDespersionNode(despersionNode1);

    AirwaysPostRequestAirwaySectionEntity section1 = new AirwaysPostRequestAirwaySectionEntity();
    section1.setName("airwaySection1");
    part1.setAirwaySection(section1);

    // 1つ目のジャンクション生成
    AirwaysPostRequestAirwayJunctionEntity junction1 = new AirwaysPostRequestAirwayJunctionEntity();
    junction1.setName("airwayJunction1");
    AirwaysPostRequestAirwayJunctionEntityGeometry junction1Geometry =
        new AirwaysPostRequestAirwayJunctionEntityGeometry();
    junction1Geometry.setType("Polygon");
    junction1Geometry.setCoordinates(Arrays.asList(Arrays.asList(
        Arrays.asList(BigDecimal.valueOf(130.41642438682777),
            BigDecimal.valueOf(33.406866436466146), BigDecimal.valueOf(100)),
        Arrays.asList(BigDecimal.valueOf(130.41942907079016),
            BigDecimal.valueOf(33.410420202208456), BigDecimal.valueOf(100)),
        Arrays.asList(BigDecimal.valueOf(130.41942907079016),
            BigDecimal.valueOf(33.410420202208456), BigDecimal.valueOf(50)),
        Arrays.asList(BigDecimal.valueOf(130.41642438682777),
            BigDecimal.valueOf(33.406866436466146), BigDecimal.valueOf(50)),
        Arrays.asList(BigDecimal.valueOf(130.41642438682777),
            BigDecimal.valueOf(33.406866436466146), BigDecimal.valueOf(100)))));
    junction1.setGeometry(junction1Geometry);

    AirwaysPostRequestAirwayJunctionEntityDeviationGeometry deviationGeometry1 =
        new AirwaysPostRequestAirwayJunctionEntityDeviationGeometry();
    deviationGeometry1.setType("Polygon");
    deviationGeometry1.setCoordinates(Arrays.asList(Arrays.asList(
        Arrays.asList(BigDecimal.valueOf(130.41526276513633), BigDecimal.valueOf(33.40542597628402),
            BigDecimal.valueOf(200)),
        Arrays.asList(BigDecimal.valueOf(130.4200479662618), BigDecimal.valueOf(33.411202108008766),
            BigDecimal.valueOf(200)),
        Arrays.asList(BigDecimal.valueOf(130.4200479662618), BigDecimal.valueOf(33.411202108008766),
            BigDecimal.valueOf(25)),
        Arrays.asList(BigDecimal.valueOf(130.41526276513633), BigDecimal.valueOf(33.40542597628402),
            BigDecimal.valueOf(25)),
        Arrays.asList(BigDecimal.valueOf(130.41526276513633), BigDecimal.valueOf(33.40542597628402),
            BigDecimal.valueOf(200)))));
    junction1.setDeviationGeometry(deviationGeometry1);

    part1.setAirwayJunction(Arrays.asList(junction1));

    // 2つ目の航路パーツを作成
    AirwaysPostRequestAirwayPartsEntity part2 = new AirwaysPostRequestAirwayPartsEntity();
    part2.setPrevAirwayPartsIndex(0);

    AirwaysPostRequestDespersionNodeEntity despersionNode2 =
        new AirwaysPostRequestDespersionNodeEntity();
    despersionNode2.setName("despersionNodeB");
    AirwaysPostRequestDespersionNodeEntityGeometry geometry2 =
        new AirwaysPostRequestDespersionNodeEntityGeometry();
    geometry2.setType("LineString");
    geometry2.setCoordinates(Arrays.asList(
        Arrays.asList(BigDecimal.valueOf(130.40111353692225),
            BigDecimal.valueOf(33.41539351521946)),
        Arrays.asList(BigDecimal.valueOf(130.40538443166355),
            BigDecimal.valueOf(33.419803843639926))));
    despersionNode2.setGeometry(geometry2);
    despersionNode2.setFallSpaceCrossSectionId(2);
    part2.setDespersionNode(despersionNode2);

    // 2つ目のジャンクション生成
    AirwaysPostRequestAirwayJunctionEntity junction2 = new AirwaysPostRequestAirwayJunctionEntity();
    junction2.setName("airwayJunction2");
    AirwaysPostRequestAirwayJunctionEntityGeometry junction2Geometry =
        new AirwaysPostRequestAirwayJunctionEntityGeometry();
    junction2Geometry.setType("Polygon");
    junction2Geometry.setCoordinates(Arrays.asList(Arrays.asList(
        Arrays.asList(BigDecimal.valueOf(130.40310035501545),
            BigDecimal.valueOf(33.417446593576216), BigDecimal.valueOf(100)),
        Arrays.asList(BigDecimal.valueOf(130.4042909895261), BigDecimal.valueOf(33.418674770513505),
            BigDecimal.valueOf(100)),
        Arrays.asList(BigDecimal.valueOf(130.4042909895261), BigDecimal.valueOf(33.418674770513505),
            BigDecimal.valueOf(50)),
        Arrays.asList(BigDecimal.valueOf(130.40310035501545),
            BigDecimal.valueOf(33.417446593576216), BigDecimal.valueOf(50)),
        Arrays.asList(BigDecimal.valueOf(130.40310035501545),
            BigDecimal.valueOf(33.417446593576216), BigDecimal.valueOf(100)))));
    junction2.setGeometry(junction2Geometry);

    AirwaysPostRequestAirwayJunctionEntityDeviationGeometry deviationGeometry2 =
        new AirwaysPostRequestAirwayJunctionEntityDeviationGeometry();
    deviationGeometry2.setType("Polygon");
    deviationGeometry2.setCoordinates(Arrays.asList(Arrays.asList(
        Arrays.asList(BigDecimal.valueOf(130.40163502283042),
            BigDecimal.valueOf(33.415916470269025), BigDecimal.valueOf(200)),
        Arrays.asList(BigDecimal.valueOf(130.40491186857008), BigDecimal.valueOf(33.41931003641862),
            BigDecimal.valueOf(200)),
        Arrays.asList(BigDecimal.valueOf(130.40491186857008), BigDecimal.valueOf(33.41931003641862),
            BigDecimal.valueOf(25)),
        Arrays.asList(BigDecimal.valueOf(130.40163502283042),
            BigDecimal.valueOf(33.415916470269025), BigDecimal.valueOf(25)),
        Arrays.asList(BigDecimal.valueOf(130.40163502283042),
            BigDecimal.valueOf(33.415916470269025), BigDecimal.valueOf(200)))));
    junction2.setDeviationGeometry(deviationGeometry2);

    part2.setAirwayJunction(Arrays.asList(junction2));

    // 1つ目と2つ目のパーツを合わせて、リクエスト情報生成
    requestEntity.setAirwayParts(Arrays.asList(part1, part2));
    return requestEntity;
  }

  private AirwaysGSWPostRequestEntity createAirwayPostGSWRequest() {
    AirwaysGSWPostRequestEntity requestEntity = new AirwaysGSWPostRequestEntity();

    // AirwaysGSWPostRequestEntityにデータをセット
    requestEntity.setAirwayName("航路A");
    requestEntity.setFlightPurpose("空撮");

    // FallToleranceRangeの設定
    FallToleranceRangePostRequestEntity fallToleranceRange =
        new FallToleranceRangePostRequestEntity();
    fallToleranceRange.setBusinessNumber("12345");
    fallToleranceRange.setAirwayOperatorId("67890");
    fallToleranceRange.setName("範囲A");
    fallToleranceRange.setAreaName("地域B");
    fallToleranceRange.setElevationTerrain("TerrainXYZ");

    FallToleranceRangePostRequestEntityGeometry geometryRange =
        new FallToleranceRangePostRequestEntityGeometry();
    geometryRange.setType("Polygon");
    geometryRange
        .setCoordinates(List.of(List.of(List.of(new BigDecimal("130.0"), new BigDecimal("30.0")),
            List.of(new BigDecimal("131.0"), new BigDecimal("31.0")),
            List.of(new BigDecimal("132.0"), new BigDecimal("32.0")))));

    fallToleranceRange.setGeometry(geometryRange);
    requestEntity.setFallToleranceRange(fallToleranceRange);

    // DroneListの仮設定
    FallSpacePostRequestDroneEntity drone1 = new FallSpacePostRequestDroneEntity();
    drone1.setAircraftInfoId(101);
    drone1.setMaker("MakerA");
    drone1.setModelNumber("Model 1");
    drone1.setName("Drone A");
    drone1.setType("Quadcopter");
    drone1.setIp("192.168.1.1");
    drone1.setLength(350);
    drone1.setWeight(1380);
    drone1.setMaximumTakeoffWeight(1500);
    drone1.setMaximumFlightTime(30);

    FallSpacePostRequestDroneEntity drone2 = new FallSpacePostRequestDroneEntity();
    drone2.setAircraftInfoId(102);
    drone2.setMaker("MakerB");
    drone2.setModelNumber("Model 2");
    drone2.setName("Drone B");
    drone2.setType("Quadcopter");
    drone2.setIp("192.168.1.2");
    drone2.setLength(240);
    drone2.setWeight(320);
    drone2.setMaximumTakeoffWeight(500);
    drone2.setMaximumFlightTime(25);

    List<FallSpacePostRequestDroneEntity> droneList = new ArrayList<>();
    droneList.add(drone1);
    droneList.add(drone2);

    requestEntity.setDroneList(droneList);

    // 最初のAirwayPartsを作成
    AirwaysGSWPostRequestAirwayPartsEntity part1 = new AirwaysGSWPostRequestAirwayPartsEntity();
    part1.setPrevAirwayPartsIndex(null);

    // DespersionNodeの設定
    AirwaysPostRequestDespersionNodeWithFallSpaceEntity despersionNodeA =
        new AirwaysPostRequestDespersionNodeWithFallSpaceEntity();
    despersionNodeA.setName("despersionNodeA");

    AirwaysPostRequestDespersionNodeWithFallSpaceEntityGeometry geometryA =
        new AirwaysPostRequestDespersionNodeWithFallSpaceEntityGeometry();
    geometryA.setType("LineString");
    geometryA.setCoordinates(
        List.of(List.of(new BigDecimal("130.41554149543992"), new BigDecimal("33.4058179898002")),
            List.of(new BigDecimal("130.41981239018116"), new BigDecimal("33.410868696816124"))));
    despersionNodeA.setGeometry(geometryA);
    part1.setDespersionNode(despersionNodeA);

    // AirwayJunctionの設定
    List<AirwaysPostRequestAirwayJunctionEntity> airwayJunctions = new ArrayList<>();
    AirwaysPostRequestAirwayJunctionEntity airwayJunction1 =
        new AirwaysPostRequestAirwayJunctionEntity();
    airwayJunction1.setName("airwayJunction1");

    AirwaysPostRequestAirwayJunctionEntityGeometry junctionGeometry =
        new AirwaysPostRequestAirwayJunctionEntityGeometry();
    junctionGeometry.setType("Polygon");
    junctionGeometry.setCoordinates(List.of(List.of(
        List.of(new BigDecimal("130.41642438682777"), new BigDecimal("33.406866436466146"),
            new BigDecimal("100")),
        List.of(new BigDecimal("130.41942907079016"), new BigDecimal("33.410420202208456"),
            new BigDecimal("100")),
        List.of(new BigDecimal("130.41942907079016"), new BigDecimal("33.410420202208456"),
            new BigDecimal("50")),
        List.of(new BigDecimal("130.41642438682777"), new BigDecimal("33.406866436466146"),
            new BigDecimal("50")),
        List.of(new BigDecimal("130.41642438682777"), new BigDecimal("33.406866436466146"),
            new BigDecimal("100")))));
    airwayJunction1.setGeometry(junctionGeometry);

    AirwaysPostRequestAirwayJunctionEntityDeviationGeometry deviationGeometry =
        new AirwaysPostRequestAirwayJunctionEntityDeviationGeometry();
    deviationGeometry.setType("Polygon");
    deviationGeometry.setCoordinates(List.of(List.of(
        List.of(new BigDecimal("130.41526276513633"), new BigDecimal("33.40542597628402"),
            new BigDecimal("200")),
        List.of(new BigDecimal("130.4200479662618"), new BigDecimal("33.411202108008766"),
            new BigDecimal("200")),
        List.of(new BigDecimal("130.4200479662618"), new BigDecimal("33.411202108008766"),
            new BigDecimal("25")),
        List.of(new BigDecimal("130.41526276513633"), new BigDecimal("33.40542597628402"),
            new BigDecimal("25")),
        List.of(new BigDecimal("130.41526276513633"), new BigDecimal("33.40542597628402"),
            new BigDecimal("200")))));
    airwayJunction1.setDeviationGeometry(deviationGeometry);

    airwayJunctions.add(airwayJunction1);
    part1.setAirwayJunction(airwayJunctions);

    // AirwaySectionの設定
    AirwaysPostRequestAirwaySectionEntity airwaySection1 =
        new AirwaysPostRequestAirwaySectionEntity();
    airwaySection1.setName("airwaySection1");
    part1.setAirwaySection(airwaySection1);

    // 次のAirwayPartsを作成（2番目）
    AirwaysGSWPostRequestAirwayPartsEntity part2 = new AirwaysGSWPostRequestAirwayPartsEntity();
    part2.setPrevAirwayPartsIndex(0);

    AirwaysPostRequestDespersionNodeWithFallSpaceEntity despersionNodeB =
        new AirwaysPostRequestDespersionNodeWithFallSpaceEntity();
    despersionNodeB.setName("despersionNodeB");

    AirwaysPostRequestDespersionNodeWithFallSpaceEntityGeometry geometryB =
        new AirwaysPostRequestDespersionNodeWithFallSpaceEntityGeometry();
    geometryB.setType("LineString");
    geometryB.setCoordinates(
        List.of(List.of(new BigDecimal("130.40111353692225"), new BigDecimal("33.41539351521946")),
            List.of(new BigDecimal("130.40538443166355"), new BigDecimal("33.419803843639926"))));
    despersionNodeB.setGeometry(geometryB);
    part2.setDespersionNode(despersionNodeB);

    // AirwayJunctionの設定（2番目）
    List<AirwaysPostRequestAirwayJunctionEntity> secondAirwayJunctions = new ArrayList<>();
    AirwaysPostRequestAirwayJunctionEntity airwayJunction2 =
        new AirwaysPostRequestAirwayJunctionEntity();
    airwayJunction2.setName("airwayJunction2");

    AirwaysPostRequestAirwayJunctionEntityGeometry junctionGeometry2 =
        new AirwaysPostRequestAirwayJunctionEntityGeometry();
    junctionGeometry2.setType("Polygon");
    junctionGeometry2.setCoordinates(List.of(List.of(
        List.of(new BigDecimal("130.40310035501545"), new BigDecimal("33.417446593576216"),
            new BigDecimal("100")),
        List.of(new BigDecimal("130.4042909895261"), new BigDecimal("33.418674770513505"),
            new BigDecimal("100")),
        List.of(new BigDecimal("130.4042909895261"), new BigDecimal("33.418674770513505"),
            new BigDecimal("50")),
        List.of(new BigDecimal("130.40310035501545"), new BigDecimal("33.417446593576216"),
            new BigDecimal("50")),
        List.of(new BigDecimal("130.40310035501545"), new BigDecimal("33.417446593576216"),
            new BigDecimal("100")))));
    airwayJunction2.setGeometry(junctionGeometry2);

    AirwaysPostRequestAirwayJunctionEntityDeviationGeometry deviationGeometry2 =
        new AirwaysPostRequestAirwayJunctionEntityDeviationGeometry();
    deviationGeometry2.setType("Polygon");
    deviationGeometry2.setCoordinates(List.of(List.of(
        List.of(new BigDecimal("130.40163502283042"), new BigDecimal("33.415916470269025"),
            new BigDecimal("200")),
        List.of(new BigDecimal("130.40491186857008"), new BigDecimal("33.41931003641862"),
            new BigDecimal("200")),
        List.of(new BigDecimal("130.40491186857008"), new BigDecimal("33.41931003641862"),
            new BigDecimal("25")),
        List.of(new BigDecimal("130.40163502283042"), new BigDecimal("33.415916470269025"),
            new BigDecimal("25")),
        List.of(new BigDecimal("130.40163502283042"), new BigDecimal("33.415916470269025"),
            new BigDecimal("200")))));
    airwayJunction2.setDeviationGeometry(deviationGeometry2);

    secondAirwayJunctions.add(airwayJunction2);
    part2.setAirwayJunction(secondAirwayJunctions);

    // 2番目の部品のAirwaySectionはnull
    part2.setAirwaySection(null);

    // 1つ目と2つ目のパーツを合わせて、リクエスト情報生成
    requestEntity.setAirwayParts(Arrays.asList(part1, part2));
    return requestEntity;
  }

  private AirwayDeleteRequestEntity createAirwayDeleteRequestEntity() {
    AirwayDeleteRequestEntity entity = new AirwayDeleteRequestEntity();
    entity.setAirwayId("airwayId");
    return entity;
  }
}
