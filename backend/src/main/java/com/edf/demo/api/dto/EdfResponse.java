package com.edf.demo.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
public record EdfResponse(
    String fileName,
    @Schema(description = "Unique identifier of the recording") String identifier,
    @Schema(description = "Start of the recording", example = "2026-01-20 00:00:00")
        String recordingDateTime,
    @Schema(description = "Name of the patient. The value is Anonymous if not found")
        String patientName,
    @Schema(description = "Number of the channels in the recording (excluding annotations)")
        int channelNumber,
    @Schema(description = "Length of the recording in seconds") String recordingLength,
    int annotationNumber,
    @Schema(description = "Details of the channels in the recording (excluding annotations)")
        List<ChannelResponse> channels,
    @Schema(description = "All the annotation that are in the recording")
        List<AnnotationResponse> annotations) {}
