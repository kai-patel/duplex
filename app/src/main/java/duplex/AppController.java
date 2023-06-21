package duplex;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class AppController {
    public ObservableList<String> messages;
    public ObservableList<String> users;

    public TextArea textArea;
    public Button sendButton;

    public ListView<String> messageHistory;
    public ListView<String> userList;

    public void sendMessage() {
        String newMessage = textArea.getText();

        if (newMessage.isBlank()) {
            return;
        }

        System.out.println(newMessage);
        messages.add(newMessage);
        messageHistory.scrollTo(messages.size());
        textArea.clear();
    }

    @FXML
    public void sendClicked(Event e) {
        sendMessage();
    }

    @FXML
    public void textAreaShortcut(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER && e.isControlDown()) {
            sendMessage();
        }
    }

    @FXML
    public void initialize() {
        messages = FXCollections.observableArrayList("this", "is", "a", "test");
        users = FXCollections.observableArrayList("John", "Adam", "Steve");
        messageHistory.setItems(messages);
        userList.setItems(users);
    }
}
