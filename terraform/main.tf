provider "google" {
  credentials = "${file("${var.service_account_key_path}")}"
  project = "${var.project}"
  region  = "${var.region}"
  zone    = "${var.zone}"
}

#Front

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
  count              = 2
  name               = "front-group"
  description        = "Terraform front instance group manager"
  instance_template  = "${google_compute_instance_template.front_template.self_link}"
  base_instance_name = "front-${count.index + 1}"
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
  region  = "europe-west6"
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



#Firewall

resource "google_compute_firewall" "front-open-all-in-80" {
  name    = "front-open-all-in-80"
  network = "default"

  allow {
    protocol = "tcp"
    ports    = ["80"]
  }

  source_ranges = ["0.0.0.0/0"]
  target_tags   = ["front"]
}

resource "google_compute_firewall" "back-open-front-in-8080" {
  name    = "back-open-front-in-8080"
  network = "default"

  allow {
    protocol = "tcp"
    ports    = ["8080"]
  }

  source_tags = ["front"]
  target_tags   = ["back"]
}
