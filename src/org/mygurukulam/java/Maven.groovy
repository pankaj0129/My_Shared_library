package org.mygurukulam.java

def compile() {
  sh "mvn clean package"
}

def test() {
  sh "mvn test"
}

return this