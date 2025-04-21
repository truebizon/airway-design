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
 * AirwayConditions
 */

@JsonTypeName("airwayConditions")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-11-27T19:32:38.323068+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class AirwayConditions {

  @Valid
  private List<String> airwayId = new ArrayList<>();

  private Boolean all = false;

  public AirwayConditions airwayId(List<String> airwayId) {
    this.airwayId = airwayId;
    return this;
  }

  public AirwayConditions addAirwayIdItem(String airwayIdItem) {
    if (this.airwayId == null) {
      this.airwayId = new ArrayList<>();
    }
    this.airwayId.add(airwayIdItem);
    return this;
  }

  /**
   * Get airwayId
   * @return airwayId
   */
  
  @Schema(name = "airwayId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("airwayId")
  public List<String> getAirwayId() {
    return airwayId;
  }

  public void setAirwayId(List<String> airwayId) {
    this.airwayId = airwayId;
  }

  public AirwayConditions all(Boolean all) {
    this.all = all;
    return this;
  }

  /**
   * Get all
   * @return all
   */
  
  @Schema(name = "all", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("all")
  public Boolean getAll() {
    return all;
  }

  public void setAll(Boolean all) {
    this.all = all;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AirwayConditions airwayConditions = (AirwayConditions) o;
    return Objects.equals(this.airwayId, airwayConditions.airwayId) &&
        Objects.equals(this.all, airwayConditions.all);
  }

  @Override
  public int hashCode() {
    return Objects.hash(airwayId, all);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AirwayConditions {\n");
    sb.append("    airwayId: ").append(toIndentedString(airwayId)).append("\n");
    sb.append("    all: ").append(toIndentedString(all)).append("\n");
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


