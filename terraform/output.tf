output "public_ip" {
  value = "${google_compute_address.front-pool.address}"
}
