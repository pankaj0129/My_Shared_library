def call(Map config = [:]) {
    pipeline {
        agent any

        tools {
            maven "MAVEN3"
        }

        parameters {
            booleanParam(name: 'SKIP_TEST', defaultValue: false, description: 'Skip test stage')
            booleanParam(name: 'SKIP_DEPENDENCY_CHECK', defaultValue: false, description: 'Skip dependency-check stage')
            booleanParam(name: 'SKIP_BUG_ANALYSIS', defaultValue: false, description: 'Skip bug analysis stage')
            booleanParam(name: 'SKIP_CREDENTIAL_SCANNING', defaultValue: false, description: 'Skip bug analysis stage')
        }

        stages {

            stage('Checkout') {
                steps {
                    script {
                        def checkout = new org.mygurukulam.common.Checkout()
                        checkout.clone(config.url, config.creds, config.branch)
                    }
                }
            }

            stage('Credentials scanning') {

                when {
                    expression { params.SKIP_CREDENTIAL_SCANNING == false }
                }

                steps {
                    script {
                        def c = new org.mygurukulam.common.CredentialScanning()
                        c.scane()
                    }
                }
            }

            stage('Compile') {
                when {
                    expression { params.SKIP_DEPENDENCY_CHECK == false }
                }
                steps {
                    script {
                        def z = new org.mygurukulam.java.Maven()
                        z.compile()
                    }
                }
            }

            stage('Test') {

                when {
                    expression { params.SKIP_TEST == false }
                }
            
                steps {
                    script {
                        def z = new org.mygurukulam.java.Maven()
                        z.test()
                    }
                }
            }

            stage('Dependency check') {
            
                when {
                    expression { params.SKIP_DEPENDENCY_CHECK == false }
                }
            
                steps {
                    script {
                        def d = new org.mygurukulam.common.Dependency()
                        d.check(config.project)
                    }
                }
            }

            stage("Bug Analysis ") {

                when {
                    expression { params.SKIP_BUG_ANALYSIS == false }
                }

                steps {
                    script {
                        def z = new org.mygurukulam.java.Bug()
                        z.analyze()        
                    }
                }
            }
        }
    }
}
