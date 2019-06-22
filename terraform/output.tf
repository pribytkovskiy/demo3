output "public_ip" {
  value = "${google_compute_address.front-pool.address}"
}

output "instance_front_pool_ips" {
  value = "${join(" ", google_compute_instance.front.*.network_interface.0.access_config.0.assigned_nat_ip)}"
}