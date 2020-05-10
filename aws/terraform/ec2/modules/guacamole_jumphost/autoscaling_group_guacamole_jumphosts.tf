
locals {
  aws_autoscaling_group_tags = merge(local.common_tags, {
    "Name" = "${var.project}-guacamole-jumphost"
  })
}
resource "aws_autoscaling_group" "guacamole_jumphosts" {
  name = "${var.project}-guacamole-jumphost"
  max_size = var.guacamole_jumphosts_count
  min_size = 0
  desired_capacity = var.guacamole_jumphosts_count

  launch_template {
    id = aws_launch_template.guacamole_jumphosts.id
    version = "$Latest"
  }

  vpc_zone_identifier = var.allow_public_ssh ? var.public_subnet_ids : var.private_subnet_ids

  tags = [for key in keys(local.aws_autoscaling_group_tags):
  tomap({
    "key" = key
    "value" = local.aws_autoscaling_group_tags[key]
    propagate_at_launch = false
  })
  ]

}
