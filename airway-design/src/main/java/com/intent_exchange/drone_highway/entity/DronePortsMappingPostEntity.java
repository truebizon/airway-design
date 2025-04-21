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
import java.util.List;
import java.util.Objects;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DronePortsMappingPostEntity
 */

@JsonTypeName("dronePortsMappingPostEntity")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2025-01-29T09:19:22.536020600+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class DronePortsMappingPostEntity {

  private String airwayId;

  @Valid
  private List<@Valid DronePortsMappingPostEntityAirwaySectionsInner> airwaySections =
      new ArrayList<>();

  public DronePortsMappingPostEntity() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DronePortsMappingPostEntity(String airwayId,
      List<@Valid DronePortsMappingPostEntityAirwaySectionsInner> airwaySections) {
    this.airwayId = airwayId;
    this.airwaySections = airwaySections;
  }

  public DronePortsMappingPostEntity airwayId(String airwayId) {
    this.airwayId = airwayId;
    return this;
  }

  /**
   * 航路ID
   * 
   * @return airwayId
   */
  @NotNull
  @NotEmpty
  @Schema(name = "airwayId", description = "航路ID", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("airwayId")
  public String getAirwayId() {
    return airwayId;
  }

  public void setAirwayId(String airwayId) {
    this.airwayId = airwayId;
  }

  public DronePortsMappingPostEntity airwaySections(
      List<@Valid DronePortsMappingPostEntityAirwaySectionsInner> airwaySections) {
    this.airwaySections = airwaySections;
    return this;
  }

  public DronePortsMappingPostEntity addAirwaySectionsItem(
      DronePortsMappingPostEntityAirwaySectionsInner airwaySectionsItem) {
    if (this.airwaySections == null) {
      this.airwaySections = new ArrayList<>();
    }
    this.airwaySections.add(airwaySectionsItem);
    return this;
  }

  /**
   * 航路区画とポートの組み合わせ
   * 
   * @return airwaySections
   */
  @NotNull
  @NotEmpty
  @Valid
  @Schema(name = "airwaySections", description = "航路区画とポートの組み合わせ",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("airwaySections")
  public List<@Valid DronePortsMappingPostEntityAirwaySectionsInner> getAirwaySections() {
    return airwaySections;
  }

  public void setAirwaySections(
      List<@Valid DronePortsMappingPostEntityAirwaySectionsInner> airwaySections) {
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
    DronePortsMappingPostEntity dronePortsMappingPostEntity = (DronePortsMappingPostEntity) o;
    return Objects.equals(this.airwayId, dronePortsMappingPostEntity.airwayId)
        && Objects.equals(this.airwaySections, dronePortsMappingPostEntity.airwaySections);
  }

  @Override
  public int hashCode() {
    return Objects.hash(airwayId, airwaySections);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DronePortsMappingPostEntity {\n");
    sb.append("    airwayId: ").append(toIndentedString(airwayId)).append("\n");
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


