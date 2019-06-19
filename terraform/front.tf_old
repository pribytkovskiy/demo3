resource "google_compute_instance" "front" {
  name         = "front"
  machine_type = "g1-small"
  zone         = "europe-west6-a"

  tags = ["front"]

  boot_disk {
    initialize_params {
      image = "ubuntu-os-cloud/ubuntu-1604-lts"
    }
  }

  // Local SSD disk
  scratch_disk {
  }

  network_interface {
    network = "default"

    access_config {
      // Ephemeral IP
    }
  }

  metadata = {
    ssh-keys = "${var.ssh_user}:${file("/home/dr_trem86/.ssh/gcloud_id_rsa.pub")}"
  }

    provisioner "remote-exec" {
    connection {
      type        = "ssh"
      user        = "root"
      private_key = "${file("/home/dr_trem86/.ssh/gcloud_id_rsa")}"
      agent       = false
    }

    inline = [
      "echo hi > /test.txt",
      "echo hi"
    ]
  }

  // metadata_startup_script = "echo hi > /test.txt"

  service_account {
    scopes = ["userinfo-email", "compute-ro", "storage-ro"]
  }
}
