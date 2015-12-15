package com.minicluster.example;

import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Demo:
 * 1. Start N services in separate threads
 * 2. compute 1+2+...+N using allreduce where reduce = addition
 */
public class Demo {

    private final static Logger log = Logger.getLogger(Demo.class);

    /**
     * start the HelloWorld application in several threads, one application per threads
     */
    public static void main(String[] args) throws Exception {

        if (args == null || args.length != 1) {
            log.error("missing argument: cluster size");
            return;
        }

        final Integer clusterSize = Integer.parseInt(args[0]);
        final String sharedDirectory = System.getProperty("user.home") + "/.miniclusterconfig/";

        new Demo().run(clusterSize, sharedDirectory);
    }

    private void run(final Integer clusterSize, final String sharedDirectory) {
        ExecutorService threadPool = Executors.newFixedThreadPool(clusterSize);
        try {
            for (int i = 1; i <= clusterSize; ++i) {

                final int id = i;
                threadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        new HelloWorld().run(id, clusterSize, sharedDirectory);
                    }
                });
            }
        } finally {
            threadPool.shutdown();
        }
    }
}
