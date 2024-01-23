# Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
# SPDX-License-Identifier: Apache-2.0
from unittest import TestCase
from pkg_resources import iter_entry_points

from pkg_resources import DistributionNotFound, require
# from amazon.opentelemetry.distro.aws_opentelemetry_distro import AwsOpenTelemetryDistro
import os
from logging import getLogger

from opentelemetry.distro import OpenTelemetryDistro
from opentelemetry.environment_variables import OTEL_PROPAGATORS, OTEL_PYTHON_ID_GENERATOR
from opentelemetry.sdk.environment_variables import OTEL_EXPORTER_OTLP_METRICS_DEFAULT_HISTOGRAM_AGGREGATION ,OTEL_TRACES_SAMPLER
from opentelemetry.environment_variables import (
    OTEL_METRICS_EXPORTER,
    OTEL_TRACES_EXPORTER,
)

logger = getLogger(__name__)


class TestAwsOpenTelemetryDistro(TestCase):
    def test_package_available(self):
        try:
            require(["aws-opentelemetry-distro"])
        except DistributionNotFound:
            self.fail("aws-opentelemetry-distro not installed")

    def test_distro(self):
        distro = AwsOpenTelemetryDistro()
        for entry_point in iter_entry_points("opentelemetry_instrumentor"):
            # if entry_point.name in package_to_exclude:
            #     _logger.debug(
            #         "Instrumentation skipped for library %s", entry_point.name
            #     )
            #     continue

            # try:
            #     conflict = get_dist_dependency_conflicts(entry_point.dist)
            #     if conflict:
            #         _logger.debug(
            #             "Skipping instrumentation %s: %s",
            #             entry_point.name,
            #             conflict,
            #         )
            #         continue

                # tell instrumentation to not run dep checks again as we already did it above
            with open("instrumwent.txt", 'a') as file:
                file.write(entry_point.name)
            distro.load_instrumentor(entry_point, skip_dep_check=True)
            print("Instrumented %s", entry_point.name)

class AwsOpenTelemetryDistro(OpenTelemetryDistro):
    def _configure(self, **kwargs):
        super(AwsOpenTelemetryDistro, self)._configure()
        os.environ.setdefault(OTEL_TRACES_SAMPLER, "parentbased_always_on")
        os.environ.setdefault(OTEL_TRACES_EXPORTER, "console")
        os.environ.setdefault(OTEL_METRICS_EXPORTER, "console")
        os.environ.setdefault(
            OTEL_EXPORTER_OTLP_METRICS_DEFAULT_HISTOGRAM_AGGREGATION, "base2_exponential_bucket_histogram"
        )
        os.environ.setdefault(OTEL_PROPAGATORS, "xray,tracecontext,b3,b3multi")
        os.environ.setdefault(OTEL_PYTHON_ID_GENERATOR, "xray")
