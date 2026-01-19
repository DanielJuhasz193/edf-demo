package com.edf.demo.api.dto;

import lombok.Builder;

@Builder
public record AnnotationResponse(String description, String timeStamp) {}
