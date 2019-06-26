provider "google" {
  credentials = "${file("${var.service_account_key_path}")}"
  project = "${var.project}"
  region  = "${var.region}"
  zone    = "${var.zone}"
}

#Database

resource "random_id" "db_name_suffix" {
  byte_length = 4
}

resource "google_sql_database_instance" "db18" {
  name = "db18"
  database_version = "MYSQL_5_6"
  region = "${var.region}"

  settings {
    tier = "db-n1-standard-2"
  }
}
