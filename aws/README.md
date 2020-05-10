# AWS Specific Scripts

## Components

* Guacamole Configuration AWS Lambda JavaScript Source
* Packer Scripts
    * Guacamole Jumphost
* Terraform Scripts
    * ECR
    * EC2
    



## Build Order

1. Anyhow Java Extensions (`project_dir/`)
2. Anyhow Guacamole Client Docker Image (`project_dir/docker/guacamole-client`)    
3. Anyhow Guacamole Configuration AWS Lambda (`project_dir/aws/lambda`)
4. ECR AWS Terraform Script (`project_dir/aws/terraform/ecr`)
5. Guacamole Jumphost AMI Packer Script (`project_dir/aws/packer/guacamole-jumphost`)
6. EC2 AWS Terraform Script (`project_dir/aws/terraform/ec2`)
