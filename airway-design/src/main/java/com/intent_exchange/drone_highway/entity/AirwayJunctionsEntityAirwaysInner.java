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
import com.intent_exchange.drone_highway.entity.AirwayJunctionsEntityAirwaysInnerAirway;
import com.intent_exchange.drone_highway.entity.AirwayJunctionsEntityAirwaysInnerDeviation;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AirwayJunctionsEntityAirwaysInner
 */

@JsonTypeName("airwayJunctionsEntity_airways_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-01-16T10:26:17.028232400+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class AirwayJunctionsEntityAirwaysInner {

  private AirwayJunctionsEntityAirwaysInnerAirway airway;

  private AirwayJunctionsEntityAirwaysInnerDeviation deviation;

  public AirwayJunctionsEntityAirwaysInner airway(AirwayJunctionsEntityAirwaysInnerAirway airway) {
    this.airway = airway;
    return this;
  }

  /**
   * Get airway
   * @return airway
   */
  @Valid 
  @Schema(name = "airway", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("airway")
  public AirwayJunctionsEntityAirwaysInnerAirway getAirway() {
    return airway;
  }

  public void setAirway(AirwayJunctionsEntityAirwaysInnerAirway airway) {
    this.airway = airway;
  }

  public AirwayJunctionsEntityAirwaysInner deviation(AirwayJunctionsEntityAirwaysInnerDeviation deviation) {
    this.deviation = deviation;
    return this;
  }

  /**
   * Get deviation
   * @return deviation
   */
  @Valid 
  @Schema(name = "deviation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deviation")
  public AirwayJunctionsEntityAirwaysInnerDeviation getDeviation() {
    return deviation;
  }

  public void setDeviation(AirwayJunctionsEntityAirwaysInnerDeviation deviation) {
    this.deviation = deviation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AirwayJunctionsEntityAirwaysInner airwayJunctionsEntityAirwaysInner = (AirwayJunctionsEntityAirwaysInner) o;
    return Objects.equals(this.airway, airwayJunctionsEntityAirwaysInner.airway) &&
        Objects.equals(this.deviation, airwayJunctionsEntityAirwaysInner.deviation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(airway, deviation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AirwayJunctionsEntityAirwaysInner {\n");
    sb.append("    airway: ").append(toIndentedString(airway)).append("\n");
    sb.append("    deviation: ").append(toIndentedString(deviation)).append("\n");
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


