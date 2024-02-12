# Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
# SPDX-License-Identifier: Apache-2.0


# Disable snake_case naming style so this class can match the sampling rules response from X-Ray
# pylint: disable=invalid-name
class _SamplingTarget:
    def __init__(
        self,
        FixedRate: float = None,
        Interval: int = None,
        ReservoirQuota: int = None,
        ReservoirQuotaTTL: float = None,
        RuleName: str = None,
    ):
        self.FixedRate = FixedRate if FixedRate is not None else 0.0
        self.Interval = Interval  # can be None
        self.ReservoirQuota = ReservoirQuota  # can be None
        self.ReservoirQuotaTTL = ReservoirQuotaTTL  # can be None
        self.RuleName = RuleName if RuleName is not None else ""


class _UnprocessedStatistics:
    def __init__(
        self,
        ErrorCode: str = None,
        Message: str = None,
        RuleName: str = None,
    ):
        self.ErrorCode = ErrorCode if ErrorCode is not None else ""
        self.Message = Message if ErrorCode is not None else ""
        self.RuleName = RuleName if ErrorCode is not None else ""


class _SamplingTargetResponse:
    def __init__(
        self,
        LastRuleModification: float,
        SamplingTargetDocuments: [_SamplingTarget] = None,
        UnprocessedStatistics: [_UnprocessedStatistics] = None,
    ):
        self.LastRuleModification = LastRuleModification if LastRuleModification is not None else 0.0

        self.SamplingTargetDocuments = []
        if SamplingTargetDocuments is not None:
            for document in SamplingTargetDocuments:
                self.SamplingTargetDocuments.append(_SamplingTarget(**document))

        self.UnprocessedStatistics = []
        if UnprocessedStatistics is not None:
            for unprocessed in UnprocessedStatistics:
                self.UnprocessedStatistics.append(_UnprocessedStatistics(**unprocessed))
