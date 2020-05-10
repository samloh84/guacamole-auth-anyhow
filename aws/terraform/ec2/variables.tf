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

variable "vpc_cidr" {
  type = string
  default = "192.168.0.0/16"
}

variable "whitelisted_cidrs" {
  type = list(string)
  default = []
}

variable "centos_workloads_count" {
  type = number
  default = 1
}

variable "windows_workloads_count" {
  type = number
  default = 1
}

variable "key_name" {
  type = string
}

variable "key_path" {
  type = string
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
variable "public_zone_name" {
  type = string
}


variable "allow_public_ssh" {
  type = bool
  default = true
}

variable "guacamole_jumphosts_count" {
  type = number
  default = 1
}