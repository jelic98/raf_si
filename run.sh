#!/bin/bash
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

log "Setting up log directory"
rm -rf "$PATH_LOG" && mkdir "$PATH_LOG"

log "Starting $BROKER_DIR on port $BROKER_PORT"
nohup php -S 127.0.0.1:$BROKER_PORT -t "$BROKER_DIR" > "$PATH_LOG/$BROKER_DIR.txt" 2>&1 &
BROKER_PID=$(lsof -t -i:$BROKER_PORT)

log "Starting $LOG_DIR on port $LOG_PORT"
nohup php -S 127.0.0.1:$LOG_PORT -t "$LOG_DIR" > "$PATH_LOG/$LOG_DIR.txt" 2>&1 &
LOG_PID=$(lsof -t -i:$LOG_PORT)

log "Starting $AUTH_DIR on port $AUTH_PORT"
nohup mvn -f "$AUTH_DIR/pom.xml" spring-boot:run > "$PATH_LOG/$AUTH_DIR.txt" 2>&1 &
AUTH_PID=$(lsof -t -i:$AUTH_PORT)

log "Starting $CORE_DIR on port $CORE_PORT"
nohup mvn -f "$CORE_DIR/pom.xml" spring-boot:run > "$PATH_LOG/$CORE_DIR.txt" 2>&1 &
CORE_PID=$(lsof -t -i:$CORE_PORT)

log "Starting $CLIENT_DIR on port $CLIENT_PORT"
nohup npm run serve --prefix "$CLIENT_DIR" -- --port $CLIENT_PORT > "$PATH_LOG/$CLIENT_DIR.txt" 2>&1 &
CLIENT_PID=$(lsof -t -i:$CLIENT_PORT)

waitforq

log "Stopping $CLIENT_DIR (PID $CLIENT_PID) on port $CLIENT_PORT"
kill -9 $CLIENT_PID

log "Stopping $CORE_DIR (PID $CORE_PID) on port $CORE_PORT"
kill -9 $CORE_PID

log "Stopping $AUTH_DIR (PID $AUTH_PID) on port $AUTH_PORT"
kill -9 $AUTH_PID

log "Stopping $LOG_DIR (PID $LOG_PID) on port $LOG_PORT"
kill -9 $LOG_PID

log "Stopping $BROKER_DIR (PID $BROKER_PID) on port $BROKER_PORT"
kill -9 $BROKER_PID
