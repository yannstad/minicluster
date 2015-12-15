package com.minicluster.cluster.service;

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


}
