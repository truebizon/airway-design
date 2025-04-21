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

import java.util.Objects;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * FallToleranceRangePostRequestEntity
 */

@JsonTypeName("fallToleranceRangePostRequestEntity")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2025-01-07T01:28:12.507742400Z[Etc/UTC]", comments = "Generator version: 7.8.0")
public class FallToleranceRangePostRequestEntity {

  private String businessNumber;

  private String airwayOperatorId;

  private String name;

  private String areaName;

  private String elevationTerrain;

  private FallToleranceRangePostRequestEntityGeometry geometry;

  public FallToleranceRangePostRequestEntity() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public FallToleranceRangePostRequestEntity(String businessNumber, String airwayOperatorId,
      FallToleranceRangePostRequestEntityGeometry geometry) {
    this.businessNumber = businessNumber;
    this.airwayOperatorId = airwayOperatorId;
    this.geometry = geometry;
  }

  public FallToleranceRangePostRequestEntity businessNumber(String businessNumber) {
    this.businessNumber = businessNumber;
    return this;
  }

  /**
   * 事業者番号
   * 
   * @return businessNumber
   */
  @NotNull
  @NotEmpty
  @Size(min = 1, max = 200)
  @Schema(name = "businessNumber", description = "事業者番号",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("businessNumber")
  public String getBusinessNumber() {
    return businessNumber;
  }

  public void setBusinessNumber(String businessNumber) {
    this.businessNumber = businessNumber;
  }

  public FallToleranceRangePostRequestEntity airwayOperatorId(String airwayOperatorId) {
    this.airwayOperatorId = airwayOperatorId;
    return this;
  }

  /**
   * 航路運営者ID
   * 
   * @return airwayOperatorId
   */
  @NotNull
  @NotEmpty
  @Size(min = 1, max = 200)
  @Schema(name = "airwayOperatorId", description = "航路運営者ID",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("airwayOperatorId")
  public String getAirwayOperatorId() {
    return airwayOperatorId;
  }

  public void setAirwayOperatorId(String airwayOperatorId) {
    this.airwayOperatorId = airwayOperatorId;
  }

  public FallToleranceRangePostRequestEntity name(String name) {
    this.name = name;
    return this;
  }

  /**
   * 名称
   * 
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

  public FallToleranceRangePostRequestEntity areaName(String areaName) {
    this.areaName = areaName;
    return this;
  }

  /**
   * エリア名称
   * 
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

  public FallToleranceRangePostRequestEntity elevationTerrain(String elevationTerrain) {
    this.elevationTerrain = elevationTerrain;
    return this;
  }

  /**
   * 最大標高・地形
   * 
   * @return elevationTerrain
   */
  @Size(max = 200)
  @Schema(name = "elevationTerrain", description = "最大標高・地形",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("elevationTerrain")
  public String getElevationTerrain() {
    return elevationTerrain;
  }

  public void setElevationTerrain(String elevationTerrain) {
    this.elevationTerrain = elevationTerrain;
  }

  public FallToleranceRangePostRequestEntity geometry(
      FallToleranceRangePostRequestEntityGeometry geometry) {
    this.geometry = geometry;
    return this;
  }

  /**
   * Get geometry
   * 
   * @return geometry
   */
  @NotNull
  @Valid
  @Schema(name = "geometry", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("geometry")
  public FallToleranceRangePostRequestEntityGeometry getGeometry() {
    return geometry;
  }

  public void setGeometry(FallToleranceRangePostRequestEntityGeometry geometry) {
    this.geometry = geometry;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FallToleranceRangePostRequestEntity fallToleranceRangePostRequestEntity =
        (FallToleranceRangePostRequestEntity) o;
    return Objects.equals(this.businessNumber, fallToleranceRangePostRequestEntity.businessNumber)
        && Objects.equals(this.airwayOperatorId,
            fallToleranceRangePostRequestEntity.airwayOperatorId)
        && Objects.equals(this.name, fallToleranceRangePostRequestEntity.name)
        && Objects.equals(this.areaName, fallToleranceRangePostRequestEntity.areaName)
        && Objects.equals(this.elevationTerrain,
            fallToleranceRangePostRequestEntity.elevationTerrain)
        && Objects.equals(this.geometry, fallToleranceRangePostRequestEntity.geometry);
  }

  @Override
  public int hashCode() {
    return Objects.hash(businessNumber, airwayOperatorId, name, areaName, elevationTerrain,
        geometry);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FallToleranceRangePostRequestEntity {\n");
    sb.append("    businessNumber: ").append(toIndentedString(businessNumber)).append("\n");
    sb.append("    airwayOperatorId: ").append(toIndentedString(airwayOperatorId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    areaName: ").append(toIndentedString(areaName)).append("\n");
    sb.append("    elevationTerrain: ").append(toIndentedString(elevationTerrain)).append("\n");
    sb.append("    geometry: ").append(toIndentedString(geometry)).append("\n");
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


