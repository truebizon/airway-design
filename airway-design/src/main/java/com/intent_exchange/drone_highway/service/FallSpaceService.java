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
import com.intent_exchange.drone_highway.dto.request.FallSpaceCrossSectionPostRequestDto;
import com.intent_exchange.drone_highway.dto.request.FallSpacePostRequestDto;
import com.intent_exchange.drone_highway.dto.response.FallSpaceCrossSectionPostResponseDto;
import com.intent_exchange.drone_highway.dto.response.FallSpacePostResponseDto;
import com.intent_exchange.drone_highway.entity.FallSpaceCrossSectionPostRequestEntity;
import com.intent_exchange.drone_highway.entity.FallSpaceCrossSectionPostResponseEntity;
import com.intent_exchange.drone_highway.entity.FallSpacePostRequestEntity;
import com.intent_exchange.drone_highway.entity.FallSpacePostResponseEntity;
import com.intent_exchange.drone_highway.exception.DataNotFoundException;
import com.intent_exchange.drone_highway.logic.FallSpaceLogic;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

/**
 * 航路確定/落下空間(A-1-2-2-2)サービス
 */
@Service
public class FallSpaceService {

  /** 落下空間に関するロジック */
  @Autowired
  private FallSpaceLogic fallSpaceLogic;

  /**
   * 落下空間に関する基本情報を登録する
   * 
   * @param fallSpacePostRequestEntity 落下空間POSTリクエストエンティティ
   * @return レスポンスエンティティ
   * @throws DataNotFoundException データが存在しない場合
   */
  public FallSpacePostResponseEntity fallSpacePost(
      FallSpacePostRequestEntity fallSpacePostRequestEntity) {
    FallSpacePostRequestDto requestDto =
        ModelMapperUtil.map(fallSpacePostRequestEntity, FallSpacePostRequestDto.class);
    FallSpacePostResponseDto responseDto = fallSpaceLogic.basicInfoRegistration(requestDto);
    return ModelMapperUtil.map(responseDto, FallSpacePostResponseEntity.class);
  }

  /**
   * 落下空間(断面)を取得する
   * 
   * @param fallSpaceCrossSectionPostRequestEntity 落下空間(断面)POSTリクエストエンティティ
   * @return レスポンスエンティティ
   * @throws DataNotFoundException データが存在しない場合
   */
  public FallSpaceCrossSectionPostResponseEntity fallSpaceCrossSectionPost(
      FallSpaceCrossSectionPostRequestEntity fallSpaceCrossSectionPostRequestEntity) {
    FallSpaceCrossSectionPostRequestDto requestDto = ModelMapperUtil
        .map(fallSpaceCrossSectionPostRequestEntity, FallSpaceCrossSectionPostRequestDto.class);
    FallSpaceCrossSectionPostResponseDto responseDto =
        fallSpaceLogic.getFallSpaceCrossSection(requestDto);
    return ModelMapperUtil.map(responseDto, FallSpaceCrossSectionPostResponseEntity.class);
  }

}

