package org.mygurukulam.terraform

def terraform(String action) {
    sh("terraform ${action} -no-color")
}
return this

def linting(Map args) {
    sh('tflint --format default | tee tflint_report.json')
}
return this

def security(String dir) {
    sh("checkov --directory . || true")
}
return this

def infracost(Map args) {
    sh('infracost breakdown --path . --format html > infracost_report.html')
}
return this
