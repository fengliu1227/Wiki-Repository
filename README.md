# Introduction
This web application is a repository that stores Ebooks for public. The admin could upload the ebooks on this website and set the category, So it is open source for the user to view and learn the new thing from the ebooks, built with RESTful architectural style and managed by MAVEN. <br/>

mybatis generator&nbsp;=>  auto generator the bean, mapper and .xml file <br/>
Lomok&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;=>  so easy to buld a class<br>
redis&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;=>  store a token<br/>
Ant Design Vue&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;=>  make it easy for the style of the page<br>
Vue 3.0, Vue Cli&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;=>  develop the frontend<br>
RocketMQ&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;=>  Decouple the spring boot aplication and the message function<br>
spring, Spring boot&nbsp;=>  never felt so easy to develop a web application before<br>

## technology stack


### backend: 
Spring, Spring Boot, Mybatis, Mysql, lombok(easy for getter and setter, etc.)<br/>
### frontend: 
Vue 3.0, Vue ClI, Ant Design Vue, axios (data interaction between backend and frontend), wangeditor(Rich text editor)<br/>
### middleware: 
redis(store token), RocketMQ(send message for all user)<br/>


## how to use:
1. Configure database information in src/main/source/application.properties.<br/>
2. Configure the websocket and backend url in web/.env.dev or web/.env.prod/<br/>
3. Make sure you created Schema named wiki.<br/>
4. go to the web folder and $npm install.<br/>
5. if you want to use RocketMQ, download it, The thing you need to notice:<br/>
````bash
nohup sh bin/mqnamesrv & //start server
nohup sh bin/mqbroker -n localhost:9876 autoCreateTopicEnable=true & // start broker(don't use the commend in the offical website if you want to auto create topic)

sh bin/mqshutdown broker //stop broker

sh bin/mqshutdown namesrv // stop server

````
4. execute the .sql file in sqlScript => main.sql, make sure you connect to your database first.<br/>
5. run the spring boot application in src/main/java/com/andrew/wiki/WikiApplication.java. Of course you can just use your IDE<br/>
6. run the Vue application: 
````bash 
    vue-cli-service serve --mode dev // run in dev environment
    vue-cli-service serve --mode prod // run in production environment
````

## user information:
### Role: User
Login name: user1<br/>
PassWord: user<br/>
### Role: Admin
Login name: admin1<br/>
PassWord: admin<br/>

# Database ER
![image](https://github.com/fengliu1227/Wiki-Repository/blob/master/image-readme/databaseER.png)

# Page Layout

## user home page
![image](https://github.com/fengliu1227/Wiki-Repository/blob/master/image-readme/user-home.png)

## category in the home page
![image](https://github.com/fengliu1227/Wiki-Repository/blob/master/image-readme/category.png)

## admin home page 
![image](https://github.com/fengliu1227/Wiki-Repository/blob/master/image-readme/admin-home.png)

## admin user mgmt. page
![image](https://github.com/fengliu1227/Wiki-Repository/blob/master/image-readme/user-management.png)

## admin ebook mgmt. page 
![image](https://github.com/fengliu1227/Wiki-Repository/blob/master/image-readme/ebook-management.png)

## admin doc mgmt. page
if the doc mgmt. button was clicked. The admin doc mgmt. page will show<br/>
![image](https://github.com/fengliu1227/Wiki-Repository/blob/master/image-readme/doc-content-management.png)

## admin category mgmt. page
![image](https://github.com/fengliu1227/Wiki-Repository/blob/master/image-readme/category-management.png)

## statistic page
![image](https://github.com/fengliu1227/Wiki-Repository/blob/master/image-readme/statistic.png)
