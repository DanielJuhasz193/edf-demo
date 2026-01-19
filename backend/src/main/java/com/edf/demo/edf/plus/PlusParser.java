package com.edf.demo.edf.plus;

import com.edf.demo.edf.header.Header;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PlusParser {

  public Optional<Plus> parsePlus(Header header) {
    if (!header.reserved().contains("EDF+")) {
      return Optional.empty();
    }
    var patientData = header.patientId().split(" ");
    if (patientData.length != 4) {
      log.error("Corrupt patient data for EDF+ spec");
      return Optional.empty();
    }
    var recordingData = header.recordingId().split(" ");
    if (recordingData.length != 5) {
      log.error("Corrupt recording data for EDF+ spec");
      return Optional.empty();
    }
    return Optional.of(
        Plus.builder()
            .patientCode(patientData[0])
            .patientSex(patientData[1])
            .patientBirthDate(patientData[2])
            .patientName(patientData[3])
            .recordingStartDate(recordingData[1])
            .hospitalAdminCode(recordingData[2])
            .technicianCode(recordingData[3])
            .equipmentCode(recordingData[4])
            .build());
  }
}
