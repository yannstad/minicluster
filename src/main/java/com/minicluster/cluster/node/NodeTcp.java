package com.minicluster.cluster.node;

import com.google.common.collect.Lists;
import com.minicluster.cluster.service.Reducible;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;

class NodeTcp implements Node {

    private static Logger log = Logger.getLogger(NodeTcp.class);

    private ServerSocket listener;
    private Socket father;
    private Collection<Socket> children;
    private Integer nodeId;

    public NodeTcp(Integer NodeId) {
        listener = null;
        father = null;
        children = Lists.newArrayList();
        this.nodeId = NodeId;
    }

    /**
     * create a server socket, bound to any free port.
     */
    @Override
    public void start() throws IOException {
        this.start(0);
    }

    /**
     * create a server socket, bound to the given port. A port of 0 creates a
     * socket on any free port.
     */
    private void start(Integer port) throws IOException {
        listener = new ServerSocket(port);
        log.debug("starting the local node = " + getEndpoint());
    }

    /**
     * get the port on which the server socket is listening
     */
    private Integer getPort() {
        return listener.getLocalPort();
    }

    /**
     * get the hostname on which the server socket is listening
     */
    private String getHostname() throws IOException {
        return InetAddress.getLocalHost().getHostName();
    }

    /**
     * connect to other nodes
     *
     * @throws IOException
     */
    @Override
    public void connect(Endpoint father, Collection<Endpoint> children) throws IOException, ClassNotFoundException {
        log.debug("connecting node to the distributed environment");

        if (father != null) {
            log.debug("waiting for incoming connection");
            this.father = listener.accept();
        }

        if (children != null) {
            for (Endpoint childInfo : children) {
                this.children.add(new Socket(childInfo.getHostname(), childInfo.getPort()));
            }
        }
    }

    /**
     * close all sockets
     */
    @Override
    public void disconnect() throws IOException {
        log.debug("disconnecting node from the distributed environment");
        if (father != null) {
            father.close();
        }
        if (children != null) {
            for (Socket socket : children) {
                socket.close();
            }
        }
        log.debug("stopping the local node");
        if (listener != null) {
            listener.close();
        }
    }

    @Override
    public void reduce(Message<? extends Reducible> msg) throws ClassNotFoundException, IOException {
        reduceChildren(msg);
        sendToFather(msg);
    }

    @Override
    public void broadcast(Message<?> msg) throws ClassNotFoundException, IOException {
        receiveFromFather(msg);
        sendToChildren(msg);
    }

    @Override
    public Endpoint getEndpoint() throws IOException {
        return new Endpoint(this.getPort(), this.getHostname());
    }

    @Override
    public Integer getId() {
        return nodeId;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void reduceChildren(Message<? extends Reducible> msg) throws ClassNotFoundException, IOException {

        for (Socket child : children) {
            if (child != null) {
                log.debug("receiving from child node");

                Message<? extends Reducible> m = new Message(msg.getContent());
                m.deSerializeContent(child.getInputStream());
                msg.getContent().reduce(m.getContent());
            }
        }
    }

    private void sendToFather(Message<?> msg) throws IOException {

        if (father != null) {
            log.debug("sending to father node");
            msg.serializeContent(father.getOutputStream());
        }
    }

    private void receiveFromFather(Message<?> msg) throws ClassNotFoundException, IOException {

        if (father != null) {
            log.debug("receiving from father node");
            msg.deSerializeContent(father.getInputStream());
        }
    }

    private void sendToChildren(Message<?> msg) throws IOException {

        for (Socket child : children) {
            if (child != null) {
                log.debug("sending to child node");
                msg.serializeContent(child.getOutputStream());
            }
        }
    }
}
