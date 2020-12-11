#! /bin/bash

/usr/bin/xrandr -d :0 --output screen --primary --auto
/usr/bin/xrandr --newmode "1920x968" 172.80  1920 2040 2248 2576  1080 1081 1084 1118  -HSync +Vsync #118.25 1600 1696 1856 2112 900$
/usr/bin/xrandr --addmode screen "1920x968"
/usr/bin/xrandr --output screen --mode "1920x968"
/usr/bin/xrandr