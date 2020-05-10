resource "aws_subnet" "public_1a" {
  cidr_block = local.subnet_cidrs[0]
  availability_zone = "ap-southeast-1a"
  map_public_ip_on_launch = true

  tags = merge(local.common_tags, {
    Name = "${var.project}-private-1a-subnet"
  })

  vpc_id = aws_vpc.vpc.id
}
resource "aws_subnet" "public_1b" {
  cidr_block = local.subnet_cidrs[1]
  availability_zone = "ap-southeast-1b"
  map_public_ip_on_launch = true

  tags = merge(local.common_tags, {
    Name = "${var.project}-public-1b-subnet"
  })

  vpc_id = aws_vpc.vpc.id
}


resource "aws_eip" "nat_ip" {
  vpc = true
}

resource "aws_nat_gateway" "nat" {
  allocation_id = aws_eip.nat_ip.id
  subnet_id = aws_subnet.public_1a.id

  tags = merge(local.common_tags, {
    Name = "${var.project}-nat-gateway"
  })

}

resource "aws_route_table" "public" {
  vpc_id = aws_vpc.vpc.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }

  tags = merge(local.common_tags, {
    Name = "${var.project}-public-route-table"
  })
}

resource "aws_route_table_association" "public_1a" {
  route_table_id = aws_route_table.public.id
  subnet_id = aws_subnet.public_1a.id
}


resource "aws_route_table_association" "public_1b" {
  route_table_id = aws_route_table.public.id
  subnet_id = aws_subnet.public_1b.id
}

