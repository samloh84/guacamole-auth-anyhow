variable "project" {
  type = string
}
variable "owner" {
  type = string
}

variable "custom_tags" {
  type = map(string)
  default = {}
}

variable "guacamole_configuration_lambda_env_vpc_ids_filter" {
  type = string
  default = ""
}

variable "guacamole_configuration_lambda_zip_path" {
  type = string
  default = "guacamole-config.zip"
}

