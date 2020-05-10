module "demo_windows_workloads" {
  source = "./modules/demo_windows_workloads"

  key_name = module.demo_vpc.key_name
  owner = var.owner
  project = var.project
  custom_tags = var.custom_tags

  windows_workloads_count = var.windows_workloads_count
  security_group_ids = [
    module.demo_vpc.private_security_group_id]
  subnet_ids = module.demo_vpc.private_subnet_ids
}
