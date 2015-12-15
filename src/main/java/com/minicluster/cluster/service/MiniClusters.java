package com.minicluster.cluster.service;

import com.minicluster.cluster.configuration.Configuration;
import com.minicluster.cluster.configuration.Configurations;
import com.minicluster.cluster.node.Node;
import com.minicluster.cluster.node.Nodes;
import com.minicluster.cluster.topology.Topologies;
import com.minicluster.cluster.topology.Topology;
import org.apache.hadoop.fs.FileSystem;

/**
 * Factory
 */
public class MiniClusters {

    /**
     * create a new service instance
     *
     * @param id a unique id per service, where id belongs to [1, clusterSize]
     * @param clusterSize the total number of nodes of the cluster
     * @param sharedDirectory a directory on the local file system, shared by all nodes
     * @return a new service instance
     */
    static public MiniCluster newLocalCluster(Integer id, Integer clusterSize, String sharedDirectory) {

        Node node = Nodes.newTcpNode(id);
        Topology topology = Topologies.newBinaryTree(id, clusterSize);
        Configuration configuration = Configurations.newSharedLocalDirectory(sharedDirectory);

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
    static public MiniCluster newMapReduceCluster(Integer id, Integer clusterSize, String hdfsDirectory, FileSystem fs) {

        Node node = Nodes.newTcpNode(id);
        Topology topology = Topologies.newBinaryTree(id, clusterSize);
        Configuration configuration = Configurations.newSharedHdfsDirectory(hdfsDirectory, fs);

        return new MiniClusterImpl(node, topology, configuration);
    }
}
