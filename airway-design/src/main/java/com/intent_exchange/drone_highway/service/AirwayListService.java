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
import com.intent_exchange.drone_highway.dto.request.AirwayListRequestDto;
import com.intent_exchange.drone_highway.dto.response.AirwayListResponseDto;
import com.intent_exchange.drone_highway.entity.AirwayEntity;
import com.intent_exchange.drone_highway.entity.AirwayListRequestEntity;
import com.intent_exchange.drone_highway.logic.AirwayDesignLogic;
import com.intent_exchange.drone_highway.logic.AirwayListLogic;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

/**
 * 航路画定/航路情報一覧取得（ポート管理画面からのリクエスト）のサービスクラス
 */
@Service
public class AirwayListService {

  @Autowired
  private AirwayListLogic airwayListLogic;

  @Autowired
  private AirwayDesignLogic airwayDesignLogic;

  /**
   * 引数で指定された座標４点の範囲内の航路を取得する
   *
   * @param airwayListEntity 地図上の座標４点を指定したクラス
   * @return 航路情報のリスト
   */
  public AirwayEntity airwayListGet(AirwayListRequestEntity airwayListRequestEntity) {


    AirwayListRequestDto requestDto =
        ModelMapperUtil.map(airwayListRequestEntity, AirwayListRequestDto.class);


    // 対象の航路IDのリストを取得する。
    AirwayListResponseDto airwayGetList = airwayListLogic.selectAirways(requestDto);

    List<String> airwayIdList = airwayGetList.getAirwayIdList();

    // 航路情報取得のロジックを呼び出す
    AirwayEntity airway = airwayDesignLogic.airwayGet(airwayIdList, false);

    return airway;
  }

}

