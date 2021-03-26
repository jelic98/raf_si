#!/bin/bash
readonly REPO="racunarski-fakultet/si2021-si-group-deepsea"
if [ $# -eq 1 ]; then
	# https://docs.github.com/en/github/authenticating-to-github/creating-a-personal-access-token
	gh auth login --with-token < token.txt \
	&& git push -u origin HEAD \
	&& gh pr create -f -a @me -b main --title "Close #$1" \
	&& gh pr merge -d -s "pr-issue-$1" \
	&& git status \
	&& git branch
else
	echo "[FAIL] No issue number provided"
fi
