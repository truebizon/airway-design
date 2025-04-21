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

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intent_exchange.drone_highway.controller.AirwayDesignApiController;
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
import com.intent_exchange.drone_highway.dto.request.FallSpacePostRequestDroneDto;
import com.intent_exchange.drone_highway.dto.request.FallSpacePostRequestDto;
import com.intent_exchange.drone_highway.dto.request.FallToleranceRangePostRequestDto;
import com.intent_exchange.drone_highway.dto.response.AirwayGeometry;
import com.intent_exchange.drone_highway.entity.AirwayEntity;
import com.intent_exchange.drone_highway.entity.AirwayEntityAirway;
import com.intent_exchange.drone_highway.entity.AirwayJunctionsEntity;
import com.intent_exchange.drone_highway.entity.AirwayJunctionsEntityAirwaysInner;
import com.intent_exchange.drone_highway.entity.AirwayJunctionsEntityAirwaysInnerAirway;
import com.intent_exchange.drone_highway.entity.AirwayJunctionsEntityAirwaysInnerAirwayGeometry;
import com.intent_exchange.drone_highway.entity.AirwayJunctionsEntityAirwaysInnerDeviation;
import com.intent_exchange.drone_highway.entity.AirwayMqttConditions;
import com.intent_exchange.drone_highway.entity.AirwaySectionsEntity;
import com.intent_exchange.drone_highway.entity.AirwaysEntity;
import com.intent_exchange.drone_highway.entity.AirwaysGSWPostRequestAirwayPartsEntity;
import com.intent_exchange.drone_highway.entity.AirwaysGSWPostRequestEntity;
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestAirwayJunctionEntity;
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestAirwayPartsEntity;
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestDespersionNodeEntity;
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestDespersionNodeEntityGeometry;
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestDespersionNodeWithFallSpaceEntity;
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestEntity;
import com.intent_exchange.drone_highway.entity.AirwaysPostResponseEntity;
import com.intent_exchange.drone_highway.exception.DataNotFoundException;
import com.intent_exchange.drone_highway.exception.DroneHighwayException;
import com.intent_exchange.drone_highway.model.Airway;
import com.intent_exchange.drone_highway.model.AirwayCompatibleModels;
import com.intent_exchange.drone_highway.model.AirwayDetermination;
import com.intent_exchange.drone_highway.model.AirwayJunction;
import com.intent_exchange.drone_highway.model.AirwaySection;
import com.intent_exchange.drone_highway.model.DespersionNode;
import com.intent_exchange.drone_highway.model.FallSpace;
import com.intent_exchange.drone_highway.model.MappingDroneportSection;
import com.intent_exchange.drone_highway.model.MappingJunctionSection;
import com.intent_exchange.drone_highway.util.PropertyUtil;

@Component
public class AirwayDesignLogic {

  /** 航路状態：新規追加 */
  private static final short AIRWAY_STATUS_REGISTERED = 0;
  /** 航路状態：更新 */
  public static final short AIRWAY_STATUS_UPDATED = AIRWAY_STATUS_REGISTERED + 1;
  /** 航路状態：削除 */
  private static final short AIRWAY_STATUS_DELETED = AIRWAY_STATUS_UPDATED + 1;


  private static final Logger logger = LoggerFactory.getLogger(AirwayDesignApiController.class);

  @Autowired
  private AirwayMapper airwayMapper;
  @Autowired
  private AirwaySectionMapper airwaySectionMapper;
  @Autowired
  private AirwayJunctionMapper airwayJunctionMapper;
  @Autowired
  private DespersionNodeMapper despersionNodeMapper;
  @Autowired
  private MappingJunctionSectionMapper mappingJunctionSectionMapper;
  @Autowired
  private MappingDroneportSectionMapper mappingDroneportSectionMapper;
  @Autowired
  private AirwaySequenceMapper airwaySequenceMapper;
  @Autowired
  private AirwayDeterminationMapper airwayDeterminationMapper;
  @Autowired
  private FallSpaceMapper fallSpaceMapper;
  @Autowired
  private FallToleranceRangeMapper fallToleranceRangeMapper;
  @Autowired
  private FallSpaceLogic fallSpaceLogic;
  @Autowired
  private FallToleranceRangeLogic fallToleranceRangeLogic;
  @Autowired
  private RailwayCrossingInfoLogic railwayCrossingInfoLogic;


  /** クロック */
  @Autowired
  private Clock clock;
  @Autowired
  private AirwayCompatibleModelsMapper airwayCompatibleModelsMapper;

  /** オブジェクトマッパー */
  @Autowired
  private ObjectMapper beanObjectMapper;

  /** MQTTブローカーにTopicを送信する際に使用するプロトコル */
  private static final String MQTT_NETWORK_PROTOCOL = "tcp";

  /** MQTTブローカーの宛先ホスト名 */
  private static final String MQTT_HOST = PropertyUtil.getProperty("mqtt.host");

  /** MQTTブローカーの宛先ポート番号 */
  private static final String MQTT_PORT = PropertyUtil.getProperty("mqtt.port");

  /** MQTTブローカーに送信するTOPIC(航路画定) */
  private static final String MQTT_AIRWAY_TOPIC = PropertyUtil.getProperty("mqtt.topic");

  /**
   * 指定した航路IDの航路情報を取得する。
   *
   * @param airwayId 対象とする航路ID
   * @param all 航路情報をすべて取得するかを指示するフラグ。指定された場合はcorridorIdは無視する
   * @return 航路情報
   */
  @Transactional(readOnly = true)
  public AirwayEntity airwayGet(final List<String> airwayId, final boolean all) {
    return airwayGet(airwayId, all, null, null, null, null, null);
  }

  /**
   * 航路情報を様々な条件で検索し取得する
   * 
   * @param airwayId 対象とする航路IDのリスト
   * @param all 全ての航路情報を取得するかどうかを示すフラグ。trueの場合、airwayIdは無視される
   * @param flightPurpose 航路の飛行目的のリスト
   * @param determinationDateFrom 航路の更新開始日
   * @param determinationDateTo 航路の更新終了日
   * @param businessNumber 検索対象の事業者番号
   * @param areaName 検索対象のエリア名
   * @return 検索条件に一致する航路情報を格納した AirwayEntityオブジェクト
   * @throws JsonProcessingException geometry型の変換に失敗した場合に発生
   */
  @Transactional(readOnly = true)
  public AirwayEntity airwayGet(final List<String> airwayId, final boolean all,
      final List<String> flightPurpose, final String determinationDateFrom,
      final String determinationDateTo, final String businessNumber, final String areaName) {

    AirwayEntity entityAirway = new AirwayEntity();
    AirwayEntityAirway entityAirwayAirway = new AirwayEntityAirway();
    entityAirway.airway(entityAirwayAirway);
    List<AirwaysEntity> airwaysEntity = new ArrayList<>();

    // 日時フォーマットの検証
    try {
      if (determinationDateFrom != null) {
        Instant.parse(determinationDateFrom);
      }
      if (determinationDateTo != null) {
        Instant.parse(determinationDateTo);
      }
    } catch (DateTimeParseException e) {
      throw new DroneHighwayException("日時のフォーマットが違います", e);
    }

    try {
      if ((flightPurpose == null || flightPurpose.isEmpty()) && determinationDateFrom == null
          && determinationDateTo == null && businessNumber == null && areaName == null) {

        if (all) {
          // 全件取得
          retrieveAllAirways(entityAirwayAirway, airwaysEntity);
        } else {
          // 特定ID取得
          retrieveByIdAirways(airwayId, entityAirwayAirway, airwaysEntity);
        }
      } else {
        // オプション検索
        searchAirways(flightPurpose, determinationDateFrom, determinationDateTo, businessNumber,
            areaName, entityAirwayAirway, airwaysEntity);
      }
    } catch (JsonProcessingException e) {
      throw new DroneHighwayException(e.getMessage());
    }

    entityAirwayAirway.airways(airwaysEntity);
    entityAirway.airway(entityAirwayAirway);
    return entityAirway;

  }

  /**
   * 全ての航路情報を取得し、airwaysEntityリストに追加する
   *
   * @param entityAirwayAirway 航路情報を含むエンティティ
   * @param airwaysEntity 取得した航路情報を格納するリスト
   * @throws DataNotFoundException 取得したデータが空の場合にスローされる例外
   */
  private void retrieveAllAirways(AirwayEntityAirway entityAirwayAirway,
      List<AirwaysEntity> airwaysEntity) throws JsonProcessingException {

    List<Airway> modelAirway = airwayMapper.selectAll();
    if (null == modelAirway) {
      throw new DataNotFoundException();
    }

    for (Airway airway : modelAirway) {
      logger.debug("### modelAirway:" + modelAirway.toString());
      airwaysEntity.add(transferSelectedAirwayToResponse(airway));

      if (entityAirwayAirway.getBusinessNumber() == null
          || entityAirwayAirway.getBusinessNumber().isEmpty()) {
        // 航路確定IDから、事業者番号を取得する(航路確定IDは一意になるため、一度飲み取得)
        entityAirwayAirway.businessNumber(getBussinessNumber(airway.getAirwayDeterminationId()));
        entityAirwayAirway
            .airwayAdministratorId(getOperatorId(entityAirwayAirway.getBusinessNumber()));
      }
    }
  }

  /**
   * 特定のIDに基づいて航路情報を取得し、airwaysEntityリストに追加する
   *
   * @param airwayId 対象とする航路IDのリスト
   * @param entityAirwayAirway 航路情報を含むエンティティ
   * @param airwaysEntity 取得した航路情報を格納するリスト
   * @throws DataNotFoundException 指定されたIDに対するデータが存在しない場合にスローされる例外
   */
  private void retrieveByIdAirways(List<String> airwayId, AirwayEntityAirway entityAirwayAirway,
      List<AirwaysEntity> airwaysEntity) throws JsonProcessingException {

    for (String id : airwayId) {

      Airway modelAirway = airwayMapper.selectByPrimaryKey(id);
      if (null == modelAirway) {
        throw new DataNotFoundException();
      }
      logger.debug("### modelAirway:" + modelAirway.toString());

      airwaysEntity.add(transferSelectedAirwayToResponse(modelAirway));

      if (entityAirwayAirway.getBusinessNumber() == null
          || entityAirwayAirway.getBusinessNumber().isEmpty()) {
        // 航路確定IDから、事業者番号を取得する(航路確定IDは一意になるため、一度飲み取得)
        entityAirwayAirway
            .businessNumber(getBussinessNumber(modelAirway.getAirwayDeterminationId()));
        entityAirwayAirway
            .airwayAdministratorId(getOperatorId(entityAirwayAirway.getBusinessNumber()));
      }
    }
  }

  /**
   * オプション検索。指定された条件に基づいて航路情報を取得する
   * 
   * @param flightPurpose 飛行目的のリスト
   * @param determinationDateFrom 画定履歴の検索開始日
   * @param determinationDateTo 画定履歴の検索終了日
   * @param businessNumber 事業者番号
   * @param areaName エリア名
   * @param entityAirwayAirway 航路情報を含むエンティティ
   * @param airwaysEntity 取得した航路情報を格納するリスト
   * @throws DataNotFoundException 一致するデータが見つからない場合にスローされる例外
   */
  private void searchAirways(List<String> flightPurpose, String determinationDateFrom,
      String determinationDateTo, String businessNumber, String areaName,
      AirwayEntityAirway entityAirwayAirway, List<AirwaysEntity> airwaysEntity)
      throws JsonProcessingException {

    // 飛行目的が指定されていない場合は、空の結果を返す
    if (flightPurpose == null || flightPurpose.isEmpty()) {
      return;
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    LocalDateTime dateFrom =
        (determinationDateFrom != null) ? LocalDateTime.parse(determinationDateFrom, formatter)
            : null;
    LocalDateTime dateTo =
        (determinationDateTo != null)
            ? LocalDateTime.parse(determinationDateTo, formatter)
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999_000_000)
            : null;


    List<Airway> modelAirways =
        airwayMapper.selectWithFilters(flightPurpose, dateFrom, dateTo, businessNumber, areaName);
    if (modelAirways == null || modelAirways.isEmpty()) {
      throw new DataNotFoundException();
    }

    for (Airway airway : modelAirways) {
      airwaysEntity.add(transferSelectedAirwayToResponse(airway));

      if (entityAirwayAirway.getBusinessNumber() == null
          || entityAirwayAirway.getBusinessNumber().isEmpty()) {
        entityAirwayAirway.businessNumber(getBussinessNumber(airway.getAirwayDeterminationId()));
        entityAirwayAirway
            .airwayAdministratorId(getOperatorId(entityAirwayAirway.getBusinessNumber()));
      }
    }
  }

  /**
   * 航路確定IDから事業者番号を取得する
   * 
   * @param airwayDeterminationId 航路確定ID
   * @return 事業者番号
   */
  private final String getBussinessNumber(final int airwayDeterminationId) {
    final AirwayDetermination airwayDetermination =
        airwayDeterminationMapper.selectByPrimaryKey(airwayDeterminationId);
    if (null == airwayDetermination) {
      throw new DataNotFoundException();
    }
    return airwayDetermination.getBusinessNumber();
  }

  /**
   * 航路確定IDから航路運営者IDを取得する
   * 
   * @param bussinessNumber
   * @return 航路運営者ID
   */
  private final String getOperatorId(final String bussinessNumber) {
    // 航路運営者IDの取得
    final String airwayOperatorId = fallToleranceRangeMapper.selectForOperatorId(bussinessNumber);
    if (null == airwayOperatorId) {
      throw new DataNotFoundException();
    }
    return airwayOperatorId;
  }

  /**
   * 抽出した単一の航路情報をレスポンス用に詰め替える
   * 
   * @param modelAirway DBから取得した航路情報
   * @return レスポンスに変換した航路情報
   * @throws JsonProcessingException geometry型の変換に失敗した場合に発生
   */
  private AirwaysEntity transferSelectedAirwayToResponse(final Airway modelAirway)
      throws JsonProcessingException {

    logger.debug("## AirwayId          : " + modelAirway.getAirwayId());
    logger.debug("## Name              : " + modelAirway.getName());
    logger.debug("## ParentNodeAirwayId: " + modelAirway.getParentNodeAirwayId());

    AirwaysEntity airways = new AirwaysEntity();
    airways.airwayId(modelAirway.getAirwayId());
    airways.airwayName(modelAirway.getName());
    airways.flightPurpose(modelAirway.getFlightPurpose());
    airways.createdAt(
        Date.from(ZonedDateTime.of(modelAirway.getCreatedAt(), clock.getZone()).toInstant()));
    airways.updatedAt(
        Date.from(ZonedDateTime.of(modelAirway.getUpdatedAt(), clock.getZone()).toInstant()));

    // 航路を利用可能なドローンの機体種別IDリストの取得
    List<Integer> droneIds = new ArrayList<>();
    for (AirwayCompatibleModels droneModel : airwayCompatibleModelsMapper
        .selectByAirwayDeterminationId(modelAirway.getAirwayDeterminationId())) {
      droneIds.add(droneModel.getAircraftInfoId());
    }
    airways.droneList(droneIds);

    // 航路区画の取得
    final List<AirwaySection> airwaySections =
        airwaySectionMapper.selectByAirwayId(airways.getAirwayId());
    airways.airwaySections(getAirwaySection(airwaySections));

    // ジャンクションの取得
    final List<AirwayJunction> airwayJunction =
        airwayJunctionMapper.selectByAirwayId(airways.getAirwayId());
    airways.airwayJunctions(getAirwayJunction(airwayJunction));

    return airways;
  }


  /**
   * 取得した航路区画情報をレスポンスに変換する
   * 
   * @param airwaySections DBから取得した航路区画
   * @return 変換した航路区画のリスト
   */
  private List<AirwaySectionsEntity> getAirwaySection(final List<AirwaySection> airwaySections) {

    List<AirwaySectionsEntity> list = new ArrayList<>();

    for (AirwaySection section : airwaySections) {
      AirwaySectionsEntity entityAirwaySections = new AirwaySectionsEntity();
      entityAirwaySections.airwaySectionId(section.getAirwaySectionId().toString());
      entityAirwaySections.airwaySectionName(section.getName());

      logger.debug("## AirwayId    : " + section.getAirwayId());
      logger.debug("## SectionId   : " + section.getAirwaySectionId());
      logger.debug("## SectionName : " + section.getName());

      // マッピングの取得(0件は基本ないはず)
      final List<MappingJunctionSection> mappingJunctionSection =
          mappingJunctionSectionMapper.selectByAirwaySectionId(section.getAirwaySectionId());

      for (MappingJunctionSection mappingJuncton : mappingJunctionSection) {
        entityAirwaySections
            .addAirwayJunctionIdsItem(mappingJuncton.getAirwayJunctionId().toString());
      }

      // ドローンポートとのマッピング取得(0件もあり)
      final List<MappingDroneportSection> mappingDroneportSection =
          mappingDroneportSectionMapper.selectByAirwaySectionId(section.getAirwaySectionId());
      if (mappingDroneportSection.isEmpty()) {
        // 0件の場合はnullを設定
        entityAirwaySections.setDroneportIds(null);
      } else {
        // 1件以上の場合はドローンポートIDを取得
        for (MappingDroneportSection mappingDroneport : mappingDroneportSection) {
          entityAirwaySections.addDroneportIdsItem(mappingDroneport.getDroneportId().toString());
          logger.debug("## DroneportId : " + mappingDroneport.getDroneportId().toString());
        }
      }

      list.add(entityAirwaySections);

    }

    return list;
  }

  /**
   * 取得したジャンクション情報をレスポンスに変換する
   * 
   * @param airwayJunction DBから取得したジャンクション情報
   * @return 変換したジャンクションのリスト
   * @throws JsonMappingException Jsonデータからクラスメンバへマッピングできない場合に発生する例外
   * @throws JsonProcessingException geometry型の変換に失敗した場合に発生
   */
  private List<AirwayJunctionsEntity> getAirwayJunction(final List<AirwayJunction> airwayJunction)
      throws JsonMappingException, JsonProcessingException {

    List<AirwayJunctionsEntity> list = new ArrayList<>();
    for (AirwayJunction junction : airwayJunction) {

      // airway.airways.airwayJunctions(ジャンクション)
      AirwayJunctionsEntity entityAirwayJunctions = new AirwayJunctionsEntity();
      entityAirwayJunctions.airwayJunctionId(junction.getAirwayJunctionId().toString());
      entityAirwayJunctions.airwayJunctionName(junction.getName());

      logger.debug("## JunctionId   : " + entityAirwayJunctions.getAirwayJunctionId());
      logger.debug("## JunctionName : " + entityAirwayJunctions.getAirwayJunctionName());

      // airway.airways.airwayJunctions.airways(ジャンクション内のairwaysスコープ)
      AirwayJunctionsEntityAirwaysInner inner = new AirwayJunctionsEntityAirwaysInner();

      // airway.airways.airwayJunctions.airways.airway(airwayスコープ内、航路スコープ) --------------
      final AirwayJunctionsEntityAirwaysInnerAirwayGeometry innerAirwayGeometry =
          createInnerAirway(junction.getGeometry());
      AirwayJunctionsEntityAirwaysInnerAirway innerAirway =
          new AirwayJunctionsEntityAirwaysInnerAirway();
      innerAirway.geometry(innerAirwayGeometry);

      // airway.airways.airwayJunctions.airways.airway(airwayスコープ内、逸脱範囲スコープ) ----------
      final AirwayJunctionsEntityAirwaysInnerAirwayGeometry innerDeviationGeometry =
          createInnerAirway(junction.getDeviationGeometry());
      final AirwayJunctionsEntityAirwaysInnerDeviation innerDeviation =
          new AirwayJunctionsEntityAirwaysInnerDeviation();
      innerDeviation.geometry(innerDeviationGeometry);

      inner.airway(innerAirway);
      inner.deviation(innerDeviation);

      entityAirwayJunctions.addAirwaysItem(inner);
      list.add(entityAirwayJunctions);
    }

    return list;
  }

  /**
   * ジャンクションの位置情報を作成する
   * 
   * @param airwayGeometry DBから取得した位置情報を持つオブジェクト
   * @return ジャンクションの位置情報を持つオブジェクト
   * @throws JsonMappingException Jsonデータからクラスメンバへマッピングできない場合に発生する例外
   * @throws JsonProcessingException geometry型の変換に失敗した場合に発生
   */
  private AirwayJunctionsEntityAirwaysInnerAirwayGeometry createInnerAirway(Object airwayGeometry)
      throws JsonMappingException, JsonProcessingException {

    // SELECTした位置情報を、Objectからクラスインスタンスに変換する
    final AirwayGeometry geometry = convertObjToGeometry(airwayGeometry);
    logger.debug("## geometry TYPE : " + geometry.getType());

    AirwayJunctionsEntityAirwaysInnerAirwayGeometry innerAirwayGeometry =
        new AirwayJunctionsEntityAirwaysInnerAirwayGeometry();
    innerAirwayGeometry.type("Polygon");
    innerAirwayGeometry.setCoordinates(geometry.getCoordinates().get(0));

    return innerAirwayGeometry;
  }

  /**
   * 指定した航路を追加する(航路確定テーブル、最大落下許容範囲テーブルは作成済み)
   * 
   * @param airwaysRequest 登録する航路のデータ
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public AirwaysPostResponseEntity airwayPost(final AirwaysPostRequestEntity airwaysRequest) {

    // コンテキスト単位で日時を統一するためにここで時刻を作成する
    final LocalDateTime currentDateTimeUTC = getCurrentDateTimeUTC();

    // 航路確定ID
    final int airwayDeterminationId = airwaysRequest.getAirwayDeterminationId();

    // 事業者番号の取得
    final String bussinessNumber = getBussinessNumber(airwayDeterminationId);

    // 航路ID採番
    final String newAirwayId = bussinessNumber + "_" + UUID.randomUUID().toString();

    // 航路テーブルの登録
    Airway airWay = new Airway();
    airWay.setAirwayId(newAirwayId);
    airWay.setAirwayDeterminationId(airwaysRequest.getAirwayDeterminationId());
    airWay.setName(airwaysRequest.getAirwayName());
    airWay.setFlightPurpose(airwaysRequest.getFlightPurpose());
    airWay.setCreatedAt(currentDateTimeUTC);
    airWay.setUpdatedAt(currentDateTimeUTC);
    airwayMapper.insert(airWay);

    Integer prevPartsIndex = null;
    ArrayList<String> curJunctionIds = new ArrayList<>();
    ArrayList<ArrayList<String>> prevJunctionIds = new ArrayList<ArrayList<String>>();

    // 航路パーツの登録
    for (AirwaysPostRequestAirwayPartsEntity airwayElement : airwaysRequest.getAirwayParts()) {
      try {
        // 落下範囲節の登録
        int despersionNodeId = insertAirwayDespersionNode(airwayElement.getDespersionNode(),
            airwayDeterminationId, currentDateTimeUTC);

        // 落下空間の更新
        updateFallSpace(airwayElement.getDespersionNode().getFallSpaceCrossSectionId(),
            despersionNodeId, currentDateTimeUTC);

        // ひとつ前のパーツインデックスを保持
        prevPartsIndex = airwayElement.getPrevAirwayPartsIndex();

        // 航路ジャンクションの登録
        curJunctionIds = insertAirwayJunction(airwayElement.getAirwayJunction(), newAirwayId,
            despersionNodeId, currentDateTimeUTC);
        if (prevPartsIndex != null) {
          // 区画とジャンクションのマッピング登録
          insertAirwaySectionAndMapping(prevJunctionIds.get(prevPartsIndex), curJunctionIds,
              newAirwayId,
              airwaysRequest.getAirwayParts().get(prevPartsIndex).getAirwaySection().getName(),
              currentDateTimeUTC);
        }

        // ひとつ前のジャンクションIDを記録
        prevJunctionIds.add(curJunctionIds);

      } catch (JsonProcessingException e) {
        throw new DroneHighwayException(e.getMessage());
      }
    }

    // 路線と航路との交点情報の登録
    railwayCrossingInfoLogic.insertRelativePosition(airwayDeterminationId, newAirwayId,
        currentDateTimeUTC);

    notifyAirwayUpdated(newAirwayId, currentDateTimeUTC, currentDateTimeUTC,
        AIRWAY_STATUS_REGISTERED);

    return new AirwaysPostResponseEntity().airwayId(newAirwayId);

  }

  /**
   * 指定した航路(GSW)を追加する
   * 
   * @param airwaysGSWPostRequestEntity
   * @return
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public AirwaysPostResponseEntity airwayPostGSW(
      AirwaysGSWPostRequestEntity airwaysGSWPostRequestEntity) {

    final LocalDateTime now = LocalDateTime.now(clock);

    // 最大落下許容範囲登録
    FallToleranceRangePostRequestDto fallToleranceRangePostRequestDto =
        new ObjectMapper().convertValue(airwaysGSWPostRequestEntity.getFallToleranceRange(),
            FallToleranceRangePostRequestDto.class);
    final String fallToleranceRangeId =
        fallToleranceRangeLogic.insert(fallToleranceRangePostRequestDto).getFallToleranceRangeId();

    // 機体グループの登録
    FallSpacePostRequestDto fallSpacePostRequestDto = new FallSpacePostRequestDto();

    fallSpacePostRequestDto.setFallToleranceRangeId(fallToleranceRangeId);
    fallSpacePostRequestDto.setNumCrossSectionDivisions(0); // 落下空間は計算済みのデータを登録するため、0を設定する

    List<FallSpacePostRequestDroneDto> droneList = airwaysGSWPostRequestEntity.getDroneList()
        .stream()
        .map(droneEntity -> new ObjectMapper().convertValue(droneEntity,
            FallSpacePostRequestDroneDto.class))
        .collect(Collectors.toList());
    fallSpacePostRequestDto.setDroneList(droneList);
    final int airwayDeterminationId =
        fallSpaceLogic.basicInfoRegistration(fallSpacePostRequestDto).getAirwayDeterminationId();

    // 落下空間の登録
    FallSpace fallSpace = new FallSpace();
    fallSpace.setAirwayDeterminationId(airwayDeterminationId);

    List<AirwaysPostRequestAirwayPartsEntity> airwaysPostRequestAirwayPartsEntityList =
        new ArrayList<>();
    for (AirwaysGSWPostRequestAirwayPartsEntity parts : airwaysGSWPostRequestEntity
        .getAirwayParts()) {
      AirwaysPostRequestDespersionNodeWithFallSpaceEntity despersionNode =
          parts.getDespersionNode();
      List<List<BigDecimal>> data = despersionNode.getFallSpace().getData();

      try {
        fallSpace.setData(new ObjectMapper().writeValueAsString(data));
      } catch (JsonProcessingException e) {
        throw new DroneHighwayException(e.getMessage());
      }

      final int fallSpaceId = airwaySequenceMapper.selectFallSpaceIdSeqNextVal();
      fallSpace.setFallSpaceId(fallSpaceId);
      fallSpace.setCreatedAt(now);
      fallSpace.setUpdatedAt(now);
      fallSpaceMapper.insert(fallSpace);

      // 航路用のデータを作成(採番された落下空間IDを設定)
      AirwaysPostRequestAirwayPartsEntity entity = new AirwaysPostRequestAirwayPartsEntity();
      entity.prevAirwayPartsIndex(parts.getPrevAirwayPartsIndex());
      entity.airwayJunction(parts.getAirwayJunction());
      entity.airwaySection(parts.getAirwaySection());

      AirwaysPostRequestDespersionNodeEntity dNodeEntity =
          new AirwaysPostRequestDespersionNodeEntity();
      dNodeEntity.setName(parts.getDespersionNode().getName());
      dNodeEntity
          .setGeometry(new ObjectMapper().convertValue(parts.getDespersionNode().getGeometry(),
              AirwaysPostRequestDespersionNodeEntityGeometry.class));
      dNodeEntity.setFallSpaceCrossSectionId(fallSpaceId);
      entity.setDespersionNode(dNodeEntity);

      airwaysPostRequestAirwayPartsEntityList.add(entity);
    }

    // 航路の登録
    AirwaysPostRequestEntity airwaysPostRequestEntity = new AirwaysPostRequestEntity();
    airwaysPostRequestEntity.setAirwayDeterminationId(airwayDeterminationId);
    airwaysPostRequestEntity.setAirwayName(airwaysGSWPostRequestEntity.getAirwayName());
    airwaysPostRequestEntity.setFlightPurpose(airwaysGSWPostRequestEntity.getFlightPurpose());
    airwaysPostRequestEntity.setAirwayParts(airwaysPostRequestAirwayPartsEntityList);

    return this.airwayPost(airwaysPostRequestEntity);
  }

  /**
   * 落下空間の更新
   * 
   * @param fallSpaceCrossSectionId 更新対象になる落下空間ID
   * @param despersionNodeId 落下範囲節ID
   * @param currentDateTimeUTC 現在時刻
   */
  private void updateFallSpace(final int fallSpaceCrossSectionId, final int despersionNodeId,
      final LocalDateTime currentDateTimeUTC) {
    FallSpace fallSpace = new FallSpace();
    fallSpace.setFallSpaceId(fallSpaceCrossSectionId);
    fallSpace.setDespersionNodeId(despersionNodeId);
    fallSpace.setUpdatedAt(currentDateTimeUTC);
    fallSpaceMapper.updateByPrimaryKeySelective(fallSpace);
  }

  /**
   * 落下範囲節の登録
   * 
   * @param reqestNodeList 登録する落下範囲節のリスト
   * @param airwayDeterminationId 航路確定ID
   * @param currentDateTimeUTC 現在時刻
   * @return 登録された落下範囲節のIDのリスト
   * @throws JsonProcessingException geometry型の変換に失敗した場合に発生
   */
  private int insertAirwayDespersionNode(final AirwaysPostRequestDespersionNodeEntity reqestNode,
      final int airwayDeterminationId, final LocalDateTime currentDateTimeUTC)
      throws JsonProcessingException {

    // 落下範囲説IDリスト
    int despersionNodeId = 0;

    // 落下範囲節IDの採番
    final Integer seqVal = airwaySequenceMapper.selectDespersionNodeIdSeqNextVal();

    despersionNodeId = seqVal;

    DespersionNode insertNode = new DespersionNode();
    insertNode.setAirwayDeterminationId(airwayDeterminationId);
    insertNode.setDespersionNodeId(seqVal);
    insertNode.setName(reqestNode.getName());
    insertNode.setGeometry(convertGeometryToGeoJson(reqestNode.getGeometry()));
    insertNode.setCreatedAt(currentDateTimeUTC);
    insertNode.setUpdatedAt(currentDateTimeUTC);
    despersionNodeMapper.insert(insertNode);

    return despersionNodeId;
  }

  /**
   * 航路ジャンクション登録
   * 
   * @param requestJunctionList 登録するジャンクションのリスト
   * @param newAirwayId 新規に登録する航路ID
   * @param despersionNodeIdList 登録する落下範囲節のリスト
   * @return 登録したジャンクションIDのリスト
   * @throws JsonProcessingException geometry型の変換に失敗した場合に発生
   */
  private ArrayList<String> insertAirwayJunction(
      final List<AirwaysPostRequestAirwayJunctionEntity> requestJunctionList,
      final String newAirwayId, final int despersionNodeId, final LocalDateTime currentDateTimeUTC)
      throws JsonProcessingException {

    ArrayList<String> newJunctionIdList = new ArrayList<>();

    // 航路ジャンクションの登録
    for (AirwaysPostRequestAirwayJunctionEntity requestJunction : requestJunctionList) {

      // ジャンクションIDの採番
      final String seqValJunction = UUID.randomUUID().toString();

      // 採番したIDを記録
      newJunctionIdList.add(seqValJunction);
      logger.debug("## add Junction Id : " + seqValJunction);

      AirwayJunction insertJunction = new AirwayJunction();
      insertJunction.setAirwayId(newAirwayId);
      insertJunction.setAirwayJunctionId(seqValJunction);
      insertJunction.setDespersionNodeId(despersionNodeId);
      insertJunction.setName(requestJunction.getName());
      insertJunction.setGeometry(convertGeometryToGeoJson(requestJunction.getGeometry()));
      insertJunction
          .setDeviationGeometry(convertGeometryToGeoJson(requestJunction.getDeviationGeometry()));
      insertJunction.setCreatedAt(currentDateTimeUTC);
      insertJunction.setUpdatedAt(currentDateTimeUTC);

      airwayJunctionMapper.insert(insertJunction);
    }

    return newJunctionIdList;
  }

  /**
   * 航路区画とマッピングテーブルにデータを登録する
   * 
   * @param beforeJunctionId 接続元のジャンクションID
   * @param newAirwayId 新規に登録する航路ID
   * @param seqValJunction 新規に登録するジャンクションID
   * @param sectionName 新規に登録する区画名
   */
  private void insertAirwaySectionAndMapping(final List<String> prevJunctionIds,
      final List<String> curJunctionIds, final String newAirwayId, final String sectionName,
      final LocalDateTime currentDateTimeUTC) {
    logger.debug("## beforeJunctionId : " + prevJunctionIds);

    // 航路区画IDの採番
    final String seqValSection = UUID.randomUUID().toString();

    // 航路区画の登録
    AirwaySection airwaySection = new AirwaySection();
    airwaySection.setAirwaySectionId(seqValSection);
    airwaySection.setAirwayId(newAirwayId);
    airwaySection.setName(sectionName);
    airwaySection.setCreatedAt(currentDateTimeUTC);
    airwaySection.setUpdatedAt(currentDateTimeUTC);
    airwaySectionMapper.insert(airwaySection);

    // 登録するジャンクションのひとつ前のジャンクションと区画のマッピングを登録
    for (String id : prevJunctionIds) {
      insertMappingJunctionSection(id, seqValSection, currentDateTimeUTC);
    }

    // 新しく登録するジャンクションと区画のマッピングを登録
    for (String id : curJunctionIds) {
      insertMappingJunctionSection(id, seqValSection, currentDateTimeUTC);
    }
  }

  /**
   * ジャンクションマッピングの登録
   * 
   * @param airwayJunctionId マッピングテーブルに登録するジャンクションのID
   * @param airwaySectionId マッピングテーブルに登録するセクションのID
   * @param currentDateTimeUTC 現在時刻
   */
  private void insertMappingJunctionSection(final String airwayJunctionId,
      final String airwaySectionId, final LocalDateTime currentDateTimeUTC) {
    // ジャンクションマッピングIDの採番
    final Integer seqValMapping = airwaySequenceMapper.selectMappingJunctionSectionIdSeqNextVal();

    // 登録するジャンクションのひとつ前のジャンクションを登録
    MappingJunctionSection mappingJunctionSection = new MappingJunctionSection();
    mappingJunctionSection.setMappingJunctionSectionId(seqValMapping);
    mappingJunctionSection.setAirwayJunctionId(airwayJunctionId);
    mappingJunctionSection.setAirwaySectionId(airwaySectionId);
    mappingJunctionSection.setCreatedAt(currentDateTimeUTC);
    mappingJunctionSection.setUpdatedAt(currentDateTimeUTC);
    mappingJunctionSectionMapper.insert(mappingJunctionSection);
  }

  /**
   * Geometry列のデータを航路情報用のクラスに変換する
   * 
   * @param geometry 変換するgeometryデータ
   * @return gemometry列のデータを変換したAirwayGeometryクラスインスタンス
   * @throws JsonProcessingException Json変換時に発生する例外
   * @throws JsonMappingException Jsonデータからクラスメンバへマッピングできない場合に発生する例外
   */
  private AirwayGeometry convertObjToGeometry(Object geometry)
      throws JsonMappingException, JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(geometry.toString(), AirwayGeometry.class);

  }

  /**
   * geometry列のデータを文字列に変換する
   * 
   * @param geometry 変換するgeometryデータ
   * @return 変換された文字列
   * @throws JsonProcessingException Json変換時に発生する例外
   */
  private String convertGeometryToGeoJson(Object geometry) throws JsonProcessingException {
    return new ObjectMapper().writeValueAsString(geometry);
  }

  /**
   * 航路情報の登録/更新/削除を通知する
   * 
   * @param airwayId 対象になる航路ID
   * @param registeredDateTime 登録日時
   * @param updatedDateTime 更新日時
   * @param status ステータス
   */
  public void notifyAirwayUpdated(String airwayId, LocalDateTime registeredDateTime,
      LocalDateTime updatedDateTime, short status) {
    AirwayEntityAirway airwayEntity = airwayGet(List.of(airwayId), false).getAirway();
    notifyAirwayUpdated(airwayId, registeredDateTime, updatedDateTime, status, airwayEntity);
  }

  /**
   * 航路情報の登録/更新/削除を通知する
   * 
   * @param airwayId 対象になる航路ID
   * @param registeredDateTime 登録日時
   * @param updatedDateTime 更新日時
   * @param status ステータス
   * @param airwayEntity 航路エンティティ
   */
  private void notifyAirwayUpdated(final String airwayId, final LocalDateTime registeredDateTime,
      final LocalDateTime updatedDateTime, final short status, AirwayEntityAirway airwayEntity) {

    final int qos = 2; // subscriberに1回のみ到達を保証させるために、2を設定する
    MqttClient client = null;
    Function<LocalDateTime, String> function = (localDateTime) -> {
      java.util.Date date = Date.from(ZonedDateTime.of(localDateTime, clock.getZone()).toInstant());
      return beanObjectMapper.getDateFormat().format(date);
    };
    try {
      AirwayMqttConditions airwayMqttConditions = new AirwayMqttConditions();
      airwayMqttConditions.setAirwayAdministratorId(airwayEntity.getAirwayAdministratorId());
      airwayMqttConditions.setRegisteredAt(function.apply(registeredDateTime));
      airwayMqttConditions.setUpdatedAt(
          AIRWAY_STATUS_REGISTERED == status ? StringUtils.EMPTY : function.apply(updatedDateTime));

      if (status == AIRWAY_STATUS_DELETED) {
        airwayMqttConditions.setAirway(null);
      } else {
        airwayMqttConditions.setAirway(airwayEntity.getAirways().get(0));
      }

      final String airwayEntityJson = beanObjectMapper.writeValueAsString(airwayMqttConditions);

      client = new MqttClient(MQTT_NETWORK_PROTOCOL + "://" + MQTT_HOST + ":" + MQTT_PORT,
          "Publisher", new MemoryPersistence());
      MqttMessage message = new MqttMessage(airwayEntityJson.getBytes(StandardCharsets.UTF_8));
      message.setQos(qos);

      MqttConnectOptions opt = new MqttConnectOptions();
      opt.setCleanSession(false);
      client.connect(opt);
      client.publish(MQTT_AIRWAY_TOPIC.replace("#", "") + airwayId, message);
      client.disconnect();
      client.close();

      logger.debug("[MQTT] -> MsgBroker: airway/administrator/airwayInformation/" + airwayId);
      logger.debug("[MQTT] -> " + airwayEntityJson);

    } catch (MqttException e) {
      throw new DroneHighwayException(e.getMessage());
    } catch (JsonProcessingException e) {
      throw new DroneHighwayException(e.getMessage());
    } finally {
      if (client != null && client.isConnected()) {
        try {
          client.disconnect();
          client.close();
        } catch (MqttException e) {
          throw new DroneHighwayException(e.getMessage());
        }
      }
    }
  }

  /**
   * 現在日時を得る
   * 
   * @return 現在日時
   */
  private LocalDateTime getCurrentDateTimeUTC() {
    return LocalDateTime.now(clock);
  }

  /**
   * 引数で指定された情報を削除する
   * 
   * @param airwayDeleteRequestDto 航路確定/航路削除DTO
   * @throws DataNotFoundException データが存在しない場合
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public void delete(AirwayDeleteRequestDto airwayDeleteRequestDto) {
    Airway airway = airwayMapper.selectByPrimaryKey(airwayDeleteRequestDto.getAirwayId());
    if (Objects.isNull(airway)) {
      throw new DataNotFoundException();
    }
    AirwayDetermination airwayDetermination =
        airwayDeterminationMapper.selectLockByPrimaryKey(airway.getAirwayDeterminationId());
    if (Objects.isNull(airwayDetermination)) {
      throw new DataNotFoundException();
    }
    if (airwayDetermination.getDelete()) {
      throw new DataNotFoundException();
    }
    AirwayEntityAirway airwayEntity = airwayGet(List.of(airway.getAirwayId()), false).getAirway();
    LocalDateTime now = getCurrentDateTimeUTC();
    airwayDetermination.setDelete(true);
    airwayDetermination.setUpdatedAt(now);
    airwayDeterminationMapper.updateByPrimaryKey(airwayDetermination);
    notifyAirwayUpdated(airway.getAirwayId(), airway.getCreatedAt(), now, AIRWAY_STATUS_DELETED,
        airwayEntity);
  }

}

