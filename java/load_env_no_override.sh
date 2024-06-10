#!/bin/bash

SCRIPTPATH="$( cd -- "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"

if [ -z "$1" ]; then
    ENV_FILE_PATH="$SCRIPTPATH/.env"
else
    ENV_FILE_PATH="$SCRIPTPATH/$1"
fi

#echo "coucou $(dirname "$0")"
#echo "coucou $ENV_FILE_PATH"

export $(grep -v '^#' $ENV_FILE_PATH | while read -r line; do
  VAR=$(echo "$line" | cut -d '=' -f 1)
  if [ -z "${!VAR}" ]; then
    echo "$line";
  fi
done)
