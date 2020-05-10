
locals {
  aws_autoscaling_group_tags = merge(local.common_tags, {
    "Name" = "${var.project}-windows-workload"
  })
}
resource "aws_autoscaling_group" "windows_workloads" {
  name = "${var.project}-windows-workload"
  max_size = var.windows_workloads_count
  min_size = 0
  desired_capacity = var.windows_workloads_count

  launch_template {
    id = aws_launch_template.windows_workloads.id
  }

  vpc_zone_identifier = var.subnet_ids

  tags = [for key in keys(local.aws_autoscaling_group_tags):
  tomap({
    "key" = key
    "value" = local.aws_autoscaling_group_tags[key]
    propagate_at_launch = false
  })
  ]

}
