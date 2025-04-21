/*
 * Copyright 2025 Intent Exchange, Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.intent_exchange.drone_highway.logic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intent_exchange.drone_highway.algo.calculator.FlightGeographyCalculator;
import com.intent_exchange.drone_highway.algo.calculator.InverseFunctionCalculator;
import com.intent_exchange.drone_highway.algo.calculator.MachineLearningCalculator;
import com.intent_exchange.drone_highway.algo.calculator.SpaceEnumerationCalculator;
import com.intent_exchange.drone_highway.algo.model.DespersionNode;
import com.intent_exchange.drone_highway.algo.model.Drone;
import com.intent_exchange.drone_highway.algo.model.DroneBallisticBuffer;
import com.intent_exchange.drone_highway.algo.model.DroneBullet;
import com.intent_exchange.drone_highway.algo.model.DroneParachute;
import com.intent_exchange.drone_highway.algo.model.DroneSimpleWind;
import com.intent_exchange.drone_highway.algo.model.DroneStaticBuffer;
import com.intent_exchange.drone_highway.dao.AirwayCompatibleModelsMapper;
import com.intent_exchange.drone_highway.dao.AirwayDeterminationMapper;
import com.intent_exchange.drone_highway.dao.AirwaySequenceMapper;
import com.intent_exchange.drone_highway.dao.FallSpaceMapper;
import com.intent_exchange.drone_highway.dao.FallToleranceRangeMapper;
import com.intent_exchange.drone_highway.dto.request.FallSpaceCrossSectionPostRequestDto;
import com.intent_exchange.drone_highway.dto.request.FallSpacePostRequestDto;
import com.intent_exchange.drone_highway.dto.response.FallSpaceCrossSectionPostResponseDto;
import com.intent_exchange.drone_highway.dto.response.FallSpacePostResponseDto;
import com.intent_exchange.drone_highway.exception.DataNotFoundException;
import com.intent_exchange.drone_highway.exception.DroneHighwayException;
import com.intent_exchange.drone_highway.model.AirwayCompatibleModels;
import com.intent_exchange.drone_highway.model.AirwayDetermination;
import com.intent_exchange.drone_highway.model.FallSpace;
import com.intent_exchange.drone_highway.model.FallToleranceRange;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

/**
 * 落下空間に関するロジック
 */
@Component
public class FallSpaceLogic {

  /** 落下許容範囲マッパー */
  @Autowired
  private FallToleranceRangeMapper fallToleranceRangeMapper;

  /** 航路画定マッパー */
  @Autowired
  private AirwayDeterminationMapper airwayDeterminationMapper;

  /** 航路対応機種マッパー */
  @Autowired
  private AirwayCompatibleModelsMapper airwayCompatibleModelsMapper;

  /** 落下空間マッパー */
  @Autowired
  private FallSpaceMapper fallSpaceMapper;

  /** 航路シーケンスマッパー */
  @Autowired
  private AirwaySequenceMapper airwaySequenceMapper;

  /** クロック */
  @Autowired
  private Clock clock;

  /** 逆関数方式 */
  private static final String INVERSE_FUNCTION = "InverseFunctionCalculator";

  /** 空間列挙方式 */
  private static final String SPACE_ENUMERATION = "SpaceEnumerationCalculator";

  /** 機械学習方式 */
  private static final String MACHINE_LEARNING = "MachineLearningCalculator";

  /** 機体情報（共通項目）CSVのファイル名 */
  private static final String DRONE_MODEL_CONFIG_FILE = "DroneModelConfig.csv";

  /** パラシュートモデルのパラメータCSVファイル名 */
  private static final String PARACHUTE_MODEL_PARAMETERS_FILE = "ParachuteModelParameters.csv";

  /** 弾丸モデルのパラメータCSVファイル名 */
  private static final String BULLET_MODEL_PARAMETERS_FILE = "BulletModelParameters.csv";

  /** 簡易風速モデルのパラメータCSVファイル名 */
  private static final String SIMPLE_WIND_MODEL_PARAMETERS_FILE = "SimpleWindModelParameters.csv";

  /** 弾道バッファモデルのパラメータCSVファイル名 */
  private static final String BALLISTIC_BUFFER_MODEL_PARAMETERS_FILE =
      "BallisticBufferModelParameters.csv";

  /** 固定バッファモデルのパラメータCSVファイル名 */
  private static final String STATIC_BUFFER_MODEL_PARAMETERS_FILE =
      "StaticBufferModelParameters.csv";

  /** CSVファイルが配置されているディレクトリへのパス */
  @Value("${csv.file.path}")
  private String csvFilePath;

  /**
   * 落下空間に関する基本情報を登録する
   * 
   * @param fallSpacePostRequestDto 航路確定/落下空間基本情報登録(A-1-2-2-2)レスポンスDTO
   * @return 航路確定/落下空間基本情報登録(A-1-2-2-2)レスポンスDTO
   * @throws DataNotFoundException データが存在しない場合
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public FallSpacePostResponseDto basicInfoRegistration(
      FallSpacePostRequestDto fallSpacePostRequestDto) {
    // 最大落下許容範囲の削除処理と同時に実行される場合を考慮しロックする
    FallToleranceRange fallToleranceRange = fallToleranceRangeMapper
        .selectLockByPrimaryKey(fallSpacePostRequestDto.getFallToleranceRangeId());
    if (Objects.isNull(fallToleranceRange)) {
      throw new DataNotFoundException();
    }
    if (fallToleranceRange.getDelete()) {
      throw new DataNotFoundException();
    }
    LocalDateTime now = LocalDateTime.now(clock);
    int airwayDeterminationId = airwaySequenceMapper.selectAirwayDeterminationIdSeqNextVal();
    AirwayDetermination airwayDetermination = new AirwayDetermination();
    airwayDetermination.setAirwayDeterminationId(airwayDeterminationId);
    airwayDetermination.setBusinessNumber(fallToleranceRange.getBusinessNumber());
    airwayDetermination.setFallToleranceRangeId(fallToleranceRange.getFallToleranceRangeId());
    airwayDetermination
        .setNumCrossSectionDivisions(fallSpacePostRequestDto.getNumCrossSectionDivisions());
    airwayDetermination.setCreatedAt(now);
    airwayDetermination.setUpdatedAt(now);
    airwayDeterminationMapper.insert(airwayDetermination);

    fallSpacePostRequestDto.getDroneList().forEach(item -> {
      AirwayCompatibleModels airwayCompatibleModels =
          ModelMapperUtil.map(item, AirwayCompatibleModels.class);
      airwayCompatibleModels.setAirwayCompatibleModelsId(
          airwaySequenceMapper.selectAirwayCompatibleModelsIdSeqNextVal());
      airwayCompatibleModels.setAirwayDeterminationId(airwayDeterminationId);
      airwayCompatibleModels.setCreatedAt(now);
      airwayCompatibleModels.setUpdatedAt(now);
      airwayCompatibleModelsMapper.insert(airwayCompatibleModels);
    });
    FallSpacePostResponseDto result = new FallSpacePostResponseDto();
    result.setAirwayDeterminationId(airwayDeterminationId);
    return result;

  }

  /**
   * 落下空間(断面)を取得する
   * 
   * @param fallToleranceRangePostRequestDto 航路確定/最大落下許容範囲(A-1-2-1)登録DTO
   * @return 落下空間(断面)
   * @throws DataNotFoundException データが存在しない場合
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public FallSpaceCrossSectionPostResponseDto getFallSpaceCrossSection(
      FallSpaceCrossSectionPostRequestDto fallSpaceCrossSectionPostRequestDto) {

    // 航路画定情報取得
    AirwayDetermination airwayDetermination = airwayDeterminationMapper
        .selectByPrimaryKey(fallSpaceCrossSectionPostRequestDto.getAirwayDeterminationId());
    if (Objects.isNull(airwayDetermination)) {
      throw new DataNotFoundException();
    }

    // 最大落下許容範囲取得
    FallToleranceRange fallToleranceRangeDB =
        fallToleranceRangeMapper.selectByPrimaryKey(airwayDetermination.getFallToleranceRangeId());
    if (Objects.isNull(fallToleranceRangeDB)) {
      throw new DataNotFoundException();
    }
    if (fallToleranceRangeDB.getDelete()) {
      throw new DataNotFoundException();
    }

    // 最大落下許容範囲設定(DBから取得、ライブラリモデル用に変換)
    com.intent_exchange.drone_highway.algo.model.FallToleranceRange fallToleranceRangeLib =
        convertToModelFallToleranceRange(fallToleranceRangeDB);

    // 機体パラメータ設定(DB、CSVから取得)
    List<Drone> drones =
        getDronesForLibrary(fallSpaceCrossSectionPostRequestDto.getAirwayDeterminationId());

    // 落下範囲節設定(リクエストから取得)
    DespersionNode despersionNode = new DespersionNode("LineString",
        fallSpaceCrossSectionPostRequestDto.getGeometry().getCoordinates());

    int fallSpaceId = airwaySequenceMapper.selectFallSpaceIdSeqNextVal();

    // 落下空間計算(逆関数方式固定)
    String calcMethod = INVERSE_FUNCTION;
    List<List<BigDecimal>> data = computeFallSpace(calcMethod, fallToleranceRangeLib, drones,
        despersionNode, airwayDetermination.getNumCrossSectionDivisions());

    LocalDateTime now = LocalDateTime.now(clock);
    FallSpace fallSpace = new FallSpace();
    fallSpace.setFallSpaceId(fallSpaceId);
    fallSpace.setAirwayDeterminationId(airwayDetermination.getAirwayDeterminationId());
    try {
      fallSpace.setData(new ObjectMapper().writeValueAsString(data));
    } catch (JsonProcessingException e) {
      throw new DroneHighwayException(e.getMessage());
    }
    fallSpace.setCreatedAt(now);
    fallSpace.setUpdatedAt(now);
    fallSpaceMapper.insert(fallSpace);
    FallSpaceCrossSectionPostResponseDto result = new FallSpaceCrossSectionPostResponseDto();
    result.setFallSpaceCrossSectionId(fallSpaceId);
    result.setData(data);
    return result;
  }

  /**
   * 最大落下許容範囲をモデル用に変換する。
   *
   * @param fallToleranceRange プロジェクト内のオブジェクト
   * @return fallToleranceRange ライブラリ用のオブジェクト
   */
  private com.intent_exchange.drone_highway.algo.model.FallToleranceRange convertToModelFallToleranceRange(
      FallToleranceRange fallToleranceRangeDB) {
    // Json形式の最大落下許容範囲を落下計算ライブラリのモデルに格納するためにデータ加工
    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> jsonMap = null;
    try {
      jsonMap = mapper.readValue(fallToleranceRangeDB.getJson(),
          new TypeReference<Map<String, Object>>() {});
    } catch (JsonMappingException e) {
      throw new DroneHighwayException("JSONマッピングエラーが発生しました", e);
    } catch (JsonProcessingException e) {
      throw new DroneHighwayException("JSON処理エラーが発生しました", e);
    }

    try {
      // coordinates部分のデータを生成
      List<List<List<BigDecimal>>> coordinates = new ArrayList<>();
      // 例：[[[AAA.AAA,BBB.BBB],[CCC.CCC,DDD.DDD]]]の取得
      @SuppressWarnings("unchecked")
      List<List<List<Object>>> coordList = (List<List<List<Object>>>) jsonMap.get("coordinates");
      if (coordList != null) {
        coordinates = coordList.stream()
            .map(subCoordList -> subCoordList.stream() // [[AAA.AAA,BBB.BBB],[CCC.CCC,DDD.DDD]]の取得
                .map(pointList -> pointList.stream().map(value -> { // [AAA.AAA,BBB.BBB],[CCC.CCC,DDD.DDD]の取得
                  try {
                    return new BigDecimal(String.valueOf(value)); // AAA.AAA,BBB.BBBの取得
                  } catch (NumberFormatException e) {
                    throw new DroneHighwayException("数値変換エラーです: " + value, e);
                  }
                }).toList()) // List<BigDecimal>に格納
                .toList()) // List<List<BigDecimal>>に格納
            .toList(); // List<List<List<BigDecimal>>>に格納
        return new com.intent_exchange.drone_highway.algo.model.FallToleranceRange("Polygon",
            coordinates);
      }
    } catch (ClassCastException e) {
      throw new DroneHighwayException("型が期待通りではありません", e);
    }
    throw new DroneHighwayException("最大落下許容範囲をDBから取得できませんでした");
  }

  /**
   * 指定された航路画定IDに基づくドローンリストをCSVから取得
   *
   * @param airwayDeterminationId 航路画定ID
   * @return ドローンのリスト
   * @throws DroneHighwayException ビジネスロジックの実行に失敗した場合
   */
  private List<Drone> getDronesForLibrary(int airwayDeterminationId) {
    List<Drone> drones = new ArrayList<>();

    // 航路画定IDをキーにして機体リスト取得
    List<AirwayCompatibleModels> airwayCompatibleModels =
        airwayCompatibleModelsMapper.selectByAirwayDeterminationId(airwayDeterminationId);
    if (Objects.isNull(airwayCompatibleModels)) {
      throw new DataNotFoundException();
    }

    // CSVから機体情報を取得
    String fullPathDrone = csvFilePath + DRONE_MODEL_CONFIG_FILE;
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fullPathDrone)) {
      if (inputStream == null) {
        throw new FileNotFoundException("CSV file not found: " + fullPathDrone);
      }
      try (CSVReader reader =
          new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
        reader.readNext();
        List<String[]> data = reader.readAll();

        for (AirwayCompatibleModels AirwayCompatibleModel : airwayCompatibleModels) {
          // 一致するCSVの行が見つかったか確認するフラグ
          boolean dataMatch = false;

          for (String[] row : data) {
            String maker = row[1];
            String modelNumber = row[2];
            String name = row[3];
            String fallingModel = row[11];

            // 製造メーカー名,型式が一致している機体が対象
            if (AirwayCompatibleModel.getMaker().equals(maker)
                && AirwayCompatibleModel.getModelNumber().equals(modelNumber)) {

              switch (fallingModel) {
                // パラシュートモデル
                case PARACHUTE_MODEL_PARAMETERS_FILE -> drones
                    .add(createDroneParachuteFromCsv(maker, modelNumber, name));
                // 弾丸モデル
                case BULLET_MODEL_PARAMETERS_FILE -> drones
                    .add(createDroneBulletFromCsv(maker, modelNumber, name));
                // 簡易風速モデル
                case SIMPLE_WIND_MODEL_PARAMETERS_FILE -> drones
                    .add(createDroneSimpleWindFromCsv(maker, modelNumber, name));
                // 弾道バッファモデル
                case BALLISTIC_BUFFER_MODEL_PARAMETERS_FILE -> drones
                    .add(createDroneBallisticBufferFromCsv(maker, modelNumber, name));
                // 固定バッファモデル
                case STATIC_BUFFER_MODEL_PARAMETERS_FILE -> drones
                    .add(createDroneStaticBufferFromCsv(maker, modelNumber, name));
                default -> throw new DroneHighwayException("落下モデルの読み込みに失敗しました");
              };
              dataMatch = true;
              break;
            }
          }

          if (!dataMatch) {
            throw new DroneHighwayException("指定されたメーカー：" + AirwayCompatibleModel.getMaker()
                + " と型式：" + AirwayCompatibleModel.getModelNumber() + " の機体データが見つかりませんでした");
          }
        }
      }
    } catch (FileNotFoundException e) {
      throw new DroneHighwayException("機体データのCSVファイルが見つかりませんでした", e);
    } catch (IOException | CsvException | NumberFormatException
        | ArrayIndexOutOfBoundsException e) {
      throw new DroneHighwayException("機体データの読み込みに失敗しました：" + e.getMessage(), e);
    }
    return drones;
  }

  /**
   * CSVからパラシュートモデルを生成
   *
   * @param config 機体情報
   * @return パラシュートモデル
   * @throws DroneHighwayException ビジネスロジックに失敗した場合
   */
  private DroneParachute createDroneParachuteFromCsv(String maker, String modelNumber,
      String name) {
    String fullPath = csvFilePath + PARACHUTE_MODEL_PARAMETERS_FILE;

    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fullPath)) {
      if (inputStream == null) {
        throw new FileNotFoundException("CSV file not found: " + fullPath);
      }
      try (CSVReader reader =
          new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
        reader.readNext();
        List<String[]> data = reader.readAll();

        for (String[] row : data) {
          if (row[0].equals(maker) && row[1].equals(modelNumber)) {
            return new DroneParachute(row[0], // maker
                row[1], // modelNumber
                name, // name
                Double.parseDouble(row[4]), // gpsInaccuracy
                Double.parseDouble(row[5]), // positionHoldingError
                Double.parseDouble(row[6]), // mapError
                Double.parseDouble(row[7]), // maxFlightSpeed
                Double.parseDouble(row[8]), // responseTime
                Double.parseDouble(row[9]), // parachuteOpenTime
                row[10], // altitudeMeasurementErrorType
                row[11], // contingencyManeuversHeightType
                row[12], // responseTimeHeightType
                row[13], // responseTimeDistanceType
                row[14], // contingencyManeuversDistanceType
                createDummyMaxWindSpeedVector(Double.parseDouble(row[15])), // maxWindSpeedVector(ダミー)
                Double.parseDouble(row[2]), // verticalSpeed
                Double.parseDouble(row[3]) // maxWindSpeed
            );
          }
        }
      }
    } catch (FileNotFoundException e) {
      throw new DroneHighwayException("パラシュートモデルのCSVファイルが見つかりませんでした", e);
    } catch (IOException | CsvException | NumberFormatException
        | ArrayIndexOutOfBoundsException e) {
      throw new DroneHighwayException("パラシュートモデル情報の読み込みに失敗しました：" + e.getMessage(), e);
    }

    throw new DroneHighwayException(
        "指定されたメーカー：" + maker + " と型式：" + modelNumber + " のパラシュートモデルが見つかりませんでした");
  }

  /**
   * CSVから弾丸モデルを生成
   *
   * @param config 機体情報
   * @return 弾丸モデル
   * @throws DroneHighwayException ビジネスロジックに失敗した場合
   */
  private DroneBullet createDroneBulletFromCsv(String maker, String modelNumber, String name) {
    String fullPath = csvFilePath + BULLET_MODEL_PARAMETERS_FILE;

    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fullPath)) {
      if (inputStream == null) {
        throw new FileNotFoundException("CSV file not found: " + fullPath);
      }
      try (CSVReader reader =
          new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
        reader.readNext();
        List<String[]> data = reader.readAll();

        for (String[] row : data) {
          if (row[0].equals(maker) && row[1].equals(modelNumber)) {
            return new DroneBullet(row[0], // maker
                row[1], // modelNumber
                name, // name
                Double.parseDouble(row[3]), // gpsInaccuracy
                Double.parseDouble(row[4]), // positionHoldingError
                Double.parseDouble(row[5]), // mapError
                Double.parseDouble(row[6]), // maxFlightSpeed
                Double.parseDouble(row[7]), // responseTime
                Double.parseDouble(row[8]), // parachuteOpenTime
                row[9], // altitudeMeasurementErrorType
                row[10], // contingencyManeuversHeightType
                row[11], // responseTimeHeightType
                row[12], // responseTimeDistanceType
                row[13], // contingencyManeuversDistanceType
                createDummyMaxWindSpeedVector(Double.parseDouble(row[14])), // maxWindSpeedVector(ダミー)
                Double.parseDouble(row[2]) // characteristicDimension
            );
          }
        }
      }
    } catch (FileNotFoundException e) {
      throw new DroneHighwayException("弾丸モデルのCSVファイルが見つかりませんでした", e);
    } catch (IOException | CsvException | NumberFormatException
        | ArrayIndexOutOfBoundsException e) {
      throw new DroneHighwayException("弾丸モデル情報の読み込みに失敗しました：" + e.getMessage(), e);
    }

    throw new DroneHighwayException(
        "指定されたメーカー：" + maker + " と型式：" + modelNumber + " の弾丸モデルが見つかりませんでした");
  }

  /**
   * CSVから簡易風速モデルを生成
   *
   * @param config 機体情報
   * @return 簡易風速モデル
   * @throws DroneHighwayException ビジネスロジックに失敗した場合
   */
  private DroneSimpleWind createDroneSimpleWindFromCsv(String maker, String modelNumber,
      String name) {
    String fullPath = csvFilePath + SIMPLE_WIND_MODEL_PARAMETERS_FILE;

    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fullPath)) {
      if (inputStream == null) {
        throw new FileNotFoundException("CSV file not found: " + fullPath);
      }
      try (CSVReader reader =
          new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
        reader.readNext();
        List<String[]> data = reader.readAll();

        for (String[] row : data) {
          if (row[0].equals(maker) && row[1].equals(modelNumber)) {
            return new DroneSimpleWind(row[0], // maker
                row[1], // modelNumber
                name, // name
                0.0, // gpsInaccuracy
                0.0, // positionHoldingError
                0.0, // mapError
                Double.parseDouble(row[3]), // maxFlightSpeed
                0.0, // responseTime
                0.0, // parachuteOpenTime
                "", // altitudeMeasurementErrorType
                "", // contingencyManeuversHeightType
                "", // responseTimeHeightType
                "", // responseTimeDistanceType
                "", // contingencyManeuversDistanceType
                createDummyMaxWindSpeedVector(0.0), // maxWindSpeedVector(ダミー)
                Double.parseDouble(row[2]) // maxWindSpeed
            );
          }
        }
      }
    } catch (FileNotFoundException e) {
      throw new DroneHighwayException("簡易風速モデルのCSVファイルが見つかりませんでした", e);
    } catch (IOException | CsvException | NumberFormatException
        | ArrayIndexOutOfBoundsException e) {
      throw new DroneHighwayException("簡易風速モデル情報の読み込みに失敗しました：" + e.getMessage(), e);
    }

    throw new DroneHighwayException(
        "指定されたメーカー：" + maker + " と型式：" + modelNumber + " の簡易風速モデルが見つかりませんでした");
  }

  /**
   * CSVから弾道バッファモデルを生成
   *
   * @param config 機体情報
   * @return 弾道バッファモデル
   * @throws DroneHighwayException ビジネスロジックに失敗した場合
   */
  private DroneBallisticBuffer createDroneBallisticBufferFromCsv(String maker, String modelNumber,
      String name) {
    String fullPath = csvFilePath + BALLISTIC_BUFFER_MODEL_PARAMETERS_FILE;

    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fullPath)) {
      if (inputStream == null) {
        throw new FileNotFoundException("CSV file not found: " + fullPath);
      }
      try (CSVReader reader =
          new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
        reader.readNext();
        List<String[]> data = reader.readAll();

        for (String[] row : data) {
          if (row[0].equals(maker) && row[1].equals(modelNumber)) {
            return new DroneBallisticBuffer(row[0], // maker
                row[1], // modelNumber
                name, // name
                Double.parseDouble(row[4]), // gpsInaccuracy
                Double.parseDouble(row[5]), // positionHoldingError
                Double.parseDouble(row[6]), // mapError
                Double.parseDouble(row[7]), // maxFlightSpeed
                Double.parseDouble(row[8]), // responseTime
                0.0, // parachuteOpenTime
                row[9], // altitudeMeasurementErrorType
                row[10], // contingencyManeuversHeightType
                row[11], // responseTimeHeightType
                row[12], // responseTimeDistanceType
                row[13], // contingencyManeuversDistanceType
                createDummyMaxWindSpeedVector(0.0), // maxWindSpeedVector(ダミー)
                Double.parseDouble(row[2]), // characteristicDimension
                Double.parseDouble(row[3]) // staticBuffer
            );
          }
        }
      }
    } catch (FileNotFoundException e) {
      throw new DroneHighwayException("弾道バッファモデルのCSVファイルが見つかりませんでした", e);
    } catch (IOException | CsvException | NumberFormatException
        | ArrayIndexOutOfBoundsException e) {
      throw new DroneHighwayException("弾道バッファモデル情報の読み込みに失敗しました：" + e.getMessage(), e);
    }

    throw new DroneHighwayException(
        "指定されたメーカー：" + maker + " と型式：" + modelNumber + " の弾道バッファモデルが見つかりませんでした");
  }

  /**
   * CSVから固定バッファモデルを生成
   *
   * @param config 機体情報
   * @return 固定バッファモデル
   * @throws DroneHighwayException ビジネスロジックに失敗した場合
   */
  private DroneStaticBuffer createDroneStaticBufferFromCsv(String maker, String modelNumber,
      String name) {
    String fullPath = csvFilePath + STATIC_BUFFER_MODEL_PARAMETERS_FILE;

    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fullPath)) {
      if (inputStream == null) {
        throw new FileNotFoundException("CSV file not found: " + fullPath);
      }
      try (CSVReader reader =
          new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
        reader.readNext();
        List<String[]> data = reader.readAll();

        for (String[] row : data) {
          if (row[0].equals(maker) && row[1].equals(modelNumber)) {
            return new DroneStaticBuffer(row[0], // maker
                row[1], // modelNumber
                name, // name
                0.0, // gpsInaccuracy
                0.0, // positionHoldingError
                0.0, // mapError
                0.0, // maxFlightSpeed
                0.0, // responseTime
                0.0, // parachuteOpenTime
                "", // altitudeMeasurementErrorType
                "", // contingencyManeuversHeightType
                "", // responseTimeHeightType
                "", // responseTimeDistanceType
                "", // contingencyManeuversDistanceType
                createDummyMaxWindSpeedVector(0.0), // maxWindSpeedVector(ダミー)
                Double.parseDouble(row[2]) // staticBuffer
            );
          }
        }
      }
    } catch (FileNotFoundException e) {
      throw new DroneHighwayException("固定バッファモデルのCSVファイルが見つかりませんでした", e);
    } catch (IOException | CsvException | NumberFormatException
        | ArrayIndexOutOfBoundsException e) {
      throw new DroneHighwayException("固定バッファモデル情報の読み込みに失敗しました：" + e.getMessage(), e);
    }

    throw new DroneHighwayException(
        "指定されたメーカー：" + maker + " と型式：" + modelNumber + " の固定バッファモデルが見つかりませんでした");
  }

  /**
   * ダミーの最大風速ベクトルを生成
   * 
   * @param value 基準風速
   * @return ダミーの最大風速ベクトル
   */
  private List<Double> createDummyMaxWindSpeedVector(double value) {
    List<Double> maxWindSpeedVector = new ArrayList<>();
    for (int i = 0; i < 16; i++) {
      maxWindSpeedVector.add(value);
    }
    return maxWindSpeedVector;
  }

  /**
   * 落下空間を計算する。
   *
   * @param calcMethod 計算方式
   * @param fallToleranceRange 最大落下許容範囲
   * @param drones ドローンのリスト
   * @param despersionNode 落下範囲節
   * @param numDivisions 分割数
   * @return 計算された座標リスト
   */
  private List<List<BigDecimal>> computeFallSpace(String calcMethod,
      com.intent_exchange.drone_highway.algo.model.FallToleranceRange fallToleranceRange,
      List<Drone> drones, DespersionNode despersionNode, int numDivisions) {
    FlightGeographyCalculator calculator = switch (calcMethod) {
      // 逆関数方式
      case INVERSE_FUNCTION -> new InverseFunctionCalculator(fallToleranceRange);
      // 空間列挙方式
      case SPACE_ENUMERATION -> new SpaceEnumerationCalculator(fallToleranceRange);
      // 機械学習方式
      case MACHINE_LEARNING -> new MachineLearningCalculator(fallToleranceRange);
      default -> throw new DroneHighwayException("計算方式が指定されていません");
    };

    return calculator.compute(drones, despersionNode, numDivisions);
  }

}

