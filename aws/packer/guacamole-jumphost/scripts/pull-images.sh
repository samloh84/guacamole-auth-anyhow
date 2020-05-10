
set -euxo pipefail

if [ ${EUID} != 0 ]; then
    sudo "$0" "$@"
    exit $?
fi


GUACAMOLE_CLIENT_DOCKER_FULL_IMAGE_NAME="${GUACAMOLE_CLIENT_DOCKER_FULL_IMAGE_NAME:-docker pull registry.gitlab.com/govtech-iacwg/guacamole-auth-anyhow/guacamole-client:latest}"
AWS_ECR_GET_LOGIN_PASSWORD="${AWS_ECR_GET_LOGIN_PASSWORD:-0}"
DOCKER_REPOSITORY_URL=${DOCKER_REPOSITORY_URL:-}
DOCKER_REPOSITORY_USERNAME=${DOCKER_REPOSITORY_USERNAME:-}
DOCKER_REPOSITORY_PASSWORD=${DOCKER_REPOSITORY_PASSWORD:-}
if [[ ${AWS_ECR_GET_LOGIN_PASSWORD} -eq 1 ]]; then
  aws configure set region ap-southeast-1
  AWS_ACCOUNT_ID=$(aws sts get-caller-identity | jq -r .Account)
	aws ecr get-login-password --region ap-southeast-1 | \
	docker login --username AWS --password-stdin "${AWS_ACCOUNT_ID}.dkr.ecr.ap-southeast-1.amazonaws.com"
fi

if [[ ! -z "${DOCKER_REPOSITORY_URL}" && ! -z "${DOCKER_REPOSITORY_USERNAME}" && ! -z "${DOCKER_REPOSITORY_PASSWORD}" ]]; then
  echo "${DOCKER_REPOSITORY_PASSWORD}" | docker login --username "${DOCKER_REPOSITORY_USERNAME}" --password-stdin "${DOCKER_REPOSITORY_URL}"
fi
docker pull guacamole/guacd:latest
docker pull "${GUACAMOLE_CLIENT_DOCKER_FULL_IMAGE_NAME}"
