output "project" {
  value = var.project
}
output "owner" {
  value = var.owner
}

output "module_demo_vpc" {
  value = module.demo_vpc
}

output "module_demo_centos_workloads" {
  value = module.demo_centos_workloads
}

output "key_name" {
  value = var.key_name
}