package com.edf.demo.edf.signal;

import com.edf.demo.edf.header.Header;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SignalParser {

  public List<Signal> parseSignals(byte[] raw, Header header) {
    log.debug("Parsing signals");

    var rawBuffer = ByteBuffer.wrap(raw);
    var signalBuilders =
        IntStream.range(0, header.signalNumber()).mapToObj(_ -> Signal.builder()).toList();

    for (var signalField : SignalField.getOrdered()) {
      var buffer = new byte[signalField.getLength()];

      for (var i = 0; i < header.signalNumber(); i++) {
        rawBuffer.get(buffer);

        var s = new String(buffer, StandardCharsets.US_ASCII).trim();

        signalField.getSetter().accept(s, signalBuilders.get(i));
      }
    }

    var signals = signalBuilders.stream().map(Signal.SignalBuilder::build).toList();

    // Adding some metadata for easier data parsing later
    var result = new ArrayList<Signal>();
    var offset = 0;
    for (var i = 0; i < signalBuilders.size(); i++) {
      var signal = signals.get(i);
      result.add(
          signal.toBuilder().index(i).offset(offset).size(signal.sampleNumber() * 2).build());
      offset += signal.sampleNumber() * 2;
    }
    return result;
  }
}
