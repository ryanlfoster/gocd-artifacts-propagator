#!/bin/bash

# Usage:
#
#   bash# ./fetch_upstream_artifacts.sh <optional list of jobs>
#
#   bash# ./fetch_upstream_artifacts.sh test package
#
#   Execute this script in the setup phase for all jobs to pull in artifacts
#   from all its upstream pipelines

#   The reason to have an optional list of jobs as an argument is its not possible
#   to derive the list of jobs that are present in a stage of an upstream. Hence the
#   hack way to solve this is to hit this list of jobs and die silently if 404


# Steps:
# 1. Find out the materials for current stream
# 2. mkdir artifacts and pull *all artifacts* in this folder

rm -rf artifacts
mkdir -p artifacts/pipelines

rm -rf tmp_artifacts
mkdir tmp_artifacts

list_of_jobs=$@
list_of_jobs=${list_of_jobs:=test}

# By default go provides a HTTPS url, but HTTPS is an overkill for us.
http_go_server_url="http://admin:Helpdesk@y-ci-master.dnspam:8153/go/"

# What kind of API optimization is this? Return 202 and ask client to hit the same url again after sometime?
function try_url
{
    url=$1
    while [ `curl $url -w '%{response_code}' -so /dev/null` -eq 202 ]
    do
        echo "GO just became lazy and returned 202. Retrying to fetch artifacts"
        sleep 2
    done
}

for upstream in `(set -o posix ; set) | grep "^GO_DEPENDENCY_LOCATOR_*"`
do
    upstream_url=`echo $upstream | cut -d "=" -f 2`
    for job in $list_of_jobs
    do
        artifacts_url="${http_go_server_url}files/$upstream_url/$job/artifacts.zip"
        echo "Pulling artifacts from $upstream_url:$job - $artifacts_url"
        try_url $artifacts_url
        curl "$artifacts_url" > tmp_artifacts/artifacts.zip
        [ $? -eq  0 ] && unzip tmp_artifacts/artifacts.zip
        rm -rf tmp_artifacts/*
    done
done

rm -rf tmp_artifacts
