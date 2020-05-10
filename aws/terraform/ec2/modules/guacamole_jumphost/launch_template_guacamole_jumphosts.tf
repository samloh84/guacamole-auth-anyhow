resource "aws_launch_template" "guacamole_jumphosts" {
  name = "${var.project}-guacamole-jumphost"
  image_id =  data.aws_ami.guacamole_jumphost.id
  instance_type = "t3.small"

  vpc_security_group_ids = var.allow_public_ssh ? var.public_security_group_ids : var.private_security_group_ids

  key_name = local.key_name

  iam_instance_profile {
    name = aws_iam_instance_profile.guacamole_jumphost_instance_profile.name
  }


  credit_specification {
    cpu_credits = "unlimited"
  }

  user_data = base64encode(local.user_data_yml)

  tag_specifications {
    resource_type = "instance"
    tags = merge(local.common_tags, {
      Name = "${var.project}-guacamole-jumphost"
      "Custodian-Scheduler-StopTime" = "off=();tz=sgt"
    })
  }

  tag_specifications {
    resource_type = "volume"
    tags = merge(local.common_tags, {
      Name = "${var.project}-guacamole-jumphost"
    })
  }

  tags = merge(local.common_tags, {
    Name = "${var.project}-guacamole-jumphost"
  })
}