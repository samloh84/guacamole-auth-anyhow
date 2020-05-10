resource "aws_ecr_repository" "ecr" {
  name = "${var.project}-guacamole-client"
  image_scanning_configuration  {
    scan_on_push = true
  }

  tags = merge(local.common_tags, {
    Name = "${var.project}-guacamole-client-repository"
  })
}