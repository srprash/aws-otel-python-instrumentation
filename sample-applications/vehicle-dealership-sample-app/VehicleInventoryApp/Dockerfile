# Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
# SPDX-License-Identifier: Apache-2.0

# Use an official Python runtime as a parent image
FROM python:3.10

# Set environment variables
ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

# Set the working directory
WORKDIR /vehicle-inventory-app

# Install dependencies
COPY VehicleInventoryApp/requirements.txt /vehicle-inventory-app/
COPY VehicleInventoryApp/aws_opentelemetry_distro-0.0.1-py3-none-any.whl /vehicle-inventory-app/
COPY VehicleInventoryApp/opentelemetry_sdk_extension_aws-2.0.1-py3-none-any.whl /vehicle-inventory-app/

RUN pip install -r requirements.txt && pip install aws_opentelemetry_distro-0.0.1-py3-none-any.whl && pip install opentelemetry_sdk_extension_aws-2.0.1-py3-none-any.whl

# Copy the project code into the container
COPY VehicleInventoryApp/. /vehicle-inventory-app/