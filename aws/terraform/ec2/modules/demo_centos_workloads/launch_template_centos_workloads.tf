
resource "aws_launch_template" "centos_workloads" {
  image_id = data.aws_ami.centos.id
  instance_type = "t3.micro"
  vpc_security_group_ids = var.security_group_ids

  key_name = local.key_name

  credit_specification {
    cpu_credits = "unlimited"
  }

  //  user_data = <<-EOF
  //#cloud-config
  //runcmd:
  //  - cloud-init-per once allow_public_sshd_password_authentication sed -e "s:#PasswordAuthentication yes:PasswordAuthentication yes:g /etc/ssh/sshd_config
  //  - cloud-init-per once change_password usermod --password $(openssl passwd -1 ${var.password}) centos
  //EOF

  tag_specifications {
    resource_type = "instance"
    tags = merge(local.common_tags, {
      Name = "${var.project}-centos-workload"
      "Custodian-Scheduler-StopTime" = "off=();tz=sgt"
    })
  }

  tag_specifications {
    resource_type = "volume"
    tags = merge(local.common_tags, {
      Name = "${var.project}-centos-workload"
    })
  }

  tags = merge(local.common_tags, {
    Name = "${var.project}-centos-workload"
  })
}