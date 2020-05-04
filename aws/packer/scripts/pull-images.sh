
set -euxo pipefail

if [ ${EUID} != 0 ]; then
    sudo "$0" "$@"
    exit $?
fi

docker pull guacamole/guacd:latest
docker pull registry.gitlab.com/govtech-iacwg/guacamole-auth-anyhow/guacamole-client:latest

