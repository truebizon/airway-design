package com.intent_exchange.drone_highway.logic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intent_exchange.drone_highway.algo.model.DespersionNode;
import com.intent_exchange.drone_highway.algo.model.Drone;
import com.intent_exchange.drone_highway.algo.model.DroneBallisticBuffer;
import com.intent_exchange.drone_highway.algo.model.DroneBullet;
import com.intent_exchange.drone_highway.algo.model.DroneParachute;
import com.intent_exchange.drone_highway.algo.model.DroneSimpleWind;
import com.intent_exchange.drone_highway.algo.model.DroneStaticBuffer;
import com.intent_exchange.drone_highway.config.TestConfig;
import com.intent_exchange.drone_highway.dao.AirwayCompatibleModelsMapper;
import com.intent_exchange.drone_highway.dao.AirwayDeterminationMapper;
import com.intent_exchange.drone_highway.dao.AirwaySequenceMapper;
import com.intent_exchange.drone_highway.dao.FallSpaceMapper;
import com.intent_exchange.drone_highway.dao.FallToleranceRangeMapper;
import com.intent_exchange.drone_highway.dto.request.FallSpaceCrossSectionPostRequestDto;
import com.intent_exchange.drone_highway.dto.request.FallSpaceCrossSectionPostRequestGeometryDto;
import com.intent_exchange.drone_highway.dto.request.FallSpacePostRequestDroneDto;
import com.intent_exchange.drone_highway.dto.request.FallSpacePostRequestDto;
import com.intent_exchange.drone_highway.dto.request.FallToleranceRangePostRequestGeometryDto;
import com.intent_exchange.drone_highway.dto.response.FallSpaceCrossSectionPostResponseDto;
import com.intent_exchange.drone_highway.dto.response.FallSpacePostResponseDto;
import com.intent_exchange.drone_highway.exception.DataNotFoundException;
import com.intent_exchange.drone_highway.exception.DroneHighwayException;
import com.intent_exchange.drone_highway.model.AirwayCompatibleModels;
import com.intent_exchange.drone_highway.model.AirwayDetermination;
import com.intent_exchange.drone_highway.model.FallToleranceRange;
import com.opencsv.exceptions.CsvException;


@SpringBootTest(classes = TestConfig.class)
public class FallSpaceLogicTest {

  @Mock
  private AirwayCompatibleModelsMapper airwayCompatibleModelsMapper;

  @InjectMocks
  private FallSpaceLogic fallSpaceLogic;

  @BeforeEach
  public void setUp() {
    // csvFilePath の設定
    ReflectionTestUtils.setField(fallSpaceLogic, "csvFilePath", "../test/data/testFallSpace/");
  }

  @Test
  @DisplayName("最大落下許容範囲をライブラリ用に正しく変換すること")
  void testConvertToModelFallToleranceRange() throws Exception {
    // JSON文字列を準備
    String jsonString =
        "{\"type\":\"Polygon\",\"coordinates\":[[[130.419919026, 33.425986891], [130.421901286, 33.425507951], [130.425318213, 33.433692723], [130.420597168, 33.434171379], [130.419919026,33.425986891]]]}";

    // プロジェクトのFallToleranceRangeインスタンスを作成
    FallToleranceRange range = new FallToleranceRange();
    range.setJson(jsonString);

    Method method = FallSpaceLogic.class.getDeclaredMethod("convertToModelFallToleranceRange",
        FallToleranceRange.class);
    method.setAccessible(true);

    // メソッドを呼び出してライブラリのFallToleranceRangeを得る
    com.intent_exchange.drone_highway.algo.model.FallToleranceRange result =
        (com.intent_exchange.drone_highway.algo.model.FallToleranceRange) method
            .invoke(new FallSpaceLogic(), range);

    // 結果が返ってきていること
    assertNotNull(result);

    // typeがポリゴンであること
    assertEquals("Polygon", result.getType());

    // coordinatesに値が入ってきていること
    String expectedCoordinates = "[[[130.419919026, 33.425986891], "
        + "[130.421901286, 33.425507951], " + "[130.425318213, 33.433692723], "
        + "[130.420597168, 33.434171379], " + "[130.419919026, 33.425986891]]]";
    assertEquals(expectedCoordinates, result.getCoordinates().toString());

    // 各ポイントの詳細な検証
    List<List<List<BigDecimal>>> coordinates = result.getCoordinates();
    assertEquals(1, coordinates.size(), "トップレベルのリストのサイズが正しいこと");

    List<List<BigDecimal>> polygon = coordinates.get(0);
    assertEquals(5, polygon.size(), "ポリゴンの頂点数が期待通りであること");

    // 各ポイントの具体的な値を確認
    assertListOfBigDecimalEquals(polygon.get(0), 130.419919026, 33.425986891);
    assertListOfBigDecimalEquals(polygon.get(1), 130.421901286, 33.425507951);
    assertListOfBigDecimalEquals(polygon.get(2), 130.425318213, 33.433692723);
    assertListOfBigDecimalEquals(polygon.get(3), 130.420597168, 33.434171379);
    assertListOfBigDecimalEquals(polygon.get(4), 130.419919026, 33.425986891);
  }

  @Test
  @DisplayName("空のJSON文字列でJsonMappingExceptionが発生した場合に適切なエラーメッセージが返されること")
  void testConvertToModelFallToleranceRangeWithJsonMappingError() throws Exception {
    // Jsonを空で設定
    String malformedJsonString = "";

    FallToleranceRange range = new FallToleranceRange();
    range.setJson(malformedJsonString);

    Method method = FallSpaceLogic.class.getDeclaredMethod("convertToModelFallToleranceRange",
        FallToleranceRange.class);
    method.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(new FallSpaceLogic(), range);
    });

    Throwable cause = exception.getTargetException();
    assertTrue(cause instanceof DroneHighwayException);

    Throwable innerCause = cause.getCause();
    assertNotNull(innerCause);
    assertTrue(innerCause instanceof JsonMappingException);
    assertEquals("JSONマッピングエラーが発生しました", cause.getMessage());
  }

  @Test
  @DisplayName("無効なJSON形式でJsonProcessingExceptionが発生した場合に適切なエラーメッセージが返されること")
  void testConvertToModelFallToleranceRangeParserError() throws Exception {
    // 不正なJSON文字列を設定
    String invalidJsonString = "{\"type\":\"Polygon\",\"coordinates\":[[[AAA.AAA, BBB.BBB]]]}";

    FallToleranceRange range = new FallToleranceRange();
    range.setJson(invalidJsonString);

    Method method = FallSpaceLogic.class.getDeclaredMethod("convertToModelFallToleranceRange",
        FallToleranceRange.class);
    method.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(new FallSpaceLogic(), range);
    });

    Throwable cause = exception.getTargetException();
    assertTrue(cause instanceof DroneHighwayException);

    Throwable innerCause = cause.getCause();
    assertNotNull(innerCause);
    assertTrue(cause.getCause() instanceof JsonProcessingException);
    assertEquals("JSON処理エラーが発生しました", cause.getMessage());
  }

  @Test
  @DisplayName("DBから最大落下許容範囲が取得できなかった場合に適切なエラーメッセージが返されること")
  public void testCoordListEmptyHandling() throws NoSuchMethodException, SecurityException,
      IllegalAccessException, InvocationTargetException {
    // coordinatesを空で設定
    String nullJsonString = "{\"type\":\"Polygon\",\"coordinates\":null}";

    FallToleranceRange range = new FallToleranceRange();
    range.setJson(nullJsonString);

    Method method = FallSpaceLogic.class.getDeclaredMethod("convertToModelFallToleranceRange",
        FallToleranceRange.class);
    method.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(new FallSpaceLogic(), range);
    });

    Throwable cause = exception.getTargetException();
    assertTrue(cause instanceof DroneHighwayException);
    assertEquals("最大落下許容範囲をDBから取得できませんでした", cause.getMessage());
  }

  @Test
  @DisplayName("無効な数値形式でNumberFormatExceptionが発生した場合に適切なエラーメッセージが返されること")
  void testConvertToModelFallToleranceRangeWithNumberFormatException() throws Exception {
    // coordinates の要素として無効な文字列 "invalid" を配置
    String malformedJsonString =
        "{\"type\":\"Polygon\",\"coordinates\": [[[\"invalid\", \"123.456\"]]]}";

    FallToleranceRange range = new FallToleranceRange();
    range.setJson(malformedJsonString);

    Method method = FallSpaceLogic.class.getDeclaredMethod("convertToModelFallToleranceRange",
        FallToleranceRange.class);
    method.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(new FallSpaceLogic(), range);
    });

    Throwable cause = exception.getTargetException();
    assertTrue(cause instanceof DroneHighwayException);

    Throwable innerCause = cause.getCause();
    assertNotNull(innerCause);
    assertTrue(innerCause instanceof NumberFormatException);
    assertEquals("数値変換エラーです: invalid", cause.getMessage());
  }

  @Test
  @DisplayName("誤った型情報でClassCastExceptionが発生した場合に適切なエラーメッセージが返されること")
  void testConvertToModelFallToleranceRangeWithInvalidCoordinates() throws Exception {
    String malformedJsonString = "{\"type\":\"Polygon\",\"coordinates\": \"not a list\"}";

    FallToleranceRange range = new FallToleranceRange();
    range.setJson(malformedJsonString);

    Method method = FallSpaceLogic.class.getDeclaredMethod("convertToModelFallToleranceRange",
        FallToleranceRange.class);
    method.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(new FallSpaceLogic(), range);
    });

    Throwable cause = exception.getTargetException();
    assertTrue(cause instanceof DroneHighwayException);

    Throwable innerCause = cause.getCause();
    assertNotNull(innerCause);
    assertTrue(innerCause instanceof ClassCastException);
    assertEquals("型が期待通りではありません", cause.getMessage());
  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("CSVからパラシュートドローンモデルが正常に生成されること")
  public void testCreateDroneParachuteFromCsv() throws IOException, CsvException,
      NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {
    int airwayDeterminationId = 1;
    List<AirwayCompatibleModels> mockModels = List
        .of(createMockAirwayCompatibleModels(airwayDeterminationId, "メーカー1", "型番1_1", "パラシュートモデル"));
    when(airwayCompatibleModelsMapper.selectByAirwayDeterminationId(eq(airwayDeterminationId)))
        .thenReturn(mockModels);

    Method method = FallSpaceLogic.class.getDeclaredMethod("getDronesForLibrary", int.class);
    method.setAccessible(true);
    List<Drone> drones = (List<Drone>) method.invoke(fallSpaceLogic, airwayDeterminationId);

    assertEquals(1, drones.size());
    Drone drone = drones.get(0);

    // パラシュートモデルであることを確認
    assertEquals(DroneParachute.class, drone.getClass());

    DroneParachute parachute = (DroneParachute) drone;
    assertEquals("メーカー1", parachute.getMaker());
    assertEquals("型番1_1", parachute.getModelNumber());
    assertEquals(3, parachute.getVerticalSpeed()); // 鉛直速度
    assertEquals(5, parachute.getMaxWindSpeed()); // 最大風速
  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("CSVから弾丸ドローンモデルが正常に生成されること")
  public void testCreateDroneBulletlFromCsv() throws IOException, CsvException,
      NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {
    int airwayDeterminationId = 1;
    List<AirwayCompatibleModels> mockModels =
        List.of(createMockAirwayCompatibleModels(airwayDeterminationId, "メーカー2", "型番2_1", "弾丸モデル"));
    when(airwayCompatibleModelsMapper.selectByAirwayDeterminationId(eq(airwayDeterminationId)))
        .thenReturn(mockModels);

    Method method = FallSpaceLogic.class.getDeclaredMethod("getDronesForLibrary", int.class);
    method.setAccessible(true);
    List<Drone> drones = (List<Drone>) method.invoke(fallSpaceLogic, airwayDeterminationId);

    assertEquals(1, drones.size());
    Drone drone = drones.get(0);

    // 弾丸モデルであることを確認
    assertEquals(DroneBullet.class, drone.getClass());

    DroneBullet bullet = (DroneBullet) drone;
    assertEquals("メーカー2", bullet.getMaker());
    assertEquals("型番2_1", bullet.getModelNumber());
    assertEquals(0.95, bullet.getCharacteristicDimension()); // 機体の最大長
  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("CSVから簡易風速ドローンモデルが正常に生成されること")
  public void testCreateDroneSimpleWindFromCsv() throws IOException, CsvException,
      NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {
    int airwayDeterminationId = 1;
    List<AirwayCompatibleModels> mockModels =
        List.of(createMockAirwayCompatibleModels(airwayDeterminationId, "メーカー3", "型番3_1",
            "簡易風速モデル"));
    when(airwayCompatibleModelsMapper.selectByAirwayDeterminationId(eq(airwayDeterminationId)))
        .thenReturn(mockModels);

    Method method = FallSpaceLogic.class.getDeclaredMethod("getDronesForLibrary", int.class);
    method.setAccessible(true);
    List<Drone> drones = (List<Drone>) method.invoke(fallSpaceLogic, airwayDeterminationId);

    assertEquals(1, drones.size());
    Drone drone = drones.get(0);

    // 簡易風速モデルであることを確認
    assertEquals(DroneSimpleWind.class, drone.getClass());

    DroneSimpleWind simpleWind = (DroneSimpleWind) drone;
    assertEquals("メーカー3", simpleWind.getMaker());
    assertEquals("型番3_1", simpleWind.getModelNumber());
    assertEquals(10, simpleWind.getMaxWindSpeed()); // 最大風速
    assertEquals(17, simpleWind.getMaxFlightSpeed()); // 最大速度
  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("CSVから弾道バッファモデルが正常に生成されること")
  public void testCreateDroneBallisticBufferFromCsv() throws IOException, CsvException,
      NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {
    int airwayDeterminationId = 1;
    List<AirwayCompatibleModels> mockModels = List
        .of(createMockAirwayCompatibleModels(airwayDeterminationId, "メーカー4", "型番4_1", "弾道バッファモデル"));
    when(airwayCompatibleModelsMapper.selectByAirwayDeterminationId(eq(airwayDeterminationId)))
        .thenReturn(mockModels);

    Method method = FallSpaceLogic.class.getDeclaredMethod("getDronesForLibrary", int.class);
    method.setAccessible(true);
    List<Drone> drones = (List<Drone>) method.invoke(fallSpaceLogic, airwayDeterminationId);

    assertEquals(1, drones.size());
    Drone drone = drones.get(0);

    // 弾道バッファモデルであることを確認
    assertEquals(DroneBallisticBuffer.class, drone.getClass());

    DroneBallisticBuffer ballisticBuffer = (DroneBallisticBuffer) drone;
    assertEquals("メーカー4", ballisticBuffer.getMaker());
    assertEquals("型番4_1", ballisticBuffer.getModelNumber());
    assertEquals(1.7, ballisticBuffer.getCharacteristicDimension()); // 機体の最大長
    assertEquals(15, ballisticBuffer.getStaticBuffer()); // 固定バッファ
  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("CSVから固定バッファモデルが正常に生成されること")
  public void testCreateDroneStaticBufferFromCsvv() throws IOException, CsvException,
      NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {
    int airwayDeterminationId = 1;
    List<AirwayCompatibleModels> mockModels = List
        .of(createMockAirwayCompatibleModels(airwayDeterminationId, "メーカー5", "型番5_1", "固定バッファモデル"));
    when(airwayCompatibleModelsMapper.selectByAirwayDeterminationId(eq(airwayDeterminationId)))
        .thenReturn(mockModels);

    Method method = FallSpaceLogic.class.getDeclaredMethod("getDronesForLibrary", int.class);
    method.setAccessible(true);
    List<Drone> drones = (List<Drone>) method.invoke(fallSpaceLogic, airwayDeterminationId);

    assertEquals(1, drones.size());
    Drone drone = drones.get(0);

    // 固定バッファモデルであることを確認
    assertEquals(DroneStaticBuffer.class, drone.getClass());

    DroneStaticBuffer staticBuffer = (DroneStaticBuffer) drone;
    assertEquals("メーカー5", staticBuffer.getMaker());
    assertEquals("型番5_1", staticBuffer.getModelNumber());
    assertEquals(15, staticBuffer.getStaticBuffer()); // 固定バッファ
  }

  @Test
  @DisplayName("航路画定IDに対応するデータがDBに存在しない場合に例外が発生すること")
  public void testAirwayCompatibleModelsNotFound()
      throws NoSuchMethodException, IllegalAccessException {
    // 存在しないダミーの航路画定IDを設定
    int airwayDeterminationId = 99;

    when(airwayCompatibleModelsMapper.selectByAirwayDeterminationId(eq(airwayDeterminationId)))
        .thenReturn(null);

    Method method = FallSpaceLogic.class.getDeclaredMethod("getDronesForLibrary", int.class);
    method.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(fallSpaceLogic, airwayDeterminationId);
    });

    assertTrue(thrown.getTargetException() instanceof DataNotFoundException);
  }

  @Test
  @DisplayName("落下モデルが指定されていない場合に適切なエラーメッセージが返されること")
  public void testFallingModelUnknown() throws NoSuchMethodException, IllegalAccessException {
    int airwayDeterminationId = 3;
    List<AirwayCompatibleModels> mockModels = List.of(
        createMockAirwayCompatibleModels(airwayDeterminationId, "メーカー91", "型番91_1", "落下モデル未指定"));
    when(airwayCompatibleModelsMapper.selectByAirwayDeterminationId(eq(airwayDeterminationId)))
        .thenReturn(mockModels);

    // 例外テスト用のファイルパスを設定
    ReflectionTestUtils.setField(fallSpaceLogic, "csvFilePath",
        "../test/data/testFallSpace/error/");

    Method method = FallSpaceLogic.class.getDeclaredMethod("getDronesForLibrary", int.class);
    method.setAccessible(true);

    Throwable thrown = assertThrows(InvocationTargetException.class,
        () -> method.invoke(fallSpaceLogic, airwayDeterminationId)).getCause();

    if (thrown instanceof DroneHighwayException exception) {
      assertEquals("落下モデルの読み込みに失敗しました", exception.getMessage());
    } else {
      throw new AssertionError("Expected DroneHighwayException to be thrown");
    }
  }

  @Test
  @DisplayName("製造メーカー名と型式が一致している機体が存在しない場合に適切なエラーメッセージが返されること")
  public void testFallingModelNotFound() throws IOException, CsvException, NoSuchMethodException,
      SecurityException, IllegalAccessException, InvocationTargetException {
    int airwayDeterminationId = 4;
    List<AirwayCompatibleModels> mockModels = List
        .of(createMockAirwayCompatibleModels(airwayDeterminationId, "メーカー1", "型番2_1", "メーカー型式不一致"));
    when(airwayCompatibleModelsMapper.selectByAirwayDeterminationId(eq(airwayDeterminationId)))
        .thenReturn(mockModels);

    Method method = FallSpaceLogic.class.getDeclaredMethod("getDronesForLibrary", int.class);
    method.setAccessible(true);

    Throwable thrown = assertThrows(InvocationTargetException.class,
        () -> method.invoke(fallSpaceLogic, airwayDeterminationId)).getCause();

    if (thrown instanceof DroneHighwayException exception) {
      assertEquals("指定されたメーカー：メーカー1 と型式：型番2_1 の機体データが見つかりませんでした", exception.getMessage());
    } else {
      throw new AssertionError("Expected DroneHighwayException to be thrown");
    }
  }

  @Test
  @DisplayName("機体コンフィグCSVファイルが見つからない場合に適切なエラーメッセージが返されること")
  public void testDroneConfigCsvFileNotFound()
      throws NoSuchMethodException, IllegalAccessException {
    int airwayDeterminationId = 1;
    List<AirwayCompatibleModels> mockModels =
        List.of(createMockAirwayCompatibleModels(airwayDeterminationId, "メーカー1", "型番1_1", "機種1"));
    when(airwayCompatibleModelsMapper.selectByAirwayDeterminationId(eq(airwayDeterminationId)))
        .thenReturn(mockModels);

    // 無効なファイルパスを設定
    ReflectionTestUtils.setField(fallSpaceLogic, "csvFilePath", "invalid/path/");

    Method method = FallSpaceLogic.class.getDeclaredMethod("getDronesForLibrary", int.class);
    method.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(fallSpaceLogic, airwayDeterminationId);
    });
    Throwable cause = exception.getTargetException();
    assertTrue(cause instanceof DroneHighwayException);
    assertTrue(cause.getCause() instanceof FileNotFoundException);
    assertEquals("機体データのCSVファイルが見つかりませんでした", cause.getMessage());
  }

  @Test
  @DisplayName("機体情報の読み込みに失敗した場合に適切なエラーメッセージが返されること")
  public void testDroneConfigCsvArrayIndexOutOfBoundsException()
      throws NoSuchMethodException, IllegalAccessException {
    int airwayDeterminationId = 1;
    List<AirwayCompatibleModels> mockModels = List
        .of(createMockAirwayCompatibleModels(airwayDeterminationId, "メーカー92", "型番92_1", "カラム数不一致"));
    when(airwayCompatibleModelsMapper.selectByAirwayDeterminationId(eq(airwayDeterminationId)))
        .thenReturn(mockModels);

    // 例外テスト用のファイルパスを設定
    ReflectionTestUtils.setField(fallSpaceLogic, "csvFilePath",
        "../test/data/testFallSpace/error/");

    Method method = FallSpaceLogic.class.getDeclaredMethod("getDronesForLibrary", int.class);
    method.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(fallSpaceLogic, airwayDeterminationId);
    });
    Throwable cause = exception.getTargetException();
    assertTrue(cause instanceof DroneHighwayException);
    assertTrue(cause.getCause() instanceof ArrayIndexOutOfBoundsException);
    assertTrue(cause.getMessage().contains("機体データの読み込みに失敗しました"));
  }

  @Test
  @DisplayName("パラシュートモデルCSVファイルが見つからない場合に適切なエラーメッセージが返されること")
  public void testParachuteCsvFileNotFound() throws NoSuchMethodException, IllegalAccessException {
    String maker = "メーカー1";
    String modelNumber = "型番1_1";
    String name = "パラシュートモデル1";

    // 無効なファイルパスを設定
    ReflectionTestUtils.setField(fallSpaceLogic, "csvFilePath", "invalid/path/");

    Method method = FallSpaceLogic.class.getDeclaredMethod("createDroneParachuteFromCsv",
        String.class, String.class, String.class);
    method.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(fallSpaceLogic, maker, modelNumber, name);
    });

    Throwable cause = exception.getTargetException();
    assertTrue(cause instanceof DroneHighwayException);
    assertTrue(cause.getCause() instanceof FileNotFoundException);
    assertEquals("パラシュートモデルのCSVファイルが見つかりませんでした", cause.getMessage());
  }

  @Test
  @DisplayName("弾丸モデルCSVファイルが見つからない場合に適切なエラーメッセージが返されること")
  public void testBulletCsvFileNotFound() throws NoSuchMethodException, IllegalAccessException {
    String maker = "メーカー2";
    String modelNumber = "型番2_1";
    String name = "弾丸モデル";

    // 無効なファイルパスを設定
    ReflectionTestUtils.setField(fallSpaceLogic, "csvFilePath", "invalid/path/");


    Method method = FallSpaceLogic.class.getDeclaredMethod("createDroneBulletFromCsv", String.class,
        String.class, String.class);
    method.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(fallSpaceLogic, maker, modelNumber, name);
    });

    Throwable cause = exception.getTargetException();
    assertTrue(cause instanceof DroneHighwayException);
    assertTrue(cause.getCause() instanceof FileNotFoundException);
    assertEquals("弾丸モデルのCSVファイルが見つかりませんでした", cause.getMessage());
  }

  @Test
  @DisplayName("簡易風速モデルCSVファイルが見つからない場合に適切なエラーメッセージが返されること")
  public void testSimpleWindCsvFileNotFound() throws NoSuchMethodException, IllegalAccessException {
    String maker = "メーカー3";
    String modelNumber = "型番3_1";
    String name = "簡易風速モデル";

    // 無効なファイルパスを設定
    ReflectionTestUtils.setField(fallSpaceLogic, "csvFilePath", "invalid/path/");


    Method method = FallSpaceLogic.class.getDeclaredMethod("createDroneSimpleWindFromCsv",
        String.class, String.class, String.class);
    method.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(fallSpaceLogic, maker, modelNumber, name);
    });

    Throwable cause = exception.getTargetException();
    assertTrue(cause instanceof DroneHighwayException);
    assertTrue(cause.getCause() instanceof FileNotFoundException);
    assertEquals("簡易風速モデルのCSVファイルが見つかりませんでした", cause.getMessage());
  }

  @Test
  @DisplayName("弾道バッファモデルCSVファイルが見つからない場合に適切なエラーメッセージが返されること")
  public void testBallisticBufferCsvFileNotFound()
      throws NoSuchMethodException, IllegalAccessException {
    String maker = "メーカー4";
    String modelNumber = "型番4_1";
    String name = "弾道バッファモデル";

    // 無効なファイルパスを設定
    ReflectionTestUtils.setField(fallSpaceLogic, "csvFilePath", "invalid/path/");


    Method method = FallSpaceLogic.class.getDeclaredMethod("createDroneBallisticBufferFromCsv",
        String.class, String.class, String.class);
    method.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(fallSpaceLogic, maker, modelNumber, name);
    });

    Throwable cause = exception.getTargetException();
    assertTrue(cause instanceof DroneHighwayException);
    assertTrue(cause.getCause() instanceof FileNotFoundException);
    assertEquals("弾道バッファモデルのCSVファイルが見つかりませんでした", cause.getMessage());
  }

  @Test
  @DisplayName("固定バッファモデルCSVファイルが見つからない場合に適切なエラーメッセージが返されること")
  public void testStaticBufferCsvFileNotFound()
      throws NoSuchMethodException, IllegalAccessException {
    String maker = "メーカー5";
    String modelNumber = "型番5_1";
    String name = "固定バッファモデル";

    // 無効なファイルパスを設定
    ReflectionTestUtils.setField(fallSpaceLogic, "csvFilePath", "invalid/path/");


    Method method = FallSpaceLogic.class.getDeclaredMethod("createDroneStaticBufferFromCsv",
        String.class, String.class, String.class);
    method.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(fallSpaceLogic, maker, modelNumber, name);
    });

    Throwable cause = exception.getTargetException();
    assertTrue(cause instanceof DroneHighwayException);
    assertTrue(cause.getCause() instanceof FileNotFoundException);
    assertEquals("固定バッファモデルのCSVファイルが見つかりませんでした", cause.getMessage());
  }

  @Test
  @DisplayName("パラシュートモデル情報の読み込みに失敗した場合に適切なエラーメッセージが返されること")
  public void testParachuteCsvArrayIndexOutOfBoundsException()
      throws NoSuchMethodException, IllegalAccessException {
    String maker = "メーカー1";
    String modelNumber = "型番1_1";
    String name = "カラム数不一致";

    // 例外テスト用のファイルパスを設定
    ReflectionTestUtils.setField(fallSpaceLogic, "csvFilePath",
        "../test/data/testFallSpace/error/");

    Method method = FallSpaceLogic.class.getDeclaredMethod("createDroneParachuteFromCsv",
        String.class, String.class, String.class);
    method.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(fallSpaceLogic, maker, modelNumber, name);
    });
    Throwable cause = exception.getTargetException();
    assertTrue(cause instanceof DroneHighwayException);

    Throwable innerCause = cause.getCause();
    assertNotNull(innerCause);
    assertTrue(cause.getCause() instanceof ArrayIndexOutOfBoundsException);
    assertTrue(cause.getMessage().contains("パラシュートモデル情報の読み込みに失敗しました"));
  }

  @Test
  @DisplayName("弾丸モデル情報の読み込みに失敗した場合に適切なエラーメッセージが返されること")
  public void testBulletCsvArrayIndexOutOfBoundsException()
      throws NoSuchMethodException, IllegalAccessException {
    String maker = "メーカー2";
    String modelNumber = "型番2_1";
    String name = "カラム数不一致";

    // 例外テスト用のファイルパスを設定
    ReflectionTestUtils.setField(fallSpaceLogic, "csvFilePath",
        "../test/data/testFallSpace/error/");

    Method method = FallSpaceLogic.class.getDeclaredMethod("createDroneBulletFromCsv", String.class,
        String.class, String.class);
    method.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(fallSpaceLogic, maker, modelNumber, name);
    });
    Throwable cause = exception.getTargetException();
    assertTrue(cause instanceof DroneHighwayException);

    Throwable innerCause = cause.getCause();
    assertNotNull(innerCause);
    assertTrue(cause.getCause() instanceof ArrayIndexOutOfBoundsException);
    assertTrue(cause.getMessage().contains("弾丸モデル情報の読み込みに失敗しました"));
  }

  @Test
  @DisplayName("簡易風速モデル情報の読み込みに失敗した場合に適切なエラーメッセージが返されること")
  public void testSimpleWindCsvArrayIndexOutOfBoundsException()
      throws NoSuchMethodException, IllegalAccessException {
    String maker = "メーカー3";
    String modelNumber = "型番3_1";
    String name = "カラム数不一致";

    // 例外テスト用のファイルパスを設定
    ReflectionTestUtils.setField(fallSpaceLogic, "csvFilePath",
        "../test/data/testFallSpace/error/");

    Method method = FallSpaceLogic.class.getDeclaredMethod("createDroneSimpleWindFromCsv",
        String.class, String.class, String.class);
    method.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(fallSpaceLogic, maker, modelNumber, name);
    });
    Throwable cause = exception.getTargetException();
    assertTrue(cause instanceof DroneHighwayException);

    Throwable innerCause = cause.getCause();
    assertNotNull(innerCause);
    assertTrue(cause.getCause() instanceof ArrayIndexOutOfBoundsException);
    assertTrue(cause.getMessage().contains("簡易風速モデル情報の読み込みに失敗しました"));
  }

  @Test
  @DisplayName("弾道バッファモデル情報の読み込みに失敗した場合に適切なエラーメッセージが返されること")
  public void testBallisticBufferCsvArrayIndexOutOfBoundsException()
      throws NoSuchMethodException, IllegalAccessException {
    String maker = "メーカー4";
    String modelNumber = "型番4_1";
    String name = "カラム数不一致";

    // 例外テスト用のファイルパスを設定
    ReflectionTestUtils.setField(fallSpaceLogic, "csvFilePath",
        "../test/data/testFallSpace/error/");

    Method method = FallSpaceLogic.class.getDeclaredMethod("createDroneBallisticBufferFromCsv",
        String.class, String.class, String.class);
    method.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(fallSpaceLogic, maker, modelNumber, name);
    });
    Throwable cause = exception.getTargetException();
    assertTrue(cause instanceof DroneHighwayException);

    Throwable innerCause = cause.getCause();
    assertNotNull(innerCause);
    assertTrue(cause.getCause() instanceof ArrayIndexOutOfBoundsException);
    assertTrue(cause.getMessage().contains("弾道バッファモデル情報の読み込みに失敗しました"));
  }

  @Test
  @DisplayName("固定バッファモデル情報の読み込みに失敗した場合に適切なエラーメッセージが返されること")
  public void testStaticBufferCsvArrayIndexOutOfBoundsException()
      throws NoSuchMethodException, IllegalAccessException {
    String maker = "メーカー5";
    String modelNumber = "型番5_1";
    String name = "カラム数不一致";

    // 例外テスト用のファイルパスを設定
    ReflectionTestUtils.setField(fallSpaceLogic, "csvFilePath",
        "../test/data/testFallSpace/error/");

    Method method = FallSpaceLogic.class.getDeclaredMethod("createDroneStaticBufferFromCsv",
        String.class, String.class, String.class);
    method.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(fallSpaceLogic, maker, modelNumber, name);
    });
    Throwable cause = exception.getTargetException();
    assertTrue(cause instanceof DroneHighwayException);

    Throwable innerCause = cause.getCause();
    assertNotNull(innerCause);
    assertTrue(cause.getCause() instanceof ArrayIndexOutOfBoundsException);
    assertTrue(cause.getMessage().contains("固定バッファモデル情報の読み込みに失敗しました"));
  }

  @Test
  @DisplayName("指定されたメーカーと型式のパラシュートモデルが見つからない場合に適切なエラーメッセージが返されること")
  public void testParachuteModelNotFound() throws NoSuchMethodException {
    String maker = "メーカー1";
    String modelNumber = "型番1_2";
    String name = "パラメータ情報が存在しないデータ";

    // 例外テスト用のファイルパスを設定
    String invalidPath = "../test/data/testFallSpace/error/";
    ReflectionTestUtils.setField(fallSpaceLogic, "csvFilePath", invalidPath);

    Method method = FallSpaceLogic.class.getDeclaredMethod("createDroneParachuteFromCsv",
        String.class, String.class, String.class);
    method.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(fallSpaceLogic, maker, modelNumber, name);
    });

    Throwable cause = exception.getTargetException();
    assertTrue(cause instanceof DroneHighwayException);
    assertTrue(cause.getMessage().contains("指定されたメーカー：メーカー1 と型式：型番1_2 のパラシュートモデルが見つかりませんでした"));
  }

  @Test
  @DisplayName("指定されたメーカーと型式の弾丸モデルが見つからない場合に適切なエラーメッセージが返されること")
  public void testBulletModelNotFound() throws NoSuchMethodException {
    String maker = "メーカー2";
    String modelNumber = "型番2_2";
    String name = "パラメータ情報が存在しないデータ";

    // 例外テスト用のファイルパスを設定
    String invalidPath = "../test/data/testFallSpace/error/";
    ReflectionTestUtils.setField(fallSpaceLogic, "csvFilePath", invalidPath);

    Method method = FallSpaceLogic.class.getDeclaredMethod("createDroneBulletFromCsv", String.class,
        String.class, String.class);
    method.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(fallSpaceLogic, maker, modelNumber, name);
    });

    Throwable cause = exception.getTargetException();
    assertTrue(cause instanceof DroneHighwayException);
    assertTrue(cause.getMessage().contains("指定されたメーカー：メーカー2 と型式：型番2_2 の弾丸モデルが見つかりませんでした"));
  }

  @Test
  @DisplayName("指定されたメーカーと型式の簡易風速モデルが見つからない場合に適切なエラーメッセージが返されること")
  public void testSimpleWindModelNotFound() throws NoSuchMethodException {
    String maker = "メーカー3";
    String modelNumber = "型番3_2";
    String name = "パラメータ情報が存在しないデータ";

    // 例外テスト用のファイルパスを設定
    String invalidPath = "../test/data/testFallSpace/error/";
    ReflectionTestUtils.setField(fallSpaceLogic, "csvFilePath", invalidPath);

    Method method = FallSpaceLogic.class.getDeclaredMethod("createDroneSimpleWindFromCsv",
        String.class, String.class, String.class);
    method.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(fallSpaceLogic, maker, modelNumber, name);
    });

    Throwable cause = exception.getTargetException();
    assertTrue(cause instanceof DroneHighwayException);
    assertTrue(
        cause.getMessage().contains("指定されたメーカー：メーカー3 と型式：型番3_2 の簡易風速モデルが見つかりませんでした"));
  }

  @Test
  @DisplayName("指定されたメーカーと型式の弾道バッファモデルが見つからない場合に適切なエラーメッセージが返されること")
  public void testBallisticBufferModelNotFound() throws NoSuchMethodException {
    String maker = "メーカー4";
    String modelNumber = "型番4_2";
    String name = "パラメータ情報が存在しないデータ";

    // 例外テスト用のファイルパスを設定
    String invalidPath = "../test/data/testFallSpace/error/";
    ReflectionTestUtils.setField(fallSpaceLogic, "csvFilePath", invalidPath);

    Method method = FallSpaceLogic.class.getDeclaredMethod("createDroneBallisticBufferFromCsv",
        String.class, String.class, String.class);
    method.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(fallSpaceLogic, maker, modelNumber, name);
    });

    Throwable cause = exception.getTargetException();
    assertTrue(cause instanceof DroneHighwayException);
    assertTrue(cause.getMessage().contains("指定されたメーカー：メーカー4 と型式：型番4_2 の弾道バッファモデルが見つかりませんでした"));
  }

  @Test
  @DisplayName("指定されたメーカーと型式の固定バッファモデルが見つからない場合に適切なエラーメッセージが返されること")
  public void testStaticBufferModelNotFound() throws NoSuchMethodException {
    String maker = "メーカー5";
    String modelNumber = "型番5_2";
    String name = "パラメータ情報が存在しないデータ";

    // 例外テスト用のファイルパスを設定
    String invalidPath = "../test/data/testFallSpace/error/";
    ReflectionTestUtils.setField(fallSpaceLogic, "csvFilePath", invalidPath);

    Method method = FallSpaceLogic.class.getDeclaredMethod("createDroneStaticBufferFromCsv",
        String.class, String.class, String.class);
    method.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(fallSpaceLogic, maker, modelNumber, name);
    });

    Throwable cause = exception.getTargetException();
    assertTrue(cause instanceof DroneHighwayException);
    assertTrue(cause.getMessage().contains("指定されたメーカー：メーカー5 と型式：型番5_2 の固定バッファモデルが見つかりませんでした"));
  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("逆関数方式：指定した計算方式でライブラリが呼び出され落下空間が計算されること")
  public void testComputeFallSpaceWithInverseFunction() throws NoSuchMethodException,
      SecurityException, IllegalAccessException, InvocationTargetException {
    com.intent_exchange.drone_highway.algo.model.FallToleranceRange fallToleranceRange =
        new com.intent_exchange.drone_highway.algo.model.FallToleranceRange("Polygon",
            List.of(List.of(List.of(BigDecimal.ZERO, BigDecimal.ZERO),
                List.of(BigDecimal.ONE, BigDecimal.ZERO), List.of(BigDecimal.ONE, BigDecimal.ONE),
                List.of(BigDecimal.ZERO, BigDecimal.ZERO))));

    List<Drone> drones =
        List.of(
            new DroneParachute("メーカー1", "型番1_1", "機体2", 3.0, 3.0, 1.0, 10.0, 0.5, 1.0, "gps",
                "multirotor", "manual", "automatic", "multirotor",
                List.of(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                    0.0),
                3.0, 5.0),
            new DroneBullet("メーカー2", "型番2_1", "機体1", 1.0, 1.0, 1.0, 15.0, 0.5, 1.0, "barometer",
                "multirotor", "manual", "manual", "multirotor", List.of(0.0, 0.0, 0.0, 0.0, 0.0,
                    0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
                0.95));

    DespersionNode despersionNode = new DespersionNode("LineString", List
        .of(List.of(BigDecimal.ZERO, BigDecimal.ZERO), List.of(BigDecimal.ONE, BigDecimal.ONE)));


    Method method = FallSpaceLogic.class.getDeclaredMethod("computeFallSpace", String.class,
        com.intent_exchange.drone_highway.algo.model.FallToleranceRange.class, List.class,
        com.intent_exchange.drone_highway.algo.model.DespersionNode.class, int.class);
    method.setAccessible(true);

    List<List<BigDecimal>> result = (List<List<BigDecimal>>) method.invoke(fallSpaceLogic,
        "InverseFunctionCalculator", fallToleranceRange, drones, despersionNode, 100);

    // 結果の確認
    assertEquals(101, result.size(), "期待される分割数プラス1であること");

    for (List<BigDecimal> point : result) {
      BigDecimal longitude = point.get(0);
      BigDecimal latitude = point.get(1);
      BigDecimal altitude = point.get(2);

      // 経度・緯度が想定範囲内であることを確認 (例: 0 <= 値 <= 1)
      assertTrue(
          longitude.compareTo(BigDecimal.ZERO) >= 0 && longitude.compareTo(BigDecimal.ONE) <= 0,
          "経度が範囲内であること");
      assertTrue(
          latitude.compareTo(BigDecimal.ZERO) >= 0 && latitude.compareTo(BigDecimal.ONE) <= 0,
          "緯度が範囲内であること");

      // 高度が非負であることを確認
      assertTrue(altitude.compareTo(BigDecimal.ZERO) >= 0, "高度が負でないこと");

      // さらに高度が特定の期待範囲に入っているか確認 (例: 0 <= 高度 <= 15)
      assertTrue(altitude.compareTo(BigDecimal.valueOf(15)) <= 0, "高度が期待される最大値を超えていないこと");
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("空間列挙方式：指定した計算方式でライブラリが呼び出されること")
  public void testComputeFallSpaceWithSpaceEnumeration() throws NoSuchMethodException,
      SecurityException, IllegalAccessException, InvocationTargetException {
    com.intent_exchange.drone_highway.algo.model.FallToleranceRange fallToleranceRange =
        new com.intent_exchange.drone_highway.algo.model.FallToleranceRange("Polygon",
            List.of(List.of(List.of(BigDecimal.ZERO, BigDecimal.ZERO),
                List.of(BigDecimal.ONE, BigDecimal.ZERO), List.of(BigDecimal.ONE, BigDecimal.ONE),
                List.of(BigDecimal.ZERO, BigDecimal.ZERO))));

    List<Drone> drones =
        List.of(
            new DroneParachute("メーカー1", "型番1_1", "機体2", 3.0, 3.0, 1.0, 10.0, 0.5, 1.0, "gps",
                "multirotor", "manual", "automatic", "multirotor",
                List.of(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                    0.0),
                3.0, 5.0),
            new DroneBullet("メーカー2", "型番2_1", "機体1", 1.0, 1.0, 1.0, 15.0, 0.5, 1.0, "barometer",
                "multirotor", "manual", "manual", "multirotor", List.of(0.0, 0.0, 0.0, 0.0, 0.0,
                    0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
                0.95));

    DespersionNode despersionNode = new DespersionNode("LineString", List
        .of(List.of(BigDecimal.ZERO, BigDecimal.ZERO), List.of(BigDecimal.ONE, BigDecimal.ONE)));


    Method method = FallSpaceLogic.class.getDeclaredMethod("computeFallSpace", String.class,
        com.intent_exchange.drone_highway.algo.model.FallToleranceRange.class, List.class,
        com.intent_exchange.drone_highway.algo.model.DespersionNode.class, int.class);
    method.setAccessible(true);

    List<List<BigDecimal>> result = (List<List<BigDecimal>>) method.invoke(fallSpaceLogic,
        "SpaceEnumerationCalculator", fallToleranceRange, drones, despersionNode, 100);

    // 結果の確認
    assertEquals(101, result.size(), "期待される分割数プラス1であること");

    // TODO: 詳細なアサートは正式に実装されてから行う
  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("機械学習方式：指定した計算方式でライブラリが呼び出されること")
  public void testComputeFallSpaceWithMachineLearning() throws NoSuchMethodException,
      SecurityException, IllegalAccessException, InvocationTargetException {
    com.intent_exchange.drone_highway.algo.model.FallToleranceRange fallToleranceRange =
        new com.intent_exchange.drone_highway.algo.model.FallToleranceRange("Polygon",
            List.of(List.of(List.of(BigDecimal.ZERO, BigDecimal.ZERO),
                List.of(BigDecimal.ONE, BigDecimal.ZERO), List.of(BigDecimal.ONE, BigDecimal.ONE),
                List.of(BigDecimal.ZERO, BigDecimal.ZERO))));

    List<Drone> drones =
        List.of(
            new DroneParachute("メーカー1", "型番1_1", "機体2", 3.0, 3.0, 1.0, 10.0, 0.5, 1.0, "gps",
                "multirotor", "manual", "automatic", "multirotor",
                List.of(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                    0.0),
                3.0, 5.0),
            new DroneBullet("メーカー2", "型番2_1", "機体1", 1.0, 1.0, 1.0, 15.0, 0.5, 1.0, "barometer",
                "multirotor", "manual", "manual", "multirotor", List.of(0.0, 0.0, 0.0, 0.0, 0.0,
                    0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
                0.95));

    DespersionNode despersionNode = new DespersionNode("LineString", List
        .of(List.of(BigDecimal.ZERO, BigDecimal.ZERO), List.of(BigDecimal.ONE, BigDecimal.ONE)));


    Method method = FallSpaceLogic.class.getDeclaredMethod("computeFallSpace", String.class,
        com.intent_exchange.drone_highway.algo.model.FallToleranceRange.class, List.class,
        com.intent_exchange.drone_highway.algo.model.DespersionNode.class, int.class);
    method.setAccessible(true);

    List<List<BigDecimal>> result = (List<List<BigDecimal>>) method.invoke(fallSpaceLogic,
        "MachineLearningCalculator", fallToleranceRange, drones, despersionNode, 100);

    // 結果の確認
    assertEquals(0, result.size(), "未実装のため空のリストが返ってくること");
  }

  @Test
  @DisplayName("計算方式が未設定の場合に適切なエラーメッセージが返されること")
  public void testComputeFallSpaceWithUnknownMethod()
      throws NoSuchMethodException, SecurityException {
    FallSpaceLogic fallSpaceLogic = new FallSpaceLogic();

    com.intent_exchange.drone_highway.algo.model.FallToleranceRange fallToleranceRange =
        new com.intent_exchange.drone_highway.algo.model.FallToleranceRange("Polygon",
            new ArrayList<>());
    List<Drone> drones = new ArrayList<>();
    DespersionNode despersionNode = new DespersionNode("LineString", new ArrayList<>());

    Method method = FallSpaceLogic.class.getDeclaredMethod("computeFallSpace", String.class,
        com.intent_exchange.drone_highway.algo.model.FallToleranceRange.class, List.class,
        com.intent_exchange.drone_highway.algo.model.DespersionNode.class, int.class);
    method.setAccessible(true);

    Throwable thrown =
        assertThrows(InvocationTargetException.class, () -> method.invoke(fallSpaceLogic,
            "UnknownCalculator", fallToleranceRange, drones, despersionNode, 100)).getCause();

    if (thrown instanceof DroneHighwayException exception) {
      assertEquals("計算方式が指定されていません", exception.getMessage());
    } else {
      throw new AssertionError("Expected DroneHighwayException to be thrown");
    }
  }

  /**
   * 指定されたパラメータに基づいて、モックの AirwayCompatibleModels オブジェクトを生成します。 テストで使用されるダミーデータ作成用。
   *
   * @param id 対応機種IDおよび航路画定ID
   * @param maker 製造メーカー名
   * @param modelNumber 型式（モデル）
   * @param name 機種名
   * @return 新規に作成された AirwayCompatibleModels オブジェクト
   */
  private AirwayCompatibleModels createMockAirwayCompatibleModels(int id, String maker,
      String modelNumber, String name) {
    AirwayCompatibleModels model = new AirwayCompatibleModels();
    model.setAirwayCompatibleModelsId(id);
    model.setAirwayDeterminationId(id);
    model.setMaker(maker);
    model.setModelNumber(modelNumber);
    model.setName(name);
    model.setType("回転翼航空機（ヘリコプター）");
    model.setIp("IP68");
    model.setLength(950);
    model.setWeight(3000);
    model.setMaximumTakeoffWeight(4000);
    model.setMaximumFlightTime(30);
    model.setCreatedAt(LocalDateTime.now());
    model.setUpdatedAt(LocalDateTime.now());
    return model;
  }

  /**
   * BigDecimal のリストが指定された期待値と一致することを確認します。
   *
   * @param actual チェックする実際の BigDecimal リスト
   * @param expected1 第一の期待される経度の値
   * @param expected2 第二の期待される緯度の値
   */
  private void assertListOfBigDecimalEquals(List<BigDecimal> actual, double expected1,
      double expected2) {
    assertEquals(BigDecimal.valueOf(expected1), actual.get(0), "各ポイントの経度が正しいこと");
    assertEquals(BigDecimal.valueOf(expected2), actual.get(1), "各ポイントの緯度が正しいこと");
  }

  /** 落下許容範囲マッパー */
  @Mock
  private FallToleranceRangeMapper fallToleranceRangeMapper;

  /** 航路画定マッパー */
  @Mock
  private AirwayDeterminationMapper airwayDeterminationMapper;

  /** 落下空間マッパー */
  @Mock
  private FallSpaceMapper fallSpaceMapper;

  /** 航路シーケンスマッパー */
  @Mock
  private AirwaySequenceMapper airwaySequenceMapper;

  /** クロック */
  @Mock
  private Clock clock;

  @Test
  @DisplayName("落下空間に関する基本情報を登録する")
  void testBasicInfoRegistration() {
    FallSpacePostRequestDto fallSpacePostRequestDto = getFallSpacePostRequestDto();
    when(clock.instant()).thenReturn(Instant.now(Clock.systemUTC()));
    when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    when(fallToleranceRangeMapper.selectLockByPrimaryKey(anyString()))
        .thenReturn(getFallToleranceRange(fallSpacePostRequestDto.getFallToleranceRangeId()));
    FallSpacePostResponseDto result = fallSpaceLogic.basicInfoRegistration(fallSpacePostRequestDto);
    assertNotNull(result);
    assertNotNull(result.getAirwayDeterminationId());
  }

  @Test
  @DisplayName("落下空間に関する基本情報を登録する 最大落下許容範囲無し")
  void testBasicInfoRegistrationFallToleranceRangeIsNull() {
    FallSpacePostRequestDto fallSpacePostRequestDto = getFallSpacePostRequestDto();
    when(clock.instant()).thenReturn(Instant.now(Clock.systemUTC()));
    when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    when(fallToleranceRangeMapper.selectLockByPrimaryKey(anyString())).thenReturn(null);
    assertThrows(DataNotFoundException.class,
        () -> fallSpaceLogic.basicInfoRegistration(fallSpacePostRequestDto));
  }

  @Test
  @DisplayName("落下空間に関する基本情報を登録する 最大落下許容範囲削除済み")
  void testBasicInfoRegistrationFallToleranceRangeIsDelete() {
    FallSpacePostRequestDto fallSpacePostRequestDto = getFallSpacePostRequestDto();
    when(clock.instant()).thenReturn(Instant.now(Clock.systemUTC()));
    when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    FallToleranceRange fallToleranceRange =
        getFallToleranceRange(fallSpacePostRequestDto.getFallToleranceRangeId());
    fallToleranceRange.setDelete(true);
    when(fallToleranceRangeMapper.selectLockByPrimaryKey(anyString()))
        .thenReturn(fallToleranceRange);
    assertThrows(DataNotFoundException.class,
        () -> fallSpaceLogic.basicInfoRegistration(fallSpacePostRequestDto));
  }

  private FallSpacePostRequestDto getFallSpacePostRequestDto() {
    FallSpacePostRequestDroneDto drone = new FallSpacePostRequestDroneDto();
    drone.setAircraftInfoId(1);
    drone.setMaker("maker");
    drone.setModelNumber("modelNumber");
    drone.setName("name");
    drone.setType("type");
    drone.setIp("ip");
    drone.setLength(1);
    drone.setWeight(1);
    drone.setMaximumTakeoffWeight(1);
    drone.setMaximumFlightTime(1);
    FallSpacePostRequestDto result = new FallSpacePostRequestDto();
    result.setFallToleranceRangeId("123456789012345678901234567890123456");
    result.setNumCrossSectionDivisions(20);
    result.setDroneList(Arrays.asList(drone));
    return result;
  }

  private FallToleranceRange getFallToleranceRange(String fallToleranceRangeId) {
    FallToleranceRange result = new FallToleranceRange();
    String json =
        "{\"type\": \"Polygon\",\"coordinates\": [[[139.84425094162697,35.65649757318167],[139.8506696707106,35.65611144123251],[139.85103098834156,35.67572492318939],[139.8603064948997,35.69311183099994],[139.85495492073812,35.69407775293554],[139.84484594816934,35.67678697870946],[139.84425094162697,35.65649757318167]]]}";
    result.setFallToleranceRangeId(fallToleranceRangeId);
    result.setBusinessNumber("businessNumber");
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
  @DisplayName("落下空間(断面)を取得する")
  void testGetFallSpaceCrossSection() {
    FallSpaceCrossSectionPostRequestDto fallSpaceCrossSectionPostRequestDto =
        getFallSpaceCrossSectionPostRequestDto();
    AirwayDetermination airwayDetermination =
        getAirwayDetermination(fallSpaceCrossSectionPostRequestDto);
    when(clock.instant()).thenReturn(Instant.now(Clock.systemUTC()));
    when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    when(airwayDeterminationMapper.selectByPrimaryKey(anyInt())).thenReturn(airwayDetermination);
    when(fallToleranceRangeMapper.selectByPrimaryKey(anyString()))
        .thenReturn(getFallToleranceRange(airwayDetermination.getFallToleranceRangeId()));


    when(airwayCompatibleModelsMapper
        .selectByAirwayDeterminationId(airwayDetermination.getAirwayDeterminationId()))
            .thenReturn(getAirwayCompatibleModels(airwayDetermination));
    FallSpaceCrossSectionPostResponseDto result =
        fallSpaceLogic.getFallSpaceCrossSection(fallSpaceCrossSectionPostRequestDto);
    assertNotNull(result);
    assertNotNull(result.getFallSpaceCrossSectionId());
  }

  @Test
  @DisplayName("落下空間(断面)を取得する 航路確定無し")
  void testGetFallSpaceCrossSectionAirwayDeterminationNull() {
    when(airwayDeterminationMapper.selectByPrimaryKey(anyInt())).thenReturn(null);
    assertThrows(DataNotFoundException.class,
        () -> fallSpaceLogic.getFallSpaceCrossSection(getFallSpaceCrossSectionPostRequestDto()));
  }

  @Test
  @DisplayName("落下空間(断面)を取得する 最大落下許容範囲無し")
  void testGetFallSpaceCrossSectionFallToleranceRangeNull() {
    FallSpaceCrossSectionPostRequestDto fallSpaceCrossSectionPostRequestDto =
        getFallSpaceCrossSectionPostRequestDto();
    AirwayDetermination airwayDetermination =
        getAirwayDetermination(fallSpaceCrossSectionPostRequestDto);
    when(airwayDeterminationMapper.selectByPrimaryKey(anyInt())).thenReturn(airwayDetermination);
    when(fallToleranceRangeMapper.selectByPrimaryKey(anyString())).thenReturn(null);
    assertThrows(DataNotFoundException.class,
        () -> fallSpaceLogic.getFallSpaceCrossSection(getFallSpaceCrossSectionPostRequestDto()));
  }

  @Test
  @DisplayName("落下空間(断面)を取得する 最大落下許容範囲削除済み")
  void testGetFallSpaceCrossSectionFallToleranceRangeDelete() {
    FallSpaceCrossSectionPostRequestDto fallSpaceCrossSectionPostRequestDto =
        getFallSpaceCrossSectionPostRequestDto();
    AirwayDetermination airwayDetermination =
        getAirwayDetermination(fallSpaceCrossSectionPostRequestDto);
    when(airwayDeterminationMapper.selectByPrimaryKey(anyInt())).thenReturn(airwayDetermination);
    FallToleranceRange fallToleranceRange =
        getFallToleranceRange(airwayDetermination.getFallToleranceRangeId());
    fallToleranceRange.setDelete(true);
    when(fallToleranceRangeMapper.selectByPrimaryKey(anyString())).thenReturn(fallToleranceRange);
    assertThrows(DataNotFoundException.class,
        () -> fallSpaceLogic.getFallSpaceCrossSection(getFallSpaceCrossSectionPostRequestDto()));
  }

  private FallSpaceCrossSectionPostRequestDto getFallSpaceCrossSectionPostRequestDto() {
    FallSpaceCrossSectionPostRequestGeometryDto geometry =
        new FallSpaceCrossSectionPostRequestGeometryDto();
    geometry.setType("LineString");
    geometry.setCoordinates(Arrays.asList(
        Arrays.asList(new BigDecimal(138.9363042103374), new BigDecimal(35.95184004619426)),
        Arrays.asList(new BigDecimal(138.9362491725152), new BigDecimal(35.95198265653977))));
    FallSpaceCrossSectionPostRequestDto result = new FallSpaceCrossSectionPostRequestDto();
    result.setAirwayDeterminationId(1);
    result.setGeometry(geometry);
    return result;
  }

  private AirwayDetermination getAirwayDetermination(FallSpaceCrossSectionPostRequestDto dto) {
    AirwayDetermination result = new AirwayDetermination();
    result.setAirwayDeterminationId(1);
    result.setFallToleranceRangeId("123456789012345678901234567890123456");
    result.setNumCrossSectionDivisions(20);
    return result;
  }

  private List<AirwayCompatibleModels> getAirwayCompatibleModels(
      AirwayDetermination airwayDetermination) {
    return List.of(createMockAirwayCompatibleModels(airwayDetermination.getAirwayDeterminationId(),
        "メーカー1", "型番1_1", "機種1"));
  }


}
