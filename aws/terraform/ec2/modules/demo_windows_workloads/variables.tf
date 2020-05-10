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

variable "key_name" {
  type = string
}

variable "key_path" {
  type = string
  default = null
}

variable "windows_workloads_count" {
  type = number
  default = 1
}

variable "security_group_ids" {
  type = list(string)
}

variable "subnet_ids" {
  type = list(string)
}
