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
 * 落下空間の断面に描かれる航路/航路逸脱領域の位置座標
 */

@Schema(name = "airwaySectionsEntity", description = "落下空間の断面に描かれる航路/航路逸脱領域の位置座標")
@JsonTypeName("airwaySectionsEntity")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-01-22T16:30:39.584461700+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class AirwaySectionsEntity {

  private String airwaySectionId;

  private String airwaySectionName;

  @Valid
  private List<String> airwayJunctionIds = new ArrayList<>();

  @Valid
  private List<String> droneportIds = new ArrayList<>();

  public AirwaySectionsEntity airwaySectionId(String airwaySectionId) {
    this.airwaySectionId = airwaySectionId;
    return this;
  }

  /**
   * 航路区画ID
   * @return airwaySectionId
   */
  
  @Schema(name = "airwaySectionId", description = "航路区画ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("airwaySectionId")
  public String getAirwaySectionId() {
    return airwaySectionId;
  }

  public void setAirwaySectionId(String airwaySectionId) {
    this.airwaySectionId = airwaySectionId;
  }

  public AirwaySectionsEntity airwaySectionName(String airwaySectionName) {
    this.airwaySectionName = airwaySectionName;
    return this;
  }

  /**
   * 航路区画名
   * @return airwaySectionName
   */
  
  @Schema(name = "airwaySectionName", description = "航路区画名", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("airwaySectionName")
  public String getAirwaySectionName() {
    return airwaySectionName;
  }

  public void setAirwaySectionName(String airwaySectionName) {
    this.airwaySectionName = airwaySectionName;
  }

  public AirwaySectionsEntity airwayJunctionIds(List<String> airwayJunctionIds) {
    this.airwayJunctionIds = airwayJunctionIds;
    return this;
  }

  public AirwaySectionsEntity addAirwayJunctionIdsItem(String airwayJunctionIdsItem) {
    if (this.airwayJunctionIds == null) {
      this.airwayJunctionIds = new ArrayList<>();
    }
    this.airwayJunctionIds.add(airwayJunctionIdsItem);
    return this;
  }

  /**
   * 航路区画に接している航路点ID
   * @return airwayJunctionIds
   */
  
  @Schema(name = "airwayJunctionIds", example = "[\"airway_point_1\",\"airway_point_2\"]", description = "航路区画に接している航路点ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("airwayJunctionIds")
  public List<String> getAirwayJunctionIds() {
    return airwayJunctionIds;
  }

  public void setAirwayJunctionIds(List<String> airwayJunctionIds) {
    this.airwayJunctionIds = airwayJunctionIds;
  }

  public AirwaySectionsEntity droneportIds(List<String> droneportIds) {
    this.droneportIds = droneportIds;
    return this;
  }

  public AirwaySectionsEntity addDroneportIdsItem(String droneportIdsItem) {
    if (this.droneportIds == null) {
      this.droneportIds = new ArrayList<>();
    }
    this.droneportIds.add(droneportIdsItem);
    return this;
  }

  /**
   * 航路区画にて使用できるドローンポートID
   * @return droneportIds
   */
  
  @Schema(name = "droneportIds", example = "[\"droneport_1\",\"droneport_2\"]", description = "航路区画にて使用できるドローンポートID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("droneportIds")
  public List<String> getDroneportIds() {
    return droneportIds;
  }

  public void setDroneportIds(List<String> droneportIds) {
    this.droneportIds = droneportIds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AirwaySectionsEntity airwaySectionsEntity = (AirwaySectionsEntity) o;
    return Objects.equals(this.airwaySectionId, airwaySectionsEntity.airwaySectionId) &&
        Objects.equals(this.airwaySectionName, airwaySectionsEntity.airwaySectionName) &&
        Objects.equals(this.airwayJunctionIds, airwaySectionsEntity.airwayJunctionIds) &&
        Objects.equals(this.droneportIds, airwaySectionsEntity.droneportIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(airwaySectionId, airwaySectionName, airwayJunctionIds, droneportIds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AirwaySectionsEntity {\n");
    sb.append("    airwaySectionId: ").append(toIndentedString(airwaySectionId)).append("\n");
    sb.append("    airwaySectionName: ").append(toIndentedString(airwaySectionName)).append("\n");
    sb.append("    airwayJunctionIds: ").append(toIndentedString(airwayJunctionIds)).append("\n");
    sb.append("    droneportIds: ").append(toIndentedString(droneportIds)).append("\n");
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


