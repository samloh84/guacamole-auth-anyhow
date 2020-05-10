
resource "aws_launch_template" "windows_workloads" {
  image_id = data.aws_ami.windows_server.id
  instance_type = "t3.micro"
  vpc_security_group_ids = var.security_group_ids

  key_name = local.key_name

  credit_specification {
    cpu_credits = "unlimited"
  }

  //  user_data = <<-EOF
  //#cloud-config
  //script:
  //  - |
  //    <powershell>
  //    (Get-WmiObject -class "Win32_TSGeneralSetting" -Namespace root\cimv2\terminalservices -ComputerName $server -Filter "TerminalName='RDP-tcp'").SetUserAuthenticationRequired(0)
  //    </powershell>
  //  - net user administrator "${var.password}"
  //EOF


  tag_specifications {
    resource_type = "instance"
    tags = merge(local.common_tags, {
      Name = "${var.project}-windows-workload"
      "Custodian-Scheduler-StopTime" = "off=();tz=sgt"
    })
  }

  tag_specifications {
    resource_type = "volume"
    tags = merge(local.common_tags, {
      Name = "${var.project}-windows-workload"
    })
  }

  tags = merge(local.common_tags, {
    Name = "${var.project}-windows-workload"
  })
}