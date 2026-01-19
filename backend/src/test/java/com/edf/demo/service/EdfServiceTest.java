package com.edf.demo.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.edf.demo.edf.EdfDataParser;
import com.edf.demo.edf.EdfParser;
import com.edf.demo.exception.EdfNotFoundException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EdfServiceTest {
  @Mock private EdfParser edfParser;
  @Mock private EdfDataParser edfDataParser;
  @Mock private FileService fileService;
  @InjectMocks private EdfService edfService;

  @Test
  void testPreLoading_shouldSkip_givenException() {
    when(fileService.getFileNames()).thenReturn(List.of("test"));
    doThrow(RuntimeException.class).when(edfParser).parseEdf("test");
    new EdfService(edfParser, edfDataParser, fileService);
  }

  @Test
  void testGetEdfFile_shouldThrow_givenNotFoundFile() {
    var fileName = "test";
    when(fileService.fileNotExists(fileName)).thenReturn(true);
    assertThrows(EdfNotFoundException.class, () -> edfService.getEdfFile(fileName));
  }

  @Test
  void testGetData_shouldThrow_givenNotFoundFile() {
    var fileName = "test";
    when(fileService.fileNotExists(fileName)).thenReturn(true);
    assertThrows(
        EdfNotFoundException.class, () -> edfService.getData(fileName, 1, 1, "test", 1.0f));
  }
}
