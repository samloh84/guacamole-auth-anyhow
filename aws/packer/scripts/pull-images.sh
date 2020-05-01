
set -euxo pipefail

if [ ${EUID} != 0 ]; then
    sudo "$0" "$@"
    exit $?
fi

docker pull guacamole/guacd:latest
docker pull samloh84/guacamole-client:latest

