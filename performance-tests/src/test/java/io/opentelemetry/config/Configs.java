/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.config;

import io.opentelemetry.distros.DistroConfig;
import io.opentelemetry.util.RuntimeUtil;
import java.util.Arrays;
import java.util.stream.Stream;

/** Defines all test configurations */
public enum Configs {
  JAVA_100_TPS(
      TestConfig.builder()
          .name("java-100-tps")
          .description("Compares no agent, Pulse disabled, Pulse (metrics), Pulse (metrics and traces), Pulse (metrics and 100% traces) at 100 TPS")
          .withAgents(DistroConfig.NONE, DistroConfig.PULSE_DISABLED, DistroConfig.PULSE_NO_TRACE, DistroConfig.PULSE, DistroConfig.PULSE_FULL_TRACE)
          .warmupSeconds(0)
          .maxRequestRate(100)
          .totalIterations(50000) // 500 for 01:33, set large enough to 50000 for 1 hour.
          .duration(RuntimeUtil.getDuration()) // set "60m" for 1 hour
          .build());

  public final TestConfig config;

  public static Stream<TestConfig> all() {
    return Arrays.stream(Configs.values()).map(x -> x.config);
  }

  Configs(TestConfig config) {
    this.config = config;
  }
}

