package com.minicluster.cluster.node;

import com.minicluster.cluster.service.Reducible;

import java.io.*;
import java.util.Collection;

/**
 * The cluster is made of nodes, connected to other remote nodes.
 * Each node acts like a proxy, and can exchange serializable data with other nodes.
 * <p>
 * Starting/connecting operations:
 * The node creates a socket server, listen to its parent node, then tries to connect to its child nodes.
 * <p>
 * Reducing/broadcasting operations:
 * The all-reduce operation is implemented as a reduce, followed by a broadcast.
 * reduce step: aggregate data from the leaves nodes to the root node. After that, only the root node contains the reduced data
 * broadcast step: broadcast the root node data to all other nodes. After that, all nodes contain the reduced data
 */
public interface Node {

    /**
     * start the node
     */
    void start() throws IOException;

    /**
     * connect the node to the distributed environment
     */
    void connect(Endpoint father, Collection<Endpoint> children) throws IOException, ClassNotFoundException;

    /**
     * disconnect from the distributed environment
     */
    void disconnect() throws IOException;

    /**
     * reduce message contents received from child nodes, and send result to the father node
     */
    void reduce(Message<? extends Reducible> msg) throws ClassNotFoundException, IOException;

    /**
     * send to child nodes the message received from the father node
     */
    void broadcast(Message<?> msg) throws ClassNotFoundException, IOException;

    /**
     * get endpoint (hostname and port) of the node
     */
    Endpoint getEndpoint() throws IOException;

    /**
     * get node id
     */
    Integer getId();
}
