package com.edf.demo.api.mapper;

import com.edf.demo.api.dto.AnnotationResponse;
import com.edf.demo.api.dto.ChannelResponse;
import com.edf.demo.api.dto.EdfResponse;
import com.edf.demo.edf.domain.EdfFile;
import com.edf.demo.edf.plus.Plus;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class EdfResponseMapper {
  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public EdfResponse map(EdfFile edfFile) {
    var channels =
        edfFile.signals().stream()
            .filter(signal -> !signal.isAnnotation())
            .map(
                signal ->
                    ChannelResponse.builder()
                        .name(signal.label())
                        .type(signal.transducerType())
                        .sampleNumber(signal.sampleNumber())
                        .build())
            .sorted(Comparator.comparing(ChannelResponse::name))
            .toList();
    return EdfResponse.builder()
        .fileName(edfFile.fileName())
        .identifier(edfFile.header().recordingId())
        .patientName(
            Optional.ofNullable(edfFile.plus())
                .map(Plus::patientName)
                .orElseGet(() -> edfFile.header().patientId()))
        .recordingDateTime(edfFile.metaData().edfStartTime().format(DATE_TIME_FORMATTER))
        .annotationNumber(edfFile.annotations().size())
        .channelNumber(channels.size())
        .channels(channels)
        .recordingLength(
            String.valueOf(
                ChronoUnit.MILLIS.between(
                        edfFile.metaData().edfStartTime(), edfFile.metaData().edfEndTime())
                    / 1000))
        .annotations(
            edfFile.annotations().stream()
                .map(
                    annotation ->
                        AnnotationResponse.builder()
                            .description(annotation.description())
                            .timeStamp(annotation.onset())
                            .build())
                .toList())
        .build();
  }
}
