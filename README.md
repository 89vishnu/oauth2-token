# vishnu-miworld-base-java

DB set up
******************

1) install mongo db
2) install robo3t
3) sudo service mongod start
4) for executing db open terminal 
     a) got to home
     b) take the querry from mongo-db paste there enter
     c) create new collection -> create it in user-service -> models
     
5) Authentication -> run auth-service
6) hide authentication 
    a) security -> ResourceServerConfig
       hide  ->   http.authorizeRequests().antMatchers("/actuator/health").permitAll();
       add   ->   http.authorizeRequests().anyRequest().permitAll(); (not sure)
7) postman collection link -> https://www.getpostman.com/collections/153b649741ebed19b458

Ui
**
run 
vishnu-miworld-base-java

