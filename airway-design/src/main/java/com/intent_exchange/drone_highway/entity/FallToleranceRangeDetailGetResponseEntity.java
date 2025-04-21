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
import com.intent_exchange.drone_highway.entity.FallToleranceRangePostRequestEntityGeometry;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * FallToleranceRangeDetailGetResponseEntity
 */

@JsonTypeName("fallToleranceRangeDetailGetResponseEntity")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-01-07T04:51:07.653413100Z[Etc/UTC]", comments = "Generator version: 7.8.0")
public class FallToleranceRangeDetailGetResponseEntity {

  private String fallToleranceRangeId;

  private String businessNumber;

  private String airwayOperatorId;

  private String name;

  private String areaName;

  private String elevationTerrain;

  private FallToleranceRangePostRequestEntityGeometry geometry;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Date createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Date updatedAt;

  public FallToleranceRangeDetailGetResponseEntity() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public FallToleranceRangeDetailGetResponseEntity(String fallToleranceRangeId, String businessNumber, String airwayOperatorId, FallToleranceRangePostRequestEntityGeometry geometry, Date createdAt, Date updatedAt) {
    this.fallToleranceRangeId = fallToleranceRangeId;
    this.businessNumber = businessNumber;
    this.airwayOperatorId = airwayOperatorId;
    this.geometry = geometry;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public FallToleranceRangeDetailGetResponseEntity fallToleranceRangeId(String fallToleranceRangeId) {
    this.fallToleranceRangeId = fallToleranceRangeId;
    return this;
  }

  /**
   * 最大落下許容範囲ID
   * @return fallToleranceRangeId
   */
  @NotNull@NotEmpty @Size(min = 36, max = 36) 
  @Schema(name = "fallToleranceRangeId", description = "最大落下許容範囲ID", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("fallToleranceRangeId")
  public String getFallToleranceRangeId() {
    return fallToleranceRangeId;
  }

  public void setFallToleranceRangeId(String fallToleranceRangeId) {
    this.fallToleranceRangeId = fallToleranceRangeId;
  }

  public FallToleranceRangeDetailGetResponseEntity businessNumber(String businessNumber) {
    this.businessNumber = businessNumber;
    return this;
  }

  /**
   * 事業者番号
   * @return businessNumber
   */
  @NotNull@NotEmpty @Size(min = 1, max = 200) 
  @Schema(name = "businessNumber", description = "事業者番号", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("businessNumber")
  public String getBusinessNumber() {
    return businessNumber;
  }

  public void setBusinessNumber(String businessNumber) {
    this.businessNumber = businessNumber;
  }

  public FallToleranceRangeDetailGetResponseEntity airwayOperatorId(String airwayOperatorId) {
    this.airwayOperatorId = airwayOperatorId;
    return this;
  }

  /**
   * 航路運営者ID
   * @return airwayOperatorId
   */
  @NotNull@NotEmpty @Size(min = 1, max = 200) 
  @Schema(name = "airwayOperatorId", description = "航路運営者ID", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("airwayOperatorId")
  public String getAirwayOperatorId() {
    return airwayOperatorId;
  }

  public void setAirwayOperatorId(String airwayOperatorId) {
    this.airwayOperatorId = airwayOperatorId;
  }

  public FallToleranceRangeDetailGetResponseEntity name(String name) {
    this.name = name;
    return this;
  }

  /**
   * 名称
   * @return name
   */
  @Size(max = 200) 
  @Schema(name = "name", description = "名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public FallToleranceRangeDetailGetResponseEntity areaName(String areaName) {
    this.areaName = areaName;
    return this;
  }

  /**
   * エリア名称
   * @return areaName
   */
  @Size(max = 200) 
  @Schema(name = "areaName", description = "エリア名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("areaName")
  public String getAreaName() {
    return areaName;
  }

  public void setAreaName(String areaName) {
    this.areaName = areaName;
  }

  public FallToleranceRangeDetailGetResponseEntity elevationTerrain(String elevationTerrain) {
    this.elevationTerrain = elevationTerrain;
    return this;
  }

  /**
   * 最大標高・地形
   * @return elevationTerrain
   */
  @Size(max = 200) 
  @Schema(name = "elevationTerrain", description = "最大標高・地形", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("elevationTerrain")
  public String getElevationTerrain() {
    return elevationTerrain;
  }

  public void setElevationTerrain(String elevationTerrain) {
    this.elevationTerrain = elevationTerrain;
  }

  public FallToleranceRangeDetailGetResponseEntity geometry(FallToleranceRangePostRequestEntityGeometry geometry) {
    this.geometry = geometry;
    return this;
  }

  /**
   * Get geometry
   * @return geometry
   */
  @NotNull@Valid 
  @Schema(name = "geometry", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("geometry")
  public FallToleranceRangePostRequestEntityGeometry getGeometry() {
    return geometry;
  }

  public void setGeometry(FallToleranceRangePostRequestEntityGeometry geometry) {
    this.geometry = geometry;
  }

  public FallToleranceRangeDetailGetResponseEntity createdAt(Date createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * 登録日時
   * @return createdAt
   */
  @NotNull@Valid 
  @Schema(name = "createdAt", example = "2020-01-31T23:59:59+09:00", description = "登録日時", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("createdAt")
  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public FallToleranceRangeDetailGetResponseEntity updatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * 更新日時
   * @return updatedAt
   */
  @NotNull@Valid 
  @Schema(name = "updatedAt", example = "2020-01-31T23:59:59+09:00", description = "更新日時", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("updatedAt")
  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FallToleranceRangeDetailGetResponseEntity fallToleranceRangeDetailGetResponseEntity = (FallToleranceRangeDetailGetResponseEntity) o;
    return Objects.equals(this.fallToleranceRangeId, fallToleranceRangeDetailGetResponseEntity.fallToleranceRangeId) &&
        Objects.equals(this.businessNumber, fallToleranceRangeDetailGetResponseEntity.businessNumber) &&
        Objects.equals(this.airwayOperatorId, fallToleranceRangeDetailGetResponseEntity.airwayOperatorId) &&
        Objects.equals(this.name, fallToleranceRangeDetailGetResponseEntity.name) &&
        Objects.equals(this.areaName, fallToleranceRangeDetailGetResponseEntity.areaName) &&
        Objects.equals(this.elevationTerrain, fallToleranceRangeDetailGetResponseEntity.elevationTerrain) &&
        Objects.equals(this.geometry, fallToleranceRangeDetailGetResponseEntity.geometry) &&
        Objects.equals(this.createdAt, fallToleranceRangeDetailGetResponseEntity.createdAt) &&
        Objects.equals(this.updatedAt, fallToleranceRangeDetailGetResponseEntity.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fallToleranceRangeId, businessNumber, airwayOperatorId, name, areaName, elevationTerrain, geometry, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FallToleranceRangeDetailGetResponseEntity {\n");
    sb.append("    fallToleranceRangeId: ").append(toIndentedString(fallToleranceRangeId)).append("\n");
    sb.append("    businessNumber: ").append(toIndentedString(businessNumber)).append("\n");
    sb.append("    airwayOperatorId: ").append(toIndentedString(airwayOperatorId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    areaName: ").append(toIndentedString(areaName)).append("\n");
    sb.append("    elevationTerrain: ").append(toIndentedString(elevationTerrain)).append("\n");
    sb.append("    geometry: ").append(toIndentedString(geometry)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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


