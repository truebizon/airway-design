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
import com.intent_exchange.drone_highway.entity.FallSpacePostRequestDroneEntity;
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
 * FallSpacePostRequestEntity
 */

@JsonTypeName("fallSpacePostRequestEntity")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-12-16T04:57:50.352694600Z[Etc/UTC]", comments = "Generator version: 7.8.0")
public class FallSpacePostRequestEntity {

  private String fallToleranceRangeId;

  private Integer numCrossSectionDivisions;

  @Valid
  private List<@Valid FallSpacePostRequestDroneEntity> droneList = new ArrayList<>();

  public FallSpacePostRequestEntity() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public FallSpacePostRequestEntity(String fallToleranceRangeId, Integer numCrossSectionDivisions, List<@Valid FallSpacePostRequestDroneEntity> droneList) {
    this.fallToleranceRangeId = fallToleranceRangeId;
    this.numCrossSectionDivisions = numCrossSectionDivisions;
    this.droneList = droneList;
  }

  public FallSpacePostRequestEntity fallToleranceRangeId(String fallToleranceRangeId) {
    this.fallToleranceRangeId = fallToleranceRangeId;
    return this;
  }

  /**
   * 最大落下許容範囲ID
   * @return fallToleranceRangeId
   */
  @NotNull@NotEmpty @Size(min = 36, max = 36) 
  @Schema(name = "fallToleranceRangeId", example = "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", description = "最大落下許容範囲ID", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("fallToleranceRangeId")
  public String getFallToleranceRangeId() {
    return fallToleranceRangeId;
  }

  public void setFallToleranceRangeId(String fallToleranceRangeId) {
    this.fallToleranceRangeId = fallToleranceRangeId;
  }

  public FallSpacePostRequestEntity numCrossSectionDivisions(Integer numCrossSectionDivisions) {
    this.numCrossSectionDivisions = numCrossSectionDivisions;
    return this;
  }

  /**
   * 断面分割数 落下空間(断面)のX軸を分割する数 設定値+1で分割される
   * @return numCrossSectionDivisions
   */
  @NotNull
  @Schema(name = "numCrossSectionDivisions", description = "断面分割数 落下空間(断面)のX軸を分割する数 設定値+1で分割される", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("numCrossSectionDivisions")
  public Integer getNumCrossSectionDivisions() {
    return numCrossSectionDivisions;
  }

  public void setNumCrossSectionDivisions(Integer numCrossSectionDivisions) {
    this.numCrossSectionDivisions = numCrossSectionDivisions;
  }

  public FallSpacePostRequestEntity droneList(List<@Valid FallSpacePostRequestDroneEntity> droneList) {
    this.droneList = droneList;
    return this;
  }

  public FallSpacePostRequestEntity addDroneListItem(FallSpacePostRequestDroneEntity droneListItem) {
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
  @NotNull@Valid 
  @Schema(name = "droneList", description = "機体リスト", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("droneList")
  public List<@Valid FallSpacePostRequestDroneEntity> getDroneList() {
    return droneList;
  }

  public void setDroneList(List<@Valid FallSpacePostRequestDroneEntity> droneList) {
    this.droneList = droneList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FallSpacePostRequestEntity fallSpacePostRequestEntity = (FallSpacePostRequestEntity) o;
    return Objects.equals(this.fallToleranceRangeId, fallSpacePostRequestEntity.fallToleranceRangeId) &&
        Objects.equals(this.numCrossSectionDivisions, fallSpacePostRequestEntity.numCrossSectionDivisions) &&
        Objects.equals(this.droneList, fallSpacePostRequestEntity.droneList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fallToleranceRangeId, numCrossSectionDivisions, droneList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FallSpacePostRequestEntity {\n");
    sb.append("    fallToleranceRangeId: ").append(toIndentedString(fallToleranceRangeId)).append("\n");
    sb.append("    numCrossSectionDivisions: ").append(toIndentedString(numCrossSectionDivisions)).append("\n");
    sb.append("    droneList: ").append(toIndentedString(droneList)).append("\n");
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


