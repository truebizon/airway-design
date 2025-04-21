package com.intent_exchange.drone_highway.controller;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intent_exchange.drone_highway.entity.FallToleranceRangeDeleteRequestEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangeDetailGetRequestEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangeDetailGetResponseEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangeGetResponseEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangeGetResponseItemEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangeGetResponseItemEntityGeometry;
import com.intent_exchange.drone_highway.entity.FallToleranceRangePostRequestEntity;
import com.intent_exchange.drone_highway.entity.FallToleranceRangePostRequestEntityGeometry;
import com.intent_exchange.drone_highway.entity.FallToleranceRangePostResponseEntity;
import com.intent_exchange.drone_highway.exception.DataInUseException;
import com.intent_exchange.drone_highway.exception.DataNotFoundException;
import com.intent_exchange.drone_highway.service.FallToleranceRangeService;

@WebMvcTest(controllers = FallToleranceRangeController.class)
public class FallToleranceRangeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FallToleranceRangeService fallToleranceRangeService;

  @Autowired
  private ObjectMapper beanObjectMapper;

  @Test
  @DisplayName("最大落下許容範囲取得")
  public void fallToleranceRangeGet() throws Exception {
    FallToleranceRangeGetResponseItemEntityGeometry fallToleranceRangeGetResponseItemEntityGeometry =
        new FallToleranceRangeGetResponseItemEntityGeometry();
    fallToleranceRangeGetResponseItemEntityGeometry.setType("type");
    fallToleranceRangeGetResponseItemEntityGeometry
        .setCoordinates(Arrays.asList(Arrays.asList(Arrays.asList(BigDecimal.ONE))));
    FallToleranceRangeGetResponseItemEntity fallToleranceRangeGetResponseItemEntity =
        new FallToleranceRangeGetResponseItemEntity();
    fallToleranceRangeGetResponseItemEntity.setFallToleranceRangeId("fallToleranceRangeId");
    fallToleranceRangeGetResponseItemEntity.setName("name");
    fallToleranceRangeGetResponseItemEntity.setAreaName("areaName");
    fallToleranceRangeGetResponseItemEntity.setAirwayOperatorId("airwayOperatorId");
    fallToleranceRangeGetResponseItemEntity.setAirwayIdUse(Arrays.asList("airwayIdUse"));
    fallToleranceRangeGetResponseItemEntity
        .setGeometry(fallToleranceRangeGetResponseItemEntityGeometry);
    fallToleranceRangeGetResponseItemEntity.setCreatedAt(new Date());
    fallToleranceRangeGetResponseItemEntity.setUpdatedAt(new Date());
    FallToleranceRangeGetResponseEntity fallToleranceRangeGetResponseEntity =
        new FallToleranceRangeGetResponseEntity();
    fallToleranceRangeGetResponseEntity
        .setFallToleranceRanges(Arrays.asList(fallToleranceRangeGetResponseItemEntity));

    given(fallToleranceRangeService.fallToleranceRangeGet(Mockito.any()))
        .willReturn(fallToleranceRangeGetResponseEntity);

    mockMvc
        .perform(get("/fall-tolerance-range").param("businessNumber", "1234567890")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content()
            .json(beanObjectMapper.writeValueAsString(fallToleranceRangeGetResponseEntity)));
  }

  @Test
  @DisplayName("最大落下許容範囲登録")
  public void fallToleranceRangePost() throws Exception {
    FallToleranceRangePostRequestEntityGeometry fallToleranceRangePostRequestEntityGeometry =
        new FallToleranceRangePostRequestEntityGeometry();
    fallToleranceRangePostRequestEntityGeometry.setType("type");
    fallToleranceRangePostRequestEntityGeometry
        .setCoordinates(Arrays.asList(Arrays.asList(Arrays.asList(BigDecimal.ONE))));
    FallToleranceRangePostRequestEntity fallToleranceRangePostRequestEntity =
        new FallToleranceRangePostRequestEntity();
    fallToleranceRangePostRequestEntity.setBusinessNumber("businessNumber");
    fallToleranceRangePostRequestEntity.setAirwayOperatorId("airwayOperatorId");
    fallToleranceRangePostRequestEntity.setName("name");
    fallToleranceRangePostRequestEntity.setAreaName("areaName");
    fallToleranceRangePostRequestEntity.setElevationTerrain("elevationTerrain");
    fallToleranceRangePostRequestEntity.setGeometry(fallToleranceRangePostRequestEntityGeometry);
    FallToleranceRangePostResponseEntity fallToleranceRangePostResponseEntity =
        new FallToleranceRangePostResponseEntity();
    fallToleranceRangePostResponseEntity.setFallToleranceRangeId("fallToleranceRangeId");
    fallToleranceRangePostResponseEntity.setCreatedAt(new Date());
    fallToleranceRangePostResponseEntity.setName(fallToleranceRangePostRequestEntity.getName());
    given(fallToleranceRangeService.fallToleranceRangePost(fallToleranceRangePostRequestEntity))
        .willReturn(fallToleranceRangePostResponseEntity);
    mockMvc
        .perform(post("/fall-tolerance-range").contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(fallToleranceRangePostRequestEntity)))
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("最大落下許容範囲削除")
  public void fallToleranceRangeDelete() throws Exception {
    FallToleranceRangeDeleteRequestEntity fallToleranceRangeDeleteRequestEntity =
        getFallToleranceRangeDeleteRequestEntity();
    doNothing().when(fallToleranceRangeService)
        .fallToleranceRangeDelete(fallToleranceRangeDeleteRequestEntity);
    mockMvc
        .perform(delete("/fall-tolerance-range")
            .param("fallToleranceRangeId",
                fallToleranceRangeDeleteRequestEntity.getFallToleranceRangeId())
            .param("businessNumber", fallToleranceRangeDeleteRequestEntity.getBusinessNumber()))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("最大落下許容範囲削除 404")
  public void fallToleranceRangeDelete404() throws Exception {
    FallToleranceRangeDeleteRequestEntity fallToleranceRangeDeleteRequestEntity =
        getFallToleranceRangeDeleteRequestEntity();
    doThrow(new DataNotFoundException()).when(fallToleranceRangeService)
        .fallToleranceRangeDelete(fallToleranceRangeDeleteRequestEntity);
    mockMvc
        .perform(delete("/fall-tolerance-range")
            .param("fallToleranceRangeId",
                fallToleranceRangeDeleteRequestEntity.getFallToleranceRangeId())
            .param("businessNumber", fallToleranceRangeDeleteRequestEntity.getBusinessNumber()))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("最大落下許容範囲削除 409")
  public void fallToleranceRangeDelete409() throws Exception {
    FallToleranceRangeDeleteRequestEntity fallToleranceRangeDeleteRequestEntity =
        getFallToleranceRangeDeleteRequestEntity();
    doThrow(new DataInUseException()).when(fallToleranceRangeService)
        .fallToleranceRangeDelete(fallToleranceRangeDeleteRequestEntity);
    mockMvc
        .perform(delete("/fall-tolerance-range")
            .param("fallToleranceRangeId",
                fallToleranceRangeDeleteRequestEntity.getFallToleranceRangeId())
            .param("businessNumber", fallToleranceRangeDeleteRequestEntity.getBusinessNumber()))
        .andExpect(status().isConflict());

  }

  private FallToleranceRangeDeleteRequestEntity getFallToleranceRangeDeleteRequestEntity() {
    FallToleranceRangeDeleteRequestEntity result = new FallToleranceRangeDeleteRequestEntity();
    result.setFallToleranceRangeId("123456789012345678901234567890123456");
    result.setBusinessNumber("businessNumber");
    return result;
  }

  @Test
  @DisplayName("最大落下許容範囲情報の取得")
  public void fallToleranceRangeDetailGet() throws Exception {
    FallToleranceRangeDetailGetRequestEntity fallToleranceRangeDetailGetRequestEntity =
        getFallToleranceRangeDetailGetRequestEntity();
    FallToleranceRangePostRequestEntityGeometry fallToleranceRangePostRequestEntityGeometry =
        new FallToleranceRangePostRequestEntityGeometry();
    fallToleranceRangePostRequestEntityGeometry.setType("type");
    fallToleranceRangePostRequestEntityGeometry
        .setCoordinates(Arrays.asList(Arrays.asList(Arrays.asList(BigDecimal.ONE))));
    FallToleranceRangeDetailGetResponseEntity fallToleranceRangeDetailGetResponseEntity =
        new FallToleranceRangeDetailGetResponseEntity();
    fallToleranceRangeDetailGetResponseEntity.setFallToleranceRangeId(
        fallToleranceRangeDetailGetRequestEntity.getFallToleranceRangeId());
    fallToleranceRangeDetailGetResponseEntity
        .setBusinessNumber(fallToleranceRangeDetailGetRequestEntity.getBusinessNumber());
    fallToleranceRangeDetailGetResponseEntity.setAirwayOperatorId("airwayOperatorId");
    fallToleranceRangeDetailGetResponseEntity.setName("name");
    fallToleranceRangeDetailGetResponseEntity.setAreaName("areaName");
    fallToleranceRangeDetailGetResponseEntity.setElevationTerrain("elevationTerrain");
    fallToleranceRangeDetailGetResponseEntity
        .setGeometry(fallToleranceRangePostRequestEntityGeometry);
    fallToleranceRangeDetailGetResponseEntity.setCreatedAt(new Date());
    fallToleranceRangeDetailGetResponseEntity.setUpdatedAt(new Date());
    given(fallToleranceRangeService
        .fallToleranceRangeDetailGet(fallToleranceRangeDetailGetRequestEntity))
            .willReturn(fallToleranceRangeDetailGetResponseEntity);
    mockMvc
        .perform(get("/fall-tolerance-range/"
            + fallToleranceRangeDetailGetRequestEntity.getFallToleranceRangeId() + "/"
            + fallToleranceRangeDetailGetRequestEntity.getBusinessNumber()))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("最大落下許容範囲情報の取得404")
  public void fallToleranceRangeDetailGet404() throws Exception {
    FallToleranceRangeDetailGetRequestEntity fallToleranceRangeDetailGetRequestEntity =
        getFallToleranceRangeDetailGetRequestEntity();
    doThrow(new DataNotFoundException()).when(fallToleranceRangeService)
        .fallToleranceRangeDetailGet(fallToleranceRangeDetailGetRequestEntity);
    mockMvc
        .perform(get("/fall-tolerance-range/"
            + fallToleranceRangeDetailGetRequestEntity.getFallToleranceRangeId() + "/"
            + fallToleranceRangeDetailGetRequestEntity.getBusinessNumber()))
        .andExpect(status().isNotFound());
  }

  private FallToleranceRangeDetailGetRequestEntity getFallToleranceRangeDetailGetRequestEntity() {
    FallToleranceRangeDetailGetRequestEntity fallToleranceRangeDetailGetRequestEntity =
        new FallToleranceRangeDetailGetRequestEntity();
    fallToleranceRangeDetailGetRequestEntity
        .setFallToleranceRangeId("123456789012345678901234567890123456");
    fallToleranceRangeDetailGetRequestEntity.setBusinessNumber("businessNumber");
    return fallToleranceRangeDetailGetRequestEntity;
  }

}
