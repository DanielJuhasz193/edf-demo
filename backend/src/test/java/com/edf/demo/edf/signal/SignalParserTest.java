package com.edf.demo.edf.signal;

import static org.assertj.core.api.Assertions.assertThat;

import com.edf.demo.edf.header.Header;
import org.junit.jupiter.api.Test;

public class SignalParserTest {
  private final SignalParser signalParser = new SignalParser();

  @Test
  void testReadHeader() {
    var test =
        "EEG Fp1         EEG Fp2         AgCl                                                                 "
            + "           AgCl                                                                            uV  "
            + "    uV      -3000.0 -2000.0 3000.0  2000.0  -32768  -32768  32767   32767   test               "
            + "                                                                                               "
            + "                                              250     50      ";

    var result =
        signalParser.parseSignals(test.getBytes(), Header.builder().signalNumber(2).build());

    assertThat(result.size()).isEqualTo(2);
    assertThat(result.getFirst().label()).isEqualTo("EEG Fp1");
    assertThat(result.getFirst().transducerType()).isEqualTo("AgCl");
    assertThat(result.getFirst().physicalDimension()).isEqualTo("uV");
    assertThat(result.getFirst().physicalMin()).isEqualTo(-3000.0f);
    assertThat(result.getFirst().physicalMax()).isEqualTo(3000.0f);
    assertThat(result.getFirst().digitalMin()).isEqualTo(-32768f);
    assertThat(result.getFirst().digitalMax()).isEqualTo(32767f);
    assertThat(result.getFirst().preFiltering()).isEqualTo("test");
    assertThat(result.getFirst().sampleNumber()).isEqualTo(250);
    assertThat(result.getFirst().offset()).isEqualTo(0);
    assertThat(result.getFirst().index()).isEqualTo(0);
    assertThat(result.getFirst().size()).isEqualTo(500);

    assertThat(result.getLast().label()).isEqualTo("EEG Fp2");
    assertThat(result.getLast().transducerType()).isEqualTo("AgCl");
    assertThat(result.getLast().physicalDimension()).isEqualTo("uV");
    assertThat(result.getLast().physicalMin()).isEqualTo(-2000.0f);
    assertThat(result.getLast().physicalMax()).isEqualTo(2000.0f);
    assertThat(result.getLast().digitalMin()).isEqualTo(-32768f);
    assertThat(result.getLast().digitalMax()).isEqualTo(32767f);
    assertThat(result.getLast().preFiltering()).isEqualTo("");
    assertThat(result.getLast().sampleNumber()).isEqualTo(50);
    assertThat(result.getLast().offset()).isEqualTo(500);
    assertThat(result.getLast().index()).isEqualTo(1);
    assertThat(result.getLast().size()).isEqualTo(100);
  }
}
