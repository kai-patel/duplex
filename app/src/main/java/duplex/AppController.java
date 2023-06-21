package duplex;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class AppController {
    public TextArea textArea;
    public Button sendButton;

    public void sendMessage() {
        System.out.println(textArea.getText());
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
}
