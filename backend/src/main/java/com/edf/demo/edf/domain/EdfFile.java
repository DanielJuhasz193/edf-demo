package com.edf.demo.edf.domain;

import com.edf.demo.edf.annotation.Annotation;
import com.edf.demo.edf.header.Header;
import com.edf.demo.edf.plus.Plus;
import com.edf.demo.edf.signal.Signal;
import java.util.List;
import lombok.Builder;

@Builder(toBuilder = true)
public record EdfFile(
    String fileName,
    Header header,
    List<Signal> signals,
    List<Annotation> annotations,
    MetaData metaData,
    Plus plus,
    List<String> errors,
    List<String> warnings) {}
