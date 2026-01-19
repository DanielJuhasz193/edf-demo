package com.edf.demo.edf.domain;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record MetaData(
    long edfSize,
    LocalDateTime edfStartTime,
    LocalDateTime edfEndTime,
    boolean hasAnnotation,
    int annotationSize,
    int recordStartIndex,
    int recordSize,
    int annotationOffset,
    long fileSize) {}
