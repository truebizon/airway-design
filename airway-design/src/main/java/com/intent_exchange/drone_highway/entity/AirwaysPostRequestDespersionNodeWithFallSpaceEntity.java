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
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestDespersionNodeWithFallSpaceEntityFallSpace;
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestDespersionNodeWithFallSpaceEntityGeometry;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AirwaysPostRequestDespersionNodeWithFallSpaceEntity
 */

@JsonTypeName("airwaysPostRequestDespersionNodeWithFallSpaceEntity")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-01-06T14:28:36.995126+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class AirwaysPostRequestDespersionNodeWithFallSpaceEntity {

  private String name;

  private AirwaysPostRequestDespersionNodeWithFallSpaceEntityGeometry geometry;

  private AirwaysPostRequestDespersionNodeWithFallSpaceEntityFallSpace fallSpace;

  public AirwaysPostRequestDespersionNodeWithFallSpaceEntity name(String name) {
    this.name = name;
    return this;
  }

  /**
   * 名称 落下範囲節の名称
   * @return name
   */
  @Size(min = 0, max = 200) 
  @Schema(name = "name", description = "名称 落下範囲節の名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public AirwaysPostRequestDespersionNodeWithFallSpaceEntity geometry(AirwaysPostRequestDespersionNodeWithFallSpaceEntityGeometry geometry) {
    this.geometry = geometry;
    return this;
  }

  /**
   * Get geometry
   * @return geometry
   */
  @Valid 
  @Schema(name = "geometry", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("geometry")
  public AirwaysPostRequestDespersionNodeWithFallSpaceEntityGeometry getGeometry() {
    return geometry;
  }

  public void setGeometry(AirwaysPostRequestDespersionNodeWithFallSpaceEntityGeometry geometry) {
    this.geometry = geometry;
  }

  public AirwaysPostRequestDespersionNodeWithFallSpaceEntity fallSpace(AirwaysPostRequestDespersionNodeWithFallSpaceEntityFallSpace fallSpace) {
    this.fallSpace = fallSpace;
    return this;
  }

  /**
   * Get fallSpace
   * @return fallSpace
   */
  @Valid 
  @Schema(name = "fallSpace", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fallSpace")
  public AirwaysPostRequestDespersionNodeWithFallSpaceEntityFallSpace getFallSpace() {
    return fallSpace;
  }

  public void setFallSpace(AirwaysPostRequestDespersionNodeWithFallSpaceEntityFallSpace fallSpace) {
    this.fallSpace = fallSpace;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AirwaysPostRequestDespersionNodeWithFallSpaceEntity airwaysPostRequestDespersionNodeWithFallSpaceEntity = (AirwaysPostRequestDespersionNodeWithFallSpaceEntity) o;
    return Objects.equals(this.name, airwaysPostRequestDespersionNodeWithFallSpaceEntity.name) &&
        Objects.equals(this.geometry, airwaysPostRequestDespersionNodeWithFallSpaceEntity.geometry) &&
        Objects.equals(this.fallSpace, airwaysPostRequestDespersionNodeWithFallSpaceEntity.fallSpace);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, geometry, fallSpace);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AirwaysPostRequestDespersionNodeWithFallSpaceEntity {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    geometry: ").append(toIndentedString(geometry)).append("\n");
    sb.append("    fallSpace: ").append(toIndentedString(fallSpace)).append("\n");
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


