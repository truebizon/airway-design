package com.intent_exchange.drone_highway.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intent_exchange.drone_highway.entity.AirwayConditions;
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
import com.intent_exchange.drone_highway.exception.DataNotFoundException;
import com.intent_exchange.drone_highway.exception.DroneHighwayException;
import com.intent_exchange.drone_highway.service.AirwayDesignService;

@WebMvcTest(controllers = AirwayDesignApiController.class)
public class AirwayDesignApiControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AirwayDesignService airwayDesignService;

  @Autowired
  private ObjectMapper objectMapper;

  private AirwayEntity testEntity;


  @BeforeEach
  public void setUp() {
    testEntity = createAirwayEntityWithAirways();
  }

  @Test
  @DisplayName("全件取得フラグがtrueの場合、航路情報取得が成功すること")
  public void testAirwayGetAllTrue() throws Exception {
    given(airwayDesignService.airwayGet(any(), eq(true), any(), anyString(), anyString(),
        anyString(), anyString())).willReturn(testEntity);

    mockMvc
        .perform(get("/airway").param("all", "true")
            .param("flightPurpose", "")
            .param("determinationDateFrom", "")
            .param("determinationDateTo", "")
            .param("businessNumber", "")
            .param("areaName", "")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.airway.airways").isArray())
        .andExpect(jsonPath("$.airway.airways.length()").value(3));
  }

  @Test
  @DisplayName("航路IDが指定されてある場合、対象の航路情報取得が成功すること")
  public void testAirwayGetById() throws Exception {
    AirwayEntity subEntity = new AirwayEntity();
    subEntity.setAirway(testEntity.getAirway()); // 全体から1つのサブセットを作成

    subEntity.getAirway().setAirways(testEntity.getAirway().getAirways().subList(0, 2));

    given(airwayDesignService.airwayGet(eq(Arrays.asList("1", "2")), eq(false), any(), anyString(),
        anyString(), anyString(), anyString())).willReturn(subEntity);

    mockMvc
        .perform(get("/airway").param("airwayId", "1", "2")
            .param("flightPurpose", "")
            .param("determinationDateFrom", "")
            .param("determinationDateTo", "")
            .param("businessNumber", "")
            .param("areaName", "")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.airway.airways").isArray())
        .andExpect(jsonPath("$.airway.airways.length()").value(2)); // 2件の確認
  }

  @Test
  @DisplayName("飛行目的が指定されてある場合、対象の航路情報取得が成功すること")
  public void testAirwayGetWithFlightPurpose() throws Exception {
    // "物資運搬" が設定されているエンティティだけを抽出するテストエンティティを作成
    AirwayEntity singlePurposeEntity = createAirwayEntityWithAirways();
    singlePurposeEntity.getAirway()
        .setAirways(singlePurposeEntity.getAirway()
            .getAirways()
            .stream()
            .filter(a -> "物資運搬".equals(a.getFlightPurpose()))
            .toList());

    given(airwayDesignService.airwayGet(any(), anyBoolean(), eq(Arrays.asList("物資運搬")), anyString(),
        anyString(), anyString(), anyString())).willReturn(singlePurposeEntity);

    mockMvc
        .perform(get("/airway").param("flightPurpose", "物資運搬")
            .param("determinationDateFrom", "")
            .param("determinationDateTo", "")
            .param("businessNumber", "")
            .param("areaName", "")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.airway.airways").isArray())
        .andExpect(jsonPath("$.airway.airways.length()").value(1));
  }

  @Test
  @DisplayName("飛行目的が複数指定されてある場合、対象の航路情報取得が成功すること")
  public void testAirwayGetWithFlightPurposes() throws Exception {
    // "送電線点検" と "河川監視" が設定されているエンティティだけを抽出
    AirwayEntity multiplePurposeEntity = createAirwayEntityWithAirways();
    multiplePurposeEntity.getAirway()
        .setAirways(multiplePurposeEntity.getAirway()
            .getAirways()
            .stream()
            .filter(a -> Arrays.asList("送電線点検", "河川監視").contains(a.getFlightPurpose()))
            .toList());

    given(airwayDesignService.airwayGet(any(), anyBoolean(), eq(Arrays.asList("送電線点検", "河川監視")),
        anyString(), anyString(), anyString(), anyString())).willReturn(multiplePurposeEntity);

    mockMvc
        .perform(get("/airway").param("flightPurpose", "送電線点検,河川監視")
            .param("determinationDateFrom", "")
            .param("determinationDateTo", "")
            .param("businessNumber", "")
            .param("areaName", "")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.airway.airways").isArray())
        .andExpect(jsonPath("$.airway.airways.length()").value(2));
  }

  @Test
  @DisplayName("オプション検索において複数のパラメータを指定した場合、対象の航路情報取得が成功すること")
  public void testAirwayGetWithOptionalParameter() throws Exception {
    // テストエンティティと条件に基づくレスポンスをモック
    given(airwayDesignService.airwayGet(any(), anyBoolean(), eq(Arrays.asList("物資運搬")),
        eq("2023-01-01T00:00:00Z"), eq("2023-12-31T23:59:59Z"), eq("BN-123"), eq("Area A")))
            .willReturn(testEntity);

    mockMvc
        .perform(get("/airway").param("flightPurpose", "物資運搬")
            .param("determinationDateFrom", "2023-01-01T00:00:00Z")
            .param("determinationDateTo", "2023-12-31T23:59:59Z")
            .param("businessNumber", "BN-123")
            .param("areaName", "Area A")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.airway.airways").isArray())
        .andExpect(jsonPath("$.airway.airways.length()").value(3));
  }

  @Test
  @DisplayName("全件取得フラグ、航路ID、およびオプション検索項目が全て指定されていない場合、MethodArgumentNotValidExceptionがスローされること")
  public void testAirwayGetWithoutAnyParametersThrowsException() throws Exception {
    // nullの場合
    mockMvc.perform(get("/airway").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    // 空の場合
    mockMvc.perform(get("/airway").param("all", "")
        .param("airwayId", "")
        .param("flightPurpose", "")
        .param("determinationDateFrom", "")
        .param("determinationDateTo", "")
        .param("businessNumber", "")
        .param("areaName", "")
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("全件取得フラグfalseかつ、航路IDが指定されてない場合、MethodArgumentNotValidExceptionがスローされること")
  public void testAirwayGetAllFlagFalseWithNoId() throws Exception {
    // nullの場合
    AirwayConditions conditions = new AirwayConditions();
    conditions.setAll(false);
    conditions.setAirwayId(null);

    mockMvc
        .perform(get("/airway").param("all", "false")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(conditions)))

        .andExpect(status().isBadRequest());

    // 空の場合
    conditions.setAirwayId(new ArrayList<>());

    mockMvc
        .perform(get("/airway").param("all", "false")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(conditions)))

        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("開始日のみ指定された場合は、検索結果が0件であること")
  public void testAirwayGetWithOnlyDeterminationDateFrom() throws Exception {
    AirwayEntity nullEntity = createEmptyAirwayEntity();
    given(airwayDesignService.airwayGet(any(), eq(false), eq(null), eq("2023-01-01T00:00:00Z"),
        eq(null), eq(null), eq(null))).willReturn(nullEntity);

    mockMvc
        .perform(get("/airway").param("determinationDateFrom", "2023-01-01T00:00:00Z")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.airway.airways").isArray())
        .andExpect(jsonPath("$.airway.airways.length()").value(0));
  }

  @Test
  @DisplayName("終了日のみ指定された場合は、検索結果が0件であること")
  public void testAirwayGetWithOnlyDeterminationDateTo() throws Exception {
    AirwayEntity nullEntity = createEmptyAirwayEntity();
    given(airwayDesignService.airwayGet(any(), eq(false), eq(null), eq(null),
        eq("2023-12-31T23:59:59Z"), eq(null), eq(null))).willReturn(nullEntity);

    mockMvc
        .perform(get("/airway").param("determinationDateTo", "2023-12-31T23:59:59Z")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.airway.airways").isArray())
        .andExpect(jsonPath("$.airway.airways.length()").value(0));
  }

  @Test
  @DisplayName("事業者番号のみ指定された場合は、検索結果が0件であること")
  public void testAirwayGetWithOnlyBusinessNumber() throws Exception {
    AirwayEntity nullEntity = createEmptyAirwayEntity();
    given(airwayDesignService.airwayGet(any(), eq(false), eq(null), eq(null), eq(null),
        eq("BN-123"), eq(null))).willReturn(nullEntity);

    mockMvc
        .perform(
            get("/airway").param("businessNumber", "BN-123").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.airway.airways").isArray())
        .andExpect(jsonPath("$.airway.airways.length()").value(0));
  }

  @Test
  @DisplayName("エリア名のみ指定された場合は、検索結果が0件であること")
  public void testAirwayGetWithOnlyAreaName() throws Exception {
    AirwayEntity nullEntity = createEmptyAirwayEntity();
    given(airwayDesignService.airwayGet(any(), eq(false), eq(null), eq(null), eq(null), eq(null),
        eq("Area A"))).willReturn(nullEntity);

    mockMvc.perform(get("/airway").param("areaName", "Area A").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.airway.airways").isArray())
        .andExpect(jsonPath("$.airway.airways.length()").value(0));
  }

  @Test
  @DisplayName("DB接続エラーが発生した場合、適切なエラーメッセージが返却されること")
  public void testAirwayGetWithDbConnectionError() throws Exception {
    given(airwayDesignService.airwayGet(anyList(), eq(true), anyList(), anyString(), anyString(),
        anyString(), anyString())).willThrow(new DroneHighwayException("DB接続エラー"));

    mockMvc
        .perform(get("/airway").param("all", "true")
            .param("flightPurpose", "")
            .param("determinationDateFrom", "")
            .param("determinationDateTo", "")
            .param("businessNumber", "")
            .param("areaName", "")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.description").value("DB接続エラー"));
  }

  @Test
  @DisplayName("通信エラーが発生した場合、適切なエラーメッセージが返却されること")
  public void testAirwayGetWithCommunicationError() throws Exception {
    given(airwayDesignService.airwayGet(anyList(), eq(true), anyList(), anyString(), anyString(),
        anyString(), anyString())).willThrow(new DroneHighwayException("通信エラー"));

    mockMvc
        .perform(get("/airway").param("all", "true")
            .param("flightPurpose", "")
            .param("determinationDateFrom", "")
            .param("determinationDateTo", "")
            .param("businessNumber", "")
            .param("areaName", "")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.description").value("通信エラー"));
  }

  @Test
  @DisplayName("予期しない例外が発生した場合、適切なエラーメッセージが返却されること")
  public void testAirwayGetWithUnexpectedException() throws Exception {
    given(airwayDesignService.airwayGet(anyList(), eq(true), anyList(), anyString(), anyString(),
        anyString(), anyString())).willThrow(new RuntimeException("予期しないエラー"));

    mockMvc
        .perform(get("/airway").param("all", "true")
            .param("flightPurpose", "")
            .param("determinationDateFrom", "")
            .param("determinationDateTo", "")
            .param("businessNumber", "")
            .param("areaName", "")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError()) // 500エラー
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.description").value("予期しないエラー"));
  }
  
  @Test
  @DisplayName("航路情報取得時にDataNotFoundExceptionが発生すると404エラーが返されること")
  public void testAirwayGetDataNotFoundException() throws Exception {

    // サービスのメソッド呼び出しでDataNotFoundExceptionをスローするように設定
    doThrow(new DataNotFoundException()).when(airwayDesignService)
        .airwayGet(anyList(), anyBoolean(), anyList(), anyString(), anyString(), anyString(),
            anyString());

    mockMvc.perform(get("/airway").param("all", "true")
        .param("flightPurpose", "")
        .param("determinationDateFrom", "")
        .param("determinationDateTo", "")
        .param("businessNumber", "")
        .param("areaName", "")
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("航路情報登録が成功すること")
  public void testAirwayPost() throws Exception {

    // リクエスト(登録する航路)の生成
    AirwaysPostRequestEntity requestEntity = createAirwayPostRequest();

    AirwaysPostResponseEntity responseEntity = new AirwaysPostResponseEntity();
    responseEntity.setAirwayId("registeredAirwayId");

    given(airwayDesignService.airwayPost(any(AirwaysPostRequestEntity.class)))
        .willReturn(responseEntity);

    mockMvc
        .perform(post("/airway").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestEntity)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    verify(airwayDesignService).airwayPost(any(AirwaysPostRequestEntity.class));
  }



  @Test
  @DisplayName("航路情報登録(GSW)が成功すること")
  public void testAirwayPostGSW() throws Exception {
    AirwaysGSWPostRequestEntity requestEntity = createAirwayPostGSWRequest();

    AirwaysPostResponseEntity responseEntity = new AirwaysPostResponseEntity();
    responseEntity.setAirwayId("registeredAirwayId(GSW)");

    given(airwayDesignService.airwayPostGSW(any(AirwaysGSWPostRequestEntity.class)))
        .willReturn(responseEntity);

    mockMvc
        .perform(post("/gsw-airway").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestEntity)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    verify(airwayDesignService).airwayPostGSW(any(AirwaysGSWPostRequestEntity.class));
  }



  @Test
  @DisplayName("航路削除が成功すること")
  public void testAirwayDelete() throws Exception {

    doNothing().when(airwayDesignService).airwayDelete(any(AirwayDeleteRequestEntity.class));

    mockMvc.perform(
        delete("/airway").param("airwayId", "deleteAirwayId").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    verify(airwayDesignService).airwayDelete(any(AirwayDeleteRequestEntity.class));
  }

  @Test
  @DisplayName("航路削除が存在しない場合、DataNotFoundExceptionにより404エラーを返すこと")
  public void testAirwayDeleteDataNotFoundException() throws Exception {

    // サービスのメソッド呼び出しでDataNotFoundExceptionをスローするように設定
    doThrow(new DataNotFoundException()).when(airwayDesignService)
        .airwayDelete(any(AirwayDeleteRequestEntity.class));

    mockMvc.perform(delete("/airway").param("airwayId", "nonExistentAirwayId")
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

    verify(airwayDesignService).airwayDelete(any(AirwayDeleteRequestEntity.class));
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

  private AirwayEntity createEmptyAirwayEntity() {
    AirwayEntityAirway airwayEntityAirway =
        new AirwayEntityAirway().airwayAdministratorId("AdminID")
            .businessNumber("businessNumber")
            .airways(Collections.emptyList());

    return new AirwayEntity().airway(airwayEntityAirway);
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
    part2.setAirwaySection(null);

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
}
