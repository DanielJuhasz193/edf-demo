package com.edf.demo.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record ChannelResponse(
    String name,
    @Schema(description = "Transducer type used", example = "AgCl") String type,
    @Schema(description = "Number of samples in each recording for this channel")
        int sampleNumber) {}
