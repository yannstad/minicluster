# goal
This project uses distributed computing to implement the allreduce algorithm (see for instance the following description of allreduce: https://github.com/JohnLangford/vowpal_wabbit/wiki/Cluster_parallel.pdf).

# description
The base mechanism is the following:
* start a server, giving the number of clients that will come
* start clients (as many as configured when the server was launched)

Each client will connect to the server to get its location in the communication system.
Then, all clients will be able to communicate between each other, in order to perform the allreduce operation on their respective value.

# example
For instance, if we define allreduce as a simple integer sum, running 3 clients with respective values 11, 22 and 33 leads to the final value 66 for each client:
* client 1 has value 11
* client 2 has value 22
* client 3 has value 33

After the allreduce, all clients will have value 66 (=11+22+33)

# running example with code
This example can be run as follows:
* launch <code>./script/server.sh 3</code>, where 3 denotes the number of expected clients
* launch <code>./script/start_node.sh 1 11</code>, where 1 denotes the client id (unique) and 11 denotes the value that will be aggregated through all clients
* launch <code>./script/start_node.sh 2 22</code>, where 2 denotes the client id (unique) and 22 denotes the value that will be aggregated through all clients
* launch <code>./script/start_node.sh 3 33</code>, where 3 denotes the client id (unique) and 33 denotes the value that will be aggregated through all clients
