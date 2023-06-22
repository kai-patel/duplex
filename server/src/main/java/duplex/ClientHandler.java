package duplex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clients = new ArrayList<>();
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("Could not create ClientHandler");
            e.printStackTrace();
        }

        clients.add(this);
        broadcastMessage("A user has entered the chat");
    }

    private void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            if (this != client) {
                client.out.write(message + '\n');
                client.out.flush();
            }
        }
    }

    public void close() {
        clients.remove(this);

        try {
            if (this.socket != null) {
                this.socket.close();
            }
        } catch (IOException e) {
            System.err.println("Could not close ClientHandler socket");
        }

        try {
            if (this.in != null) {
                this.in.close();
            }

        } catch (IOException e) {
            System.err.println("Could not close ClientHandler reader");
        }

        if (this.out != null) {
            this.out.close();
        }
    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                String message = in.readLine();
                broadcastMessage(message);
            } catch (IOException e) {
                System.err.println("Could not run ClientHandler");
                e.printStackTrace();
                close();
                break;
            }
        }

        return;
    }

}
