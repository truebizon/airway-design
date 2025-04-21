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
import java.math.BigDecimal;
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
 * ジオメトリ geomjson形式のオブジェクト
 */

@Schema(name = "airwaySectionGetResponseEntity_features_inner_geometry", description = "ジオメトリ geomjson形式のオブジェクト")
@JsonTypeName("airwaySectionGetResponseEntity_features_inner_geometry")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-12-05T15:25:28.056268900+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class AirwaySectionGetResponseEntityFeaturesInnerGeometry {

  private String type;

  @Valid
  private List<List<List<BigDecimal>>> coordinates = new ArrayList<>();

  public AirwaySectionGetResponseEntityFeaturesInnerGeometry() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AirwaySectionGetResponseEntityFeaturesInnerGeometry(String type, List<List<List<BigDecimal>>> coordinates) {
    this.type = type;
    this.coordinates = coordinates;
  }

  public AirwaySectionGetResponseEntityFeaturesInnerGeometry type(String type) {
    this.type = type;
    return this;
  }

  /**
   * 種別 固定値「Polygon」を設定
   * @return type
   */
  @NotNull@NotEmpty 
  @Schema(name = "type", example = "Polygon", description = "種別 固定値「Polygon」を設定", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public AirwaySectionGetResponseEntityFeaturesInnerGeometry coordinates(List<List<List<BigDecimal>>> coordinates) {
    this.coordinates = coordinates;
    return this;
  }

  public AirwaySectionGetResponseEntityFeaturesInnerGeometry addCoordinatesItem(List<List<BigDecimal>> coordinatesItem) {
    if (this.coordinates == null) {
      this.coordinates = new ArrayList<>();
    }
    this.coordinates.add(coordinatesItem);
    return this;
  }

  /**
   * 座標 配列内は経度,緯度,高度順で記述
   * @return coordinates
   */
  @NotNull@Valid 
  @Schema(name = "coordinates", description = "座標 配列内は経度,緯度,高度順で記述", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("coordinates")
  public List<List<List<BigDecimal>>> getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(List<List<List<BigDecimal>>> coordinates) {
    this.coordinates = coordinates;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AirwaySectionGetResponseEntityFeaturesInnerGeometry airwaySectionGetResponseEntityFeaturesInnerGeometry = (AirwaySectionGetResponseEntityFeaturesInnerGeometry) o;
    return Objects.equals(this.type, airwaySectionGetResponseEntityFeaturesInnerGeometry.type) &&
        Objects.equals(this.coordinates, airwaySectionGetResponseEntityFeaturesInnerGeometry.coordinates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, coordinates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AirwaySectionGetResponseEntityFeaturesInnerGeometry {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    coordinates: ").append(toIndentedString(coordinates)).append("\n");
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


