package org.mygurukulam.common

def check(project) {
    def currentWorkspace = pwd()
    dependencyCheck additionalArguments: "--project ${project} --scan ${currentWorkspace} --format HTML", odcInstallation: 'DP-Check'
}

return this