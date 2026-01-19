package com.edf.demo.api;

import com.edf.demo.api.dto.EdfResponse;
import com.edf.demo.api.mapper.EdfResponseMapper;
import com.edf.demo.service.EdfService;
import com.edf.demo.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/edf")
@Slf4j
@Validated
public class EdfApi {

  private final EdfService edfService;
  private final FileService fileService;
  private final EdfResponseMapper edfResponseMapper;

  @Operation(description = "List all the file names, that the application can access and load")
  @GetMapping("/file-names")
  public List<String> getFiles() {
    log.debug("Handling GET /file-names");
    return fileService.getFileNames();
  }

  @Operation(description = "Load a single file and get the basic information about its content")
  @GetMapping("/files")
  public EdfResponse loadFile(
      @Parameter(description = "Name of the file, URL encoded format if applicable")
          @RequestParam
          @NotBlank(message = "fileName must be provided")
          String fileName) {
    log.debug("Handling GET /files fileName={}", fileName);
    return edfResponseMapper.map(edfService.getEdfFile(fileName));
  }

  @Operation(
      description =
"""
Experimental feature. Load the recording data points for a signal.
The endpoint works with recording numbers instead of timestamps, so it is up to the caller to calculate it.
Each recording has a set duration. So if a file has 20 recording, and a duration of 1 second for each one,
it means the file has 20 s of recording,
where fromRecording=10 and toRecording=12 will fetch the samples from 10-11, 10 s to 12 s (2 seconds worth of data).
In addition a precision can be provided, up to 1.0, which means 100% precision and returns all data points.
For smaller values aggregation will be performed and fewer data points will be in the response.
""")
  @GetMapping("/data")
  public List<Float> loadData(
      @Parameter(description = "Name of the file, URL encoded if applicable")
          @RequestParam
          @NotBlank(message = "fileName must be provided")
          String fileName,
      @Parameter(description = "Starting point of the load (inclusive)")
          @RequestParam
          @Min(value = 0, message = "fromRecording must be positive")
          int fromRecording,
      @Parameter(description = "End point of the load (exclusive)")
          @RequestParam
          @Min(value = 0, message = "toRecording must be positive")
          int toRecording,
      @Parameter(description = "Name of the signal, for which data will be loaded")
          @RequestParam
          @NotBlank(message = "signal must be provided")
          String signal,
      @RequestParam(defaultValue = "0.1", required = false)
          @DecimalMin(value = "0.01", message = "precision min value is 0.01")
          @DecimalMax(value = "1.0", message = "precision max value is 1.0")
          BigDecimal precision) {
    log.debug(
        "Handling GET /load fileName={} fromRecording={} toRecording={} signal={} precision={}",
        fileName,
        fromRecording,
        toRecording,
        signal,
        precision);
    return edfService.getData(fileName, fromRecording, toRecording, signal, precision.floatValue());
  }
}
