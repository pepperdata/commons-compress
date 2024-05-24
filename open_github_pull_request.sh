#!/usr/bin/env bash

# TODO(ankan): This does not work yet: need to set up authentication
#              (e.g., set the env var GH_TOKEN appropriately, etc.).

# TODO(ankan): Paramterize the hard-coded values below:
_BASE_BRANCH=commons-compress-1.26.2
_USER=pepper-ankan
_TYPE=enhancement

gh pr create --base ${_BASE_BRANCH} --assignee ${_USER} --label ${_TYPE} --fill --web

