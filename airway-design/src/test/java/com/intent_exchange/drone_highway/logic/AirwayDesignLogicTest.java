package com.intent_exchange.drone_highway.logic;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.servlet.NoHandlerFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intent_exchange.drone_highway.dao.AirwayCompatibleModelsMapper;
import com.intent_exchange.drone_highway.dao.AirwayDeterminationMapper;
import com.intent_exchange.drone_highway.dao.AirwayJunctionMapper;
import com.intent_exchange.drone_highway.dao.AirwayMapper;
import com.intent_exchange.drone_highway.dao.AirwaySectionMapper;
import com.intent_exchange.drone_highway.dao.AirwaySequenceMapper;
import com.intent_exchange.drone_highway.dao.DespersionNodeMapper;
import com.intent_exchange.drone_highway.dao.FallSpaceMapper;
import com.intent_exchange.drone_highway.dao.FallToleranceRangeMapper;
import com.intent_exchange.drone_highway.dao.MappingDroneportSectionMapper;
import com.intent_exchange.drone_highway.dao.MappingJunctionSectionMapper;
import com.intent_exchange.drone_highway.dto.request.AirwayDeleteRequestDto;
import com.intent_exchange.drone_highway.dto.response.AirwayGeometry;
import com.intent_exchange.drone_highway.dto.response.FallSpacePostResponseDto;
import com.intent_exchange.drone_highway.dto.response.FallToleranceRangePostResponseDto;
import com.intent_exchange.drone_highway.entity.AirwayEntity;
import com.intent_exchange.drone_highway.entity.AirwayJunctionsEntity;
import com.intent_exchange.drone_highway.entity.AirwayJunctionsEntityAirwaysInner;
import com.intent_exchange.drone_highway.entity.AirwayJunctionsEntityAirwaysInnerAirwayGeometry;
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
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestDespersionNodeWithFallSpaceEntityFallSpace;
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestDespersionNodeWithFallSpaceEntityGeometry;
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestEntity;
import com.intent_exchange.drone_highway.entity.AirwaysPostResponseEntity;
import com.intent_exchange.drone_highway.entity.FallSpacePostRequestDroneEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangePostRequestEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangePostRequestEntityGeometry;
import com.intent_exchange.drone_highway.exception.DataNotFoundException;
import com.intent_exchange.drone_highway.exception.DroneHighwayException;
import com.intent_exchange.drone_highway.model.Airway;
import com.intent_exchange.drone_highway.model.AirwayCompatibleModels;
import com.intent_exchange.drone_highway.model.AirwayDetermination;
import com.intent_exchange.drone_highway.model.AirwayJunction;
import com.intent_exchange.drone_highway.model.AirwaySection;
import com.intent_exchange.drone_highway.model.MappingDroneportSection;
import com.intent_exchange.drone_highway.model.MappingJunctionSection;
import com.intent_exchange.drone_highway.service.AirwayDesignService;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AirwayDesignLogicTest {

  @Mock
  private AirwayMapper airwayMapper;
  @Mock
  private AirwaySectionMapper airwaySectionMapper;
  @Mock
  private AirwayJunctionMapper airwayJunctionMapper;
  @Mock
  private DespersionNodeMapper despersionNodeMapper;
  @Mock
  private MappingJunctionSectionMapper mappingJunctionSectionMapper;
  @Mock
  private MappingDroneportSectionMapper mappingDroneportSectionMapper;
  @Mock
  private AirwaySequenceMapper airwaySequenceMapper;
  @Mock
  private AirwayDeterminationMapper airwayDeterminationMapper;
  @Mock
  private FallSpaceMapper fallSpaceMapper;
  @Mock
  private FallToleranceRangeMapper fallToleranceRangeMapper;
  @Mock
  private AirwayCompatibleModelsMapper airwayCompatibleModelsMapper;

  @MockBean
  private AirwayDesignService airwayDesignService;
  @Mock
  private ObjectMapper beanObjectMapper;
  @Mock
  private RailwayCrossingInfoLogic railwayCrossingInfoLogic;
  @Mock
  private FallSpaceLogic fallSpaceLogic;
  @Mock
  private FallToleranceRangeLogic fallToleranceRangeLogic;

  /** クロック */
  @Mock
  private Clock clock;

  @InjectMocks
  AirwayDesignLogic logic;

  private String currentDateTime = null;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("GSW航路情報を登録する")
  public void testAirwayPostGSW() throws Exception {

    // 現在日時
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    currentDateTime = dtf.format(LocalDateTime.now());

    AirwaysGSWPostRequestEntity request = new AirwaysGSWPostRequestEntity();
    request.setFallToleranceRange(createFallToleranceRangePostRequestEntity());
    request.setAirwayName("UnitTest-Airway-" + currentDateTime);
    request.setFlightPurpose("送電線点検");
    request.setAirwayParts(createGSWAirwayPartsForTest());

    FallSpacePostRequestDroneEntity drone = new FallSpacePostRequestDroneEntity();
    drone.setAircraftInfoId(1);
    drone.setMaker("UnitTest-Maker");
    drone.setModelNumber("UnitTest-Model");
    drone.setName("UnitTest-Name");
    drone.setType("UnitTest-Type");
    drone.setIp("IP64");
    drone.setLength(10);
    drone.setWeight(10);
    drone.setMaximumFlightTime(100);
    drone.setMaximumTakeoffWeight(100);
    request.setDroneList(List.of(drone));

    FallToleranceRangePostResponseDto mockFallToleranceRangePostResponseDto =
        new FallToleranceRangePostResponseDto();
    mockFallToleranceRangePostResponseDto.setFallToleranceRangeId("1");

    FallSpacePostResponseDto mockFallSpacePostResponseDto = new FallSpacePostResponseDto();
    mockFallSpacePostResponseDto.setAirwayDeterminationId(1);

    AirwayDetermination mockAirwayDetermination = createAirwayDeterminationMockForGet("1");

    Airway mockAirway = createAirwayMockForGet("1");

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {

      mockedStatic.when(() -> clock.instant()).thenReturn(Instant.now(Clock.systemUTC()));
      mockedStatic.when(() -> clock.getZone()).thenReturn(ZoneId.of("UTC"));

      mockedStatic.when(() -> fallToleranceRangeLogic.insert(any()))
          .thenReturn(mockFallToleranceRangePostResponseDto);

      mockedStatic.when(() -> fallSpaceLogic.basicInfoRegistration(any()))
          .thenReturn(mockFallSpacePostResponseDto);

      mockedStatic.when(() -> airwayDeterminationMapper.selectByPrimaryKey(1))
          .thenReturn(mockAirwayDetermination);

      mockedStatic.when(() -> airwayMapper.selectByPrimaryKey(any())).thenReturn(mockAirway);

      mockedStatic.when(() -> fallToleranceRangeMapper.selectForOperatorId("bussinessNumber-0"))
          .thenReturn("1");

      mockedStatic.when(() -> beanObjectMapper.getDateFormat())
          .thenReturn(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));

      mockedStatic.when(() -> beanObjectMapper.writeValueAsString(any())).thenReturn("{}");

      // 航路情報登録
      AirwaysPostResponseEntity response = logic.airwayPostGSW(request);

      // 航路IDが入っているか確認する
      assertNotNull(response);
      assertNotNull(response.getAirwayId());
      assertNotEquals(response.getAirwayId(), "");
      assertEquals(response.getAirwayId().indexOf("bussinessNumber-0"), 0);
    }

  }

  /**
   * GSW航路情報登録用テストデータ作成
   * 
   * @return
   */
  private List<AirwaysGSWPostRequestAirwayPartsEntity> createGSWAirwayPartsForTest() {

    List<AirwaysGSWPostRequestAirwayPartsEntity> list =
        new ArrayList<AirwaysGSWPostRequestAirwayPartsEntity>();


    AirwaysPostRequestDespersionNodeWithFallSpaceEntity despersionNode =
        new AirwaysPostRequestDespersionNodeWithFallSpaceEntity();
    despersionNode.setName("despersionNode");
    despersionNode.setFallSpace(new AirwaysPostRequestDespersionNodeWithFallSpaceEntityFallSpace());
    despersionNode.setGeometry(new AirwaysPostRequestDespersionNodeWithFallSpaceEntityGeometry());

    {
      AirwaysGSWPostRequestAirwayPartsEntity airwayParts =
          new AirwaysGSWPostRequestAirwayPartsEntity();
      airwayParts.setPrevAirwayPartsIndex(null);
      airwayParts.setAirwayJunction(createTestJunction());

      AirwaysPostRequestAirwaySectionEntity section = new AirwaysPostRequestAirwaySectionEntity();
      section.setName("A-1");

      airwayParts.setAirwaySection(section);
      airwayParts.setDespersionNode(despersionNode);
      list.add(airwayParts);
    }

    {
      AirwaysGSWPostRequestAirwayPartsEntity airwayParts =
          new AirwaysGSWPostRequestAirwayPartsEntity();
      airwayParts.setPrevAirwayPartsIndex(0);
      airwayParts.setAirwayJunction(createTestJunction());
      airwayParts.setAirwaySection(null);
      airwayParts.setDespersionNode(despersionNode);
      list.add(airwayParts);
    }
    return list;
  }

  /**
   * 最大落下許容範囲リクエストボディ作成
   * 
   * @return
   */
  private FallToleranceRangePostRequestEntity createFallToleranceRangePostRequestEntity() {
    FallToleranceRangePostRequestEntity fallToleranceRange =
        new FallToleranceRangePostRequestEntity();
    fallToleranceRange.setBusinessNumber("bussinessNumber-0");
    fallToleranceRange.setAirwayOperatorId("1");
    fallToleranceRange.setName("name");
    fallToleranceRange.setAreaName("UnitTest-FallToleranceRange-" + currentDateTime);
    fallToleranceRange.setElevationTerrain("10m");

    FallToleranceRangePostRequestEntityGeometry fallToleranceRangeGeometry =
        new FallToleranceRangePostRequestEntityGeometry();
    fallToleranceRangeGeometry.setType("Polygon");
    List<List<List<BigDecimal>>> coordinates = new ArrayList<>();
    List<List<BigDecimal>> coordinatesFirst = new ArrayList<>();
    coordinatesFirst.add(createPointGeometry(new BigDecimal(130.34985374430653),
        new BigDecimal(33.593513745109334), new BigDecimal(130.00)));
    coordinatesFirst.add(createPointGeometry(new BigDecimal(130.34985374430653),
        new BigDecimal(33.59207488455793), new BigDecimal(120.00)));
    coordinatesFirst.add(createPointGeometry(new BigDecimal(130.35321186999215),
        new BigDecimal(33.59207488455793), new BigDecimal(120.00)));
    coordinatesFirst.add(createPointGeometry(new BigDecimal(130.35321186999215),
        new BigDecimal(33.593513745109334), new BigDecimal(130.00)));
    coordinatesFirst.add(createPointGeometry(new BigDecimal(130.34985374430653),
        new BigDecimal(33.593513745109334), new BigDecimal(130.00)));
    coordinates.add(coordinatesFirst);
    fallToleranceRangeGeometry.setCoordinates(coordinates);
    fallToleranceRange.setGeometry(fallToleranceRangeGeometry);
    return fallToleranceRange;
  }

  @Test
  @DisplayName("航路情報を登録する")
  public void testAirwayPost() throws Exception {

    // 現在日時
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    currentDateTime = dtf.format(LocalDateTime.now());

    /**
     * 航路情報登録のリクエストをモックする
     */
    // 試験用のリクエストを作成する
    AirwaysPostRequestEntity request = new AirwaysPostRequestEntity();
    request.setAirwayDeterminationId(1);
    request.setAirwayName("UnitTest-Airway-" + currentDateTime);
    request.setFlightPurpose("送電線点検");
    List<AirwaysPostRequestAirwayPartsEntity> airwayPartsList =
        new ArrayList<AirwaysPostRequestAirwayPartsEntity>();

    // 航路情報のパーツを作成する
    airwayPartsList.add(createAirwayPartsForTest());
    // 2つ目のパーツを作成する
    airwayPartsList.add(createAirwayPartsForTest(airwayPartsList.get(0), 0));
    request.setAirwayParts(airwayPartsList);

    // 事業者番号を取得するモックデータを用意
    AirwayDetermination mockAirwayDetermination = new AirwayDetermination();
    mockAirwayDetermination.setBusinessNumber("bussinessNumber-0");
    mockAirwayDetermination.setAirwayDeterminationId(1);
    mockAirwayDetermination.setFallToleranceRangeId("1");

    /**
     * MQTTで送信する酵素情報をMockする
     */
    // 航路情報をMockする
    final String airwayId =
        mockAirwayDetermination.getBusinessNumber() + "_" + UUID.randomUUID().toString();

    Airway mockAirway = createAirwayMockForGet(airwayId);

    // 事業者番号をMockする
    AirwayDetermination mockAirwayDeterminationForGet =
        createAirwayDeterminationMockForGet(airwayId);

    // 航路区画をMockする
    List<AirwaySection> mockAirwaySectionsForGet = createAirwaySectionMockForGet(airwayId);

    // ジャンクションをMockする
    List<AirwayJunction> mockAirwayJunctionsForGet = createAirwayJunctionMockForGet(airwayId);


    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {

      mockedStatic.when(() -> clock.instant()).thenReturn(Instant.now(Clock.systemUTC()));
      mockedStatic.when(() -> clock.getZone()).thenReturn(ZoneId.of("UTC"));

      /** 航路情報登録用のMock定義 */
      mockedStatic.when(() -> airwayDeterminationMapper.selectByPrimaryKey(1))
          .thenReturn(mockAirwayDetermination);

      mockedStatic.when(() -> fallToleranceRangeMapper.selectForOperatorId("bussinessNumber-0"))
          .thenReturn("1");

      // Mock航路情報
      mockedStatic.when(() -> airwayMapper.selectByPrimaryKey(any())).thenReturn(mockAirway);

      // Mock事業者番号
      mockedStatic.when(
          () -> airwayDeterminationMapper.selectByPrimaryKey(mockAirway.getAirwayDeterminationId()))
          .thenReturn(mockAirwayDeterminationForGet);

      // Mock航路区画
      mockedStatic.when(() -> airwaySectionMapper.selectByAirwayId(airwayId))
          .thenReturn(mockAirwaySectionsForGet);

      // Mockジャンクション
      mockedStatic.when(() -> airwayJunctionMapper.selectByAirwayId(airwayId))
          .thenReturn(mockAirwayJunctionsForGet);

      mockedStatic.when(() -> beanObjectMapper.getDateFormat())
          .thenReturn(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));

      mockedStatic.when(() -> beanObjectMapper.writeValueAsString(any())).thenReturn("{}");

      // 航路情報登録
      AirwaysPostResponseEntity response = logic.airwayPost(request);

      // 航路IDが入っているか確認する
      assertNotNull(response);
      assertNotNull(response.getAirwayId());
      assertNotEquals(response.getAirwayId(), "");
      assertEquals(response.getAirwayId().indexOf("bussinessNumber-0"), 0);
    }
  }

  private Airway createAirwayMockForGet(final String airwayId) {
    Airway mockAirway = new Airway();
    mockAirway.setAirwayDeterminationId(1);
    mockAirway.setAirwayId(airwayId);
    mockAirway.setFlightPurpose("送電線点検");
    mockAirway.setParentNodeAirwayId(null);
    mockAirway.setCreatedAt(LocalDateTime.now());
    mockAirway.setUpdatedAt(LocalDateTime.now());
    return mockAirway;
  }

  private AirwayDetermination createAirwayDeterminationMockForGet(final String airwayId) {
    AirwayDetermination mockAirwayDetermination = new AirwayDetermination();
    mockAirwayDetermination.setBusinessNumber("bussinessNumber-0");
    mockAirwayDetermination.setAirwayDeterminationId(1);
    mockAirwayDetermination.setFallToleranceRangeId("1");
    return mockAirwayDetermination;
  }

  private List<AirwayJunction> createAirwayJunctionMockForGet(final String airwayId)
      throws JsonProcessingException {

    ObjectMapper objectMapper = new ObjectMapper();
    List<AirwayJunction> mockAirwayJunctions = new ArrayList<AirwayJunction>();
    AirwayJunction mockAirwayJunction = new AirwayJunction();
    mockAirwayJunction.setAirwayId(airwayId);
    mockAirwayJunction.setAirwayJunctionId("1");
    mockAirwayJunction.setDespersionNodeId(1);
    mockAirwayJunction.setName("福岡タワー");

    // ジャンクション:航路をMockする
    AirwayGeometry geometry = new AirwayGeometry();
    geometry.setType("Polygon");
    List<List<List<BigDecimal>>> juncGeoCoordinates = new ArrayList<List<List<BigDecimal>>>();
    List<List<BigDecimal>> juncGeoCoordinatesFirst = new ArrayList<List<BigDecimal>>();

    juncGeoCoordinatesFirst.add(createPointGeometry(new BigDecimal(130.34985374430653),
        new BigDecimal(33.593513745109334), new BigDecimal(130.00)));

    juncGeoCoordinatesFirst.add(createPointGeometry(new BigDecimal(130.34985374430653),
        new BigDecimal(33.59207488455793), new BigDecimal(120.00)));

    juncGeoCoordinatesFirst.add(createPointGeometry(new BigDecimal(130.35321186999215),
        new BigDecimal(33.59207488455793), new BigDecimal(120.00)));

    juncGeoCoordinatesFirst.add(createPointGeometry(new BigDecimal(130.35321186999215),
        new BigDecimal(33.593513745109334), new BigDecimal(130.00)));

    juncGeoCoordinatesFirst.add(createPointGeometry(new BigDecimal(130.34985374430653),
        new BigDecimal(33.593513745109334), new BigDecimal(130.00)));

    juncGeoCoordinates.add(juncGeoCoordinatesFirst);
    geometry.setCoordinates(juncGeoCoordinates);
    mockAirwayJunction.setGeometry(objectMapper.writeValueAsString(geometry));

    // ジャンクション:逸脱範囲をMockする
    AirwayGeometry devGeometry = new AirwayGeometry();
    devGeometry.setType("Polygon");
    List<List<List<BigDecimal>>> juncDevCoordinates = new ArrayList<List<List<BigDecimal>>>();
    List<List<BigDecimal>> juncDevGeoCoordinatesFirst = new ArrayList<List<BigDecimal>>();

    juncDevGeoCoordinatesFirst.add(createPointGeometry(new BigDecimal(130.34959624682978),
        new BigDecimal(33.59371929853779), new BigDecimal(131.00)));

    juncDevGeoCoordinatesFirst.add(createPointGeometry(new BigDecimal(130.34959624682978),
        new BigDecimal(33.59127948317507), new BigDecimal(119.00)));

    juncDevGeoCoordinatesFirst.add(createPointGeometry(new BigDecimal(130.35441349422058),
        new BigDecimal(33.59127948317507), new BigDecimal(119.00)));

    juncDevGeoCoordinatesFirst.add(createPointGeometry(new BigDecimal(130.35441349422058),
        new BigDecimal(33.59371929853779), new BigDecimal(131.00)));

    juncDevGeoCoordinatesFirst.add(createPointGeometry(new BigDecimal(130.34959624682978),
        new BigDecimal(33.59371929853779), new BigDecimal(131.00)));
    juncDevCoordinates.add(juncDevGeoCoordinatesFirst);
    devGeometry.setCoordinates(juncDevCoordinates);
    mockAirwayJunction.setDeviationGeometry(objectMapper.writeValueAsString(devGeometry));

    mockAirwayJunctions.add(mockAirwayJunction);

    return mockAirwayJunctions;
  }

  private List<AirwaySection> createAirwaySectionMockForGet(final String airwayId) {
    List<AirwaySection> mockAirwaySections = new ArrayList<AirwaySection>();
    AirwaySection mockAirwaySection = new AirwaySection();
    mockAirwaySection.setAirwayId(airwayId);
    mockAirwaySection.setAirwaySectionId("1");
    mockAirwaySection.setName("A-1");
    mockAirwaySections.add(mockAirwaySection);
    return mockAirwaySections;
  }

  private List<MappingDroneportSection> createDroneportSectionsMockForGet() {
    List<MappingDroneportSection> mockDroneportSections = new ArrayList<MappingDroneportSection>();
    MappingDroneportSection mockDroneportSection1 = new MappingDroneportSection();
    mockDroneportSection1.setMappingDroneportSectionId(1);
    mockDroneportSection1.setAirwaySectionId("1");
    mockDroneportSection1.setDroneportId("droneport1");
    mockDroneportSections.add(mockDroneportSection1);
    MappingDroneportSection mockDroneportSection2 = new MappingDroneportSection();
    mockDroneportSection2.setMappingDroneportSectionId(2);
    mockDroneportSection2.setAirwaySectionId("1");
    mockDroneportSection2.setDroneportId("droneport2");
    mockDroneportSections.add(mockDroneportSection2);
    return mockDroneportSections;
  }

  private AirwayGeometry convertObjToGeometry(Object geometry)
      throws JsonMappingException, JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(geometry.toString(), AirwayGeometry.class);

  }

  @Test
  @DisplayName("航路情報を取得する(Jsonプロセスエラー)")
  public void testAllAirwayGetJsonProccesingError()
      throws JsonProcessingException, NoHandlerFoundException {

    // 現在日時
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    currentDateTime = dtf.format(LocalDateTime.now());

    final String airwayId = "1";

    // 航路情報をMockする
    Airway mockAirway = createAirwayMockForGet(airwayId);

    // 事業者番号をMockする
    AirwayDetermination mockAirwayDetermination = createAirwayDeterminationMockForGet(airwayId);

    // 航路区画をMockする
    List<AirwaySection> mockAirwaySections = createAirwaySectionMockForGet(airwayId);

    AirwayJunction mockAirwayJunction = new AirwayJunction();
    mockAirwayJunction.setAirwayId(airwayId);
    mockAirwayJunction.setAirwayJunctionId("1");
    mockAirwayJunction.setCreatedAt(null);
    mockAirwayJunction.setDespersionNodeId(1);
    mockAirwayJunction.setUpdatedAt(null);
    mockAirwayJunction.setName("福岡タワー");
    mockAirwayJunction.setGeometry(
        "\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[138.7309,35.3628,100],[138.8079,35.1983,100],[138.8079,35.1983,50],[138.7309,35.3628,50],[138.7309,35.3628,100]]}");
    List<AirwayJunction> mockAirwayJunctions = List.of(mockAirwayJunction);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {

      // Mock航路情報
      mockedStatic.when(() -> airwayMapper.selectByPrimaryKey(any())).thenReturn(mockAirway);

      // Mpck航路運営者ID
      mockedStatic.when(() -> fallToleranceRangeMapper.selectForOperatorId(any())).thenReturn("1");

      // Mock事業者番号
      mockedStatic.when(
          () -> airwayDeterminationMapper.selectByPrimaryKey(mockAirway.getAirwayDeterminationId()))
          .thenReturn(mockAirwayDetermination);

      // Mock航路区画
      mockedStatic.when(() -> airwaySectionMapper.selectByAirwayId(any()))
          .thenReturn(mockAirwaySections);

      // Mockジャンクション
      mockedStatic.when(() -> airwayJunctionMapper.selectByAirwayId(any()))
          .thenReturn(mockAirwayJunctions);

      mockedStatic.when(() -> clock.instant()).thenReturn(Instant.now(Clock.systemUTC()));
      mockedStatic.when(() -> clock.getZone()).thenReturn(ZoneId.of("UTC"));

      // 航路情報取得
      assertThrows(DroneHighwayException.class, () -> logic.airwayGet(List.of(airwayId), false));
    }
  }

  private List<MappingJunctionSection> createMappingJunctionSection() {
    List<MappingJunctionSection> mappingJunctionSection = new ArrayList<MappingJunctionSection>();
    {
      MappingJunctionSection section = new MappingJunctionSection();
      section.setAirwayJunctionId("1");
      section.setAirwaySectionId("1");
      section.setMappingJunctionSectionId(1);
      mappingJunctionSection.add(section);
    }
    {
      MappingJunctionSection section = new MappingJunctionSection();
      section.setAirwayJunctionId("2");
      section.setAirwaySectionId("2");
      section.setMappingJunctionSectionId(2);
      mappingJunctionSection.add(section);
    }
    return mappingJunctionSection;
  }

  @Test
  @DisplayName("航路情報を取得する(日時フォーマットエラー)")
  public void testAllAirwayGetDateError() throws JsonProcessingException, NoHandlerFoundException {

    {
      DroneHighwayException ex = assertThrows(DroneHighwayException.class,
          () -> logic.airwayGet(null, false, null, "20250101", null, null, null));
      assertThat(ex.getMessage(), is("日時のフォーマットが違います"));
    }
    {
      DroneHighwayException ex = assertThrows(DroneHighwayException.class,
          () -> logic.airwayGet(null, false, null, null, "20250101", null, null));
      assertThat(ex.getMessage(), is("日時のフォーマットが違います"));
    }
  }

  @Test
  @DisplayName("航路情報を取得する(NotFound)")
  public void testAllAirwayGetNotFound() {

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      // 全件Mock航路情報
      mockedStatic.when(() -> airwayMapper.selectAll()).thenReturn(null);
      assertThrows(DataNotFoundException.class, () -> logic.airwayGet(null, true));
    }

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      // ID指定
      mockedStatic.when(() -> airwayMapper.selectByPrimaryKey(any())).thenReturn(null);
      assertThrows(DataNotFoundException.class, () -> logic.airwayGet(List.of("1"), false));
    }
  }

  @Test
  @DisplayName("航路運営者IDを取得する(NotFound)")
  public void testOperatorIdNotFound() throws JsonProcessingException {

    final String airwayId = UUID.randomUUID().toString();
    Airway mockAirway = createAirwayMockForGet(airwayId);

    // 航路区画をMockする
    List<AirwaySection> mockAirwaySections = createAirwaySectionMockForGet(airwayId);
    // AirwaySection mockAirwaySection = mockAirwaySections.get(0);

    // ジャンクションをMockする
    List<AirwayJunction> mockAirwayJunctions = createAirwayJunctionMockForGet(airwayId);
    // AirwayJunction mockAirwayJunction = mockAirwayJunctions.get(0);

    // ドローンポート/航路区画をMockする
    List<MappingDroneportSection> mockDroneportSections = createDroneportSectionsMockForGet();

    List<MappingJunctionSection> mappingJunctionSection = createMappingJunctionSection();

    AirwayDetermination mockAirwayDetermination = createAirwayDeterminationMockForGet(airwayId);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      // Mock航路区画
      mockedStatic.when(() -> airwaySectionMapper.selectByAirwayId(any()))
          .thenReturn(mockAirwaySections);

      // Mockジャンクション
      mockedStatic.when(() -> airwayJunctionMapper.selectByAirwayId(any()))
          .thenReturn(mockAirwayJunctions);

      // Mockドローンリスト
      AirwayCompatibleModels droneModel = new AirwayCompatibleModels();
      droneModel.setAirwayDeterminationId(mockAirway.getAirwayDeterminationId());
      droneModel.setAirwayCompatibleModelsId(1);
      List<AirwayCompatibleModels> droneList = new ArrayList<AirwayCompatibleModels>();
      droneList.add(droneModel);
      mockedStatic
          .when(() -> airwayCompatibleModelsMapper
              .selectByAirwayDeterminationId(mockAirway.getAirwayDeterminationId()))
          .thenReturn(droneList);

      // Mockドローンポート/航路区画
      mockedStatic.when(() -> mappingDroneportSectionMapper.selectByAirwaySectionId(any()))
          .thenReturn(mockDroneportSections);

      mockedStatic.when(() -> clock.instant()).thenReturn(Instant.now(Clock.systemUTC()));
      mockedStatic.when(() -> clock.getZone()).thenReturn(ZoneId.of("UTC"));

      mockedStatic.when(() -> mappingJunctionSectionMapper.selectByAirwaySectionId(any()))
          .thenReturn(mappingJunctionSection);

      // Mock航路情報
      mockedStatic.when(() -> airwayMapper.selectByPrimaryKey(any())).thenReturn(mockAirway);

      mockedStatic.when(() -> airwayDeterminationMapper.selectByPrimaryKey(any()))
          .thenReturn(mockAirwayDetermination);

      // 航路運営者ID
      mockedStatic.when(() -> fallToleranceRangeMapper.selectForOperatorId(any())).thenReturn(null);
      assertThrows(DataNotFoundException.class, () -> logic.airwayGet(List.of(airwayId), false));
    }
  }

  @Test
  @DisplayName("事業者番号を取得する(NotFound)")
  public void testBussinessNumberNotFound() throws JsonProcessingException {
    final String airwayId = UUID.randomUUID().toString();
    Airway mockAirway = createAirwayMockForGet(airwayId);

    // 航路区画をMockする
    List<AirwaySection> mockAirwaySections = createAirwaySectionMockForGet(airwayId);
    // AirwaySection mockAirwaySection = mockAirwaySections.get(0);

    // ジャンクションをMockする
    List<AirwayJunction> mockAirwayJunctions = createAirwayJunctionMockForGet(airwayId);
    // AirwayJunction mockAirwayJunction = mockAirwayJunctions.get(0);

    // ドローンポート/航路区画をMockする
    List<MappingDroneportSection> mockDroneportSections = createDroneportSectionsMockForGet();

    List<MappingJunctionSection> mappingJunctionSection = createMappingJunctionSection();

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      // Mock航路区画
      mockedStatic.when(() -> airwaySectionMapper.selectByAirwayId(any()))
          .thenReturn(mockAirwaySections);

      // Mockジャンクション
      mockedStatic.when(() -> airwayJunctionMapper.selectByAirwayId(any()))
          .thenReturn(mockAirwayJunctions);

      // Mockドローンリスト
      AirwayCompatibleModels droneModel = new AirwayCompatibleModels();
      droneModel.setAirwayDeterminationId(mockAirway.getAirwayDeterminationId());
      droneModel.setAirwayCompatibleModelsId(1);
      List<AirwayCompatibleModels> droneList = new ArrayList<AirwayCompatibleModels>();
      droneList.add(droneModel);
      mockedStatic
          .when(() -> airwayCompatibleModelsMapper
              .selectByAirwayDeterminationId(mockAirway.getAirwayDeterminationId()))
          .thenReturn(droneList);

      // Mockドローンポート/航路区画
      mockedStatic.when(() -> mappingDroneportSectionMapper.selectByAirwaySectionId(any()))
          .thenReturn(mockDroneportSections);

      mockedStatic.when(() -> clock.instant()).thenReturn(Instant.now(Clock.systemUTC()));
      mockedStatic.when(() -> clock.getZone()).thenReturn(ZoneId.of("UTC"));

      mockedStatic.when(() -> mappingJunctionSectionMapper.selectByAirwaySectionId(any()))
          .thenReturn(mappingJunctionSection);

      // Mock航路情報
      mockedStatic.when(() -> airwayMapper.selectByPrimaryKey(any())).thenReturn(mockAirway);

      mockedStatic.when(() -> fallToleranceRangeMapper.selectForOperatorId(any())).thenReturn("1");

      // 事業者番号
      mockedStatic.when(() -> airwayDeterminationMapper.selectByPrimaryKey(any())).thenReturn(null);
      assertThrows(DataNotFoundException.class, () -> logic.airwayGet(List.of(airwayId), false));
    }
  }

  @Test
  @DisplayName("航路情報を全件取得する")
  public void testAllAirwayGet() throws JsonProcessingException, NoHandlerFoundException {
    this.testAirwayGet(null, true);
  }

  @Test
  @DisplayName("航路情報を取得する")
  public void testAirwayGetById() throws JsonProcessingException, NoHandlerFoundException {
    final String airwayId = UUID.randomUUID().toString();
    this.testAirwayGet(airwayId, false);
  }

  public void testAirwayGet(final String airwayId, final boolean isAll)
      throws JsonProcessingException, NoHandlerFoundException {

    // 現在日時
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    currentDateTime = dtf.format(LocalDateTime.now());

    // 航路情報をMockする
    Airway mockAirway = createAirwayMockForGet(airwayId);

    // 事業者番号をMockする
    AirwayDetermination mockAirwayDetermination = createAirwayDeterminationMockForGet(airwayId);

    // 航路区画をMockする
    List<AirwaySection> mockAirwaySections = createAirwaySectionMockForGet(airwayId);
    AirwaySection mockAirwaySection = mockAirwaySections.get(0);

    // ジャンクションをMockする
    List<AirwayJunction> mockAirwayJunctions = createAirwayJunctionMockForGet(airwayId);
    AirwayJunction mockAirwayJunction = mockAirwayJunctions.get(0);

    // ドローンポート/航路区画をMockする
    List<MappingDroneportSection> mockDroneportSections = createDroneportSectionsMockForGet();

    List<MappingJunctionSection> mappingJunctionSection = createMappingJunctionSection();

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {

      // 全件Mock航路情報
      mockedStatic.when(() -> airwayMapper.selectAll()).thenReturn(List.of(mockAirway));

      // Mock航路情報
      mockedStatic.when(() -> airwayMapper.selectByPrimaryKey(any())).thenReturn(mockAirway);

      // Mpck航路運営者ID
      mockedStatic.when(() -> fallToleranceRangeMapper.selectForOperatorId(any())).thenReturn("1");

      // Mock事業者番号
      mockedStatic.when(
          () -> airwayDeterminationMapper.selectByPrimaryKey(mockAirway.getAirwayDeterminationId()))
          .thenReturn(mockAirwayDetermination);

      // Mock航路区画
      mockedStatic.when(() -> airwaySectionMapper.selectByAirwayId(any()))
          .thenReturn(mockAirwaySections);

      // Mockジャンクション
      mockedStatic.when(() -> airwayJunctionMapper.selectByAirwayId(any()))
          .thenReturn(mockAirwayJunctions);

      // Mockドローンリスト
      AirwayCompatibleModels droneModel = new AirwayCompatibleModels();
      droneModel.setAirwayDeterminationId(mockAirway.getAirwayDeterminationId());
      droneModel.setAirwayCompatibleModelsId(1);
      List<AirwayCompatibleModels> droneList = new ArrayList<AirwayCompatibleModels>();
      droneList.add(droneModel);
      mockedStatic
          .when(() -> airwayCompatibleModelsMapper
              .selectByAirwayDeterminationId(mockAirway.getAirwayDeterminationId()))
          .thenReturn(droneList);

      // Mockドローンポート/航路区画
      mockedStatic.when(() -> mappingDroneportSectionMapper.selectByAirwaySectionId(any()))
          .thenReturn(mockDroneportSections);

      mockedStatic.when(() -> clock.instant()).thenReturn(Instant.now(Clock.systemUTC()));
      mockedStatic.when(() -> clock.getZone()).thenReturn(ZoneId.of("UTC"));

      mockedStatic.when(() -> mappingJunctionSectionMapper.selectByAirwaySectionId(any()))
          .thenReturn(mappingJunctionSection);

      // 航路情報取得
      AirwayEntity response = null;
      if (isAll) {
        response = logic.airwayGet(null, true);
      } else {
        response = logic.airwayGet(List.of(airwayId), false);
      }
      assertNotNull(response);

      // 航路情報の確認
      assertNotNull(response.getAirway());
      assertEquals("1", response.getAirway().getAirwayAdministratorId());
      assertEquals(mockAirwayDetermination.getBusinessNumber(),
          response.getAirway().getBusinessNumber());

      assertNotNull(response.getAirway().getAirways());
      assertEquals(1, response.getAirway().getAirways().size());

      AirwaysEntity airwaysEntity = response.getAirway().getAirways().get(0);
      assertEquals(airwayId, airwaysEntity.getAirwayId());
      assertEquals(mockAirway.getName(), airwaysEntity.getAirwayName());

      // 航路区画の確認
      assertNotNull(airwaysEntity.getAirwaySections());
      assertEquals(airwaysEntity.getAirwaySections().size(), 1);
      AirwaySectionsEntity airwaySectionsEntity = airwaysEntity.getAirwaySections().get(0);
      assertEquals(airwaySectionsEntity.getAirwaySectionId(),
          mockAirwaySection.getAirwaySectionId().toString());
      assertEquals(airwaySectionsEntity.getAirwaySectionName(), mockAirwaySection.getName());

      assertEquals(airwaySectionsEntity.getAirwayJunctionIds().size(), 2);
      // 区画に紐づくドローンポートリストは、mockにて2つ設定済み
      assertEquals(airwaySectionsEntity.getDroneportIds().size(), 2);
      // 1つ目が同値
      assertEquals(airwaySectionsEntity.getDroneportIds().get(0),
          mockDroneportSections.get(0).getDroneportId());
      // 2つ目が同値
      assertEquals(airwaySectionsEntity.getDroneportIds().get(1),
          mockDroneportSections.get(1).getDroneportId());

      // ジャンクションの確認
      assertNotNull(airwaysEntity.getAirwayJunctions());
      assertEquals(airwaysEntity.getAirwayJunctions().size(), 1);
      AirwayJunctionsEntity airwayJunctionsEntity = airwaysEntity.getAirwayJunctions().get(0);
      assertEquals(airwayJunctionsEntity.getAirwayJunctionId(),
          mockAirwayJunction.getAirwayJunctionId().toString());
      assertEquals(airwayJunctionsEntity.getAirwayJunctionName(), mockAirwayJunction.getName());
      assertNotNull(airwayJunctionsEntity.getAirways());
      assertEquals(airwayJunctionsEntity.getAirways().size(), 1);

      // ジャンクション：航路の確認
      AirwayJunctionsEntityAirwaysInner airwayJunctionsEntityAirwaysInner =
          airwayJunctionsEntity.getAirways().get(0);
      assertNotNull(airwayJunctionsEntityAirwaysInner.getAirway());
      assertNotNull(airwayJunctionsEntityAirwaysInner.getAirway().getGeometry());
      AirwayJunctionsEntityAirwaysInnerAirwayGeometry airwayJunctionsEntityAirwaysInnerAirwayGeometry =
          airwayJunctionsEntityAirwaysInner.getAirway().getGeometry();
      assertEquals(airwayJunctionsEntityAirwaysInnerAirwayGeometry.getType(), "Polygon");
      assertNotNull(airwayJunctionsEntityAirwaysInnerAirwayGeometry.getCoordinates());

      List<List<BigDecimal>> coorinates =
          airwayJunctionsEntityAirwaysInnerAirwayGeometry.getCoordinates();
      assertEquals(coorinates.size(), 5);
      assertNotNull(coorinates.get(0));
      assertNotNull(coorinates.get(1));
      assertNotNull(coorinates.get(2));
      assertNotNull(coorinates.get(3));
      assertNotNull(coorinates.get(4));

      assertEquals(coorinates.get(0).size(), 3); // 位置情報の確認
      final List<List<BigDecimal>> juncGeoCoordinatesFirst =
          convertObjToGeometry(mockAirwayJunction.getGeometry()).getCoordinates().get(0);
      assertIterableEquals(coorinates.get(0), juncGeoCoordinatesFirst.get(0));

      // ジャンクション：逸脱範囲の確認
      assertNotNull(airwayJunctionsEntityAirwaysInner.getDeviation());
      AirwayJunctionsEntityAirwaysInnerAirwayGeometry airwayJunctionsEntityAirwaysInnerDevGeometry =
          airwayJunctionsEntityAirwaysInner.getDeviation().getGeometry();
      assertEquals(airwayJunctionsEntityAirwaysInnerDevGeometry.getType(), "Polygon");
      assertNotNull(airwayJunctionsEntityAirwaysInnerDevGeometry.getCoordinates());

      List<List<BigDecimal>> devCoorinates =
          airwayJunctionsEntityAirwaysInnerDevGeometry.getCoordinates();
      assertEquals(devCoorinates.size(), 5);
      assertNotNull(devCoorinates.get(0));
      assertNotNull(devCoorinates.get(1));
      assertNotNull(devCoorinates.get(2));
      assertNotNull(devCoorinates.get(3));
      assertNotNull(devCoorinates.get(4));

      assertEquals(devCoorinates.get(0).size(), 3); // 位置情報の確認
      final List<List<BigDecimal>> juncDevGeoCoordinatesFirst =
          convertObjToGeometry(mockAirwayJunction.getDeviationGeometry()).getCoordinates().get(0);
      assertIterableEquals(devCoorinates.get(0), juncDevGeoCoordinatesFirst.get(0));
    }
  }

  private AirwaysPostRequestAirwayPartsEntity createAirwayPartsForTest() {
    return createAirwayPartsForTest(null, null);
  }

  private AirwaysPostRequestAirwayPartsEntity createAirwayPartsForTest(
      AirwaysPostRequestAirwayPartsEntity prevParts, final Integer prevAirwayPartsIndex) {

    AirwaysPostRequestAirwayPartsEntity partsEntity = new AirwaysPostRequestAirwayPartsEntity();
    partsEntity.setPrevAirwayPartsIndex(prevAirwayPartsIndex);

    // 落下範囲節
    AirwaysPostRequestDespersionNodeEntity despersionNode =
        new AirwaysPostRequestDespersionNodeEntity();
    despersionNode.setName("福岡タワー");
    despersionNode.setFallSpaceCrossSectionId(1);
    AirwaysPostRequestDespersionNodeEntityGeometry dnGeometry =
        new AirwaysPostRequestDespersionNodeEntityGeometry();
    dnGeometry.setType("LieString");
    List<List<BigDecimal>> dnCoordinates = new ArrayList<List<BigDecimal>>();
    dnCoordinates.add(
        createPointGeometry(new BigDecimal(130.34989665964878), new BigDecimal(33.59353161900219)));
    dnCoordinates.add(
        createPointGeometry(new BigDecimal(130.353190412321), new BigDecimal(33.59320095137866)));
    dnGeometry.setCoordinates(dnCoordinates);
    despersionNode.setGeometry(dnGeometry);

    partsEntity.setDespersionNode(despersionNode);

    // 航路区画
    if (null != prevAirwayPartsIndex) {
      AirwaysPostRequestAirwaySectionEntity section = new AirwaysPostRequestAirwaySectionEntity();
      section.setName("A-1");
      prevParts.setAirwaySection(section);
    }
    partsEntity.setAirwaySection(new AirwaysPostRequestAirwaySectionEntity());
    partsEntity.setAirwayJunction(createTestJunction());

    return partsEntity;
  }

  private List<AirwaysPostRequestAirwayJunctionEntity> createTestJunction() {
    // ジャンクション
    List<AirwaysPostRequestAirwayJunctionEntity> airwayJunction = new ArrayList<>();
    AirwaysPostRequestAirwayJunctionEntity junctionEntity =
        new AirwaysPostRequestAirwayJunctionEntity();
    junctionEntity.setName("タワー上空");

    // ジャンクション:航路
    AirwaysPostRequestAirwayJunctionEntityGeometry geometry =
        new AirwaysPostRequestAirwayJunctionEntityGeometry();
    geometry.setType("Polygon");
    List<List<List<BigDecimal>>> juncGeoCoordinates = new ArrayList<List<List<BigDecimal>>>();
    List<List<BigDecimal>> juncGeoCoordinatesFirst = new ArrayList<List<BigDecimal>>();

    juncGeoCoordinatesFirst.add(createPointGeometry(new BigDecimal(130.34985374430653),
        new BigDecimal(33.593513745109334), new BigDecimal(130.00)));

    juncGeoCoordinatesFirst.add(createPointGeometry(new BigDecimal(130.34985374430653),
        new BigDecimal(33.59207488455793), new BigDecimal(120.00)));

    juncGeoCoordinatesFirst.add(createPointGeometry(new BigDecimal(130.35321186999215),
        new BigDecimal(33.59207488455793), new BigDecimal(120.00)));

    juncGeoCoordinatesFirst.add(createPointGeometry(new BigDecimal(130.35321186999215),
        new BigDecimal(33.593513745109334), new BigDecimal(130.00)));

    juncGeoCoordinatesFirst.add(createPointGeometry(new BigDecimal(130.34985374430653),
        new BigDecimal(33.593513745109334), new BigDecimal(130.00)));

    juncGeoCoordinates.add(juncGeoCoordinatesFirst);
    geometry.setCoordinates(juncGeoCoordinates);
    junctionEntity.setGeometry(geometry);

    // ジャンクション:逸脱範囲
    AirwaysPostRequestAirwayJunctionEntityDeviationGeometry devGeometry =
        new AirwaysPostRequestAirwayJunctionEntityDeviationGeometry();
    geometry.setType("Polygon");
    List<List<List<BigDecimal>>> juncDevGeoCoordinates = new ArrayList<List<List<BigDecimal>>>();
    List<List<BigDecimal>> juncDevGeoCoordinatesFirst = new ArrayList<List<BigDecimal>>();

    juncDevGeoCoordinatesFirst.add(createPointGeometry(new BigDecimal(130.34959624682978),
        new BigDecimal(33.59371929853779), new BigDecimal(131.00)));

    juncDevGeoCoordinatesFirst.add(createPointGeometry(new BigDecimal(130.34959624682978),
        new BigDecimal(33.59127948317507), new BigDecimal(119.00)));

    juncDevGeoCoordinatesFirst.add(createPointGeometry(new BigDecimal(130.35441349422058),
        new BigDecimal(33.59127948317507), new BigDecimal(119.00)));

    juncDevGeoCoordinatesFirst.add(createPointGeometry(new BigDecimal(130.35441349422058),
        new BigDecimal(33.59371929853779), new BigDecimal(131.00)));

    juncDevGeoCoordinatesFirst.add(createPointGeometry(new BigDecimal(130.34959624682978),
        new BigDecimal(33.59371929853779), new BigDecimal(131.00)));

    juncDevGeoCoordinates.add(juncDevGeoCoordinatesFirst);
    devGeometry.setCoordinates(juncDevGeoCoordinates);
    junctionEntity.setDeviationGeometry(devGeometry);

    airwayJunction.add(junctionEntity);
    return airwayJunction;
  }


  private List<BigDecimal> createPointGeometry(final BigDecimal longitutde,
      final BigDecimal latitude) {
    return createPointGeometry(longitutde, latitude, null);

  }

  private List<BigDecimal> createPointGeometry(final BigDecimal longitutde,
      final BigDecimal latitude, final BigDecimal altitude) {

    List<BigDecimal> point = new ArrayList<BigDecimal>();
    point.add(longitutde);
    point.add(latitude);

    if (null != altitude) {
      point.add(altitude);
    }
    return point;

  }

  @Test
  @DisplayName("航路削除")
  void testDelete() throws JsonProcessingException {
    AirwayDeleteRequestDto requestDto = new AirwayDeleteRequestDto();
    requestDto.setAirwayId("AABBCC_c027b2a6-03b9-413a-b233-722e8bd7b3cb");
    Airway airway = createAirwayMockForGet(requestDto.getAirwayId());
    AirwayDetermination airwayDetermination = getAirwayDetermination(airway);

    when(clock.instant()).thenReturn(Instant.now(Clock.systemUTC()));
    when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    when(airwayMapper.selectByPrimaryKey(requestDto.getAirwayId())).thenReturn(airway);
    when(airwayDeterminationMapper.selectLockByPrimaryKey(airway.getAirwayDeterminationId()))
        .thenReturn(getAirwayDetermination(airway));
    when(airwayDeterminationMapper.selectByPrimaryKey(airway.getAirwayDeterminationId()))
        .thenReturn(airwayDetermination);
    when(fallToleranceRangeMapper.selectForOperatorId(airwayDetermination.getBusinessNumber()))
        .thenReturn("operatorId01");
    when(beanObjectMapper.writeValueAsString(any())).thenReturn("{}");
    when(beanObjectMapper.getDateFormat())
        .thenReturn(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
    assertDoesNotThrow(() -> logic.delete(requestDto));
  }

  private AirwayDetermination getAirwayDetermination(Airway airway) {
    AirwayDetermination result = createAirwayDeterminationMockForGet(currentDateTime);
    result.setAirwayDeterminationId(airway.getAirwayDeterminationId());
    result.setBusinessNumber("businessNumber01");
    result.setFallToleranceRangeId("fallToleranceRangeId01");
    result.setNumCrossSectionDivisions(1);
    result.setCreatedAt(LocalDateTime.now());
    result.setUpdatedAt(result.getCreatedAt());
    result.setDelete(false);
    return result;
  }

  @Test
  @DisplayName("飛行目的に基づいたオプション検索が正常に行えること")
  public void testAirwayGetWithFlightPurposeFilters()
      throws JsonProcessingException, NoHandlerFoundException {
    final String airwayId1 = UUID.randomUUID().toString();
    final String airwayId2 = UUID.randomUUID().toString();
    final String airwayId3 = UUID.randomUUID().toString();

    // 飛行目的の設定
    List<String> flightPurposes = List.of("物資運搬", "送電線点検");

    // Mock1: 物資運搬
    Airway mockAirway1 = createAirwayMockForGet(airwayId1);
    mockAirway1.setFlightPurpose("物資運搬");

    // Mock2: 送電線点検
    Airway mockAirway2 = createAirwayMockForGet(airwayId2);
    mockAirway2.setFlightPurpose("送電線点検");

    // Mock3: 送電線点検
    Airway mockAirway3 = createAirwayMockForGet(airwayId3);
    mockAirway3.setFlightPurpose("河川監視");

    // 事業者番号をMockする
    AirwayDetermination mockAirwayDetermination1 = createAirwayDeterminationMockForGet(airwayId1);
    AirwayDetermination mockAirwayDetermination2 = createAirwayDeterminationMockForGet(airwayId2);
    AirwayDetermination mockAirwayDetermination3 = createAirwayDeterminationMockForGet(airwayId3);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {

      // Mock: 航路情報(指定した飛行目的に基づいてリストを返す)
      mockedStatic
          .when(() -> airwayMapper.selectWithFilters(eq(flightPurposes), isNull(), isNull(),
              isNull(), isNull()))
          .thenReturn(List.of(mockAirway1, mockAirway2));

      // Mock: 航路運営者ID
      mockedStatic.when(() -> fallToleranceRangeMapper.selectForOperatorId("bussinessNumber-0"))
          .thenReturn("1");

      // Mock: 事業者番号
      mockedStatic.when(() -> airwayDeterminationMapper.selectByPrimaryKey(anyInt()))
          .thenReturn(mockAirwayDetermination1, mockAirwayDetermination2, mockAirwayDetermination3);

      // Mock: 航路区画
      mockedStatic.when(() -> airwaySectionMapper.selectByAirwayId(anyString()))
          .thenReturn(createAirwaySectionMockForGet(airwayId1));

      // Mock: ジャンクション
      mockedStatic.when(() -> airwayJunctionMapper.selectByAirwayId(anyString()))
          .thenReturn(createAirwayJunctionMockForGet(airwayId1));

      mockedStatic.when(() -> clock.instant()).thenReturn(Instant.now(Clock.systemUTC()));
      mockedStatic.when(() -> clock.getZone()).thenReturn(ZoneId.of("UTC"));

      // 航路情報取得
      AirwayEntity response = logic.airwayGet(null, false, flightPurposes, null, null, null, null);
      assertNotNull(response);

      // 航路情報の確認
      assertNotNull(response.getAirway());
      assertNotNull(response.getAirway().getAirways());

      // 各飛行目的が含まれていることをアサート
      List<AirwaysEntity> airwaysEntities = response.getAirway().getAirways();
      assertEquals(2, airwaysEntities.size(), "検索結果が２件であること");

      // 各エンティティの飛行目的を確認
      boolean foundPurpose1 = false;
      boolean foundPurpose2 = false;
      boolean foundPurpose3 = false;

      for (AirwaysEntity entity : airwaysEntities) {
        if ("物資運搬".equals(entity.getFlightPurpose())) {
          foundPurpose1 = true;
        }
        if ("送電線点検".equals(entity.getFlightPurpose())) {
          foundPurpose2 = true;
        }
        if ("河川監視".equals(entity.getFlightPurpose())) {
          foundPurpose3 = true;
        }
      }

      assertTrue(foundPurpose1, "選択した飛行目的「物資運搬」が結果に含まれていること");
      assertTrue(foundPurpose2, "選択した飛行目的「送電線点検」が結果に含まれていること");
      assertFalse(foundPurpose3, "選択していない飛行目的「河川監視」が結果に含まれていないこと");
    }
  }

  @Test
  @DisplayName("画定履歴の開始日に基づいたオプション検索が正常に行えること")
  public void testAirwayGetWithDateFromFilter()
      throws JsonProcessingException, NoHandlerFoundException {
    final String airwayId1 = UUID.randomUUID().toString();
    final String airwayId2 = UUID.randomUUID().toString();
    List<String> flightPurposes = List.of("物資運搬");

    // 画定履歴の開始日を設定
    LocalDateTime dateFrom = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    // フォーマット済みの文字列に変換
    String dateFromStr = dateFrom.format(formatter);

    // Mock1: 更新日時がフィルタの対象内
    Airway mockAirway1 = createAirwayMockForGet(airwayId1);
    mockAirway1.setFlightPurpose("物資運搬");
    mockAirway1.setUpdatedAt(dateFrom.plusHours(1)); // 2025-01-01T01:00:00

    // Mock2: 更新日時がフィルタの対象外
    Airway mockAirway2 = createAirwayMockForGet(airwayId2);
    mockAirway2.setFlightPurpose("物資運搬");
    mockAirway2.setUpdatedAt(dateFrom.minusDays(1)); // 2024-12-31T00:00:00

    // 事業者番号をMockする
    AirwayDetermination mockAirwayDetermination1 = createAirwayDeterminationMockForGet(airwayId1);
    AirwayDetermination mockAirwayDetermination2 = createAirwayDeterminationMockForGet(airwayId2);

    // 各モックを再利用可能に設定
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {

      // Mock: 航路情報。フィルタ条件に合致するもののみ返す
      mockedStatic
          .when(() -> airwayMapper.selectWithFilters(eq(flightPurposes), eq(dateFrom), isNull(),
              isNull(), isNull()))
          .thenReturn(List.of(mockAirway1));

      // Mock: 航路運営者ID
      mockedStatic.when(() -> fallToleranceRangeMapper.selectForOperatorId("bussinessNumber-0"))
          .thenReturn("1");

      // Mock: 事業者番号
      mockedStatic.when(() -> airwayDeterminationMapper.selectByPrimaryKey(anyInt()))
          .thenReturn(mockAirwayDetermination1, mockAirwayDetermination2);

      // Mock: 航路区画
      mockedStatic.when(() -> airwaySectionMapper.selectByAirwayId(anyString()))
          .thenReturn(createAirwaySectionMockForGet(airwayId1));

      // Mock: ジャンクション
      mockedStatic.when(() -> airwayJunctionMapper.selectByAirwayId(anyString()))
          .thenReturn(createAirwayJunctionMockForGet(airwayId1));

      mockedStatic.when(() -> clock.instant()).thenReturn(Instant.now(Clock.systemUTC()));
      mockedStatic.when(() -> clock.getZone()).thenReturn(ZoneId.of("UTC"));

      // 航路情報取得
      AirwayEntity response =
          logic.airwayGet(null, false, flightPurposes, dateFromStr, null, null, null);
      assertNotNull(response);

      // 航路情報の確認
      assertNotNull(response.getAirway());
      assertNotNull(response.getAirway().getAirways());

      // 画定履歴開始日以降のデータが1件であること
      List<AirwaysEntity> airwaysEntities = response.getAirway().getAirways();
      assertEquals(1, airwaysEntities.size(), "開始日以降の更新日時を持つ航路が1件であること");

      // エンティティの確認
      AirwaysEntity entity = airwaysEntities.get(0);
      assertEquals(entity.getAirwayId(), airwayId1, "フィルターに合致する航路IDが結果に含まれていること");
      assertEquals("2025-01-01T00:00:00Z", dateFromStr, "日付のフォーマットが正しいこと");
    }
  }

  @Test
  @DisplayName("画定履歴の終了日に基づいたオプション検索が正常に行えること")
  public void testAirwayGetWithDateToFilter()
      throws JsonProcessingException, NoHandlerFoundException {
    final String airwayId1 = UUID.randomUUID().toString();
    final String airwayId2 = UUID.randomUUID().toString();
    List<String> flightPurposes = List.of("物資運搬");

    // 画定履歴の終了日を設定
    LocalDateTime dateTo = LocalDateTime.of(2025, 1, 1, 23, 59, 59, 999_000_000);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    // フォーマット済みの文字列に変換
    String dateToStr = dateTo.format(formatter);

    // Mock1: 更新日時がフィルタの対象内
    Airway mockAirway1 = createAirwayMockForGet(airwayId1);
    mockAirway1.setFlightPurpose("物資運搬");
    mockAirway1.setUpdatedAt(dateTo.minusNanos(999_000_000)); // 2025-01-01T23:59:59

    // Mock2: 更新日時がフィルタの対象外
    Airway mockAirway2 = createAirwayMockForGet(airwayId2);
    mockAirway2.setFlightPurpose("物資運搬");
    mockAirway2.setUpdatedAt(dateTo.plusSeconds(1)); // 2025-01-02T00:00:00

    // 事業者番号をMockする
    AirwayDetermination mockAirwayDetermination1 = createAirwayDeterminationMockForGet(airwayId1);
    AirwayDetermination mockAirwayDetermination2 = createAirwayDeterminationMockForGet(airwayId2);

    // 各モックを再利用可能に設定
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {

      // Mock: 航路情報。フィルタ条件に合致するもののみ返す
      mockedStatic
          .when(() -> airwayMapper.selectWithFilters(eq(flightPurposes), isNull(), eq(dateTo),
              isNull(), isNull()))
          .thenReturn(List.of(mockAirway1));

      // Mock: 航路運営者ID
      mockedStatic.when(() -> fallToleranceRangeMapper.selectForOperatorId("bussinessNumber-0"))
          .thenReturn("1");

      // Mock: 事業者番号
      mockedStatic.when(() -> airwayDeterminationMapper.selectByPrimaryKey(anyInt()))
          .thenReturn(mockAirwayDetermination1, mockAirwayDetermination2);

      // Mock: 航路区画
      mockedStatic.when(() -> airwaySectionMapper.selectByAirwayId(anyString()))
          .thenReturn(createAirwaySectionMockForGet(airwayId1));

      // Mock: ジャンクション
      mockedStatic.when(() -> airwayJunctionMapper.selectByAirwayId(anyString()))
          .thenReturn(createAirwayJunctionMockForGet(airwayId1));

      mockedStatic.when(() -> clock.instant()).thenReturn(Instant.now(Clock.systemUTC()));
      mockedStatic.when(() -> clock.getZone()).thenReturn(ZoneId.of("UTC"));

      // 航路情報取得
      AirwayEntity response =
          logic.airwayGet(null, false, flightPurposes, null, dateToStr, null, null);
      assertNotNull(response);

      // 航路情報の確認
      assertNotNull(response.getAirway());
      assertNotNull(response.getAirway().getAirways());

      // 画定履歴開始日以降のデータが1件であること
      List<AirwaysEntity> airwaysEntities = response.getAirway().getAirways();
      assertEquals(1, airwaysEntities.size(), "終了日以前の更新日時を持つ航路が1件であること");

      // エンティティの確認
      AirwaysEntity entity = airwaysEntities.get(0);
      assertEquals(entity.getAirwayId(), airwayId1, "フィルターに合致する航路IDが結果に含まれていること");
      assertEquals("2025-01-01T23:59:59Z", dateToStr, "日付のフォーマットが正しいこと");
    }
  }

  @Test
  @DisplayName("開始日と終了日が同一日の画定履歴が正常に検索できること")
  public void testAirwayGetWithSameDateFromAndTo()
      throws JsonProcessingException, NoHandlerFoundException {
    final String airwayId1 = UUID.randomUUID().toString();
    final String airwayId2 = UUID.randomUUID().toString();
    final String airwayId3 = UUID.randomUUID().toString();
    final String airwayId4 = UUID.randomUUID().toString();
    List<String> flightPurposes = List.of("物資運搬");

    // 同一日の開始日と終了日を設定（終了日を23:59:59と設定）
    LocalDateTime determinationDateStart = LocalDateTime.of(2020, 1, 11, 0, 0, 0);
    LocalDateTime determinationDateEnd = LocalDateTime.of(2020, 1, 11, 23, 59, 59, 999_000_000);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    // フォーマット済みの文字列に変換
    String determinationDateStartStr = determinationDateStart.format(formatter);
    String determinationDateEndStr = determinationDateEnd.format(formatter);

    // Mock1: 更新日時がフィルタの対象外（2020-01-10T23:59:59Z）
    Airway mockAirway1 = createAirwayMockForGet(airwayId1);
    mockAirway1.setFlightPurpose("物資運搬");
    mockAirway1.setUpdatedAt(determinationDateStart.minusNanos(1)); // 2020-01-10T23:59:59.999

    // Mock2: 更新日時がフィルタの対象内（2020-01-11T00:00:01Z）
    Airway mockAirway2 = createAirwayMockForGet(airwayId2);
    mockAirway2.setFlightPurpose("物資運搬");
    mockAirway2.setUpdatedAt(determinationDateStart.plusSeconds(1)); // 2020-01-11T00:00:01.000

    // Mock3: 更新日時がフィルタの対象内（2020-01-11T23:59:59Z）
    Airway mockAirway3 = createAirwayMockForGet(airwayId3);
    mockAirway3.setFlightPurpose("物資運搬");
    mockAirway3.setUpdatedAt(determinationDateEnd); // 2020-01-11T23:59:59.999

    // Mock4: 更新日時がフィルタの対象外（2020-01-12T00:00:00Z）
    Airway mockAirway4 = createAirwayMockForGet(airwayId4);
    mockAirway4.setFlightPurpose("物資運搬");
    mockAirway4.setUpdatedAt(determinationDateEnd.plusNanos(1)); // 2020-01-12T00:00:00.000

    // 事業者番号をMockする
    AirwayDetermination mockAirwayDetermination2 = createAirwayDeterminationMockForGet(airwayId2);
    AirwayDetermination mockAirwayDetermination3 = createAirwayDeterminationMockForGet(airwayId3);

    // 各モックを再利用可能に設定
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {

      // Mock: 航路情報。フィルタ条件に合致するもののみ返す
      mockedStatic
          .when(() -> airwayMapper.selectWithFilters(eq(flightPurposes), eq(determinationDateStart),
              eq(determinationDateEnd), isNull(), isNull()))
          .thenReturn(List.of(mockAirway2, mockAirway3));

      // Mock: 航路運営者ID
      mockedStatic.when(() -> fallToleranceRangeMapper.selectForOperatorId("bussinessNumber-0"))
          .thenReturn("1");

      // Mock: 事業者番号
      mockedStatic.when(() -> airwayDeterminationMapper.selectByPrimaryKey(anyInt()))
          .thenReturn(mockAirwayDetermination2, mockAirwayDetermination3);

      // Mock: 航路区画
      mockedStatic.when(() -> airwaySectionMapper.selectByAirwayId(anyString()))
          .thenReturn(createAirwaySectionMockForGet(airwayId2));

      // Mock: ジャンクション
      mockedStatic.when(() -> airwayJunctionMapper.selectByAirwayId(anyString()))
          .thenReturn(createAirwayJunctionMockForGet(airwayId2));

      mockedStatic.when(() -> clock.instant()).thenReturn(Instant.now(Clock.systemUTC()));
      mockedStatic.when(() -> clock.getZone()).thenReturn(ZoneId.of("UTC"));

      // 航路情報取得
      AirwayEntity response = logic.airwayGet(null, false, flightPurposes,
          determinationDateStartStr, determinationDateEndStr, null, null);
      assertNotNull(response);

      // 航路情報の確認
      assertNotNull(response.getAirway());
      assertNotNull(response.getAirway().getAirways());

      // 画定履歴開始日から終了日までのデータが2件であること
      List<AirwaysEntity> airwaysEntities = response.getAirway().getAirways();
      assertEquals(2, airwaysEntities.size(), "開始日と終了日が同じ日での更新日時の航路が2件であること");

      // 各エンティティのID確認
      List<String> returnedIds =
          airwaysEntities.stream().map(AirwaysEntity::getAirwayId).collect(Collectors.toList());
      assertTrue(returnedIds.contains(airwayId2), "検索条件に合致する航路ID2が含まれること");
      assertTrue(returnedIds.contains(airwayId3), "検索条件に合致する航路ID3が含まれること");
    }
  }

  @Test
  @DisplayName("事業者番号に基づいたオプション検索が正常に行えること")
  public void testAirwayGetWithBusinessNumberFilter()
      throws JsonProcessingException, NoHandlerFoundException {
    final String airwayId1 = UUID.randomUUID().toString();
    final String airwayId2 = UUID.randomUUID().toString();
    final String airwayId3 = UUID.randomUUID().toString();
    List<String> flightPurposes = List.of("物資運搬");

    // 設定する事業者番号
    String selectedBusinessNumber = "12345"; // ここで事業者番号を宣言
    String nonSelectedBusinessNumber = "67890";

    // Mock1: 選択する事業者番号に関連
    Airway mockAirway1 = createAirwayMockForGet(airwayId1);
    mockAirway1.setFlightPurpose("物資運搬");
    AirwayDetermination mockAirwayDetermination1 = createAirwayDeterminationMockForGet(airwayId1);
    mockAirwayDetermination1.setBusinessNumber(selectedBusinessNumber);

    // Mock2: 選択する事業者番号に関連
    Airway mockAirway2 = createAirwayMockForGet(airwayId2);
    mockAirway2.setFlightPurpose("物資運搬");
    AirwayDetermination mockAirwayDetermination2 = createAirwayDeterminationMockForGet(airwayId2);
    mockAirwayDetermination2.setBusinessNumber(selectedBusinessNumber);

    // Mock3: 選択していない事業者番号に関連
    Airway mockAirway3 = createAirwayMockForGet(airwayId3);
    mockAirway3.setFlightPurpose("物資運搬");
    AirwayDetermination mockAirwayDetermination3 = createAirwayDeterminationMockForGet(airwayId3);
    mockAirwayDetermination3.setBusinessNumber(nonSelectedBusinessNumber);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {

      // Mock: フィルタ条件に合致する航路情報
      mockedStatic.when(() -> airwayMapper.selectWithFilters(eq(flightPurposes), isNull(), isNull(),
          eq(selectedBusinessNumber), isNull())).thenReturn(List.of(mockAirway1, mockAirway2));

      // Mock: 航路運営者ID
      mockedStatic.when(() -> fallToleranceRangeMapper.selectForOperatorId(anyString()))
          .thenReturn("1");

      // Mock: 事業者番号
      mockedStatic.when(() -> airwayDeterminationMapper.selectByPrimaryKey(anyInt()))
          .thenReturn(mockAirwayDetermination1, mockAirwayDetermination2, mockAirwayDetermination3);

      // Mock: 航路区画
      mockedStatic.when(() -> airwaySectionMapper.selectByAirwayId(anyString()))
          .thenReturn(createAirwaySectionMockForGet(airwayId1));

      // Mock: ジャンクション
      mockedStatic.when(() -> airwayJunctionMapper.selectByAirwayId(anyString()))
          .thenReturn(createAirwayJunctionMockForGet(airwayId1));

      mockedStatic.when(() -> clock.instant()).thenReturn(Instant.now(Clock.systemUTC()));
      mockedStatic.when(() -> clock.getZone()).thenReturn(ZoneId.of("UTC"));

      // 航路情報取得
      AirwayEntity response =
          logic.airwayGet(null, false, flightPurposes, null, null, selectedBusinessNumber, null);
      assertNotNull(response);

      // 航路情報の確認
      assertNotNull(response.getAirway());
      assertEquals(selectedBusinessNumber, response.getAirway().getBusinessNumber(),
          "選択した事業者番号が結果に含まれていること");

      // 事業者番号に合致したデータが2件であること
      List<AirwaysEntity> airwaysEntities = response.getAirway().getAirways();
      assertEquals(2, airwaysEntities.size(), "事業者番号に合致する航路が2件であること");

      // 各エンティティの確認
      for (AirwaysEntity entity : airwaysEntities) {
        // 航路に関連する事業者番号が正しいことを確認
        String businessNumber = response.getAirway().getBusinessNumber();
        assertEquals(selectedBusinessNumber, businessNumber, "選択した事業者番号が結果に含まれていること");
        assertNotEquals(nonSelectedBusinessNumber, businessNumber, "選択していない事業者番号が結果に含まれていないこと");
      }
    }
  }

  @Test
  @DisplayName("エリア名に基づいたオプション検索が正常に行えること")
  public void testAirwayGetWithAreaNameFilter()
      throws JsonProcessingException, NoHandlerFoundException {
    final String airwayId1 = UUID.randomUUID().toString();
    final String airwayId2 = UUID.randomUUID().toString();
    final String airwayId3 = UUID.randomUUID().toString();
    List<String> flightPurposes = List.of("物資運搬");

    // エリア名の設定
    String selectedAreaName = "Tokyo";
    String nonSelectedAreaName = "Osaka";

    // Mock1: 選択したエリア名に関連
    Airway mockAirway1 = createAirwayMockForGet(airwayId1);
    mockAirway1.setFlightPurpose("物資運搬");
    AirwayDetermination mockAirwayDetermination1 = createAirwayDeterminationMockForGet(airwayId1);
    mockAirwayDetermination1.setFallToleranceRangeId("range1");

    // Mock2: 選択したエリア名に関連
    Airway mockAirway2 = createAirwayMockForGet(airwayId2);
    mockAirway2.setFlightPurpose("物資運搬");
    AirwayDetermination mockAirwayDetermination2 = createAirwayDeterminationMockForGet(airwayId2);
    mockAirwayDetermination2.setFallToleranceRangeId("range2");

    // Mock3: 選択していないエリア名に関連
    Airway mockAirway3 = createAirwayMockForGet(airwayId3);
    mockAirway3.setFlightPurpose("物資運搬");
    AirwayDetermination mockAirwayDetermination3 = createAirwayDeterminationMockForGet(airwayId3);
    mockAirwayDetermination3.setFallToleranceRangeId("range3");

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {

      // Mock: 航路情報。フィルタ条件に合致するもののみ返す
      mockedStatic
          .when(() -> airwayMapper.selectWithFilters(eq(flightPurposes), isNull(), isNull(),
              isNull(), eq(selectedAreaName)))
          .thenReturn(List.of(mockAirway1, mockAirway2));

      // Mock: 航路運営者ID
      mockedStatic.when(() -> fallToleranceRangeMapper.selectForOperatorId(anyString()))
          .thenReturn("1");

      // Mock: 事業者番号
      mockedStatic.when(() -> airwayDeterminationMapper.selectByPrimaryKey(anyInt()))
          .thenReturn(mockAirwayDetermination1, mockAirwayDetermination2, mockAirwayDetermination3);

      // Mock: 航路区画
      mockedStatic.when(() -> airwaySectionMapper.selectByAirwayId(anyString()))
          .thenReturn(createAirwaySectionMockForGet(airwayId1));

      // Mock: ジャンクション
      mockedStatic.when(() -> airwayJunctionMapper.selectByAirwayId(anyString()))
          .thenReturn(createAirwayJunctionMockForGet(airwayId1));

      mockedStatic.when(() -> clock.instant()).thenReturn(Instant.now(Clock.systemUTC()));
      mockedStatic.when(() -> clock.getZone()).thenReturn(ZoneId.of("UTC"));

      // 航路情報取得
      AirwayEntity response =
          logic.airwayGet(null, false, flightPurposes, null, null, null, selectedAreaName);
      assertNotNull(response);

      // 航路情報の確認
      assertNotNull(response.getAirway());
      assertNotNull(response.getAirway().getAirways());

      // エリア名に合致したデータが2件であること
      List<AirwaysEntity> airwaysEntities = response.getAirway().getAirways();
      assertEquals(2, airwaysEntities.size(), "エリア名に合致する航路が2件であること");

      // 各エンティティのID確認
      List<String> returnedIds =
          airwaysEntities.stream().map(AirwaysEntity::getAirwayId).collect(Collectors.toList());
      assertTrue(returnedIds.contains(airwayId1), "フィルターに合致する航路ID1が結果に含まれていること");
      assertTrue(returnedIds.contains(airwayId2), "フィルターに合致する航路ID2が結果に含まれていること");
    }
  }

  @Test
  @DisplayName("複数条件に基づいたオプション検索が正常に行えること")
  public void testAirwayGetWithMultipleFilters()
      throws JsonProcessingException, NoHandlerFoundException {
    final String airwayId1 = UUID.randomUUID().toString();
    final String airwayId2 = UUID.randomUUID().toString();
    final String airwayId3 = UUID.randomUUID().toString();

    // 複数条件の設定
    List<String> flightPurposes = List.of("物資運搬");
    LocalDateTime dateFrom = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
    LocalDateTime dateTo = LocalDateTime.of(2025, 12, 31, 23, 59, 59, 999_000_000);
    String businessNumber = "12345";
    String areaName = "Tokyo";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    String dateFromStr = dateFrom.format(formatter);
    String dateToStr = dateTo.format(formatter);

    Airway mockAirway1 = createAirwayMockForGet(airwayId1);
    mockAirway1.setFlightPurpose("物資運搬");
    mockAirway1.setUpdatedAt(dateFrom.plusDays(1)); // 2025-01-02
    AirwayDetermination mockAirwayDetermination1 = createAirwayDeterminationMockForGet(airwayId1);
    mockAirwayDetermination1.setBusinessNumber(businessNumber);

    Airway mockAirway2 = createAirwayMockForGet(airwayId2);
    mockAirway2.setFlightPurpose("物資運搬");
    mockAirway2.setUpdatedAt(dateTo.minusDays(1)); // 2025-12-30
    AirwayDetermination mockAirwayDetermination2 = createAirwayDeterminationMockForGet(airwayId2);
    mockAirwayDetermination2.setBusinessNumber(businessNumber);

    Airway mockAirway3 = createAirwayMockForGet(airwayId3);
    mockAirway3.setFlightPurpose("送電線点検");
    mockAirway3.setUpdatedAt(dateFrom.minusDays(1)); // 2024-12-31

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {

      mockedStatic.when(() -> airwayMapper.selectWithFilters(eq(flightPurposes), eq(dateFrom),
          eq(dateTo), eq(businessNumber), eq(areaName)))
          .thenReturn(List.of(mockAirway1, mockAirway2));

      // Mock: 事業者番号の確認
      mockedStatic.when(() -> fallToleranceRangeMapper.selectForOperatorId(anyString()))
          .thenReturn("1");

      // Mock: 事業者番号
      mockedStatic.when(() -> airwayDeterminationMapper.selectByPrimaryKey(anyInt()))
          .thenReturn(mockAirwayDetermination1, mockAirwayDetermination2);

      // Mock: 航路区画
      mockedStatic.when(() -> airwaySectionMapper.selectByAirwayId(anyString()))
          .thenReturn(createAirwaySectionMockForGet(airwayId1));

      // Mock: ジャンクション
      mockedStatic.when(() -> airwayJunctionMapper.selectByAirwayId(anyString()))
          .thenReturn(createAirwayJunctionMockForGet(airwayId1));

      mockedStatic.when(() -> clock.instant()).thenReturn(Instant.now(Clock.systemUTC()));
      mockedStatic.when(() -> clock.getZone()).thenReturn(ZoneId.of("UTC"));

      // 航路情報取得
      AirwayEntity response = logic.airwayGet(null, false, flightPurposes, dateFromStr, dateToStr,
          businessNumber, areaName);
      assertNotNull(response);

      // 航路情報の確認
      assertNotNull(response.getAirway());
      assertEquals(businessNumber, response.getAirway().getBusinessNumber(),
          "選択した事業者番号が結果に含まれていること");

      // 合致したデータが2件であること
      List<AirwaysEntity> airwaysEntities = response.getAirway().getAirways();
      assertEquals(2, airwaysEntities.size(), "複数条件に合致する航路が2件であること");

      // 各エンティティのID確認
      List<String> returnedIds =
          airwaysEntities.stream().map(AirwaysEntity::getAirwayId).collect(Collectors.toList());
      assertTrue(returnedIds.contains(airwayId1), "検索条件に合致する航路ID1が含まれること");
      assertTrue(returnedIds.contains(airwayId2), "検索条件に合致する航路ID2が含まれること");
    }
  }

  @Test
  @DisplayName("論理削除されたデータが検索結果に含まれないこと")
  public void testAirwayGetExcludesLogicallyDeletedRecords()
      throws JsonProcessingException, NoHandlerFoundException {
    final String airwayId1 = UUID.randomUUID().toString();
    final String airwayIdDeleted = UUID.randomUUID().toString();

    // 正常なデータの設定
    Airway mockAirway1 = createAirwayMockForGet(airwayId1);
    mockAirway1.setFlightPurpose("物資運搬");
    LocalDateTime dateFrom = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    String dateFromStr = dateFrom.format(formatter);
    mockAirway1.setUpdatedAt(dateFrom.plusDays(1)); // 2025-01-02

    // 論理削除されたデータの設定
    Airway mockDeletedAirway = createAirwayMockForGet(airwayIdDeleted);
    mockDeletedAirway.setFlightPurpose("物資運搬");
    mockDeletedAirway.setUpdatedAt(dateFrom.plusDays(1)); // 2025-01-02

    // 正常な事業者番号
    AirwayDetermination mockAirwayDetermination1 = createAirwayDeterminationMockForGet(airwayId1);
    mockAirwayDetermination1.setDelete(false);

    // 論理削除された事業者番号の設定
    AirwayDetermination mockDeletedDetermination =
        createAirwayDeterminationMockForGet(airwayIdDeleted);
    mockDeletedDetermination.setDelete(true);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {

      // Mock: フィルタ条件に合致する航路情報。論理削除されたデータは含まない
      mockedStatic.when(() -> airwayMapper.selectWithFilters(anyList(), eq(dateFrom), isNull(),
          isNull(), isNull())).thenReturn(List.of(mockAirway1));

      // Mock: 航路運営者ID
      mockedStatic.when(() -> fallToleranceRangeMapper.selectForOperatorId(anyString()))
          .thenReturn("1");

      // Mock: 事業者番号と削除状態
      mockedStatic.when(() -> airwayDeterminationMapper.selectByPrimaryKey(any()))
          .thenAnswer(invocation -> {
            Integer id = invocation.getArgument(0);
            if (id.equals(mockAirway1.getAirwayDeterminationId())) {
              return mockAirwayDetermination1;
            } else {
              return mockDeletedDetermination;
            }
          });

      // Mock: 航路区画
      mockedStatic.when(() -> airwaySectionMapper.selectByAirwayId(anyString()))
          .thenReturn(createAirwaySectionMockForGet(airwayId1));

      // Mock: ジャンクション
      mockedStatic.when(() -> airwayJunctionMapper.selectByAirwayId(anyString()))
          .thenReturn(createAirwayJunctionMockForGet(airwayId1));

      mockedStatic.when(() -> clock.instant()).thenReturn(Instant.now(Clock.systemUTC()));
      mockedStatic.when(() -> clock.getZone()).thenReturn(ZoneId.of("UTC"));

      // 航路情報取得
      AirwayEntity response =
          logic.airwayGet(null, false, List.of("物資運搬"), dateFromStr, null, null, null);
      assertNotNull(response);

      // 航路情報の確認
      assertNotNull(response.getAirway());
      assertNotNull(response.getAirway().getAirways());

      // 合致したデータが1件のみであること（削除されているデータは含まれない）
      List<AirwaysEntity> airwaysEntities = response.getAirway().getAirways();
      assertEquals(1, airwaysEntities.size(), "論理削除された航路は結果に含まれないこと");

      // エンティティのID確認
      List<String> returnedIds =
          airwaysEntities.stream().map(AirwaysEntity::getAirwayId).collect(Collectors.toList());
      assertTrue(returnedIds.contains(airwayId1), "検索条件に合致する正常な航路IDが結果に含まれていること");
      assertFalse(returnedIds.contains(airwayIdDeleted), "論理削除された航路IDが結果に含まれていないこと");
    }
  }

  @Test
  @DisplayName("飛行目的が指定されていないとき、検索結果は0件であること")
  public void testAirwayGetWithNoFlightPurpose()
      throws JsonProcessingException, NoHandlerFoundException {
    // 飛行目的が指定されていない（nullまたは空のリスト）
    List<String> nullFlightPurpose = null;
    List<String> emptyFlightPurpose = List.of();

    // モックのセットアップ
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {

      // Mock: airwayMapper.selectWithFiltersがデフォルトで何も返さない設定
      mockedStatic.when(
          () -> airwayMapper.selectWithFilters(isNull(), isNull(), isNull(), isNull(), isNull()))
          .thenReturn(Collections.emptyList());

      // nullの場合
      AirwayEntity responseNull = logic.airwayGet(Collections.emptyList(), false, nullFlightPurpose,
          null, null, null, null);
      assertNotNull(responseNull, "nullの飛行目的で結果が返ること");
      assertNotNull(responseNull.getAirway(), "AirwayEntityAirwayが初期化されていること");
      assertTrue(responseNull.getAirway().getAirways().isEmpty(), "nullの飛行目的で航路が0件であること");

      // 空のリストの場合
      AirwayEntity responseEmpty = logic.airwayGet(Collections.emptyList(), false,
          emptyFlightPurpose, null, null, null, null);
      assertNotNull(responseEmpty, "空の飛行目的で結果が返ること");
      assertNotNull(responseEmpty.getAirway(), "AirwayEntityAirwayが初期化されていること");
      assertTrue(responseEmpty.getAirway().getAirways().isEmpty(), "空の飛行目的で航路が0件であること");
    }
  }

  @Test
  @DisplayName("開始日のみ指定されている場合は、検索結果が0件であること")
  public void testAirwayGetWithDateFromWithoutFlightPurpose()
      throws JsonProcessingException, NoHandlerFoundException {

    final String airwayId1 = UUID.randomUUID().toString();
    // 飛行目的が指定されていない（nullまたは空のリスト）
    List<String> nullFlightPurpose = null;
    List<String> emptyFlightPurpose = List.of();

    // 画定履歴の開始日を設定
    LocalDateTime dateFrom = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    // フォーマット済みの文字列に変換
    String dateFromStr = dateFrom.format(formatter);

    // モックデータ設定
    Airway mockAirway = createAirwayMockForGet(airwayId1);
    AirwayDetermination mockAirwayDetermination = createAirwayDeterminationMockForGet(airwayId1);
    mockAirway.setUpdatedAt(dateFrom);

    // 各モックを再利用可能に設定
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {

      // Mock: フィルタ条件に合致する航路情報が返ってこない
      mockedStatic.when(() -> airwayMapper.selectWithFilters(isNull(), eq(dateFrom), isNull(),
          isNull(), isNull())).thenReturn(List.of());

      // Mock: 事業者番号
      mockedStatic.when(() -> airwayDeterminationMapper.selectByPrimaryKey(anyInt()))
          .thenReturn(mockAirwayDetermination);

      // Mock: 航路区画
      mockedStatic.when(() -> airwaySectionMapper.selectByAirwayId(anyString()))
          .thenReturn(createAirwaySectionMockForGet(airwayId1));

      // Mock: ジャンクション
      mockedStatic.when(() -> airwayJunctionMapper.selectByAirwayId(anyString()))
          .thenReturn(createAirwayJunctionMockForGet(airwayId1));

      // nullの場合
      AirwayEntity responseNull =
          logic.airwayGet(null, false, nullFlightPurpose, dateFromStr, null, null, null);
      assertNotNull(responseNull);
      assertNotNull(responseNull.getAirway());
      assertNotNull(responseNull.getAirway().getAirways());
      List<AirwaysEntity> airwaysEntitiesNull = responseNull.getAirway().getAirways();
      assertEquals(0, airwaysEntitiesNull.size(), "飛行目的未指定の場合は結果が0件であること");

      // 空のリストの場合
      AirwayEntity responseEmpty =
          logic.airwayGet(null, false, emptyFlightPurpose, dateFromStr, null, null, null);
      assertNotNull(responseEmpty);
      assertNotNull(responseEmpty.getAirway());
      assertNotNull(responseEmpty.getAirway().getAirways());
      List<AirwaysEntity> airwaysEntitiesEmpty = responseEmpty.getAirway().getAirways();
      assertEquals(0, airwaysEntitiesEmpty.size(), "飛行目的未指定の場合は結果が0件であること");
    }
  }

  @Test
  @DisplayName("終了日のみ指定されている場合は、検索結果が0件であること")
  public void testAirwayGetWithDateToWithoutFlightPurpose()
      throws JsonProcessingException, NoHandlerFoundException {

    final String airwayId1 = UUID.randomUUID().toString();
    // 飛行目的が指定されていない（nullまたは空のリスト）
    List<String> nullFlightPurpose = null;
    List<String> emptyFlightPurpose = List.of();

    // 画定履歴の終了日を設定
    LocalDateTime dateTo = LocalDateTime.of(2025, 1, 1, 23, 59, 59, 999_000_000);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    // フォーマット済みの文字列に変換
    String dateToStr = dateTo.format(formatter);

    // モックデータ設定
    Airway mockAirway = createAirwayMockForGet(airwayId1);
    AirwayDetermination mockAirwayDetermination = createAirwayDeterminationMockForGet(airwayId1);
    mockAirway.setUpdatedAt(dateTo.minusNanos(999_000_000));

    // 各モックを再利用可能に設定
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {

      // Mock: フィルタ条件に合致する航路情報が返ってこない
      mockedStatic.when(
          () -> airwayMapper.selectWithFilters(isNull(), isNull(), eq(dateTo), isNull(), isNull()))
          .thenReturn(List.of());

      // Mock: 事業者番号
      mockedStatic.when(() -> airwayDeterminationMapper.selectByPrimaryKey(anyInt()))
          .thenReturn(mockAirwayDetermination);

      // Mock: 航路区画
      mockedStatic.when(() -> airwaySectionMapper.selectByAirwayId(anyString()))
          .thenReturn(createAirwaySectionMockForGet(airwayId1));

      // Mock: ジャンクション
      mockedStatic.when(() -> airwayJunctionMapper.selectByAirwayId(anyString()))
          .thenReturn(createAirwayJunctionMockForGet(airwayId1));

      // nullの場合
      AirwayEntity responseNull =
          logic.airwayGet(null, false, nullFlightPurpose, null, dateToStr, null, null);
      assertNotNull(responseNull);
      assertNotNull(responseNull.getAirway());
      assertNotNull(responseNull.getAirway().getAirways());
      List<AirwaysEntity> airwaysEntitiesNull = responseNull.getAirway().getAirways();
      assertEquals(0, airwaysEntitiesNull.size(), "飛行目的未指定の場合は結果が0件であること");

      // 空のリストの場合
      AirwayEntity responseEmpty =
          logic.airwayGet(null, false, emptyFlightPurpose, null, dateToStr, null, null);
      assertNotNull(responseEmpty);
      assertNotNull(responseEmpty.getAirway());
      assertNotNull(responseEmpty.getAirway().getAirways());
      List<AirwaysEntity> airwaysEntitiesEmpty = responseEmpty.getAirway().getAirways();
      assertEquals(0, airwaysEntitiesEmpty.size(), "飛行目的未指定の場合は結果が0件であること");
    }
  }

  @Test
  @DisplayName("事業者番号のみ指定されている場合は、検索結果が0件であること")
  public void testAirwayGetWithBusinessNumberWithoutFlightPurpose()
      throws JsonProcessingException, NoHandlerFoundException {

    final String airwayId1 = UUID.randomUUID().toString();
    // 飛行目的が指定されていない（nullまたは空のリスト）
    List<String> nullFlightPurpose = null;
    List<String> emptyFlightPurpose = List.of();

    // 正常な事業者番号を設定
    String businessNumber = "12345";

    // モックデータ設定
    Airway mockAirway = createAirwayMockForGet(airwayId1);
    AirwayDetermination mockAirwayDetermination = createAirwayDeterminationMockForGet(airwayId1);
    mockAirwayDetermination.setBusinessNumber(businessNumber);

    // 各モックを再利用可能に設定
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {

      // Mock: フィルタ条件に合致する航路情報が返ってこない
      mockedStatic
          .when(() -> airwayMapper.selectWithFilters(isNull(), isNull(), isNull(),
              eq(businessNumber), isNull()))
          .thenReturn(List.of());

      // Mock: 事業者番号
      mockedStatic.when(() -> airwayDeterminationMapper.selectByPrimaryKey(anyInt()))
          .thenReturn(mockAirwayDetermination);

      // Mock: 航路区画
      mockedStatic.when(() -> airwaySectionMapper.selectByAirwayId(anyString()))
          .thenReturn(createAirwaySectionMockForGet(airwayId1));

      // Mock: ジャンクション
      mockedStatic.when(() -> airwayJunctionMapper.selectByAirwayId(anyString()))
          .thenReturn(createAirwayJunctionMockForGet(airwayId1));

      // nullの場合
      AirwayEntity responseNull =
          logic.airwayGet(null, false, nullFlightPurpose, null, null, businessNumber, null);
      assertNotNull(responseNull);
      assertNotNull(responseNull.getAirway());
      assertNotNull(responseNull.getAirway().getAirways());
      List<AirwaysEntity> airwaysEntitiesNull = responseNull.getAirway().getAirways();
      assertEquals(0, airwaysEntitiesNull.size(), "飛行目的未指定の場合は結果が0件であること");

      // 空のリストの場合
      AirwayEntity responseEmpty =
          logic.airwayGet(null, false, emptyFlightPurpose, null, null, businessNumber, null);
      assertNotNull(responseEmpty);
      assertNotNull(responseEmpty.getAirway());
      assertNotNull(responseEmpty.getAirway().getAirways());
      List<AirwaysEntity> airwaysEntitiesEmpty = responseEmpty.getAirway().getAirways();
      assertEquals(0, airwaysEntitiesEmpty.size(), "飛行目的未指定の場合は結果が0件であること");
    }
  }

  @Test
  @DisplayName("エリア名のみ指定されている場合は、検索結果が0件であること")
  public void testAirwayGetWithAreaNameWithoutFlightPurpose()
      throws JsonProcessingException, NoHandlerFoundException {

    final String airwayId1 = UUID.randomUUID().toString();
    // 飛行目的が指定されていない（nullまたは空のリスト）
    List<String> nullFlightPurpose = null;
    List<String> emptyFlightPurpose = List.of();

    // エリア名の設定
    String areaName = "Tokyo";

    // モックデータ設定
    Airway mockAirway = createAirwayMockForGet(airwayId1);
    AirwayDetermination mockAirwayDetermination = createAirwayDeterminationMockForGet(airwayId1);
    // mockAirwayDetermination.setBusinessNumber(businessNumber);

    // 各モックを再利用可能に設定
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {

      // Mock: フィルタ条件に合致する航路情報が返ってこない
      mockedStatic.when(() -> airwayMapper.selectWithFilters(isNull(), isNull(), isNull(), isNull(),
          eq(areaName))).thenReturn(List.of());

      // Mock: 事業者番号
      mockedStatic.when(() -> airwayDeterminationMapper.selectByPrimaryKey(anyInt()))
          .thenReturn(mockAirwayDetermination);

      // Mock: 航路区画
      mockedStatic.when(() -> airwaySectionMapper.selectByAirwayId(anyString()))
          .thenReturn(createAirwaySectionMockForGet(airwayId1));

      // Mock: ジャンクション
      mockedStatic.when(() -> airwayJunctionMapper.selectByAirwayId(anyString()))
          .thenReturn(createAirwayJunctionMockForGet(airwayId1));

      // nullの場合
      AirwayEntity responseNull =
          logic.airwayGet(null, false, nullFlightPurpose, null, null, null, areaName);
      assertNotNull(responseNull);
      assertNotNull(responseNull.getAirway());
      assertNotNull(responseNull.getAirway().getAirways());
      List<AirwaysEntity> airwaysEntitiesNull = responseNull.getAirway().getAirways();
      assertEquals(0, airwaysEntitiesNull.size(), "飛行目的未指定の場合は結果が0件であること");

      // 空のリストの場合
      AirwayEntity responseEmpty =
          logic.airwayGet(null, false, emptyFlightPurpose, null, null, null, areaName);
      assertNotNull(responseEmpty);
      assertNotNull(responseEmpty.getAirway());
      assertNotNull(responseEmpty.getAirway().getAirways());
      List<AirwaysEntity> airwaysEntitiesEmpty = responseEmpty.getAirway().getAirways();
      assertEquals(0, airwaysEntitiesEmpty.size(), "飛行目的未指定の場合は結果が0件であること");
    }
  }

  @Test
  @DisplayName("検索条件に合致するデータがない場合、DataNotFoundException が発生すること")
  public void testAirwayGetWithNoMatchingDataThrowsException()
      throws JsonProcessingException, NoHandlerFoundException {
    List<String> flightPurposes = List.of("物資運搬");

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {

      // Mock: 航路情報が何も返さない設定（Empty）
      mockedStatic
          .when(() -> airwayMapper.selectWithFilters(eq(flightPurposes), isNull(), isNull(),
              isNull(), isNull()))
          .thenReturn(Collections.emptyList());

      // テストを実行し、例外を確認
      assertThrows(DataNotFoundException.class, () -> {
        logic.airwayGet(null, false, flightPurposes, null, null, null, null);
      });

      // Mock: 航路情報が何も返さない設定（Null）
      mockedStatic
          .when(() -> airwayMapper.selectWithFilters(eq(flightPurposes), isNull(), isNull(),
              isNull(), isNull()))
          .thenReturn(null);

      // テストを実行し、例外を確認
      assertThrows(DataNotFoundException.class, () -> {
        logic.airwayGet(null, false, flightPurposes, null, null, null, null);
      });
    }
  }

  @Test
  @DisplayName("日時フォーマットが誤っていた場合、適切なエラーメッセージが返却されること")
  public void testOptionSearchByDateTimeParseException() throws Exception {
    List<String> flightPurposes = List.of("物資運搬");
    // 基準期間を設定
    LocalDateTime dateFrom = LocalDateTime.of(2023, 8, 1, 0, 0);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    // フォーマット済みの文字列に変換
    String dateFromStr = dateFrom.format(formatter);

    // モックされた航路データを設定（基準期間内）
    List<Airway> mockAirwaysWithinPeriod = new ArrayList<>();
    Airway airwayIn = new Airway();
    airwayIn.setAirwayId(UUID.randomUUID().toString());
    airwayIn.setAirwayDeterminationId(1);
    airwayIn.setFlightPurpose("物資運搬");

    // airwayMapper の selectWithFilters メソッドをモック
    when(airwayMapper.selectWithFilters(any(), eq(dateFrom), any(), any(), any()))
        .thenReturn(mockAirwaysWithinPeriod);

    AirwayDetermination mockDetermination =
        createAirwayDeterminationMockForGet(UUID.randomUUID().toString());
    when(airwayDeterminationMapper.selectByPrimaryKey(anyInt())).thenReturn(mockDetermination);
    when(fallToleranceRangeMapper.selectForOperatorId("bussinessNumber-0")).thenReturn("1");

    DroneHighwayException exception = assertThrows(DroneHighwayException.class, () -> {
      logic.airwayGet(null, false, flightPurposes, dateFromStr, null, null, null);
    });

    // エラーメッセージの内容確認
    assertEquals("日時のフォーマットが違います", exception.getMessage(), "エラーメッセージが期待通りであること");

    Throwable innerCause = exception.getCause();
    assertNotNull(innerCause, "内側の例外が存在すること");
    assertTrue(innerCause instanceof DateTimeParseException,
        "期待されるDateTimeParseExceptionが内側に存在すること");
  }

  @Test
  @DisplayName("航路削除(航路NotFound)")
  void testDeleteAirwayNotFound() throws JsonProcessingException {
    AirwayDeleteRequestDto requestDto = new AirwayDeleteRequestDto();
    requestDto.setAirwayId("AABBCC_c027b2a6-03b9-413a-b233-722e8bd7b3cb");
    Airway airway = createAirwayMockForGet(requestDto.getAirwayId());

    when(clock.instant()).thenReturn(Instant.now(Clock.systemUTC()));
    when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    when(airwayMapper.selectByPrimaryKey(requestDto.getAirwayId())).thenReturn(null);
    assertThrows(DataNotFoundException.class, () -> logic.delete(requestDto));
  }

  @Test
  @DisplayName("航路削除(航路画定NotFound)")
  void testDeleteAirwayDeterminationNotFound() throws JsonProcessingException {
    AirwayDeleteRequestDto requestDto = new AirwayDeleteRequestDto();
    requestDto.setAirwayId("AABBCC_c027b2a6-03b9-413a-b233-722e8bd7b3cb");
    Airway airway = createAirwayMockForGet(requestDto.getAirwayId());

    when(clock.instant()).thenReturn(Instant.now(Clock.systemUTC()));
    when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    when(airwayMapper.selectByPrimaryKey(requestDto.getAirwayId())).thenReturn(airway);
    when(airwayDeterminationMapper.selectLockByPrimaryKey(airway.getAirwayDeterminationId()))
        .thenReturn(null);
    assertThrows(DataNotFoundException.class, () -> logic.delete(requestDto));
  }

  @Test
  @DisplayName("航路削除(削除済み航路画定NotFound)")
  void testDeleteAirwayDeterminationDeletedNotFound() throws JsonProcessingException {
    AirwayDeleteRequestDto requestDto = new AirwayDeleteRequestDto();
    requestDto.setAirwayId("AABBCC_c027b2a6-03b9-413a-b233-722e8bd7b3cb");
    Airway airway = createAirwayMockForGet(requestDto.getAirwayId());
    AirwayDetermination airwayDetermination = getAirwayDetermination(airway);
    airwayDetermination.setDelete(true);

    when(clock.instant()).thenReturn(Instant.now(Clock.systemUTC()));
    when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    when(airwayMapper.selectByPrimaryKey(requestDto.getAirwayId())).thenReturn(airway);
    when(airwayDeterminationMapper.selectLockByPrimaryKey(airway.getAirwayDeterminationId()))
        .thenReturn(airwayDetermination);
    assertThrows(DataNotFoundException.class, () -> logic.delete(requestDto));
  }
}

