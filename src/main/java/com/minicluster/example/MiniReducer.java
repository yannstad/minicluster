package com.minicluster.example;

import com.minicluster.cluster.service.MiniCluster;
import com.minicluster.example.reducible.Summable;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * This reducer illustrates how to start the minicluster in a Hadoop reducer.
 * You can use it from a map-reduce job.
*/
public abstract class MiniReducer<KEY_IN, VALUE_IN, KEY_OUT, VALUE_OUT> extends Reducer<KEY_IN, VALUE_IN, KEY_OUT, VALUE_OUT> {

    private final static Logger log = Logger.getLogger(MiniReducer.class);
    protected MiniCluster minicluster;

    @Override
    protected void setup(Reducer<KEY_IN, VALUE_IN, KEY_OUT, VALUE_OUT>.Context context) throws IOException, InterruptedException {
        Integer reducerId = context.getTaskAttemptID().getTaskID().getId();
        Integer clusterSize = context.getNumReduceTasks();
        FileSystem fs = FileSystem.get(context.getConfiguration());
        final Path outputFolder = FileOutputFormat.getOutputPath(context);

        //start the minicluster
        minicluster = MiniCluster.Builder.create(reducerId+1, clusterSize, outputFolder.toString(), fs);
        boolean success = minicluster.start();
        if (!success) {
            System.err.println("cannot start the minicluster. Check errors in logs");
        } else {
            System.err.println("minicluster started. Check with a quick allreduce that compute sum of reducerIds");
            Summable s = new Summable(reducerId);
            System.err.print("before s = " + s.getValue());
            s = minicluster.allReduce(s);
            System.err.print(" -- after s = " + s.getValue() + "\n");
        }
    }

    @Override
    protected abstract void reduce(KEY_IN key, Iterable<VALUE_IN> values, Reducer<KEY_IN, VALUE_IN, KEY_OUT, VALUE_OUT>.Context context) throws IOException, InterruptedException;

    @Override
    protected void cleanup(Reducer<KEY_IN, VALUE_IN, KEY_OUT, VALUE_OUT>.Context context) throws IOException, InterruptedException {

        //stop the minicluster
        boolean success = minicluster.stop();
        if (!success) {
            log.error("cannot stop the minicluster. Check errors in logs");
        }
    }
}
