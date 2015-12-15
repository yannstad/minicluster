package com.minicluster.cluster.service;

import com.google.common.base.Preconditions;
import com.minicluster.cluster.configuration.Configuration;
import com.minicluster.cluster.node.Endpoint;
import com.minicluster.cluster.node.Message;
import com.minicluster.cluster.node.Node;
import com.minicluster.cluster.topology.Topology;
import com.minicluster.misc.ChuckNorris;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 */
class MiniClusterImpl implements MiniCluster {

    private final static Logger log = Logger.getLogger(MiniClusterImpl.class);

    private final Node node;
    private final Topology topology;
    private final Configuration configuration;

    public MiniClusterImpl(Node node, Topology topology, Configuration configuration) {
        Preconditions.checkNotNull(node, "node is null");
        Preconditions.checkNotNull(topology, "topology is null");
        Preconditions.checkNotNull(configuration, "configuration is null");

        this.node = node;
        this.topology = topology;
        this.configuration = configuration;
    }

    /**
     * start the local node and connect it to the distributed environment
     *
     * @return true if success, false if error happened
     */
    @Override
    public boolean start() {
        log.info("starting the minicluster");
        try {
            //starting local node
            node.start();

            //sending local endpoint to global configuration
            configuration.setEndpoint(node.getId(), node.getEndpoint());

            //getting father endpoint from global configuration
            Endpoint father = configuration.getEndpoint(topology.getFatherId());

            //getting child endpoints from global configuration
            Collection<Endpoint> children = configuration.getEndpoints(topology.getChildIds());

            //connecting node to father and child nodes
            node.connect(father, children);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        log.info("minicluster is ready");
        log.info("By the way, did you know that " + new ChuckNorris().getRandomQuote());
        return true;
    }

    /**
     * disconnect the local node from the distributed environment, then stop it.
     *
     * @return true if success, false if error happened
     */
    @Override
    public boolean stop() {
        log.info("stopping the minicluster");
        try {
            node.disconnect();
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
            return false;
        }
        log.info("minicluster is stopped");
        return true;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T extends Reducible & Serializable> T allReduce(T data) {
        Message msg = new Message(data);
        try {
            node.reduce(msg);
            node.broadcast(msg);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException();
        }
        return (T) msg.getContent();
    }
}
