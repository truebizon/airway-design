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
import com.intent_exchange.drone_highway.entity.FallSpaceCrossSectionPostRequestEntityGeometry;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * FallSpaceCrossSectionPostRequestEntity
 */

@JsonTypeName("fallSpaceCrossSectionPostRequestEntity")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-11-28T12:02:41.994211300Z[Etc/UTC]", comments = "Generator version: 7.8.0")
public class FallSpaceCrossSectionPostRequestEntity {

  private Integer airwayDeterminationId;

  private FallSpaceCrossSectionPostRequestEntityGeometry geometry;

  public FallSpaceCrossSectionPostRequestEntity() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public FallSpaceCrossSectionPostRequestEntity(Integer airwayDeterminationId, FallSpaceCrossSectionPostRequestEntityGeometry geometry) {
    this.airwayDeterminationId = airwayDeterminationId;
    this.geometry = geometry;
  }

  public FallSpaceCrossSectionPostRequestEntity airwayDeterminationId(Integer airwayDeterminationId) {
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

  public FallSpaceCrossSectionPostRequestEntity geometry(FallSpaceCrossSectionPostRequestEntityGeometry geometry) {
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
  public FallSpaceCrossSectionPostRequestEntityGeometry getGeometry() {
    return geometry;
  }

  public void setGeometry(FallSpaceCrossSectionPostRequestEntityGeometry geometry) {
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
    FallSpaceCrossSectionPostRequestEntity fallSpaceCrossSectionPostRequestEntity = (FallSpaceCrossSectionPostRequestEntity) o;
    return Objects.equals(this.airwayDeterminationId, fallSpaceCrossSectionPostRequestEntity.airwayDeterminationId) &&
        Objects.equals(this.geometry, fallSpaceCrossSectionPostRequestEntity.geometry);
  }

  @Override
  public int hashCode() {
    return Objects.hash(airwayDeterminationId, geometry);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FallSpaceCrossSectionPostRequestEntity {\n");
    sb.append("    airwayDeterminationId: ").append(toIndentedString(airwayDeterminationId)).append("\n");
    sb.append("    geometry: ").append(toIndentedString(geometry)).append("\n");
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


