resource "aws_acm_certificate" "guacamole_cert" {
  domain_name = aws_route53_record.guacamole_hostname.fqdn
  validation_method = "DNS"
  lifecycle {
    create_before_destroy = true
  }
}


resource "aws_route53_record" "guacamole_cert_validation" {
  name = aws_acm_certificate.guacamole_cert.domain_validation_options[0].resource_record_name
  type = aws_acm_certificate.guacamole_cert.domain_validation_options[0].resource_record_type
  zone_id = data.aws_route53_zone.public.id
  records = [
    aws_acm_certificate.guacamole_cert.domain_validation_options[0].resource_record_value]
  ttl = 60
}


resource "aws_acm_certificate_validation" "guacamole_cert" {
  certificate_arn = aws_acm_certificate.guacamole_cert.arn
  validation_record_fqdns = [
    aws_route53_record.guacamole_cert_validation.fqdn]
}