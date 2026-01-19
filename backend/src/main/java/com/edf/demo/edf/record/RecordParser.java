package com.edf.demo.edf.record;

import com.edf.demo.edf.signal.Signal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RecordParser {

  public List<Float> parseRecord(byte[] byteArray, Signal signal, float precision) {
    log.debug("Parsing signal {} samples", signal.label());
    var samples = new float[signal.sampleNumber()];

    var bb = ByteBuffer.wrap(byteArray);
    bb.order(ByteOrder.LITTLE_ENDIAN); // EDF is in Little Endian

    for (var i = 0; i < samples.length; i++) {
      samples[i] = convertToPhysical(bb.getShort(), signal);
    }
    return generateSummary(samples, precision);
  }

  private float convertToPhysical(short digitalValue, Signal signal) {
    var gain =
        (signal.physicalMax() - signal.physicalMin()) / (signal.digitalMax() - signal.digitalMin());
    var physicalMin = signal.physicalMin();
    var digitalMin = signal.digitalMin();
    return (digitalValue - digitalMin) * gain + physicalMin;
  }

  /**
   * Based on the precision the data points are handled in chunks/buckets. In each bucket the min
   * and max values are kept
   */
  private List<Float> generateSummary(float[] fullSignal, float precision) {
    var summary = new ArrayList<Float>();

    // Return all sample
    if (precision == 1.0f) {
      for (float sample : fullSignal) {
        summary.add(sample);
      }
      return summary;
    }

    // Calculate total points based on precision
    var totalDesiredPoints = Math.round(fullSignal.length * precision);
    log.debug("Generating summary with target points {}", totalDesiredPoints);

    // Since each bucket gives us 2 points (min and max), we need half as many buckets
    var numBuckets = Math.max(1, totalDesiredPoints / 2);
    var bucketSize = fullSignal.length / numBuckets;

    for (var i = 0; i < fullSignal.length; i += bucketSize) {
      var min = Float.POSITIVE_INFINITY;
      var max = Float.NEGATIVE_INFINITY;

      // Scan the bucket
      for (var j = i; j < i + bucketSize && j < fullSignal.length; j++) {
        min = Math.min(min, fullSignal[j]);
        max = Math.max(max, fullSignal[j]);
      }

      summary.add(min);
      summary.add(max);
    }
    return summary;
  }
}
