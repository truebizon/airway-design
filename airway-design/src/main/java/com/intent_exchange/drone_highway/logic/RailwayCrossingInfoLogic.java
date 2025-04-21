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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.intent_exchange.drone_highway.dao.AirwayDeterminationMapper;
import com.intent_exchange.drone_highway.dao.FallToleranceRangeMapper;
import com.intent_exchange.drone_highway.dao.RailwayCrossingInfoMapper;
import com.intent_exchange.drone_highway.dto.request.RailwayRelativePositionRequestDto;
import com.intent_exchange.drone_highway.dto.response.RailwayRelativePositionResponseDto;
import com.intent_exchange.drone_highway.exception.DataNotFoundException;
import com.intent_exchange.drone_highway.logic.web.WebRailwayOperationLogic;
import com.intent_exchange.drone_highway.model.AirwayDetermination;
import com.intent_exchange.drone_highway.model.FallToleranceRange;
import com.intent_exchange.drone_highway.model.RailwayCrossingInfo;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

@Component
public class RailwayCrossingInfoLogic {

  @Autowired
  private AirwayDeterminationMapper airwayDeterminationMapper;
  @Autowired
  private FallToleranceRangeMapper fallToleranceRangeMapper;
  @Autowired
  private RailwayCrossingInfoMapper railwayCrossingInfoMapper;

  /**
   * 路線と航路の交点に近い二つの駅と相対値への通信用ロジック
   */
  @Autowired
  private WebRailwayOperationLogic webRailwayOperationLogic;

  /** ロガー */
  private static final Logger logger = LoggerFactory.getLogger(RailwayCrossingInfoLogic.class);

  /**
   * 路線と航路の交点に近い二つの駅と相対値を取得し、DBに登録
   * 
   * @param airwayDeterminationId 航路画定ID
   * @param airwayId 航路ID
   * @param currentDateTimeUTC 現在時刻
   */
  public void insertRelativePosition(int airwayDeterminationId, String airwayId,
      LocalDateTime currentDateTimeUTC) {

    // 最大落下許容範囲ID取得
    String fallToleranceRangeId = getFallToleranceRangeId(airwayDeterminationId);

    // 最大落下許容範囲取得
    RailwayRelativePositionRequestDto requestDto = new RailwayRelativePositionRequestDto();
    requestDto.setFallToleranceRange(getFallToleranceRange(fallToleranceRangeId));

    // 路線と航路の交点に近い二つの駅と相対値取得
    RailwayRelativePositionResponseDto railwayRelativePositionResponseDto =
        webRailwayOperationLogic.getRailwayRelativePosition(requestDto);

    // 路線と航路の交点に近い二つの駅と相対値を取得できた場合 DBに登録
    // MVP3では、航路IDと交点情報は1:1の関係とする
    if (null != railwayRelativePositionResponseDto) {
      RailwayCrossingInfo railwayCrossingInfo = new RailwayCrossingInfo();
      railwayCrossingInfo.setAirwayId(airwayId);
      railwayCrossingInfo.setStation1(railwayRelativePositionResponseDto.getStation1());
      railwayCrossingInfo.setStation2(railwayRelativePositionResponseDto.getStation2());
      railwayCrossingInfo
          .setRelativeValue(railwayRelativePositionResponseDto.getRelativePosition());
      railwayCrossingInfo.setCreatedAt(currentDateTimeUTC);
      railwayCrossingInfo.setUpdatedAt(currentDateTimeUTC);
      railwayCrossingInfoMapper.insert(railwayCrossingInfo);
    }
  }

  /**
   * 航路画定IDから最大落下許容範囲IDを取得する
   * 
   * @param airwayDeterminationId 航路画定ID
   * @return fallToleranceRangeId 最大落下許容範囲ID
   */
  private String getFallToleranceRangeId(final int airwayDeterminationId) {
    final AirwayDetermination airwayDetermination =
        airwayDeterminationMapper.selectByPrimaryKey(airwayDeterminationId);
    if (null == airwayDetermination) {
      throw new DataNotFoundException();
    }
    return airwayDetermination.getFallToleranceRangeId();
  }

  /**
   * 最大落下許容範囲IDから最大落下許容範囲を取得する
   * 
   * @param airwayDeterminationId 最大落下許容範囲ID
   * @return fallToleranceRange 最大落下許容範囲
   */
  private List<List<Double>> getFallToleranceRange(final String fallToleranceRangeId) {
    final FallToleranceRange fallToleranceRange =
        fallToleranceRangeMapper.selectByPrimaryKey(fallToleranceRangeId);
    if (null == fallToleranceRange) {
      throw new DataNotFoundException();
    }
    List<List<Double>> coordinates = new ArrayList<>();
    try {
      coordinates = ModelMapperUtil.stringToDoubleList(fallToleranceRange.getJson());
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }

    return coordinates;
  }

}

