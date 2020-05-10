# https://www.terraform.io/docs/providers/aws/r/lambda_function.html
resource "aws_lambda_function" "guacamole_configuration_lambda" {
  function_name = "${var.project}-guacamole-configuration-lambda"
  handler = "index.handler"
  role = aws_iam_role.lambda_guacamole_configuration_iam_role.arn
  runtime = "nodejs12.x"
  filename = var.guacamole_configuration_lambda_zip_path
  source_code_hash = filebase64sha256(var.guacamole_configuration_lambda_zip_path)
  timeout = 300

  environment {
    variables = {
      VPC_IDS = var.guacamole_configuration_lambda_env_vpc_ids_filter
    }
  }

  tags = merge(local.common_tags, {
    Name = "${var.project}-guacamole-configuration-lambda"
  })
}
