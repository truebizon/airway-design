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

package com.intent_exchange.drone_highway.logic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.intent_exchange.drone_highway.dto.request.RailwayRelativePositionRequestDto;
import com.intent_exchange.drone_highway.dto.response.RailwayRelativePositionResponseDto;
import com.intent_exchange.drone_highway.util.PropertyUtil;

/**
 * 空域デジタルツイン 鉄道運行情報 (A-1-1-1-5)への通信用ロジック
 */
@Component
public class WebRailwayOperationLogic {

  /** 鉄道運行位置特定情報取得URL。 */
  private static final String POST_RAILWAY_SERVICE_RELATIVE_POSITION_URL =
      PropertyUtil.getProperty("post.railway.relative.position.url");

  /** RestTemplateインスタンス。 このインスタンスを使用してHTTPリクエストを送信します。 */
  @Autowired
  @Qualifier("customRestTemplate")
  private RestTemplate restTemplate;

  /**
   * 指定されたエリア情報に基づいて路線と航路との交点情報を取得します。
   * 
   * @param requestDto 路線と航路との交点情報を取得するためのリクエストDTO
   * @return 路線と航路の交点に近い二つの駅と相対値
   */
  public RailwayRelativePositionResponseDto getRailwayRelativePosition(
      RailwayRelativePositionRequestDto requestDto) {
    return restTemplate.postForObject(POST_RAILWAY_SERVICE_RELATIVE_POSITION_URL, requestDto,
        RailwayRelativePositionResponseDto.class);

  }

}

