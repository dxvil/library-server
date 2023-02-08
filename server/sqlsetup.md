Setup mysql db from dockerfile

(i will show all docker commands with sudo, but feel free to do without it if you setup your docker)
 
// you need to be in the root folder -> server
sudo docker build .
//it will build an image called mysql
sudo docker run --name mydb -d -p 3306:3306 mysql
//after running the container, let's create a db 
sudo docker exec -it mydb bash
//when you will be in bash, go to mysql
mysql -uroot -p
//(password in application.properties)
create database library;
exit

//then start server
if you want to save your db backup before stop docker container
do that command in the root folder (server):
sudo docker exec mydb /usr/bin/mysqldump -u root --password=(here is password) library > mysql_backup.sql

and then stop container
sudo docker stop mydb

if you want to get your data from backup then run container again and do:

cat mysql_backup.sql | sudo docker exec -i mydb /usr/bin/mysql -u root --password=(password) library
