package bgu.spl.net.impl.BidiMessaging.Messages;

import bgu.spl.net.impl.BidiMessaging.Message;

public class PMmessage extends Message {
    String userName;
    String content;

    public PMmessage(String userName, String content) {
        super((short) 6);
        this.userName = userName;
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public String getContent() {
        return content;
    }
}
