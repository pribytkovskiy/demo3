provider "google" {
  credentials = "${file("${var.service_account_key_path}")}"
  project = "${var.project}"
  region  = "${var.region}"
  zone    = "${var.zone}"
}

#Front

resource "google_compute_instance_template" "front_template" {
  machine_type = "g1-small"
  tags = ["front"]

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

  metadata = {
    ssh-keys = "root:${file("${var.public_key_path}")}"
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
    name = "http"
    port = 3000
  }
}

resource "google_compute_target_pool" "front-pool" {
  name    = "front-pool"
  region  = "${var.region}"

  health_checks = [
    "${google_compute_http_health_check.front-health-check.name}",
  ]
}

resource "google_compute_http_health_check" "front-health-check" {
  port               = 3000
  name               = "front-health-check"
  request_path       = "/"
  check_interval_sec = 60
  timeout_sec        = 10
}

#Back

resource "google_compute_instance" "back" {
  name         = "back"
  machine_type = "g1-small"
  zone         = "${var.zone}"

  tags = ["back"]

  boot_disk {
    auto_delete  = true
    initialize_params {
      image = "ubuntu-os-cloud/ubuntu-1604-lts"
      size = 10
    }
  }

  network_interface {
    network = "default"

    access_config {
    }
  }

  metadata = {
    ssh-keys = "root:${file("${var.public_key_path}")}"
  }
}

#Database

resource "google_sql_user" "root" {
  name     = "root"
  instance = "db11"
  host     = "%"
  password = "root"
}

#Firewall

resource "google_compute_firewall" "front-open-all-in-3000" {
  name    = "front-open-all-in-3000"
  network = "default"

  allow {
    protocol = "tcp"
    ports    = ["3000"]
  }

  source_ranges = ["0.0.0.0/0"]
  target_tags   = ["front"]
}

resource "google_compute_firewall" "back-open-all-in-8080" {
  name    = "back-open-all-in-8080"
  network = "default"

  allow {
    protocol = "tcp"
    ports    = ["8080"]
  }

  source_ranges = ["0.0.0.0/0"]
  target_tags   = ["back"]
}

resource "google_compute_address" "www-app" {
    name = "www-app"
}

resource "google_compute_forwarding_rule" "front-pool" {
  name = "front-pool"
  target = "${google_compute_target_pool.front-pool.self_link}"
  ip_address = "${google_compute_address.www-app.address}"
  port_range = "3000"
}
