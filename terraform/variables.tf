variable "public_key_path" {
    description = "public_key_path"
    default     = "/var/lib/jenkins/.ssh/gcloud_id_rsa.pub"
}

variable "private_key_path" {
    description = "Path to the private SSH key, used to access the instance."
    default     = "/var/lib/jenkins/.ssh/gcloud_id_rsa"
}

variable "service_account_key_path" {
    description = "service_account_key"
    default     = "/home/dr_trem86/secrets/java-243611-6d1d9066ee2b.json"
}

variable "project" {
    description = "project"
    default     = "java-243611"
}

variable "region" {
    description = "region"
    default     = "europe-west6"
}

variable "zone" {
    description = "zone"
    default     = "europe-west6-a"
}