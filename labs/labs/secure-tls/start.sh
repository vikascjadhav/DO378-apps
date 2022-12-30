#!/bin/sh

cd ~/DO378/labs/secure-tls/

echo "Starting the 'solver' project "
cd solver
mvn clean quarkus:dev -Ddebug=5005 &
SOLVER_PID=$!
sleep 5
cd ..

echo "Starting the 'adder' project "
cd adder
mvn clean quarkus:dev -Ddebug=5006 &
ADDER_PID=$!
sleep 5
cd ..

echo "Starting the 'multiplier' project "
cd multiplier
mvn clean quarkus:dev -Ddebug=5007 &
MULTIPLIER_PID=$!
sleep 5
cd ..

while :; do 
  (tput sc; tput cup "$(tput lines)" 0; tput rev; tput el)
  echo -n "Press enter to Terminate"
  (tput rc; tput sgr0)
  if read -s -r -t1 -n1; then break; fi;
  (tput el)
done

kill $SOLVER_PID $ADDER_PID $MULTIPLIER_PID
sleep 2
echo "All services terminated"
echo
