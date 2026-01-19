package com.edf.demo.edf;

import com.edf.demo.edf.domain.EdfFile;
import com.edf.demo.edf.record.RecordParser;
import com.edf.demo.exception.EdfBadRequestException;
import com.edf.demo.exception.EdfGeneralException;
import com.edf.demo.exception.EdfStateException;
import com.edf.demo.service.resource.ResourceProvider;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EdfDataParser {

  private final RecordParser recordParser;
  private final ResourceProvider resourceProvider;

  public List<Float> parseData(
      EdfFile edfFile, int fromRecording, int toRecording, String signalName, float precision) {
    log.debug("Parsing EDF data for file {} and signal {}", edfFile.fileName(), signalName);
    validateRequest(edfFile, fromRecording, toRecording);

    var signal =
        edfFile.signals().stream()
            .filter(sig -> sig.label().equals(signalName))
            .findAny()
            .orElseThrow();

    try (var inputStream = resourceProvider.getFileResource(edfFile.fileName())) {

      var bb = ByteBuffer.wrap(inputStream.readAllBytes());

      if (bb.capacity() != edfFile.metaData().fileSize()) {
        throw new EdfStateException("File content does not match file definition");
      }

      var result = new ArrayList<Float>();
      var signalBuffer = new byte[signal.size()];

      for (var i = fromRecording; i < toRecording; i++) {
        log.debug("Reading recording no. {} data", i);
        bb.position(
            edfFile.metaData().recordStartIndex()
                + fromRecording * edfFile.metaData().recordSize()
                + signal.offset());
        bb.get(signalBuffer);

        result.addAll(recordParser.parseRecord(signalBuffer, signal, precision));
      }
      return result;
    } catch (IOException e) {
      throw new EdfGeneralException(e);
    }
  }

  private void validateRequest(EdfFile edfFile, int fromRecording, int toRecording) {
    log.debug("Validating data load request");
    if (toRecording > edfFile.header().recordNumber()) {
      throw new EdfBadRequestException("toRecording is larger then available recordings");
    }
    if (fromRecording >= toRecording) {
      throw new EdfBadRequestException("fromRecording is bigger then toRecording");
    }
  }
}
