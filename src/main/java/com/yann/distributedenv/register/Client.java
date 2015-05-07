package com.yann.distributedenv.register;

import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.yann.distributedenv.comm.Message;
import com.yann.distributedenv.context.Environment;

public class Client implements Runnable {

	private static Logger LOG = Logger.getLogger(Client.class);

	private Socket _child;
	private Environment _env;

	public Environment getEnv() {
		return _env;
	}

	public Client(Environment env) throws IOException {
		_env = env;
		_child = new Socket(Server.getHostname(), Server.PORT);
		LOG.info("registration client is up");
	}

	@Override
	public void run() {
		try {
			sendToServer();
			receiveFromServer();
		} catch (Exception ex) {
			LOG.error("client crashed", ex);
		}
	}

	public void close() throws IOException {
		if (_child != null) {
			_child.close();
		}
	}

	private void sendToServer() throws IOException {
		LOG.info("sending environment to registration server");

		Message<Environment> msg = new Message<Environment>(_env);
		msg.serializeContent(_child.getOutputStream());
	}

	private void receiveFromServer() throws ClassNotFoundException, IOException {
		LOG.info("receiving environment from registration server");

		Message<Environment> msg = new Message<Environment>();
		msg.deSerializeContent(_child.getInputStream());
		_env = msg.getContent();
	}
}
