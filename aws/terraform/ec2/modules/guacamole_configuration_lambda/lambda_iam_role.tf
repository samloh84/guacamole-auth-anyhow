resource "aws_iam_role" "lambda_guacamole_configuration_iam_role" {
  name = "${var.project}-lambda-guacamole-config-iam-role"
  path = "/${var.project}/"

  assume_role_policy = file("${path.module}/iam_lambda_assume_role_policy.json")

  tags = merge(local.common_tags, {
    Name = "${var.project}-guacamole-configuration-lambda-iam-role"
  })
}

resource "aws_iam_policy" "lambda_guacamole_configuration_iam_policy_cloudwatch_logging" {
  name = "${var.project}-lambda-guacamole-config-iam-policy-cloudwatch-logging"
  path = "/${var.project}/"

  policy = file("${path.module}/iam_policy_cloudwatch_logging.json")
}

resource "aws_iam_role_policy_attachment" "lambda_guacamole_configuration_iam_policy_cloudwatch_logging" {
  role = aws_iam_role.lambda_guacamole_configuration_iam_role.name
  policy_arn = aws_iam_policy.lambda_guacamole_configuration_iam_policy_cloudwatch_logging.arn
}


resource "aws_iam_policy" "lambda_guacamole_configuration_iam_policy_describe_ec2" {
  name = "${var.project}-lambda-guacamole-config-iam-policy-describe-ec2"
  path = "/${var.project}/"

  policy = file("${path.module}/iam_policy_describe_ec2.json")
}

resource "aws_iam_role_policy_attachment" "lambda_guacamole_configuration_iam_policy_ec2_describe_permissions" {
  role = aws_iam_role.lambda_guacamole_configuration_iam_role.name
  policy_arn = aws_iam_policy.lambda_guacamole_configuration_iam_policy_describe_ec2.arn
}
