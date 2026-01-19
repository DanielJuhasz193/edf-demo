package com.edf.demo.edf.header;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum HeaderField {
  VERSION(0, 8, (s, builder) -> builder.version(s)),
  PATIENT_ID(8, 80, (s, builder) -> builder.patientId(s)),
  RECORDING_ID(88, 80, (s, builder) -> builder.recordingId(s)),
  START_DATE(
      168,
      8,
      (s, builder) -> {
        var date = s.split("\\.");
        var year = Integer.parseInt(date[2]) < 85 ? "20" + date[2] : "19" + date[2];
        builder.startDate("%s-%s-%s".formatted(year, date[1], date[0]));
      }),
  START_TIME(176, 8, (s, builder) -> builder.startTime(s.replace(".", ":"))),
  BYTE_NUMBER(184, 8, (s, builder) -> builder.byteNumber(Integer.parseInt(s))),
  RESERVED(192, 44, (s, builder) -> builder.reserved(s)),
  RECORD_NUMBER(236, 8, (s, builder) -> builder.recordNumber(Integer.parseInt(s))),
  RECORD_DURATION(244, 8, (s, builder) -> builder.recordDuration(Float.parseFloat(s))),
  SIGNAL_NUMBER(252, 4, (s, builder) -> builder.signalNumber(Integer.parseInt(s)));

  private final int start;
  private final int length;
  private final BiConsumer<String, Header.HeaderBuilder> setter;

  public static List<HeaderField> getOrdered() {
    return Arrays.stream(HeaderField.values())
        .sorted(Comparator.comparingInt(Enum::ordinal))
        .toList();
  }
}
