# Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
# SPDX-License-Identifier: Apache-2.0

from amazon.opentelemetry.distro.sampler._clock import _Clock
from amazon.opentelemetry.distro.sampler._rate_limiting_sampler import _RateLimitingSampler
from opentelemetry.sdk.trace.sampling import Decision, Sampler, SamplingResult, TraceIdRatioBased


class _FallbackSampler(Sampler):
    def __init__(self, clock: _Clock):
        self.__rate_limiting_sampler = _RateLimitingSampler(1, clock)
        self.__fixed_rate_sampler = TraceIdRatioBased(0.05)

    # pylint: disable=no-self-use
    def should_sample(self, *args, **kwargs) -> SamplingResult:
        sampling_result = self.__rate_limiting_sampler.should_sample(*args, **kwargs)
        if sampling_result.decision is not Decision.DROP:
            return sampling_result
        return self.__fixed_rate_sampler.should_sample(*args, **kwargs)

    # pylint: disable=no-self-use
    def get_description(self) -> str:
        description = (
            "FallbackSampler{fallback sampling with sampling config of 1 req/sec and 5% of additional requests}"
        )
        return description
