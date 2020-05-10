output "project" {
  value = var.project
}
output "owner" {
  value = var.owner
}

output "vpc" {
  value = aws_vpc.vpc
}

output "vpc_id" {
  value = aws_vpc.vpc.id
}

output "private_security_group" {
  value = aws_security_group.private
}

output "private_security_group_id" {
  value = aws_security_group.private.id
}

output "public_security_group" {
  value = aws_security_group.public
}

output "public_security_group_id" {
  value = aws_security_group.public.id
}

output "private_1a_subnet" {
  value = aws_subnet.private_1a
}

output "private_1a_subnet_id" {
  value = aws_subnet.private_1a.id
}

output "private_1b_subnet" {
  value = aws_subnet.private_1b
}

output "private_1b_subnet_id" {
  value = aws_subnet.private_1b.id
}

output "private_subnet_ids" {
  value = [
    aws_subnet.private_1a.id,
    aws_subnet.private_1b.id]
}

output "public_1a_subnet" {
  value = aws_subnet.public_1a
}

output "public_1a_subnet_id" {
  value = aws_subnet.public_1a.id
}

output "public_1b_subnet" {
  value = aws_subnet.public_1b
}

output "public_1b_subnet_id" {
  value = aws_subnet.public_1b.id
}


output "public_subnet_ids" {
  value = [
    aws_subnet.public_1a.id,
    aws_subnet.public_1b.id]
}

output "key_name" {
  value = local.key_name
}

