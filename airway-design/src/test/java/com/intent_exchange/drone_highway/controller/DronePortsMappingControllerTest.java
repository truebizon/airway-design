package com.intent_exchange.drone_highway.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.intent_exchange.drone_highway.exception.DataNotFoundException;
import com.intent_exchange.drone_highway.service.DronePortsMappingService;

@WebMvcTest(DronePortsMappingController.class)
public class DronePortsMappingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private DronePortsMappingService dronePortsMappingService;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
  }

  // 正常系パターン
  @Test
  @DisplayName("全項目が設定されている場合、ステータスコード201が返却されること")
  public void testDronePortsMappingPost_success1() throws Exception {
    doNothing().when(dronePortsMappingService).dronePortsMappingPost(Mockito.any());

    mockMvc
        .perform(post("/droneports-mapping").contentType(MediaType.APPLICATION_JSON)
            .content("{\"airwayId\":\"airway-TestId\","
                + "\"airwaySections\":[{\"airwaySectionId\":\"section-TestId\","
                + "\"droneportIds\":[\"dronePortTestId1\",\"dronePortTestId2\"]}]}"))
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("パラメータ「droneportIds」が空の場合、ステータスコード201が返却されること")
  public void testDronePortsMappingPost_succes2() throws Exception {
    // droneportIdsを空に設定
    mockMvc
        .perform(post("/droneports-mapping").contentType(MediaType.APPLICATION_JSON)
            .content("{\"airwayId\":\"airway-TestId\","
                + "\"airwaySections\":[{\"airwaySectionId\":\"section-TestId\","
                + "\"droneportIds\":[\"dronePortTestId1\",\"dronePortTestId2\"]}]}"))
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("パラメータ「droneportIds」がnullの場合、ステータスコード201が返却されること")
  public void testDronePortsMappingPost_succes3() throws Exception {
    doNothing().when(dronePortsMappingService).dronePortsMappingPost(Mockito.any());
    // droneportIdsをnullに設定
    mockMvc
        .perform(post("/droneports-mapping").contentType(MediaType.APPLICATION_JSON)
            .content("{\"airwayId\":\"airway-TestId\","
                + "\"airwaySections\":[{\"airwaySectionId\":\"section-TestId\","
                + "\"droneportIds\":[\"dronePortTestId1\",\"dronePortTestId2\"]}]}"))
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("パラメータ「droneportIds」が指定されていない場合、ステータスコード201が返却されること")
  public void testDronePortsMappingPost_succes4() throws Exception {
    doNothing().when(dronePortsMappingService).dronePortsMappingPost(Mockito.any());
    // droneportIds自体が存在しない
    mockMvc
        .perform(post("/droneports-mapping").contentType(MediaType.APPLICATION_JSON)
            .content("{\"airwayId\":\"airway-TestId\","
                + "\"airwaySections\":[{\"airwaySectionId\":\"section-TestId\"}]}"))
        .andExpect(status().isCreated());
  }

  // 準正常系パターン
  @Test
  @DisplayName("必須パラメータ「航路ID」が空の場合、ステータスコード400が返却されること")
  public void testDronePortsMappingPost_badRequest1() throws Exception {
    // airwayIdを空に設定
    mockMvc
        .perform(post("/droneports-mapping").contentType(MediaType.APPLICATION_JSON)
            .content("{\"airwayId\":\"\","
                + "\"airwaySections\":[{\"airwaySectionId\":\"section-TestId\","
                + "\"droneportIds\":[\"dronePortTestId1\",\"dronePortTestId2\"]}]}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("必須パラメータ「航路ID」がnullの場合、ステータスコード400が返却されること")
  public void testDronePortsMappingPost_badRequest2() throws Exception {
    // airwayIdをnullに設定
    mockMvc
        .perform(post("/droneports-mapping").contentType(MediaType.APPLICATION_JSON)
            .content("{\"airwayId\":null,"
                + "\"airwaySections\":[{\"airwaySectionId\":\"section-TestId\","
                + "\"droneportIds\":[\"dronePortTestId1\",\"dronePortTestId2\"]}]}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("必須パラメータ「航路ID」が指定されていない場合、ステータスコード400が返却されること")
  public void testDronePortsMappingPost_badRequest3() throws Exception {
    // airwayId自体が存在しない
    mockMvc
        .perform(post("/droneports-mapping").contentType(MediaType.APPLICATION_JSON)
            .content("{\"airwaySections\":[{\"airwaySectionId\":\"section-TestId\","
                + "\"droneportIds\":[\"dronePortTestId1\",\"dronePortTestId2\"]}]}"))
        .andExpect(status().isBadRequest());
  }


  @Test
  @DisplayName("必須パラメータ「ドローンポートマッピング情報」が空の場合、ステータスコード400が返却されること")
  public void testDronePortsMappingPost_badRequest4() throws Exception {
    // airwaySectionsを空に設定
    mockMvc
        .perform(post("/droneports-mapping").contentType(MediaType.APPLICATION_JSON)
            .content("{\"airwayId\":\"airway-TestId\"," + "\"airwaySections\":[]}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("必須パラメータ「ドローンポートマッピング情報」がnullの場合、ステータスコード400が返却されること")
  public void testDronePortsMappingPost_badRequest5() throws Exception {
    // airwaySectionsをnullに設定
    mockMvc
        .perform(post("/droneports-mapping").contentType(MediaType.APPLICATION_JSON)
            .content("{\"airwayId\":\"airway-TestId\"," + "\"airwaySections\":null}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("必須パラメータ「ドローンポートマッピング情報」が指定されていない場合、ステータスコード400が返却されること")
  public void testDronePortsMappingPost_badRequest6() throws Exception {
    // airwaySections自体が存在しない
    mockMvc.perform(post("/droneports-mapping").contentType(MediaType.APPLICATION_JSON)
        .content("{\"airwayId\":\"airway-TestId\"}")).andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("必須パラメータ「航路区画ID」が空の場合、ステータスコード400が返却されること")
  public void testDronePortsMappingPost_badRequest7() throws Exception {
    // airwaySectionIdを空に設定
    mockMvc
        .perform(post("/droneports-mapping").contentType(MediaType.APPLICATION_JSON)
            .content("{\"airwayId\":\"airway-TestId\","
                + "\"airwaySections\":[{\"airwaySectionId\":\"\","
                + "\"droneportIds\":[\"dronePortTestId1\",\"dronePortTestId2\"]}]}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("必須パラメータ「航路区画ID」がnullの場合、ステータスコード400が返却されること")
  public void testDronePortsMappingPost_badRequest8() throws Exception {
    // airwaySectionIdをnullに設定
    mockMvc
        .perform(post("/droneports-mapping").contentType(MediaType.APPLICATION_JSON)
            .content("{\"airwayId\":\"airway-TestId\","
                + "\"airwaySections\":[{\"airwaySectionId\":null,"
                + "\"droneportIds\":[\"dronePortTestId1\",\"dronePortTestId2\"]}]}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("必須パラメータ「航路区画ID」が指定されていない場合、ステータスコード400が返却されること")
  public void testDronePortsMappingPost_badRequest9() throws Exception {
    // airwaySectionId自体が存在しない
    mockMvc
        .perform(post("/droneports-mapping").contentType(MediaType.APPLICATION_JSON)
            .content("{\"airwayId\":\"airway-TestId\"," + "\"airwaySections\":[{"
                + "\"droneportIds\":[\"dronePortTestId1\",\"dronePortTestId2\"]}]}"))
        .andExpect(status().isBadRequest());
  }


  @Test
  @DisplayName("存在チェック、整合性チェックがNGの場合、ステータスコード404が返却されること")
  public void testDronePortsMappingPost_notFound() throws Exception {
    doThrow(new DataNotFoundException()).when(dronePortsMappingService)
        .dronePortsMappingPost(Mockito.any());

    mockMvc
        .perform(post("/droneports-mapping").contentType(MediaType.APPLICATION_JSON)
            .content("{\"airwayId\":\"airway-TestId\","
                + "\"airwaySections\":[{\"airwaySectionId\":\"section-TestId\","
                + "\"droneportIds\":[\"dronePortTestId1\",\"dronePortTestId2\"]}]}"))
        .andExpect(status().isNotFound());
  }

  // 異常系テストパターン
  @Test
  @DisplayName("サーバー内部エラーが発生した場合、ステータスコードが500が返却されること")
  public void testDronePortsMappingPost_internalServerError() throws Exception {
    // サーバー内部エラーをシミュレート
    doThrow(new RuntimeException("Internal Server Error")).when(dronePortsMappingService)
        .dronePortsMappingPost(Mockito.any());

    mockMvc
        .perform(post("/droneports-mapping").contentType(MediaType.APPLICATION_JSON)
            .content("{\"airwayId\":\"airway-TestId\","
                + "\"airwaySections\":[{\"airwaySectionId\":\"section-TestId\","
                + "\"droneportIds\":[\"dronePortTestId1\",\"dronePortTestId2\"]}]}"))
        .andExpect(status().isInternalServerError());
  }
}
