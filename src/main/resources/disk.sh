#/bin/sh
case $1 in 
	rb)
		iostat -d | tail -n +4 | awk '{sum+=$6} END {print sum}'
		;;
	wb)
		iostat -d | tail -n +4 | awk '{sum+=$7} END {print sum}'
		;;
esac
