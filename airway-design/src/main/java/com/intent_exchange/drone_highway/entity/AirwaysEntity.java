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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * AirwaysEntity
 */

@JsonTypeName("airwaysEntity")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2025-01-22T11:28:55.756126300+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class AirwaysEntity {

  private String airwayId;

  private String airwayName;

  private String flightPurpose;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Date createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Date updatedAt;

  @Valid
  private List<Integer> droneList = new ArrayList<>();

  @Valid
  private List<@Valid AirwayJunctionsEntity> airwayJunctions = new ArrayList<>();

  @Valid
  private List<@Valid AirwaySectionsEntity> airwaySections = new ArrayList<>();

  public AirwaysEntity airwayId(String airwayId) {
    this.airwayId = airwayId;
    return this;
  }

  /**
   * 航路ID
   * 
   * @return airwayId
   */

  @Schema(name = "airwayId", description = "航路ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("airwayId")
  public String getAirwayId() {
    return airwayId;
  }

  public void setAirwayId(String airwayId) {
    this.airwayId = airwayId;
  }

  public AirwaysEntity airwayName(String airwayName) {
    this.airwayName = airwayName;
    return this;
  }

  /**
   * 航路名
   * 
   * @return airwayName
   */

  @Schema(name = "airwayName", description = "航路名", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("airwayName")
  public String getAirwayName() {
    return airwayName;
  }

  public void setAirwayName(String airwayName) {
    this.airwayName = airwayName;
  }

  public AirwaysEntity flightPurpose(String flightPurpose) {
    this.flightPurpose = flightPurpose;
    return this;
  }

  /**
   * 飛行目的
   * 
   * @return flightPurpose
   */

  @Schema(name = "flightPurpose", description = "飛行目的",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("flightPurpose")
  public String getFlightPurpose() {
    return flightPurpose;
  }

  public void setFlightPurpose(String flightPurpose) {
    this.flightPurpose = flightPurpose;
  }

  public AirwaysEntity createdAt(Date createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * 
   * @return createdAt
   */
  @Valid
  @Schema(name = "createdAt", example = "2020-01-31T23:59:59+09:00",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("createdAt")
  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public AirwaysEntity updatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * 
   * @return updatedAt
   */
  @Valid
  @Schema(name = "updatedAt", example = "2020-01-31T23:59:59+09:00",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updatedAt")
  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  public AirwaysEntity droneList(List<Integer> droneList) {
    this.droneList = droneList;
    return this;
  }

  public AirwaysEntity addDroneListItem(Integer droneListItem) {
    if (this.droneList == null) {
      this.droneList = new ArrayList<>();
    }
    this.droneList.add(droneListItem);
    return this;
  }

  /**
   * 航路を利用可能なドローンの機体種別IDのリスト
   * 
   * @return droneList
   */
  @Valid
  @Schema(name = "droneList", example = "[0,1]", description = "航路を利用可能なドローンの機体種別IDのリスト",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("droneList")
  public List<Integer> getDroneList() {
    return droneList;
  }

  public void setDroneList(List<Integer> droneList) {
    this.droneList = droneList;
  }

  public AirwaysEntity airwayJunctions(List<@Valid AirwayJunctionsEntity> airwayJunctions) {
    this.airwayJunctions = airwayJunctions;
    return this;
  }

  public AirwaysEntity addAirwayJunctionsItem(AirwayJunctionsEntity airwayJunctionsItem) {
    if (this.airwayJunctions == null) {
      this.airwayJunctions = new ArrayList<>();
    }
    this.airwayJunctions.add(airwayJunctionsItem);
    return this;
  }

  /**
   * ジャンクション
   * 
   * @return airwayJunctions
   */
  @Valid
  @Schema(name = "airwayJunctions", description = "ジャンクション",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("airwayJunctions")
  public List<@Valid AirwayJunctionsEntity> getAirwayJunctions() {
    return airwayJunctions;
  }

  public void setAirwayJunctions(List<@Valid AirwayJunctionsEntity> airwayJunctions) {
    this.airwayJunctions = airwayJunctions;
  }

  public AirwaysEntity airwaySections(List<@Valid AirwaySectionsEntity> airwaySections) {
    this.airwaySections = airwaySections;
    return this;
  }

  public AirwaysEntity addAirwaySectionsItem(AirwaySectionsEntity airwaySectionsItem) {
    if (this.airwaySections == null) {
      this.airwaySections = new ArrayList<>();
    }
    this.airwaySections.add(airwaySectionsItem);
    return this;
  }

  /**
   * Get airwaySections
   * 
   * @return airwaySections
   */
  @Valid
  @Schema(name = "airwaySections", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("airwaySections")
  public List<@Valid AirwaySectionsEntity> getAirwaySections() {
    return airwaySections;
  }

  public void setAirwaySections(List<@Valid AirwaySectionsEntity> airwaySections) {
    this.airwaySections = airwaySections;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AirwaysEntity airwaysEntity = (AirwaysEntity) o;
    return Objects.equals(this.airwayId, airwaysEntity.airwayId)
        && Objects.equals(this.airwayName, airwaysEntity.airwayName)
        && Objects.equals(this.flightPurpose, airwaysEntity.flightPurpose)
        && Objects.equals(this.createdAt, airwaysEntity.createdAt)
        && Objects.equals(this.updatedAt, airwaysEntity.updatedAt)
        && Objects.equals(this.droneList, airwaysEntity.droneList)
        && Objects.equals(this.airwayJunctions, airwaysEntity.airwayJunctions)
        && Objects.equals(this.airwaySections, airwaysEntity.airwaySections);
  }

  @Override
  public int hashCode() {
    return Objects.hash(airwayId, airwayName, flightPurpose, createdAt, updatedAt, droneList,
        airwayJunctions, airwaySections);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AirwaysEntity {\n");
    sb.append("    airwayId: ").append(toIndentedString(airwayId)).append("\n");
    sb.append("    airwayName: ").append(toIndentedString(airwayName)).append("\n");
    sb.append("    flightPurpose: ").append(toIndentedString(flightPurpose)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("    droneList: ").append(toIndentedString(droneList)).append("\n");
    sb.append("    airwayJunctions: ").append(toIndentedString(airwayJunctions)).append("\n");
    sb.append("    airwaySections: ").append(toIndentedString(airwaySections)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}


