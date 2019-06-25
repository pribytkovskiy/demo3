#Database

resource "random_id" "db_name_suffix" {
  byte_length = 4
}

resource "google_sql_database_instance" "db3" {
  name = "db3"
  database_version = "MYSQL_5_6"
  region = "${var.region}"

  settings {
    tier = "db-n1-standard-2"
  }
}

resource "google_sql_database" "db3" {
  name      = "bike_championship"
  instance  = "db3"

  timeouts {
    create = "2m"
  }
}