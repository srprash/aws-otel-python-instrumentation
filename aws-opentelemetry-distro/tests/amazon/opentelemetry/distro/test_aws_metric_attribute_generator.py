# Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
# SPDX-License-Identifier: Apache-2.0
from unittest import TestCase
from unittest.mock import MagicMock

from opentelemetry.attributes import BoundedAttributes
from opentelemetry.sdk.trace import Resource
from opentelemetry.trace import Span, SpanContext, SpanKind
from opentelemetry.sdk.util.instrumentation import InstrumentationScope
from opentelemetry.util.types import Attributes

from amazon.opentelemetry.distro._aws_attribute_keys import AWS_SPAN_KIND, AWS_LOCAL_SERVICE, AWS_LOCAL_OPERATION
from amazon.opentelemetry.distro._aws_metric_attribute_generator import _AwsMetricAttributeGenerator, DEPENDENCY_METRIC, SERVICE_METRIC
from amazon.opentelemetry.distro.metric_attribute_generator import MetricAttributeGenerator


class TestAwsMetricAttributeGenerator(TestCase):
    AWS_LOCAL_OPERATION_VALUE = "AWS local operation"
    AWS_REMOTE_SERVICE_VALUE = "AWS remote service"
    AWS_REMOTE_OPERATION_VALUE = "AWS remote operation"
    SERVICE_NAME_VALUE = "Service name"
    SPAN_NAME_VALUE = "Span name"
    UNKNOWN_SERVICE = "UnknownService"
    UNKNOWN_OPERATION = "UnknownOperation"
    UNKNOWN_REMOTE_SERVICE = "UnknownRemoteService"
    UNKNOWN_REMOTE_OPERATION = "UnknownRemoteOperation"
    INTERNAL_OPERATION = "InternalOperation"
    LOCAL_ROOT = "LOCAL_ROOT"
    GENERATOR: MetricAttributeGenerator = _AwsMetricAttributeGenerator()

    class ThrowableWithMethodGetStatusCode(Exception):
        def __init__(self, http_status_code: int):
            self.http_status_code: int = http_status_code

    def get_status_code(self):
        return self.http_status_code


    class ThrowableWithMethodStatusCode(Exception):
        def __init__(self, http_status_code: int):
            self.http_status_code:int = http_status_code

        def status_code(self):
            return self.http_status_code


    class ThrowableWithoutStatusCode(Exception):
        pass

    def setUp(self):
        self.attributes_mock: Attributes = MagicMock()
        self. instrumentation_scope_info_mock: InstrumentationScope = MagicMock()
        self.instrumentation_scope_info_mock.name = "Scope name"
        self.span_data_mock: Span = MagicMock()
        self.span_data_mock.attributes = self.attributes_mock
        self.span_data_mock.instrumentation_scope_info = self.instrumentation_scope_info_mock
        self.span_context_mock: SpanContext = MagicMock()
        self.span_data_mock.get_span_context.return_value = self.span_context_mock
        self.parent_span_context: SpanContext = MagicMock()
        self.parent_span_context.is_valid = True
        self.parent_span_context.is_remote = False
        self.span_data_mock.parent = self.parent_span_context
        self.resource: Resource = Resource.get_empty()

    def test_span_attributes_for_empty_resource(self):
        expected_attributes: BoundedAttributes = BoundedAttributes(attributes={
            AWS_SPAN_KIND: SpanKind.SERVER,
            AWS_LOCAL_SERVICE: self.UNKNOWN_SERVICE,
            AWS_LOCAL_OPERATION: self.UNKNOWN_OPERATION
        })
        self.__valid_attributes_produced_for_non_local_root_span_of_kind(expected_attributes, SpanKind.SERVER)

    def __valid_attributes_produced_for_non_local_root_span_of_kind(self, expected_attributes: Attributes, kind: SpanKind):
        self.span_data_mock.kind = kind
        self.span_data_mock.name = None
        attribute_map: dict = self.GENERATOR.generate_metric_attributes_dict_from_span(self.span_data_mock, self.resource)
        service_attributes = attribute_map.get(SERVICE_METRIC)
        dependency_attributes = attribute_map.get(DEPENDENCY_METRIC)
        if attribute_map.items():
            if SpanKind.PRODUCER == kind or SpanKind.CLIENT == kind or SpanKind.CONSUMER == kind:
                self.assertIsNone(service_attributes)
                self.assertEqual(dependency_attributes, expected_attributes)
            else:
                self.assertEqual(service_attributes, expected_attributes)
                self.assertIsNone(dependency_attributes)






