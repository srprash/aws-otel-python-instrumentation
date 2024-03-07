#!/bin/bash
echo "Set up Java"
sudo yum update -y
sudo yum install java-17-amazon-corretto-devel -y
java -version

echo "Set up Docker"
sudo amazon-linux-extras install docker -y
sudo service docker start
sudo systemctl enable docker
sudo usermod -a -G docker ec2-user
docker info

echo "Copy and prepare files from S3"
aws s3api get-object --bucket pulse-sdk-perf-test --key aws-otel-python-instrumentation.zip ~/aws-otel-python-instrumentation.zip
cd ~
unzip -q ~/aws-otel-python-instrumentation.zip
cd ~/aws-otel-python-instrumentation/performance-tests

echo "Run test"
DATE=$(date '+%Y-%m-%d')
export DURATION=10m
echo $DURATION
mkdir -p results
./gradlew -i clean test > results/test_run_logs.out

echo "Copy files to S3"
INSTANCE_TYPE="`wget -q -O - http://169.254.169.254/latest/meta-data/instance-type`"
EC2_ID="`wget -q -O - http://169.254.169.254/latest/meta-data/instance-id`"
echo $INSTANCE_TYPE
echo $EC2_ID
aws s3 cp results s3://pulse-sdk-perf-test/$DATE/$INSTANCE_TYPE/$EC2_ID --recursive

