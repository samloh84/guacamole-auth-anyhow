output "project" {
  value = var.project
}
output "owner" {
  value = var.owner
}

output "ecr" {
  value = aws_ecr_repository.ecr
}

output "ecr_repository_url" {
  value = aws_ecr_repository.ecr.repository_url
}
