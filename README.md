# DataMarketplace_app

A simple implementation of a Data Marketplace app, containing a catalogue; a shopping cart; a checkout place and web pages for admin use.

## Instructions for starting the application
- MySql must be running on your machine.
- Download the <i>anansi_sql_dump.txt</i> file and run it in MySQL Workbench or through a SQL shell. The SQL script will create and set the anansi_db as the active database and fill in some starter data.
- You will need to update the following in src/main/resources/application.properties to match your mySql user and password. 
spring.datasource.username=root
spring.datasource.password=1234
- Once the database is active you can start the application via IDE or maven shell commands.
- Open a browser and go to http://localhost:8080
- To explore the admin pages use the following: 
Username: admin101 
Password: 1234
