module "guacamole_configuration_lambda" {
  source = "./modules/guacamole_configuration_lambda"

  owner = var.owner
  project = var.project
  custom_tags = var.custom_tags
  guacamole_configuration_lambda_env_vpc_ids_filter = module.demo_vpc.vpc_id
  guacamole_configuration_lambda_zip_path = "${path.module}/guacamole-config.zip"

}
