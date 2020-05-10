locals {
  common_tags = merge({
    Owner = var.owner
    Project = var.project
  }, var.custom_tags)
}