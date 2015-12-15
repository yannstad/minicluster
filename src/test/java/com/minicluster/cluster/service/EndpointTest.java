package com.minicluster.cluster.service;

import com.minicluster.cluster.node.Endpoint;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EndpointTest {

    private final Integer port = 37460;
    private final String hostname = "localhost";

    private Endpoint myEndpoint;

    @Before
    public void setUp() throws Exception {
        myEndpoint = new Endpoint(port, hostname);
    }

    @Test
    public void testGetPort() throws Exception {
        assertEquals(port, myEndpoint.getPort());
    }

    @Test
    public void testGetHostname() throws Exception {
        assertEquals(hostname, myEndpoint.getHostname());
    }
}