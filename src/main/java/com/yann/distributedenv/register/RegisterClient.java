package com.yann.distributedenv.register;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.yann.distributedenv.comm.Message;
import com.yann.distributedenv.context.Environment;

public class RegisterClient implements Runnable {

	private static Logger LOG = Logger.getLogger(RegisterClient.class);

	private Socket _child;
	private String _registerServerHostname;
	private Integer _registerServerPort;
	private Environment _env;

	public Environment getEnv() {
		return _env;
	}

	public RegisterClient(Environment env, String registerServerHostname,
			Integer registerServerPort) throws IOException {
		_env = env;
		_registerServerHostname = registerServerHostname;
		_registerServerPort = registerServerPort;
		_child = null;
	}

	@Override
	public void run() {
		try {
			createChildSocket();
			sendEnvToServer();
			receiveEnvFromServer();
		} catch (Exception ex) {
			LOG.error("client crashed", ex);
		} finally {
			try {
				_child.close();
			} catch (IOException ex) {
				LOG.error("socket close failed", ex);
			}
		}
	}

	private void createChildSocket() throws UnknownHostException, IOException {
		_child = new Socket(_registerServerHostname, _registerServerPort);
	}

	private void sendEnvToServer() throws IOException {
		LOG.info("sending environment to registration server");

		Message<Environment> msg = new Message<Environment>(_env);
		msg.serializeContent(_child.getOutputStream());
	}

	private void receiveEnvFromServer() throws ClassNotFoundException, IOException {
		LOG.info("receiving environment from registration server");

		Message<Environment> msg = new Message<Environment>();
		msg.deSerializeContent(_child.getInputStream());
		_env = msg.getContent();
	}
}
