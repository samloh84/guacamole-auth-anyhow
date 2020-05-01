// Find latest Centos AMI
data "aws_ami" "guacamole_jumphost" {
  most_recent = true

  filter {
    name = "tag:Name"
    values = [
      "guacamole-jumphost"]
  }


  owners = [
    "self"
  ]
}
