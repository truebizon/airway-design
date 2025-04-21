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

package com.intent_exchange.drone_highway.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.NoHandlerFoundException;
import com.intent_exchange.drone_highway.dto.request.AirwayDeleteRequestDto;
import com.intent_exchange.drone_highway.entity.AirwayDeleteRequestEntity;
import com.intent_exchange.drone_highway.entity.AirwayEntity;
import com.intent_exchange.drone_highway.entity.AirwaysGSWPostRequestEntity;
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestEntity;
import com.intent_exchange.drone_highway.entity.AirwaysPostResponseEntity;
import com.intent_exchange.drone_highway.exception.DataNotFoundException;
import com.intent_exchange.drone_highway.logic.AirwayDesignLogic;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

/**
 * AirwayDesignServiceは、航路画定に関連するビジネスロジックを処理します。 このサービスクラスは、AirwayDesignLogicクラスと連携して、
 * 航路情報の取得、登録の操作を行います。
 */
@Service
public class AirwayDesignService {

  @Autowired
  private AirwayDesignLogic airwayDesignLogic;

  /**
   * 指定した航路IDの航路情報を取得する。
   *
   * @param corridorId 対象とする航路ID
   * @param all 航路情報をすべて取得するかを指示するフラグ。指定された場合はcorridorIdは無視する
   * @return 航路情報のリスト
   * @throws NoHandlerFoundException 航路情報が見つからない場合
   */
  public AirwayEntity airwayGet(List<String> airwayId, boolean all, List<String> flightPurpose,
      String determinationDateFrom, String determinationDateTo, String businessNumber, String areaName)
      throws NoHandlerFoundException {
    AirwayEntity airway = airwayDesignLogic.airwayGet(airwayId, all, flightPurpose, determinationDateFrom,
        determinationDateTo, businessNumber, areaName);
    return airway;
  }

  /**
   * 指定した航路を追加する
   * 
   * @param airwaysRequest 取得する航路情報
   */
  public AirwaysPostResponseEntity airwayPost(AirwaysPostRequestEntity airwaysRequest) {
    return airwayDesignLogic.airwayPost(airwaysRequest);
  }

  /**
   * 指定した航路(GSW)を追加する
   * 
   * @param airwaysGSWPostRequestEntity
   * @return
   */
  public AirwaysPostResponseEntity airwayPostGSW(
      AirwaysGSWPostRequestEntity airwaysGSWPostRequestEntity) {
    return airwayDesignLogic.airwayPostGSW(airwaysGSWPostRequestEntity);
  }

  /**
   * 航路を削除する
   * 
   * @param airwayDeleteRequestEntity 航路DELETEリクエストエンティティ
   * @throws DataNotFoundException データが存在しない場合
   */
  public void airwayDelete(AirwayDeleteRequestEntity airwayDeleteRequestEntity) {
    AirwayDeleteRequestDto dto =
        ModelMapperUtil.map(airwayDeleteRequestEntity, AirwayDeleteRequestDto.class);
    airwayDesignLogic.delete(dto);
  }
}

