#!/bin/sh

COUNTER=0
while true
do
  # WARNING: %3N might not work on OS X
  TIME=`date +%s%3N`

  if [ $((COUNTER%10)) -eq 0 ];
  then
    # every tenth invocation will be more than a minute old
    TIME=$((TIME - 61000))
  else
    if [ $((COUNTER%5)) -eq 0 ];
    then
      # every 5th invocation in between will be 15 seconds into the future
      TIME=$((TIME + 15000))
    fi
  fi

  BODY="{\"amount\":  $COUNTER, \"timestamp\": $TIME}"
  STATUS=`curl -X POST --write-out %{http_code} -d "$BODY" 'http://localhost:8080/transactions/' --silent`
  echo "POST $STATUS - $BODY"

  RESPONSE=`curl -G 'http://localhost:8080/statistics' --silent`
  echo "GET $RESPONSE"

  COUNTER=$((COUNTER + 1))
  sleep 2
done
