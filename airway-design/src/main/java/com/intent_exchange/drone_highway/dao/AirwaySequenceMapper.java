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

package com.intent_exchange.drone_highway.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AirwaySequenceMapper {
  Integer selectDespersionNodeIdSeqNextVal();

  Integer selectAirwayDeterminationIdSeqNextVal();

  Integer selectMappingJunctionSectionIdSeqNextVal();

  Integer selectMappingDroneportSectionIdSeqNextVal();


  /**
   * 次のシーケンス「airway_compatible_models_id」番号を取得する
   * 
   * @return シーケンス番号
   */
  Integer selectAirwayCompatibleModelsIdSeqNextVal();

  /**
   * 次のシーケンス「fall_space_id_seq」番号を取得する
   * 
   * @return シーケンス番号
   */
  Integer selectFallSpaceIdSeqNextVal();

}

