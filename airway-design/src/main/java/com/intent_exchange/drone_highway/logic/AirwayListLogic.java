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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.intent_exchange.drone_highway.dao.AirwaySectionGeometryMapper;
import com.intent_exchange.drone_highway.dto.request.AirwayListRequestDto;
import com.intent_exchange.drone_highway.dto.response.AirwayListResponseDto;

@Component
public class AirwayListLogic {

  /** 航路区画ジオメトリービューマッパー */
  @Autowired
  private AirwaySectionGeometryMapper airwaySectionGeometryMapper;

  private static final Logger logger = LoggerFactory.getLogger(AirwayListLogic.class);

  /**
   * 引数で指定された座標４点の範囲内の航路を取得する
   * 
   * @param airwayListRequestDto 航路画定/航路情報一覧(A-1-2-6-4)取得DTO
   */
  @Transactional(propagation = Propagation.SUPPORTS)
  public AirwayListResponseDto selectAirways(AirwayListRequestDto airwayListRequestDto) {

    logger.debug("## Point1          : " + airwayListRequestDto.getPoint1());
    logger.debug("## Point2          : " + airwayListRequestDto.getPoint2());
    logger.debug("## Point3          : " + airwayListRequestDto.getPoint3());
    logger.debug("## Point4          : " + airwayListRequestDto.getPoint4());

    AirwayListResponseDto result = new AirwayListResponseDto();

    // 引数からSQLで使用できる文字列に変更する
    String coordinates = String.format("%s %s, %s %s, %s %s, %s %s, %s %s",
        airwayListRequestDto.getPoint1().split(",")[0].trim(),
        airwayListRequestDto.getPoint1().split(",")[1].trim(),
        airwayListRequestDto.getPoint2().split(",")[0].trim(),
        airwayListRequestDto.getPoint2().split(",")[1].trim(),
        airwayListRequestDto.getPoint3().split(",")[0].trim(),
        airwayListRequestDto.getPoint3().split(",")[1].trim(),
        airwayListRequestDto.getPoint4().split(",")[0].trim(),
        airwayListRequestDto.getPoint4().split(",")[1].trim(),
        airwayListRequestDto.getPoint1().split(",")[0].trim(),
        airwayListRequestDto.getPoint1().split(",")[1].trim());


    result.setAirwayIdList(airwaySectionGeometryMapper.selectAirwayIdsByPolygon(coordinates));
    // リストの長さが０なら空の配列を返す。

    return result;

  }

}

