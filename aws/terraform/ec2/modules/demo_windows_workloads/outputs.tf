output "project" {
  value = var.project
}
output "owner" {
  value = var.owner
}

output "autoscaling_group_windows_workloads" {
  value = aws_autoscaling_group.windows_workloads
}

output "autoscaling_group_windows_workloads_id" {
  value = aws_autoscaling_group.windows_workloads.id
}

output "launch_template_windows_workloads" {
  value = aws_launch_template.windows_workloads
}

output "launch_template_windows_workloads_id" {
  value = aws_launch_template.windows_workloads.id
}


output "key_name" {
  value = local.key_name
}

