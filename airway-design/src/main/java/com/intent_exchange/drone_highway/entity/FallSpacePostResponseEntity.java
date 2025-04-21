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
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * FallSpacePostResponseEntity
 */

@JsonTypeName("fallSpacePostResponseEntity")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-11-28T12:02:41.994211300Z[Etc/UTC]", comments = "Generator version: 7.8.0")
public class FallSpacePostResponseEntity {

  private Integer airwayDeterminationId;

  public FallSpacePostResponseEntity() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public FallSpacePostResponseEntity(Integer airwayDeterminationId) {
    this.airwayDeterminationId = airwayDeterminationId;
  }

  public FallSpacePostResponseEntity airwayDeterminationId(Integer airwayDeterminationId) {
    this.airwayDeterminationId = airwayDeterminationId;
    return this;
  }

  /**
   * 航路画定IDを表すID  作成中の航路を一意に表すID  以降の航路作成系のAPIで必ず指定する 
   * @return airwayDeterminationId
   */
  @NotNull
  @Schema(name = "airwayDeterminationId", description = "航路画定IDを表すID  作成中の航路を一意に表すID  以降の航路作成系のAPIで必ず指定する ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("airwayDeterminationId")
  public Integer getAirwayDeterminationId() {
    return airwayDeterminationId;
  }

  public void setAirwayDeterminationId(Integer airwayDeterminationId) {
    this.airwayDeterminationId = airwayDeterminationId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FallSpacePostResponseEntity fallSpacePostResponseEntity = (FallSpacePostResponseEntity) o;
    return Objects.equals(this.airwayDeterminationId, fallSpacePostResponseEntity.airwayDeterminationId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(airwayDeterminationId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FallSpacePostResponseEntity {\n");
    sb.append("    airwayDeterminationId: ").append(toIndentedString(airwayDeterminationId)).append("\n");
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


