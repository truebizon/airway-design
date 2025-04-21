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

import java.util.Date;
import java.util.Objects;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * FallToleranceRangePostResponseEntity
 */

@JsonTypeName("fallToleranceRangePostResponseEntity")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-12-10T06:19:55.056956500Z[Etc/UTC]", comments = "Generator version: 7.8.0")
public class FallToleranceRangePostResponseEntity {

  private String fallToleranceRangeId;

  private String name;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Date createdAt;

  public FallToleranceRangePostResponseEntity() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public FallToleranceRangePostResponseEntity(String fallToleranceRangeId, Date createdAt) {
    this.fallToleranceRangeId = fallToleranceRangeId;
    this.createdAt = createdAt;
  }

  public FallToleranceRangePostResponseEntity fallToleranceRangeId(String fallToleranceRangeId) {
    this.fallToleranceRangeId = fallToleranceRangeId;
    return this;
  }

  /**
   * 登録した際に発行されたID
   * 
   * @return fallToleranceRangeId
   */
  @NotNull
  @NotEmpty
  @Size(min = 36, max = 36)
  @Schema(name = "fallToleranceRangeId", example = "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX",
      description = "登録した際に発行されたID", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("fallToleranceRangeId")
  public String getFallToleranceRangeId() {
    return fallToleranceRangeId;
  }

  public void setFallToleranceRangeId(String fallToleranceRangeId) {
    this.fallToleranceRangeId = fallToleranceRangeId;
  }

  public FallToleranceRangePostResponseEntity name(String name) {
    this.name = name;
    return this;
  }

  /**
   * 名称
   * 
   * @return name
   */
  @Size(max = 200)
  @Schema(name = "name", description = "名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public FallToleranceRangePostResponseEntity createdAt(Date createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * 
   * @return createdAt
   */
  @NotNull
  @Valid
  @Schema(name = "createdAt", example = "2020-01-31T23:59:59+09:00",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("createdAt")
  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FallToleranceRangePostResponseEntity fallToleranceRangePostResponseEntity =
        (FallToleranceRangePostResponseEntity) o;
    return Objects.equals(this.fallToleranceRangeId,
        fallToleranceRangePostResponseEntity.fallToleranceRangeId)
        && Objects.equals(this.name, fallToleranceRangePostResponseEntity.name)
        && Objects.equals(this.createdAt, fallToleranceRangePostResponseEntity.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fallToleranceRangeId, name, createdAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FallToleranceRangePostResponseEntity {\n");
    sb.append("    fallToleranceRangeId: ")
        .append(toIndentedString(fallToleranceRangeId))
        .append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
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


