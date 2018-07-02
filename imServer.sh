#!/usr/bin/env bash
VECTORBIN="${BASH_SOURCE-$0}"
VECTORBIN="$(dirname "${VECTORBIN}")"
VECTORBINDIR="$(cd "${VECTORBIN}"; pwd)"
RUNNAME="ImServer"
VECTORCONFIG=""
LOGPATH="/dev/null"
PID=$(ps aux | grep ${RUNNAME} | grep -v grep | awk '{print $2}' )
export CLASSPATH="$CLASSPATH:$VECTORBINDIR/lib/*"
VECTORMAIN="com.yk.im.ImServer"
JAVA_OPTS="-Xms256M -Xmx512M -Xmn256M"
case $1 in
stop)
	echo -e "Stop ${RUNNAME}...."
	if [[ $PID = "" ]]
		then
			echo "no ${RUNNAME} to stop"
		else
			kill -9 $PID
			echo -e "${RUNNAME}  Stoped"


	fi
	exit 0
	;;
start)
    echo  -e "Starting ${RUNNAME}... "
	if [[ ! $PID = "" ]]
		then
			echo ${RUNNAME} has started with PID $PID
	else
		#1>/dev/null 2>&1 &--spring.profiles.active=
		java  $JAVA_OPTS  -cp $CLASSPATH $VECTORMAIN $VECTORCONFIG 1>$LOGPATH 2>&1 &
		echo -e "${RUNNAME} Started"
	fi
	exit 0
	;;
*)
	echo "Usage: $0 {start|stop}" >&2
esac
