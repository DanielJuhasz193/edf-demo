package com.edf.demo.edf.header;

import lombok.Builder;

@Builder
public record Header(
    String version,
    String patientId,
    String recordingId,
    String startDate,
    String startTime,
    int byteNumber,
    String reserved,
    int recordNumber,
    float recordDuration,
    int signalNumber) {}
