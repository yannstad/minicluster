package com.yann.distributedenv.allreduce;

import java.io.IOException;
import java.io.Serializable;

import org.apache.log4j.Logger;

import com.yann.distributedenv.comm.Message;
import com.yann.distributedenv.comm.Proxy;
import com.yann.distributedenv.comm.Reducable;
import com.yann.distributedenv.comm.SocketProxy;

public class AllReducableOperator implements AllReducable {

	private static Logger log = Logger.getLogger(AllReducableOperator.class);

	private final Proxy _proxy;

	public AllReducableOperator(Proxy proxy) {
		if (proxy == null) {
			throw new IllegalArgumentException("proxy");
		}
		_proxy = proxy;
	}

	public AllReducableOperator(Integer nodeId, String registerServerHostname, Integer registerServerPort) throws IOException {
		this(new SocketProxy(nodeId, registerServerHostname, registerServerPort));
		this.connect();
	}

	/**
	 * connect to the distributed environment
	 */
	private void connect() throws IOException {
		log.info("connecting to the distributed environment");
		if (_proxy != null) {
			_proxy.connect();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			this.disconnect();
		} catch (Throwable t) {
			throw t;
		} finally {
			super.finalize();
		}
	}

	/**
	 * disconnect from the distributed environment
	 */
	private void disconnect() throws IOException {
		log.info("disconnecting from the distributed environment");
		if (_proxy != null) {
			_proxy.disconnect();
		}
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T extends Reducable & Serializable> T allReduce(T data) throws ClassNotFoundException, IOException {
		Message msg = new Message(data);
		_proxy.reduce(msg);
		_proxy.broadcast(msg);
		return (T) msg.getContent();
	}
}
