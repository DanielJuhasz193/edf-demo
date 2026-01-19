package com.edf.demo.edf.annotation;

import lombok.Builder;

@Builder
public record Annotation(String description, String onset, String duration) {}
