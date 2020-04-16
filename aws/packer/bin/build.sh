#!/bin/bash
export LOCAL_CIDR="$(curl -sSLj "http://checkip.amazonaws.com/")/32"
echo "LOCAL_CIDR: ${LOCAL_CIDR}"
packer build guacamole-jumphost.json