package com.minicluster.cluster.topology;

/**
 * Factory
 */
public class Topologies {

    public static Topology newBinaryTree(Integer nodeId, Integer size) {
        return new BinaryTree(nodeId, size);
    }

    //never used or tested
    public Topology newFlatTree(Integer nodeId, Integer size) {
        return new FlatTree(nodeId, size);
    }

    //never used or tested
    public Topology newRing(Integer nodeId, Integer size) {
        return new Ring(nodeId, size);
    }
}
