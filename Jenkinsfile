pipeline {
    agent any

    stages {
        stage('Terraform build') {
            steps {
                echo 'Terraform build' 
                sh 'terraform apply'
            }
        }
        stage('Ansible build') {
            steps {
                echo 'Ansible build'
                sh 'sudo ansible.sh'
            }
        }
        stage('Open app') {
            steps {
                echo 'Open app'
                sh 'gnome-open URL'
            }
        }
    }
}
