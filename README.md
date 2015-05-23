# Goal
This project uses distributed computing to implement the allreduce algorithm (see for instance the following description of allreduce: https://github.com/JohnLangford/vowpal_wabbit/wiki/Cluster_parallel.pdf).

# Description
The base mechanism is the following:
* start a registration server, passing the number of clients that will try to register
* start clients (as many as configured when the registration server was launched), giving a unique id, the hostname and the ip of the registration server

Each client will connect to the server to get its location in the communication system.
Then, all clients will be able to communicate between each other, in order to perform the allreduce operation on their respective value.

# Example
For instance, let us define the reduce operation as a simple integer sum. Then, running 3 clients with respective values 11, 22 and 33, leads to the final value 66 for each client:
* client 1 has value 11
* client 2 has value 22
* client 3 has value 33

After the allreduce, all clients will have value 66 ( = 11 + 22 + 33 )

This example is implemented in the main class com.yann.distributedenv.example.HelloWorld:
```java
java -cp <path_to_jar> com.yann.distributedenv.example.HelloWorld [nb_clients]
```


Here, all clients are identified by an id from 1 to nbClient, and their respective value is equal to their id.

You may also run the example using:
```sh
sh script/run_example.sh   # start 2 clients with values 1 and 2, result after allreduce is 3
sh script/run_example.sh 5 # start 5 clients with values 1 ... 5, result after allreduce is 15
```

# Package using maven
```sh
mvn package
```
