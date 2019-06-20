pipeline {
    agent any

    stages {
        stage('Terraform build') {
            steps {
                echo 'Terraform build' 
                dir("./terraform'") {
                    sh 'ls -la'
                    sh 'terraform init -input=false'
                    sh 'terraform apply -input=false -auto-approve'
                    echo 'Open app'
                    sh 'gnome-open http://i.ua'
                }
            }
        }
        stage('Ansible build back') {
            steps {
                echo 'Ansible build'
                cd './ansible'
                sh 'ansible-playbook playbook_back.yml'
            }
        }
        stage('Ansible build front') {
            steps {
                echo 'Ansible build'
                cd './ansible'
                sh 'ansible-playbook playbook_front.yml'
            }
        }
        stage('Open app') {
            steps {
                echo 'Open app'
                sh 'gnome-open http://i.ua'
            }
        }
    }
}
