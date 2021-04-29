#!/bin/bash

# Define constants
readonly PATH_LOG=logs/
readonly BROKER_PORT=9000
readonly BROKER_DIR=Broker
readonly LOG_PORT=9001
readonly LOG_DIR=Log
readonly AUTH_PORT=9002
readonly AUTH_DIR=Auth
readonly CORE_PORT=9003
readonly CORE_DIR=Core
readonly CLIENT_PORT=9004
readonly CLIENT_DIR=Client
readonly DATE="date +'%d-%m-%Y %H:%M:%S'"

# Extract global arguments
readonly ARG_N=$#
readonly ARG_1=$1

# Define logging helper function
log() {
	printf "\033[0;36m"
	file=$(basename $0)
	echo "[$(eval $DATE)] [$(echo "${file%.*}" | tr a-z A-Z)] $1"
	printf "\033[0m"
}

# Define starting helper function
start() {
	if [ $ARG_N -eq 0 ] || [ $ARG_1 = $1 ]; then
		log "Starting $1 on port $2"
		eval "nohup $3 2>&1 &"
	fi
}

# Define stopping helper function
stop() {
	if [ $ARG_N -eq 0 ] || [ $ARG_1 = $1 ]; then
		pid=$(lsof -t -i:$2)
		if [ $pid ]; then
			log "Stopping $1 (PID $pid) on port $2"
		fi
		kill -9 $pid 2>/dev/null
	fi
}

# Define waiting helper function
waitforq() {
	log "Press Q to stop"
	while [ true ]; do
		read -n 1 k <&1
		if [[ $k = q ]]; then
			break;
		else
			echo "Waiting for Q keypress"
		fi
	done
}

# Create log directory
log "Setting up log directory"
rm -rf "$PATH_LOG" && mkdir "$PATH_LOG"

# Stop previously started service before restarting it
stop $CLIENT_DIR $CLIENT_PORT
stop $CORE_DIR $CORE_PORT
stop $AUTH_DIR $AUTH_PORT
stop $LOG_DIR $LOG_PORT
stop $BROKER_DIR $BROKER_PORT

# Start service
start $BROKER_DIR $BROKER_PORT "php -S 127.0.0.1:$BROKER_PORT -t '$BROKER_DIR' > '$PATH_LOG/$BROKER_DIR.txt'"
start $LOG_DIR $LOG_PORT "php -S 127.0.0.1:$LOG_PORT -t '$LOG_DIR' > '$PATH_LOG/$LOG_DIR.txt'"
start $AUTH_DIR $AUTH_PORT "mvn -f '$AUTH_DIR/pom.xml' spring-boot:run > '$PATH_LOG/$AUTH_DIR.txt'"
start $CORE_DIR $CORE_PORT "mvn -f '$CORE_DIR/pom.xml' spring-boot:run > '$PATH_LOG/$CORE_DIR.txt'"
start $CLIENT_DIR $CLIENT_PORT "npm run serve --prefix '$CLIENT_DIR' -- --port $CLIENT_PORT > '$PATH_LOG/$CLIENT_DIR.txt'"

# Wait for cancellation request by user
waitforq

# Stop service
stop $CLIENT_DIR $CLIENT_PORT
stop $CORE_DIR $CORE_PORT
stop $AUTH_DIR $AUTH_PORT
stop $LOG_DIR $LOG_PORT
stop $BROKER_DIR $BROKER_PORT
