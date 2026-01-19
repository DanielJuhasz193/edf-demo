package com.edf.demo.edf.record;

import static org.assertj.core.api.Assertions.assertThat;

import com.edf.demo.edf.signal.Signal;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class RecordParserTest {

  private final RecordParser recordParser = new RecordParser();

  @ParameterizedTest
  @CsvSource({"1.0,100", "0.5,50", "0.25,26", "0.1,10"})
  void testParseRecord(float precision, int expected) {
    var a =
        new byte[] {
          1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2,
          1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2,
          1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2,
          1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2,
          1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2,
          1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2,
          1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2,
        };
    var signal =
        Signal.builder()
            .sampleNumber(100)
            .digitalMin(0.0f)
            .physicalMin(0.0f)
            .physicalMax(1)
            .digitalMax(1)
            .build();
    var result = recordParser.parseRecord(a, signal, precision);
    assertThat(result.size()).isEqualTo(expected);
  }
}
