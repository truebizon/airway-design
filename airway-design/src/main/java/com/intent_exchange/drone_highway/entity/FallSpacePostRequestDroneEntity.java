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
 * FallSpacePostRequestDroneEntity
 */

@JsonTypeName("fallSpacePostRequestDroneEntity")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-01-30T04:54:41.388111700Z[Etc/UTC]", comments = "Generator version: 7.8.0")
public class FallSpacePostRequestDroneEntity {

  private Integer aircraftInfoId;

  private String maker;

  private String modelNumber;

  private String name;

  private String type;

  private String ip;

  private Integer length;

  private Integer weight;

  private Integer maximumTakeoffWeight;

  private Integer maximumFlightTime;

  public FallSpacePostRequestDroneEntity() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public FallSpacePostRequestDroneEntity(Integer aircraftInfoId, String maker, String modelNumber, String name, String type, String ip, Integer length, Integer weight, Integer maximumTakeoffWeight, Integer maximumFlightTime) {
    this.aircraftInfoId = aircraftInfoId;
    this.maker = maker;
    this.modelNumber = modelNumber;
    this.name = name;
    this.type = type;
    this.ip = ip;
    this.length = length;
    this.weight = weight;
    this.maximumTakeoffWeight = maximumTakeoffWeight;
    this.maximumFlightTime = maximumFlightTime;
  }

  public FallSpacePostRequestDroneEntity aircraftInfoId(Integer aircraftInfoId) {
    this.aircraftInfoId = aircraftInfoId;
    return this;
  }

  /**
   * 機体情報ID
   * @return aircraftInfoId
   */
  @NotNull
  @Schema(name = "aircraftInfoId", description = "機体情報ID", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("aircraftInfoId")
  public Integer getAircraftInfoId() {
    return aircraftInfoId;
  }

  public void setAircraftInfoId(Integer aircraftInfoId) {
    this.aircraftInfoId = aircraftInfoId;
  }

  public FallSpacePostRequestDroneEntity maker(String maker) {
    this.maker = maker;
    return this;
  }

  /**
   * 製造メーカー名
   * @return maker
   */
  @NotNull@NotEmpty @Size(min = 1, max = 200) 
  @Schema(name = "maker", description = "製造メーカー名", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("maker")
  public String getMaker() {
    return maker;
  }

  public void setMaker(String maker) {
    this.maker = maker;
  }

  public FallSpacePostRequestDroneEntity modelNumber(String modelNumber) {
    this.modelNumber = modelNumber;
    return this;
  }

  /**
   * 型式（モデル）
   * @return modelNumber
   */
  @NotNull@NotEmpty @Size(min = 1, max = 200) 
  @Schema(name = "modelNumber", description = "型式（モデル）", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("modelNumber")
  public String getModelNumber() {
    return modelNumber;
  }

  public void setModelNumber(String modelNumber) {
    this.modelNumber = modelNumber;
  }

  public FallSpacePostRequestDroneEntity name(String name) {
    this.name = name;
    return this;
  }

  /**
   * 機種名
   * @return name
   */
  @NotNull@NotEmpty @Size(min = 1, max = 200) 
  @Schema(name = "name", description = "機種名", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public FallSpacePostRequestDroneEntity type(String type) {
    this.type = type;
    return this;
  }

  /**
   * 機体種別
   * @return type
   */
  @NotNull@NotEmpty @Size(min = 1, max = 100) 
  @Schema(name = "type", description = "機体種別", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public FallSpacePostRequestDroneEntity ip(String ip) {
    this.ip = ip;
    return this;
  }

  /**
   * IP番号
   * @return ip
   */
  @NotNull@NotEmpty @Size(min = 1, max = 100) 
  @Schema(name = "ip", description = "IP番号", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ip")
  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public FallSpacePostRequestDroneEntity length(Integer length) {
    this.length = length;
    return this;
  }

  /**
   * 機体長
   * @return length
   */
  @NotNull
  @Schema(name = "length", description = "機体長", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("length")
  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
  }

  public FallSpacePostRequestDroneEntity weight(Integer weight) {
    this.weight = weight;
    return this;
  }

  /**
   * 重量
   * @return weight
   */
  @NotNull
  @Schema(name = "weight", description = "重量", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("weight")
  public Integer getWeight() {
    return weight;
  }

  public void setWeight(Integer weight) {
    this.weight = weight;
  }

  public FallSpacePostRequestDroneEntity maximumTakeoffWeight(Integer maximumTakeoffWeight) {
    this.maximumTakeoffWeight = maximumTakeoffWeight;
    return this;
  }

  /**
   * 最大離陸重量
   * @return maximumTakeoffWeight
   */
  @NotNull
  @Schema(name = "maximumTakeoffWeight", description = "最大離陸重量", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("maximumTakeoffWeight")
  public Integer getMaximumTakeoffWeight() {
    return maximumTakeoffWeight;
  }

  public void setMaximumTakeoffWeight(Integer maximumTakeoffWeight) {
    this.maximumTakeoffWeight = maximumTakeoffWeight;
  }

  public FallSpacePostRequestDroneEntity maximumFlightTime(Integer maximumFlightTime) {
    this.maximumFlightTime = maximumFlightTime;
    return this;
  }

  /**
   * 最大飛行時間
   * @return maximumFlightTime
   */
  @NotNull
  @Schema(name = "maximumFlightTime", description = "最大飛行時間", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("maximumFlightTime")
  public Integer getMaximumFlightTime() {
    return maximumFlightTime;
  }

  public void setMaximumFlightTime(Integer maximumFlightTime) {
    this.maximumFlightTime = maximumFlightTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FallSpacePostRequestDroneEntity fallSpacePostRequestDroneEntity = (FallSpacePostRequestDroneEntity) o;
    return Objects.equals(this.aircraftInfoId, fallSpacePostRequestDroneEntity.aircraftInfoId) &&
        Objects.equals(this.maker, fallSpacePostRequestDroneEntity.maker) &&
        Objects.equals(this.modelNumber, fallSpacePostRequestDroneEntity.modelNumber) &&
        Objects.equals(this.name, fallSpacePostRequestDroneEntity.name) &&
        Objects.equals(this.type, fallSpacePostRequestDroneEntity.type) &&
        Objects.equals(this.ip, fallSpacePostRequestDroneEntity.ip) &&
        Objects.equals(this.length, fallSpacePostRequestDroneEntity.length) &&
        Objects.equals(this.weight, fallSpacePostRequestDroneEntity.weight) &&
        Objects.equals(this.maximumTakeoffWeight, fallSpacePostRequestDroneEntity.maximumTakeoffWeight) &&
        Objects.equals(this.maximumFlightTime, fallSpacePostRequestDroneEntity.maximumFlightTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(aircraftInfoId, maker, modelNumber, name, type, ip, length, weight, maximumTakeoffWeight, maximumFlightTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FallSpacePostRequestDroneEntity {\n");
    sb.append("    aircraftInfoId: ").append(toIndentedString(aircraftInfoId)).append("\n");
    sb.append("    maker: ").append(toIndentedString(maker)).append("\n");
    sb.append("    modelNumber: ").append(toIndentedString(modelNumber)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    ip: ").append(toIndentedString(ip)).append("\n");
    sb.append("    length: ").append(toIndentedString(length)).append("\n");
    sb.append("    weight: ").append(toIndentedString(weight)).append("\n");
    sb.append("    maximumTakeoffWeight: ").append(toIndentedString(maximumTakeoffWeight)).append("\n");
    sb.append("    maximumFlightTime: ").append(toIndentedString(maximumFlightTime)).append("\n");
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


