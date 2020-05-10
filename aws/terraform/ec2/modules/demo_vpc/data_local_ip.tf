data "http" "local_ip" {
  url = "https://ipv4.icanhazip.com/"
}

locals {
  local_cidr = "${chomp(data.http.local_ip.body)}/32"
}
