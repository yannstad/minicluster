package com.minicluster.cluster.topology;


import java.util.Collection;

/**
 * The topology define the relations between nodes
 */
public interface Topology {

    /**
     * Given a node id, return the father id or null if it does not exits
     */
    Integer getFatherId();

    /**
     * Given a node id, return child ids (each of them may be null)
     */
    Collection<Integer> getChildIds();


}
