#!/bin/bash

set -euxo pipefail

if [ ${EUID} != 0 ]; then
    sudo "$0" "$@"
    exit $?
fi

yum install -y epel-release
yum install -y unzip jq

AWS_CLI_TEMP_DIR=/tmp/aws-cli
AWS_CLI_INSTALL_DIR=/opt/aws-cli
AWS_CLI_ARCHIVE_URL=https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip
AWS_CLI_ARCHIVE_FILE=awscli-exe-linux-x86_64.zip
AWS_CLI_ARCHIVE_PATH="${AWS_CLI_TEMP_DIR}/${AWS_CLI_ARCHIVE_FILE}"

mkdir -p "${AWS_CLI_TEMP_DIR}" "${AWS_CLI_INSTALL_DIR}"
curl -L -o "${AWS_CLI_ARCHIVE_PATH}" "${AWS_CLI_ARCHIVE_URL}"
unzip -o -d "${AWS_CLI_TEMP_DIR}" "${AWS_CLI_ARCHIVE_PATH}"
/tmp/aws-cli/aws/install --install-dir "${AWS_CLI_INSTALL_DIR}" --bin-dir "/usr/bin"
rm -rf "${AWS_CLI_TEMP_DIR}"
