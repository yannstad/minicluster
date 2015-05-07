package com.yann.distributedenv.register;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yann.distributedenv.comm.Message;
import com.yann.distributedenv.context.Environment;
import com.yann.distributedenv.context.Node;
import com.yann.distributedenv.log.Log4j;

public class Server implements Runnable {

	private static Logger LOG = Logger.getLogger(Server.class);

	public static void main(String[] args) throws NumberFormatException, IOException {
		Log4j.initialize();

		Integer nbClients = Integer.parseInt(args[0]);
		Server pop = null;
		try {
			pop = new Server(nbClients);
			pop.run();
		} catch (Exception ex) {
			LOG.error("failed", ex);
		} finally {
			pop.close();
		}
	}

	public static final Integer PORT = 37460;

	private ServerSocket _listener;
	private Collection<Socket> _parents;
	private List<Environment> _environmentPerParent;
	private Map<Integer, Node> _nodePerId;
	private Integer _nbClients;

	private void startListener() throws IOException {
		_listener = new ServerSocket(PORT);
		LOG.info("start socket server on " + String.format("[%s:%d]", InetAddress.getLocalHost().getHostName(), _listener.getLocalPort()));
	}

	public Server(Integer nbClients) throws IOException {
		_nbClients = nbClients;
	}

	@Override
	public void run() {
		try {
			setupServer();
			receiveFromParents();
			sendToParents();
		} catch (Exception ex) {
			LOG.error("server crashed", ex);
		}
	}

	private void setupServer() throws IOException {
		startListener();
		_parents = new ArrayList<Socket>();
		while (_nbClients > 0) {
			_parents.add(_listener.accept());
			// TODO check that same container tries to connect several times
			_nbClients--;
		}
		_environmentPerParent = new ArrayList<Environment>();
		_nodePerId = new HashMap<Integer, Node>();
		LOG.info("server is up");
	}

	public void close() throws IOException {
		LOG.info("close server");
		if (_parents != null) {
			for (Socket socket : _parents) {
				socket.close();
			}
		}
		if (_listener != null) {
			_listener.close();
		}
	}

	private void receiveFromParents() throws IOException, ClassNotFoundException {
		for (Socket parent : _parents) {
			LOG.info("receiving from client...");

			Message<Environment> msg = new Message<Environment>();
			msg.deSerializeContent(parent.getInputStream());
			_environmentPerParent.add(msg.getContent());
			Node me = msg.getContent().getMe();
			_nodePerId.put(me.getId(), me);
		}
	}

	private Environment buildEnvAroundMe(Node me) {
		Node father = null;
		Collection<Node> children = new ArrayList<Node>();

		Integer nodeId = me.getId() / 2;
		if (nodeId > 0) {
			father = new Node(nodeId, _nodePerId.get(nodeId).getPort(), _nodePerId.get(nodeId).getHostname());
		}
		nodeId = me.getId() * 2;
		if (nodeId <= _parents.size()) {
			Node child = new Node(nodeId, _nodePerId.get(nodeId).getPort(), _nodePerId.get(nodeId).getHostname());
			children.add(child);
		}
		nodeId = me.getId() * 2 + 1;
		if (nodeId <= _parents.size()) {
			Node child = new Node(nodeId, _nodePerId.get(nodeId).getPort(), _nodePerId.get(nodeId).getHostname());
			children.add(child);
		}
		return new Environment(me, father, children);
	}

	private void sendToParents() throws IOException {
		int index = 0;
		for (Socket parent : _parents) {
			Environment env = buildEnvAroundMe(_environmentPerParent.get(index++).getMe());
			Message<Environment> msg = new Message<Environment>(env);

			LOG.info("sending to client...");
			msg.serializeContent(parent.getOutputStream());
		}
	}

	public Integer getPort() {
		return _listener.getLocalPort();
	}

	public static String getHostname() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostName();
	}
}
