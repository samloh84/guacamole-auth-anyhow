// Find latest guacamole AMI
data "aws_ami" "guacamole_jumphost" {
  most_recent = true

  filter {
    name = "tag:Name"
    values = [
      "${var.project}-guacamole-jumphost"]
  }

  filter {
    name = "tag:Project"
    values = [
      var.project]
  }

  owners = [
    "self"
  ]
}
