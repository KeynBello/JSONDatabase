package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {
        String address = "127.0.0.1";
        int port = 23456;
        ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address));
        System.out.println("Server started!");
        Socket socket = server.accept();
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());

        String clientMessage = input.readUTF();
        System.out.println("Received: " + clientMessage);

        String response = "A record # " + parseMessage(clientMessage) + " was sent!";
        output.writeUTF(response);
        System.out.println("Sent: " + response);
    }

    private static int parseMessage(String clientMessage) {
        int number = -1;

        try {
            String[] splitedMessage = clientMessage.split(" ");
            if (splitedMessage[5].matches("\\d+")) {
                number = Integer.parseInt(splitedMessage[5]);
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
        return number;
    }
}
