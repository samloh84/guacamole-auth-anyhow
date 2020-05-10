resource "aws_key_pair" "key_pair" {
  count = var.key_path != null && var.key_path != "" ? 1 : 0
  key_name = local.key_name
  public_key = file(var.key_path)

  tags = merge(local.common_tags, {
    Name = "${var.project}-key"
  })
}

locals {
  key_name = var.key_name != null && var.key_name != "" ? var.key_name: "${var.project}-key"
}