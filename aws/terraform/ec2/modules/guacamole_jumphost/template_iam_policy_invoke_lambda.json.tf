locals {
  iam_policy_invoke_lamda_json = templatefile("${path.module}/template_iam_policy_invoke_lambda.json",
  {
    LAMBDA_GUACAMOLE_CONFIGURATION_ARN = var.lambda_guacamole_configuration_arn
  })
}
