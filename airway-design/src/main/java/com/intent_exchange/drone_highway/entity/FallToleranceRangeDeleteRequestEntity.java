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
 * FallToleranceRangeDeleteRequestEntity
 */

@JsonTypeName("fallToleranceRangeDeleteRequestEntity")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-12-25T05:08:08.155172600Z[Etc/UTC]", comments = "Generator version: 7.8.0")
public class FallToleranceRangeDeleteRequestEntity {

  private String fallToleranceRangeId;

  private String businessNumber;

  public FallToleranceRangeDeleteRequestEntity() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public FallToleranceRangeDeleteRequestEntity(String fallToleranceRangeId, String businessNumber) {
    this.fallToleranceRangeId = fallToleranceRangeId;
    this.businessNumber = businessNumber;
  }

  public FallToleranceRangeDeleteRequestEntity fallToleranceRangeId(String fallToleranceRangeId) {
    this.fallToleranceRangeId = fallToleranceRangeId;
    return this;
  }

  /**
   * 最大落下許容範囲ID
   * @return fallToleranceRangeId
   */
  @NotNull@NotEmpty @Size(min = 36, max = 36) 
  @Schema(name = "fallToleranceRangeId", description = "最大落下許容範囲ID", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("fallToleranceRangeId")
  public String getFallToleranceRangeId() {
    return fallToleranceRangeId;
  }

  public void setFallToleranceRangeId(String fallToleranceRangeId) {
    this.fallToleranceRangeId = fallToleranceRangeId;
  }

  public FallToleranceRangeDeleteRequestEntity businessNumber(String businessNumber) {
    this.businessNumber = businessNumber;
    return this;
  }

  /**
   * 事業者番号
   * @return businessNumber
   */
  @NotNull@NotEmpty @Size(min = 1, max = 200) 
  @Schema(name = "businessNumber", description = "事業者番号", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("businessNumber")
  public String getBusinessNumber() {
    return businessNumber;
  }

  public void setBusinessNumber(String businessNumber) {
    this.businessNumber = businessNumber;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FallToleranceRangeDeleteRequestEntity fallToleranceRangeDeleteRequestEntity = (FallToleranceRangeDeleteRequestEntity) o;
    return Objects.equals(this.fallToleranceRangeId, fallToleranceRangeDeleteRequestEntity.fallToleranceRangeId) &&
        Objects.equals(this.businessNumber, fallToleranceRangeDeleteRequestEntity.businessNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fallToleranceRangeId, businessNumber);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FallToleranceRangeDeleteRequestEntity {\n");
    sb.append("    fallToleranceRangeId: ").append(toIndentedString(fallToleranceRangeId)).append("\n");
    sb.append("    businessNumber: ").append(toIndentedString(businessNumber)).append("\n");
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


