package org.mygurukulam.common

def scane() {
    sh 'gitleaks detect --report-path gitleaks-report.json'
}

return this