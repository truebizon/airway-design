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
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestAirwayJunctionEntityDeviationGeometry;
import com.intent_exchange.drone_highway.entity.AirwaysPostRequestAirwayJunctionEntityGeometry;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AirwaysPostRequestAirwayJunctionEntity
 */

@JsonTypeName("airwaysPostRequestAirwayJunctionEntity")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-12-05T15:25:28.056268900+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class AirwaysPostRequestAirwayJunctionEntity {

  private String name;

  private AirwaysPostRequestAirwayJunctionEntityGeometry geometry;

  private AirwaysPostRequestAirwayJunctionEntityDeviationGeometry deviationGeometry;

  public AirwaysPostRequestAirwayJunctionEntity() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AirwaysPostRequestAirwayJunctionEntity(AirwaysPostRequestAirwayJunctionEntityGeometry geometry, AirwaysPostRequestAirwayJunctionEntityDeviationGeometry deviationGeometry) {
    this.geometry = geometry;
    this.deviationGeometry = deviationGeometry;
  }

  public AirwaysPostRequestAirwayJunctionEntity name(String name) {
    this.name = name;
    return this;
  }

  /**
   * 名称 ジャンクションの名称
   * @return name
   */
  @Size(min = 0, max = 200) 
  @Schema(name = "name", description = "名称 ジャンクションの名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public AirwaysPostRequestAirwayJunctionEntity geometry(AirwaysPostRequestAirwayJunctionEntityGeometry geometry) {
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
  public AirwaysPostRequestAirwayJunctionEntityGeometry getGeometry() {
    return geometry;
  }

  public void setGeometry(AirwaysPostRequestAirwayJunctionEntityGeometry geometry) {
    this.geometry = geometry;
  }

  public AirwaysPostRequestAirwayJunctionEntity deviationGeometry(AirwaysPostRequestAirwayJunctionEntityDeviationGeometry deviationGeometry) {
    this.deviationGeometry = deviationGeometry;
    return this;
  }

  /**
   * Get deviationGeometry
   * @return deviationGeometry
   */
  @NotNull@Valid 
  @Schema(name = "deviationGeometry", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("deviationGeometry")
  public AirwaysPostRequestAirwayJunctionEntityDeviationGeometry getDeviationGeometry() {
    return deviationGeometry;
  }

  public void setDeviationGeometry(AirwaysPostRequestAirwayJunctionEntityDeviationGeometry deviationGeometry) {
    this.deviationGeometry = deviationGeometry;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AirwaysPostRequestAirwayJunctionEntity airwaysPostRequestAirwayJunctionEntity = (AirwaysPostRequestAirwayJunctionEntity) o;
    return Objects.equals(this.name, airwaysPostRequestAirwayJunctionEntity.name) &&
        Objects.equals(this.geometry, airwaysPostRequestAirwayJunctionEntity.geometry) &&
        Objects.equals(this.deviationGeometry, airwaysPostRequestAirwayJunctionEntity.deviationGeometry);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, geometry, deviationGeometry);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AirwaysPostRequestAirwayJunctionEntity {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    geometry: ").append(toIndentedString(geometry)).append("\n");
    sb.append("    deviationGeometry: ").append(toIndentedString(deviationGeometry)).append("\n");
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


