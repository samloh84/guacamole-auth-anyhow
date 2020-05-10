

resource "aws_lb" "guacamole" {
  name = "guacamole-lb-tf"
  internal = false
  load_balancer_type = "application"
  security_groups =var.public_security_group_ids
  subnets = var.public_subnet_ids

}

resource "aws_lb_target_group" "guacamole" {
  name = "guacamole-lb-tg"
  port = 8080
  protocol = "HTTP"
  vpc_id = var.vpc_id

}

resource "aws_autoscaling_attachment" "guacamole" {
  autoscaling_group_name = aws_autoscaling_group.guacamole_jumphosts.name
  alb_target_group_arn = aws_lb_target_group.guacamole.arn
}

resource "aws_lb_listener" "guacamole" {
  load_balancer_arn = aws_lb.guacamole.arn
  port = "443"
  protocol = "HTTPS"
  ssl_policy = "ELBSecurityPolicy-2016-08"
  certificate_arn = aws_acm_certificate_validation.guacamole_cert.certificate_arn


  default_action {
    type = "forward"
    target_group_arn = aws_lb_target_group.guacamole.arn
  }
}
