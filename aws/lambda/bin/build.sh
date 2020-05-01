#!/bin/bash
set -euxo pipefail

SCRIPT_PATH="$(
  cd "$(dirname "${BASH_SOURCE[0]}")"
  pwd
)"
LAMBDA_SRC_DIR="$(dirname "${SCRIPT_PATH}")"
echo "LAMBDA_SRC_DIR: ${LAMBDA_SRC_DIR}"

PROJECT_DIR="$(
  cd "${LAMBDA_SRC_DIR}"/../..
  pwd
)"
echo "PROJECT_DIR: ${PROJECT_DIR}"


cd "${LAMBDA_SRC_DIR}"
npm install
mkdir -p ./output
./node_modules/.bin/webpack
cd ./output
zip -9 -q -r ./guacamole-config.zip ./index.js
