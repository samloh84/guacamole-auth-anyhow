
locals {
  aws_autoscaling_group_tags = merge(local.common_tags, {
    "Name" = "${var.project}-centos-workload"
  })
}
resource "aws_autoscaling_group" "centos_workloads" {
  name = "${var.project}-centos-workload"
  max_size = var.centos_workloads_count
  min_size = 0
  desired_capacity = var.centos_workloads_count

  launch_template {
    id = aws_launch_template.centos_workloads.id
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
