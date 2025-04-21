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
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * FallToleranceRangeGetResponseItemEntity
 */

@JsonTypeName("fallToleranceRangeGetResponseItemEntity")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-12-10T06:19:55.056956500Z[Etc/UTC]", comments = "Generator version: 7.8.0")
public class FallToleranceRangeGetResponseItemEntity {

  private String fallToleranceRangeId;

  private String name;

  private String areaName;

  private String airwayOperatorId;

  @Valid
  private List<String> airwayIdUse = new ArrayList<>();

  private FallToleranceRangeGetResponseItemEntityGeometry geometry;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Date createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Date updatedAt;

  public FallToleranceRangeGetResponseItemEntity() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public FallToleranceRangeGetResponseItemEntity(String fallToleranceRangeId,
      String airwayOperatorId, List<String> airwayIdUse,
      FallToleranceRangeGetResponseItemEntityGeometry geometry, Date createdAt, Date updatedAt) {
    this.fallToleranceRangeId = fallToleranceRangeId;
    this.airwayOperatorId = airwayOperatorId;
    this.airwayIdUse = airwayIdUse;
    this.geometry = geometry;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public FallToleranceRangeGetResponseItemEntity fallToleranceRangeId(String fallToleranceRangeId) {
    this.fallToleranceRangeId = fallToleranceRangeId;
    return this;
  }

  /**
   * 最大落下許容範囲ID
   * 
   * @return fallToleranceRangeId
   */
  @NotNull
  @NotEmpty
  @Size(min = 36, max = 36)
  @Schema(name = "fallToleranceRangeId", example = "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX",
      description = "最大落下許容範囲ID", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("fallToleranceRangeId")
  public String getFallToleranceRangeId() {
    return fallToleranceRangeId;
  }

  public void setFallToleranceRangeId(String fallToleranceRangeId) {
    this.fallToleranceRangeId = fallToleranceRangeId;
  }

  public FallToleranceRangeGetResponseItemEntity name(String name) {
    this.name = name;
    return this;
  }

  /**
   * 名称
   * 
   * @return name
   */
  @Size(min = 0, max = 200)
  @Schema(name = "name", description = "名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public FallToleranceRangeGetResponseItemEntity areaName(String areaName) {
    this.areaName = areaName;
    return this;
  }

  /**
   * エリア名称
   * 
   * @return areaName
   */
  @Size(min = 0, max = 200)
  @Schema(name = "areaName", description = "エリア名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("areaName")
  public String getAreaName() {
    return areaName;
  }

  public void setAreaName(String areaName) {
    this.areaName = areaName;
  }

  public FallToleranceRangeGetResponseItemEntity airwayOperatorId(String airwayOperatorId) {
    this.airwayOperatorId = airwayOperatorId;
    return this;
  }

  /**
   * エリア名称
   * 
   * @return airwayOperatorId
   */
  @NotNull
  @NotEmpty
  @Size(min = 1, max = 200)
  @Schema(name = "airwayOperatorId", description = "エリア名称",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("airwayOperatorId")
  public String getAirwayOperatorId() {
    return airwayOperatorId;
  }

  public void setAirwayOperatorId(String airwayOperatorId) {
    this.airwayOperatorId = airwayOperatorId;
  }

  public FallToleranceRangeGetResponseItemEntity airwayIdUse(List<String> airwayIdUse) {
    this.airwayIdUse = airwayIdUse;
    return this;
  }

  public FallToleranceRangeGetResponseItemEntity addAirwayIdUseItem(String airwayIdUseItem) {
    if (this.airwayIdUse == null) {
      this.airwayIdUse = new ArrayList<>();
    }
    this.airwayIdUse.add(airwayIdUseItem);
    return this;
  }

  /**
   * 使用中航路ID
   * 
   * @return airwayIdUse
   */
  @NotNull
  @Schema(name = "airwayIdUse", description = "使用中航路ID",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("airwayIdUse")
  public List<String> getAirwayIdUse() {
    return airwayIdUse;
  }

  public void setAirwayIdUse(List<String> airwayIdUse) {
    this.airwayIdUse = airwayIdUse;
  }

  public FallToleranceRangeGetResponseItemEntity geometry(
      FallToleranceRangeGetResponseItemEntityGeometry geometry) {
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
  public FallToleranceRangeGetResponseItemEntityGeometry getGeometry() {
    return geometry;
  }

  public void setGeometry(FallToleranceRangeGetResponseItemEntityGeometry geometry) {
    this.geometry = geometry;
  }

  public FallToleranceRangeGetResponseItemEntity createdAt(Date createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * 
   * @return createdAt
   */
  @NotNull
  @Valid
  @Schema(name = "createdAt", example = "2020-01-31T23:59:59+09:00",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("createdAt")
  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public FallToleranceRangeGetResponseItemEntity updatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * 
   * @return updatedAt
   */
  @NotNull
  @Valid
  @Schema(name = "updatedAt", example = "2020-01-31T23:59:59+09:00",
      requiredMode = Schema.RequiredMode.REQUIRED)
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
    FallToleranceRangeGetResponseItemEntity fallToleranceRangeGetResponseItemEntity =
        (FallToleranceRangeGetResponseItemEntity) o;
    return Objects.equals(this.fallToleranceRangeId,
        fallToleranceRangeGetResponseItemEntity.fallToleranceRangeId)
        && Objects.equals(this.name, fallToleranceRangeGetResponseItemEntity.name)
        && Objects.equals(this.areaName, fallToleranceRangeGetResponseItemEntity.areaName)
        && Objects.equals(this.airwayOperatorId,
            fallToleranceRangeGetResponseItemEntity.airwayOperatorId)
        && Objects.equals(this.airwayIdUse, fallToleranceRangeGetResponseItemEntity.airwayIdUse)
        && Objects.equals(this.geometry, fallToleranceRangeGetResponseItemEntity.geometry)
        && Objects.equals(this.createdAt, fallToleranceRangeGetResponseItemEntity.createdAt)
        && Objects.equals(this.updatedAt, fallToleranceRangeGetResponseItemEntity.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fallToleranceRangeId, name, areaName, airwayOperatorId, airwayIdUse,
        geometry, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FallToleranceRangeGetResponseItemEntity {\n");
    sb.append("    fallToleranceRangeId: ")
        .append(toIndentedString(fallToleranceRangeId))
        .append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    areaName: ").append(toIndentedString(areaName)).append("\n");
    sb.append("    airwayOperatorId: ").append(toIndentedString(airwayOperatorId)).append("\n");
    sb.append("    airwayIdUse: ").append(toIndentedString(airwayIdUse)).append("\n");
    sb.append("    geometry: ").append(toIndentedString(geometry)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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


