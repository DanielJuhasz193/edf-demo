package com.edf.demo.api.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TimingFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    if (!log.isDebugEnabled()) {
      chain.doFilter(request, response);
      return;
    }

    var start = System.nanoTime();

    try {
      chain.doFilter(request, response);
    } finally {
      var end = System.nanoTime();
      var durationMs = (end - start) / 1_000_000;
      log.debug("Request took [{}] ms", durationMs);
    }
  }
}
