#!/bin/bash

set -e

# Steps:
# 1. Find out the materials for current stream
# 2. mkdir artifacts and pull *all artifacts* in this folder

rm -rf artifacts
mkdir artifacts

rm -rf tmp_artifacts
mkdir tmp_artifacts

# What kind of API optimization is this? Return 202 and ask client to hit the same url again after sometime?
function try_url
{
    url=$1
    while [ `curl $url -w '%{response_code}' -so /dev/null` -eq 202 ]
    do
        echo -e "GO just became lazy and returned 202. Retrying to fetch artifacts \n"
        sleep 2
    done
}

for url in `cat .artifacts_to_be_fetched`
do
    echo -e "Pulling artifacts from $url \n"
    try_url $url
    curl "$url" > tmp_artifacts/artifacts.zip
    [ $? -eq  0 ] && unzip tmp_artifacts/artifacts.zip
    rm -rf tmp_artifacts/*
done

ls -1 artifacts/* > .artifacts_fetched_from_upstream

rm -rf tmp_artifacts

exit 0