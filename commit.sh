#!/bin/bash
if [ $# -eq 1 ]; then
	git add . \
	&& git commit -m "$1" \
	&& git status \
	&& git branch
else
	echo "[FAIL] No commit description provided"
fi
