package com.edf.demo.service;

import com.edf.demo.service.resource.ResourceProvider;
import java.io.InputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

  private final ResourceProvider resourceProvider;

  public List<String> getFileNames() {
    log.debug("Getting all files");
    return resourceProvider.getFiles();
  }

  public InputStream getAsInputStream(String fileName) {
    log.debug("Getting file {}", fileName);
    return resourceProvider.getFileResource(fileName);
  }

  public boolean fileNotExists(String fileName) {
    log.debug("Checking if file {} exists", fileName);
    return !resourceProvider.fileExists(fileName);
  }
}
