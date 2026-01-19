package com.edf.demo.service;

import com.edf.demo.edf.EdfDataParser;
import com.edf.demo.edf.EdfParser;
import com.edf.demo.edf.domain.EdfFile;
import com.edf.demo.exception.EdfNotFoundException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EdfService {
  // In memory cache
  private static final ConcurrentHashMap<String, EdfFile> EDF_STORE = new ConcurrentHashMap<>();

  private final EdfParser edfParser;
  private final EdfDataParser edfDataParser;
  private final FileService fileService;

  public EdfService(EdfParser edfParser, EdfDataParser edfDataParser, FileService fileService) {
    this.edfParser = edfParser;
    this.edfDataParser = edfDataParser;
    this.fileService = fileService;

    log.debug("Pre-loading EDF file cache");
    for (var fileName : fileService.getFileNames()) {
      try {
        EDF_STORE.put(fileName, edfParser.parseEdf(fileName));
      } catch (Exception e) {
        log.warn("Something is wrong with {}, skipping it from pre-loading", fileName);
      }
    }
  }

  public EdfFile getEdfFile(String fileName) {
    if (fileService.fileNotExists(fileName)) {
      throw new EdfNotFoundException();
    }
    return EDF_STORE.computeIfAbsent(fileName, edfParser::parseEdf);
  }

  public List<Float> getData(
      String fileName, int fromRecording, int toRecording, String signal, float precision) {
    if (fileService.fileNotExists(fileName)) {
      throw new EdfNotFoundException();
    }
    var edfFile = EDF_STORE.computeIfAbsent(fileName, edfParser::parseEdf);
    return edfDataParser.parseData(edfFile, fromRecording, toRecording, signal, precision);
  }
}
