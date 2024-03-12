/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.distros;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DistroConfig {

  static final String OTEL_LATEST =
      "https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar";

  static final String ADOT_1_27_0 =
      "https://github.com/aws-observability/aws-otel-java-instrumentation/releases/download/v1.27.0/aws-opentelemetry-agent.jar";

  public static final DistroConfig NONE = new DistroConfig("none", "no agent at all");
  public static final DistroConfig LATEST_RELEASE =
      new DistroConfig("latest", "latest mainstream release", OTEL_LATEST);
  public static final DistroConfig LATEST_SNAPSHOT =
      new DistroConfig("snapshot", "latest available snapshot version from main");

  public static final DistroConfig ADOT_LATEST_RELEASE =
      new DistroConfig("adot", "latest ADOT release", ADOT_1_27_0);

  static final List<String> pulseDisabled = List.of(
      "-Dotel.smp.enabled=false",
      "-Dotel.traces.sampler=traceidratio",
      "-Dotel.traces.sampler.arg=0.05",
      "-Dotel.metrics.exporter=none");
  public static final DistroConfig PULSE_DISABLED =
      new DistroConfig("pulse-disabled", "Pulse is disabled", null, pulseDisabled);

  static final List<String> pulseEnabledWithoutTrace = List.of(
      "-Dotel.smp.enabled=true",
      "-Dotel.traces.sampler=traceidratio",
      "-Dotel.traces.sampler.arg=0.00",
      "-Dotel.metrics.exporter=none");
  public static final DistroConfig PULSE_NO_TRACE =
      new DistroConfig("pulse-no-trace", "Pulse is enabled with metrics only", null, pulseEnabledWithoutTrace);

  static final List<String> pulseEnabledWithTrace = List.of(
      "-Dotel.smp.enabled=true",
      "-Dotel.traces.sampler=traceidratio",
      "-Dotel.traces.sampler.arg=0.05",
      "-Dotel.metrics.exporter=none");
  public static final DistroConfig PULSE =
      new DistroConfig("pulse", "Pulse is enabled with tracing and metrics", null, pulseEnabledWithTrace);

  static final List<String> pulseEnabledFullTrace = List.of(
      "-Dotel.smp.enabled=true",
      "-Dotel.traces.sampler=traceidratio",
      "-Dotel.traces.sampler.arg=1.00",
      "-Dotel.metrics.exporter=none");
  public static final DistroConfig PULSE_FULL_TRACE =
      new DistroConfig("pulse-full-trace", "Pulse is enabled with 100% traces and metrics", null, pulseEnabledFullTrace);

  private final String name;
  private final String description;
  private final URL url;
  private final List<String> additionalJvmArgs;

  public DistroConfig(String name, String description) {
    this(name, description, null);
  }

  public DistroConfig(String name, String description, String url) {
    this(name, description, url, Collections.emptyList());
  }

  public DistroConfig(String name, String description, String url, List<String> additionalJvmArgs) {
    this.name = name;
    this.description = description;
    this.url = makeUrl(url);
    this.additionalJvmArgs = new ArrayList<>(additionalJvmArgs);
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public boolean hasUrl() {
    return url != null;
  }

  public URL getUrl() {
    return url;
  }

  public List<String> getAdditionalJvmArgs() {
    return Collections.unmodifiableList(additionalJvmArgs);
  }

  private static URL makeUrl(String url) {
    try {
      if (url == null) {
        return null;
      }
      return URI.create(url).toURL();
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error parsing url", e);
    }
  }
}
