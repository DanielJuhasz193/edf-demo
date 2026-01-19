package com.edf.demo.edf.signal;

import lombok.Builder;

@Builder(toBuilder = true)
public record Signal(
    String label,
    String transducerType,
    String physicalDimension,
    float physicalMin,
    float physicalMax,
    float digitalMin,
    float digitalMax,
    String preFiltering,
    int sampleNumber,
    int index,
    int offset,
    int size) {

  public boolean isAnnotation() {
    return this.label.equals("EDF Annotations");
  }
}
