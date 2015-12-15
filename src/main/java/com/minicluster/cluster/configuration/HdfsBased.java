package com.minicluster.cluster.configuration;

import com.minicluster.cluster.node.Endpoint;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Use a shared folder in Hdfs to store the global configuration
 */
class HdfsBased extends Configuration {

    private final static Logger log = Logger.getLogger(HdfsBased.class);

    private final static String EXTENSION_DONE = ".done";
    private final static String EXTENSION_TMP = ".tmp";

    private final String directory;
    private final FileSystem fs;

    public HdfsBased(String directory, FileSystem fs) {
        this.directory = directory;
        this.fs = fs;
    }

    @Override
    public void setEndpoint(Integer nodeId, Endpoint endpoint) throws IOException {
        log.debug(String.format("set endpoint in conf for nodeId = %s", nodeId.toString()));

        Path tmp = new Path(this.directory, nodeId.toString() + EXTENSION_TMP);
        Path done = new Path(this.directory, nodeId.toString() + EXTENSION_DONE);

        //check if file already exits
        if (this.fs.exists(done)){
            throw new RuntimeException("a file already exists at this location = " + done.toString() + ". Please clean before re-running");
        }

        //write to tmp file
        OutputStream stream = this.fs.create(tmp);
        endpoint.serializeToStream(stream);
        stream.close();

        //replace file extension EXTENSION_TMP with EXTENSION_DONE
        this.fs.rename(tmp, done);
    }

    @Override
    public Endpoint getEndpoint(Integer nodeId) throws IOException, ClassNotFoundException {
        if (nodeId==null) {
            log.debug(String.format("nodeId is null => endpoint is null"));
            return null;
        }
        log.debug(String.format("get endpoint for nodeId = %s", nodeId.toString()));

        //file that contains the serialized endpoint
        Path done = new Path(this.directory, nodeId + EXTENSION_DONE);

        //wait until file is created by other nodes
        while (!fs.exists(done)) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }

        //read file content
        InputStream inputStream = this.fs.open(done);
        Endpoint endpoint = Endpoint.deserializeFromStream(inputStream);
        inputStream.close();

        log.debug(String.format("found endpoint  = %s", endpoint.toString()));

        return endpoint;
    }
}
