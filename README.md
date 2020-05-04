# Anyhow Authentication Extension for Apache Guacamole

[Apache Guacamole][apache-guacamole] is a remote administration tool that presents a web interface to control remote workloads using RDP, VNC or SSH. 

The Anyhow series of authentication extensions allow Apache Guacamole to retrieve a configuration written in JSON/YAML. 

It is not intended to perform authentication, but is to provide mechanisms for retrieving configurations.

The [Anyhow AWS Lambda authentication extension][guacamole-auth-anyhow-aws-lambda] is our first cloud service provider extension, and we intend to port the extension to other cloud service providers soon.
Please read the Anyhow AWS Lambda authentication extension [documentation][guacamole-auth-anyhow-aws-lambda-docs] for information regarding the various components in this repository and the deployment architecture for the Anyhow extensions.  

Please raise any issues in the issue tracker.

## Other Extensions  
The configurations can be retrieved through the following mechanisms using the appropriate extension:
* file - [guacamole-auth-anyhow-file][guacamole-auth-anyhow-file]
    * Read configuration from a local JSON/YAML file
* url - [guacamole-auth-anyhow-url][guacamole-auth-anyhow-url]
    * Read configuration from a JSON/YAML response from HTTP GET or POST requests to a URL     
* executable - [guacamole-auth-anyhow-executable][guacamole-auth-anyhow-executable]
    * Read configuration from a JSON/YAML output from a local executable script
* aws-lambda - [guacamole-auth-anyhow-aws-lambda][guacamole-auth-anyhow-aws-lambda]
    * Read configuration from a JSON/YAML response from invocation of an AWS Lambda
    * [Further documentation][guacamole-auth-anyhow-aws-lambda-docs]    
* aws-describe-ec2-instances - [guacamole-auth-anyhow-aws-describe-ec2-instances][guacamole-auth-anyhow-aws-describe-ec2-instances]
    * Read configuration from querying the AWS API for describing EC2 instances



## About Apache Guacamole
[Apache Guacamole's architecture][apache-guacamole-architecture] consists of a frontend Java servlet container and a backend daemon written in C.

The frontend Guacamole Client manages authentication and retrieval of remote workload connection information, presenting the authenticated remote administrator with a GUI to choose a connection.    

The frontend connects to the backend daemon and supplies connection parameters to configure remote administration session parameters.

Apache Guacamole is highly extensible through the creation of [custom authentication extensions][apache-guacamole-custom-auth], allowing different authentication and connection information retrieval mechanisms. 



Notes:
* [Remote Workload Configuration][remote-workload-configuration]


[apache-guacamole]: https://guacamole.apache.org/
[apache-guacamole-architecture]: https://guacamole.apache.org/doc/gug/guacamole-architecture.html
[apache-guacamole-custom-auth]: https://guacamole.apache.org/doc/gug/custom-auth.html
[remote-workload-configuration]: docs/remote-workload-configuration.md
[guacamole-auth-anyhow-file]: modules/guacamole-auth-anyhow-file
[guacamole-auth-anyhow-url]: modules/guacamole-auth-anyhow-url
[guacamole-auth-anyhow-executable]: modules/guacamole-auth-anyhow-executable
[guacamole-auth-anyhow-aws-lambda]: modules/guacamole-auth-anyhow-aws-lambda
[guacamole-auth-anyhow-aws-describe-ec2-instances]: modules/guacamole-auth-anyhow-aws-describe-ec2-instances
[guacamole-auth-anyhow-aws-lambda-docs]: docs/guacamole-auth-anyhow-aws-lambda-docs.md
