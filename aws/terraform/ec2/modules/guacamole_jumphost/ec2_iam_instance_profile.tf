
resource "aws_iam_role" "guacamole_jumphost_iam_role" {
  name = "guacamole_jumphost_iam_role"
  path = "/${var.project}/"
  assume_role_policy = file("${path.module}/iam_ec2_assume_role_policy.json")

  tags = merge(local.common_tags, {
    Name = "${var.project}-guacamole-configuration-lambda-iam-role"
  })
}

resource "aws_iam_policy" "guacamole_jumphost_iam_policy_invoke_lambda_guacamole_config" {
  name = "guacamole_jumphost_iam_policy_invoke_lambda_guacamole_config"
  path = "/${var.project}/"
  policy = local.iam_policy_invoke_lamda_json
}


resource "aws_iam_role_policy_attachment" "guacamole_jumphost_iam_policy_invoke_lambda_guacamole_config" {
  role = aws_iam_role.guacamole_jumphost_iam_role.name
  policy_arn = aws_iam_policy.guacamole_jumphost_iam_policy_invoke_lambda_guacamole_config.arn
}

resource "aws_iam_instance_profile" "guacamole_jumphost_instance_profile" {
  name = "guacamole_jumphost_instance_profile"
  path = "/${var.project}/"
  role = aws_iam_role.guacamole_jumphost_iam_role.name
}
