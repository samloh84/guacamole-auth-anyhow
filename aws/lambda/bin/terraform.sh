#!/bin/bash
set -euxo pipefail

SCRIPT_PATH="$(
  cd "$(dirname "${BASH_SOURCE[0]}")"
  pwd
)"
TERRAFORM_MODULE_DIR="$(dirname "${SCRIPT_PATH}")"
echo "TERRAFORM_MODULE_DIR: ${TERRAFORM_MODULE_DIR}"

PROJECT_DIR="$(
  cd "${TERRAFORM_MODULE_DIR}"/../..
  pwd
)"
echo "PROJECT_DIR: ${PROJECT_DIR}"

LAMBDA_DIR="${PROJECT_DIR}/aws/lambda"
echo "LAMBDA_DIR: ${LAMBDA_DIR}"

if ls "${SCRIPT_PATH}"/.secret-env-*.sh; then
  for SECRET_ENV in "${SCRIPT_PATH}"/.secret-env-*.sh; do
    # shellcheck disable=SC1090
    source "${SECRET_ENV}"
    echo "Loaded secret environment variables from ${SECRET_ENV}"
  done
fi

function apply() {

  if ! ls "${LAMBDA_DIR}"/output/guacamole-config.zip; then
    cd "${LAMBDA_DIR}"/bin
    ./build.sh
  fi

  cd "${TERRAFORM_MODULE_DIR}"
  terraform init
  terraform apply \
    -var-file .secret-variables.tfvars \
    -auto-approve

}

function destroy() {
  cd "${TERRAFORM_MODULE_DIR}"
  terraform init
  terraform destroy \
    -var-file .secret-variables.tfvars \
    -auto-approve
}

function invoke() {
  aws lambda invoke --function-name guacamole-config-dev /dev/stdout
}

function taint() {
  cd "${TERRAFORM_MODULE_DIR}"
  terraform taint aws_lambda_function.guacamole_config_dev
  apply
}

HELP_MESSAGE="available subcommands: apply destroy taint invoke"

if [[ "$#" -lt 1 ]]; then
  echo "${HELP_MESSAGE}"
  exit 1
else
  case "$1" in
  apply)
    apply
    ;;
  destroy)
    destroy
    ;;
  invoke)
    invoke
    ;;
  taint)
    taint
    ;;
  *)
    echo "${HELP_MESSAGE}"
    exit 1
    ;;
  esac
fi
