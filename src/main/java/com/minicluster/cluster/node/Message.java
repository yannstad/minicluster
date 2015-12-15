package com.minicluster.cluster.node;

import java.io.*;

public class Message<T extends Serializable> {

    /**
     * any serializable data
     */
    private T content;

    /**
     * ctor
     *
     * @param content
     */
    public Message(T content) {
        this.content = content;
    }

    /**
     * getter
     *
     * @return
     */
    public T getContent() {
        return content;
    }

    /**
     * serialize 'content' in stream
     *
     * @param stream
     * @throws IOException
     */
    public void serializeContent(OutputStream stream) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(stream);
        out.writeObject(content);
    }

    /**
     * deserialize stream in 'content'
     *
     * @param stream
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void deSerializeContent(InputStream stream) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(stream);
        content = (T) in.readObject();
    }
}
