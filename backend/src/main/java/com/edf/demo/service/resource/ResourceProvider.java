package com.edf.demo.service.resource;

import java.io.InputStream;
import java.util.List;

public interface ResourceProvider {

  List<String> getFiles();

  InputStream getFileResource(String fileName);

  boolean fileExists(String fileName);
}
