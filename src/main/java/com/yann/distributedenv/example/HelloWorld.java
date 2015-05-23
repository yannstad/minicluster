package com.yann.distributedenv.example;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.mortbay.log.Log;

import com.yann.distributedenv.allreduce.AllReducableOperator;
import com.yann.distributedenv.log.Log4j;
import com.yann.distributedenv.register.RegisterServer;

/**
 * Example class: start a registration server in a thread. Start n clients in
 * separate threads, that will register. Then the system is up to do allreduce
 * computations
 */
public class HelloWorld
{
	private static Logger LOG = Logger.getLogger(HelloWorld.class);

	public static void main(String[] args) throws IOException,
	ClassNotFoundException, InterruptedException
	{
		Log4j.initialize();

		//parse args
		Integer nbClients = 2;
		if (args.length == 1) {
			nbClients = Integer.parseInt(args[0]);
		}

		ExecutorService threadPool = Executors.newFixedThreadPool(1 + nbClients);
		try {

			//====================== code to start server ==========================
			final RegisterServer registerServer = new RegisterServer(nbClients);
			threadPool.submit(registerServer);
			//======================================================================

			// sleep 1 second
			Thread.sleep(1000);

			// start all clients
			for (int i = 1; i <= nbClients; ++i) {
				startClient(threadPool, i, registerServer.getHostname(), registerServer.getPort());
			}

		} finally {
			threadPool.shutdown();
		}
	}

	private static void startClient(ExecutorService threadPool, final int id,
			final String hostname, final Integer port) {

		// start client id
		threadPool.submit(new Runnable() {
			@Override
			public void run() {

				//======================= code to run client ===========================
				AllReducableOperator operator = null;
				try {
					operator = new AllReducableOperator(id, hostname, port);

					Sum sum = new Sum(id);
					Log.info("before all reduce, sum = " + sum.getSum());
					sum = operator.allReduce(sum);
					Log.info("after all reduce, sum = " + sum.getSum());

				} catch (Exception ex) {
					LOG.error("opertaor crashed", ex);
				} finally {
					try {
						operator.close();
					} catch (IOException ex) {
						LOG.error("fail to close allreducable operator", ex);
					}
				}
				//======================================================================
			}
		});
	}
}
