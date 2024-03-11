/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 * Modifications Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 */

package io.opentelemetry.distros;

import java.util.Collections;
import java.util.Map;

public enum DistroConfig {
  NONE(
      "none",
      "no distro at all",
      false,
      "performance-test/vehicle-inventory-service",
      Collections.EMPTY_MAP),
  OTEL_VANILLA(
      "vanilla_otel",
      "vanilla otel distro without app signals",
      true,
      "performance-test/vehicle-inventory-service-otel" ,
      Map.of("OTEL_TRACES_EXPORTER", "none")),
  APPLICATION_SIGNALS_DISABLED(
      "app_signals_disabled",
      "ADOT distro with Application Signals disabled",
      true,
      "performance-test/vehicle-inventory-service",
      Map.of("OTEL_AWS_APP_SIGNALS_ENABLED", "false", "OTEL_TRACES_SAMPLER", "xray")),
  APPLICATION_SIGNALS_NO_TRACES(
      "app_signals_no_traces",
      "ADOT distro with Application Signals enabled and no tracing",
      true,
      "performance-test/vehicle-inventory-service",
      Map.of(
          "OTEL_AWS_APP_SIGNALS_ENABLED",
          "true",
          "OTEL_AWS_SMP_EXPORTER_ENDPOINT",
          "http://collector:4317",
          "OTEL_TRACES_SAMPLER",
          "always_off")),
  APPLICATION_SIGNALS_TRACES(
      "app_signals_traces",
      "ADOT distro with Application Signals enabled and tracing",
      true,
      "performance-test/vehicle-inventory-service",
      Map.of(
          "OTEL_AWS_APP_SIGNALS_ENABLED",
          "true",
          "OTEL_AWS_SMP_EXPORTER_ENDPOINT",
          "http://collector:4317",
          "OTEL_TRACES_SAMPLER",
          "xray"));

  private final String name;
  private final String description;
  private final boolean doInstrument;
  private final String imageName;
  private final Map<String, String> additionalEnvVars;

  DistroConfig(
      String name,
      String description,
      boolean doInstrument,
      String imageName,
      Map<String, String> additionalEnvVars) {
    this.name = name;
    this.description = description;
    this.doInstrument = doInstrument;
    this.imageName = imageName;
    this.additionalEnvVars = additionalEnvVars;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public boolean doInstrument() {
    return doInstrument;
  }

  public String imageName() {
    return imageName;
  }

  public Map<String, String> getAdditionalEnvVars() {
    return Collections.unmodifiableMap(additionalEnvVars);
  }
}
