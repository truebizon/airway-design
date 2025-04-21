package com.intent_exchange.drone_highway.logic;

import static org.junit.jupiter.api.Assertions.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import com.intent_exchange.drone_highway.config.TestConfig;
import com.intent_exchange.drone_highway.dto.response.AircraftDto;
import com.intent_exchange.drone_highway.exception.DroneHighwayException;
import com.opencsv.exceptions.CsvException;

@SpringBootTest(classes = TestConfig.class)
public class AircraftLogicTest {

  @InjectMocks
  private AircraftLogic aircraftLogic;

  @BeforeEach
  public void setUp() throws Exception {
    Field csvFilePathField = AircraftLogic.class.getDeclaredField("csvFilePath");
    csvFilePathField.setAccessible(true);
    csvFilePathField.set(aircraftLogic, "../test/data/testAircraft/");
  }

  @Test
  @DisplayName("機体情報が正常に取得されること")
  public void testGetAircraft() throws IOException, CsvException {
    List<AircraftDto> result = aircraftLogic.getAircraft();

    assertEquals(8, result.size());

    AircraftDto aircraft = result.get(0);
    assertEquals(1, aircraft.getAircraftInfoId());
    assertEquals("メーカー1", aircraft.getMaker());
    assertEquals("型番1_1", aircraft.getModelNumber());
    assertEquals("機体1", aircraft.getName());
    assertEquals("回転翼航空機（ヘリコプター）", aircraft.getType());
    assertEquals("IP68", aircraft.getIp());
    assertEquals(950, aircraft.getLength());
    assertEquals(1000, aircraft.getWeight());
    assertEquals(1500, aircraft.getMaximumTakeoffWeight());
    assertEquals(30, aircraft.getMaximumFlightTime());
    assertEquals(1, aircraft.getDeviationRange());
    assertEquals("ParachuteModelParameters.csv", aircraft.getFallingModel());

    aircraft = result.get(7);
    assertEquals(8, aircraft.getAircraftInfoId());
    assertEquals("メーカー2", aircraft.getMaker());
    assertEquals("型番2_4", aircraft.getModelNumber());
    assertEquals("機体8", aircraft.getName());
    assertEquals("回転翼航空機（マルチローター）", aircraft.getType());
    assertEquals("IP68", aircraft.getIp());
    assertEquals(950, aircraft.getLength());
    assertEquals(1000, aircraft.getWeight());
    assertEquals(1500, aircraft.getMaximumTakeoffWeight());
    assertEquals(30, aircraft.getMaximumFlightTime());
    assertEquals(1, aircraft.getDeviationRange());
    assertEquals("BulletModelParameters.csv", aircraft.getFallingModel());
  }

  @Test
  @DisplayName("機体情報CSVファイルが見つからない場合に適切なエラーメッセージが返されること")
  public void testGetAircraftFileNotFound() throws NoSuchFieldException, IllegalAccessException {
    // csvFilePathを存在しないディレクトリに設定
    Field csvFilePathField = AircraftLogic.class.getDeclaredField("csvFilePath");
    csvFilePathField.setAccessible(true);
    csvFilePathField.set(aircraftLogic, "../nonexistent/data/");

    DroneHighwayException exception =
        assertThrows(DroneHighwayException.class, () -> aircraftLogic.getAircraft());

    Throwable cause = exception.getCause();
    assertNotNull(cause, "内側の例外が存在すること");
    assertTrue(cause instanceof FileNotFoundException, "期待されるFileNotFoundExceptionが内側に存在すること");
    assertEquals("CSVファイルが見つかりませんでした", exception.getMessage(), "エラーメッセージが期待通りであること");
  }

  @Test
  @DisplayName("機体情報の読み込みに失敗した場合に適切なエラーメッセージが返されること")
  public void testGetAircraftException() throws NoSuchFieldException, IllegalAccessException {
    // 例外テスト用のファイルパスを設定
    Field csvFilePathField = AircraftLogic.class.getDeclaredField("csvFilePath");
    csvFilePathField.setAccessible(true);
    csvFilePathField.set(aircraftLogic, "../test/data/testAircraft/error/");

    DroneHighwayException exception =
        assertThrows(DroneHighwayException.class, () -> aircraftLogic.getAircraft());

    assertTrue(exception.getMessage().contains("機体データの読み込みに失敗しました"));
  }
}
