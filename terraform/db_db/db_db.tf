provider "google" {
  credentials = "${file("${var.service_account_key_path}")}"
  project = "${var.project}"
  region  = "${var.region}"
  zone    = "${var.zone}"
}

#Database

resource "google_sql_database" "db8" {
  name      = "bike_championship"
  instance  = "db8"

  timeouts {
    create = "2m"
  }
}

resource "null_resource" "delay" {
  provisioner "local-exec" {
    command = "sleep 30"
  }
}

resource "null_resource" "after" {
  depends_on = ["null_resource.delay"]
}
