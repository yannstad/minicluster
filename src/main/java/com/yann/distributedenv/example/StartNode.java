package com.yann.distributedenv.example;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.yann.distributedenv.allreduce.Operator;
import com.yann.distributedenv.log.Log4j;

/**
 * Main class takes two arguments: node id and node value
 */
public class StartNode
{
	private static Logger LOG = Logger.getLogger(StartNode.class);

	public static void main(String[] args) throws IOException, ClassNotFoundException
	{
		Log4j.initialize();

		Integer nodeId = Integer.parseInt(args[0]);
		Integer value = Integer.parseInt(args[1]);

		LOG.info("start node");
		LOG.info("node id ..... = " + nodeId);
		LOG.info("node value .. = " + value);

		Operator operator = null;
		try {
			operator = new Operator(nodeId);
			operator.connect();

			Sum sum = new Sum(value);

			LOG.info("before allreduce. Sum = " + sum.getSum());
			sum = operator.allReduce(sum);
			LOG.info("after allreduce. Sum = " + sum.getSum());
		} catch (Exception ex) {
			LOG.error("failed", ex);
		} finally {
			operator.disconnect();
		}

	}
}
