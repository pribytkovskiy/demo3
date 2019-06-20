pipeline {
    agent any

    stages {
        stage('Terraform build') {
            steps {
                echo 'Terraform build' 
                dir("./terraform") {
                    sh 'terraform init -input=false'
                    sh 'terraform apply -input=false -auto-approve'
                }
            }
        }
        stage('Ansible build back') {
            steps {
                echo 'Ansible build'
                sh 'ls -la'
                dir("./ansible") {
                    sh 'ls -la'
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
