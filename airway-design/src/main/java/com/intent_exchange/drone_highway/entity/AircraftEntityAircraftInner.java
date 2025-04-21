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
 * AircraftEntityAircraftInner
 */

@JsonTypeName("AircraftEntity_aircraft_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-01-30T02:40:48.403692100Z[Etc/UTC]", comments = "Generator version: 7.8.0")
public class AircraftEntityAircraftInner {

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

  private Integer deviationRange;

  private String fallingModel;

  public AircraftEntityAircraftInner aircraftInfoId(Integer aircraftInfoId) {
    this.aircraftInfoId = aircraftInfoId;
    return this;
  }

  /**
   * 機体情報ID
   * @return aircraftInfoId
   */
  
  @Schema(name = "aircraftInfoId", description = "機体情報ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aircraft_info_id")
  public Integer getAircraftInfoId() {
    return aircraftInfoId;
  }

  public void setAircraftInfoId(Integer aircraftInfoId) {
    this.aircraftInfoId = aircraftInfoId;
  }

  public AircraftEntityAircraftInner maker(String maker) {
    this.maker = maker;
    return this;
  }

  /**
   * 製造メーカー名
   * @return maker
   */
  
  @Schema(name = "maker", example = "XXXXX", description = "製造メーカー名", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maker")
  public String getMaker() {
    return maker;
  }

  public void setMaker(String maker) {
    this.maker = maker;
  }

  public AircraftEntityAircraftInner modelNumber(String modelNumber) {
    this.modelNumber = modelNumber;
    return this;
  }

  /**
   * 型式（モデル）
   * @return modelNumber
   */
  
  @Schema(name = "modelNumber", example = "XXXXX", description = "型式（モデル）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("model_number")
  public String getModelNumber() {
    return modelNumber;
  }

  public void setModelNumber(String modelNumber) {
    this.modelNumber = modelNumber;
  }

  public AircraftEntityAircraftInner name(String name) {
    this.name = name;
    return this;
  }

  /**
   * 機種名
   * @return name
   */
  
  @Schema(name = "name", example = "機体1", description = "機種名", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public AircraftEntityAircraftInner type(String type) {
    this.type = type;
    return this;
  }

  /**
   * 機体種別
   * @return type
   */
  
  @Schema(name = "type", example = "回転翼航空機（ヘリコプター）", description = "機体種別", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public AircraftEntityAircraftInner ip(String ip) {
    this.ip = ip;
    return this;
  }

  /**
   * IP番号（防水・防塵性能を表すIPコード）
   * @return ip
   */
  
  @Schema(name = "ip", example = "IP68", description = "IP番号（防水・防塵性能を表すIPコード）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ip")
  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public AircraftEntityAircraftInner length(Integer length) {
    this.length = length;
    return this;
  }

  /**
   * 機体長
   * @return length
   */
  
  @Schema(name = "length", example = "950", description = "機体長", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("length")
  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
  }

  public AircraftEntityAircraftInner weight(Integer weight) {
    this.weight = weight;
    return this;
  }

  /**
   * 重量
   * @return weight
   */
  
  @Schema(name = "weight", example = "3000", description = "重量", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weight")
  public Integer getWeight() {
    return weight;
  }

  public void setWeight(Integer weight) {
    this.weight = weight;
  }

  public AircraftEntityAircraftInner maximumTakeoffWeight(Integer maximumTakeoffWeight) {
    this.maximumTakeoffWeight = maximumTakeoffWeight;
    return this;
  }

  /**
   * 最大離陸重量
   * @return maximumTakeoffWeight
   */
  
  @Schema(name = "maximumTakeoffWeight", example = "4000", description = "最大離陸重量", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maximum_takeoff_weight")
  public Integer getMaximumTakeoffWeight() {
    return maximumTakeoffWeight;
  }

  public void setMaximumTakeoffWeight(Integer maximumTakeoffWeight) {
    this.maximumTakeoffWeight = maximumTakeoffWeight;
  }

  public AircraftEntityAircraftInner maximumFlightTime(Integer maximumFlightTime) {
    this.maximumFlightTime = maximumFlightTime;
    return this;
  }

  /**
   * 最大飛行時間
   * @return maximumFlightTime
   */
  
  @Schema(name = "maximumFlightTime", example = "30", description = "最大飛行時間", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maximum_flight_time")
  public Integer getMaximumFlightTime() {
    return maximumFlightTime;
  }

  public void setMaximumFlightTime(Integer maximumFlightTime) {
    this.maximumFlightTime = maximumFlightTime;
  }

  public AircraftEntityAircraftInner deviationRange(Integer deviationRange) {
    this.deviationRange = deviationRange;
    return this;
  }

  /**
   * 逸脱範囲
   * @return deviationRange
   */
  
  @Schema(name = "deviationRange", example = "1", description = "逸脱範囲", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deviation_range")
  public Integer getDeviationRange() {
    return deviationRange;
  }

  public void setDeviationRange(Integer deviationRange) {
    this.deviationRange = deviationRange;
  }

  public AircraftEntityAircraftInner fallingModel(String fallingModel) {
    this.fallingModel = fallingModel;
    return this;
  }

  /**
   * 落下モデル
   * @return fallingModel
   */
  
  @Schema(name = "fallingModel", example = "ParachuteModelParameters.csv", description = "落下モデル", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("falling_model")
  public String getFallingModel() {
    return fallingModel;
  }

  public void setFallingModel(String fallingModel) {
    this.fallingModel = fallingModel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AircraftEntityAircraftInner aircraftEntityAircraftInner = (AircraftEntityAircraftInner) o;
    return Objects.equals(this.aircraftInfoId, aircraftEntityAircraftInner.aircraftInfoId) &&
        Objects.equals(this.maker, aircraftEntityAircraftInner.maker) &&
        Objects.equals(this.modelNumber, aircraftEntityAircraftInner.modelNumber) &&
        Objects.equals(this.name, aircraftEntityAircraftInner.name) &&
        Objects.equals(this.type, aircraftEntityAircraftInner.type) &&
        Objects.equals(this.ip, aircraftEntityAircraftInner.ip) &&
        Objects.equals(this.length, aircraftEntityAircraftInner.length) &&
        Objects.equals(this.weight, aircraftEntityAircraftInner.weight) &&
        Objects.equals(this.maximumTakeoffWeight, aircraftEntityAircraftInner.maximumTakeoffWeight) &&
        Objects.equals(this.maximumFlightTime, aircraftEntityAircraftInner.maximumFlightTime) &&
        Objects.equals(this.deviationRange, aircraftEntityAircraftInner.deviationRange) &&
        Objects.equals(this.fallingModel, aircraftEntityAircraftInner.fallingModel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(aircraftInfoId, maker, modelNumber, name, type, ip, length, weight, maximumTakeoffWeight, maximumFlightTime, deviationRange, fallingModel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AircraftEntityAircraftInner {\n");
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
    sb.append("    deviationRange: ").append(toIndentedString(deviationRange)).append("\n");
    sb.append("    fallingModel: ").append(toIndentedString(fallingModel)).append("\n");
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


