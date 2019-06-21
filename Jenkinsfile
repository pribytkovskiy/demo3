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
                timeout(time: 1, unit: 'MINUTES') {
                    dir("./ansible") {
                        sh 'ansible-playbook playbook_back.yml -vvvv'
                    }
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
