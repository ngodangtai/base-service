#!/bin/bash

ARCH_NAMES=$1

for docker_arch in $ARCH_NAMES; do
# generate Dockerfile
  cp Dockerfile.cross Dockerfile
  sed -i -e "s|__BASEIMAGE_ARCH__|${docker_arch}|g" Dockerfile
done