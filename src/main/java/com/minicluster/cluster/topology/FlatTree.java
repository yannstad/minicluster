package com.minicluster.cluster.topology;

import com.google.common.collect.Lists;

import java.util.Collection;

/**
 * The topology of the cluster is defined by a flat tree.
 *
 * All connected nodes form a flat tree: 1 root node, and all others are child nodes.
 * Here is an example of a tree with 4 nodes:
 *
 *        1
 *      / | \
 *     2  3 4
 *
 * Here, node 1 is the father of node 2, 3 and 4, or equivalently node 2, 3, 4 are children of node 1.
 */
class FlatTree implements Topology {

    private static final Integer ROOT_ID = 1;
    private final Integer nodeId;
    private final Integer size;

    public FlatTree(Integer nodeId, Integer size) {
        this.nodeId = nodeId;
        this.size = size;
    }

    @Override
    public Integer getFatherId() {
        if (nodeId != ROOT_ID) {
            return ROOT_ID;
        }
        return null;
    }

    @Override
    public Collection<Integer> getChildIds() {
        if (nodeId != ROOT_ID) {
            return null;
        }
        Collection<Integer> childrenIds = Lists.newArrayList();
        for (int id = 2; id < this.size; ++id) {
            childrenIds.add(id);
        }
        return childrenIds;
    }
}
