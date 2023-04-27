#!/bin/bash

export HONEYCOMB_API_KEY={{INSERT API KEY HERE}}
export OTEL_EXPORTER_OTLP_ENDPOINT=http://collector

tilt up web java

