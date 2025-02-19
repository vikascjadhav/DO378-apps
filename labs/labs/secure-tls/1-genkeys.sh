#!/bin/bash

cd ~/DO378/labs/secure-tls

echo "Generating keystore file for solver..."
keytool -noprompt -genkeypair -keyalg RSA -keysize 2048 -validity 365 \
  -dname "CN=solver,OU=Training, O=RedHat, L=Raleigh, ST=NC, C=US" \
  -ext "SAN:c=DNS:localhost,IP:127.0.0.1" \
  -alias solver \
  -storepass redhat -keypass redhat \
  -keystore solver/solver.keystore

echo "Generating keystore file for adder..."
keytool -noprompt -genkeypair -keyalg RSA -keysize 2048 -validity 365 \
  -dname "CN=adder,OU=Training, O=RedHat, L=Raleigh, ST=NC, C=US" \
  -ext "SAN:c=DNS:localhost,IP:127.0.0.1" \
  -alias adder \
  -storepass redhat -keypass redhat \
  -keystore adder/adder.keystore

echo "Generating keystore file for multiplier..."
keytool -noprompt -genkeypair -keyalg RSA -keysize 2048 -validity 365 \
  -dname "CN=multiplier,OU=Training, O=RedHat, L=Raleigh, ST=NC, C=US" \
  -ext "SAN:c=DNS:localhost,IP:127.0.0.1" \
  -alias multiplier \
  -storepass redhat -keypass redhat \
  -keystore multiplier/multiplier.keystore
