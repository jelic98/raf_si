#!/bin/bash
if [ $# -eq 1 ]; then
	git pull origin main \
	&& git checkout -b "pr-issue-$1"
else
	echo "[FAIL] No issue number provided"
fi
