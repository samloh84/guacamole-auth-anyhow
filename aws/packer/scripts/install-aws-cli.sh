
set -euxo pipefail

if [ ${EUID} != 0 ]; then
    sudo "$0" "$@"
    exit $?
fi

yum install -y epel-release
yum install -y python3 python3-pip
pip3 install --upgrade pip awscli
