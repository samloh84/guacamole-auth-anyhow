#!/bin/bash
set -euxo pipefail

SCRIPT_PATH="$(
  cd "$(dirname "${BASH_SOURCE[0]}")"
  pwd
)"
TERRAFORM_MODULE_DIR="$(dirname "${SCRIPT_PATH}")"
echo "TERRAFORM_MODULE_DIR: ${TERRAFORM_MODULE_DIR}"

PROJECT_DIR="$(
  cd "${TERRAFORM_MODULE_DIR}"/../../..
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
  source ~/.aws/login_aws.sh

  if ! ls "${TERRAFORM_MODULE_DIR}"/guacamole-config.zip; then

    cd "${LAMBDA_DIR}"/bin
    ./build.sh
    cp "${LAMBDA_DIR}"/output/guacamole-config.zip "${TERRAFORM_MODULE_DIR}"/guacamole-config.zip
  fi

  cd "${TERRAFORM_MODULE_DIR}"
  terraform init
  terraform apply \
    -var-file .secret-variables.tfvars \
    -auto-approve

}

function _ssh() {
  SSH_SERVER=$(terraform output -json | jq -r '.guacamole_ip.value')
  CENTOS_SERVER=$(terraform output -json | jq -r '.centos_ip.value')
  WINDOWS_SERVER=$(terraform output -json | jq -r '.windows_ip.value')
  eval $(ssh-agent) && ssh-add ~/.ssh/guacamole

  ssh \
    -o StrictHostKeyChecking=no \
    -o ForwardAgent=yes \
    -L63389:"${WINDOWS_SERVER}":3389 \
    -L6622:"${CENTOS_SERVER}":22 \
    centos@"${SSH_SERVER}"

}

function taint_jumphost() {
  terraform taint aws_instance.guacamole_jumphost
  apply
}

function taint_lambda() {
  terraform taint aws_lambda_function.guacamole_config
  apply
}

function taint() {
  terraform taint aws_lambda_function.guacamole_config
  terraform taint aws_instance.guacamole_jumphost
  apply
}

function destroy() {
  cd "${TERRAFORM_MODULE_DIR}"
  terraform init
  terraform destroy \
    -var-file .secret-variables.tfvars \
    -auto-approve
}

function docker_rebuild() {
  cd "${PROJECT_DIR}/docker/guacamole-client"
  bin/docker.sh rebuild
  bin/docker.sh push
}
function packer_build() {
  source ~/.aws/login_aws.sh

  cd "${PROJECT_DIR}/aws/packer"
  bin/build.sh
}
function lambda_build() {
  source ~/.aws/login_aws.sh
  cd "${PROJECT_DIR}/aws/lambda"
  bin/build.sh
  cp "${LAMBDA_DIR}"/output/guacamole-config.zip "${TERRAFORM_MODULE_DIR}"/guacamole-config.zip
}

function rebuild() {
  docker_rebuild
  lambda_build
  packer_build
  apply
}

HELP_MESSAGE="available subcommands: apply ssh"

if [[ "$#" -lt 1 ]]; then
  echo "${HELP_MESSAGE}"
  exit 1
else
  case "$1" in
  apply)
    apply
    ;;
  ssh)
    _ssh
    ;;
  docker_build)
    docker_rebuild
    ;;
  packer_build)
    packer_build
    ;;
  lambda_build)
    lambda_build
    ;;
  rebuild)
    rebuild
    ;;
  taint_jumphost)
    taint_jumphost
    ;;
  taint_lambda)
    taint_lambda
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
