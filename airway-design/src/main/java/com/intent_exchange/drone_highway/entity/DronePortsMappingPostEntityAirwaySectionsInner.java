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
 * DronePortsMappingPostEntityAirwaySectionsInner
 */

@JsonTypeName("dronePortsMappingPostEntity_airwaySections_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2025-01-29T09:19:22.536020600+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class DronePortsMappingPostEntityAirwaySectionsInner {

  private String airwaySectionId;

  private List<String> droneportIds = new ArrayList<>();

  public DronePortsMappingPostEntityAirwaySectionsInner() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DronePortsMappingPostEntityAirwaySectionsInner(String airwaySectionId) {
    this.airwaySectionId = airwaySectionId;
  }

  public DronePortsMappingPostEntityAirwaySectionsInner airwaySectionId(String airwaySectionId) {
    this.airwaySectionId = airwaySectionId;
    return this;
  }

  /**
   * 航路区画ID
   * 
   * @return airwaySectionId
   */
  @NotNull
  @NotEmpty
  @Schema(name = "airwaySectionId", description = "航路区画ID",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("airwaySectionId")
  public String getAirwaySectionId() {
    return airwaySectionId;
  }

  public void setAirwaySectionId(String airwaySectionId) {
    this.airwaySectionId = airwaySectionId;
  }

  public DronePortsMappingPostEntityAirwaySectionsInner droneportIds(List<String> droneportIds) {
    this.droneportIds = droneportIds;
    return this;
  }

  public DronePortsMappingPostEntityAirwaySectionsInner addDroneportIdsItem(
      String droneportIdsItem) {
    if (this.droneportIds == null) {
      this.droneportIds = new ArrayList<>();
    }
    this.droneportIds.add(droneportIdsItem);
    return this;
  }

  /**
   * 航路区画に紐づけるドローンポートID。複数指定可 未指定もしくは空の場合、航路区画に紐づくポートを解除する。
   * 
   * @return droneportIds
   */
  @Valid
  @Schema(name = "droneportIds", example = "[\"droneport_1\",\"droneport_2\"]",
      description = "航路区画に紐づけるドローンポートID。複数指定可  未指定もしくは空の場合、航路区画に紐づくポートを解除する。 ",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("droneportIds")
  public List<String> getDroneportIds() {
    return droneportIds;
  }

  public void setDroneportIds(List<String> droneportIds) {
    this.droneportIds = droneportIds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DronePortsMappingPostEntityAirwaySectionsInner dronePortsMappingPostEntityAirwaySectionsInner =
        (DronePortsMappingPostEntityAirwaySectionsInner) o;
    return Objects.equals(this.airwaySectionId,
        dronePortsMappingPostEntityAirwaySectionsInner.airwaySectionId)
        && Objects.equals(this.droneportIds,
            dronePortsMappingPostEntityAirwaySectionsInner.droneportIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(airwaySectionId, droneportIds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DronePortsMappingPostEntityAirwaySectionsInner {\n");
    sb.append("    airwaySectionId: ").append(toIndentedString(airwaySectionId)).append("\n");
    sb.append("    droneportIds: ").append(toIndentedString(droneportIds)).append("\n");
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


