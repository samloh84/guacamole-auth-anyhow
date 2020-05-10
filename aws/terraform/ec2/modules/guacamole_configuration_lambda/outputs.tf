output "guacamole_configuration_lambda" {
  value = aws_lambda_function.guacamole_configuration_lambda
}


output "guacamole_configuration_lambda_function_arn" {
  value = aws_lambda_function.guacamole_configuration_lambda.arn
}

output "guacamole_configuration_lambda_function_name" {
  value = aws_lambda_function.guacamole_configuration_lambda.function_name
}

output "lambda_guacamole_configuration_iam_role" {
  value = aws_iam_role.lambda_guacamole_configuration_iam_role
}

output "lambda_guacamole_configuration_iam_policy_cloudwatch_logging" {
  value = aws_iam_policy.lambda_guacamole_configuration_iam_policy_cloudwatch_logging
}

output "lambda_guacamole_configuration_iam_policy_describe_ec2" {
  value = aws_iam_policy.lambda_guacamole_configuration_iam_policy_describe_ec2
}
