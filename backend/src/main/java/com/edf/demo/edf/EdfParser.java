package com.edf.demo.edf;

import com.edf.demo.edf.annotation.Annotation;
import com.edf.demo.edf.annotation.AnnotationParser;
import com.edf.demo.edf.domain.EdfFile;
import com.edf.demo.edf.domain.MetaData;
import com.edf.demo.edf.header.Header;
import com.edf.demo.edf.header.HeaderParser;
import com.edf.demo.edf.plus.PlusParser;
import com.edf.demo.edf.signal.Signal;
import com.edf.demo.edf.signal.SignalParser;
import com.edf.demo.exception.EdfGeneralException;
import com.edf.demo.service.FileService;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EdfParser {
  private static final int BASE_HEADER_LENGTH = 256;

  private final FileService fileService;
  private final HeaderParser headerParser;
  private final SignalParser signalParser;
  private final AnnotationParser annotationParser;
  private final PlusParser plusParser;

  public EdfFile parseEdf(String fileName) {
    log.debug("Reading edf file {}", fileName);

    try (var is = fileService.getAsInputStream(fileName)) {
      var bb = ByteBuffer.wrap(is.readAllBytes());

      var headerBuffer = new byte[BASE_HEADER_LENGTH];
      bb.get(headerBuffer);
      var header = headerParser.parseHeader(headerBuffer);

      var signalsBuffer = new byte[header.signalNumber() * BASE_HEADER_LENGTH];
      bb.get(signalsBuffer);
      var signals = signalParser.parseSignals(signalsBuffer, header);

      var metaData = getMetaData(header, signals);

      var annotations = new ArrayList<Annotation>();
      if (metaData.hasAnnotation()) {
        var annotationBuffer = new byte[metaData.annotationSize()];
        for (var i = 0; i < header.recordNumber(); i++) {
          bb.get(
              metaData.recordStartIndex() + i * metaData.recordSize() + metaData.annotationOffset(),
              annotationBuffer);
          annotations.addAll(annotationParser.parseAnnotations(annotationBuffer));
        }
      }

      return EdfFile.builder()
          .fileName(fileName)
          .header(header)
          .annotations(annotations)
          .signals(signals)
          .metaData(metaData)
          .plus(plusParser.parsePlus(header).orElse(null))
          .build();
    } catch (IOException e) {
      log.error("Failed to read EDF file {}", fileName, e);
      throw new EdfGeneralException(e);
    }
  }

  /**
   * Returns EDF file metadata that helps further calculations without the need of header
   * information The recording has 2 byte short values, hence the multiplications with 2 inside
   */
  private MetaData getMetaData(Header header, List<Signal> signals) {
    log.debug("Calculating file metadata");
    var annotationSignal = signals.stream().filter(Signal::isAnnotation).findAny();
    var recordSize =
        signals.stream().map(signal -> signal.sampleNumber() * 2).reduce(0, Integer::sum);
    var recordingSize = (long) recordSize * header.recordNumber();
    var headerSize = BASE_HEADER_LENGTH + BASE_HEADER_LENGTH * header.signalNumber();
    var startTime = getStartTime(header);
    return MetaData.builder()
        .recordStartIndex(headerSize)
        .recordSize(recordSize)
        .hasAnnotation(annotationSignal.isPresent())
        .annotationSize(annotationSignal.map(signal -> signal.sampleNumber() * 2).orElse(0))
        .edfSize(recordingSize)
        .annotationOffset(annotationSignal.map(Signal::offset).orElse(0))
        .fileSize(recordingSize + headerSize)
        .edfStartTime(startTime)
        .edfEndTime(calculateEndTime(startTime, header))
        .build();
  }

  private LocalDateTime getStartTime(Header header) {
    var date = header.startDate().split("-");
    var time = header.startTime().split(":");

    return LocalDateTime.of(
        Integer.parseInt(date[0]),
        Integer.parseInt(date[1]),
        Integer.parseInt(date[2]),
        Integer.parseInt(time[0]),
        Integer.parseInt(time[1]),
        Integer.parseInt(time[2]));
  }

  private LocalDateTime calculateEndTime(LocalDateTime startTime, Header header) {
    return startTime.plus(
        Math.round(header.recordDuration() * header.recordNumber() * 1000), ChronoUnit.MILLIS);
  }
}
