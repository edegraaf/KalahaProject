KalahaProject
=============

The game of Kalaha implemented as a web project. Uses velocity, hibernate, JSON, JQuery and Spring

An implementation of the Kalaha game as a web project:
https://drive.google.com/file/d/0By12YvcV1ZSkVjZTZnFpdDAtM3c/edit?usp=sharing

The project can be loaded into Eclipse and if needed the ant build script can be used to compile the project.
Furthermore, to use the web project you will need to:
1.) Load KalahaProject/sql/mysql_tables.sql into the database
2.) Change jdbc settings in  KalahaProject/src/hibernate.cfg.xml
3.) Add jdbc drivers to KalahaProject/WebContent/WEB-INF/lib/ (if needed)
4.) Rerun ant build.xml

In Chrome you can go to http://localhost:8080/KalahaProject/ and login with username player1 and password player1.
Using another browser in Incognito mode, you can play player2 on the same computer (username player2 and password player2). 

For more information on the source code see also the api docs under KalahaProject/docs/api
