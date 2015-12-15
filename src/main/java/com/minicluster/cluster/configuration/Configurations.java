package com.minicluster.cluster.configuration;

import org.apache.hadoop.fs.FileSystem;

/**
 * Factory
 */
public class Configurations {
    static public Configuration newSharedHdfsDirectory(String directory, FileSystem fs) {
        return new HdfsBased(directory, fs);
    }

    static public Configuration newSharedLocalDirectory(String directory) {
        return new LocalFsBased(directory);

    }
}
