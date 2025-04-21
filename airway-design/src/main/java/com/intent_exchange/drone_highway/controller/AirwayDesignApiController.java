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

package com.intent_exchange.drone_highway.controller;

import java.lang.reflect.Method;
import java.util.List;
import javax.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;
import com.intent_exchange.drone_highway.api.AirwayDesignApi;
import com.intent_exchange.drone_highway.entity.AirwayConditions;
import com.intent_exchange.drone_highway.entity.AirwayDeleteRequestEntity;
import com.intent_exchange.drone_highway.entity.AirwayEntity;
import com.intent_exchange.drone_highway.entity.AirwaysGSWPostRequestEntity;
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestEntity;
import com.intent_exchange.drone_highway.entity.AirwaysPostResponseEntity;
import com.intent_exchange.drone_highway.exception.DataNotFoundException;
import com.intent_exchange.drone_highway.service.AirwayDesignService;
import com.intent_exchange.drone_highway.util.SessionUtil;

@RestController
public class AirwayDesignApiController implements AirwayDesignApi {

  private static final Logger logger = LoggerFactory.getLogger(AirwayDesignApiController.class);

  @Autowired
  private AirwayDesignService airwayDesignService;

  @Autowired
  private HttpServletRequest request;

  /**
   * 航路情報取得
   * 
   * @throws SecurityException
   * @throws NoSuchMethodException
   * @throws MethodArgumentNotValidException
   * @throws NoHandlerFoundException
   */
  @Override
  public ResponseEntity<AirwayEntity> airwayGet(@Valid AirwayConditions airwayConditions,
      @RequestParam(required = false) List<String> flightPurpose,
      @RequestParam(required = false) String determinationDateFrom,
      @RequestParam(required = false) String determinationDateTo,
      @RequestParam(required = false) String businessNumber,
      @RequestParam(required = false) String areaName) throws MethodArgumentNotValidException,
      NoSuchMethodException, SecurityException, NoHandlerFoundException {

    logger.debug("SESSION ID : " + SessionUtil.getSession().getId());
    logger.debug("### AirwayConditions : " + airwayConditions.toString());

    // 追加されたオプション検索項目が指定されていないかのチェック
    boolean isOptionSearchMissing = (flightPurpose == null || flightPurpose.isEmpty())
        && (determinationDateFrom == null || determinationDateFrom.isEmpty())
        && (determinationDateTo == null || determinationDateTo.isEmpty())
        && (businessNumber == null || businessNumber.isEmpty())
        && (areaName == null || areaName.isEmpty());

    // 航路IDも検索条件もない場合のエラー条件チェック
    if ((airwayConditions.getAll() == null || !airwayConditions.getAll())
        && (airwayConditions.getAirwayId() == null || airwayConditions.getAirwayId().isEmpty())
        && isOptionSearchMissing) {
      logger.error(
          "MethodArgumentNotValidException: You must specify at least one search criteria, either all, airwayId, or option search parameters.");
      throw getMethodArgumentNotValidException(airwayConditions,
          "You must specify at least one search criteria, either all, airwayId, or option search parameters.");
    }

    AirwayEntity body = null;
    try {
      body =
          airwayDesignService.airwayGet(airwayConditions.getAirwayId(), airwayConditions.getAll(),
              flightPurpose, determinationDateFrom, determinationDateTo, businessNumber, areaName);
    } catch (DataNotFoundException e) {
      logger.error("DataNotFoundException : " + e.getMessage());
      throw new NoHandlerFoundException(request.getMethod(), request.getRequestURI(),
          new ServletServerHttpRequest(request).getHeaders());
    }

    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(body);
  }

  /**
   * 航路情報登録
   */
  @Override
  public ResponseEntity<AirwaysPostResponseEntity> airwayPost(
      @Valid AirwaysPostRequestEntity airwaysRequest) {
    logger.debug("SESSION ID : " + SessionUtil.getSession().getId());
    AirwaysPostResponseEntity body = airwayDesignService.airwayPost(airwaysRequest);
    return ResponseEntity.status(HttpStatus.CREATED)
        .contentType(MediaType.APPLICATION_JSON)
        .body(body);
  }

  /**
   * 航路情報登録(GSW)
   */
  @Override
  public ResponseEntity<AirwaysPostResponseEntity> airwayPostGSW(
      @Valid AirwaysGSWPostRequestEntity airwaysGSWPostRequestEntity) {
    AirwaysPostResponseEntity body = airwayDesignService.airwayPostGSW(airwaysGSWPostRequestEntity);
    return ResponseEntity.status(HttpStatus.CREATED)
        .contentType(MediaType.APPLICATION_JSON)
        .body(body);
  }

  /**
   * MethodArgumentNotValidExceptionを生成する
   * 
   * @param airwayConditions
   * @param message
   * @return
   * @throws NoSuchMethodException
   * @throws SecurityException
   */
  private MethodArgumentNotValidException getMethodArgumentNotValidException(
      final AirwayConditions airwayConditions, final String message)
      throws NoSuchMethodException, SecurityException {

    Method method = this.getClass()
        .getMethod("airwayGet", AirwayConditions.class, List.class, String.class, String.class,
            String.class, String.class);
    BeanPropertyBindingResult bindingResult =
        new BeanPropertyBindingResult(airwayConditions, "airwayConditions");

    String detailedMessage = message
        + ": either specify 'all=true', a non-empty 'airwayId', or at least one valid optional search parameter.";
    bindingResult.addError(new FieldError("airwayConditions", "airwayId", detailedMessage));

    return new MethodArgumentNotValidException(
        new org.springframework.core.MethodParameter(method, 0), bindingResult);
  }

  @Override
  public ResponseEntity<Void> airwayDelete(
      @Valid AirwayDeleteRequestEntity airwayDeleteRequestEntity) throws NoHandlerFoundException {
    try {
      airwayDesignService.airwayDelete(airwayDeleteRequestEntity);
    } catch (DataNotFoundException e) {
      throw new NoHandlerFoundException(request.getMethod(), request.getRequestURI(),
          new ServletServerHttpRequest(request).getHeaders());
    }
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}

