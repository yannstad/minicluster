package com.minicluster.cluster.node;

/**
 * Factory
 */
public class Nodes {

    public static Node newTcpNode(Integer nodeId) {
        return new NodeTcp(nodeId);
    }
}
