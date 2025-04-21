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

import java.util.Objects;
import jakarta.annotation.Generated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * FallToleranceRangeGetRequestEntity
 */

@JsonTypeName("fallToleranceRangeGetRequestEntity")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2025-01-08T04:20:06.579396Z[Etc/UTC]", comments = "Generator version: 7.8.0")
public class FallToleranceRangeGetRequestEntity {

  private String businessNumber;

  private String areaName;

  public FallToleranceRangeGetRequestEntity() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public FallToleranceRangeGetRequestEntity(String businessNumber) {
    this.businessNumber = businessNumber;
  }

  public FallToleranceRangeGetRequestEntity businessNumber(String businessNumber) {
    this.businessNumber = businessNumber;
    return this;
  }

  /**
   * 事業者番号
   * 
   * @return businessNumber
   */
  @NotNull
  @NotEmpty
  @Size(min = 1, max = 200)
  @Schema(name = "businessNumber", description = "事業者番号",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("businessNumber")
  public String getBusinessNumber() {
    return businessNumber;
  }

  public void setBusinessNumber(String businessNumber) {
    this.businessNumber = businessNumber;
  }

  public FallToleranceRangeGetRequestEntity areaName(String areaName) {
    this.areaName = areaName;
    return this;
  }

  /**
   * エリア名称
   * 
   * @return areaName
   */
  @Size(min = 0, max = 200)
  @Schema(name = "areaName", description = "エリア名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("areaName")
  public String getAreaName() {
    return areaName;
  }

  public void setAreaName(String areaName) {
    this.areaName = areaName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FallToleranceRangeGetRequestEntity fallToleranceRangeGetRequestEntity =
        (FallToleranceRangeGetRequestEntity) o;
    return Objects.equals(this.businessNumber, fallToleranceRangeGetRequestEntity.businessNumber)
        && Objects.equals(this.areaName, fallToleranceRangeGetRequestEntity.areaName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(businessNumber, areaName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FallToleranceRangeGetRequestEntity {\n");
    sb.append("    businessNumber: ").append(toIndentedString(businessNumber)).append("\n");
    sb.append("    areaName: ").append(toIndentedString(areaName)).append("\n");
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


