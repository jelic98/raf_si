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
readonly VALIDATOR_PORT=9005
readonly VALIDATOR_DIR=Validator
readonly STORAGE_PORT=9006
readonly STORAGE_DIR=Storage
readonly GENERATOR_PORT=9007
readonly GENERATOR_DIR=Generator
readonly TRANSFORMER_PORT=9008
readonly TRANSFORMER_DIR=Transformer
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
		mkdir -p "$PATH_LOG"
		rm -rf "$PATH_LOG/$1.txt"
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

# Stop previously started service before restarting it
stop $TRANSFORMER_DIR $TRANSFORMER_PORT
stop $GENERATOR_DIR $GENERATOR_PORT
stop $STORAGE_DIR $STORAGE_PORT
stop $VALIDATOR_DIR $VALIDATOR_PORT
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
start $VALIDATOR_DIR $VALIDATOR_PORT "python3 '$VALIDATOR_DIR/main.py' $VALIDATOR_DIR $VALIDATOR_PORT > '$PATH_LOG/$VALIDATOR_DIR.txt'"
start $STORAGE_DIR $STORAGE_PORT "php -S 127.0.0.1:$STORAGE_PORT -t '$STORAGE_DIR' > '$PATH_LOG/$STORAGE_DIR.txt'"
start $GENERATOR_DIR $GENERATOR_PORT "gradle run -p '$GENERATOR_DIR' --args='$GENERATOR_PORT' > '$PATH_LOG/$GENERATOR_DIR.txt'"
start $TRANSFORMER_DIR $TRANSFORMER_PORT "gradle run -p '$TRANSFORMER_DIR' --args='$TRANSFORMER_PORT' > '$PATH_LOG/$TRANSFORMER_DIR.txt'"

# Wait for cancellation request by user
waitforq

# Stop service
stop $TRANSFORMER_DIR $TRANSFORMER_PORT
stop $GENERATOR_DIR $GENERATOR_PORT
stop $STORAGE_DIR $STORAGE_PORT
stop $VALIDATOR_DIR $VALIDATOR_PORT
stop $CLIENT_DIR $CLIENT_PORT
stop $CORE_DIR $CORE_PORT
stop $AUTH_DIR $AUTH_PORT
stop $LOG_DIR $LOG_PORT
stop $BROKER_DIR $BROKER_PORT
