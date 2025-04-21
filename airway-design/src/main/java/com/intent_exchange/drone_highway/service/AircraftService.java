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
import com.intent_exchange.drone_highway.dto.response.AircraftDto;
import com.intent_exchange.drone_highway.entity.AircraftEntity;
import com.intent_exchange.drone_highway.entity.AircraftEntityAircraftInner;
import com.intent_exchange.drone_highway.logic.AircraftLogic;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

/**
 * AircraftServiceは、機体情報候補リストに関連するビジネスロジックを処理します。
 * このサービスクラスは、AircraftLogicクラスと連携して、機体情報候補リストの取得の操作を行います。
 */
@Service
public class AircraftService {


  /** 機体情報候補リスト取得のロジック */
  @Autowired
  private AircraftLogic aircraftLogic;

  /**
   * 機体情報候補リストを取得します。
   *
   * @return {@code AircraftEntity} 機体情報候補リスト
   */
  public AircraftEntity getAircraft() {
    List<AircraftDto> dtoList = aircraftLogic.getAircraft();

    List<AircraftEntityAircraftInner> responseInners =
        ModelMapperUtil.mapList(dtoList, AircraftEntityAircraftInner.class);

    AircraftEntity entity = new AircraftEntity();
    entity.setAircraft(responseInners);
    return entity;
  }

}

