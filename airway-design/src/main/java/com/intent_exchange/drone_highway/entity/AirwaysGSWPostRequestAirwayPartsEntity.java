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

package com.intent_exchange.drone_highway.entity;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestAirwayJunctionEntity;
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestAirwaySectionEntity;
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestDespersionNodeWithFallSpaceEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AirwaysGSWPostRequestAirwayPartsEntity
 */

@JsonTypeName("airwaysGSWPostRequestAirwayPartsEntity")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-01-06T14:28:36.995126+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class AirwaysGSWPostRequestAirwayPartsEntity {

  private Integer prevAirwayPartsIndex;

  private AirwaysPostRequestDespersionNodeWithFallSpaceEntity despersionNode;

  @Valid
  private List<@Valid AirwaysPostRequestAirwayJunctionEntity> airwayJunction = new ArrayList<>();

  private AirwaysPostRequestAirwaySectionEntity airwaySection;

  public AirwaysGSWPostRequestAirwayPartsEntity prevAirwayPartsIndex(Integer prevAirwayPartsIndex) {
    this.prevAirwayPartsIndex = prevAirwayPartsIndex;
    return this;
  }

  /**
   * 接続元になるパーツのインデックス番号  一直線の航路の場合は1つ前に格納されてるパーツのインデックスを格納  分岐を含む航路の場合は、適切な前のパーツとなるインデックスを格納する  前のパーツが存在しない場合は、NULLを設定する 
   * @return prevAirwayPartsIndex
   */
  
  @Schema(name = "prevAirwayPartsIndex", description = "接続元になるパーツのインデックス番号  一直線の航路の場合は1つ前に格納されてるパーツのインデックスを格納  分岐を含む航路の場合は、適切な前のパーツとなるインデックスを格納する  前のパーツが存在しない場合は、NULLを設定する ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("prevAirwayPartsIndex")
  public Integer getPrevAirwayPartsIndex() {
    return prevAirwayPartsIndex;
  }

  public void setPrevAirwayPartsIndex(Integer prevAirwayPartsIndex) {
    this.prevAirwayPartsIndex = prevAirwayPartsIndex;
  }

  public AirwaysGSWPostRequestAirwayPartsEntity despersionNode(AirwaysPostRequestDespersionNodeWithFallSpaceEntity despersionNode) {
    this.despersionNode = despersionNode;
    return this;
  }

  /**
   * Get despersionNode
   * @return despersionNode
   */
  @Valid 
  @Schema(name = "despersionNode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("despersionNode")
  public AirwaysPostRequestDespersionNodeWithFallSpaceEntity getDespersionNode() {
    return despersionNode;
  }

  public void setDespersionNode(AirwaysPostRequestDespersionNodeWithFallSpaceEntity despersionNode) {
    this.despersionNode = despersionNode;
  }

  public AirwaysGSWPostRequestAirwayPartsEntity airwayJunction(List<@Valid AirwaysPostRequestAirwayJunctionEntity> airwayJunction) {
    this.airwayJunction = airwayJunction;
    return this;
  }

  public AirwaysGSWPostRequestAirwayPartsEntity addAirwayJunctionItem(AirwaysPostRequestAirwayJunctionEntity airwayJunctionItem) {
    if (this.airwayJunction == null) {
      this.airwayJunction = new ArrayList<>();
    }
    this.airwayJunction.add(airwayJunctionItem);
    return this;
  }

  /**
   * ジャンクション
   * @return airwayJunction
   */
  @Valid 
  @Schema(name = "airwayJunction", description = "ジャンクション", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("airwayJunction")
  public List<@Valid AirwaysPostRequestAirwayJunctionEntity> getAirwayJunction() {
    return airwayJunction;
  }

  public void setAirwayJunction(List<@Valid AirwaysPostRequestAirwayJunctionEntity> airwayJunction) {
    this.airwayJunction = airwayJunction;
  }

  public AirwaysGSWPostRequestAirwayPartsEntity airwaySection(AirwaysPostRequestAirwaySectionEntity airwaySection) {
    this.airwaySection = airwaySection;
    return this;
  }

  /**
   * Get airwaySection
   * @return airwaySection
   */
  @Valid 
  @Schema(name = "airwaySection", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("airwaySection")
  public AirwaysPostRequestAirwaySectionEntity getAirwaySection() {
    return airwaySection;
  }

  public void setAirwaySection(AirwaysPostRequestAirwaySectionEntity airwaySection) {
    this.airwaySection = airwaySection;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AirwaysGSWPostRequestAirwayPartsEntity airwaysGSWPostRequestAirwayPartsEntity = (AirwaysGSWPostRequestAirwayPartsEntity) o;
    return Objects.equals(this.prevAirwayPartsIndex, airwaysGSWPostRequestAirwayPartsEntity.prevAirwayPartsIndex) &&
        Objects.equals(this.despersionNode, airwaysGSWPostRequestAirwayPartsEntity.despersionNode) &&
        Objects.equals(this.airwayJunction, airwaysGSWPostRequestAirwayPartsEntity.airwayJunction) &&
        Objects.equals(this.airwaySection, airwaysGSWPostRequestAirwayPartsEntity.airwaySection);
  }

  @Override
  public int hashCode() {
    return Objects.hash(prevAirwayPartsIndex, despersionNode, airwayJunction, airwaySection);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AirwaysGSWPostRequestAirwayPartsEntity {\n");
    sb.append("    prevAirwayPartsIndex: ").append(toIndentedString(prevAirwayPartsIndex)).append("\n");
    sb.append("    despersionNode: ").append(toIndentedString(despersionNode)).append("\n");
    sb.append("    airwayJunction: ").append(toIndentedString(airwayJunction)).append("\n");
    sb.append("    airwaySection: ").append(toIndentedString(airwaySection)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}


