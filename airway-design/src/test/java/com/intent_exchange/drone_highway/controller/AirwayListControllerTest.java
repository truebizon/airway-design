package com.intent_exchange.drone_highway.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intent_exchange.drone_highway.entity.AirwayEntity;
import com.intent_exchange.drone_highway.service.AirwayListService;

@WebMvcTest(AirwayListController.class)
public class AirwayListControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AirwayListService airwayListService;

  @InjectMocks
  private AirwayListController airwayListController;

  @Autowired
  private ObjectMapper objectMapper;

  private AirwayEntity dummyAirwayEntity;

  @BeforeEach
  public void setUp() {
    dummyAirwayEntity = new AirwayEntity();
    // フィールドの初期化
    // e.g., dummyAirwayEntity.setCompanyName("Test Company");
    // e.g., dummyAirwayEntity.setDeployName("Test Department");
    // e.g., dummyAirwayEntity.setEmployeeName("Test Employee");
  }

  @Test
  @DisplayName("パラメータに問題ないとき")
  public void testAirwayListGet() throws Exception {
    when(airwayListService.airwayListGet(any())).thenReturn(dummyAirwayEntity);

    MvcResult result = mockMvc
        .perform(get("/airway-list").param("point1", "125.75,33.45")
            .param("point2", "127.55,33.45")
            .param("point3", "127.55,36.45")
            .param("point4", "125.75,36.45")
            .contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andReturn();

    String jsonResponse = result.getResponse().getContentAsString();
    AirwayEntity responseEntity = objectMapper.readValue(jsonResponse, AirwayEntity.class);

    assertThat(responseEntity).usingRecursiveComparison().isEqualTo(dummyAirwayEntity);
  }

  @Test
  @DisplayName("パラメータを欠損させてリクエスト")
  public void testAirwayListGet_MissingParameter_ShouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/airway-list").param("point1", "125.75,33.45")
        .param("point2", "127.55,33.45")
        .param("point3", "127.55,36.45")
        .contentType("application/json")).andExpect(status().isBadRequest()); // HTTP 400を期待
  }

  @Test
  @DisplayName("意図しないエラーがおきたとき")
  public void testAirwayListGet_InternalServerError() throws Exception {
    when(airwayListService.airwayListGet(any()))
        .thenThrow(new RuntimeException("Unexpected Error"));

    mockMvc.perform(get("/airway-list").param("point1", "125.75,33.45")
        .param("point2", "127.55,33.45")
        .param("point3", "127.55,36.45")
        .param("point4", "125.75,36.45")
        .contentType("application/json")).andExpect(status().isInternalServerError()); // HTTP
                                                                                       // 500を期待
  }
}
