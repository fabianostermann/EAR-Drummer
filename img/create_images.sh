find ./ -name \*.svg -exec rsvg -w 24 -a {} {}.png \;
rename -f 's/.svg.png$/.png/' *.svg.png
