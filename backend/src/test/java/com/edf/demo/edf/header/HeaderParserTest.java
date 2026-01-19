package com.edf.demo.edf.header;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class HeaderParserTest {
  private final HeaderParser headerParser = new HeaderParser();

  @Test
  void testParseHeader() {
    var test =
        "0       DO0815199 F 06-MAY-2024 Jane_Doe                                                Startdate"
            + " 19-MAR-2021 ZHI27402 Gabor_Braun Zeto_WR-19                           19.03.2108.40.575376   "
            + " EDF+C                                       624     1       20  ";

    var result = headerParser.parseHeader(test.getBytes());

    assertThat(result.version()).isEqualTo("0");
    assertThat(result.patientId()).isEqualTo("DO0815199 F 06-MAY-2024 Jane_Doe");
    assertThat(result.recordingId())
        .isEqualTo("Startdate 19-MAR-2021 ZHI27402 Gabor_Braun Zeto_WR-19");
    assertThat(result.startDate()).isEqualTo("2021-03-19");
    assertThat(result.startTime()).isEqualTo("08:40:57");
    assertThat(result.byteNumber()).isEqualTo(5376);
    assertThat(result.reserved()).isEqualTo("EDF+C");
    assertThat(result.recordNumber()).isEqualTo(624);
    assertThat(result.recordDuration()).isEqualTo(1);
    assertThat(result.signalNumber()).isEqualTo(20);
  }
}
