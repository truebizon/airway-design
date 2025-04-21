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
import com.intent_exchange.drone_highway.api.FallSpaceApi;
import com.intent_exchange.drone_highway.entity.FallSpaceCrossSectionPostRequestEntity;
import com.intent_exchange.drone_highway.entity.FallSpaceCrossSectionPostResponseEntity;
import com.intent_exchange.drone_highway.entity.FallSpacePostRequestEntity;
import com.intent_exchange.drone_highway.entity.FallSpacePostResponseEntity;
import com.intent_exchange.drone_highway.exception.DataNotFoundException;
import com.intent_exchange.drone_highway.service.FallSpaceService;

/**
 * 航路確定/落下空間コントローラ<br>
 * A-1-2-2-2<br>
 * A-1-2-2-3
 */
@RestController
public class FallSpaceController implements FallSpaceApi {

  /** 航路確定/最大落下許容範囲(A-1-2-1)サービス */
  @Autowired
  private FallSpaceService fallSpaceService;

  /** リクエスト */
  @Autowired
  private HttpServletRequest request;

  @Override
  public ResponseEntity<FallSpacePostResponseEntity> fallSpacePost(
      @Valid FallSpacePostRequestEntity fallSpacePostRequestEntity) throws NoHandlerFoundException {
    try {
      FallSpacePostResponseEntity response =
          fallSpaceService.fallSpacePost(fallSpacePostRequestEntity);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (DataNotFoundException e) {
      throw new NoHandlerFoundException(request.getMethod(), request.getRequestURI(),
          new ServletServerHttpRequest(request).getHeaders());
    }

  }

  @Override
  public ResponseEntity<FallSpaceCrossSectionPostResponseEntity> fallSpaceCrossSectionPost(
      @Valid FallSpaceCrossSectionPostRequestEntity fallSpaceCrossSectionPostRequestEntity)
      throws NoHandlerFoundException {
    try {
      FallSpaceCrossSectionPostResponseEntity response =
          fallSpaceService.fallSpaceCrossSectionPost(fallSpaceCrossSectionPostRequestEntity);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (DataNotFoundException e) {
      throw new NoHandlerFoundException(request.getMethod(), request.getRequestURI(),
          new ServletServerHttpRequest(request).getHeaders());
    }
  }

}

