resource "aws_vpc" "vpc" {
  cidr_block = var.vpc_cidr
  enable_dns_hostnames = true
  enable_dns_support = true

  tags = merge(local.common_tags, {
    Name = "${var.project}-vpc"
  })

}


resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.vpc.id

  tags = merge(local.common_tags, {
    Name = "${var.project}-internet-gateway"
  })


}

locals {
  subnet_cidrs = cidrsubnets(var.vpc_cidr, 8, 8, 8, 8)
}
