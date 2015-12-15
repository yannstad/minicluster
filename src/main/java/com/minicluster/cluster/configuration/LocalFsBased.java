package com.minicluster.cluster.configuration;

import com.google.common.io.Files;
import com.minicluster.cluster.node.Endpoint;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * Use a shared folder in local file system to store the global configuration
 */
public class LocalFsBased extends Configuration {

    private final static Logger log = Logger.getLogger(LocalFsBased.class);

    private final static String EXTENSION_DONE = ".done";
    private final static String EXTENSION_TMP = ".tmp";

    private final String directory;

    public LocalFsBased(String directory) {
        this.directory = directory;
    }

    @Override
    public void setEndpoint(Integer nodeId, Endpoint endpoint) throws IOException {
        log.debug(String.format("set endpoint in conf for nodeId = %s", nodeId.toString()));

        File tmp = new File(directory + File.separator + nodeId + EXTENSION_TMP);
        File done = new File(directory + File.separator + nodeId + EXTENSION_DONE);

        //check if file already exits
        if (done.exists()){
            throw new RuntimeException("a file already exists at this location = " + done.getAbsolutePath() + ". Please clean before re-running");
        }

        //write to tmp file
        OutputStream stream = new FileOutputStream(tmp);
        endpoint.serializeToStream(stream);
        stream.close();

        //rename tmp to done
        Files.move(tmp, done);
    }

    @Override
    public Endpoint getEndpoint(Integer nodeId) throws IOException, ClassNotFoundException {
        if (nodeId==null) {
            log.debug(String.format("nodeId is null => endpoint is null"));
            return null;
        }
        log.debug(String.format("get endpoint for nodeId = %s", nodeId.toString()));

        //file that contains the serialized endpoint
        File done = new File(this.directory + File.separator + nodeId + EXTENSION_DONE);

        //wait until file is created by other nodes
        while (!done.exists()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }

        //read file content
        InputStream stream = new FileInputStream(done);
        Endpoint endpoint = Endpoint.deserializeFromStream(stream);
        stream.close();

        log.debug(String.format("found endpoint  = %s", endpoint.toString()));

        return endpoint;
    }
}
