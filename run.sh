#!/bin/bash
readonly BROKER_PORT=9000
readonly BROKER_DIR=Broker
readonly BROKER_LOG=log/broker.txt

readonly LOG_PORT=9001
readonly LOG_DIR=Log
readonly LOG_LOG=log/log.txt

readonly DATE="date +'%d-%m-%Y %H:%M:%S'"

log() {
	printf "\033[0;36m"
	file=$(basename $0)
	echo "[$(eval $DATE)] [$(echo "${file%.*}" | tr a-z A-Z)] $1"
	printf "\033[0m"
}

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

log "Starting $BROKER_DIR on port $BROKER_PORT"
nohup php -S localhost:$BROKER_PORT -t $BROKER_DIR > $BROKER_LOG 2>&1 &
BROKER_PID=$(lsof -t -i:$BROKER_PORT)

log "Starting $LOG_DIR on port $LOG_PORT"
nohup php -S localhost:$LOG_PORT -t $LOG_DIR > $LOG_LOG 2>&1 &
LOG_PID=$(lsof -t -i:$LOG_PORT)

waitforq

log "Stopping $LOG_DIR (PID $LOG_PID) on port $LOG_PORT"
kill -9 $LOG_PID

log "Stopping $BROKER_DIR (PID $BROKER_PID) on port $BROKER_PORT"
kill -9 $BROKER_PID
