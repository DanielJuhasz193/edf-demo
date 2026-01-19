package com.edf.demo.edf.header;

import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HeaderParser {

  public Header parseHeader(byte[] header) {
    log.debug("Parsing header");
    var headerBuilder = Header.builder();
    HeaderField.getOrdered()
        .forEach(
            headerField ->
                headerField.getSetter().accept(getField(headerField, header), headerBuilder));
    return headerBuilder.build();
  }

  private String getField(HeaderField headerField, byte[] header) {
    return new String(
            header, headerField.getStart(), headerField.getLength(), StandardCharsets.US_ASCII)
        .trim();
  }
}
