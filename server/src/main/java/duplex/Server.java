package duplex;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public ServerSocket serverSocket;

    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            this.start();
        } catch (IOException e) {
            System.err.println("Could not create server");
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        while (!this.serverSocket.isClosed()) {
            Socket clientSocket = this.serverSocket.accept();
            ClientHandler handler = new ClientHandler(clientSocket);

            Thread thread = new Thread(handler);
            thread.start();
        }
    }

    public void close() {
        try {
            if (this.serverSocket != null) {
                this.serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Could not close server socket");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server(585);
        server.close();
    }
}
