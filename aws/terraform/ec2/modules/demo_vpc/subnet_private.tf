resource "aws_subnet" "private_1a" {
  cidr_block = local.subnet_cidrs[2]
  availability_zone = "ap-southeast-1a"
  map_public_ip_on_launch = false

  tags = merge(local.common_tags, {
    Name = "${var.project}-private-1a-subnet"
  })

  vpc_id = aws_vpc.vpc.id
}
resource "aws_subnet" "private_1b" {
  cidr_block = local.subnet_cidrs[3]
  availability_zone = "ap-southeast-1b"
  map_public_ip_on_launch = false

  tags = merge(local.common_tags, {
    Name = "${var.project}-private-1b-subnet"
  })

  vpc_id = aws_vpc.vpc.id
}

resource "aws_route_table" "private" {
  vpc_id = aws_vpc.vpc.id
  route {
    cidr_block = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.nat.id
  }

  tags = merge(local.common_tags, {
    Name = "${var.project}-private-route-table"
  })

}

resource "aws_route_table_association" "private_1a" {
  route_table_id = aws_route_table.private.id
  subnet_id = aws_subnet.private_1a.id
}


resource "aws_route_table_association" "private_1b" {
  route_table_id = aws_route_table.private.id
  subnet_id = aws_subnet.private_1b.id
}


