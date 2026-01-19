package com.edf.demo.edf.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class AnnotationParserTest {
  private final AnnotationParser annotationParser = new AnnotationParser();

  @Test
  void testParseAnnotations() {
    var test = "+109\u0014\u0014\u0000+109.978\u001410Hz backround\u0014\u0000";

    var result = annotationParser.parseAnnotations(test.getBytes());

    assertThat(result.size()).isEqualTo(1);
    assertThat(result.getFirst().description()).isEqualTo("10Hz backround");
    assertThat(result.getFirst().onset()).isEqualTo("109.978");
    assertThat(result.getFirst().duration()).isEqualTo("0.0");
  }

  @Test
  void testParseAnnotation_shouldSkip_givenNoAnnotation() {
    var result = annotationParser.parseAnnotations("test".getBytes());

    assertThat(result.size()).isEqualTo(0);
  }

  @Test
  void testParseAnnotation_shouldSkip_givenInvalidFirstChar() {
    var test = "!109\u0014\u0014\u0000+109.978\u001410Hz backround\u0014\u0000";

    var result = annotationParser.parseAnnotations(test.getBytes());

    assertThat(result.size()).isEqualTo(0);
  }
}
