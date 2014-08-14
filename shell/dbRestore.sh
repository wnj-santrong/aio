#!/bin/sh

# restore mysql
backdir=/opt/AIO/Service/webservice/dbbackup
if [ -f $backdir/$1 ]; then
mysql -uroot -plinux  aio < $backdir/$1
fi
