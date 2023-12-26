package org.mygurukulam.java

def analyze() {
    sh 'mvn spotbugs:spotbugs'
    sh 'mvn site'
}

return this