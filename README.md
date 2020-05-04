# Guacamole Anyhow Authentication Extension

Apache Guacamole is a remote administration tool that presents a web interface to control remote workloads using RDP, VNC or SSH.
The Anyhow series of authentication extensions allow Apache Guacamole to retrieve a configuration written in JSON/YAML and present connections to the user.

The configurations can be retrieved through the following mechanisms using the appropriate extension:
* file
* url
* executable
* aws-lambda
* aws-describe-ec2-instances


Notes:
* In order for RDP on Windows Server to work without providing credentials:
    1. Disable NLA
    2. Set 'security' parameter to 'tls'
    
* In order for SSH to work on Linux without providing credentials:
    1. Enable PasswordAuthentication on /etc/ssh/sshd_config
