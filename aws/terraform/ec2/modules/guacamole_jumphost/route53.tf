data aws_route53_zone public {
  private_zone = false
  name = var.public_zone_name
}


resource "aws_route53_record" "guacamole_hostname" {

  zone_id = data.aws_route53_zone.public.id
  name = "guacamole.${trimsuffix(data.aws_route53_zone.public.name,".")}"
  type = "A"
  alias {
    evaluate_target_health = true
    name = aws_lb.guacamole.dns_name
    zone_id = aws_lb.guacamole.zone_id
  }
}


