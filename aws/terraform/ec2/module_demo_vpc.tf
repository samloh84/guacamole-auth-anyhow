module "demo_vpc" {
  source = "./modules/demo_vpc"

  key_name = var.key_name
  key_path = var.key_path
  owner = var.owner
  project = var.project
  custom_tags = var.custom_tags
  vpc_cidr = var.vpc_cidr
  whitelisted_cidrs = var.whitelisted_cidrs
  allow_public_ssh = var.allow_public_ssh

}
