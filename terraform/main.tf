provider "google" {
  credentials = "${file("${var.service_account_key_path}")}"
  project = "${var.project}"
  region  = "${var.region}"
  zone    = "${var.zone}"
}

#Front

resource "google_compute_instance" "front-1" {
  name         = "front-1"
  machine_type = "g1-small"
  zone         = "${var.zone}"

  tags = ["front"]

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

resource "google_compute_instance" "front-2" {
  name         = "front-2"
  machine_type = "g1-small"
  zone         = "${var.zone}"

  tags = ["front"]

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

resource "google_compute_instance_group" "front-group" {
  name        = "front-group"
  description = "Front instance group"

  instances = [
    "${google_compute_instance.front-1.self_link}",
    "${google_compute_instance.front-2.self_link}"
  ]

  named_port {
    name = "http"
    port = "3000"
  }

  zone = "${var.zone}"
}

resource "google_compute_target_pool" "front-pool" {
  name = "front-pool"

  instances = [
    "${google_compute_instance.front-1.self_link}",
    "${google_compute_instance.front-2.self_link}"
  ]

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

# resource "google_compute_instance_template" "front_template" {
#   machine_type = "g1-small"
#   tags = ["front-group"]

#   network_interface {
#     network = "default"
#     access_config {
#     }
#   }

#   disk {
#     auto_delete  = true
#     boot         = true
#     source_image = "ubuntu-os-cloud/ubuntu-1604-lts"
#   }
# }

# resource "google_compute_instance_group_manager" "front-group" {
#   name               = "front-group"
#   description        = "Terraform front instance group manager"
#   instance_template  = "${google_compute_instance_template.front_template.self_link}"
#   base_instance_name = "front"
#   zone               = "${var.zone}"

#   target_size        = 2
#   target_pools       = ["${google_compute_target_pool.front-pool.self_link}"]

#   named_port {
#     name = "front-port"
#     port = 3000
#   }
# }

# resource "google_compute_target_pool" "front-pool" {
#   name    = "front-pool"
#   region  = "${var.region}"
# }

#Back

# resource "google_compute_instance" "back" {
#   name         = "back"
#   machine_type = "g1-small"
#   zone         = "${var.zone}"

#   tags = ["back"]

#   boot_disk {
#     auto_delete  = true
#     initialize_params {
#       image = "ubuntu-os-cloud/ubuntu-1604-lts"
#       size = 10
#     }
#   }

#   network_interface {
#     network = "default"

#     access_config {
#     }
#   }

#   metadata = {
#     ssh-keys = "root:${file("${var.public_key_path}")}"
#   }
# }

#Database

# resource "random_id" "db_name_suffix" {
#   byte_length = 4
# }

# resource "google_sql_database_instance" "db1" {
#   name = "db1"
#   database_version = "MYSQL_5_6"
#   region = "${var.region}"

#   settings {
#     tier = "db-n1-standard-2"
#   }
# }

# resource "google_sql_database" "db1" {
#   name      = "bike_championship"
#   instance  = "db1"
# }

# resource "google_sql_user" "root" {
#   name     = "root"
#   instance = "db1"
#   host     = "%"
#   password = "root"
# }

#Firewall

resource "google_compute_firewall" "front-open-all-in-3000" {
  name    = "front-open-all-in-3000"
  network = "default"

  allow {
    protocol = "tcp"
    ports    = ["3000"]
  }

  source_ranges = ["0.0.0.0/0"]
  target_tags   = ["www-app"]
}

# resource "google_compute_firewall" "back-open-front-in-8080" {
#   name    = "back-open-front-in-8080"
#   network = "default"

#   allow {
#     protocol = "tcp"
#     ports    = ["8080"]
#   }

#   source_tags = ["front"]
#   target_tags   = ["back"]
# }

resource "google_compute_address" "www-app" {
    name = "www-app"
}

resource "google_compute_forwarding_rule" "front-pool" {
  name = "front-pool"
  target = "${google_compute_target_pool.front-pool.self_link}"
  ip_address = "${google_compute_address.www-app.address}"
  port_range = "3000"
}
