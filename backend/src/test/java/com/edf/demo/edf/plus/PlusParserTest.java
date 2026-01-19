package com.edf.demo.edf.plus;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.edf.demo.edf.header.Header;
import org.junit.jupiter.api.Test;

public class PlusParserTest {
  private final PlusParser plusParser = new PlusParser();

  @Test
  void testParsePlus_shouldSkip_givenNonPlusVersion() {
    var header = Header.builder().reserved("").build();
    assertTrue(plusParser.parsePlus(header).isEmpty());
  }

  @Test
  void testParsePlus_shouldSkip_givenWrongPatientData() {
    var header = Header.builder().reserved("EDF+").patientId("X X").build();
    assertTrue(plusParser.parsePlus(header).isEmpty());
  }

  @Test
  void testParsePlus_shouldSkip_givenWrongRecordingData() {
    var header = Header.builder().reserved("EDF+").recordingId("X X").patientId("X X X X").build();
    assertTrue(plusParser.parsePlus(header).isEmpty());
  }
}
