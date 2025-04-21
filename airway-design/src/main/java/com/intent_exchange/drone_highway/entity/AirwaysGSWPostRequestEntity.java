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
import com.intent_exchange.drone_highway.entity.AirwaysGSWPostRequestAirwayPartsEntity;
import com.intent_exchange.drone_highway.entity.FallSpacePostRequestDroneEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangePostRequestEntity;
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
 * AirwaysGSWPostRequestEntity
 */

@JsonTypeName("airwaysGSWPostRequestEntity")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-01-06T14:28:36.995126+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class AirwaysGSWPostRequestEntity {

  private FallToleranceRangePostRequestEntity fallToleranceRange;

  @Valid
  private List<@Valid FallSpacePostRequestDroneEntity> droneList = new ArrayList<>();

  private String airwayName;

  private String flightPurpose;

  @Valid
  private List<@Valid AirwaysGSWPostRequestAirwayPartsEntity> airwayParts = new ArrayList<>();

  public AirwaysGSWPostRequestEntity fallToleranceRange(FallToleranceRangePostRequestEntity fallToleranceRange) {
    this.fallToleranceRange = fallToleranceRange;
    return this;
  }

  /**
   * Get fallToleranceRange
   * @return fallToleranceRange
   */
  @Valid 
  @Schema(name = "fallToleranceRange", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fallToleranceRange")
  public FallToleranceRangePostRequestEntity getFallToleranceRange() {
    return fallToleranceRange;
  }

  public void setFallToleranceRange(FallToleranceRangePostRequestEntity fallToleranceRange) {
    this.fallToleranceRange = fallToleranceRange;
  }

  public AirwaysGSWPostRequestEntity droneList(List<@Valid FallSpacePostRequestDroneEntity> droneList) {
    this.droneList = droneList;
    return this;
  }

  public AirwaysGSWPostRequestEntity addDroneListItem(FallSpacePostRequestDroneEntity droneListItem) {
    if (this.droneList == null) {
      this.droneList = new ArrayList<>();
    }
    this.droneList.add(droneListItem);
    return this;
  }

  /**
   * 機体リスト
   * @return droneList
   */
  @Valid 
  @Schema(name = "droneList", description = "機体リスト", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("droneList")
  public List<@Valid FallSpacePostRequestDroneEntity> getDroneList() {
    return droneList;
  }

  public void setDroneList(List<@Valid FallSpacePostRequestDroneEntity> droneList) {
    this.droneList = droneList;
  }

  public AirwaysGSWPostRequestEntity airwayName(String airwayName) {
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

  public AirwaysGSWPostRequestEntity flightPurpose(String flightPurpose) {
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

  public AirwaysGSWPostRequestEntity airwayParts(List<@Valid AirwaysGSWPostRequestAirwayPartsEntity> airwayParts) {
    this.airwayParts = airwayParts;
    return this;
  }

  public AirwaysGSWPostRequestEntity addAirwayPartsItem(AirwaysGSWPostRequestAirwayPartsEntity airwayPartsItem) {
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
  public List<@Valid AirwaysGSWPostRequestAirwayPartsEntity> getAirwayParts() {
    return airwayParts;
  }

  public void setAirwayParts(List<@Valid AirwaysGSWPostRequestAirwayPartsEntity> airwayParts) {
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
    AirwaysGSWPostRequestEntity airwaysGSWPostRequestEntity = (AirwaysGSWPostRequestEntity) o;
    return Objects.equals(this.fallToleranceRange, airwaysGSWPostRequestEntity.fallToleranceRange) &&
        Objects.equals(this.droneList, airwaysGSWPostRequestEntity.droneList) &&
        Objects.equals(this.airwayName, airwaysGSWPostRequestEntity.airwayName) &&
        Objects.equals(this.flightPurpose, airwaysGSWPostRequestEntity.flightPurpose) &&
        Objects.equals(this.airwayParts, airwaysGSWPostRequestEntity.airwayParts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fallToleranceRange, droneList, airwayName, flightPurpose, airwayParts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AirwaysGSWPostRequestEntity {\n");
    sb.append("    fallToleranceRange: ").append(toIndentedString(fallToleranceRange)).append("\n");
    sb.append("    droneList: ").append(toIndentedString(droneList)).append("\n");
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


