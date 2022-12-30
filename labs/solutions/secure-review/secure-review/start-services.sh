#!/bin/bash

pushd ~/DO378/solutions/secure-review/quarkus-conference/

echo "Starting the 'microservice-speaker' project "
cd microservice-speaker
mvn clean quarkus:dev -Ddebug=5006 &
SPEAKER_PID=$!
sleep 5
cd ..

echo "Starting the 'microservice-session' project "
cd microservice-session
mvn clean quarkus:dev -Ddebug=5007 &
SESSION_PID=$!
sleep 5
cd ..

while :; do 
  (tput sc; tput cup "$(tput lines)" 0; tput rev; tput el)
  echo -n "Press enter to Terminate"
  (tput rc; tput sgr0)
  if read -s -r -t1 -n1; then break; fi;
  (tput el)
done

kill $SPEAKER_PID $SESSION_PID
sleep 2
echo "All services terminated"
echo

popd
