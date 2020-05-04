#!/bin/bash
set -euxo pipefail

SCRIPT_PATH="$(
  cd "$(dirname "${BASH_SOURCE[0]}")"
  pwd
)"
DOCKER_BUILD_DIR="$(dirname "${SCRIPT_PATH}")"
echo "DOCKER_BUILD_DIR: ${DOCKER_BUILD_DIR}"

PROJECT_DIR="$(
  cd "${DOCKER_BUILD_DIR}"/../..
  pwd
)"
echo "PROJECT_DIR: ${PROJECT_DIR}"

for SECRET_ENV in "${SCRIPT_PATH}"/.secret-env-*.sh; do
  # shellcheck disable=SC1090
  source "${SECRET_ENV}"
  echo "Loaded secret environment variables from ${SECRET_ENV}"
done

DOCKER_IMAGE_REPOSITORY="registry.gitlab.com/govtech-iacwg/guacamole-auth-anyhow/guacamole-client"
DOCKER_IMAGE_TAG="latest"
LOGBACK_LEVEL="${DEBUG_LOG:-info}"


MODULES_DIR="${PROJECT_DIR}/modules"
echo "MODULES_DIR: ${MODULES_DIR}"

MODULES_SUBDIRS="$(ls -d "${MODULES_DIR}"/*)"
echo "MODULES_SUBDIRS: ${MODULES_SUBDIRS}"

MODULES_TARGET_DIR="${DOCKER_BUILD_DIR}/anyhow"
echo "MODULES_TARGET_DIR: ${MODULES_TARGET_DIR}"

function build() {

  rm -rf "${MODULES_TARGET_DIR}"
  mkdir -p "${MODULES_TARGET_DIR}"

  for MODULE_SUBDIR in ${MODULES_SUBDIRS}; do
    echo "MODULE_SUBDIR: ${MODULE_SUBDIR}"
    MODULE=$(basename "${MODULE_SUBDIR}")
    echo "MODULE: ${MODULE}"

    if ! ls "${MODULES_TARGET_DIR}"/"${MODULE}"-*.jar; then

      if ! ls "${MODULE_SUBDIR}"/target/"${MODULE}"-*.jar; then
        echo "Building modules in ${PROJECT_DIR}"
        cd "${PROJECT_DIR}"
        mvn package
      fi

      echo "Copying ${MODULE} to ${DOCKER_BUILD_DIR}"
      cp "${MODULE_SUBDIR}"/target/"${MODULE}"-*.jar "${MODULES_TARGET_DIR}"
    fi

  done

  cd "${DOCKER_BUILD_DIR}"
  docker build --tag "${DOCKER_IMAGE_REPOSITORY}:${DOCKER_IMAGE_TAG}" "${DOCKER_BUILD_DIR}"
}

function rebuild(){
  cd "${PROJECT_DIR}"
  mvn clean
  build
}

function create_guacamole_network() {
  if ! docker network inspect guacamole; then
    docker network create guacamole
  fi
}

function run_guacamole_server() {

  if docker inspect guacamole-server; then
    docker rm -f guacamole-server
  fi

  docker run \
    --name guacamole-server \
    --restart unless-stopped \
    --detach \
    --network guacamole \
    guacamole/guacd

}

function run_guacamole_client() {

  if docker inspect guacamole-client; then
    docker rm -f guacamole-client
  fi

  docker run \
    --name guacamole-client \
    --restart unless-stopped \
    --network guacamole \
    --publish 8080:8080 \
    --tty \
    --interactive \
    --env "GUACD_HOSTNAME=guacamole-server" \
    --env "OPENID_AUTHORIZATION_ENDPOINT=${OPENID_AUTHORIZATION_ENDPOINT}" \
    --env "OPENID_JWKS_ENDPOINT=${OPENID_JWKS_ENDPOINT}" \
    --env "OPENID_ISSUER=${OPENID_ISSUER}" \
    --env "OPENID_CLIENT_ID=${OPENID_CLIENT_ID}" \
    --env "OPENID_REDIRECT_URI=http://localhost:8080/guacamole" \
    --env "ANYHOW_AWS_LAMBDA_FUNCTION=guacamole-config" \
    --env "ANYHOW_AWS_SESSION_TOKEN=${ANYHOW_AWS_SESSION_TOKEN}" \
    --env "ANYHOW_AWS_SECRET_ACCESS_KEY=${ANYHOW_AWS_SECRET_ACCESS_KEY}" \
    --env "ANYHOW_AWS_ACCESS_KEY_ID=${ANYHOW_AWS_ACCESS_KEY_ID}" \
    --env "ANYHOW_AWS_REGION=${ANYHOW_AWS_REGION}" \
    --env "LOGBACK_LEVEL=${LOGBACK_LEVEL}" \
    "${DOCKER_IMAGE_REPOSITORY}:${DOCKER_IMAGE_TAG}"
}

function run() {
  create_guacamole_network
  run_guacamole_server
  run_guacamole_client
}

function exec_bash() {
  docker exec --tty --interactive guacamole-client bash
}

function bash() {

  if docker inspect guacamole-client-bash; then
    docker rm -f guacamole-client-bash
  fi

  docker run \
    --name guacamole-client-bash \
    --network guacamole \
    --publish 8080:8080 \
    --tty \
    --interactive \
    --env "GUACD_HOSTNAME=guacamole-server" \
    --env "OPENID_AUTHORIZATION_ENDPOINT=${OPENID_AUTHORIZATION_ENDPOINT}" \
    --env "OPENID_JWKS_ENDPOINT=${OPENID_JWKS_ENDPOINT}" \
    --env "OPENID_ISSUER=${OPENID_ISSUER}" \
    --env "OPENID_CLIENT_ID=${OPENID_CLIENT_ID}" \
    --env "OPENID_REDIRECT_URI=http://localhost:8080/guacamole" \
    --env "ANYHOW_AWS_LAMBDA_FUNCTION=guacamole-config" \
    --env "ANYHOW_AWS_SESSION_TOKEN=${ANYHOW_AWS_SESSION_TOKEN}" \
    --env "ANYHOW_AWS_SECRET_ACCESS_KEY=${ANYHOW_AWS_SECRET_ACCESS_KEY}" \
    --env "ANYHOW_AWS_ACCESS_KEY_ID=${ANYHOW_AWS_ACCESS_KEY_ID}" \
    --env "ANYHOW_AWS_REGION=${ANYHOW_AWS_REGION}" \
    --env "LOGBACK_LEVEL=${LOGBACK_LEVEL}" \
    "${DOCKER_IMAGE_REPOSITORY}:${DOCKER_IMAGE_TAG}" \
    bash
}

function push() {
  docker push \
    "${DOCKER_IMAGE_REPOSITORY}:${DOCKER_IMAGE_TAG}"
}

HELP_MESSAGE="available subcommands: build bash exec_bash run"

if [[ "$#" -lt 1 ]]; then
  echo "${HELP_MESSAGE}"
  exit 1
else
  case "$1" in
  build)
    build
    ;;
  rebuild)
    rebuild
    ;;
  bash)
    bash
    ;;
  exec_bash)
    exec_bash
    ;;
  push)
    push
    ;;
  run)
    run
    ;;
  *)
    echo "${HELP_MESSAGE}"
    exit 1
    ;;
  esac
fi
