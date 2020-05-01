#!/bin/bash
LOCAL_CIDR="$(curl -sSLj "http://checkip.amazonaws.com/")/32"
export LOCAL_CIDR
echo "LOCAL_CIDR: ${LOCAL_CIDR}"
packer build guacamole-jumphost.json