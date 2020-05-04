# Anyhow AWS Lambda Guacamole Authentication Extension

The [Anyhow AWS Lambda Guacamole Authentication Extension][guacamole-auth-anyhow-aws-lambda] is intended for use with the [OpenID authentication extension][openid-auth]. 

![Anyhow AWS Architecture][anyhow-aws-architecture]

In the above architecture diagram, the Guacamole client is configured to leverage Azure AD OIDC flow for authentication, and an AWS Lambda for connection information.

Provided in this repository are the following components:
* [Dockerfile][anyhow-docker-guacamole-client] for building the Anyhow Guacamole Client Docker image containing the Apache Guacamole client and the Anyhow authentication extensions
* [AWS Lambda source files][anyhow-aws-lambda] for building the AWS Lambda zip
* [AWS Packer script][anyhow-aws-packer] for building the Anyhow Guacamole Client EC2 instance with the Apache Guacamole Server and Anyhow Guacamole Client Docker images predownloaded
* [AWS Terraform module][anyhow-aws-terraform-ec2] for deploying the Guacamole Client EC2 instance to AWS
* [AWS Terraform module][anyhow-aws-terraform-fargate] for deploying the Guacamole Client Fargate instance to AWS


[openid-auth]: https://guacamole.apache.org/doc/gug/openid-auth.html
[anyhow-aws-architecture]: anyhow-aws-architecture.svg
[anyhow-docker-guacamole-client]: ../docker/guacamole-client
[anyhow-aws-lambda]: ../aws/lambda
[anyhow-aws-packer]: ../aws/packer
[anyhow-aws-terraform-ec2]: ../aws/terraform/ec2
[anyhow-aws-terraform-fargate]: ../aws/terraform/fargate
[guacamole-auth-anyhow-aws-lambda]: ../modules/guacamole-auth-anyhow-aws-lambda
