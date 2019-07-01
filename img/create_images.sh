#!/bin/bash

if [ $(dpkg-query -W -f='${Status}' librsvg2-bin 2>/dev/null | grep -c "ok installed") -eq 0 ]
then
	echo "install package 'librsvg2-bin' with command 'rsvg-convert' to use this install script"
	exit 1
fi

if [ $(dpkg-query -W -f='${Status}' rename 2>/dev/null | grep -c "ok installed") -eq 0 ]
then
	echo "install package 'rename'"
	exit 1
fi

echo "Generating png out of svg.."

find ./ -name "*.svg" -exec rsvg-convert -o {}.png -w 24 -a {} \;
rename -f 's/.svg.png$/.png/' *.svg.png

exit 0
