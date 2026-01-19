package com.edf.demo.service.resource;

import com.edf.demo.exception.EdfNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

@Component
public class FileResourceProvider implements ResourceProvider {
  private static final String BASE_PATH = "/edf-files";

  @Override
  public List<String> getFiles() {
    try {
      var res = new PathMatchingResourcePatternResolver();
      return Arrays.stream(res.getResources(BASE_PATH + "/*.edf"))
          .map(Resource::getFilename)
          .toList();
    } catch (IOException e) {
      throw new EdfNotFoundException();
    }
  }

  @Override
  public InputStream getFileResource(String fileName) {
    var is = getClass().getResourceAsStream(BASE_PATH + "/" + fileName);
    if (is == null) {
      throw new EdfNotFoundException();
    }
    return is;
  }

  @Override
  public boolean fileExists(String fileName) {
    return getClass().getResource(BASE_PATH + "/" + fileName) != null;
  }
}
