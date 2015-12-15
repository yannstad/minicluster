package com.minicluster.cluster.configuration;

import com.google.common.collect.Lists;
import com.minicluster.cluster.node.Endpoint;
import org.apache.hadoop.fs.FileSystem;

import java.io.IOException;
import java.util.Collection;

/**
 * The configuration is global, like a central service reachable from all nodes
 */
public abstract class Configuration {

    /**
     * Add an endpoint in the global configuration
     */
    public abstract void setEndpoint(Integer nodeId, Endpoint endpoint) throws IOException;

    /**
     * Get an endpoint in the global configuration
     */
    public abstract Endpoint getEndpoint(Integer nodeId) throws IOException, ClassNotFoundException;

    /**
     * Get several endpoints in the global configuration
     */
    public Collection<Endpoint> getEndpoints(Collection<Integer> nodeIds) throws IOException, ClassNotFoundException {
        Collection<Endpoint> endpoints = Lists.newArrayList();

        for (Integer nodeId : nodeIds) {
            Endpoint endpoint = this.getEndpoint(nodeId);
            if (endpoint != null) {
                endpoints.add(endpoint);
            }
        }

        return endpoints;
    }


    /**
     * Factory
     */
    static public class Builder {
        static public Configuration create(String directory, FileSystem fs) {
            return new HdfsBased(directory, fs);
        }

        static public Configuration create(String directory) {
            return new LocalFsBased(directory);

        }
    }
}
