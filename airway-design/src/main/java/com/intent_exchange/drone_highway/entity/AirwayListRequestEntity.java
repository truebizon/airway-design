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
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 航路情報一覧取得API(ポート管理画面からのリクエスト)用のentity
 */

@Schema(name = "AirwayListRequestEntity", description = "航路情報一覧取得API(ポート管理画面からのリクエスト)用のentity")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-12-11T11:58:41.537130600Z[Etc/UTC]", comments = "Generator version: 7.8.0")
public class AirwayListRequestEntity {

  private String point1;

  private String point2;

  private String point3;

  private String point4;

  public AirwayListRequestEntity() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AirwayListRequestEntity(String point1, String point2, String point3, String point4) {
    this.point1 = point1;
    this.point2 = point2;
    this.point3 = point3;
    this.point4 = point4;
  }

  public AirwayListRequestEntity point1(String point1) {
    this.point1 = point1;
    return this;
  }

  /**
   * 地図上の指定範囲の座標１（南西側）
   * 
   * @return point1
   */
  @NotNull
  @NotEmpty
  @Schema(name = "point1", example = "139.55,35.3", description = "地図上の指定範囲の座標１（南西側）",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("point1")
  public String getPoint1() {
    return point1;
  }

  public void setPoint1(String point1) {
    this.point1 = point1;
  }

  public AirwayListRequestEntity point2(String point2) {
    this.point2 = point2;
    return this;
  }

  /**
   * 地図上の指定範囲の座標２（南東側）
   * 
   * @return point2
   */
  @NotNull
  @NotEmpty
  @Schema(name = "point2", example = "139.75,35.3", description = "地図上の指定範囲の座標２（南東側）",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("point2")
  public String getPoint2() {
    return point2;
  }

  public void setPoint2(String point2) {
    this.point2 = point2;
  }

  public AirwayListRequestEntity point3(String point3) {
    this.point3 = point3;
    return this;
  }

  /**
   * 地図上の指定範囲の座標３（北東側）
   * 
   * @return point3
   */
  @NotNull
  @NotEmpty
  @Schema(name = "point3", example = "139.75,34.3", description = "地図上の指定範囲の座標３（北東側）",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("point3")
  public String getPoint3() {
    return point3;
  }

  public void setPoint3(String point3) {
    this.point3 = point3;
  }

  public AirwayListRequestEntity point4(String point4) {
    this.point4 = point4;
    return this;
  }

  /**
   * 地図上の指定範囲の座標４（北西側）
   * 
   * @return point4
   */
  @NotNull
  @NotEmpty
  @Schema(name = "point4", example = "139.55,34.3", description = "地図上の指定範囲の座標４（北西側）",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("point4")
  public String getPoint4() {
    return point4;
  }

  public void setPoint4(String point4) {
    this.point4 = point4;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AirwayListRequestEntity airwayListRequestEntity = (AirwayListRequestEntity) o;
    return Objects.equals(this.point1, airwayListRequestEntity.point1)
        && Objects.equals(this.point2, airwayListRequestEntity.point2)
        && Objects.equals(this.point3, airwayListRequestEntity.point3)
        && Objects.equals(this.point4, airwayListRequestEntity.point4);
  }

  @Override
  public int hashCode() {
    return Objects.hash(point1, point2, point3, point4);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AirwayListRequestEntity {\n");
    sb.append("    point1: ").append(toIndentedString(point1)).append("\n");
    sb.append("    point2: ").append(toIndentedString(point2)).append("\n");
    sb.append("    point3: ").append(toIndentedString(point3)).append("\n");
    sb.append("    point4: ").append(toIndentedString(point4)).append("\n");
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


