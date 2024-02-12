# Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
# SPDX-License-Identifier: Apache-2.0

import datetime

from amazon.opentelemetry.distro.sampler._clock import _Clock


class MockClock(_Clock):
    def __init__(self, dt: datetime.datetime):
        self.time_now = dt

    def now(self) -> datetime:
        return self.time_now

    def add_time(self, seconds: float) -> None:
        self.time_now += self.time_delta(seconds)

    def set_time(self, dt: datetime.datetime) -> None:
        self.time_now = dt
