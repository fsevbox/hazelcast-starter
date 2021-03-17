#!/usr/bin/env bash
set -euo pipefail
IFS=$'\n\t'

# Default some Environment Variables that should be set by peng,
# Only set for convenience to use this file locally
export DRP_CF_LOCATION=${DRP_CF_LOCATION:-be-gcw1}
export DRP_CF_VERTICAL=${DRP_CF_VERTICAL:-minvas}
export DRP_CF_STAGE=${DRP_CF_STAGE:-local}
export DRP_CF_VERSION=${DRP_CF_VERSION:-jgsdfjagshdjf}
export DRP_CF_HTTP_PORT=${DRP_CF_HTTP_PORT:-8080}
export DRP_CF_SERVICE=${DRP_CF_SERVICE:-mhsimpleservice}
export DRP_CF_CUSTOMER=${DRP_CF_CUSTOMER:-mcc}

# Environments not set by peng
# Needs to be managed by the team
export DRP_EV_SOLUTION=${DRP_EV_SOLUTION:-minvoice}
export DRP_EV_SOLUTION_ID=${DRP_EV_SOLUTION_ID:-S00087}
export DRP_EV_MARC_ID=${DRP_EV_MARC_ID:-A1231}
export DRP_EV_SERVICENAME=${DRP_EV_SOLUTION}.${DRP_CF_VERTICAL}.${DRP_CF_SERVICE}

export DD_DEBUG=false
export DD_PROFILING_ENABLED=true
export DD_LOGS_STDOUT=yes
export DD_LOGS_INJECTION=true
export DD_AGENT_HOST=${DRP_CF_KUBERNETES_HOST_IP}
export DD_ENV=${DRP_CF_STAGE}
export DD_SERVICE=${DRP_EV_SERVICENAME}
export DD_VERSION=${DRP_CF_VERSION}
export DD_TAGS=solution_id:${DRP_EV_SOLUTION_ID},solution:${DRP_EV_SOLUTION},marc_id:${DRP_EV_MARC_ID},vertical:${DRP_CF_VERTICAL},sec_apm_tag:${DRP_CF_VERTICAL}-${DRP_CF_LOCATION}
export DD_TRACE_GLOBAL_TAGS=${DD_TAGS},env:${DRP_CF_STAGE},service:${DD_SERVICE},version:${DRP_CF_VERSION}


# Options for the Java Virtual Machine to be added
ADDITIONAL_JAVA_OPTS=$(cat <<- END
-Ddd.trace.global.tags=${DD_TRACE_GLOBAL_TAGS}
END
)


# NOTE: This file is intended to be used by multiple services. There should be no
# customization beyond this point. If changes are done they should be synchronized to other
# MINVAS services
# running Java


# Log a message using pengs defined json log format
# Newlines in $1 will be replaced with spaces
function log() {
    message=`echo $1 | tr '\n' ' '`
    echo '{ "@timestamp" : "'$(date +%Y-%m-%dT%H:%M:%S%z)'", "message" : "'"$message" '", "@type" : "service", "@solution" : "'$DRP_EV_SOLUTION'", "@solution_id" : "'$DRP_EV_SOLUTION_ID'", "@marcId" : "'$DRP_EV_MARC_ID'", "@hostname" : "'$HOSTNAME'", "@vertical" : "'$DRP_CF_VERTICAL'", "stage": "'$DRP_CF_STAGE'",  "@service-name" : "'$DRP_CF_SERVICE'", "@service-version" : "'${DRP_CF_VERSION}'", "@trace-id" : "none"}'
}

# Log some debugging information so we have some information if the java startup fails
log "$DRP_CF_VERTICAL $DRP_CF_SERVICE run.sh started"
log  "uptime: `uptime`"
log  "ls: `ls`"

JAVA_OPTS=$(cat <<-END
-Djava.security.egd=file:/dev/./urandom
-XX:NewSize=30m
-XX:MaxMetaspaceSize=150m
-XX:AutoBoxCacheMax=1000000
-XX:+UseContainerSupport
$ADDITIONAL_JAVA_OPTS
END
)

log "Starting with: java $JAVA_OPTS -jar /opt/service.jar "

function logErrorAsJson() {
    while read line
    do
        log "JVM: $line"
    done
}

# The purpose of exec is to have correct signal handling
exec java -javaagent:/opt/dd-java-agent.jar $JAVA_OPTS -cp app:app/lib/* com.metronom.minvas.mhsimpleService.MhSimpleServiceApplication 2> >(logErrorAsJson)


