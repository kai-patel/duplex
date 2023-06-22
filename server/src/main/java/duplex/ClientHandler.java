package duplex;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clients = new ArrayList<>();
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            this.out = new ObjectOutputStream(this.socket.getOutputStream());
            this.in = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException e) {
            System.err.println("Could not create ClientHandler");
            e.printStackTrace();
        }

        clients.add(this);
        broadcastMessage(new Message("Server", "A user has entered the chat"));
    }

    private void broadcastMessage(Message message) {
        for (ClientHandler client : clients) {
            if (this != client) {
                try {
                    client.out.writeObject(message);
                    client.out.flush();
                } catch (IOException e) {
                    System.err.println("Could not write message to client");
                }
            }
        }
    }

    public void close() {
        clients.remove(this);

        if (this.socket != null) {
            try {
                this.socket.close();
            } catch (IOException e) {
                System.err.println("Could not close ClientHandler socket");
            }
        }

        if (this.in != null) {
            try {
                this.in.close();
            }

            catch (IOException e) {
                System.err.println("Could not close ClientHandler reader");
            }
        }

        if (this.out != null) {
            try {
                this.out.close();
            } catch (IOException e) {
                System.err.println("Could not close ClientHandler writer");
            }
        }

    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                Message message = (Message) in.readObject();
                broadcastMessage(message);
            } catch (IOException e) {
                System.err.println("Could not readObject in ClientHandler");
                e.printStackTrace();
                close();
                break;
            } catch (ClassNotFoundException e) {
                System.err.println("Could not cast read Object in ClientHandler");
                e.printStackTrace();
                close();
                break;
            }
        }

        return;
    }

}
