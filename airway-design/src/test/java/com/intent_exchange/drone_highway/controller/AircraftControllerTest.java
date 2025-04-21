package com.intent_exchange.drone_highway.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intent_exchange.drone_highway.entity.AircraftEntity;
import com.intent_exchange.drone_highway.entity.AircraftEntityAircraftInner;
import com.intent_exchange.drone_highway.exception.DroneHighwayException;
import com.intent_exchange.drone_highway.service.AircraftService;
import com.intent_exchange.drone_highway.util.PropertyUtil;

@WebMvcTest(controllers = AircraftController.class)
public class AircraftControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AircraftService aircraftService;

  private ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @DisplayName("機体情報候補リストを正常に取得すること")
  public void testGetAircraft() throws Exception {
    AircraftEntityAircraftInner aircraft = new AircraftEntityAircraftInner();
    aircraft.setAircraftInfoId(1);
    aircraft.setMaker("メーカー1");
    aircraft.setModelNumber("型番1_1");
    aircraft.setName("機体1");
    aircraft.setType("回転翼航空機（ヘリコプター）");
    aircraft.setIp("IP68");
    aircraft.setLength(950);
    aircraft.setWeight(3000);
    aircraft.setMaximumTakeoffWeight(4000);
    aircraft.setMaximumFlightTime(30);
    aircraft.setDeviationRange(1);
    aircraft.setFallingModel("ParachuteModelParameters.csv");

    List<AircraftEntityAircraftInner> aircraftList = Arrays.asList(aircraft);

    AircraftEntity AircraftEntity = new AircraftEntity();
    AircraftEntity.setAircraft(aircraftList);

    given(aircraftService.getAircraft()).willReturn(AircraftEntity);

    mockMvc.perform(get("/aircraft").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(AircraftEntity)));

  }

  @Test
  @DisplayName("CSVファイルが見つからない場合に適切なエラーメッセージが返されること")
  public void testGetAircraftFileNotFound() throws Exception {
    given(aircraftService.getAircraft()).willThrow(new DroneHighwayException("CSVファイルが見つかりませんでした"));

    mockMvc.perform(get("/aircraft").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value("500"))
        .andExpect(jsonPath("$.message").value(PropertyUtil.getProperty("500.error.message")))
        .andExpect(jsonPath("$.description").value("CSVファイルが見つかりませんでした"));
  }

  @Test
  @DisplayName("サーバーエラーが発生した場合に適切なエラーメッセージが返されること")
  public void testGetAircraftServerError() throws Exception {
    given(aircraftService.getAircraft()).willThrow(new DroneHighwayException("機体データの読み込みに失敗しました"));

    mockMvc.perform(get("/aircraft").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value(PropertyUtil.getProperty("500.error.message")))
        .andExpect(jsonPath("$.description").value("機体データの読み込みに失敗しました"));
  }
}
