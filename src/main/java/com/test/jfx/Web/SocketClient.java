package com.test.jfx.Web;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketClient {
    public final static String HOST_NAME = "localhost";
    public final static int SERVER_PORT = 8080;
    private static final int BUFFER_SIZE = 1024;
    private InetSocketAddress serverAddress;
    private SocketChannel clientChannel;
    private StringProperty serverAnswerProperty;

    public SocketClient(String hostName) throws IOException {
        System.out.println("[INFO] Creating connection with server on port: " + SERVER_PORT);
        serverAnswerProperty = new SimpleStringProperty();
        serverAddress = new InetSocketAddress(hostName, SERVER_PORT);
        if (serverAddress.isUnresolved())
            throw new IOException("Unresolved host address");
        clientChannel = SocketChannel.open(serverAddress);
    }

    public SocketClient() throws IOException {
        this(HOST_NAME);
    }

    public void sendMessage(String message) throws IOException {
        System.out.println("[INFO] Starting sending...");
        byte[] byteMessage = message.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        byteBuffer.put(byteMessage);
        byteBuffer.flip();
        clientChannel.write(byteBuffer);
        byteBuffer.clear();

        getMessage();

        System.out.println("[INFO] Getting answer from server");
        clientChannel.close();
        System.out.println("[INFO] End sending");
    }

    public void getMessage() {
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            clientChannel.read(byteBuffer);
            System.out.println(clientChannel.isRegistered());
            byteBuffer.flip();
            byte[] bytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(bytes, 0, bytes.length);
            String message = new String(bytes);
            serverAnswerProperty.setValue(message);
        } catch (IOException e) {
            System.out.println("[ERROR] Error when reading message");
        }
    }

    public StringProperty getServerAnswerProperty() {
        if (serverAnswerProperty == null)
            return new SimpleStringProperty("");
        else
            return serverAnswerProperty;
    }
}
