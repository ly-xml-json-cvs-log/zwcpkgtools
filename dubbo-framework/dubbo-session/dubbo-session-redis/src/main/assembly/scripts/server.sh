#!/bin/bash
cd `dirname $0`

case "$1" in
    "start" )
        ./start.sh
        ;;
    "stop"  )
        ./stop.sh
        ;;
    "debug" )
        ./start.sh debug
        ;;
    "restart")
        ./restart.sh
        ;;
    "dump" )
        ./dump.sh
        ;;
     *)
        echo "ERROR: Please input argument: start or stop or debug or restart or dump"
        ;;
esac