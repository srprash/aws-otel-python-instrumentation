#!/bin/sh

apk add --no-cache python3 \
&& apk add gcc python3-dev musl-dev linux-headers \
&& python3 -m ensurepip \
&& pip3 install --upgrade pip setuptools \
&& pip3 install psutil \
&& rm -r /usr/lib/python*/ensurepip && \
if [ ! -e /usr/bin/pip ]; then ln -s pip3 /usr/bin/pip ; fi && \
if [[ ! -e /usr/bin/python ]]; then ln -sf /usr/bin/python3 /usr/bin/python; fi && \
rm -r /root/.cache