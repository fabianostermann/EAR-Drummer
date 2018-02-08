#!/bin/sh

if [ $(dpkg-query -W -f='${Status}' librsvg2-bin 2>/dev/null | grep -c "ok installed") -eq 0 ]
then
	echo "install package 'librsvg2-bin' with command 'rsvg' to use this install script"
	return;
fi
echo "Generating png out of svg.."

find ./ -name \*.svg -exec rsvg -w 24 -a {} {}.png \;
rename -f 's/.svg.png$/.png/' *.svg.png
