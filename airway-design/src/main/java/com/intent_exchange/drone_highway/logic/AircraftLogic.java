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

package com.intent_exchange.drone_highway.logic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.intent_exchange.drone_highway.dto.response.AircraftDto;
import com.intent_exchange.drone_highway.exception.DroneHighwayException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

/**
 * AircraftLogicは、機体情報候補リストのビジネスロジックを処理します。 このロジッククラスは、機体情報候補リストの取得の操作を行います。
 */
@Component
public class AircraftLogic {

  /** CSVのファイル名 */
  private static final String CSV_FILE_NAME = "DroneModelConfig.csv";

  /** CSVファイルが配置されているディレクトリへのパス */
  @Value("${csv.file.path}")
  private String csvFilePath;

  /**
   * 機体情報候補リストを取得します。
   *
   * @return {@code List<AircraftDto>} 機体情報候補リスト
   * @throws DroneHighwayException ビジネスロジックの実行に失敗した場合
   */
  public List<AircraftDto> getAircraft() {
    List<AircraftDto> aircraftList = new ArrayList<>();
    String fullPath = csvFilePath + CSV_FILE_NAME;

    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fullPath)) {
      if (inputStream == null) {
        throw new FileNotFoundException("CSV file not found: " + fullPath);
      }

      try (CSVReader reader =
          new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

        reader.readNext();
        List<String[]> data = reader.readAll();
        for (String[] row : data) {
          AircraftDto aircraft = new AircraftDto();
          aircraft.setAircraftInfoId(Integer.parseInt(row[0]));
          aircraft.setMaker(row[1]);
          aircraft.setModelNumber(row[2]);
          aircraft.setName(row[3]);
          aircraft.setType(row[4]);
          aircraft.setIp(row[5]);
          aircraft.setLength(Integer.parseInt(row[6]));
          aircraft.setWeight(Integer.parseInt(row[7]));
          aircraft.setMaximumTakeoffWeight(Integer.parseInt(row[8]));
          aircraft.setMaximumFlightTime(Integer.parseInt(row[9]));
          aircraft.setDeviationRange(Integer.parseInt(row[10]));
          aircraft.setFallingModel(row[11]);
          aircraftList.add(aircraft);
        }
      }
    } catch (FileNotFoundException e) {
      throw new DroneHighwayException("CSVファイルが見つかりませんでした", e);
    } catch (IOException | CsvException | NumberFormatException
        | ArrayIndexOutOfBoundsException e) {
      throw new DroneHighwayException("機体データの読み込みに失敗しました", e);
    }

    return aircraftList;
  }
}

