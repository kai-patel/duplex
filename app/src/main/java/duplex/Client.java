package duplex;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Client {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ObservableList<String> messages;

    public Client(String host, int port) {
        this.messages = FXCollections.observableArrayList();
        try {
            this.socket = new Socket(host, port);
            this.in = new ObjectInputStream(this.socket.getInputStream());
            this.out = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Could not create Client");
        }
    }

    public void close() {
        try {
            if (this.socket != null) {
                this.socket.close();
            }

            if (this.in != null) {
                this.in.close();
            }

            if (this.out != null) {
                this.out.close();
            }
        } catch (IOException e) {
            System.err.println("Could not close Client");
            e.printStackTrace();
        }
    }

    public void sendMessage(String s) {
        try {
            this.out.writeObject(new Message("test", s));
            this.out.flush();
        } catch (IOException e) {
            System.err.println("Client could not writeObject");
        }
    }

    public void getMessages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    try {
                        Message message = (Message) in.readObject();
                        System.out.println(message.toString());
                        messages.add(message.toString());
                    } catch (IOException e) {
                        System.err.println("Could not run Client");
                        close();
                        break;
                    } catch (ClassNotFoundException e) {
                        System.err.println("Could not cast read Object to Message in Client");
                        close();
                        break;
                    }
                }

                return;
            }
        }).start();
    }
}
