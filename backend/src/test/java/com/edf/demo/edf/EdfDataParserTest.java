package com.edf.demo.edf;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.edf.demo.edf.domain.EdfFile;
import com.edf.demo.edf.domain.MetaData;
import com.edf.demo.edf.header.Header;
import com.edf.demo.edf.record.RecordParser;
import com.edf.demo.edf.signal.Signal;
import com.edf.demo.exception.EdfBadRequestException;
import com.edf.demo.exception.EdfStateException;
import com.edf.demo.service.resource.ResourceProvider;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EdfDataParserTest {
  @Mock private RecordParser recordParser;
  @Mock private ResourceProvider resourceProvider;
  @InjectMocks private EdfDataParser edfDataParser;

  @ParameterizedTest
  @MethodSource
  void testParseData_shouldThrow_givenInvalidRequest(
      int recordNumber, int fromRecording, int toRecording) {
    assertThrows(
        EdfBadRequestException.class,
        () ->
            edfDataParser.parseData(
                EdfFile.builder()
                    .header(Header.builder().recordNumber(recordNumber).build())
                    .build(),
                fromRecording,
                toRecording,
                "test",
                1.0f));
  }

  @Test
  void testParseData_shouldThrow_givenInconsistentData() {
    when(resourceProvider.getFileResource("testFile"))
        .thenReturn(new ByteArrayInputStream("data".getBytes()));
    assertThrows(
        EdfStateException.class,
        () ->
            edfDataParser.parseData(
                EdfFile.builder()
                    .fileName("testFile")
                    .signals(List.of(Signal.builder().label("test").build()))
                    .header(Header.builder().recordNumber(10).build())
                    .metaData(MetaData.builder().fileSize(1000000L).build())
                    .build(),
                1,
                2,
                "test",
                1.0f));
  }

  private static Stream<Arguments> testParseData_shouldThrow_givenInvalidRequest() {
    return Stream.of(Arguments.of(1, 2, 2), Arguments.of(10, 5, 2), Arguments.of(10, 5, 5));
  }
}
