provider "google" {
  credentials = "${file("${var.service_account_key_path}")}"
  project = "${var.project}"
  region  = "${var.region}"
  zone    = "${var.zone}"
}

#Front

resource "google_compute_instance_template" "front-template" {
   machine_type = "g1-small"
   tags = ["front"]

   disk {
      auto_delete  = true
      boot         = true
      source_image = "ubuntu-os-cloud/ubuntu-1604-lts"
   }

   metadata = {
      ssh-keys = "root:${file("${var.public_key_path}")}"
   }
   
   network_interface {
      network = "default"
      access_config {
      }
   }
}

resource "google_compute_instance_group_manager" "front-group" {
   name = "front-group"
   base_instance_name = "front"
   target_size        = 2
   instance_template  = "${google_compute_instance_template.front-template.self_link}"
   target_pools       = ["${google_compute_target_pool.front-pool.self_link}"]


   named_port {
      name = "front80"
      port = "80"
   }

   named_port {
      name = "front3000"
      port = "3000"
   }
}

resource "google_compute_target_pool" "front-pool" {
   name = "front-pool"
}

resource "google_compute_forwarding_rule" "front-proxy" {
   name       = "front-proxy"
   target     = "${google_compute_target_pool.front-pool.self_link}"
   port_range = "3000"
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

  provisioner "remote-exec" {
    connection {
      host        = "back"
      type        = "ssh"
      user        = "root"
      private_key = "${file("${var.private_key_path}")}"
      agent       = false
    }

    inline = [
      "echo hi"
    ]
  }
}

#Database

resource "google_compute_instance" "db" {
  count        = 2
  name         = "db-${count.index + 1}"
  machine_type = "g1-small"

  boot_disk {
    initialize_params {
      image = "ubuntu-os-cloud/ubuntu-1604-lts"
    }
  }

  network_interface {
    network = "default"

    access_config {
    }
  }
}

data "null_data_source" "auth_netw_mysql_allowed_1" {
  count = "${length(google_compute_instance.db.*.self_link)}"

  inputs = {
    name  = "db-${count.index + 1}"
    value = "${element(google_compute_instance.db.*.network_interface.0.access_config.0.nat_ip, count.index)}"
  }
}

data "null_data_source" "auth_netw_mysql_allowed_2" {
  count = 2

  inputs = {
    name  = "onprem-${count.index + 1}"
    value = "${element(list("192.168.1.2", "192.168.2.3"), count.index)}"
  }
}

resource "random_id" "db_name_suffix" {
  byte_length = 4
}

resource "google_sql_database_instance" "mysql" {
  name = "master-instance-${random_id.db_name_suffix.hex}"
  database_version = "MYSQL_5_6"

  settings {
    tier = "D0"

    ip_configuration {
      ipv4_enabled = "true"
    }
  }
}

#Firewall

resource "google_compute_firewall" "default" {
  name    = "java-app-firewall"
  network = "default"

  allow {
    protocol = "tcp"
    ports    = ["80"]
  }

  source_ranges = ["0.0.0.0/0"]
  target_tags   = ["front"]
}

resource "google_dns_managed_zone" "java-app-dns" {
  name        = "java-app"
  dns_name    = "java-app.com."
  description = "java-app.com DNS zone"
}
