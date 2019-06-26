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

resource "google_sql_database_instance" "db8" {
  name = "db8"
  database_version = "MYSQL_5_6"
  region = "${var.region}"

  settings {
    tier = "db-n1-standard-2"
  }
}

resource "null_resource" "delay" {
  provisioner "local-exec" {
    command = "sleep 30"
  }
  triggers = {
    "before" = "${null_resource.before.id}"
  }
}

resource "null_resource" "after" {
  depends_on = ["null_resource.delay"]
}
