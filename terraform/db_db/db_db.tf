provider "google" {
  credentials = "${file("${var.service_account_key_path}")}"
  project = "${var.project}"
  region  = "${var.region}"
  zone    = "${var.zone}"
}

#Database

resource "google_sql_database" "db6" {
  name      = "bike_championship"
  instance  = "db6"

  timeouts {
    create = "2m"
  }
}
