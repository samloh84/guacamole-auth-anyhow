resource "aws_vpc" "vpc" {
  cidr_block = "10.0.0.0/16"
  enable_dns_hostnames = true
  enable_dns_support = true

  tags = {
    Name = "guacamole-vpc"
    Project = var.project
    Owner = var.owner
  }
}


resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.vpc.id
  tags = {
    Name = "guacamole"
    Project = var.project
    Owner = var.owner
  }
}
