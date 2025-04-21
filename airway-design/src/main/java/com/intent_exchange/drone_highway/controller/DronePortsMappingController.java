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

import javax.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;
import com.intent_exchange.drone_highway.api.DronePortsMappingApi;
import com.intent_exchange.drone_highway.entity.DronePortsMappingPostEntity;
import com.intent_exchange.drone_highway.exception.DataNotFoundException;
import com.intent_exchange.drone_highway.service.DronePortsMappingService;

/**
 * ドローンポートマッピングに関連するリクエストを処理するコントローラークラスです。
 */
@RestController
public class DronePortsMappingController implements DronePortsMappingApi {

  private static final Logger logger = LoggerFactory.getLogger(AirwayDesignApiController.class);

  /** ドローンポートマッピングのサービス */
  @Autowired
  private DronePortsMappingService dronePortsMappingService;

  @Autowired
  private HttpServletRequest request;

  /**
   * ドローンポートマッピング情報登録
   * 
   * @throws NoHandlerFoundException
   */
  @Override
  public ResponseEntity<Void> dronePortsMappingPost(
      @Valid DronePortsMappingPostEntity dronePortsMappingRequest) throws NoHandlerFoundException {
    try {
      dronePortsMappingService.dronePortsMappingPost(dronePortsMappingRequest);
    } catch (DataNotFoundException e) {
      logger.error("DataNotFoundException : " + e.getMessage());
      throw new NoHandlerFoundException(request.getMethod(), request.getRequestURI(),
          new ServletServerHttpRequest(request).getHeaders());
    }

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

}

