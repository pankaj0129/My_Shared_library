def call(Map config = [:]) {
    pipeline {
        agent any
        environment {
            PATH = "/var/lib/jenkins/.local/bin:$PATH"
        }
        tools {
            maven "MAVEN3"
        }

        stages {
            stage("Checkout") {
                steps {
                    script {
                        cleanWs()
                        def checkout = new org.mygurukulam.common.Checkout()
                        checkout.clone(config.url, config.creds, config.branch)
                    }
                }
            }

            stage('CredScanning') {
                steps {
                    script {
                        def tfsharedlib = new org.mygurukulam.common.CredentialScanning()
                        tfsharedlib.scane()
                    }
                }
            }

            stage('Terraform Init') {
                steps {
                    script {
                        def tfsharedlib = new org.mygurukulam.terraform.tfsharedlib()
                        tfsharedlib.terraform('init')
                    }
                }
            }

            stage('Terraform Validate') {
                steps {
                    script {
                        def tfsharedlib = new org.mygurukulam.terraform.tfsharedlib()
                        tfsharedlib.terraform('validate')
                    }
                }
            }

            stage('Static Code Analysis') {
                steps {
                    script {
                        def tfsharedlib = new org.mygurukulam.terraform.tfsharedlib()
                        tfsharedlib.linting()
                    }
                }
            }

            stage('Security/Compliance') {
                steps {
                    script {
                        def tfsharedlib = new org.mygurukulam.terraform.tfsharedlib()
                        tfsharedlib.security()
                    }
                }
            }

            stage('Infra Cost') {
                steps {
                    withCredentials([string(credentialsId: 'INFRACOST_API_KEY', variable: 'INFRACOST_API_KEY')]){
                        script {
                            def tfsharedlibrary = new org.mygurukulam.terraform.tfsharedlib()
                            tfsharedlibrary.infracost()
                        }
                    }
                }
            }

            stage('Terraform Plan') {
                steps {
                    script {
                        def tfsharedlib = new org.mygurukulam.terraform.tfsharedlib()
                        tfsharedlib.terraform('plan')
                    }
                }
            }
        }
    }
}
