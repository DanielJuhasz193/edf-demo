package com.edf.demo.api.dto;

import lombok.Builder;

@Builder
public record ErrorResponse(String message) {}
