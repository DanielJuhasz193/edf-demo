package com.edf.demo.edf.plus;

import lombok.Builder;

@Builder
public record Plus(
    String patientCode,
    String patientSex,
    String patientBirthDate,
    String patientName,
    String recordingStartDate,
    String hospitalAdminCode,
    String technicianCode,
    String equipmentCode) {

  @Override
  public String patientCode() {
    return getOrAnonymous(patientCode);
  }

  @Override
  public String patientSex() {
    return getOrAnonymous(patientSex);
  }

  @Override
  public String patientBirthDate() {
    return getOrAnonymous(patientBirthDate);
  }

  @Override
  public String patientName() {
    return getOrAnonymous(patientName);
  }

  @Override
  public String recordingStartDate() {
    return getOrAnonymous(recordingStartDate);
  }

  @Override
  public String hospitalAdminCode() {
    return getOrAnonymous(hospitalAdminCode);
  }

  @Override
  public String technicianCode() {
    return getOrAnonymous(technicianCode);
  }

  @Override
  public String equipmentCode() {
    return getOrAnonymous(equipmentCode);
  }

  private String getOrAnonymous(String s) {
    return s.equals("X") ? "Anonymous" : s.replace('_', ' ');
  }
}
