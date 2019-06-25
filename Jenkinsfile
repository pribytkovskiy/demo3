pipeline {
    agent any

    stages {
        stage('Terraform build db instance') {
           steps {
               echo 'Terraform build db_inc' 
               dir("./terraform/db_inc") {
                   sh 'terraform init -input=false'
                   sh 'terraform apply -input=false -auto-approve'
               }
           }
        }
        stage('Terraform build create db') {
           steps {
               echo 'Terraform build create db' 
               dir("./terraform/db_db") {
                   sh 'terraform init -input=false'
                   sh 'terraform apply -input=false -auto-approve'
               }
           }
        }
        stage('Terraform build main') {
           steps {
               echo 'Terraform build main' 
               dir("./terraform/main") {
                   sh 'terraform init -input=false'
                   sh 'terraform apply -input=false -auto-approve'
               }
           }
        }
        stage('Ansible build back') {
            steps {
                echo 'Ansible build'
                dir("./ansible") {
                    sh 'ansible-playbook playbook_back.yml'
                }
            }
        }
        stage('Ansible build front') {
            steps {
                echo 'Ansible build'
                dir("./ansible") {
                    sh 'ansible-playbook playbook_front.yml'
                }
            }
        }
    }
}
