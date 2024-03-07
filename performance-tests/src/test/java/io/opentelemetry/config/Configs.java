/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 * Modifications Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 */

package io.opentelemetry.config;

import io.opentelemetry.distros.DistroConfig;
import java.util.Arrays;
import java.util.stream.Stream;

/** Defines all test configurations */
public enum Configs {
  JAVA_100_TPS(
      TestConfig.builder()
          .name("java-100-tps")
          .description("Compares all DistroConfigs (100TPS test)")
          .withDistroConfigs(
              DistroConfig.NONE, DistroConfig.LATEST_RELEASE, DistroConfig.LATEST_SNAPSHOT)
          .warmupSeconds(10)
          .maxRequestRate(100)
          .duration(System.getenv("DURATION"))
          .concurrentConnections(System.getenv("CONCURRENCY"))
          .build()),
  JAVA_500_TPS(
      TestConfig.builder()
          .name("java-500-tps")
          .description("Compares all DistroConfigs (500TPS test)")
          .withDistroConfigs(
              DistroConfig.NONE, DistroConfig.LATEST_RELEASE, DistroConfig.LATEST_SNAPSHOT)
          .warmupSeconds(10)
          .maxRequestRate(500)
          .duration(System.getenv("DURATION"))
          .concurrentConnections(System.getenv("CONCURRENCY"))
          .build());

  public final TestConfig config;

  public static Stream<TestConfig> all() {
    return Arrays.stream(Configs.values()).map(x -> x.config);
  }

  Configs(TestConfig config) {
    this.config = config;
  }
}