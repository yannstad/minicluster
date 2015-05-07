package com.yann.distributedenv.context;

import java.io.Serializable;
import java.util.Collection;

public class Environment implements Serializable {

	private static final long serialVersionUID = 1L;

	protected Node _me;
	protected Node _father;
	protected Collection<Node> _children;

	public Environment(Node me, Node father, Collection<Node> children) {
		_me = me;
		_father = father;
		_children = children;
	}

	public Node getMe() {
		return _me;
	}

	public Node getFather() {
		return _father;
	}

	public Collection<Node> getChildren() {
		return _children;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("local node ..... = ").append(_me.toString());
		if (_father != null) {
			str.append("\n").append("father node .... = ").append(_father.toString());
		}
		if (_children != null) {
			for (Node child : _children) {
				str.append("\n").append("child node ..... = ").append(child.toString());
			}
		}
		return str.toString();
	}
}
