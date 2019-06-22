# Front

resource "google_compute_instance_template" "front_template" {
  machine_type = "g1-small"
  tags = ["front-group"]

  network_interface {
    network = "default"
    access_config {
    }
  }

  disk {
    auto_delete  = true
    boot         = true
    source_image = "ubuntu-os-cloud/ubuntu-1604-lts"
  }
}

resource "google_compute_instance_group_manager" "front-group" {
  name               = "front-group"
  description        = "Terraform front instance group manager"
  instance_template  = "${google_compute_instance_template.front_template.self_link}"
  base_instance_name = "front"
  zone               = "${var.zone}"

  target_size        = 2
  target_pools       = ["${google_compute_target_pool.front-pool.self_link}"]

  named_port {
    name = "front-port"
    port = 3000
  }
}

resource "google_compute_target_pool" "front-pool" {
  name    = "front-pool"
  region  = "${var.region}"
}