# Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
# SPDX-License-Identifier: Apache-2.0

# Use an official Python runtime as a parent image
FROM python:3.10

# Set environment variables
ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

# Set the working directory
WORKDIR /image-service-app

# Install dependencies
COPY ImageServiceApp/requirements.txt /image-service-app/
COPY ImageServiceApp/aws_opentelemetry_distro-0.0.1-py3-none-any.whl /image-service-app/
COPY ImageServiceApp/opentelemetry_sdk_extension_aws-2.0.1-py3-none-any.whl /image-service-app/

RUN pip install -r requirements.txt && pip install aws_opentelemetry_distro-0.0.1-py3-none-any.whl && pip install opentelemetry_sdk_extension_aws-2.0.1-py3-none-any.whl --force-reinstall

# Copy the project code into the container
COPY ImageServiceApp/. /image-service-app/