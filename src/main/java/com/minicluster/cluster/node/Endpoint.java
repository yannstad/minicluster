package com.minicluster.cluster.node;

import java.io.*;

/**
 * An endpoint is a socket address. Basically it is the pair hostname:port
 */
public class Endpoint implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String separator = ":";

    private Integer port;
    private String hostname;

    public Endpoint(Integer port, String hostname) {
        this.port = port;
        this.hostname = hostname;
    }

    public Integer getPort() {
        return port;
    }

    public String getHostname() {
        return hostname;
    }

    @Override
    public String toString() {
        return String.format("%s%s%d", hostname, separator, port);
    }

    static public Endpoint fromString(String serializedEndpoint) {
        if (serializedEndpoint == null) {
            return null;
        }
        //we expect the format "hostname:port"
        String[] split = serializedEndpoint.split(separator);
        Integer port = Integer.parseInt(split[0]);
        String hostname = split[1];
        return new Endpoint(port, hostname);
    }

    public void serializeToStream(OutputStream outputStream) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(this);
        objectOutputStream.close();
    }

    public static Endpoint deserializeFromStream(InputStream inputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Endpoint endpoint = (Endpoint) objectInputStream.readObject();
        objectInputStream.close();
        return endpoint;
    }
}
