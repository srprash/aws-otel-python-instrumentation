# Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
# SPDX-License-Identifier: Apache-2.0
import time as _time
from typing import Optional, Sequence

from amazon.opentelemetry.distro.sampler._clock import _Clock
from amazon.opentelemetry.distro.sampler._rate_limiter import _RateLimiter
from opentelemetry.context import Context
from opentelemetry.sdk.trace.sampling import Decision, Sampler, SamplingResult
from opentelemetry.trace import Link, SpanKind
from opentelemetry.trace.span import TraceState
from opentelemetry.util.types import Attributes


class _RateLimitingSampler(Sampler):
    def __init__(
        self,
        quota: int,
        clock: _Clock
    ):
        self.__reservoir = _RateLimiter(1, quota, clock)
        self.borrowing = False

    # pylint: disable=no-self-use
    def should_sample(
        self,
        parent_context: Optional[Context],
        trace_id: int,
        name: str,
        kind: SpanKind = None,
        attributes: Attributes = None,
        links: Sequence[Link] = None,
        trace_state: TraceState = None,
    ) -> SamplingResult:
        if self.__reservoir.try_spend(1, self.borrowing):
            return SamplingResult(decision=Decision.RECORD_AND_SAMPLE, attributes=attributes, trace_state=trace_state)
        return SamplingResult(decision=Decision.DROP, attributes=attributes, trace_state=trace_state)

    # pylint: disable=no-self-use
    def get_description(self) -> str:
        description = (
            "RateLimitingSampler{fallback sampling with sampling config of 1 req/sec and 5% of additional requests}"
        )
        return description