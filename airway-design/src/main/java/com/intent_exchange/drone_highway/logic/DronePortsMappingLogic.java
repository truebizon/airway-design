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

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.intent_exchange.drone_highway.controller.AirwayDesignApiController;
import com.intent_exchange.drone_highway.dao.AirwayMapper;
import com.intent_exchange.drone_highway.dao.AirwaySequenceMapper;
import com.intent_exchange.drone_highway.dao.MappingDroneportSectionMapper;
import com.intent_exchange.drone_highway.dto.request.DronePortsMappingPostRequestDto;
import com.intent_exchange.drone_highway.dto.request.DronePortsMappingPostRequestDtoAirwaySectionsInner;
import com.intent_exchange.drone_highway.exception.DataNotFoundException;
import com.intent_exchange.drone_highway.model.Airway;
import com.intent_exchange.drone_highway.model.MappingDroneportSection;

/**
 * DronePortsMappingLogicは、ドローンポートマッピングのビジネスロジックを処理します。 このロジッククラスは、ドローンポートマッピングの操作を行います。
 */
@Component
public class DronePortsMappingLogic {

  /** クロック */
  @Autowired
  private Clock clock;

  private static final Logger logger = LoggerFactory.getLogger(AirwayDesignApiController.class);

  @Autowired
  private AirwayMapper airwayMapper;

  @Autowired
  private MappingDroneportSectionMapper mappingDroneportSectionMapper;

  @Autowired
  private AirwaySequenceMapper airwaySequenceMapper;

  @Autowired
  private AirwayDesignLogic airwayDesignLogic;

  /**
   * 航路区画IDに紐づいたドローンポートIDを登録する
   * 
   * @param DronePortsMappingRequest 登録するドローンポートマッピングデータ
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public void dronePortsMappingPost(
      final DronePortsMappingPostRequestDto dronePortsMappingRequest) {
    // 航路区画IDのリストを生成
    List<String> airwaySectionIdList = extractAirwaySectionIds(dronePortsMappingRequest);

    // チェック処理
    validateAirwaySections(airwaySectionIdList, dronePortsMappingRequest.getAirwayId());

    // 日時を統一するためにここで時刻を作成する
    final LocalDateTime currentDateTimeUTC = getCurrentDateTimeUTC();
    // データベースの更新
    updateDronePortMappings(airwaySectionIdList, dronePortsMappingRequest, currentDateTimeUTC);

    // 航路情報を取得してMQTT通知
    notifyAirwayUpdate(dronePortsMappingRequest.getAirwayId(), currentDateTimeUTC);
  }

  /**
   * 航路区画IDのリストを抽出します。
   *
   * @param request ドローンポートマッピングリクエストデータ
   * @return 航路区画IDのリスト
   */
  private List<String> extractAirwaySectionIds(DronePortsMappingPostRequestDto request) {
    List<String> airwaySectionIdList = new ArrayList<>();
    for (DronePortsMappingPostRequestDtoAirwaySectionsInner section : request.getAirwaySections()) {
      airwaySectionIdList.add(section.getAirwaySectionId());
    }
    return airwaySectionIdList;
  }

  /**
   * 航路IDと航路区画IDの存在と整合性をチェックします。
   * 
   * @param airwaySectionIdList チェックする航路区画IDのリスト
   * @param airwayId チェックする航路ID
   * @throws DataNotFoundException 航路区画IDまたは航路IDが存在しない場合にスローされます
   */
  private void validateAirwaySections(List<String> airwaySectionIdList, String airwayId) {
    if (mappingDroneportSectionMapper.countAirwaySectionIds(airwaySectionIdList,
        airwayId) != airwaySectionIdList.size()) {
      throw new DataNotFoundException();
    }
  }

  /**
   * 航路区画IDに関連付けられたドローンポートIDを削除し、新たに登録します。
   * 
   * @param airwaySectionIdList 登録/削除対象の航路区画IDリスト
   * @param request ドローンポートマッピングリクエストデータ
   * @param currentDateTimeUTC 現在日時
   */
  private void updateDronePortMappings(List<String> airwaySectionIdList,
      DronePortsMappingPostRequestDto request, LocalDateTime currentDateTimeUTC) {
    // 削除処理を行う(航路区画IDにてドローンポート/航路区画マッピングテーブルデータを削除)
    mappingDroneportSectionMapper.deleteByAirwaySectionId(airwaySectionIdList);

    // 登録処理を行う
    for (DronePortsMappingPostRequestDtoAirwaySectionsInner airwaySection : request
        .getAirwaySections()) {
      if (airwaySection.getDroneportIds() != null && !airwaySection.getDroneportIds().isEmpty()) {
        for (String dronePortId : airwaySection.getDroneportIds()) {
          insertDronePortsMapping(airwaySection.getAirwaySectionId(), dronePortId,
              currentDateTimeUTC);
          logger.debug("## insertDronePortsMapping: 『" + airwaySection.getAirwaySectionId() + "⇔"
              + dronePortId + "』");
        }
      }
    }
  }

  /**
   * 指定された航路IDの更新をMQTTに通知します。
   * 
   * @param airwayId 更新通知を行う航路ID
   * @param currentDateTimeUTC 現在日時
   */
  private void notifyAirwayUpdate(String airwayId, LocalDateTime currentDateTimeUTC) {
    // 航路情報を取得し、MQTTへ更新通知を送信する
    Airway airway = airwayMapper.selectByPrimaryKey(airwayId);
    airwayDesignLogic.notifyAirwayUpdated(airwayId, airway.getCreatedAt(), currentDateTimeUTC,
        AirwayDesignLogic.AIRWAY_STATUS_UPDATED);
  }

  /**
   * ドローンポートマッピング情報の登録
   * 
   * @param airwaySectionId 登録する航路区画ID
   * @param dronePortId 登録するドローンポートID
   * @param currentDateTimeUTC 現在日時
   */
  private void insertDronePortsMapping(String airwaySectionId, String dronePortId,
      LocalDateTime currentDateTimeUTC) {
    MappingDroneportSection mappingDroneportSection = new MappingDroneportSection();
    // シーケンス番号の取得
    final Integer seqVal = airwaySequenceMapper.selectMappingDroneportSectionIdSeqNextVal();
    mappingDroneportSection.setMappingDroneportSectionId(seqVal);

    // 登録する航路区画ID
    mappingDroneportSection.setAirwaySectionId(airwaySectionId);

    // 登録するドローンポートID
    mappingDroneportSection.setDroneportId(dronePortId);

    // 登録日時
    mappingDroneportSection.setCreatedAt(currentDateTimeUTC);

    // 更新日時
    mappingDroneportSection.setUpdatedAt(currentDateTimeUTC);

    // 登録
    mappingDroneportSectionMapper.insert(mappingDroneportSection);
  }

  /**
   * 現在日時を得る
   * 
   * @return 現在日時
   */
  private LocalDateTime getCurrentDateTimeUTC() {
    return LocalDateTime.now(clock);
  }

}

