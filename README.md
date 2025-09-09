# E-Commerce web app in java spring boot (api + mysql)
## an app that exposes endpoints for managing an online e-shop. Connects with a MySql database for full CRUD operations
### How to run:
1. Download MySQL Workbench if u don't already have it: https://www.mysql.com/products/workbench/
2. Set up a local server with your username and password
3. Inside the server create a new database schema with the name: "buynowdb" and leave the mysql app open
4. clone the repo and under the "resources" folder open the "application.properties" file
5. Replace these: (spring.datasource.username=..., spring.datasource.password=...) with the username & password you configured your server with
6. In the root directory of the repo, open a terminal and run: mvn package
7. then run: java -jar ./target/buynowdotcom-0.0.1-SNAPSHOT.jar
8. the app must be running and you can now see the tables in the schema, try it with postman

### Documentation:
* All endpoints are under http://localhost:9090/api/v1
* Ready to test requests at: https://documenter.getpostman.com/view/31120142/2sB3HnKKjE
