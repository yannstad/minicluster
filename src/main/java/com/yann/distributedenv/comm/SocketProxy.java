package com.yann.distributedenv.comm;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.yann.distributedenv.context.Environment;
import com.yann.distributedenv.context.Node;
import com.yann.distributedenv.register.RegisterClient;

public class SocketProxy implements Proxy {

	private static Logger LOG = Logger.getLogger(SocketProxy.class);

	private ServerSocket _listener;
	private Socket _father;
	private Collection<Socket> _children;
	private boolean _isListening;
	private boolean _isRegistered;
	private String _registerServerHostname;
	private Integer _registerServerPort;
	private boolean _isConnected;
	private Integer _nodeId;

	public SocketProxy(Integer nodeId, String registerServerHostname,
			Integer registerServerPort) throws IOException {
		_listener = null;
		_father = null;
		_children = new ArrayList<Socket>();
		_isListening = false;
		_isRegistered = false;
		_registerServerHostname = registerServerHostname;
		_registerServerPort = registerServerPort;
		_isConnected = false;
		_nodeId = nodeId;
		this.start();
	}

	/**
	 * create a server socket, bound to any free port.
	 */
	private void start() throws IOException {
		this.start(0);
	}

	/**
	 * create a server socket, bound to the given port. A port of 0 creates a
	 * socket on any free port.
	 */
	private void start(Integer port) throws IOException {
		LOG.info("starting proxy");
		_listener = new ServerSocket(port);
		_isListening = true;
	}

	/**
	 * get the port on which the server socket is listening
	 */
	private Integer getPort() throws IOException {
		if (!_isListening) {
			start();
		}
		return _listener.getLocalPort();
	}

	/**
	 * get the hostname on which the server socket is listening
	 */
	private String getHostname() throws IOException {
		if (!_isListening) {
			start();
		}
		return InetAddress.getLocalHost().getHostName();
	}

	@Override
	public void connect() throws IOException {

		if (_isConnected) {
			LOG.info("already connected");
			return;
		}

		if (!_isListening) {
			LOG.error("cannot connect: proxy is not listening");
			return;
		}

		if (_isRegistered) {
			LOG.info("already connected");
			return;
		}

		Environment env = register(this._nodeId, this.getPort(), this.getHostname());
		_isRegistered = true;

		if (env.getFather() != null) {
			LOG.info("waiting for incoming connection");
			_father = _listener.accept();//TODO check identity of connected socket
		}

		if (env.getChildren() != null) {
			for (Node node : env.getChildren()) {
				_children.add(new Socket(node.getHostname(), node.getPort()));
			}
		}
	}

	private Environment register(Integer localNodeId, Integer port,
			String hostname) throws IOException {

		Node me = new Node(localNodeId, port, hostname);
		Environment env = new Environment(me, null, null);

		// connect to register server to obtain addresses of parents and
		// children
		RegisterClient client = new RegisterClient(env,
				_registerServerHostname, _registerServerPort);
		client.run();

		return client.getEnv();
	}

	/**
	 * stop the server socket
	 */
	@Override
	public void disconnect() throws IOException {
		LOG.info("stopping proxy");
		if (_father != null) {
			_father.close();
		}
		if (_children != null) {
			for (Socket socket : _children) {
				socket.close();
			}
		}
		if (_listener != null) {
			_listener.close();
		}
		_isConnected = false;
		_isRegistered = false;
		_isListening = false;
	}

	@Override
	public void reduce(Message<? extends Reducable> msg) throws ClassNotFoundException, IOException {
		reduceChildren(msg);
		sendToFather(msg);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void reduceChildren(Message<? extends Reducable> msg) throws ClassNotFoundException, IOException {

		for (Socket child : _children) {
			if (child != null) {
				LOG.info("receiving from child");

				Message<? extends Reducable> m = new Message(msg.getContent());
				m.deSerializeContent(child.getInputStream());
				msg.getContent().reduce(m.getContent());
			}
		}
	}

	private void sendToFather(Message<?> msg) throws IOException {

		if (_father != null) {
			LOG.info("sending to father");
			msg.serializeContent(_father.getOutputStream());
		}
	}

	@Override
	public void broadcast(Message<?> msg) throws ClassNotFoundException, IOException {
		receiveFromFather(msg);
		sendToChildren(msg);
	}

	private void receiveFromFather(Message<?> msg) throws ClassNotFoundException, IOException {

		if (_father != null) {
			LOG.info("receiving from father");
			msg.deSerializeContent(_father.getInputStream());
		}
	}

	private void sendToChildren(Message<?> msg) throws IOException {

		for (Socket child : _children) {
			if (child != null) {
				LOG.info("sending to child");
				msg.serializeContent(child.getOutputStream());
			}
		}
	}
}
