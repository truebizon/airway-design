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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.intent_exchange.drone_highway.dto.request.DronePortsMappingPostRequestDto;
import com.intent_exchange.drone_highway.entity.DronePortsMappingPostEntity;
import com.intent_exchange.drone_highway.logic.DronePortsMappingLogic;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

/**
 * DronePortsMappingServiceは、ドローンポートマッピングに関連するビジネスロジックを処理します。
 * このサービスクラスは、DronePortsMappingLogicクラスと連携して、ドローンポートマッピングの操作を行います。
 */
@Service
public class DronePortsMappingService {

  /** ドローンポートマッピングのロジック */
  @Autowired
  private DronePortsMappingLogic dronePortsMappingLogic;

  /**
   * 航路区画に紐づく指定したドローンポートIDを追加する
   * 
   * @param dronePortsMappingRequest 追加するドローンポートマッピング情報
   */
  public void dronePortsMappingPost(DronePortsMappingPostEntity dronePortsMappingRequest) {

    DronePortsMappingPostRequestDto requestDto =
        ModelMapperUtil.map(dronePortsMappingRequest, DronePortsMappingPostRequestDto.class);
    dronePortsMappingLogic.dronePortsMappingPost(requestDto);
  }
}

