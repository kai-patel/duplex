package duplex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ObservableList<String> messages;

    public Client(String host, int port) {
        this.messages = FXCollections.observableArrayList();
        try {
            this.socket = new Socket(host, port);
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new PrintWriter(this.socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Could not create Client");
            e.printStackTrace();
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
        this.out.write(s + '\n');
        this.out.flush();
    }

    public void getMessages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    try {
                        String message = in.readLine();
                        System.out.println(message);
                        messages.add(message);
                    } catch (IOException e) {
                        System.err.println("Could not run Client");
                        e.printStackTrace();
                        close();
                        break;
                    }
                }
            }
        }).start();
    }
}
