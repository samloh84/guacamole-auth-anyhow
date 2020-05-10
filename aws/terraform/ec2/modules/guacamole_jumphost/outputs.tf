output "project" {
  value = var.project
}
output "owner" {
  value = var.owner
}

output "autoscaling_group_guacamole_jumphosts" {
  value = aws_autoscaling_group.guacamole_jumphosts
}

output "autoscaling_group_guacamole_jumphosts_id" {
  value = aws_autoscaling_group.guacamole_jumphosts.id
}

output "launch_template_guacamole_jumphosts" {
  value = aws_launch_template.guacamole_jumphosts
}

output "launch_template_guacamole_jumphosts_id" {
  value = aws_launch_template.guacamole_jumphosts.id
}


output "key_name" {
  value = local.key_name
}



output "key_path" {
  value = var.key_path
}


