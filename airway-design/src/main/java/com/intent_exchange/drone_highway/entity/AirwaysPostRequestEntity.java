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
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestAirwayPartsEntity;
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
 * AirwaysPostRequestEntity
 */

@JsonTypeName("airwaysPostRequestEntity")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-12-06T10:07:14.272581300+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class AirwaysPostRequestEntity {

  private Integer airwayDeterminationId;

  private String airwayName;

  private String flightPurpose;

  @Valid
  private List<@Valid AirwaysPostRequestAirwayPartsEntity> airwayParts = new ArrayList<>();

  public AirwaysPostRequestEntity() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AirwaysPostRequestEntity(Integer airwayDeterminationId) {
    this.airwayDeterminationId = airwayDeterminationId;
  }

  public AirwaysPostRequestEntity airwayDeterminationId(Integer airwayDeterminationId) {
    this.airwayDeterminationId = airwayDeterminationId;
    return this;
  }

  /**
   * 航路画定ID 作成中の航路を一意に表すID
   * @return airwayDeterminationId
   */
  @NotNull
  @Schema(name = "airwayDeterminationId", description = "航路画定ID 作成中の航路を一意に表すID", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("airwayDeterminationId")
  public Integer getAirwayDeterminationId() {
    return airwayDeterminationId;
  }

  public void setAirwayDeterminationId(Integer airwayDeterminationId) {
    this.airwayDeterminationId = airwayDeterminationId;
  }

  public AirwaysPostRequestEntity airwayName(String airwayName) {
    this.airwayName = airwayName;
    return this;
  }

  /**
   * 航路名
   * @return airwayName
   */
  @Size(min = 0, max = 200) 
  @Schema(name = "airwayName", description = "航路名", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("airwayName")
  public String getAirwayName() {
    return airwayName;
  }

  public void setAirwayName(String airwayName) {
    this.airwayName = airwayName;
  }

  public AirwaysPostRequestEntity flightPurpose(String flightPurpose) {
    this.flightPurpose = flightPurpose;
    return this;
  }

  /**
   * 飛行目的
   * @return flightPurpose
   */
  @Size(min = 0, max = 200) 
  @Schema(name = "flightPurpose", description = "飛行目的", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("flightPurpose")
  public String getFlightPurpose() {
    return flightPurpose;
  }

  public void setFlightPurpose(String flightPurpose) {
    this.flightPurpose = flightPurpose;
  }

  public AirwaysPostRequestEntity airwayParts(List<@Valid AirwaysPostRequestAirwayPartsEntity> airwayParts) {
    this.airwayParts = airwayParts;
    return this;
  }

  public AirwaysPostRequestEntity addAirwayPartsItem(AirwaysPostRequestAirwayPartsEntity airwayPartsItem) {
    if (this.airwayParts == null) {
      this.airwayParts = new ArrayList<>();
    }
    this.airwayParts.add(airwayPartsItem);
    return this;
  }

  /**
   * 航路を構成する、節と航路区画とジャンクションのセットのリスト
   * @return airwayParts
   */
  @Valid 
  @Schema(name = "airwayParts", description = "航路を構成する、節と航路区画とジャンクションのセットのリスト", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("airwayParts")
  public List<@Valid AirwaysPostRequestAirwayPartsEntity> getAirwayParts() {
    return airwayParts;
  }

  public void setAirwayParts(List<@Valid AirwaysPostRequestAirwayPartsEntity> airwayParts) {
    this.airwayParts = airwayParts;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AirwaysPostRequestEntity airwaysPostRequestEntity = (AirwaysPostRequestEntity) o;
    return Objects.equals(this.airwayDeterminationId, airwaysPostRequestEntity.airwayDeterminationId) &&
        Objects.equals(this.airwayName, airwaysPostRequestEntity.airwayName) &&
        Objects.equals(this.flightPurpose, airwaysPostRequestEntity.flightPurpose) &&
        Objects.equals(this.airwayParts, airwaysPostRequestEntity.airwayParts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(airwayDeterminationId, airwayName, flightPurpose, airwayParts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AirwaysPostRequestEntity {\n");
    sb.append("    airwayDeterminationId: ").append(toIndentedString(airwayDeterminationId)).append("\n");
    sb.append("    airwayName: ").append(toIndentedString(airwayName)).append("\n");
    sb.append("    flightPurpose: ").append(toIndentedString(flightPurpose)).append("\n");
    sb.append("    airwayParts: ").append(toIndentedString(airwayParts)).append("\n");
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


