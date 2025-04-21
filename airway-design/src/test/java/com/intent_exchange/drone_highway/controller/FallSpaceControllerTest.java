package com.intent_exchange.drone_highway.controller;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.math.BigDecimal;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intent_exchange.drone_highway.entity.FallSpaceCrossSectionPostRequestEntity;
import com.intent_exchange.drone_highway.entity.FallSpaceCrossSectionPostRequestEntityGeometry;
import com.intent_exchange.drone_highway.entity.FallSpaceCrossSectionPostResponseEntity;
import com.intent_exchange.drone_highway.entity.FallSpacePostRequestDroneEntity;
import com.intent_exchange.drone_highway.entity.FallSpacePostRequestEntity;
import com.intent_exchange.drone_highway.entity.FallSpacePostResponseEntity;
import com.intent_exchange.drone_highway.exception.DataNotFoundException;
import com.intent_exchange.drone_highway.service.FallSpaceService;

@WebMvcTest(controllers = FallSpaceController.class)
public class FallSpaceControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FallSpaceService fallSpaceService;

  @Test
  @DisplayName("落下空間取得")
  public void fallSpacePost() throws Exception {
    FallSpacePostRequestEntity fallSpacePostRequestEntity = getFallSpacePostRequestEntity();
    FallSpacePostResponseEntity fallSpacePostResponseEntity = new FallSpacePostResponseEntity();
    fallSpacePostResponseEntity.setAirwayDeterminationId(1);
    given(fallSpaceService.fallSpacePost(fallSpacePostRequestEntity))
        .willReturn(fallSpacePostResponseEntity);
    mockMvc
        .perform(post("/fall-space").contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(fallSpacePostRequestEntity)))
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("落下空間取得404")
  public void fallSpacePost404() throws Exception {
    FallSpacePostRequestEntity fallSpacePostRequestEntity = getFallSpacePostRequestEntity();
    doThrow(new DataNotFoundException()).when(fallSpaceService)
        .fallSpacePost(fallSpacePostRequestEntity);
    mockMvc
        .perform(post("/fall-space").contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(fallSpacePostRequestEntity)))
        .andExpect(status().isNotFound());
  }

  private FallSpacePostRequestEntity getFallSpacePostRequestEntity() {
    FallSpacePostRequestDroneEntity fallSpacePostRequestDroneEntity =
        new FallSpacePostRequestDroneEntity();
    fallSpacePostRequestDroneEntity.setAircraftInfoId(1);
    fallSpacePostRequestDroneEntity.setMaker("maker");
    fallSpacePostRequestDroneEntity.setModelNumber("modelNumber");
    fallSpacePostRequestDroneEntity.setName("name");
    fallSpacePostRequestDroneEntity.setType("type");
    fallSpacePostRequestDroneEntity.setIp("ip");
    fallSpacePostRequestDroneEntity.setLength(1);
    fallSpacePostRequestDroneEntity.setWeight(1);
    fallSpacePostRequestDroneEntity.setMaximumTakeoffWeight(1);
    fallSpacePostRequestDroneEntity.setMaximumFlightTime(1);
    FallSpacePostRequestEntity result = new FallSpacePostRequestEntity();
    result.setFallToleranceRangeId("123456789012345678901234567890123456");
    result.setNumCrossSectionDivisions(100);
    result.setDroneList(Arrays.asList(fallSpacePostRequestDroneEntity));
    return result;
  }

  @Test
  @DisplayName("落下空間(断面)取得")
  public void fallSpaceCrossSectionPost() throws Exception {
    FallSpaceCrossSectionPostRequestEntity fallSpaceCrossSectionPostRequestEntity =
        getFallSpaceCrossSectionPostRequestEntity();
    FallSpaceCrossSectionPostResponseEntity fallSpaceCrossSectionPostResponseEntity =
        new FallSpaceCrossSectionPostResponseEntity();
    given(fallSpaceService.fallSpaceCrossSectionPost(fallSpaceCrossSectionPostRequestEntity))
        .willReturn(fallSpaceCrossSectionPostResponseEntity);
    mockMvc
        .perform(post("/fall-space-cross-section").contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(fallSpaceCrossSectionPostRequestEntity)))
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("落下空間(断面)取得404")
  public void fallSpaceCrossSectionPost404() throws Exception {
    FallSpaceCrossSectionPostRequestEntity fallSpaceCrossSectionPostRequestEntity =
        getFallSpaceCrossSectionPostRequestEntity();
    doThrow(new DataNotFoundException()).when(fallSpaceService)
        .fallSpaceCrossSectionPost(fallSpaceCrossSectionPostRequestEntity);
    mockMvc
        .perform(post("/fall-space-cross-section").contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(fallSpaceCrossSectionPostRequestEntity)))
        .andExpect(status().isNotFound());
  }

  private FallSpaceCrossSectionPostRequestEntity getFallSpaceCrossSectionPostRequestEntity() {
    FallSpaceCrossSectionPostRequestEntityGeometry fallSpaceCrossSectionPostRequestEntityGeometry =
        new FallSpaceCrossSectionPostRequestEntityGeometry();
    fallSpaceCrossSectionPostRequestEntityGeometry.setType("type");
    fallSpaceCrossSectionPostRequestEntityGeometry
        .setCoordinates(Arrays.asList(Arrays.asList(BigDecimal.ONE)));
    FallSpaceCrossSectionPostRequestEntity result = new FallSpaceCrossSectionPostRequestEntity();
    result.setAirwayDeterminationId(1);
    result.setGeometry(fallSpaceCrossSectionPostRequestEntityGeometry);
    return result;
  }

}
