package com.edf.demo.edf.signal;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SignalField {
  LABEL(16, (s, builder) -> builder.label(s)),
  TRANSDUCER_TYPE(80, (s, builder) -> builder.transducerType(s)),
  PHYSICAL_DIMENSION(8, (s, builder) -> builder.physicalDimension(s)),
  PHYSICAL_MIN(8, (s, builder) -> builder.physicalMin(Float.parseFloat(s))),
  PHYSICAL_MAX(8, (s, builder) -> builder.physicalMax(Float.parseFloat(s))),
  DIGITAL_MIN(8, (s, builder) -> builder.digitalMin(Float.parseFloat(s))),
  DIGITAL_MAX(8, (s, builder) -> builder.digitalMax(Float.parseFloat(s))),
  PRE_FILTERING(80, (s, builder) -> builder.preFiltering(s)),
  SAMPLE_NUMBER(8, (s, builder) -> builder.sampleNumber(Integer.parseInt(s)));

  private final int length;
  private final BiConsumer<String, Signal.SignalBuilder> setter;

  public static List<SignalField> getOrdered() {
    return Arrays.stream(SignalField.values())
        .sorted(Comparator.comparingInt(Enum::ordinal))
        .toList();
  }
}
