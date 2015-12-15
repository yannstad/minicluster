package com.minicluster.example;


import com.minicluster.cluster.service.MiniCluster;
import com.minicluster.cluster.service.MiniClusters;
import com.minicluster.example.reducible.Multipliable;
import com.minicluster.example.reducible.Summable;
import org.apache.log4j.Logger;

/**
 * Start and run the cluster service
 */
public class HelloWorld {

    private final static Logger log = Logger.getLogger(HelloWorld.class);

    public static void main(String[] args) {

        if (args == null || args.length != 2) {
            log.error("missing arguments: node id and cluster size");
            return;
        }

        final Integer id = Integer.parseInt(args[0]);
        final Integer size = Integer.parseInt(args[1]);
        final String sharedDirectory = System.getProperty("user.home") + "/.miniclusterconfig/";

        new HelloWorld().run(id, size, sharedDirectory);
    }

    void run(Integer id, Integer clusterSize, String sharedDirectory) {

        MiniCluster cluster = MiniClusters.newLocalCluster(id, clusterSize, sharedDirectory);
        boolean success = cluster.start();
        if (!success) {
            log.error("cannot start the mini cluster. Check errors in logs");
            return;
        }

        log.info("set reduce = addition");
        Summable sum = new Summable(id);
        log.info("before allreduce: sum = " + sum.getValue());
        sum = cluster.allReduce(sum);
        log.info("after allreduce: sum = " + sum.getValue());

        log.info("set reduce = multiplication");
        Multipliable product = new Multipliable(id.doubleValue());
        log.info("before allreduce: product = " + product.getValue());
        product = cluster.allReduce(product);
        log.info("after allreduce: product = " + product.getValue());

        success = cluster.stop();
        if (!success) {
            log.error("cannot stop the mini cluster. Check errors in logs");
        }

    }
}
