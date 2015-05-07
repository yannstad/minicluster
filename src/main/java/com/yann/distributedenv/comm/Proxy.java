package com.yann.distributedenv.comm;

import java.io.IOException;

public interface Proxy {

	/**
	 * connect to distributed environment
	 */
	void connect() throws IOException;

	/**
	 * disconnect from distributed environment
	 */
	void disconnect() throws IOException;

	/**
	 * reduce message contents sent by children, and sent result to father
	 */
	void reduce(Message<? extends Reducable> msg) throws ClassNotFoundException, IOException;

	/**
	 * receive message content from father, and propagate it to children
	 */
	void broadcast(Message<?> msg) throws ClassNotFoundException, IOException;
}
