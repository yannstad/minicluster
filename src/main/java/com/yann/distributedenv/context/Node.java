package com.yann.distributedenv.context;

import java.io.Serializable;

public class Node implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer _id;
	private Integer _port;
	private String _hostname;

	public Node(Integer id, Integer port, String hostname) {
		_id = id;
		_port = port;
		_hostname = hostname;
	}

	public Integer getId() {
		return _id;
	}

	public Integer getPort() {
		return _port;
	}

	public String getHostname() {
		return _hostname;
	}

	@Override
	public String toString() {
		return String.format("[node:%d -- hostname:%s -- port:%d]", _id, _hostname, _port);
	}
}
