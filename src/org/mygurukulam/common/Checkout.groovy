package org.mygurukulam.common

def clone(String url, String creds, String branch) {
    echo "Cloning repo ${url} from branch ${branch} using creds ${creds}";
    git branch: "${branch}", credentialsId: "${creds}", url: "${url}"
}

return this