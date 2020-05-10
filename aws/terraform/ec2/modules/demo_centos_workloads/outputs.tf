output "project" {
  value = var.project
}
output "owner" {
  value = var.owner
}

output "autoscaling_group_centos_workloads" {
  value = aws_autoscaling_group.centos_workloads
}

output "autoscaling_group_centos_workloads_id" {
  value = aws_autoscaling_group.centos_workloads.id
}

output "launch_template_centos_workloads" {
  value = aws_launch_template.centos_workloads
}

output "launch_template_centos_workloads_id" {
  value = aws_launch_template.centos_workloads.id
}


output "key_name" {
  value = local.key_name
}

