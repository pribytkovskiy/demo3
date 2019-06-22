output "public_ip" {
  value = "${google_compute_address.www-app.address}"
}
