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
