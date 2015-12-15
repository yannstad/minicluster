package com.minicluster.cluster.topology;

import com.google.common.collect.Lists;

import java.util.Collection;

/**
 * A ring, or a linked list. Every node has 1 father and 1 child, except the root (no father), and the unique leaf
 */
/**
 * The topology of the cluster is defined by a ring, or a linked list.
 * All connected nodes form a ring. Here is an example of a ring with 4 nodes:
 *
 *        4 -> 3 -> 2 -> 1
 *
 * Here, node 1 is the root node.
 */
class Ring implements Topology {

    private static final Integer ROOT_ID = 1;
    private final Integer nodeId;
    private final Integer size;

    public Ring(Integer nodeId, Integer size) {
        this.nodeId = nodeId;
        this.size = size;
    }

    @Override
    public Integer getFatherId() {
        if (nodeId == ROOT_ID) {
            return null;
        }
        return nodeId - 1;

    }

    @Override
    public Collection<Integer> getChildIds() {
        if (nodeId == this.size) {
            return null;
        }
        Collection<Integer> childrenIds = Lists.newArrayList();
        childrenIds.add(nodeId + 1);
        return childrenIds;
    }
}
