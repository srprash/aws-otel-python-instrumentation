/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 * Modifications Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 */

package io.opentelemetry.containers;

import io.opentelemetry.util.RuntimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

public class CollectorContainer {

  static final int COLLECTOR_PORT = 4317;
  static final int COLLECTOR_HEALTH_CHECK_PORT = 13133;
  static final int COLLECTOR_XRAY_PROXY_PORT = 2000;

  private static final Logger logger = LoggerFactory.getLogger(CollectorContainer.class);

  public static GenericContainer<?> build(Network network) {

    return new GenericContainer<>(
            DockerImageName.parse("otel/opentelemetry-collector-contrib:latest"))
        .withNetwork(network)
        .withNetworkAliases("collector")
        .withLogConsumer(new Slf4jLogConsumer(logger))
        .withExposedPorts(COLLECTOR_PORT, COLLECTOR_HEALTH_CHECK_PORT, COLLECTOR_XRAY_PROXY_PORT)
        .waitingFor(Wait.forHttp("/health").forPort(COLLECTOR_HEALTH_CHECK_PORT))
        .withCopyFileToContainer(
            MountableFile.forClasspathResource("collector.yaml"), "/etc/otel.yaml")
        .withCreateContainerCmdModifier(
            cmd -> cmd.getHostConfig().withCpusetCpus(RuntimeUtil.getNonApplicationCores()))
        .withCommand("--config /etc/otel.yaml")
        .withEnv("AWS_REGION", "us-east-1")
        .withEnv("AWS_ACCESS_KEY_ID", System.getenv("AWS_ACCESS_KEY_ID"))
        .withEnv("AWS_SECRET_ACCESS_KEY", System.getenv("AWS_SECRET_ACCESS_KEY"))
        .withEnv("AWS_SESSION_TOKEN", System.getenv("AWS_SESSION_TOKEN"));
  }
}
