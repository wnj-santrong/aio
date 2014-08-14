#!/bin/sh

# backup mysql
mysqldump -uroot -plinux  aio > /opt/AIO/Service/webservice/dbbackup/aio`date +%Y%m%d_%H%M%S`.sql
