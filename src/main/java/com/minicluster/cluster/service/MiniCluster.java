package com.minicluster.cluster.service;

import com.minicluster.cluster.configuration.Configuration;
import com.minicluster.cluster.node.Node;
import com.minicluster.cluster.topology.Topology;
import org.apache.hadoop.fs.FileSystem;

import java.io.Serializable;

/**
 * MiniCluster offers an implementation of the all-reduce algorithm (see MPI doc).
 * As a client, you must:
 * 1. create an instance with MiniCluster.Builder.create(...) factory method
 * 2. start the instance with start() method
 * 3. The service is now ready to perform allReduce(...) operation, possibly several times
 * At the end, stop the instance with stop() method
 */
public interface MiniCluster {

    /**
     * start the service and connect the cluster
     */
    boolean start();

    /**
     * Perform all-reduce: a reduce followed by a broadcast
     *
     * @param data data object that is both Serializable and Reducible
     * @param <T>  generic type of data
     * @return all-reduced data
     */
    <T extends Reducible & Serializable> T allReduce(T data);

    /**
     * disconnect and stop the service
     */
    boolean stop();


    /**
     * Factory
     */
    class Builder {

        /**
         * create a new service instance
         *
         * @param id a unique id per service, where id belongs to [1, clusterSize]
         * @param clusterSize the total number of nodes of the cluster
         * @param sharedDirectory a directory on the local file system, shared by all nodes
         * @return a new service instance
         */
        static public MiniCluster create(Integer id, Integer clusterSize, String sharedDirectory) {

            Node node = Node.Builder.create(id);
            Topology topology = Topology.Builder.createBinaryTree(id, clusterSize);
            Configuration configuration = Configuration.Builder.create(sharedDirectory);

            return new MiniClusterImpl(node, topology, configuration);
        }

        /**
         * create a new service instance, dedicated to hadoop map-reduce containers
         *
         * @param id a unique id per service, where id belongs to [1, clusterSize]
         * @param clusterSize the total number of nodes of the cluster
         * @param hdfsDirectory a directory on the Hdfs, shared by all nodes
         * @param fs a hadoop file system
         * @return a new service instance
         */
        static public MiniCluster create(Integer id, Integer clusterSize, String hdfsDirectory, FileSystem fs) {

            Node node = Node.Builder.create(id);
            Topology topology = Topology.Builder.createBinaryTree(id, clusterSize);
            Configuration configuration = Configuration.Builder.create(hdfsDirectory, fs);

            return new MiniClusterImpl(node, topology, configuration);
        }
    }
}
