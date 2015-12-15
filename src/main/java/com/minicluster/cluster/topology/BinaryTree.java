package com.minicluster.cluster.topology;

import com.google.common.collect.Lists;

import java.util.Collection;

/**
 * The topology of the cluster is defined by a self-balanced binary tree.
 *
 * All connected nodes form a binary tree. Every node has possibly a father, and 1 or 2 children.
 * Here is an example of a tree with 6 nodes:
 *
 *        1
 *      /  \
 *     2   3
 *    / \  |
 *   4  5  6
 *
 * Here, node 1 is the father of node 2 and 3, or equivalently node 2 and 3 are children of node 1.
 */
class BinaryTree implements Topology {

    private static final Integer ROOT_ID = 1;
    private final Integer nodeId;
    private final Integer size;

    public BinaryTree(Integer nodeId, Integer size) {
        this.nodeId = nodeId;
        this.size = size;
    }

    @Override
    public Integer getFatherId() {
        if (nodeId != ROOT_ID) {
            return nodeId / 2;
        }
        return null;
    }

    @Override
    public Collection<Integer> getChildIds() {
        Collection<Integer> childrenIds = Lists.newArrayList();
        int childId = 2 * nodeId;
        if (childId <= this.size) {
            childrenIds.add(childId);
        }
        childId = 2 * nodeId + 1;
        if (childId <= this.size) {
            childrenIds.add(childId);
        }
        return childrenIds;
    }
}
