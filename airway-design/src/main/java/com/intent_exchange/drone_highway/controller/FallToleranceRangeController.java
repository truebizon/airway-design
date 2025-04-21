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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;
import com.intent_exchange.drone_highway.api.FallToleranceRangeApi;
import com.intent_exchange.drone_highway.entity.FallToleranceRangeDeleteRequestEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangeDetailGetRequestEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangeDetailGetResponseEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangeGetRequestEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangeGetResponseEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangePostRequestEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangePostResponseEntity;
import com.intent_exchange.drone_highway.exception.DataInUseException;
import com.intent_exchange.drone_highway.exception.DataNotFoundException;
import com.intent_exchange.drone_highway.service.FallToleranceRangeService;

/**
 * 航路確定/最大落下許容範囲(A-1-2-1)コントローラ
 */
@RestController
public class FallToleranceRangeController implements FallToleranceRangeApi {

  /** 航路確定/最大落下許容範囲(A-1-2-1)サービス */
  @Autowired
  private FallToleranceRangeService fallToleranceRangeService;

  /** リクエスト */
  @Autowired
  private HttpServletRequest request;

  @Override
  public ResponseEntity<FallToleranceRangeGetResponseEntity> fallToleranceRangeGet(
      @Valid FallToleranceRangeGetRequestEntity fallToleranceRangeGetRequestEntity) {
    FallToleranceRangeGetResponseEntity response =
        fallToleranceRangeService.fallToleranceRangeGet(fallToleranceRangeGetRequestEntity);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @Override
  public ResponseEntity<FallToleranceRangePostResponseEntity> fallToleranceRangePost(
      @Valid FallToleranceRangePostRequestEntity fallToleranceRangePostRequestEntity) {
    FallToleranceRangePostResponseEntity response =
        fallToleranceRangeService.fallToleranceRangePost(fallToleranceRangePostRequestEntity);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Override
  public ResponseEntity<Void> fallToleranceRangeDelete(
      @Valid FallToleranceRangeDeleteRequestEntity fallToleranceRangeDeleteRequestEntity)
      throws NoHandlerFoundException {
    try {
      fallToleranceRangeService.fallToleranceRangeDelete(fallToleranceRangeDeleteRequestEntity);
    } catch (DataNotFoundException e) {
      throw new NoHandlerFoundException(request.getMethod(), request.getRequestURI(),
          new ServletServerHttpRequest(request).getHeaders());
    } catch (DataInUseException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Override
  public ResponseEntity<FallToleranceRangeDetailGetResponseEntity> fallToleranceRangeDetailGet(
      @Valid FallToleranceRangeDetailGetRequestEntity fallToleranceRangeDetailGetRequestEntity)
      throws NoHandlerFoundException {
    FallToleranceRangeDetailGetResponseEntity response;
    try {
      response = fallToleranceRangeService
          .fallToleranceRangeDetailGet(fallToleranceRangeDetailGetRequestEntity);
    } catch (DataNotFoundException e) {
      throw new NoHandlerFoundException(request.getMethod(), request.getRequestURI(),
          new ServletServerHttpRequest(request).getHeaders());
    }
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

}

