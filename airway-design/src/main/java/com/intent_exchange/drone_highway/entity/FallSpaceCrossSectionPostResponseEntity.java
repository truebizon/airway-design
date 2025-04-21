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
 * FallSpaceCrossSectionPostResponseEntity
 */

@JsonTypeName("fallSpaceCrossSectionPostResponseEntity")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-12-16T04:57:50.352694600Z[Etc/UTC]", comments = "Generator version: 7.8.0")
public class FallSpaceCrossSectionPostResponseEntity {

  private Integer fallSpaceCrossSectionId;

  @Valid
  private List<List<BigDecimal>> data = new ArrayList<>();

  public FallSpaceCrossSectionPostResponseEntity() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public FallSpaceCrossSectionPostResponseEntity(List<List<BigDecimal>> data) {
    this.data = data;
  }

  public FallSpaceCrossSectionPostResponseEntity fallSpaceCrossSectionId(Integer fallSpaceCrossSectionId) {
    this.fallSpaceCrossSectionId = fallSpaceCrossSectionId;
    return this;
  }

  /**
   * 落下空間(断面)ID
   * @return fallSpaceCrossSectionId
   */
  
  @Schema(name = "fallSpaceCrossSectionId", description = "落下空間(断面)ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fallSpaceCrossSectionId")
  public Integer getFallSpaceCrossSectionId() {
    return fallSpaceCrossSectionId;
  }

  public void setFallSpaceCrossSectionId(Integer fallSpaceCrossSectionId) {
    this.fallSpaceCrossSectionId = fallSpaceCrossSectionId;
  }

  public FallSpaceCrossSectionPostResponseEntity data(List<List<BigDecimal>> data) {
    this.data = data;
    return this;
  }

  public FallSpaceCrossSectionPostResponseEntity addDataItem(List<BigDecimal> dataItem) {
    if (this.data == null) {
      this.data = new ArrayList<>();
    }
    this.data.add(dataItem);
    return this;
  }

  /**
   * データ
   * @return data
   */
  @NotNull@Valid 
  @Schema(name = "data", example = "[[139.84354370902838,35.66367882233067,100],[139.84453992240128,35.66366930498516,150],[139.84458094577673,35.66367406608012,200],[139.84530177283676,35.663659778755544,100]]", description = "データ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("data")
  public List<List<BigDecimal>> getData() {
    return data;
  }

  public void setData(List<List<BigDecimal>> data) {
    this.data = data;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FallSpaceCrossSectionPostResponseEntity fallSpaceCrossSectionPostResponseEntity = (FallSpaceCrossSectionPostResponseEntity) o;
    return Objects.equals(this.fallSpaceCrossSectionId, fallSpaceCrossSectionPostResponseEntity.fallSpaceCrossSectionId) &&
        Objects.equals(this.data, fallSpaceCrossSectionPostResponseEntity.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fallSpaceCrossSectionId, data);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FallSpaceCrossSectionPostResponseEntity {\n");
    sb.append("    fallSpaceCrossSectionId: ").append(toIndentedString(fallSpaceCrossSectionId)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
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


