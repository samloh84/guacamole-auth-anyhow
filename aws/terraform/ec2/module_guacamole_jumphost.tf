module "guacamole_jumphost" {
  source = "./modules/guacamole_jumphost"

  owner = var.owner
  project = var.project
  custom_tags = var.custom_tags
  guacamole_jumphosts_count = var.guacamole_jumphosts_count

  key_name = module.demo_vpc.key_name
  lambda_guacamole_configuration_arn = module.guacamole_configuration_lambda.guacamole_configuration_lambda_function_arn
  openid_authorization_endpoint = var.openid_authorization_endpoint
  openid_client_id = var.openid_client_id
  openid_issuer = var.openid_issuer
  openid_jwks_endpoint = var.openid_jwks_endpoint
  private_security_group_ids = [
    module.demo_vpc.private_security_group_id]
  public_security_group_ids = [
    module.demo_vpc.public_security_group_id]
  private_subnet_ids = module.demo_vpc.private_subnet_ids
  public_subnet_ids = module.demo_vpc.public_subnet_ids
  public_zone_name = var.public_zone_name
  vpc_id = module.demo_vpc.vpc_id
  allow_public_ssh = var.allow_public_ssh
  anyhow_aws_region = var.anyhow_aws_region
  logback_level = var.logback_level
  openid_redirect_uri = var.openid_redirect_uri
  anyhow_aws_lambda_function = module.guacamole_configuration_lambda.guacamole_configuration_lambda_function_name
}
