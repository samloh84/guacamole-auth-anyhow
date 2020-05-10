locals {
  user_data_yml = templatefile("${path.module}/template_user_data.yml",
  {
    GUACAMOLE_DOCKER_COMPOSE_YML = base64encode(local.docker_compose_yml)
  })
}
