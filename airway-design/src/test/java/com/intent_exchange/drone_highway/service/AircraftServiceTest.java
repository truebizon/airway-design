package com.intent_exchange.drone_highway.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import com.intent_exchange.drone_highway.dto.response.AircraftDto;
import com.intent_exchange.drone_highway.entity.AircraftEntity;
import com.intent_exchange.drone_highway.entity.AircraftEntityAircraftInner;
import com.intent_exchange.drone_highway.logic.AircraftLogic;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {AircraftServiceTest.Config.class})
public class AircraftServiceTest {

  @InjectMocks
  private AircraftService aircraftService;

  @Mock
  private AircraftLogic aircraftLogic;

  @Configuration
  static class Config {
    @Bean
    protected ModelMapper modelMapper() {
      return new ModelMapper();
    }

    @Bean
    protected ModelMapperUtil modelMapperUtil(ModelMapper modelMapper) {
      return new ModelMapperUtil(modelMapper);
    }
  }

  @Test
  @DisplayName("機体情報の取得メソッドが正常に動作すること")
  public void testAircraftEntityRetrieval() {
    AircraftDto aircraftDto = new AircraftDto();
    aircraftDto.setAircraftInfoId(1);
    aircraftDto.setMaker("メーカー1");
    aircraftDto.setModelNumber("型番1_1");
    aircraftDto.setName("機体1");
    aircraftDto.setType("回転翼航空機（ヘリコプター）");
    aircraftDto.setIp("IP68");
    aircraftDto.setLength(950);
    aircraftDto.setWeight(1000);
    aircraftDto.setMaximumTakeoffWeight(1500);
    aircraftDto.setMaximumFlightTime(30);
    aircraftDto.setDeviationRange(1);
    aircraftDto.setFallingModel("ParachuteModelParameters.csv");

    List<AircraftDto> aircraftList = Arrays.asList(aircraftDto);

    given(aircraftLogic.getAircraft()).willReturn(aircraftList);

    AircraftEntity result = aircraftService.getAircraft();

    List<AircraftEntityAircraftInner> responseEntities = result.getAircraft();
    assertEquals(1, responseEntities.size());
    assertEquals(1, responseEntities.get(0).getAircraftInfoId());
    assertEquals("メーカー1", responseEntities.get(0).getMaker());
    assertEquals("型番1_1", responseEntities.get(0).getModelNumber());
    assertEquals("機体1", responseEntities.get(0).getName());
    assertEquals("回転翼航空機（ヘリコプター）", responseEntities.get(0).getType());
    assertEquals("IP68", responseEntities.get(0).getIp());
    assertEquals(950, responseEntities.get(0).getLength());
    assertEquals(1000, responseEntities.get(0).getWeight());
    assertEquals(1500, responseEntities.get(0).getMaximumTakeoffWeight());
    assertEquals(30, responseEntities.get(0).getMaximumFlightTime());
    assertEquals(1, responseEntities.get(0).getDeviationRange());
    assertEquals("ParachuteModelParameters.csv", responseEntities.get(0).getFallingModel());
  }
}
