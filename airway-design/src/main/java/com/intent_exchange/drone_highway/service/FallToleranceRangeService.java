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
import com.intent_exchange.drone_highway.dto.request.FallToleranceRangeDeleteRequestDto;
import com.intent_exchange.drone_highway.dto.request.FallToleranceRangeDetailGetRequestDto;
import com.intent_exchange.drone_highway.dto.request.FallToleranceRangeGetRequestDto;
import com.intent_exchange.drone_highway.dto.request.FallToleranceRangePostRequestDto;
import com.intent_exchange.drone_highway.dto.response.FallToleranceRangeDetailGetResponseDto;
import com.intent_exchange.drone_highway.dto.response.FallToleranceRangeGetResponseDto;
import com.intent_exchange.drone_highway.dto.response.FallToleranceRangePostResponseDto;
import com.intent_exchange.drone_highway.entity.FallToleranceRangeDeleteRequestEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangeDetailGetRequestEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangeDetailGetResponseEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangeGetRequestEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangeGetResponseEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangePostRequestEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangePostResponseEntity;
import com.intent_exchange.drone_highway.exception.DataInUseException;
import com.intent_exchange.drone_highway.exception.DataNotFoundException;
import com.intent_exchange.drone_highway.logic.FallToleranceRangeLogic;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

/**
 * 航路確定/最大落下許容範囲(A-1-2-1)サービス
 */
@Service
public class FallToleranceRangeService {

  /** 最大落下許容範囲情報に関するロジック */
  @Autowired
  private FallToleranceRangeLogic fallToleranceRangeLogic;

  /**
   * 引数で指定された事業者番号に関連する最大落下許容範囲を取得する
   * 
   * @param fallToleranceRangeGetRequestEntity 最大落下許容範囲GETリクエストエンティティ
   * @return レスポンスエンティティ
   */
  public FallToleranceRangeGetResponseEntity fallToleranceRangeGet(
      FallToleranceRangeGetRequestEntity fallToleranceRangeGetRequestEntity) {
    FallToleranceRangeGetRequestDto rangeDto = ModelMapperUtil
        .map(fallToleranceRangeGetRequestEntity, FallToleranceRangeGetRequestDto.class);
    FallToleranceRangeGetResponseDto responseDto = fallToleranceRangeLogic.select(rangeDto);
    return ModelMapperUtil.map(responseDto, FallToleranceRangeGetResponseEntity.class);
  }

  /**
   * 最大落下許容範囲を登録する
   * 
   * @param fallToleranceRangePostRequestEntity 最大落下許容範囲POSTリクエストエンティティ
   * @return レスポンスエンティティ
   */
  public FallToleranceRangePostResponseEntity fallToleranceRangePost(
      FallToleranceRangePostRequestEntity fallToleranceRangePostRequestEntity) {
    FallToleranceRangePostRequestDto notificationDto = ModelMapperUtil
        .map(fallToleranceRangePostRequestEntity, FallToleranceRangePostRequestDto.class);
    FallToleranceRangePostResponseDto responseDto = fallToleranceRangeLogic.insert(notificationDto);
    return ModelMapperUtil.map(responseDto, FallToleranceRangePostResponseEntity.class);
  }

  /**
   * 最大落下許容範囲を削除する
   * 
   * @param fallToleranceRangeDeleteRequestEntity 最大落下許容範囲DELETEリクエストエンティティ
   * @throws DataNotFoundException データが存在しない場合
   * @throws DataInUseException データが使用中の場合
   */
  public void fallToleranceRangeDelete(
      FallToleranceRangeDeleteRequestEntity fallToleranceRangeDeleteRequestEntity) {
    FallToleranceRangeDeleteRequestDto dto = ModelMapperUtil
        .map(fallToleranceRangeDeleteRequestEntity, FallToleranceRangeDeleteRequestDto.class);
    fallToleranceRangeLogic.delete(dto);
  }

  /**
   * 引数で指定された最大落下許容範囲の詳細情報を取得する
   * 
   * @param fallToleranceRangeDetailGet 最大落下許容範囲詳細GETリクエストエンティティ
   * @return レスポンスエンティティ
   * @throws DataNotFoundException データが存在しない場合
   */
  public FallToleranceRangeDetailGetResponseEntity fallToleranceRangeDetailGet(
      FallToleranceRangeDetailGetRequestEntity fallToleranceRangeDetailGetRequestEntity) {
    FallToleranceRangeDetailGetRequestDto dto = ModelMapperUtil
        .map(fallToleranceRangeDetailGetRequestEntity, FallToleranceRangeDetailGetRequestDto.class);
    FallToleranceRangeDetailGetResponseDto responseDto = fallToleranceRangeLogic.selectDetail(dto);
    return ModelMapperUtil.map(responseDto, FallToleranceRangeDetailGetResponseEntity.class);
  }

}

