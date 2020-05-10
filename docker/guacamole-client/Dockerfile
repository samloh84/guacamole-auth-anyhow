FROM guacamole/guacamole:latest

ARG GUACAMOLE_TMP_DIR="/tmp/guacamole"

# Copy local files
COPY anyhow/ /opt/guacamole/anyhow/
COPY start.sh /opt/guacamole/bin/start.sh
RUN chmod +x /opt/guacamole/bin/start.sh

