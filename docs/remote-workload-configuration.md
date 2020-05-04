# Remote Workload Configuration

## RDP on Windows
Network Level Authentication is enabled by default on Windows 10, Windows Server operating systems. 
If your connection information does not supply user credentials to Apache Guacamole, 
Apache Guacamole will not be able to connect to the Windows remote workload.

In order for RDP on Windows to work without providing credentials:
    1. Disable NLA
    2. Set 'security' parameter to 'tls'
    

## SSH on Linux
`PasswordAuthentication` may be disabled by default on Linux instances hosted by Cloud service providers.  
If your connection information does not supply user credentials to Apache Guacamole, no password credentials will be accepted by the Linux remote workload.

In order for RDP on Linux to work without providing credentials:
1. Enable `PasswordAuthentication on` /etc/ssh/sshd_config
