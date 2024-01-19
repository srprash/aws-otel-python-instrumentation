
from os import environ, linesep
from sys import stdout

from typing import IO, Callable, Dict, Iterable, Optional


# This kind of import is needed to avoid Sphinx errors.
import opentelemetry.sdk.metrics._internal

from opentelemetry.sdk.metrics._internal.aggregation import (
    AggregationTemporality,
    DefaultAggregation,
)
from opentelemetry.sdk.metrics._internal.point import MetricsData

from opentelemetry.sdk.metrics.export import (
    MetricExporter,
    MetricReader,
    PeriodicExportingMetricReader,
    MetricExportResult
)



class ConsoleMetricExporter(MetricExporter):
    print("ConsoleMetricExporter!!!!!!!!!!!!!!!!!!!!!!!!!!!")
    """Implementation of :class:`MetricExporter` that prints metrics to the
    console.

    This class can be used for diagnostic purposes. It prints the exported
    metrics to the console STDOUT.
    """

    def __init__(
            self,
            out: IO = stdout,
            formatter: Callable[
                ["opentelemetry.sdk.metrics.export.MetricsData"], str
            ] = lambda metrics_data: metrics_data.to_json()
                                     + linesep,
            preferred_temporality: Dict[type, AggregationTemporality] = None,
            preferred_aggregation: Dict[
                type, "opentelemetry.sdk.metrics.view.Aggregation"
            ] = None,
    ):
        super().__init__(
            preferred_temporality=preferred_temporality,
            preferred_aggregation=preferred_aggregation,
        )
        self.out = out
        self.formatter = formatter

    def export(
            self,
            metrics_data: MetricsData,
            timeout_millis: float = 10_000,
            **kwargs,
    ) -> MetricExportResult:
        print("metrics_data------------------------")
        print(metrics_data)
        print("------------------------metrics_data")
        self.out.write(self.formatter(metrics_data))
        self.out.flush()
        return MetricExportResult.SUCCESS

    def shutdown(self, timeout_millis: float = 30_000, **kwargs) -> None:
        pass

    def force_flush(self, timeout_millis: float = 10_000) -> bool:
        return True