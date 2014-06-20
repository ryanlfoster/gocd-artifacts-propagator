#!/bin/bash

set -e


CURRENT_PIPELINE_LOCATOR=$1 # Example :- http://goserver.com:8153/go/files/foo/1243/UATest/1/UAT

[ -d artifacts ] || mkdir artifacts

rm -rf tmp_artifacts
mkdir tmp_artifacts

if [ -f .artifacts_fetched_from_upstream ]; then
    for upstream_artifacts in `cat .artifacts_fetched_from_upstream`
    do
        mv $upstream_artifacts tmp_artifacts
    done
fi

zip -r artifacts.zip artifacts
echo -e "Uploading artifacts to $CURRENT_PIPELINE_LOCATOR \n"
curl -F zipfile=@artifacts.zip $CURRENT_PIPELINE_LOCATOR

cp -r tmp_artifacts/* artifacts/.

echo -e "Warming up the artifact cache for downstream pipelines \n"
curl $CURRENT_PIPELINE_LOCATOR/artifacts.zip
exit 0