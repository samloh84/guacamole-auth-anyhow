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

variable "key_name" {
  type = string
}

variable "key_path" {
  type = string
  default = null
}

variable "allow_public_ssh" {
  type = bool
  default = false
}
