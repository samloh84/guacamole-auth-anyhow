module "demo_centos_workloads" {
  source = "./modules/demo_centos_workloads"

  key_name = module.demo_vpc.key_name
  owner = var.owner
  project = var.project
  custom_tags = var.custom_tags

  centos_workloads_count = var.centos_workloads_count
  security_group_ids = [
    module.demo_vpc.private_security_group_id]
  subnet_ids = module.demo_vpc.private_subnet_ids
}
