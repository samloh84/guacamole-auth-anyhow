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

variable "openid_authorization_endpoint" {
  type = string
}
variable "openid_jwks_endpoint" {
  type = string
}
variable "openid_issuer" {
  type = string
}
variable "openid_client_id" {
  type = string
}
variable "openid_redirect_uri" {
  type = string
  default = null
}

variable "anyhow_aws_region" {
  type = string
  default = "ap-southeast-1"
}

variable "logback_level" {
  description = "Possible values: TRACE, DEBUG, INFO, WARN, ERROR"
  type = string
  default = "INFO"
}

variable "lambda_guacamole_configuration_arn" {
  type = string
}

variable "guacamole_jumphosts_count" {
  type = number
}

variable "key_name" {
  type = string
}

variable "key_path" {
  type = string
  default = null
}

variable "private_security_group_ids" {
  type = list(string)
}

variable "public_security_group_ids" {
  type = list(string)
}

variable "public_subnet_ids" {
  type = list(string)
}

variable "private_subnet_ids" {
  type = list(string)
}

variable "public_zone_name" {
  type = string
}

variable "vpc_id" {
  type = string
}

variable "allow_public_ssh" {
  type = bool
  default = false
}


variable "anyhow_aws_lambda_function" {
  type = string
}
