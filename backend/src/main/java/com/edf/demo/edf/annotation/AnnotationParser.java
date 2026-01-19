package com.edf.demo.edf.annotation;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class AnnotationParser {

  public List<Annotation> parseAnnotations(byte[] annotationBuffer) {
    log.debug("Parsing annotations");

    if (annotationBuffer[0] != 43 && annotationBuffer[0] != 45) {
      log.debug("Annotation has invalid start character");
      return Collections.emptyList();
    }
    var result = new ArrayList<Annotation>();
    var line = new String(annotationBuffer, StandardCharsets.UTF_8).trim();

    var annotationList = line.split("(?=\\+)");

    for (var tal : annotationList) {
      if (tal.isBlank()) continue;

      // Format: +[Onset] \u0015 [Duration] \u0014 [Text] \u0014
      var parts = tal.split("\u0014");

      if (parts.length >= 2) {
        var timeSection = parts[0]; // Onset and Duration
        var text = parts[1]; // Annotation label

        if (!StringUtils.hasLength(text)) {
          continue;
        }

        // Handle Duration if 0x15 (decimal 21) is present
        double onset;
        double duration = 0;

        if (timeSection.contains("\u0015")) {
          var timeParts = timeSection.split("\u0015");
          onset = Double.parseDouble(timeParts[0]);
          duration = Double.parseDouble(timeParts[1]);
        } else {
          onset = Double.parseDouble(timeSection);
        }

        result.add(
            Annotation.builder()
                .description(text)
                .onset(String.valueOf(onset))
                .duration(String.valueOf(duration))
                .build());
      }
    }
    return result;
  }
}
