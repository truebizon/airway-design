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
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intent_exchange.drone_highway.dao.AirwayDeterminationMapper;
import com.intent_exchange.drone_highway.dao.AirwayMapper;
import com.intent_exchange.drone_highway.dao.FallToleranceRangeMapper;
import com.intent_exchange.drone_highway.dto.request.FallToleranceRangeDeleteRequestDto;
import com.intent_exchange.drone_highway.dto.request.FallToleranceRangeDetailGetRequestDto;
import com.intent_exchange.drone_highway.dto.request.FallToleranceRangeGetRequestDto;
import com.intent_exchange.drone_highway.dto.request.FallToleranceRangePostRequestDto;
import com.intent_exchange.drone_highway.dto.response.FallToleranceRangeDetailGetResponseDto;
import com.intent_exchange.drone_highway.dto.response.FallToleranceRangeDetailGetResponseGeometryDto;
import com.intent_exchange.drone_highway.dto.response.FallToleranceRangeGetResponseDto;
import com.intent_exchange.drone_highway.dto.response.FallToleranceRangeGetResponseItemDto;
import com.intent_exchange.drone_highway.dto.response.FallToleranceRangeGetResponseItemGeometryDto;
import com.intent_exchange.drone_highway.dto.response.FallToleranceRangePostResponseDto;
import com.intent_exchange.drone_highway.exception.DataInUseException;
import com.intent_exchange.drone_highway.exception.DataNotFoundException;
import com.intent_exchange.drone_highway.exception.DroneHighwayException;
import com.intent_exchange.drone_highway.model.AirwayDetermination;
import com.intent_exchange.drone_highway.model.FallToleranceRange;
import com.intent_exchange.drone_highway.model.FallToleranceRangeAirwayIdUse;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

/**
 * 最大落下許容範囲情報に関するロジック
 */
@Component
public class FallToleranceRangeLogic {

  /** 落下許容範囲マッパー */
  @Autowired
  private FallToleranceRangeMapper fallToleranceRangeMapper;

  /** 航路画定マッパー */
  @Autowired
  private AirwayDeterminationMapper airwayDeterminationMapper;

  /** 航路マッパー */
  @Autowired
  private AirwayMapper airwayMapper;

  /** クロック */
  @Autowired
  private Clock clock;

  /**
   * 引数で指定された情報に関連する最大落下許容範囲情報を取得する
   * 
   * @param fallToleranceRangeGetRequestDto 航路確定/最大落下許容範囲(A-1-2-1)取得DTO
   * @return 最大落下許容範囲情報
   */
  @Transactional(propagation = Propagation.SUPPORTS)
  public FallToleranceRangeGetResponseDto select(
      FallToleranceRangeGetRequestDto fallToleranceRangeGetRequestDto) {
    FallToleranceRangeGetResponseDto result = new FallToleranceRangeGetResponseDto();
    result.setFallToleranceRanges(new ArrayList<FallToleranceRangeGetResponseItemDto>());
    FallToleranceRange queryParam =
        ModelMapperUtil.map(fallToleranceRangeGetRequestDto, FallToleranceRange.class);
    queryParam.setDelete(false);
    List<FallToleranceRangeAirwayIdUse> list =
        fallToleranceRangeMapper.selectByConditions(queryParam);
    for (FallToleranceRangeAirwayIdUse item : list) {
      FallToleranceRangeGetResponseItemDto resultDto =
          ModelMapperUtil.map(item, FallToleranceRangeGetResponseItemDto.class);
      resultDto.setCreatedAt(convertToDate(item.getCreatedAt()));
      resultDto.setUpdatedAt(convertToDate(item.getUpdatedAt()));
      try {
        resultDto.setGeometry(new ObjectMapper().readValue(item.getJson(),
            FallToleranceRangeGetResponseItemGeometryDto.class));
      } catch (JsonProcessingException e) {
        throw new DroneHighwayException(e.getMessage());
      }
      result.getFallToleranceRanges().add(resultDto);
    }
    return result;

  }

  /**
   * 引数で指定された情報を登録する
   * 
   * @param fallToleranceRangePostRequestDto 航路確定/最大落下許容範囲(A-1-2-1)登録DTO
   * @return 航路確定/最大落下許容範囲(A-1-2-1)登録レスポンスDTO
   * @throws DroneHighwayException ビジネスロジックの実行に失敗した場合
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public FallToleranceRangePostResponseDto insert(
      FallToleranceRangePostRequestDto fallToleranceRangePostRequestDto) {
    FallToleranceRange model =
        ModelMapperUtil.map(fallToleranceRangePostRequestDto, FallToleranceRange.class);
    model.setCreatedAt(LocalDateTime.now(clock));
    model.setUpdatedAt(model.getCreatedAt());
    String json = "";
    try {
      json = new ObjectMapper().writeValueAsString(fallToleranceRangePostRequestDto.getGeometry());
    } catch (JsonProcessingException e) {
      throw new DroneHighwayException(e.getMessage());
    }
    model.setFallToleranceRangeId(UUID.randomUUID().toString());
    model.setJson(json);
    model.setDelete(false);
    fallToleranceRangeMapper.insert(model);
    FallToleranceRangePostResponseDto result = new FallToleranceRangePostResponseDto();
    result.setFallToleranceRangeId(model.getFallToleranceRangeId());
    result.setName(model.getName());
    result.setCreatedAt(convertToDate(model.getCreatedAt()));
    return result;
  }

  /**
   * 引数で指定された情報を削除する
   * 
   * @param fallToleranceRangePostRequestDto 航路確定/最大落下許容範囲(A-1-2-1)登録DTO
   * @return 航路確定/最大落下許容範囲(A-1-2-1)登録レスポンスDTO
   * @throws DataNotFoundException データが存在しない場合
   * @throws DataInUseException データが使用中の場合
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public void delete(FallToleranceRangeDeleteRequestDto fallToleranceRangeDeleteRequestDto) {
    FallToleranceRange fallToleranceRange = fallToleranceRangeMapper
        .selectLockByPrimaryKey(fallToleranceRangeDeleteRequestDto.getFallToleranceRangeId());
    check(fallToleranceRange, fallToleranceRangeDeleteRequestDto.getBusinessNumber());
    List<AirwayDetermination> airwayDetermination =
        airwayDeterminationMapper.selectLockByFallToleranceRangeId(
            fallToleranceRangeDeleteRequestDto.getFallToleranceRangeId());
    if (airwayDetermination.size() > 0) {
      List<Integer> airwayDeterminationId = airwayDetermination.stream()
          .map(AirwayDetermination::getAirwayDeterminationId)
          .collect(Collectors.toList());
      if (airwayMapper.selectCountByAirwayDeterminationId(airwayDeterminationId) > 0) {
        throw new DataInUseException();
      }
    }
    LocalDateTime now = LocalDateTime.now(clock);
    for (AirwayDetermination item : airwayDetermination) {
      item.setDelete(true);
      item.setUpdatedAt(now);
      airwayDeterminationMapper.updateByPrimaryKey(item);
    }
    fallToleranceRange.setDelete(true);
    fallToleranceRange.setUpdatedAt(now);
    fallToleranceRangeMapper.updateByPrimaryKey(fallToleranceRange);
  }

  /**
   * 引数で指定された情報に関連する最大落下許容範囲の詳細情報を取得する
   * 
   * @param fallToleranceRangeDetailGetRequestDto 航路確定/最大落下許容範囲(A-1-2-1)詳細取得DTO
   * @return 最大落下許容範囲詳細情報
   * @throws DataNotFoundException データが存在しない場合
   */
  @Transactional(propagation = Propagation.SUPPORTS)
  public FallToleranceRangeDetailGetResponseDto selectDetail(
      FallToleranceRangeDetailGetRequestDto fallToleranceRangeDetailGetRequestDto) {
    FallToleranceRange model = fallToleranceRangeMapper
        .selectByPrimaryKey(fallToleranceRangeDetailGetRequestDto.getFallToleranceRangeId());
    check(model, fallToleranceRangeDetailGetRequestDto.getBusinessNumber());
    FallToleranceRangeDetailGetResponseDto result =
        ModelMapperUtil.map(model, FallToleranceRangeDetailGetResponseDto.class);
    result.setCreatedAt(convertToDate(model.getCreatedAt()));
    result.setUpdatedAt(convertToDate(model.getUpdatedAt()));
    try {
      result.setGeometry(new ObjectMapper().readValue(model.getJson(),
          FallToleranceRangeDetailGetResponseGeometryDto.class));
    } catch (JsonProcessingException e) {
      throw new DroneHighwayException(e.getMessage());
    }
    return result;
  }

  /**
   * 引数の情報をチェックする
   * 
   * @param fallToleranceRange 最大落下許容範囲
   * @param businessNumber 事業者番号
   * @throws DataNotFoundException データが存在しない場合
   */
  private void check(FallToleranceRange fallToleranceRange, String businessNumber) {
    if (Objects.isNull(fallToleranceRange)) {
      throw new DataNotFoundException();
    }
    if (!fallToleranceRange.getBusinessNumber().equals(businessNumber)) {
      throw new DataNotFoundException();
    }
    if (fallToleranceRange.getDelete()) {
      throw new DataNotFoundException();
    }
  }

  /**
   * LocalDateTime型をDate型に変換する
   * 
   * @param localDateTime 変換元
   * @return 変換したDate型
   */
  private Date convertToDate(LocalDateTime scr) {
    return Date.from(ZonedDateTime.of(scr, clock.getZone()).toInstant());
  }

}

