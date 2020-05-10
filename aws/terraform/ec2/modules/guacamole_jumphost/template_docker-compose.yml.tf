locals {
  openid_redirect_uri = var.openid_redirect_uri != null && var.openid_redirect_uri != "" ? var.openid_redirect_uri : "https://${aws_route53_record.guacamole_hostname.fqdn}/guacamole"
  docker_compose_yml = templatefile("${path.module}/template_docker-compose.yml", {
    OPENID_AUTHORIZATION_ENDPOINT = var.openid_authorization_endpoint
    OPENID_JWKS_ENDPOINT = var.openid_jwks_endpoint
    OPENID_ISSUER = var.openid_issuer
    OPENID_CLIENT_ID = var.openid_client_id
    OPENID_REDIRECT_URI = local.openid_redirect_uri
    ANYHOW_AWS_REGION = var.anyhow_aws_region
    LOGBACK_LEVEL = var.logback_level
    ANYHOW_AWS_LAMBDA_FUNCTION=var.anyhow_aws_lambda_function
    GUACAMOLE_REPOSITORY_URL="${data.aws_ecr_repository.guacamole_client_repository.repository_url}:latest"
  })
}
