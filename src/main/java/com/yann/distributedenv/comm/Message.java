package com.yann.distributedenv.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class Message<T extends Serializable> {

	private T _content;

	public Message(T content) {
		_content = content;
	}

	public Message() {
	}

	public T getContent() {
		return _content;
	}

	public void serializeContent(OutputStream stream) throws IOException {

		ObjectOutputStream out = new ObjectOutputStream(stream);
		out.writeObject(_content);
	}

	@SuppressWarnings("unchecked")
	public void deSerializeContent(InputStream stream) throws IOException,
	ClassNotFoundException {

		ObjectInputStream in = new ObjectInputStream(stream);
		_content = (T) in.readObject();
	}
}
