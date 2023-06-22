package duplex;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private String content;
    private String sender;
    private Date timeSent;

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
        this.timeSent = new Date();
    }

    public Date getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(Date timeSent) {
        this.timeSent = timeSent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return this.getSender() + ": " + this.getContent() + '\n';
    }
}
